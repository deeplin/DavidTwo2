package com.david.core.serial.spo2.strategy;

import com.david.core.serial.spo2.Spo2CommandControl;
import com.david.core.serial.spo2.command.ActivateCommand;

import io.reactivex.rxjava3.functions.Consumer;

public class UnlockStrategy implements Consumer<byte[]> {

    private final Spo2CommandControl spo2CommandControl;

    public UnlockStrategy(Spo2CommandControl spo2CommandControl) {
        this.spo2CommandControl = spo2CommandControl;
    }

    @Override
    public void accept(byte[] buffer) {
        ActivateCommand command = new ActivateCommand();
        spo2CommandControl.produce(command);
    }
}