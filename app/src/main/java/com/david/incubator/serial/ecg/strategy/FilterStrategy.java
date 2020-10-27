package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class FilterStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    ConfigRepository configRepository;

    @Inject
    public FilterStrategy() {
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        switch (command.getResponseBuffer()[0]) {
            case (1):
                configRepository.getConfig(ConfigEnum.EcgFilterMode).post(2);
                break;
            case (0x12):
                configRepository.getConfig(ConfigEnum.EcgFilterMode).post(1);
                break;
            case (0x13):
                configRepository.getConfig(ConfigEnum.EcgFilterMode).post(0);
                break;
        }
        return true;
    }
}