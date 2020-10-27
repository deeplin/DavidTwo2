package com.david.core.serial.incubator.command.alarm;

import com.david.core.serial.incubator.BaseIncubatorCommand;

/**
 * author: Ling Lin
 * created on: 2017/7/20 19:31
 * email: 10525677@qq.com
 * description:
 */

public class AlarmVolumeCommand extends BaseIncubatorCommand {

    private int volume;

    public AlarmVolumeCommand() {
        super();
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    protected String getRequestString() {
        if (volume == 0) {
            return "BEEP LOW";
        }
        return "BEEP HIGH";
    }
}