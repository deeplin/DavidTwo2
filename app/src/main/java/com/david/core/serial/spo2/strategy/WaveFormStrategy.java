package com.david.core.serial.spo2.strategy;

import com.david.core.serial.spo2.Spo2CommandControl;
import com.david.core.serial.spo2.command.SetupCommand;

import io.reactivex.rxjava3.functions.Consumer;

public class WaveFormStrategy implements Consumer<byte[]> {

    private final Spo2CommandControl spo2CommandControl;

    public WaveFormStrategy(Spo2CommandControl spo2CommandControl) {
        this.spo2CommandControl = spo2CommandControl;
    }

    @Override
    public void accept(byte[] buffer) {
        SetupCommand command = SetupCommand.getInstance();
        spo2CommandControl.produce(command);
    }
}
