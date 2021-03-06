package com.david.core.serial.spo2.strategy;

import com.david.core.serial.spo2.Spo2CommandControl;
import com.david.core.serial.spo2.command.WaveFormCommand;

import io.reactivex.rxjava3.functions.Consumer;

public class ActivateStrategy implements Consumer<byte[]> {

    private final Spo2CommandControl spo2CommandControl;

    public ActivateStrategy(Spo2CommandControl spo2CommandControl) {
        this.spo2CommandControl = spo2CommandControl;
    }

    @Override
    public void accept(byte[] buffer) {
        WaveFormCommand command = new WaveFormCommand();
        spo2CommandControl.produce(command);
    }
}