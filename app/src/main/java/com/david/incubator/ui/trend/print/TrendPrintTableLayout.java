package com.david.incubator.ui.trend.print;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.SensorModelRepository;
import com.david.core.database.DaoControl;
import com.david.core.database.entity.BaseEntity;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.TimeUtil;
import com.david.core.util.rely.RangeUtil;
import com.david.databinding.LayoutTrendPrintTableBinding;
import com.david.incubator.ui.trend.TrendCategoryEnum;
import com.david.incubator.ui.trend.TrendControlModel;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TrendPrintTableLayout extends BindingBasicLayout<LayoutTrendPrintTableBinding> {

    private static final int ROW_COUNT = 13;
    private static final int COLUMN_COUNT = 4;

    @Inject
    TrendControlModel trendControlModel;
    @Inject
    DaoControl daoControl;
    @Inject
    SensorModelRepository sensorModelRepository;

    private final TextView[][] textViews;
    private long currentFirstTime;
    private int offset;
    private int itemId;

    public TrendPrintTableLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        textViews = new TextView[ROW_COUNT][COLUMN_COUNT];

        binding.gridLayout.set(ROW_COUNT, COLUMN_COUNT);

        for (int row = 0; row < ROW_COUNT; row++) {
            for (int column = 0; column < COLUMN_COUNT; column++) {
                textViews[row][column] = new TextView(getContext());
                textViews[row][column].setGravity(Gravity.CENTER);

                textViews[row][column].setTextColor(ContextUtil.getColor(R.color.text_blue));
                textViews[row][column].setBackgroundResource(R.drawable.table_outline);

                int height = 34;
                if (row == 0) {
                    height = 56;
                }

                if (column == 0) {
                    textViews[row][column].setTextSize(18);
                    binding.gridLayout.addFill(textViews[row][column], row, column, 214, height);
                } else {
                    textViews[row][column].setTextSize(20);
                    binding.gridLayout.addFill(textViews[row][column], row, column, 168, height);
                }
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_trend_print_table;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);

        textViews[0][0].setText(ContextUtil.getString(R.string.time));

        SensorModelEnum sensorModelEnum0 = trendControlModel.getItemSensorModel(0);
        textViews[0][1].setText(ContextUtil.getString(sensorModelEnum0.getDisplayNameId()));
        SensorModelEnum sensorModelEnum1 = trendControlModel.getItemSensorModel(1);
        textViews[0][2].setText(ContextUtil.getString(sensorModelEnum1.getDisplayNameId()));
        SensorModelEnum sensorModelEnum2 = trendControlModel.getItemSensorModel(2);
        if (!Objects.equals(sensorModelEnum2, SensorModelEnum.Co2)) {
            textViews[0][3].setText(ContextUtil.getString(sensorModelEnum2.getDisplayNameId()));
        } else {
            textViews[0][3].setText(null);
        }

        long lastTime = trendControlModel.getLastTime();
        int interval = trendControlModel.getIntervalValue();
        currentFirstTime = lastTime - interval * trendControlModel.getSamplePoint();

        offset = 0;
        refresh();
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void refresh() {
        itemId = 1;
        Observable.create((ObservableOnSubscribe<BaseEntity>) emitter -> {
            BaseEntity[] baseEntities;
            if (trendControlModel.trendCategory.getValue() == TrendCategoryEnum.Incubator.ordinal()) {
                baseEntities = daoControl.getIncubatorDaoOperation().get(0, currentFirstTime, offset * (ROW_COUNT - 1), ROW_COUNT);
            } else if (trendControlModel.trendCategory.getValue() == TrendCategoryEnum.Ecg.ordinal()) {
                baseEntities = daoControl.getEcgDaoControl().get(0, currentFirstTime, offset * (ROW_COUNT - 1), ROW_COUNT);
            } else if (trendControlModel.trendCategory.getValue() == TrendCategoryEnum.Spo2.ordinal()) {
                baseEntities = daoControl.getSpo2DaoOperation().get(0, currentFirstTime, offset * (ROW_COUNT - 1), ROW_COUNT);
            } else {
                baseEntities = daoControl.getCo2DaoControl().get(0, currentFirstTime, offset * (ROW_COUNT - 1), ROW_COUNT);
            }

            for (BaseEntity baseEntity : baseEntities) {
                emitter.onNext(baseEntity);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull BaseEntity baseEntity) {
                        if (itemId < ROW_COUNT) {
                            SensorModelEnum sensorModelEnum0 = trendControlModel.getItemSensorModel(0);
                            SensorModel sensorModel0 = sensorModelRepository.getSensorModel(sensorModelEnum0);
                            SensorModelEnum sensorModelEnum1 = trendControlModel.getItemSensorModel(1);
                            SensorModel sensorModel1 = sensorModelRepository.getSensorModel(sensorModelEnum1);
                            SensorModelEnum sensorModelEnum2 = trendControlModel.getItemSensorModel(2);
                            SensorModel sensorModel2 = sensorModelRepository.getSensorModel(sensorModelEnum2);

                            textViews[itemId][0].setText(TimeUtil.getTimeFromSecond(baseEntity.timeStamp, TimeUtil.FullTime));

                            int data0 = RangeUtil.getSensorData(sensorModelEnum0, baseEntity);
                            textViews[itemId][1].setText(sensorModel0.formatValueUnit(data0));

                            int data1 = RangeUtil.getSensorData(sensorModelEnum1, baseEntity);
                            textViews[itemId][2].setText(sensorModel1.formatValueUnit(data1));

                            if (!Objects.equals(sensorModelEnum2, SensorModelEnum.Co2)) {
                                int data2 = RangeUtil.getSensorData(sensorModelEnum2, baseEntity);
                                textViews[itemId][3].setText(sensorModel2.formatValueUnit(data2));
                            } else {
                                textViews[itemId][3].setText(null);
                            }
                        }
                        itemId++;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        if (itemId < ROW_COUNT) {
                            for (int index = itemId; index < ROW_COUNT; index++) {
                                textViews[index][0].setText("");
                                textViews[index][1].setText("");
                                textViews[index][2].setText("");
                                textViews[index][3].setText("");
                            }
                        }
                    }
                });
    }
}