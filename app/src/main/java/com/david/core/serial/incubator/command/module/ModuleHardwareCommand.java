package com.david.core.serial.incubator.command.module;

import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.BaseIncubatorCommand;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/9/19 13:00
 * email: 10525677@qq.com
 * description:
 */

public class ModuleHardwareCommand extends BaseIncubatorCommand {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SystemModel systemModel;

    public void setHUM(int HUM) {
        this.HUM = HUM;
    }

    public void setO2(int o2) {
        O2 = o2;
    }

    public void setSPO2(int SPO2) {
        this.SPO2 = SPO2;
    }

    public void setSCALE(int SCALE) {
        this.SCALE = SCALE;
    }

    public void setECG(int ECG) {
        this.ECG = ECG;
    }

    public void setNIBP(int NIBP) {
        this.NIBP = NIBP;
    }

    public void setCO2(int CO2) {
        this.CO2 = CO2;
    }

    public void setWAKE(int WAKE) {
        this.WAKE = WAKE;
    }

    public void setAMBI(int AMBI) {
        this.AMBI = AMBI;
    }

    public void setCAMERA(int CAMERA) {
        this.CAMERA = CAMERA;
    }

    public void setBLUE(int BLUE) {
        this.BLUE = BLUE;
    }

    public void setMAT(int MAT) {
        this.MAT = MAT;
    }

    public void setMUTE(int MUTE) {
        this.MUTE = MUTE;
    }

    public void setANGLE(int ANGLE) {
        this.ANGLE = ANGLE;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    protected int HUM;
    protected int O2;
    protected int SPO2;
    protected int SCALE;
    protected int ECG;
    protected int NIBP;
    protected int CO2;
    protected int WAKE;
    protected int AMBI;

    private int CAMERA;
    private int BLUE;
    private int MAT;
    private int MUTE;
    private int ANGLE;
    private String MODEL;

    public ModuleHardwareCommand() {
        super();
        ContextUtil.getComponent().inject(this);
        super.setCallback((aBoolean, baseCommand) -> {
            if (aBoolean) {
                moduleHardware.post(ModuleEnum.Hum, HUM);
                moduleHardware.post(ModuleEnum.Oxygen, O2);
                moduleHardware.post(ModuleEnum.Spo2, SPO2);
                moduleHardware.post(ModuleEnum.Weight, SCALE);
                moduleHardware.post(ModuleEnum.Ecg, ECG);
                moduleHardware.post(ModuleEnum.Nibp, NIBP);
                moduleHardware.post(ModuleEnum.Co2, CO2);
                moduleHardware.post(ModuleEnum.Wake, WAKE);
                moduleHardware.post(ModuleEnum.Mat, MAT);

                moduleHardware.post(ModuleEnum.Ambient, AMBI);
                moduleHardware.post(ModuleEnum.Camera, CAMERA);
                moduleHardware.post(ModuleEnum.Blue, BLUE);
                moduleHardware.post(ModuleEnum.Mute, MUTE);
                moduleHardware.post(ModuleEnum.Angle, ANGLE);

                moduleHardware.post(ModuleEnum.Hum, 2);
                moduleHardware.post(ModuleEnum.Oxygen, 2);
                moduleHardware.post(ModuleEnum.Spo2, 2);
                moduleHardware.post(ModuleEnum.Weight, 2);
                moduleHardware.post(ModuleEnum.Ecg, 2);
                moduleHardware.post(ModuleEnum.Nibp, 2);
                moduleHardware.post(ModuleEnum.Co2, 2);
                moduleHardware.post(ModuleEnum.Wake, 2);
                moduleHardware.post(ModuleEnum.Mat, 2);

                moduleHardware.post(ModuleEnum.Ambient, 2);
                moduleHardware.post(ModuleEnum.Camera, 2);
                moduleHardware.post(ModuleEnum.Blue, 2);
                moduleHardware.post(ModuleEnum.Mute, 2);
                moduleHardware.post(ModuleEnum.Angle, 2);

                moduleHardware.setDeviceModel(MODEL);
                systemModel.systemInitState.post(1);
            }
        });
    }

    @Override
    protected String getRequestString() {
        return "MODULE HARDWARE";
    }
}