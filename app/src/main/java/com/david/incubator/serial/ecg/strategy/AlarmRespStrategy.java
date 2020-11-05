package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.model.EcgModel;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class AlarmRespStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    EcgModel ecgModel;

    @Inject
    public AlarmRespStrategy() {
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        ecgModel.setSenAlarm(AlarmWordEnum.ECG_APN, command.getResponseBuffer()[0] != 0);
        return false;
    }
}