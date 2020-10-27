package com.david.incubator.serial.ecg.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.Crc8Util;

public class ComputeLeadCommand extends BaseCommand {

    private final byte[] command = {1, 6, 0, 0x1E, 0, 0};

    public ComputeLeadCommand() {
        super();
    }

    public void setLead(int lead) {
        command[4] = (byte) lead;
        command[5] = Crc8Util.computeBestCrc(command, command.length);
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}
