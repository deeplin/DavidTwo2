package com.david.incubator.serial.nibp.command;

import com.david.core.serial.BaseCommand;

public class AbortBpCommand extends BaseCommand {
    private final byte[] COMMAND = {0x3A, 0x79, 0x01, 0x00, 0x4C};

    public AbortBpCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
