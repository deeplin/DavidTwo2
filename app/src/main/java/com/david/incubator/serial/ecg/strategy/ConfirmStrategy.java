package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class ConfirmStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    public ConfirmStrategy(){
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        return true;
    }
}