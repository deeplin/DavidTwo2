package com.david.core.serial.incubator.command.repeated;

import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.CommandEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.serial.incubator.BaseIncubatorCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/7/18 16:27
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class AnalogCommand extends BaseIncubatorCommand {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IncubatorModel incubatorModel;

    private int S1A;
    private int S1B;
    private int S2;
    private int S3;
    private int A1;
    private int A2;
    private int A3;
    private int F1;
    private int H1;
    private int O1;
    private int O2;
    private int O3;
    private int SP;
    private int PR;
    private int PI;
    private int VB;
    private int VR;
    private int VU;
    private int T1;
    private int T2;
    private int T3;
    private int M1;
    private int M2;
    private int E1;
    private int E2;

    private int T4;
    private int T5;
    private int R1;
    private int R2;
    private int R3;
    private int R4;
    private int G1;
    private int W1;
    private int SC;

    private int VC;

    private int CVL;
    private int DC12;
    private int DC24;
    private int AC12;
    private int BAT1_CAP;
    private int BAT2_CAP;
    private int BAT1_VOL;
    private int BAT2_VOL;
    private int BAT_LINK;
    private int BAT_FT0;
    private int BAT_FT1;
    private int RPM;
    private int VR1;
    private int INC_OFF;
    private int HEAT_FT;

    private SensorModel skin1SensorModel;
    private SensorModel skin2SensorModel;
    private SensorModel airSensorModel;
    private SensorModel humiditySensorModel;
    private SensorModel oxygenSensorModel;
    private SensorModel weightSensorModel;
    private SensorModel angleSensorModel;

    @Inject
    public AnalogCommand() {
        super(CommandEnum.Repeated);
    }

    @Override
    protected String getRequestString() {
        return "ANALOG ALL";
    }

    public void initCallback() {
        skin1SensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
        skin2SensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin2);
        airSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
        humiditySensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Humidity);
        oxygenSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen);
        weightSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Weight);
        angleSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Angle);

        setCallback((aBoolean, baseCommand) -> {
            if (aBoolean) {
                skin1SensorModel.textNumber.post(S1A);
                skin2SensorModel.textNumber.post(S2);
                airSensorModel.textNumber.post(A1);
                humiditySensorModel.textNumber.post(H1);
                oxygenSensorModel.textNumber.post(O1);
                weightSensorModel.textNumber.post(SC);
                angleSensorModel.textNumber.post(Math.abs(G1));

                incubatorModel.VU.post(VU);
                incubatorModel.M1.post(M1);
                incubatorModel.M2.post(M2);
                incubatorModel.VB.post(VB);
                incubatorModel.E1.post(E1);
                incubatorModel.W1.post(W1);
            }
        });
    }

    public int getS1A() {
        return this.S1A;
    }

    public void setS1A(int S1A) {
        this.S1A = S1A;
    }

    public int getS1B() {
        return this.S1B;
    }

    public void setS1B(int S1B) {
        this.S1B = S1B;
    }

    public int getS2() {
        return this.S2;
    }

    public void setS2(int S2) {
        this.S2 = S2;
    }

    public int getS3() {
        return this.S3;
    }

    public void setS3(int S3) {
        this.S3 = S3;
    }

    public int getA1() {
        return this.A1;
    }

    public void setA1(int A1) {
        this.A1 = A1;
    }

    public int getA2() {
        return this.A2;
    }

    public void setA2(int A2) {
        this.A2 = A2;
    }

    public int getA3() {
        return this.A3;
    }

    public void setA3(int A3) {
        this.A3 = A3;
    }

    public int getF1() {
        return this.F1;
    }

    public void setF1(int F1) {
        this.F1 = F1;
    }

    public int getH1() {
        return this.H1;
    }

    public void setH1(int H1) {
        this.H1 = H1;
    }

    public int getO1() {
        return this.O1;
    }

    public void setO1(int O1) {
        this.O1 = O1;
    }

    public int getO2() {
        return this.O2;
    }

    public void setO2(int O2) {
        this.O2 = O2;
    }

    public int getO3() {
        return this.O3;
    }

    public void setO3(int O3) {
        this.O3 = O3;
    }

    public int getSP() {
        return this.SP;
    }

    public void setSP(int SP) {
        this.SP = SP;
    }

    public int getPR() {
        return this.PR;
    }

    public void setPR(int PR) {
        this.PR = PR;
    }

    public int getPI() {
        return this.PI;
    }

    public void setPI(int PI) {
        this.PI = PI;
    }

    public int getVB() {
        return this.VB;
    }

    public void setVB(int VB) {
        this.VB = VB;
    }

    public int getVR() {
        return this.VR;
    }

    public void setVR(int VR) {
        this.VR = VR;
    }

    public int getVU() {
        return this.VU;
    }

    public void setVU(int VU) {
        this.VU = VU;
    }

    public int getT1() {
        return this.T1;
    }

    public void setT1(int T1) {
        this.T1 = T1;
    }

    public int getT2() {
        return this.T2;
    }

    public void setT2(int T2) {
        this.T2 = T2;
    }

    public int getT3() {
        return this.T3;
    }

    public void setT3(int T3) {
        this.T3 = T3;
    }

    public int getSC() {
        return SC;
    }

    public void setSC(int SC) {
        this.SC = SC;
    }

    public int getM1() {
        return M1;
    }

    public void setM1(int m1) {
        M1 = m1;
    }

    public int getM2() {
        return M2;
    }

    public void setM2(int m2) {
        M2 = m2;
    }

    public int getE1() {
        return this.E1;
    }

    public void setE1(int E1) {
        this.E1 = E1;
    }

    public int getE2() {
        return this.E2;
    }

    public void setE2(int E2) {
        this.E2 = E2;
    }

    public int getT4() {
        return this.T4;
    }

    public void setT4(int T4) {
        this.T4 = T4;
    }

    public int getT5() {
        return this.T5;
    }

    public void setT5(int T5) {
        this.T5 = T5;
    }

    public int getR1() {
        return this.R1;
    }

    public void setR1(int R1) {
        this.R1 = R1;
    }

    public int getR2() {
        return this.R2;
    }

    public void setR2(int R2) {
        this.R2 = R2;
    }

    public int getR3() {
        return this.R3;
    }

    public void setR3(int R3) {
        this.R3 = R3;
    }

    public int getR4() {
        return this.R4;
    }

    public void setR4(int R4) {
        this.R4 = R4;
    }

    public int getG1() {
        return this.G1;
    }

    public void setG1(int G1) {
        this.G1 = G1;
    }

    public int getW1() {
        return W1;
    }

    public void setW1(int w1) {
        W1 = w1;
    }

    public int getCVL() {
        return CVL;
    }

    public void setCVL(int CVL) {
        this.CVL = CVL;
    }

    public int getDC12() {
        return DC12;
    }

    public void setDC12(int DC12) {
        this.DC12 = DC12;
    }

    public int getDC24() {
        return DC24;
    }

    public void setDC24(int DC24) {
        this.DC24 = DC24;
    }

    public int getAC12() {
        return AC12;
    }

    public void setAC12(int AC12) {
        this.AC12 = AC12;
    }

    public int getBAT1_CAP() {
        return BAT1_CAP;
    }

    public void setBAT1_CAP(int BAT1_CAP) {
        this.BAT1_CAP = BAT1_CAP;
    }

    public int getBAT2_CAP() {
        return BAT2_CAP;
    }

    public void setBAT2_CAP(int BAT2_CAP) {
        this.BAT2_CAP = BAT2_CAP;
    }

    public int getBAT1_VOL() {
        return BAT1_VOL;
    }

    public void setBAT1_VOL(int BAT1_VOL) {
        this.BAT1_VOL = BAT1_VOL;
    }

    public int getBAT2_VOL() {
        return BAT2_VOL;
    }

    public void setBAT2_VOL(int BAT2_VOL) {
        this.BAT2_VOL = BAT2_VOL;
    }

    public int getBAT_LINK() {
        return BAT_LINK;
    }

    public void setBAT_LINK(int BAT_LINK) {
        this.BAT_LINK = BAT_LINK;
    }

    public int getBAT_FT0() {
        return BAT_FT0;
    }

    public void setBAT_FT0(int BAT_FT0) {
        this.BAT_FT0 = BAT_FT0;
    }

    public int getBAT_FT1() {
        return BAT_FT1;
    }

    public void setBAT_FT1(int BAT_FT1) {
        this.BAT_FT1 = BAT_FT1;
    }

    public int getRPM() {
        return RPM;
    }

    public void setRPM(int RPM) {
        this.RPM = RPM;
    }

    public int getVR1() {
        return VR1;
    }

    public void setVR1(int VR1) {
        this.VR1 = VR1;
    }

    public int getINC_OFF() {
        return INC_OFF;
    }

    public void setINC_OFF(int INC_OFF) {
        this.INC_OFF = INC_OFF;
    }

    public int getHEAT_FT() {
        return HEAT_FT;
    }

    public void setHEAT_FT(int HEAT_FT) {
        this.HEAT_FT = HEAT_FT;
    }

    public int getVC() {
        return VC;
    }

    public void setVC(int VC) {
        this.VC = VC;
    }

}