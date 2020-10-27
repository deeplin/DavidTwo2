package com.david.core.serial.spo2.command;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.BaseCommand;
import com.david.core.util.CrcUtil;

public abstract class BaseSpo2SerialCommand extends BaseCommand {

    public BaseSpo2SerialCommand() {
        super();
    }

    public BaseSpo2SerialCommand(CommandEnum commandEnum) {
        super(commandEnum);
    }

    protected void setCrc(byte[] buffer) {
        buffer[buffer.length - 2] = CrcUtil.computeSumCrc(buffer, 2, buffer.length - 2);
    }
}
