package com.david.core.serial.incubator.command.alarm;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/20 21:59
 * email: 10525677@qq.com
 * description:
 */

public class AlarmMuteCommand extends BaseIncubatorCommand {

    private String alertId;
    private String option;
    private int time;

    public AlarmMuteCommand() {
        super();
    }

    public void set(String alarmWord, String option, int time) {
        this.alertId = alarmWord;
        this.option = option;
        this.time = time;
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "ALERT MUTE %s %s %s", alertId, option, time);
    }

    @Override
    protected String getCommandId() {
        return String.format(Locale.US, "%s%s", getClass().getSimpleName(), alertId);
    }
}