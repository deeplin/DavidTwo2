package com.david.incubator.serial.co2;

import androidx.lifecycle.Observer;

import com.david.core.buffer.BufferRepository;
import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.Co2Model;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.BaseCommand;
import com.david.core.serial.BaseSerialControl;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.CrcUtil;
import com.david.core.util.LoggerUtil;
import com.david.core.util.NumberUtil;
import com.david.core.util.UnitUtil;
import com.david.incubator.serial.co2.command.Co2CalibrationCommand;
import com.david.incubator.serial.co2.command.Co2MeasureCommand;
import com.david.incubator.serial.co2.command.Co2N2oCompensateCommand;
import com.david.incubator.serial.co2.command.Co2O2CompensateCommand;
import com.david.incubator.serial.co2.command.Co2SetApneCommand;
import com.david.incubator.serial.co2.command.Co2SleepCommand;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.functions.BiConsumer;

@Singleton
public class Co2CommandControl extends BaseSerialControl {

    @Inject
    Co2ResponseCommand co2ResponseCommand;
    @Inject
    Co2Model co2Model;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    ConfigRepository configRepository;
    @Inject
    SystemModel systemModel;
    @Inject
    BufferRepository bufferRepository;
    @Inject
    ModuleHardware moduleHardware;

    private final SensorModel co2EtModel;
    private final SensorModel co2RrModel;
    private final SensorModel co2FiModel;

    private final int[] co2Buffer = new int[1];

