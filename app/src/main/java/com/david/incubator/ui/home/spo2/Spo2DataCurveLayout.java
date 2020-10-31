package com.david.incubator.ui.home.spo2;

import android.content.Context;
import android.util.AttributeSet;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.LazyLiveData;
import com.david.databinding.LayoutSpo2DataCurveBinding;

public class Spo2DataCurveLayout extends BindingBasicLayout<LayoutSpo2DataCurveBinding> {

    public final LazyLiveData<Integer> firstColor = new LazyLiveData<>();
    public final LazyLiveData<String> firstUpperLimit = new LazyLiveData<>();
    public final LazyLiveData<String> firstLowerLimit = new LazyLiveData<>();
    public final LazyLiveData<Integer> secondColor = new LazyLiveData<>();
    public final LazyLiveData<String> secondUpperLimit = new LazyLiveData<>();
    public final LazyLiveData<String> secondLowerLimit = new LazyLiveData<>();

    public Spo2DataCurveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        binding.setViewModel(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_spo2_data_curve;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.spo2DataWaveView.attach();
    }

    @Override
    public void detach() {
        super.detach();
        binding.spo2DataWaveView.detach();
    }

    public void setSensor0(SensorModelEnum sensorModelEnum) {
        firstColor.set(sensorModelEnum.getUniqueColor());
        firstLowerLimit.set(String.valueOf(sensorModelEnum.getCoordinatorLower()));
        firstUpperLimit.set(String.valueOf(sensorModelEnum.getCoordinatorUpper()));
        binding.spo2DataWaveView.set(0, sensorModelEnum);
    }

    public void setSensor1(SensorModelEnum sensorModelEnum) {
        secondColor.set(sensorModelEnum.getUniqueColor());
        secondLowerLimit.set(String.valueOf(sensorModelEnum.getCoordinatorLower()));
        secondUpperLimit.set(String.valueOf(sensorModelEnum.getCoordinatorUpper()));
        binding.spo2DataWaveView.set(1, sensorModelEnum);
    }

    public void drawAll(int curveId, int[] dataSeries0) {
        binding.spo2DataWaveView.drawAll(curveId, dataSeries0);
    }

    public void repaint() {
        binding.spo2DataWaveView.postInvalidate();
    }
}