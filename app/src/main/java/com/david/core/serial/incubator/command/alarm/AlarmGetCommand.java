package com.david.core.serial.incubator.command.alarm;

import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

/**
 * filename: com.eternal.davidconsole.serial.command.alertString.AlarmGetCommand.java
 * author: Ling Lin
 * created on: 2017/7/22 22:01
 * email: 10525677@qq.com
 * description:
 */

public class AlarmGetCommand extends BaseIncubatorCommand {

    private AlarmWordEnum alarmWordEnum;

    /*Offset*/
    private int ADevH;
    private int ADevL;
    private int SDevH;
    private int SDevL;
    private int ODevH;
    private int ODevL;
    private int HDevH;
    private int HDevL;

    /*Overheat*/
    private int T1;
    private int T2;
    private int T;
    private int RPM;

    /*Spo2 Pr*/
    private int limit;

    public AlarmGetCommand() {
        super();
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "ALERT GET %s", alarmWordEnum.toString());
    }

    @Override
    protected String getCommandId() {
        return String.format(Locale.US, "%s%s", getClass().getSimpleName(), alarmWordEnum.toString());
    }

    public void setMode(AlarmWordEnum alarmWordEnum) {
        this.alarmWordEnum = alarmWordEnum;
    }

    public int getADevH() {
        return ADevH;
    }

    public void setADevH(int ADevH) {
        this.ADevH = ADevH;
    }

    public int getADevL() {
        return ADevL;
    }

    public void setADevL(int ADevL) {
        this.ADevL = ADevL;
    }

    public int getSDevH() {
        return SDevH;
    }

    public void setSDevH(int SDevH) {
        this.SDevH = SDevH;
    }

    public int getSDevL() {
        return SDevL;
    }

    public void setSDevL(int SDevL) {
        this.SDevL = SDevL;
    }

    public int getODevH() {
        return ODevH;
    }

    public void setODevH(int ODevH) {
        this.ODevH = ODevH;
    }

    public int getODevL() {
        return ODevL;
    }

    public void setODevL(int ODevL) {
        this.ODevL = ODevL;
    }

    public int getHDevH() {
        return HDevH;
    }

    public void setHDevH(int HDevH) {
        this.HDevH = HDevH;
    }

    public int getHDevL() {
        return HDevL;
    }

    public void setHDevL(int HDevL) {
        this.HDevL = HDevL;
    }

    public int getT1() {
        return T1;
    }

    public void setT1(int t1) {
        T1 = t1;
    }

    public int getT2() {
        return T2;
    }

    public void setT2(int t2) {
        T2 = t2;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getT() {
        return T;
    }

    public void setT(int t) {
        T = t;
    }

    public int getRPM() {
        return RPM;
    }

    public void setRPM(int RPM) {
        this.RPM = RPM;
    }
}