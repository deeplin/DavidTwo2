package com.david.core.serial.incubator.command.ctrl;

import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.CtrlEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.BaseIncubatorCommand;
import com.david.core.util.Constant;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/26 16:21
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class CtrlGetCommand extends BaseIncubatorCommand {

    @Inject
    IncubatorModel incubatorModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    SystemModel systemModel;

    private String ctrl;
    private int c_air;
    private int c_hum;
    private int c_o2;
    private int c_skin;
    private int w_skin;
    private int w_man;
    private int w_inc;
    private int s_set;
    private int a_set;
    private int w_set;
    private int w_mat;

    private SensorModel skin1SensorModel;
    private SensorModel skin2SensorModel;
    private SensorModel airSensorModel;
    private SensorModel oxygenSensorModel;
    private SensorModel humiditySensorModel;

    @Inject
    public CtrlGetCommand() {
        super();
    }

    @Override
    protected String getRequestString() {
        return "CTRL GET";
    }

    private void setAbove37(int tempObjective) {
        if (tempObjective > Constant.TEMP_370) {
            incubatorModel.above37.post(true);
        } else {
            incubatorModel.above37.post(false);
        }
    }

    public void initCallback() {
        skin1SensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
        skin2SensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin2);
        airSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
        oxygenSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen);
        humiditySensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Humidity);

        setCallback((aBoolean, baseCommand) -> {
            if (aBoolean) {
                incubatorModel.ctrlMode.post(CtrlEnum.valueOf(ctrl));
                incubatorModel.manualObjective.post(w_man / 5 * 5);
                skin1SensorModel.objective.post(c_skin);
                skin2SensorModel.objective.post(w_skin);
                airSensorModel.objective.post(c_air);
                oxygenSensorModel.objective.post(c_o2);
                humiditySensorModel.objective.post(c_hum);

                if (incubatorModel.isSkin()) {
                    setAbove37(c_skin);
                } else if (incubatorModel.isAir()) {
                    setAbove37(c_air);
                } else {
                    incubatorModel.above37.post(false);
                }
                if (systemModel.systemInitState.getValue() < 2) {
                    systemModel.systemInitState.post(2);
                }
            }
        });
    }

    public String getCtrl() {
        return ctrl;
    }

    public void setCtrl(String ctrl) {
        this.ctrl = ctrl;
    }

    public int getC_air() {
        return c_air;
    }

    public void setC_air(int c_air) {
        this.c_air = c_air;
    }

    public int getC_hum() {
        return c_hum;
    }

    public void setC_hum(int c_hum) {
        this.c_hum = c_hum;
    }

    public int getC_o2() {
        return c_o2;
    }

    public void setC_o2(int c_o2) {
        this.c_o2 = c_o2;
    }

    public int getC_skin() {
        return c_skin;
    }

    public void setC_skin(int c_skin) {
        this.c_skin = c_skin;
    }

    public int getW_skin() {
        return w_skin;
    }

    public void setW_skin(int w_skin) {
        this.w_skin = w_skin;
    }

    public int getW_man() {
        return w_man;
    }

    public void setW_man(int w_man) {
        this.w_man = w_man;
    }

    public int getW_inc() {
        return w_inc;
    }

    public void setW_inc(int w_inc) {
        this.w_inc = w_inc;
    }

    public int getS_set() {
        return this.s_set;
    }

    public void setS_set(int s_set) {
        this.s_set = s_set;
    }

    public int getA_set() {
        return this.a_set;
    }

    public void setA_set(int a_set) {
        this.a_set = a_set;
    }

    public int getW_set() {
        return this.w_set;
    }

    public void setW_set(int w_set) {
        this.w_set = w_set;
    }

    public int getW_mat() {
        return this.w_mat;
    }

    public void setW_mat(int w_mat) {
        this.w_mat = w_mat;
    }
}