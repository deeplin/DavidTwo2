package com.david.core.model;

import com.david.core.enumeration.SensorModelEnum;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.FormatUtil;
import com.david.core.util.LazyLiveData;

public class SensorModel {

    private SensorModelEnum sensorModelEnum;

    public final LazyLiveData<Integer> textNumber = new LazyLiveData<>(Constant.SENSOR_NA_VALUE);
    public final LazyLiveData<String> textIntegerPart = new LazyLiveData<>();
    public final LazyLiveData<String> textDecimalPart = new LazyLiveData<>();

    public final LazyLiveData<String> title = new LazyLiveData<>();
    public final LazyLiveData<String> unit = new LazyLiveData<>();

    public final LazyLiveData<Integer> objective = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> lowerLimit = new LazyLiveData<>();
    public final LazyLiveData<Integer> upperLimit = new LazyLiveData<>();

    public final LazyLiveData<Integer> iconImage = new LazyLiveData<>();
    public final LazyLiveData<Integer> uniqueColor = new LazyLiveData<>();

    public SensorModel() {
    }

    public void setSensorModel(SensorModelEnum sensorModelEnum) {
        this.sensorModelEnum = sensorModelEnum;
        unit.set(sensorModelEnum.getUnit());
        lowerLimit.set(sensorModelEnum.getLowerLimit());
        upperLimit.set(sensorModelEnum.getUpperLimit());
        iconImage.set(sensorModelEnum.getImageIconId());
        if (sensorModelEnum.getUniqueColor() != 0) {
            uniqueColor.set(sensorModelEnum.getUniqueColor());
        }
        textNumber.observeForever(integer -> FormatUtil.formatSensorValue(integer, textIntegerPart,
                textDecimalPart, sensorModelEnum));
    }

    public void setSensorText(SensorModelEnum sensorModelEnum) {
        title.set(ContextUtil.getString(sensorModelEnum.getDisplayNameId()));
    }

    public String formatValue(float value) {
        return FormatUtil.formatValue(sensorModelEnum, value);
    }

    public String formatValueUnit(float value) {
        return FormatUtil.formatValueUnit(sensorModelEnum, value);
    }

    public SensorModelEnum getSensorModelEnum() {
        return sensorModelEnum;
    }
}