    @Inject
    public Co2CommandControl() {
        super();
        ContextUtil.getComponent().inject(this);

        co2EtModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2);
        co2RrModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Rr);
        co2FiModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Fi);

        co2Model.atmospheric.observeForever(integer -> {
            co2EtModel.textNumber.notifyChange();
            co2RrModel.textNumber.notifyChange();
            co2FiModel.textNumber.notifyChange();
        });

        moduleHardware.getModule(ModuleEnum.Co2).observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (moduleHardware.isActive(ModuleEnum.Co2)) {
                    /*设置 sleep 模式*/
                    co2Model.mode.set(0);
                } else {
                    /*设置 measurement 模式*/
                    co2Model.mode.set(1);
                }
            }
        });
    }

    protected boolean parseResponse(byte data) {
        co2ResponseCommand.addData(data);
        if (co2ResponseCommand.isCompleted()) {
            handleCommand(co2ResponseCommand);
            co2ResponseCommand.reset();
        }
        return false;
    }

    @Override
    protected void setConnectionError(boolean status) {
        //todo deeplin
//        alarmRepository.produceAlarmFromAndroid(AlarmWordEnum.CO2_CON, status);
        if (status && !systemModel.demo.getValue()) {
            co2RrModel.textNumber.post(Constant.SENSOR_NA_VALUE);
            co2FiModel.textNumber.post(Constant.SENSOR_NA_VALUE);
            co2EtModel.textNumber.post(Constant.SENSOR_NA_VALUE);
        }
    }

    private void handleCommand(Co2ResponseCommand co2ResponseCommand) {
        byte[] buffer = co2ResponseCommand.getResponseBuffer();
        if ((byte) (CrcUtil.computeSumCrc(buffer, 2, buffer.length - 1) + buffer[buffer.length - 1]) == 0) {
            parseCo2CommandString(co2ResponseCommand.getResponseBuffer());
        } else {
            LoggerUtil.e(String.format(Locale.US, "Co2 CRC error. %d %d",
                    CrcUtil.computeSumCrc(buffer, 2, buffer.length - 1),
                    buffer[buffer.length - 1]));
        }
    }

    private void parseCo2CommandString(byte[] buffer) {
        if (!systemModel.demo.getValue()) {
            //20Hz
            co2Buffer[0] = formatUnitValue(NumberUtil.getShortHighFirst(4, buffer));
            bufferRepository.getCo2Buffer().produce(0, 1, co2Buffer);
            byte commandId = buffer[2];
            if (commandId == 0) {
                int value = buffer[14];
                if (value < 0) {
                    value += 256;
                }
                if (value == 255) {
                    value = Constant.SENSOR_NA_VALUE;
                }
                co2FiModel.textNumber.post(formatUnitValue(value));
            } else if (commandId == 1) {
                int value = buffer[14];
                if (value < 0) {
                    value += 256;
                }
                if (value == 255) {
                    value = Constant.SENSOR_NA_VALUE;
                }
                co2EtModel.textNumber.post(formatUnitValue(value));
            } else if (commandId == 3) {
                int value = buffer[14];
                if (value < 0) {
                    value += 256;
                }
                if (value == 255) {
                    value = Constant.SENSOR_NA_VALUE;
                }
                if (value > 7) {
                    //todo deeplin
//                    alarmRepository.enableCo2Mode.set(EnableAlarmEnum.Started);
                }
                co2RrModel.textNumber.post(value);

                int highAtm = buffer[18];
                if (highAtm < 0) {
                    highAtm += 256;
                }
                int lowAtm = buffer[19];
                if (lowAtm < 0) {
                    lowAtm += 256;
                }
                co2Model.atmospheric.post(highAtm * 256 + lowAtm);
            } else if (commandId == 4) {
                setMode(buffer[14]);
                setO2Compensate(buffer[15]);
                co2Model.serException.post((int) buffer[16]);
                co2Model.asrException.post((int) buffer[17]);
                co2Model.dvrException.post((int) buffer[18]);
            } else if (commandId == 6) {
                setN2oCompensate(buffer[17]);
                co2Model.ssrException.post((int) buffer[16]);
            }
        }
    }

    private int formatUnitValue(int value) {
        int unit = configRepository.getConfig(ConfigEnum.Co2Unit).getValue();
        SensorModel co2EtModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2);
        SensorModel fiModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Fi);
        SensorModel ecgRrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr);
        switch (unit) {
            case (0):
                co2EtModel.unit.post("mmHg");
                fiModel.unit.post("mmHg");
                ecgRrModel.unit.post("mmHg");
                return UnitUtil.percentageToMmHg(value, co2Model.atmospheric.getValue());
            case (1):
                co2EtModel.unit.post("kPa");
                fiModel.unit.post("kPa");
                ecgRrModel.unit.post("kPa");
                return UnitUtil.percentageToKPa(value, co2Model.atmospheric.getValue());
            default:
                co2EtModel.unit.post("%");
                fiModel.unit.post("%");
                ecgRrModel.unit.post("%");
                return value;
        }
    }

    private void setMode(byte mode) {
        switch (mode) {
            case (1):
                if (!Objects.equals(co2Model.mode.getValue(), 1)) {
                    sendMeasureCommand();
                }
                break;
            case (2):
                if (!Objects.equals(co2Model.mode.getValue(), 0)) {
                    sendSleepCommand();
                }
                break;
        }
    }

    private void setO2Compensate(byte compensate) {
        int oldValue = configRepository.getConfig(ConfigEnum.Co2O2Compensate).getValue();
        if (compensate != oldValue) {
            sendO2CompensateCommand((byte) oldValue);
        }
    }

    private void setN2oCompensate(byte compensate) {
        int oldValue = configRepository.getConfig(ConfigEnum.Co2N2oCompensate).getValue();
        if (compensate != oldValue) {
            sendN2oCompensateCommand((byte) oldValue);
        }
    }

    public void sendApneCommand(byte data, BiConsumer<Boolean, BaseCommand> onCompleted) {
        Co2SetApneCommand command = new Co2SetApneCommand(data);
        command.setCallback(onCompleted);
        produce(command);
    }

    public void sendCalibration() {
        Co2CalibrationCommand command = new Co2CalibrationCommand();
        produce(command);
    }

    private void sendSleepCommand() {
        Co2SleepCommand command = new Co2SleepCommand();
        produce(command);
    }

    private void sendMeasureCommand() {
        Co2MeasureCommand command = new Co2MeasureCommand();
        produce(command);
    }

    private void sendO2CompensateCommand(byte compensate) {
        Co2O2CompensateCommand command = new Co2O2CompensateCommand(compensate);
        produce(command);
    }

    private void sendN2oCompensateCommand(byte compensate) {
        Co2N2oCompensateCommand command = new Co2N2oCompensateCommand(compensate);
        produce(command);
    }
}