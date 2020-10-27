package com.david.incubator.serial.ecg.command;

import com.david.core.serial.BaseCommand;

public class MonitorCommand extends BaseCommand {

    private final byte[] COMMAND_DIAGNOSTIC = {1, 6, 0, 0x26, 0x00, (byte) 0xC6};
    private final byte[] COMMAND_MONITOR = {1, 6, 0, 0x26, 0x01, 0x01, (byte) 0xC1};

    private final byte[] command;

    public MonitorCommand(boolean isMonitor) {
        super();
        if (isMonitor) {
            command = COMMAND_MONITOR;
        } else {
            command = COMMAND_DIAGNOSTIC;
        }
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}