package com.david.incubator.serial.co2.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.CrcUtil;

public class Co2N2oCompensateCommand extends BaseCommand {

    private final byte[] command = {(byte) 0xaa, 0x55, 0x05, 0x00, 0x00};

    public Co2N2oCompensateCommand(byte compensate) {
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
