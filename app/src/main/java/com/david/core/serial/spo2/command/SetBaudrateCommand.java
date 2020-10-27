package com.david.core.serial.spo2.command;

import com.david.core.serial.BaseCommand;

public class SetBaudrateCommand extends BaseCommand {
    private final byte[] COMMAND = {(byte) 0xA1, 0x02, 0x23, 0x05, 0x28, (byte) 0xAF};

    public SetBaudrateCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
