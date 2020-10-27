package com.david.core.serial.incubator.command.other;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/18 10:44
 * email: 10525677@qq.com
 * description:
 */
public class DigitalCommand extends BaseIncubatorCommand {

    private int Do;
    private int Dc;
    private int Ho;
    private int Hc;
    private int Mo;
    private int Ms;
    private int Ds;
    private int Ps;
    private int Ts;
    private int WTs;
    private int Hs;
    private int Is1;
    private int Is2;
    private int WMs;
    private int Fan;
    private String Others;

    @Inject
    public DigitalCommand() {
        super();
    }

    @Override
    protected String getRequestString() {
        return "DIGITAL";
    }

    public int getDo() {
        return Do;
    }

    public void setDo(int aDo) {
        Do = aDo;
    }

    public int getDc() {
        return Dc;
    }

    public void setDc(int dc) {
        Dc = dc;
    }

    public int getHo() {
        return Ho;
    }

    public void setHo(int ho) {
        Ho = ho;
    }

    public int getHc() {
        return Hc;
    }

    public void setHc(int hc) {
        Hc = hc;
    }

    public int getMo() {
        return Mo;
    }

    public void setMo(int mo) {
        Mo = mo;
    }

    public int getMs() {
        return Ms;
    }

    public void setMs(int ms) {
        Ms = ms;
    }

    public int getDs() {
        return Ds;
    }

    public void setDs(int ds) {
        Ds = ds;
    }

    public int getPs() {
        return Ps;
    }

    public void setPs(int ps) {
        Ps = ps;
    }

    public int getTs() {
        return Ts;
    }

    public void setTs(int ts) {
        Ts = ts;
    }

    public int getWTs() {
        return WTs;
    }

    public void setWTs(int WTs) {
        this.WTs = WTs;
    }

    public int getHs() {
        return Hs;
    }

    public void setHs(int hs) {
        Hs = hs;
    }

    public int getIs1() {
        return Is1;
    }

    public void setIs1(int is1) {
        Is1 = is1;
    }

    public int getIs2() {
        return Is2;
    }

    public void setIs2(int is2) {
        Is2 = is2;
    }

    public int getWMs() {
        return WMs;
    }

    public void setWMs(int WMs) {
        this.WMs = WMs;
    }

    public int getFan() {
        return Fan;
    }

    public void setFan(int fan) {
        Fan = fan;
    }

    public String getOthers() {
        return Others;
    }

    public void setOthers(String others) {
        Others = others;
    }
}