package com.david.incubator.serial.co2.command;

import com.david.core.serial.BaseCommand;

public class Co2SleepCommand extends BaseCommand {

    private final byte[] command = {(byte) 0xaa, 0x55, 0x00, 0x01, (byte) 0xFF};

    public Co2SleepCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}
