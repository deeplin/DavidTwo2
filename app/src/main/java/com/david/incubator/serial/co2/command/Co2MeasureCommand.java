package com.david.incubator.serial.co2.command;

import com.david.core.serial.BaseCommand;

public class Co2MeasureCommand extends BaseCommand {

    private final byte[] command = {(byte) 0xaa, 0x55, 0x00, 0x02, (byte) 0xFE};

    public Co2MeasureCommand() {
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}
