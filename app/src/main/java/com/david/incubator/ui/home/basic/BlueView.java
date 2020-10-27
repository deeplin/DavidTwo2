package com.david.incubator.ui.home.basic;

import android.content.Context;
import android.util.AttributeSet;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.ViewTitleIconBlueBinding;

import java.util.Locale;

import javax.inject.Inject;

public class BlueView extends BindingBasicLayout<ViewTitleIconBlueBinding> {

    @Inject
    IncubatorModel incubatorModel;

    private final Observer<Integer> cTimeObserver;

    public BlueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        cTimeObserver = integer -> {
            String heatString = String.format(Locale.US, "%02d:%02d", (integer / 60) % 60, integer % 60);
            binding.integerPart.setText(heatString);
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_title_icon_blue;
    }

    public void set(SensorModel sensorModel) {
        binding.setViewModel(sensorModel);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        incubatorModel.cTime.observeForever(cTimeObserver);
    }

    @Override
    public void detach() {
        super.detach();
        incubatorModel.cTime.removeObserver(cTimeObserver);
    }
}
