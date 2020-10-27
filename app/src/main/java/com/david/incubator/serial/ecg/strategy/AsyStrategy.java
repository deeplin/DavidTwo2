package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.core.alarm.AlarmRepository;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class AsyStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    AlarmRepository alarmRepository;

    @Inject
    public AsyStrategy() {
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        if (command.getResponseBuffer()[0] == 0) {
            alarmRepository.produceAlarmFromAndroid(AlarmWordEnum.ECG_ASY, false);
        } else {
            alarmRepository.produceAlarmFromAndroid(AlarmWordEnum.ECG_ASY, true);
        }
        return false;
    }
}