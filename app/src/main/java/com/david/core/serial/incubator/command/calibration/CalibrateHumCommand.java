package com.david.core.serial.incubator.command.calibration;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2018/1/6 11:10
 * email: 10525677@qq.com
 * description:
 */
public class CalibrateHumCommand extends BaseIncubatorCommand {

    private int value;

    public CalibrateHumCommand() {
        super();
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "CAL HUM %s", value);
    }
}