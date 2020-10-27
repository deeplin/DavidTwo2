package com.david.core.serial.incubator.command.alarm;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

public class AlarmDisable120Command extends BaseIncubatorCommand {

    private final String commandString;

    public AlarmDisable120Command(String commandString) {
        super();
        this.commandString = commandString;
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "ALERT DISABLE %s 120", commandString);
    }
}