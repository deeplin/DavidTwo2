package com.david.incubator.serial.print.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.Crc8Util;

public class PrintSpeedCommand extends BaseCommand {

    private final byte[] command = {1, 6, 0, 0x14, 0, 0};

    public PrintSpeedCommand(byte speed) {
        super();
        command[4] = speed;
    }

    @Override
    public byte[] getRequest() {
        command[5] = Crc8Util.computeBestCrc(command, command.length);
        return command;
    }
}