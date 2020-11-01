package com.david.incubator.serial.print.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.Crc8Util;

import javax.inject.Inject;

public class PrintForwardCommand extends BaseCommand {

    private final byte[] COMMAND = {1, 6, 0, 0x18, 0, 0};

    @Inject
    public PrintForwardCommand() {
        super();
    }

    public void setForward(byte forward) {
        COMMAND[4] = forward;
    }

    @Override
    public byte[] getRequest() {
        COMMAND[5] = Crc8Util.computeBestCrc(COMMAND, COMMAND.length);
        return COMMAND;
    }
}