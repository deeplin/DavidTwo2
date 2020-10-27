package com.david.incubator.serial.ecg.command;

import com.david.core.serial.BaseCommand;

public class UserCommand extends BaseCommand {

    private final byte[] COMMAND = {1, 6, 0, 0x22, 0x02, (byte) 0x9C};

    public UserCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}