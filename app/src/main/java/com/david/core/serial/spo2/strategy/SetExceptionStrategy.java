package com.david.core.serial.spo2.strategy;

import com.david.core.serial.spo2.Spo2CommandControl;
import com.david.core.serial.spo2.command.SetBoardCommand;

import io.reactivex.rxjava3.functions.Consumer;

public class SetExceptionStrategy implements Consumer<byte[]> {

    private final Spo2CommandControl spo2CommandControl;

    public SetExceptionStrategy(Spo2CommandControl spo2CommandControl) {
        this.spo2CommandControl = spo2CommandControl;
    }

    @Override
    public void accept(byte[] buffer) {
        if (buffer[1] == 0x01 && buffer[2] == 0x01 && buffer[3] == 0x01) {
            SetBoardCommand setBoardCommand = new SetBoardCommand();
            spo2CommandControl.produce(setBoardCommand);
        }
    }
}