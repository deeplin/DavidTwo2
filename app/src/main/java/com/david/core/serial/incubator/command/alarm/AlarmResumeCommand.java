package com.david.core.serial.incubator.command.alarm;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

public class AlarmResumeCommand extends BaseIncubatorCommand {

    private final String commandString;

    public AlarmResumeCommand(String commandString) {
        super();
        this.commandString = commandString;
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "ALERT RESUME %s", commandString);
    }
}