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

    private String statusString;
    private String sensorID;

    public ModuleSetCommand() {
        super();
    }

    public void set(boolean status, String sensorId) {
        if (status) {
            statusString = CommandChar.ON;
        } else {
            statusString = CommandChar.OFF;
        }
        this.sensorID = sensorId;
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "MODULE %s %s", statusString, sensorID);
    }
}