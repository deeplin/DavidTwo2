package com.david.core.serial.incubator.command.alarm;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/20 19:31
 * email: 10525677@qq.com
 * description:
 */

public class AlarmDisableCommand extends BaseIncubatorCommand {

    private final String commandString;

    public AlarmDisableCommand(String commandString) {
        super();
        this.commandString = commandString;
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "ALERT DISABLE %s 5", commandString);
    }
}