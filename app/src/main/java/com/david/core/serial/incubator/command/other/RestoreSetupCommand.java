package com.david.core.serial.incubator.command.other;

import com.david.core.serial.incubator.BaseIncubatorCommand;

public class RestoreSetupCommand extends BaseIncubatorCommand {

    public RestoreSetupCommand() {
        super();
    }

    @Override
    protected String getRequestString() {
        return "RESTORE_SET";
    }
}