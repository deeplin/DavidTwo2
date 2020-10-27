package com.david.core.serial.incubator.command.other;

import com.david.core.serial.incubator.BaseIncubatorCommand;
import com.david.core.serial.incubator.CommandChar;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:39
 * email: 10525677@qq.com
 * description:
 */
public class LedCommand extends BaseIncubatorCommand {

    public static final String LED37 = "37";
    public static final String BLUE = "BLUE";

    private String value;
    private String status;

    public LedCommand() {
        super();
    }

    public void set(String value, boolean status) {
        this.value = value;
        if (status) {
            this.status = CommandChar.ON;
        } else {
            this.status = CommandChar.OFF;
        }
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "LED %s %s", value, status);
    }
}