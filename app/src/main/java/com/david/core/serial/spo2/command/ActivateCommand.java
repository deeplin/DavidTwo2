package com.david.core.serial.spo2.command;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.BaseCommand;

public class ActivateCommand extends BaseCommand {

    private final byte[] COMMAND = {(byte) 0xA1, 0x03, 0x73, 0x00, 0x01, 0x74, (byte) 0xAF};

    public ActivateCommand() {
        super(CommandEnum.Critical);
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}