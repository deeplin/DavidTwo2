package com.david.core.serial.incubator.command.ctrl;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/22 12:44
 * email: 10525677@qq.com
 * description:
 */

public class CtrlModeCommand extends BaseIncubatorCommand {

    private String mode;

    public CtrlModeCommand() {
        super();
    }

    public void setMode(String mode) {
        this.mode = mode.toUpperCase();
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "CTRL MODE %s", mode);
    }
}