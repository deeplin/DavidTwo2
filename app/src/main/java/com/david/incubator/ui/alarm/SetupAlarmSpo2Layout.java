package com.david.incubator.ui.alarm;

import android.content.Context;

import com.david.core.control.ConfigRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class SetupAlarmSpo2Layout extends BaseLayout {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    ConfigRepository configRepository;

    public SetupAlarmSpo2Layout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init();
    }

    @Override
    public void attach() {
        super.attach();
        int rowId = 0;
        int enumId = KeyButtonEnum.ALARM_SPO2_UPPER.ordinal();
        for (SensorModelEnum sensorModelEnum : configRepository.getActiveSpo2Enum()) {
            SensorModel sensorModel = sensorModelRepository.getSensorModel(sensorModelEnum);
            addKeyButton(rowId, sensorModel, KeyButtonEnum.values()[enumId + rowId], KeyButtonEnum.values()[enumId + rowId + 1],
                    this::minCondition, this::maxCondition);
            rowId += 2;
        }
    }

    @Override
    public void detach() {
        super.detach();
        removeAllViews();
    }

    private Integer minCondition(KeyButtonEnum keyButtonEnum) {
        int index = keyButtonEnum.ordinal() - KeyButtonEnum.ALARM_SPO2_UPPER.ordinal();
        if (index % 2 == 0) {
            SensorModel sensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.values()[SensorModelEnum.Spo2.ordinal() + index / 2]);
            return sensorModel.lowerLimit.getValue();
        } else {
            return Integer.MIN_VALUE;
        }
    }

    private Integer maxCondition(KeyButtonEnum keyButtonEnum) {
        int index = keyButtonEnum.ordinal() - KeyButtonEnum.ALARM_SPO2_UPPER.ordinal();
        if (index % 2 == 1) {
            SensorModel sensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.values()[SensorModelEnum.Spo2.ordinal() + index / 2]);
            return sensorModel.upperLimit.getValue();
        } else {
            return Integer.MAX_VALUE;
        }
    }
}