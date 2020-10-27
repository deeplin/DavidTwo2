package com.david.core.serial.incubator.command.other;

import com.david.core.serial.incubator.BaseIncubatorCommand;

public class FactoryCommand extends BaseIncubatorCommand {

    public FactoryCommand() {
        super();
    }

    @Override
    protected String getRequestString() {
        return "FACTORY";
    }
}