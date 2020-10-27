package com.david.core.serial.incubator.command.calibration;

import com.david.core.serial.incubator.BaseIncubatorCommand;

public class ShowCalibration2Command extends BaseIncubatorCommand {

    private int A1;
    private int A2;
    private int F1;
    private int HUM;

    public ShowCalibration2Command() {
        super();
        A1 = 0;
        A2 = 0;
        F1 = 0;
        HUM = 0;
    }

    protected String getRequestString() {
        return "SHOW CAL2";
    }

    public int getA1() {
        return A1;
    }

    public void setA1(int a1) {
        A1 = a1;
    }

    public int getF1() {
        return F1;
    }

    public void setF1(int f1) {
        F1 = f1;
    }

    public int getA2() {
        return A2;
    }

    public void setA2(int a2) {
        A2 = a2;
    }

    public int getHUM() {
        return HUM;
    }

    public void setHUM(int HUM) {
        this.HUM = HUM;
    }
}
