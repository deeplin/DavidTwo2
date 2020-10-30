package com.david.incubator.serial.ecg;

import androidx.arch.core.util.Function;

import com.david.core.alarm.AlarmRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.BaseSerialControl;
import com.david.core.util.Constant;
import com.david.core.util.LoggerUtil;
import com.david.core.util.UnsignedByte;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EcgCommandControl extends BaseSerialControl {

    @Inject
    AlarmRepository alarmRepository;
    @Inject
    EcgResponseCommand ecgResponseCommand;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    SystemModel systemModel;

    private EcgCommandMode ecgCommandMode;
    private Function<EcgResponseCommand, Boolean> function;
    private int responseLength;

    @Inject
    public EcgCommandControl() {
        super();
        ecgCommandMode = EcgCommandMode.Init;
    }

    public void setFunction(Function<EcgResponseCommand, Boolean> function) {
        this.function = function;
    }

    protected boolean parseResponse(byte data) {
        boolean status = false;
        switch (data) {
            case (1):
                switch (ecgCommandMode) {
                    case Init:
                        ecgCommandMode = EcgCommandMode.LenLow;
                        break;
                    case LenLow:
                    case Type:
                        ecgCommandMode = EcgCommandMode.Init;
                        break;
                    case LenHigh:
                        ecgCommandMode = EcgCommandMode.LenHigh01;
                        break;
                    case LenHigh01:
                        setLenHigh((byte) 1);
                        ecgCommandMode = EcgCommandMode.Type;
                        break;
                    case Command:
                        ecgCommandMode = EcgCommandMode.Command01;
                        break;
                    case Command01:
                        if (ecgResponseCommand != null) {
                            ecgResponseCommand.addData((byte) 1);
                            if (ecgResponseCommand.isCompleted()) {
                                ecgCommandMode = EcgCommandMode.Init;
                                status = handleCommand();
                            } else {
                                ecgCommandMode = EcgCommandMode.Command;
                            }
                        } else {
                            ecgCommandMode = EcgCommandMode.Init;
                        }
                        break;
                }
                break;
            default:
                switch (ecgCommandMode) {
                    case Init:
                        break;
                    case LenLow:
                        setLenLow(data);
                        ecgCommandMode = EcgCommandMode.LenHigh;
                        break;
                    case LenHigh:
                        setLenHigh(data);
                        ecgCommandMode = EcgCommandMode.Type;
                        break;
                    case LenHigh01:
                        ecgCommandMode = EcgCommandMode.Init;
                        break;
                    case Type:
                        if (responseLength < 0) {
                            LoggerUtil.e("Monitor bufferLength = 0");
                            break;
                        }
                        ecgResponseCommand.reset(responseLength, data);

                        if (ecgResponseCommand.isCompleted()) {
                            ecgCommandMode = EcgCommandMode.Init;
                            status = handleCommand();
                        } else {
                            ecgCommandMode = EcgCommandMode.Command;
                        }
                        break;
                    case Command:
                        if (ecgResponseCommand != null) {
                            ecgResponseCommand.addData(data);
                            if (ecgResponseCommand.isCompleted()) {
                                ecgCommandMode = EcgCommandMode.Init;
                                status = handleCommand();
                            }
                        } else {
                            ecgCommandMode = EcgCommandMode.Init;
                        }
                        break;
                    case Command01:
                        if (ecgResponseCommand != null && ecgResponseCommand.isCompleted()) {
                            status = handleCommand();
                        }
                        setLenLow(data);
                        ecgCommandMode = EcgCommandMode.LenHigh;
                        break;
                }
                break;
        }
        return status;
    }

    @Override
    protected void setConnectionError(boolean status) {
        //todo deeplin
//        alarmRepository.produceAlarmFromAndroid(AlarmWordEnum.ECG_CON, status);
        if (status && !systemModel.demo.getValue()) {
            for (int index = SensorModelEnum.EcgHr.ordinal(); index <= SensorModelEnum.EcgRr.ordinal(); index++) {
                SensorModel sensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.values()[index]);
                sensorModel.textNumber.post(Constant.NA_VALUE);
            }
        }
    }

    private void setLenLow(byte lenLow) {
        responseLength = UnsignedByte.unsignedByteToInt(lenLow) - 4;
    }

    private void setLenHigh(byte high) {
        responseLength += UnsignedByte.unsignedByteToInt(high) * 256;
    }

    private boolean handleCommand() {
        if (ecgResponseCommand.checkCRC()) {
            return function.apply(ecgResponseCommand);
        }
        LoggerUtil.se("ECG CRC error. ");
        return false;
    }
}