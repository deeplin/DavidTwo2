package com.david.core.serial.incubator.command.calibration;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/28 9:54
 * email: 10525677@qq.com
 * description:
 */

public class CalibrateScaleCommand extends BaseIncubatorCommand {

    private final int value;

    public CalibrateScaleCommand(int value) {
        this.value = value;
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "CAL SCALE %d", value);
    }
}