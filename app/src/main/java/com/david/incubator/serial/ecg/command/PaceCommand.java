package com.david.incubator.serial.ecg.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.Crc8Util;

public class PaceCommand extends BaseCommand {

    private final byte[] command = {1, 6, 0, 0x20, 0, 0};

    public PaceCommand(boolean status) {
        super();
        if (status) {
            command[4] = 1;
        }
        command[5] = Crc8Util.computeBestCrc(command, command.length);
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}
