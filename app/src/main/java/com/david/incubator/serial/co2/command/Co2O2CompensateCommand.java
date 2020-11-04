package com.david.incubator.serial.co2.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.CrcUtil;

public class Co2O2CompensateCommand extends BaseCommand {

    private final byte[] command = {(byte) 0xaa, 0x55, 0x04, 0x00, 0x00};

    public Co2O2CompensateCommand(byte compensate) {
        super();
        command[3] = compensate;
    }

    @Override
    public byte[] getRequest() {
        byte crc = (byte) (256 - CrcUtil.computeSumCrc(command, 2, command.length - 1));
        command[command.length - 1] = crc;
        return command;
    }
}