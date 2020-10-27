package com.david.core.serial.incubator;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.BaseCommand;

public abstract class BaseIncubatorCommand extends BaseCommand {

    public BaseIncubatorCommand() {
        super();
    }

    public BaseIncubatorCommand(CommandEnum commandEnum) {
        super(commandEnum);
    }

    public byte[] getRequest() {
        return ("~" + getRequestString() + CommandChar.ENTER).getBytes();
    }

    protected abstract String getRequestString();
}