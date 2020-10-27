package com.david.incubator.serial.nibp.command;

import com.david.core.serial.BaseCommand;

import io.reactivex.rxjava3.functions.BiConsumer;

public class GetBPCommand extends BaseCommand {
    private final byte[] COMMAND = {0x3A, 0x79, 0x03, 0x00, 0x4A};

    public GetBPCommand(BiConsumer<Boolean, BaseCommand> callback) {
        super();
        setCallback(callback);
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
