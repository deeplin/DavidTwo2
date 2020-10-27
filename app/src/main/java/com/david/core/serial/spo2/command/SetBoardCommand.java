package com.david.core.serial.spo2.command;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.BaseCommand;

public class SetBoardCommand extends BaseCommand {
    private final byte[] COMMAND = {(byte) 0xA1, 0x04, 0x20, 0x0D, 0x07, (byte) 0xD0, 0x04, (byte) 0xAF};

    public SetBoardCommand() {
        super(CommandEnum.Critical);
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}

