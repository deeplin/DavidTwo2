package com.david.incubator.ui.setup;

import android.content.Context;

import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.SensorModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.ui.model.IntegerPopupModel;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class SetupHumidityLayout extends BaseLayout {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IncubatorCommandSender incubatorCommandSender;

    public SetupHumidityLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.initLargeFont();

        KeyButtonView keyButtonViewArray = addKeyButtonWithLiveData(0, 0);
        keyButtonViewArray.setBigFont();
        setKeyButtonEnum(0, KeyButtonEnum.SETUP_HUMIDITY, null, null);

        SensorModel humidityModel = sensorModelRepository.getSensorModel(SensorModelEnum.Humidity);
        setOriginValue(0, humidityModel.objective);

        setCallback(0, this::setObjective);
    }

    @Override
    public void attach() {
        optionPopupView.setBigFont(true);
        IntegerPopupModel integerPopupModel = getIntegerPopupModel(0);
        SensorModel humidityModel = sensorModelRepository.getSensorModel(SensorModelEnum.Humidity);
        integerPopupModel.setMin(humidityModel.lowerLimit.getValue());
        integerPopupModel.setMax(humidityModel.upperLimit.getValue());
    }

    @Override
    public void detach() {
        optionPopupView.setBigFont(false);
        super.detach();
    }

    private void setObjective(Integer value) {
        incubatorCommandSender.setCtrlSet(SystemEnum.Cabin.name(), SensorModelEnum.Humidity.getCommandName(), value, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                incubatorCommandSender.getCtrlGet();
            }
        });
    }
}