package com.david.incubator.serial.nibp.command;

import com.david.core.serial.BaseCommand;

import io.reactivex.rxjava3.functions.BiConsumer;

public class NibpCalibrateCommand extends BaseCommand {
    private final byte[] COMMAND = {0x3A, 0x04, 0x00, (byte) 0xC2};

    public NibpCalibrateCommand(boolean zero) {
        super();
        if (!zero) {
            COMMAND[2] = 1;
            COMMAND[3] = (byte) 0xC1;
        }
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}