package com.david.core.serial.incubator.command.ctrl;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2018/1/19 20:21
 * email: 10525677@qq.com
 * description:
 */
public class CtrlOverrideCommand extends BaseIncubatorCommand {

    private String mode;

    public CtrlOverrideCommand() {
        super();
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "CTRL OVERRIDE %s", mode);
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}