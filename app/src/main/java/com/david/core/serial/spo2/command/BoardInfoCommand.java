package com.david.core.serial.spo2.command;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.BaseCommand;

import javax.inject.Inject;

public class BoardInfoCommand extends BaseCommand {
    private final byte[] COMMAND = {(byte) 0xA1, 0x01, 0x70, 0x70, (byte) 0xAF};

    @Inject
    public BoardInfoCommand() {
        super(CommandEnum.Critical);
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
