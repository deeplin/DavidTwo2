package com.david.core.serial.spo2.command;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.BaseCommand;

public class WaveFormCommand extends BaseCommand {
    private final byte[] COMMAND = {(byte) 0xA1, 0x05, 0x21, 0x16, 0x00, 0x00, 0x10, 0x47, (byte) 0xAF};

    public WaveFormCommand() {
        super(CommandEnum.Critical);
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
