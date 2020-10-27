package com.david.incubator.serial.ecg.command;

import com.david.core.serial.BaseCommand;

public class EnableRespCommand extends BaseCommand {

    private final byte[] COMMAND = {1, 6, 0, 0x60, 0x01, 0x01, (byte) 0xE4};

    public EnableRespCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}