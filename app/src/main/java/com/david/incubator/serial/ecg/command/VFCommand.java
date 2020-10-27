package com.david.incubator.serial.ecg.command;

import com.david.core.serial.BaseCommand;

public class VFCommand extends BaseCommand {

    private final byte[] command = {1, 6, 0, 0x32, 0x01, 0x01, (byte) 0xC2};

    public VFCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}