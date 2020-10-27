package com.david.incubator.serial.ecg.command;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.BaseCommand;

public class BootUpCommand extends BaseCommand {

    private final byte[] COMMAND = {0x1, 0x6, 0x0, 0x1, (byte) 0x80, (byte) 0x8a};

    public BootUpCommand() {
        super(CommandEnum.NoResponse);
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}