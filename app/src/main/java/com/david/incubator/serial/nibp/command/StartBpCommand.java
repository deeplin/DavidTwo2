package com.david.incubator.serial.nibp.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.Constant;

public class StartBpCommand extends BaseCommand {
    /*新生儿*/
    private final byte[] COMMAND_NEONATE = {0x3A, 0x28, (byte) 0x9E};
    /*成人*/
    private final byte[] COMMAND = {0x3A, 0x20, (byte) 0xA6};

    public StartBpCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        if (Constant.IS_NEONATE) {
            return COMMAND_NEONATE;
        } else {
            return COMMAND;
        }
    }
}
