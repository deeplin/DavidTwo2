package com.david.incubator.ui.alarm;

import android.content.Context;

import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class SetupAlarmNibpLayout extends BaseLayout {

    @Inject
    SensorModelRepository sensorModelRepository;

    public SetupAlarmNibpLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init();
    }

    @Override
    public void attach() {
        super.attach();
        SensorModel nibpModel = sensorModelRepository.getSensorModel(SensorModelEnum.Nibp);
        addKeyButton(0, nibpModel, KeyButtonEnum.ALARM_NIBP_UPPER, KeyButtonEnum.ALARM_NIBP_LOWER,
                this::minCondition, this::maxCondition);
        SensorModel nibpDiaModel = sensorModelRepository.getSensorModel(SensorModelEnum.NibpDia);
        addKeyButton(2, nibpDiaModel, KeyButtonEnum.ALARM_NIBP_DIA_UPPER, KeyButtonEnum.ALARM_NIBP_DIA_LOWER,
                this::minCondition, this::maxCondition);
        SensorModel nibpMeanModel = sensorModelRepository.getSensorModel(SensorModelEnum.NibpMean);
        addKeyButton(4, nibpMeanModel, KeyButtonEnum.ALARM_NIBP_MEAN_UPPER, KeyButtonEnum.ALARM_NIBP_MEAN_LOWER,
                this::minCondition, this::maxCondition);
    }

    @Override
    public void detach() {
        super.detach();
        removeAllViews();
    }

    private Integer minCondition(KeyButtonEnum keyButtonEnum) {
        int index = keyButtonEnum.ordinal() - KeyButtonEnum.ALARM_NIBP_UPPER.ordinal();
        if (index % 2 == 0) {
            SensorModel sensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.values()[SensorModelEnum.Nibp.ordinal() + index / 2]);
            return sensorModel.lowerLimit.getValue();
        } else {
            return Integer.MIN_VALUE;
        }
    }

    private Integer maxCondition(KeyButtonEnum keyButtonEnum) {
        int index = keyButtonEnum.ordinal() - KeyButtonEnum.ALARM_NIBP_UPPER.ordinal();
        if (index % 2 == 1) {
            SensorModel sensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.values()[SensorModelEnum.Nibp.ordinal() + index / 2]);
            return sensorModel.upperLimit.getValue();
        } else {
            return Integer.MAX_VALUE;
        }
    }
}