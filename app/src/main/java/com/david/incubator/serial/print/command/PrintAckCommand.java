package com.david.incubator.serial.print.command;

import com.david.core.serial.BaseCommand;
import com.david.core.serial.CommandEnum;
import com.david.core.util.Crc8Util;

public class PrintAckCommand extends BaseCommand {
    private final byte[] COMMAND = {0x1, 0x6, 0x0, 0x1, 0x0, 0x0};

    public PrintAckCommand() {
        super(CommandEnum.NoResponse);
    }

    public void setType(byte type) {
        COMMAND[4] = type;
        COMMAND[5] = Crc8Util.computeBestCrc(COMMAND, COMMAND.length);
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
