package com.david.incubator.control;

import androidx.lifecycle.Observer;

import com.david.core.alarm.AlarmControl;
import com.david.core.control.ConfigRepository;
import com.david.core.control.GpioControl;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandControl;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.serial.incubator.command.ctrl.CtrlGetCommand;
import com.david.core.serial.incubator.command.other.LedCommand;
import com.david.core.serial.spo2.Spo2CommandControl;
import com.david.core.serial.spo2.command.BoardInfoCommand;
import com.david.core.serial.spo2.command.SetBaudrateCommand;
import com.david.core.util.Constant;
import com.david.core.util.GpioUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.LoggerUtil;
import com.david.incubator.serial.ecg.EcgCommandControl;
import com.david.incubator.serial.ecg.EcgCommandParser;
import com.david.incubator.serial.ecg.EcgCommandSender;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;

/**
 * author: Ling Lin
 * created on: 2017/7/15 13:35
 * email: 10525677@qq.com
 * description: 自动控制类
 */

@Singleton
public class AutomationControl implements ILifeCycle {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    SystemModel systemModel;
    @Inject
    IncubatorCommandControl incubatorCommandControl;
    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    CtrlGetCommand ctrlGetCommand;
    @Inject
    ConfigRepository configRepository;
    @Inject
    AlarmControl alarmControl;
    @Inject
    Spo2CommandControl spo2CommandControl;
    @Inject
    EcgCommandControl ecgCommandControl;
    @Inject
    EcgCommandSender ecgCommandSender;
    @Inject
    EcgCommandParser ecgCommandParser;
    @Inject
    GpioControl gpioControl;
    @Inject
    SensorModelRepository sensorModelRepository;
//    @Inject
//    Co2CommandControl co2CommandControl;
//    @Inject
//    NibpCommandControl nibpCommandControl;
//    @Inject
//    PrintCommandControl printCommandControl;
//    @Inject
//    WakeCommandControl wakeCommandControl;

    private Observer<Integer> startObserver;
    private final Observer<Boolean> spo2ErrorObserver;

    private final Observer<Integer> hrPrSourceObserver;
    private final Observer<Integer> hrPrIntegerObserver;

    private final Observer<Integer> respSourceObserver;
    private final Observer<Integer> respIntegerObserver;

