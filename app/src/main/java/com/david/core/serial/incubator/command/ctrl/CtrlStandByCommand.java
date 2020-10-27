package com.david.core.serial.incubator.command.ctrl;

import com.david.core.serial.incubator.BaseIncubatorCommand;
import com.david.core.serial.incubator.CommandChar;

import java.util.Locale;

public class CtrlStandByCommand extends BaseIncubatorCommand {

    private String statusString;

    public CtrlStandByCommand() {
        super();
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "CTRL STANDBY %s", statusString);
    }

    public void setStatus(boolean status) {
        if (status) {
            statusString = CommandChar.ON;
        } else {
            statusString = CommandChar.OFF;
        }
    }
}