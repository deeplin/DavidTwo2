package com.david.incubator.serial.nibp.command;

import com.david.core.serial.BaseCommand;

public class GetFirstCuffPressureCommand extends BaseCommand {
    private final byte[] COMMAND = {0x3A, 0x79, 0x05, 0x00, 0x48};

    public GetFirstCuffPressureCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}