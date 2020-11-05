package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.model.EcgModel;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class VfStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    EcgModel ecgModel;

    @Inject
    public VfStrategy() {
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        ecgModel.setSenAlarm(AlarmWordEnum.ECG_VEN, command.getResponseBuffer()[0] != 0);
        return false;
    }
}