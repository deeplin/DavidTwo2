package com.david.core.serial.spo2.command;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.BaseCommand;

public class SetExceptionOutputCommand extends BaseCommand {
    private final byte[] COMMAND = {(byte) 0xA1, 0x04, 0x20, 0x0C, 0x07, (byte) 0xD0, 0x03, (byte) 0xAF};

    public SetExceptionOutputCommand() {
        super(CommandEnum.Critical);
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
