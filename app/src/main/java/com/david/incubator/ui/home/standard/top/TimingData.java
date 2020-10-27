package com.david.incubator.ui.home.standard.top;

import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.util.ILifeCycle;
import com.david.core.util.IntervalUtil;
import com.david.core.util.LazyLiveData;
import com.david.core.util.rely.SoundUtil;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/31 22:26
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class TimingData implements ILifeCycle {

    @Inject
    SoundUtil soundUtil;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    IntervalUtil intervalUtil;

    public final LazyLiveData<String> titleString = new LazyLiveData<>();
    public final LazyLiveData<String> textString = new LazyLiveData<>();
    public final LazyLiveData<Boolean> showClock = new LazyLiveData<>();

    private boolean isApgar;
    private int count;

    private final Observer<SystemEnum> systemModeObserver;

    @Inject
    TimingData() {
        isApgar = true;
        count = 0;

        systemModeObserver = systemEnum -> {
            if (incubatorModel.isCabin()) {
                intervalUtil.removeMillisecond500Consumer(TimingLayout.class);
                init();
            }
        };
    }

    @Override
    public void attach() {
        incubatorModel.systemMode.observeForever(systemModeObserver);
    }

    @Override
    public void detach() {
        incubatorModel.systemMode.removeObserver(systemModeObserver);
    }

    public void setApgar(boolean isApgar) {
        this.isApgar = isApgar;
    }

    public boolean isApgar() {
        return isApgar;
    }

    public int getCount() {
        return count;
    }

    public void initCount() {
        count = 0;
    }

    public void increaseCount() {
        count++;
        count %= 7200;
    }

    public void init() {
        textString.set("00:00");
    }

    public void accept(TimingView timingView) {
        if (count % 2 == 1) {
            showClock.post(false);
            return;
        }

        int second = count / 2;

        textString.post(String.format(Locale.US, "%02d:%02d", second / 60, second % 60));

        if (isApgar) {
            if ((second >= 50 && second < 60) || ((second >= 5 * 60 - 10) && (second < 5 * 60))
                    || ((second >= 10 * 60 - 10) && second < 10 * 60)) {
                soundUtil.playApgar(R.raw.apgar);
                showClock.post(true);
            }
        } else {
            if ((second >= 60 && second < 64) || ((second >= 5 * 60) && (second < 5 * 60 + 4))
                    || ((second >= 10 * 60) && second < 10 * 60 + 4)) {
                showClock.post(true);
            }
            if (second % 30 == 0) {
                if ((second == 30) || (second == 5 * 60) || (second == 10 * 60)) {
                    soundUtil.playApgar(R.raw.cpr2);
                } else if (second < 600) {
                    soundUtil.playApgar(R.raw.cpr1);
                }
            }
            if (second >= 30 && second < 600) {
                int remainder = second % 30;
                if (remainder < 14) {
                    remainder %= 7;
                    timingView.set(remainder + 1);
                    timingView.postInvalidate();
                } else if (remainder == 14) {
                    timingView.set(0);
                    timingView.postInvalidate();
                }
            }
        }
    }
}