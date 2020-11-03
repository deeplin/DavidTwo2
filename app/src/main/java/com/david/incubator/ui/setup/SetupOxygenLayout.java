package com.david.incubator.ui.setup;

import android.content.Context;

import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.SensorModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.ui.model.IntegerPopupModel;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class SetupOxygenLayout extends BaseLayout {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IncubatorCommandSender incubatorCommandSender;

    public SetupOxygenLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.initLargeFont();

        addKeyButtonWithLiveData(0, 0).setBigFont();
        setKeyButtonEnum(0, KeyButtonEnum.SETUP_OXYGEN, null, null);

        SensorModel oxygenModel = sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen);
        setOriginValue(0, oxygenModel.objective);

        setCallback(0, this::setObjective);
    }

    @Override
    public void attach() {
        super.attach();
        optionPopupView.setBigFont(true);
        numberPopupView.setBigFont(true);
        IntegerPopupModel integerPopupModel = getIntegerPopupModel(0);
        SensorModel oxygenModel = sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen);
        integerPopupModel.setMin(oxygenModel.lowerLimit.getValue());
        integerPopupModel.setMax(oxygenModel.upperLimit.getValue());
    }

    @Override
    public void detach() {
        optionPopupView.setBigFont(false);
        numberPopupView.setBigFont(false);
        super.detach();
    }

    private void setObjective(Integer value) {
        incubatorCommandSender.setCtrlSet(SystemEnum.Cabin.name(), SensorModelEnum.Oxygen.getCommandName(), value, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                incubatorCommandSender.getCtrlGet();
            }
        });
    }
}