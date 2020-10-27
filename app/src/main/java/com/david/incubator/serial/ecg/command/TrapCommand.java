package com.david.incubator.serial.ecg.command;

import com.david.core.serial.BaseCommand;

/**
 * author: Ling Lin
 * created on: 2017/7/20 19:31
 * email: 10525677@qq.com
 * description:
 */

public class TrapCommand extends BaseCommand {

    public static final int TRAP_DIAGNOSTIC_NONE = 0;
    public static final int TRAP_DIAGNOSTIC_50 = 1;
    public static final int TRAP_DIAGNOSTIC_60 = 2;

    public static final int TRAP_MONITOR_50 = 0x11;
    public static final int TRAP_MONITOR_60 = 0x12;
    public static final int TRAP_MONITOR_5060 = 0x13;

    private final byte[] COMMAND_DIAGNOSTIC_NONE = {1, 6, 0, 0x1A, 0x00, (byte) 0xC3};
    private final byte[] COMMAND_DIAGNOSTIC_50 = {1, 6, 0, 0x1A, 0x01, 0x01, (byte) 0xC4};
    private final byte[] COMMAND_DIAGNOSTIC_60 = {1, 6, 0, 0x1A, 0x02, (byte) 0xCD};

    private final byte[] COMMAND_MONITOR_50 = {1, 6, 0, 0x1A, 0x11, (byte) 0xB4};
    private final byte[] COMMAND_MONITOR_60 = {1, 6, 0, 0x1A, 0x12, (byte) 0xBD};
    private final byte[] COMMAND_MONITOR_5060 = {1, 6, 0, 0x1A, 0x13, (byte) 0xBA};

    private final byte[] command;

    public TrapCommand(int mode) {
        super();
        switch (mode) {
            case (TRAP_DIAGNOSTIC_NONE):
                command = COMMAND_DIAGNOSTIC_NONE;
                break;
            case (TRAP_DIAGNOSTIC_50):
                command = COMMAND_DIAGNOSTIC_50;
                break;
            case (TRAP_DIAGNOSTIC_60):
                command = COMMAND_DIAGNOSTIC_60;
                break;
            case (TRAP_MONITOR_50):
                command = COMMAND_MONITOR_50;
                break;
            case (TRAP_MONITOR_60):
                command = COMMAND_MONITOR_60;
                break;
            default:
                command = COMMAND_MONITOR_5060;
                break;
        }
    }

    @Override
    public byte[] getRequest() {
        return command;
    }
}