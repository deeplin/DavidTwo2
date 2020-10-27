package com.david.core.serial.spo2.command;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.BaseCommand;

public class SetFunctionCommand extends BaseCommand {
    private final byte[] COMMAND = {(byte) 0xA1, 0x04, 0x20, 0x10, 0x03, (byte) 0xE8, 0x1B, (byte) 0xAF};

    public SetFunctionCommand() {
        super(CommandEnum.Critical);
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
