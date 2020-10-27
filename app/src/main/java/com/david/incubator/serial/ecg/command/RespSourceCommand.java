package com.david.incubator.serial.ecg.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.Crc8Util;

public class RespSourceCommand extends BaseCommand {

    private final byte[] command = {1, 6, 0, 0x50, 0x00, 0x00};

    public RespSourceCommand(int source) {
        super();
        if (source == 1) {
            command[4] = 1;
        }
        command[5] = Crc8Util.computeBestCrc(command, command.length);
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}