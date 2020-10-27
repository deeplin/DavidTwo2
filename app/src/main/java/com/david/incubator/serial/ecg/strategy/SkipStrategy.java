package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class SkipStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    public SkipStrategy() {
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        return false;
    }
}