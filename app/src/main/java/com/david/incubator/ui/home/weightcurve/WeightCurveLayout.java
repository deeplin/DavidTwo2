package com.david.incubator.ui.home.weightcurve;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.SensorModelRepository;
import com.david.core.database.DaoControl;
import com.david.core.database.entity.WeightEntity;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.ui.view.ChartView;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.FormatUtil;
import com.david.core.util.IntervalUtil;
import com.david.core.util.LazyLiveData;
import com.david.core.util.TimeUtil;
import com.david.core.util.ViewUtil;
import com.david.core.util.rely.RangeUtil;
import com.david.databinding.LayoutWeightCurveBinding;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeightCurveLayout extends BindingBasicLayout<LayoutWeightCurveBinding> {

    private static final int INTERVAL = 60 * 60 * 24;
    private static final int BABY_WEIGHT_THRESHOLD = 150;
    private static final int PUT_DOWN_VIBRATE = 10;
    private static final int RAISE_VIBRATE = 100;

    @Inject
    DaoControl daoControl;
    @Inject
    IntervalUtil intervalUtil;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    WeightEntity weightEntity;
    @Inject
    SystemModel systemModel;

    public final LazyLiveData<Integer> status = new LazyLiveData<>(0);
    public final LazyLiveData<String> scaleInfo = new LazyLiveData<>();
    public final LazyLiveData<String> lastWeight = new LazyLiveData<>("");

    private final int[] destArray;

    private int stepCount;
    private int weightCount;
    private int previousWeight;
    private int lowestWeight;
    private int targetWeight;
    private String lastDisplayedWeight;

    public WeightCurveLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        binding.setViewModel(this);
        destArray = new int[Constant.DOT_PER_WEIGHT_CHART];

        binding.weightChartView.setPadding(70, 80, 40, 40);

        binding.weightChartView.initDataSet(1, SensorModelEnum.Weight, FormatUtil::formatBasicValue);
        binding.weightChartView.setSensor(0, SensorModelEnum.Skin1);
        binding.weightChartView.setUnit(SensorModelEnum.Weight.getUnit());

        binding.weightButton.setOnClickListener(v -> {
            stepCount = 0;
            weightCount = 0;
            lastDisplayedWeight = lastWeight.getValue();
            status.set(1);
        });

        binding.reWeightButton.setOnClickListener(v -> {
            stepCount = 0;
            previousWeight = 0;
            lowestWeight = Integer.MAX_VALUE;
            lastDisplayedWeight = lastWeight.getValue();
            status.set(3);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_weight_curve;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.monitorListView.attach(lifeCycleOwner);
        binding.incubatorLayout.attach(lifeCycleOwner);

        binding.weightChartView.setWeightXAxis(TimeUtil.getCurrentTimeInSecond(), INTERVAL);
        binding.weightChartView.setReady(true);

        binding.weightButton.setSelected(true);
        binding.reWeightButton.setSelected(true);

        long lastTime = TimeUtil.getCurrentTimeInSecond();
        addDataSet(0, binding.weightChartView, SensorModelEnum.Weight, destArray, lastTime);

        intervalUtil.addSecondConsumer(WeightCurveLayout.class, this::secondInterval);

        status.set(0);

        if (systemModel.darkMode.getValue()) {
            binding.weightChartView.setBackgroundResource(R.drawable.background_panel_dark);
        } else {
            binding.weightChartView.setBackgroundResource(R.drawable.background_panel_white);
        }
    }

    @Override
    public void detach() {
        super.detach();
        intervalUtil.removeSecondConsumer(WeightCurveLayout.class);

        binding.incubatorLayout.detach();
        binding.monitorListView.detach();
    }

    private void addDataSet(int curveId, ChartView chartView, SensorModelEnum sensorModelEnum, int[] destList, long lastTime) {
        Observable.just(this).observeOn(Schedulers.io())
                .subscribe(obj -> {
                    long startTime = lastTime - INTERVAL * Constant.DOT_PER_WEIGHT_CHART;
                    WeightEntity[] srcList = daoControl.getWeightDaoControl().get(startTime, lastTime);
                    RangeUtil.fillData(Constant.DOT_PER_WEIGHT_CHART, lastTime, srcList, destList, INTERVAL, sensorModelEnum);
                    chartView.setDataSet(curveId, destList);
                });
    }

    private void secondInterval(Long aLong) {
        if (status.getValue() > 0) {
            if (stepCount % 2 == 0) {
                setLastWeight("----");
            } else {
                setLastWeight("    ");
            }
            switch (status.getValue()) {
                case (1):
                    scale();
                    break;
                case (2):
                    reScale();
                    break;
                case (3):
                    raiseBaby();
                    break;
            }
        }
    }

    private void setLastWeight(String weight) {
        lastWeight.post(String.format(Locale.US, "%s: %sg",
                ContextUtil.getString(R.string.current_weight), weight));
    }

    private void scale() {
        if (readWeightFromSc()) {
            weightEntity.timeStamp = TimeUtil.getCurrentTimeInSecond();
            weightEntity.weight = targetWeight;
            daoControl.getWeightDaoControl().insert(weightEntity);
            status.post(0);
        } else if (stepCount > 8) {
            ViewUtil.showToast(ContextUtil.getString(R.string.weight_failed));
            setLastWeight(lastDisplayedWeight);
            status.post(0);
        } else {
            stepCount++;
        }
    }

    private boolean readWeightFromSc() {
        SensorModel weightSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Weight);
        int newWeight = weightSensorModel.textNumber.getValue();
        if (weightCount == 0) {
            targetWeight = newWeight;
            weightCount = 1;
        } else if (Math.abs(targetWeight - newWeight) < RAISE_VIBRATE) {
            weightCount++;
            targetWeight = newWeight;
            if (weightCount >= 3) {
                setLastWeight(weightSensorModel.formatValue(targetWeight));
                return true;
            }
        } else {
            targetWeight = newWeight;
            weightCount = 0;
        }
        return false;
    }

    private void raiseBaby() {
        readLowestWeight();
        scaleInfo.post(String.format(Locale.US, "%s%02ds", ContextUtil.getString(R.string.raise_baby), (10 - stepCount)));
        if (stepCount >= 10) {
            stepCount = 0;
            scaleInfo.post("");
            status.post(2);
        } else {
            stepCount++;
        }
    }

    private void readLowestWeight() {
        SensorModel weightSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Weight);
        int newWeight = weightSensorModel.textNumber.getValue();
        if (Math.abs(newWeight - previousWeight) < PUT_DOWN_VIBRATE) {
            if (newWeight < lowestWeight) {
                lowestWeight = newWeight;
            }
        }
        previousWeight = newWeight;
    }

    private void reScale() {
        scaleInfo.post(String.format(Locale.US, "%s%02ds", ContextUtil.getString(R.string.put_down_baby), (10 - stepCount)));
        if (readStableWeight()) {
            status.post(0);
        }

        if (stepCount >= 10) {
            ViewUtil.showToast(ContextUtil.getString(R.string.weight_failed));
            setLastWeight("----");
            scaleInfo.post("");
            status.post(0);
        } else {
            stepCount++;
        }
    }

    private boolean readStableWeight() {
        SensorModel weightSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Weight);
        int newWeight = weightSensorModel.textNumber.getValue();
        if (newWeight >= lowestWeight + BABY_WEIGHT_THRESHOLD) {
            if (weightCount == 0) {
                targetWeight = newWeight;
                weightCount = 1;
            } else if (Math.abs(targetWeight - newWeight) < RAISE_VIBRATE) {
                weightCount++;
                targetWeight = newWeight;
                if (weightCount >= 3) {
                    setLastWeight(weightSensorModel.formatValue(targetWeight - lowestWeight));
                    weightEntity.timeStamp = TimeUtil.getCurrentTimeInSecond();
                    weightEntity.weight = targetWeight - lowestWeight;
                    daoControl.getWeightDaoControl().insert(weightEntity);
                    return true;
                }
            } else {
                targetWeight = newWeight;
                weightCount = 0;
            }
        }
        return false;
    }
}