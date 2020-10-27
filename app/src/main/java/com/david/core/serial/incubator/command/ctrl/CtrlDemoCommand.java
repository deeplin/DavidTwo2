package com.david.core.serial.incubator.command.ctrl;

import com.david.core.serial.incubator.BaseIncubatorCommand;

public class CtrlDemoCommand extends BaseIncubatorCommand {

    private final boolean status;

    public CtrlDemoCommand(boolean status) {
        super();
        this.status = status;
    }

    @Override
    protected String getRequestString() {
        if (status) {
            return "CTRL DEMO ON";
        } else {
            return "CTRL DEMO OFF";
        }
    }
}