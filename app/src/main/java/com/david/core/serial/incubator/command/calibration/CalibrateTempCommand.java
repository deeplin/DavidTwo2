package com.david.core.serial.incubator.command.calibration;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/28 9:55
 * email: 10525677@qq.com
 * description:
 */

public class CalibrateTempCommand extends BaseIncubatorCommand {

    private String id;
    private int value;
    private int offset;

    public CalibrateTempCommand() {
        super();
    }

    public void set(String id, int value) {
        this.id = id;
        this.value = value;
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "CAL TEMP %s %d", id, value);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}