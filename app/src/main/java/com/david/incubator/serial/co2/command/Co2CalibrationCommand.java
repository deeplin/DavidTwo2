package com.david.incubator.serial.co2.command;

import com.david.core.serial.BaseCommand;

public class Co2CalibrationCommand extends BaseCommand {

    private final byte[] command = {(byte) 0xaa, 0x55, 0x06, (byte) 0xFF, (byte) 0xFB};

    public Co2CalibrationCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}
