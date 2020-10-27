package com.david.core.serial.incubator.command.repeated;

import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.CommandEnum;
import com.david.core.enumeration.CtrlEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.BaseIncubatorCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:30
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class StatusCommand extends BaseIncubatorCommand {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    SystemModel systemModel;

    private String mode;
    private String ctrl;
    private int time;
    private int CTime;
    private int warm;
    private int inc;
    /*湿度阀门*/
    private int hum;
    /*氧气阀门*/
    private int o2;
    private int ohtest;
    private int demo;

    private SensorModel incSensorModel;
    private SensorModel warmerSensorModel;

    @Inject
    public StatusCommand() {
        super(CommandEnum.Repeated);
    }

    @Override
    protected String getRequestString() {
        return "STATUS";
    }

    public void initCallback() {
        incSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Inc);
        warmerSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Warmer);

        setCallback((aBoolean, baseCommand) -> {
            if (aBoolean) {
                SystemEnum systemMode = SystemEnum.valueOf(mode);
//                incubatorModel.systemMode.post(SystemEnum.Warmer);
                incubatorModel.systemMode.post(systemMode);

                CtrlEnum ctrlEnum = CtrlEnum.valueOf(ctrl);
                incubatorModel.ctrlMode.post(ctrlEnum);

                incubatorModel.humidityPower.post(hum > 0);
                incubatorModel.oxygenPower.post(o2 > 0);
                incubatorModel.ohTest.post(ohtest > 0);
                incubatorModel.cTime.post(CTime);
                incSensorModel.textNumber.post(inc);
                warmerSensorModel.textNumber.post(warm);
                systemModel.demo.post(demo > 0);
            }
        });
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCtrl() {
        return this.ctrl;
    }

    public void setCtrl(String ctrl) {
        this.ctrl = ctrl;
    }

    public int getWarm() {
        return this.warm;
    }

    public void setWarm(int warm) {
        this.warm = warm;
    }

    public int getInc() {
        return this.inc;
    }

    public void setInc(int inc) {
        this.inc = inc;
    }

    public int getHum() {
        return this.hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }

    public int getO2() {
        return this.o2;
    }

    public void setO2(int o2) {
        this.o2 = o2;
    }

    public int getCTime() {
        return CTime;
    }

    public void setCTime(int CTime) {
        this.CTime = CTime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getOhtest() {
        return ohtest;
    }

    public void setOhtest(int ohtest) {
        this.ohtest = ohtest;
    }

    public int getDemo() {
        return demo;
    }

    public void setDemo(int demo) {
        this.demo = demo;
    }
}