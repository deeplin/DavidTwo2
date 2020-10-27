package com.david.core.serial.incubator.command.ctrl;

import com.david.core.serial.incubator.BaseIncubatorCommand;

/**
 * filename: com.eternal.davidconsole.serial.command.ctrl.CtrlSetCommand.java
 * author: Ling Lin
 * created on: 2017/7/22 12:46
 * email: 10525677@qq.com
 * description:
 */

public class CtrlSetCommand extends BaseIncubatorCommand {

    private String mode;
    private String target;
    private String value;

    public CtrlSetCommand() {
        super();
    }

    public void set(String mode, String target, String value) {
        this.mode = mode.toUpperCase();
        this.target = target.toUpperCase();
        this.value = value;
    }

    @Override
    protected String getRequestString() {
        return String.format("CTRL SET %s %s %s", mode, target, value);
    }
}