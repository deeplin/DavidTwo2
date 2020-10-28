package com.david.incubator.ui.home.tempcurve;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.database.DaoControl;
import com.david.core.database.entity.IncubatorEntity;
import com.david.core.enumeration.CtrlEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.ui.view.ChartView;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.FormatUtil;
import com.david.core.util.IntervalUtil;
import com.david.core.util.TimeUtil;
import com.david.core.util.rely.RangeUtil;
import com.david.core.util.rely.ShapeUtil;
import com.david.databinding.LayoutTempCurveBinding;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TempCurveLayout extends BindingBasicLayout<LayoutTempCurveBinding> implements Consumer<Long> {

    private static final int INTERVAL = 60;

    @Inject
    IntervalUtil intervalUtil;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    DaoControl daoControl;
    @Inject
    SystemModel systemModel;
    @Inject
    ModuleHardware moduleHardware;

    private final int[][] destArray;

    private final Observer<CtrlEnum> ctrlEnumObserver;

    public TempCurveLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        destArray = new int[5][Constant.DOT_PER_CHART];

        binding.tempChartView.setPadding(70, 40, 60, 30);
        binding.humidityChartView.setPadding(70, 40, 60, 30);

        binding.tempChartView.initDataSet(3, SensorModelEnum.Skin1, FormatUtil::formatBasicValue);
        binding.tempChartView.setSensor(0, SensorModelEnum.Skin1);
        binding.tempChartView.setSensor(1, SensorModelEnum.Skin2);
        binding.tempChartView.setSensor(2, SensorModelEnum.Air);
        binding.tempChartView.setUnit(SensorModelEnum.Skin1.getUnit());

        binding.humidityChartView.initDataSet(2, SensorModelEnum.Humidity, FormatUtil::formatBasicValue);
        binding.humidityChartView.setSensor(0, SensorModelEnum.Humidity);
        binding.humidityChartView.setSensor(1, SensorModelEnum.Oxygen);
        binding.humidityChartView.setUnit(SensorModelEnum.Oxygen.getUnit());

        setBackground(binding.skin1IconView, SensorModelEnum.Skin1);
        setBackground(binding.skin2IconView, SensorModelEnum.Skin2);
        setBackground(binding.airIconView, SensorModelEnum.Air);

        setBackground(binding.humidityIconView, SensorModelEnum.Humidity);
        setBackground(binding.oxygenIconView, SensorModelEnum.Oxygen);

        binding.skin1IconView.setOnClickListener(v -> selectSkin1());
        binding.skin1TextView.setOnClickListener(v -> selectSkin1());

        binding.skin2IconView.setOnClickListener(v -> selectSkin2());
        binding.skin2TextView.setOnClickListener(v -> selectSkin2());

        binding.airIconView.setOnClickListener(v -> selectAir());
        binding.airTextView.setOnClickListener(v -> selectAir());

        binding.humidityIconView.setOnClickListener(v -> selectHumidity());
        binding.humidityTextView.setOnClickListener(v -> selectHumidity());

        binding.oxygenIconView.setOnClickListener(v -> selectOxygen());
        binding.oxygenTextView.setOnClickListener(v -> selectOxygen());

        ctrlEnumObserver = ctrlEnum -> {
            //todo deeeplin
            incubatorModel.systemMode.set(SystemEnum.Cabin);
            if (incubatorModel.isCabin()) {
                binding.airIconView.setSelected(true);
                binding.airIconView.setVisibility(View.VISIBLE);
                binding.airTextView.setVisibility(View.VISIBLE);
            } else {
                binding.airIconView.setSelected(false);
                binding.airIconView.setVisibility(View.INVISIBLE);
                binding.airTextView.setVisibility(View.INVISIBLE);
                binding.tempChartView.setDataSet(2, null);
            }

            if (incubatorModel.isCabin() && (moduleHardware.isActive(ModuleEnum.Hum) || moduleHardware.isActive(ModuleEnum.Oxygen))) {
                binding.humidityChartView.setXAxis(TimeUtil.getCurrentTimeInSecond(), INTERVAL);
                binding.humidityChartView.setVisibility(View.VISIBLE);
                binding.humidityChartView.setReady(true);
                if (moduleHardware.isActive(ModuleEnum.Hum)) {
                    binding.humidityIconView.setSelected(true);
                    binding.humidityIconView.setVisibility(View.VISIBLE);
                    binding.humidityTextView.setVisibility(View.VISIBLE);
                } else {
                    binding.humidityIconView.setSelected(false);
                    binding.humidityIconView.setVisibility(View.INVISIBLE);
                    binding.humidityTextView.setVisibility(View.INVISIBLE);
                    binding.humidityChartView.setDataSet(0, null);
                }
                if (moduleHardware.isActive(ModuleEnum.Oxygen)) {
                    binding.oxygenIconView.setSelected(true);
                    binding.oxygenIconView.setVisibility(View.VISIBLE);
                    binding.oxygenTextView.setVisibility(View.VISIBLE);
                } else {
                    binding.oxygenIconView.setSelected(false);
                    binding.oxygenIconView.setVisibility(View.INVISIBLE);
                    binding.oxygenTextView.setVisibility(View.INVISIBLE);
                    binding.humidityChartView.setDataSet(1, null);
                }
            } else {
                binding.humidityIconView.setSelected(false);
                binding.humidityIconView.setVisibility(View.INVISIBLE);
                binding.humidityTextView.setVisibility(View.INVISIBLE);
                binding.humidityChartView.setDataSet(0, null);

                binding.oxygenIconView.setSelected(false);
                binding.oxygenIconView.setVisibility(View.INVISIBLE);
                binding.oxygenTextView.setVisibility(View.INVISIBLE);
                binding.humidityChartView.setDataSet(1, null);

                binding.humidityChartView.setVisibility(View.GONE);
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_temp_curve;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.monitorLayout.attach(lifeCycleOwner);
        binding.incubatorLayout.attach(lifeCycleOwner);

        binding.tempChartView.setXAxis(TimeUtil.getCurrentTimeInSecond(), INTERVAL);
        binding.tempChartView.setReady(true);

        binding.humidityChartView.setXAxis(TimeUtil.getCurrentTimeInSecond(), INTERVAL);

        binding.skin1IconView.setSelected(true);
        binding.skin2IconView.setSelected(true);
        binding.airIconView.setSelected(true);

        binding.humidityIconView.setSelected(true);
        binding.oxygenIconView.setSelected(true);

        incubatorModel.ctrlMode.observeForever(ctrlEnumObserver);

        intervalUtil.addSecondConsumer(TempCurveLayout.class, this);

        if (systemModel.darkMode.getValue()) {
            binding.backgroundView.setBackgroundResource(R.drawable.background_panel_dark);
        } else {
            binding.backgroundView.setBackgroundResource(R.drawable.background_panel);
        }
    }

    @Override
    public void detach() {
        super.detach();
        intervalUtil.removeSecondConsumer(TempCurveLayout.class);

        incubatorModel.ctrlMode.removeObserver(ctrlEnumObserver);

        binding.tempChartView.setReady(false);
        binding.humidityChartView.setReady(false);

        binding.incubatorLayout.detach();
        binding.monitorLayout.detach();
    }

    private int count = 60;

    @Override
    public void accept(Long aLong) {
        if (count < 60) {
            count++;
            return;
        }
        count = 0;
        refreshSensor(binding.skin1IconView.isSelected(), 0, SensorModelEnum.Skin1,
                destArray[0], binding.tempChartView);
        refreshSensor(binding.skin2IconView.isSelected(), 1, SensorModelEnum.Skin2,
                destArray[1], binding.tempChartView);
        refreshSensor(binding.airIconView.isSelected(), 2, SensorModelEnum.Air,
                destArray[2], binding.tempChartView);
        binding.tempChartView.postInvalidate();

        refreshSensor(binding.humidityIconView.isSelected(), 0, SensorModelEnum.Humidity,
                destArray[3], binding.humidityChartView);
        refreshSensor(binding.oxygenIconView.isSelected(), 1, SensorModelEnum.Oxygen,
                destArray[4], binding.humidityChartView);
        binding.humidityChartView.postInvalidate();
    }

    private void refreshSensor(boolean selected, int curveId, SensorModelEnum sensorModelEnum, int[] destList, ChartView chartView) {
        long lastTime = TimeUtil.getCurrentTimeInSecond();
        if (selected) {
            addDataSet(curveId, chartView, sensorModelEnum, destList, lastTime);
        } else {
            chartView.setDataSet(curveId, null);
        }
    }

    private void addDataSet(int curveId, ChartView chartView, SensorModelEnum sensorModelEnum, int[] destList, long lastTime) {
        long startTime = lastTime - INTERVAL * Constant.DOT_PER_CHART;
        IncubatorEntity[] srcList = daoControl.getIncubatorDaoOperation().get(startTime, lastTime);
        RangeUtil.fillData(Constant.DOT_PER_CHART, lastTime, srcList, destList, INTERVAL, sensorModelEnum);
        chartView.setDataSet(curveId, destList);
    }

    private void setBackground(ImageView imageView, SensorModelEnum sensorModelEnum) {
        GradientDrawable unselectedBackground = ShapeUtil.getDrawable(0, ContextUtil.getColor(R.color.white),
                2, sensorModelEnum.getUniqueColor());
        GradientDrawable selectedBackground = ShapeUtil.getDrawable(0, sensorModelEnum.getUniqueColor(),
                0, sensorModelEnum.getUniqueColor());
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, selectedBackground);
        stateListDrawable.addState(new int[]{}, unselectedBackground);
        imageView.setBackground(stateListDrawable);
    }

    private void selectSkin1() {
        binding.skin1IconView.setSelected(!binding.skin1IconView.isSelected());
        Observable.just(binding.skin1IconView.isSelected()).observeOn(Schedulers.io())
                .subscribe(selected -> {
                    refreshSensor(selected, 0, SensorModelEnum.Skin1,
                            destArray[0], binding.tempChartView);
                    binding.tempChartView.postInvalidate();
                });
    }

    private void selectSkin2() {
        binding.skin2IconView.setSelected(!binding.skin2IconView.isSelected());
        Observable.just(binding.skin2IconView.isSelected()).observeOn(Schedulers.io())
                .subscribe(selected -> {
                    refreshSensor(selected, 1, SensorModelEnum.Skin2,
                            destArray[1], binding.tempChartView);
                    binding.tempChartView.postInvalidate();
                });
    }

    private void selectAir() {
        binding.airIconView.setSelected(!binding.airIconView.isSelected());
        Observable.just(binding.airIconView.isSelected()).observeOn(Schedulers.io())
                .subscribe(selected -> {
                    refreshSensor(selected, 2, SensorModelEnum.Air,
                            destArray[2], binding.tempChartView);
                    binding.tempChartView.postInvalidate();
                });
    }

    private void selectHumidity() {
        binding.humidityIconView.setSelected(!binding.humidityIconView.isSelected());
        Observable.just(binding.humidityIconView.isSelected()).observeOn(Schedulers.io())
                .subscribe(selected -> {
                    refreshSensor(selected, 0, SensorModelEnum.Humidity,
                            destArray[3], binding.humidityChartView);
                    binding.humidityChartView.postInvalidate();
                });
    }

    private void selectOxygen() {
        binding.oxygenIconView.setSelected(!binding.oxygenIconView.isSelected());
        Observable.just(binding.oxygenIconView.isSelected()).observeOn(Schedulers.io())
                .subscribe(selected -> {
                    refreshSensor(selected, 1, SensorModelEnum.Oxygen,
                            destArray[4], binding.humidityChartView);
                    binding.humidityChartView.postInvalidate();
                });
    }
}