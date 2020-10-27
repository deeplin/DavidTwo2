package com.david.core.serial.incubator;

import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.serial.BaseCommand;
import com.david.core.serial.incubator.command.repeated.AlarmListCommand;
import com.david.core.serial.incubator.command.repeated.AnalogCommand;
import com.david.core.serial.incubator.command.repeated.StatusCommand;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.LoggerUtil;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IncubatorCommandControl extends BaseIncubatorCommandControl implements ILifeCycle {

    @Inject
    public AnalogCommand analogCommand;
    @Inject
    public StatusCommand statusCommand;
    @Inject
    AlarmListCommand alarmListCommand;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ConfigRepository configRepository;

    private boolean repeat;

    @Inject
    public IncubatorCommandControl() {
        super();
        ContextUtil.getComponent().inject(this);
        analogCommand.initCallback();
        statusCommand.initCallback();
        repeat = false;
    }

    @Override
    public void accept(Long aLong) {
        super.accept(aLong);
        if (repeat) {
            produce(alarmListCommand);
            produce(statusCommand);
            produce(analogCommand);
        }
    }

    @Override
    protected void parseResponseCommand() throws Exception {
        BaseCommand baseCommand = commandList.get(0);
        LoggerUtil.i(String.format(Locale.US, "IncubatorCommandControl receive: %s",
                new String(responseBuffer, 0, responseBufferLength)));
        int length = responseBufferLength - 4;
        responseBufferLength = 0;
        IncubatorCommandParser.parse(baseCommand, responseBuffer, length);
    }

    @Override
    protected void setConnectionError(boolean status) {
//todo deeplin
//        alarmRepository.produceAlarmFromAndroid(AlarmWordEnum.SYS_CON, status);
        if (status) {
            for (int index = SensorModelEnum.Skin1.ordinal(); index <= SensorModelEnum.Inc.ordinal(); index++) {
                SensorModel sensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.values()[index]);
                sensorModel.textNumber.post(Constant.NA_VALUE);
            }
        }
    }

    @Override
    public void attach() {
        repeat = true;
    }

    @Override
    public void detach() {
        repeat = false;
    }
}