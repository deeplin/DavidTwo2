package com.david.core.serial.spo2.strategy;

import com.david.core.serial.spo2.Spo2CommandControl;
import com.david.core.serial.spo2.command.SetFunctionCommand;

import io.reactivex.rxjava3.functions.Consumer;

public class SetBoardStrategy implements Consumer<byte[]> {

    private final Spo2CommandControl spo2CommandControl;

    public SetBoardStrategy(Spo2CommandControl spo2CommandControl) {
        this.spo2CommandControl = spo2CommandControl;
    }

    @Override
    public void accept(byte[] buffer) {
        if (buffer[1] == 0x01 && buffer[2] == 0x01 && buffer[3] == 0x01) {
            SetFunctionCommand setFunctionCommand = new SetFunctionCommand();
            spo2CommandControl.produce(setFunctionCommand);
        }
    }
}