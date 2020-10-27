package com.david.incubator.serial.nibp.command;

import com.david.core.serial.BaseCommand;

import io.reactivex.rxjava3.functions.BiConsumer;

public class OpenPneumaticsCommand extends BaseCommand {
    private final byte[] COMMAND = {0x3A, 0x0C, 0x00, 0x00, 0x00, (byte) 0xBA};

    public OpenPneumaticsCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
