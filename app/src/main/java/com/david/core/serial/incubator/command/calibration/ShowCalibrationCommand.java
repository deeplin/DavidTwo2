package com.david.core.serial.incubator.command.calibration;

import com.david.core.serial.incubator.BaseIncubatorCommand;

/**
 * author: Ling Lin
 * created on: 2017/8/4 10:10
 * email: 10525677@qq.com
 * description:
 */

public class ShowCalibrationCommand extends BaseIncubatorCommand {

    private int S1A;
    private int S1B;
    private int S2;

    public ShowCalibrationCommand() {
        super();
        S1A = 0;
        S1B = 0;
        S2 = 0;
    }

    @Override
    protected String getRequestString() {
        return "SHOW CAL1";
    }

    public int getS1A() {
        return S1A;
    }

    public void setS1A(int s1A) {
        S1A = s1A;
    }

    public int getS1B() {
        return S1B;
    }

    public void setS1B(int s1B) {
        S1B = s1B;
    }

    public int getS2() {
        return S2;
    }

    public void setS2(int s2) {
        S2 = s2;
    }
}