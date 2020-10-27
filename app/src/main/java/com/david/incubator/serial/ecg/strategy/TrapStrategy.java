package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class TrapStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    ConfigRepository configRepository;

    @Inject
    public TrapStrategy() {
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        configRepository.getConfig(ConfigEnum.EcgTrapMode).post((int) command.getResponseBuffer()[0]);
        return true;
    }
}
