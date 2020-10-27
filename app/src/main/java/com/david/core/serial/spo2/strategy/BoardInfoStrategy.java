package com.david.core.serial.spo2.strategy;

import com.david.core.serial.spo2.Spo2CommandControl;
import com.david.core.serial.spo2.command.UnlockCommand;

import io.reactivex.rxjava3.functions.Consumer;

public class BoardInfoStrategy implements Consumer<byte[]> {

    private final Spo2CommandControl spo2CommandControl;

    public BoardInfoStrategy(Spo2CommandControl spo2CommandControl) {
        this.spo2CommandControl = spo2CommandControl;
    }

    @Override
    public void accept(byte[] buffer) {
        UnlockCommand command = new UnlockCommand();
        command.fillData(buffer, 3, 10);
        spo2CommandControl.produce(command);
    }
}