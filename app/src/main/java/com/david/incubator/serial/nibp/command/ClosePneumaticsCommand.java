package com.david.incubator.serial.nibp.command;

import com.david.core.serial.BaseCommand;

import io.reactivex.rxjava3.functions.BiConsumer;

public class ClosePneumaticsCommand extends BaseCommand {
    private final byte[] COMMAND = {0x3A, 0x0C, 0x00, 0x01, 0x01, (byte) 0xB8};

    public ClosePneumaticsCommand(BiConsumer<Boolean, BaseCommand> onCompleted) {
        super();
        setCallback(onCompleted);
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
