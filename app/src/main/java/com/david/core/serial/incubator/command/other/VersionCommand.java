package com.david.core.serial.incubator.command.other;

import com.david.core.serial.incubator.BaseIncubatorCommand;

/**
 * author: Ling Lin
 * created on: 2017/7/30 16:01
 * email: 10525677@qq.com
 * description:
 */
public class VersionCommand extends BaseIncubatorCommand {

    private String HARDWARE;
    private String DAVE;
    private String FUSE;
    private String MAIN;
    private String SLAVE_A;
    private String SLAVE_B;
    private String SLAVE_C;
    private String ISO_SLAVE;
    private String ALARM;

    public VersionCommand() {
        super();
    }

    @Override
    protected String getRequestString() {
        return "VERSION";
    }

    public String getHARDWARE() {
        return HARDWARE;
    }

    public void setHARDWARE(String HARDWARE) {
        this.HARDWARE = HARDWARE;
    }

    public String getDAVE() {
        return DAVE;
    }

    public void setDAVE(String DAVE) {
        this.DAVE = DAVE;
    }

    public String getFUSE() {
        return FUSE;
    }

    public void setFUSE(String FUSE) {
        this.FUSE = FUSE;
    }

    public String getMAIN() {
        return MAIN;
    }

    public void setMAIN(String MAIN) {
        this.MAIN = MAIN;
    }

    public String getSLAVE_A() {
        return SLAVE_A;
    }

    public void setSLAVE_A(String SLAVE_A) {
        this.SLAVE_A = SLAVE_A;
    }

    public String getSLAVE_B() {
        return SLAVE_B;
    }

    public void setSLAVE_B(String SLAVE_B) {
        this.SLAVE_B = SLAVE_B;
    }

    public String getSLAVE_C() {
        return SLAVE_C;
    }

    public void setSLAVE_C(String SLAVE_C) {
        this.SLAVE_C = SLAVE_C;
    }

    public String getISO_SLAVE() {
        return ISO_SLAVE;
    }

    public void setISO_SLAVE(String ISO_SLAVE) {
        this.ISO_SLAVE = ISO_SLAVE;
    }

    public String getALARM() {
        return ALARM;
    }

    public void setALARM(String ALARM) {
        this.ALARM = ALARM;
    }
}