    @Inject
    public AutomationControl() {
        spo2ErrorObserver = aBoolean -> connectSpo2();

        hrPrIntegerObserver = integer -> {
            if (moduleHardware.isActive(ModuleEnum.Ecg)) {
                SensorModel ecgHrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgHr);
                if (ecgHrModel.textNumber.getValue() > 0) {
                    systemModel.selectHr.set(true);
                    return;
                }
            }
            if (moduleHardware.isActive(ModuleEnum.Spo2)) {
                SensorModel prModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pr);
                if (prModel.textNumber.getValue() > 0) {
                    systemModel.selectHr.set(false);
                    return;
                }
            }
            systemModel.selectHr.set(null);
        };

        hrPrSourceObserver = integer -> {
            SensorModel ecgHrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgHr);
            SensorModel prModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pr);
            switch (integer) {
                case (0):
                    ecgHrModel.textNumber.removeObserver(hrPrIntegerObserver);
                    prModel.textNumber.removeObserver(hrPrIntegerObserver);
                    if (moduleHardware.isActive(ModuleEnum.Ecg)) {
                        systemModel.selectHr.set(true);
                    } else {
                        systemModel.selectHr.set(null);
                    }
                    break;
                case (1):
                    ecgHrModel.textNumber.removeObserver(hrPrIntegerObserver);
                    prModel.textNumber.removeObserver(hrPrIntegerObserver);
                    if (moduleHardware.isActive(ModuleEnum.Spo2)) {
                        systemModel.selectHr.set(false);
                    } else {
                        systemModel.selectHr.set(null);
                    }
                    break;
                case (2):
                    ecgHrModel.textNumber.observeForever(hrPrIntegerObserver);
                    prModel.textNumber.observeForever(hrPrIntegerObserver);
                    break;
            }
        };

        respIntegerObserver = integer -> {
            if (moduleHardware.isActive(ModuleEnum.Co2)) {
                SensorModel co2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Co2);
                if (co2Model.textNumber.getValue() > 0) {
                    systemModel.selectCo2.set(true);
                    return;
                }
            }
            if (moduleHardware.isActive(ModuleEnum.Ecg)) {
                SensorModel ecgRrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr);
                if (ecgRrModel.textNumber.getValue() > 0) {
                    systemModel.selectCo2.set(false);
                    return;
                }
            }
            systemModel.selectCo2.set(null);
        };

        respSourceObserver = integer -> {
            SensorModel ecgRrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr);
            SensorModel co2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Co2);
            switch (integer) {
                case (0):
                    ecgRrModel.textNumber.removeObserver(respIntegerObserver);
                    co2Model.textNumber.removeObserver(respIntegerObserver);
                    if (moduleHardware.isActive(ModuleEnum.Ecg)) {
                        systemModel.selectCo2.set(false);
                    } else {
                        systemModel.selectCo2.set(null);
                    }
                    break;
                case (1):
                    ecgRrModel.textNumber.removeObserver(respIntegerObserver);
                    co2Model.textNumber.removeObserver(respIntegerObserver);
                    if (moduleHardware.isActive(ModuleEnum.Co2)) {
                        systemModel.selectCo2.set(true);
                    } else {
                        systemModel.selectCo2.set(null);
                    }
                    break;
                case (2):
                    ecgRrModel.textNumber.observeForever(respIntegerObserver);
                    co2Model.textNumber.observeForever(respIntegerObserver);
                    break;
            }
        };

        startObserver = integer -> {
            switch (integer) {
                case (1):
                    ctrlGetCommand.initCallback();
                    incubatorCommandSender.getCtrlGet();
                    LoggerUtil.se(" state1 " + integer);
                    break;
                case (2):

                    LoggerUtil.se(" state2 " + integer);
                    systemModel.systemInitState.removeObserver(startObserver);
                    initConfig();
                    synchronizeConfig();
                    openSerialPort();
                    systemModel.systemInitState.set(3);

                    setSource();
                    break;
            }
        };
    }

    @Override
    public void attach() {
        systemModel.systemInitState.observeForever(startObserver);
        incubatorCommandSender.getHardwareModule();
        alarmControl.attach();
    }

    @Override
    public void detach() {
        //todo
//        spo2CommandControl.errorOccur.removeObserver(spo2ErrorObserver);

        alarmControl.detach();

        configRepository.getConfig(ConfigEnum.RespSource).removeObserver(respSourceObserver);
        configRepository.getConfig(ConfigEnum.EcgHrPrSource).removeObserver(hrPrSourceObserver);

        closeSerialPort();

        incubatorCommandControl.detach();

        gpioControl.detach();
        systemModel.systemInitState.removeObserver(startObserver);
    }

    private void initConfig() {
        GpioUtil.init();
        gpioControl.attach();
    }

    private void synchronizeConfig() {
        incubatorModel.above37.observeForever(aBoolean1 -> incubatorCommandSender.setLED(LedCommand.LED37, aBoolean1, null));
        incubatorCommandSender.setStandBy(false);
        incubatorCommandSender.setAlarmVolume(configRepository.getConfig(ConfigEnum.AlarmVolume).getValue(), null);
        incubatorCommandSender.setDemo(false);
        incubatorCommandControl.attach();
    }

    private void openSerialPort() {
        if (moduleHardware.isInstalled(ModuleEnum.Spo2)) {
            spo2CommandControl.init();
            spo2CommandControl.errorOccur.observeForever(spo2ErrorObserver);
        }

        if (moduleHardware.isInstalled(ModuleEnum.Ecg)) {
            try {
                ecgCommandParser.init();
                ecgCommandControl.init();
                ecgCommandControl.open(Constant.ECG_COM_ID, Constant.ECG_BAUDRATE);
                ecgCommandControl.setFunction(ecgCommandParser);
            } catch (Exception e) {
                LoggerUtil.e(e);
            }
        }

        if (moduleHardware.isInstalled(ModuleEnum.Co2)) {
//            try {
//                co2CommandControl.init();
//                co2CommandControl.open(Constant.CO2_COM_ID, Constant.CO2_BAUDRATE);
//            } catch (Exception e) {
//                LoggerUtil.e(e);
//            }
        }

        if (moduleHardware.isInstalled(ModuleEnum.Nibp)) {
//            try {
//                nibpCommandControl.init();
//                nibpCommandControl.open(Constant.NIBP_COM_ID, Constant.NIBP_BAUDRATE);
//            } catch (Exception e) {
//                LoggerUtil.e(e);
//            }
        }
        if (moduleHardware.isInstalled(ModuleEnum.Wake)) {
//            try {
//                wakeCommandControl.init();
//                wakeCommandControl.open(Constant.WAKE_COM_ID, Constant.WAKE_BAUDRATE);
//            } catch (Exception e) {
//                LoggerUtil.e(e);
//            }
        }

//        try {
//            printCommandControl.init();
//            printCommandControl.open(Constant.PRINT_COM_ID, Constant.PRINT_BAUDRATE);
//            printCommandControl.attach();
//        } catch (Exception e) {
//            LoggerUtil.e(e);
//        }
    }

    private void connectSpo2() {
        try {
            spo2CommandControl.open(Constant.SPO2_COM_ID, Constant.SPO2_BAUDRATE_SLOW_RATE);

            Observable.timer(500, TimeUnit.MILLISECONDS).subscribe(aLong -> spo2CommandControl.produce(new SetBaudrateCommand()));

            Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> spo2CommandControl.open(Constant.SPO2_COM_ID, Constant.SPO2_BAUDRATE));

            Observable.timer(1500, TimeUnit.MILLISECONDS).subscribe(aLong -> spo2CommandControl.produce(new BoardInfoCommand()));
        } catch (Exception e) {
            LoggerUtil.e(e);
        }
    }

    private void closeSerialPort() {
//        if (moduleHardware.wake.getValue()) {
//            wakeCommandControl.close();
//        }
//
//        if (moduleHardware.nibp.getValue()) {
//            nibpCommandControl.close();
//        }
//
//        if (moduleHardware.co2.getValue()) {
//            co2CommandControl.close();
//        }
//
        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            spo2CommandControl.close();
        }

        if (moduleHardware.isActive(ModuleEnum.Ecg)) {
            ecgCommandControl.close();
        }
//
//        printCommandControl.close();
//        printCommandControl.detach();
    }

    private void setSource() {
        configRepository.getConfig(ConfigEnum.EcgHrPrSource).observeForever(hrPrSourceObserver);
        configRepository.getConfig(ConfigEnum.RespSource).observeForever(respSourceObserver);
    }
}