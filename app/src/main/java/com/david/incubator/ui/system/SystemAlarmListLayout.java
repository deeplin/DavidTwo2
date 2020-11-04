package com.david.incubator.ui.system;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.alarm.AlarmRepository;
import com.david.core.database.DaoControl;
import com.david.core.database.entity.AlarmEntity;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.TimeUtil;
import com.david.databinding.LayoutSystemAlarmListBinding;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SystemAlarmListLayout extends BindingBasicLayout<LayoutSystemAlarmListBinding> {

    private static final int ROW_COUNT = 13;
    private static final int COLUMN_COUNT = 2;

    @Inject
    DaoControl daoControl;
    @Inject
    AlarmRepository alarmRepository;

    private final TextView[][] textViews;
    private long currentFirstTime;
    private int offset;
    private int itemId;

    public SystemAlarmListLayout(Context context) {
        super(context);
        binding.titleView.set(R.string.alarm_record, LayoutPageEnum.SYSTEM_HOME, true);
        ContextUtil.getComponent().inject(this);

        textViews = new TextView[ROW_COUNT][COLUMN_COUNT];
        binding.gridLayout.set(ROW_COUNT, COLUMN_COUNT);

        for (int row = 0; row < ROW_COUNT; row++) {
            for (int column = 0; column < COLUMN_COUNT; column++) {
                textViews[row][column] = new TextView(getContext());
                textViews[row][column].setTextColor(ContextUtil.getColor(R.color.text_blue));
                textViews[row][column].setBackgroundResource(R.drawable.table_outline);
                int height = 34;
                if (row == 0) {
                    height = 40;
                }

                if (column == 0) {
                    textViews[row][column].setGravity(Gravity.CENTER);
                    textViews[row][column].setTextSize(18);
                    binding.gridLayout.addFill(textViews[row][column], row, column, 230, height);
                } else {
                    if (row == 0) {
                        textViews[row][column].setGravity(Gravity.CENTER);
                    } else {
                        textViews[row][column].setGravity(Gravity.START);
                        textViews[row][column].setPadding(8, 0, 0, 0);
                    }
                    textViews[row][column].setTextSize(18);
                    binding.gridLayout.addFill(textViews[row][column], row, column, 360, height);
                }
            }
        }

        binding.moveLeft.setOnClickListener(v1 -> {
            offset--;
            refresh();
        });

        binding.moveRight.setOnClickListener(v -> {
            offset++;
            refresh();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_system_alarm_list;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);

        textViews[0][0].setText(ContextUtil.getString(R.string.time));
        textViews[0][1].setText(ContextUtil.getString(R.string.alarm_record));

        currentFirstTime = TimeUtil.getCurrentTimeInSecond();

        offset = 0;
        refresh();
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void refresh() {
        itemId = 1;
        Observable.create((ObservableOnSubscribe<AlarmEntity>) emitter -> {

            AlarmEntity[] alarmEntities = daoControl.getAlarmDaoOperation().get(0, currentFirstTime, offset * (ROW_COUNT - 1), ROW_COUNT);

            for (AlarmEntity alarmEntity : alarmEntities) {
                emitter.onNext(alarmEntity);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AlarmEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull AlarmEntity alarmEntity) {
                        if (itemId < ROW_COUNT) {
                            textViews[itemId][0].setText(TimeUtil.getTimeFromSecond(alarmEntity.timeStamp, TimeUtil.FullTime));
                            textViews[itemId][1].setText(alarmRepository.getAlarmStringById(alarmEntity.alarmId));
                        }
                        itemId++;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        binding.moveLeft.setEnabled(offset > 0);
                        binding.moveRight.setEnabled(itemId > ROW_COUNT);
                        if (itemId < ROW_COUNT) {
                            for (int index = itemId; index < ROW_COUNT; index++) {
                                textViews[index][0].setText("");
                                textViews[index][1].setText("");
                            }
                        }
                    }
                });
    }
}