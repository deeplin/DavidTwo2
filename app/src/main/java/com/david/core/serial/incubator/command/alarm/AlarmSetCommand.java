package com.david.core.serial.incubator.command.alarm;

import com.david.core.serial.incubator.BaseIncubatorCommand;
import com.david.core.util.Constant;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/22 21:55
 * email: 10525677@qq.com
 * description:
 */

public class AlarmSetCommand extends BaseIncubatorCommand {

    /*Spo2 Pr*/
    /*Offset*/
    /*OverHeat*/
    String target;
    int value1;
    int value2;

    public AlarmSetCommand() {
        super();
    }

    public void set(String target, int value) {
        this.target = target;
        this.value1 = value;
        this.value2 = Constant.SENSOR_NA_VALUE;
    }

    public void set(String target, int value1, int value2) {
        this.target = target;
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    protected String getRequestString() {
        if (value2 == Constant.SENSOR_NA_VALUE) {
            return String.format(Locale.US, "ALERT SET %s %d", target, value1);
        } else {
            return String.format(Locale.US, "ALERT SET %s %d %d", target, value1, value2);
        }
    }

    @Override
    protected String getCommandId() {
        return String.format(Locale.US, "%s%s", getClass().getSimpleName(), target);
    }
}