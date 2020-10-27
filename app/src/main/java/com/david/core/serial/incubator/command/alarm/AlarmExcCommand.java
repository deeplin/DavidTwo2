package com.david.core.serial.incubator.command.alarm;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

public class AlarmExcCommand extends BaseIncubatorCommand {

    private final String group;
    private final String category;
    private final int value;

    public AlarmExcCommand(String group, String category, int value) {
        super();
        this.group = group;
        this.category = category;
        this.value = value;
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "EXC %s %s %d", group, category, value);
    }

    @Override
    protected String getCommandId() {
        return String.format(Locale.US, "%s%s", group, category);
    }
}