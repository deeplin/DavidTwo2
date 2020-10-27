package com.david.incubator.serial.nibp.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.CrcUtil;

public class SetInitialCommand extends BaseCommand {
    private final byte[] command = {0x3A, 0x17, 0x00, 0x00, 0x00};

    public SetInitialCommand(byte pressure) {
        super();
        command[2] = pressure;
    }

    @Override
    public byte[] getRequest() {
        byte crc = CrcUtil.compute(command, command.length - 1);
        command[command.length - 1] = crc;
        return command;
    }
}