package com.david.core.serial.incubator.command.module;

import com.david.core.serial.incubator.BaseIncubatorCommand;
import com.david.core.serial.incubator.CommandChar;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/9/19 13:41
 * email: 10525677@qq.com
 * description:
 */

public class ModuleSetCommand extends BaseIncubatorCommand {

    private String sensorId;
    private boolean software;
    private String statusString;

    public ModuleSetCommand() {
        super();
    }

    public void set(String sensorId, boolean software, boolean status) {
        this.sensorId = sensorId;
        this.software = software;
        if (status) {
            statusString = CommandChar.ON;
        } else {
            statusString = CommandChar.OFF;
        }
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "MODULE %s %s%s", sensorId, software ? "S" : "H", statusString);
    }
}