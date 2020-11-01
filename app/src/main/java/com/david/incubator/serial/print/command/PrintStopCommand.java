package com.david.incubator.serial.print.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.Crc8Util;

import javax.inject.Inject;

public class PrintStopCommand extends BaseCommand {

    private final byte[] COMMAND = {1, 5, 0, 0x16, 0};

    @Inject
    public PrintStopCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        COMMAND[4] = Crc8Util.computeBestCrc(COMMAND, COMMAND.length);
        return COMMAND;
    }
}