package com.david.incubator.serial.ecg.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.Crc8Util;

public class SetLeadCommand extends BaseCommand {

    private final byte[] command = {1, 6, 0, 0x16, 0, 0};

    public SetLeadCommand() {
        super();
    }

    public void setLead(boolean lead5) {
        command[4] = (byte) (lead5 ? 1 : 0);
        command[5] = Crc8Util.computeBestCrc(command, command.length);
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}