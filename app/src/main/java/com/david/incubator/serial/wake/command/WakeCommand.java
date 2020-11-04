package com.david.incubator.serial.wake.command;

import com.david.core.serial.incubator.BaseIncubatorCommand;

public class WakeCommand extends BaseIncubatorCommand {

    private int num;

    public WakeCommand() {
        super();
    }

    public void set(int num) {
        this.num = num;
    }

    @Override
    protected String getRequestString() {
        if (num > 0) {
            return "CTRL WAKE " + num;
        } else {
            return "CTRL WAKE OFF";
        }
    }
}
