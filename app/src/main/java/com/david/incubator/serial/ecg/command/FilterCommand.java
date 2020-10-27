package com.david.incubator.serial.ecg.command;

import com.david.core.serial.BaseCommand;

public class FilterCommand extends BaseCommand {

    private final byte[] COMMAND_MONITOR = {1, 6, 0, 0x18, 0x13, (byte) 0x90};
    private final byte[] COMMAND_OPERATION = {1, 6, 0, 0x18, 0x12, (byte) 0x97};
    private final byte[] COMMAND_DIAGNOSTIC = {1, 6, 0, 0x18, 0x01, 0x01, (byte) 0xEE};

    private final byte[] command;

    public FilterCommand(int mode) {
        super();
        switch (mode) {
            case (0):
                command = COMMAND_MONITOR;
                break;
            case (1):
                command = COMMAND_OPERATION;
                break;
            default:
                command = COMMAND_DIAGNOSTIC;
                break;
        }
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}