package com.david.core.serial.incubator;

import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.serial.BaseCommand;
import com.david.core.serial.incubator.command.alarm.AlarmDisable120Command;
import com.david.core.serial.incubator.command.alarm.AlarmDisableCommand;
import com.david.core.serial.incubator.command.alarm.AlarmExcCommand;
import com.david.core.serial.incubator.command.alarm.AlarmGetCommand;
import com.david.core.serial.incubator.command.alarm.AlarmMuteCommand;
import com.david.core.serial.incubator.command.alarm.AlarmResumeCommand;
import com.david.core.serial.incubator.command.alarm.AlarmSetCommand;
import com.david.core.serial.incubator.command.alarm.AlarmVolumeCommand;
import com.david.core.serial.incubator.command.calibration.CalibrateHumCommand;
import com.david.core.serial.incubator.command.calibration.CalibrateOxygenCommand;
import com.david.core.serial.incubator.command.calibration.CalibrateScaleCommand;
import com.david.core.serial.incubator.command.calibration.CalibrateTempCommand;
import com.david.core.serial.incubator.command.calibration.ShowCalibration2Command;
import com.david.core.serial.incubator.command.calibration.ShowCalibrationCommand;
import com.david.core.serial.incubator.command.ctrl.CtrlDemoCommand;
import com.david.core.serial.incubator.command.ctrl.CtrlGetCommand;
import com.david.core.serial.incubator.command.ctrl.CtrlModeCommand;
import com.david.core.serial.incubator.command.ctrl.CtrlOverrideCommand;
import com.david.core.serial.incubator.command.ctrl.CtrlSetCommand;
import com.david.core.serial.incubator.command.ctrl.CtrlStandByCommand;
import com.david.core.serial.incubator.command.module.ModuleHardwareCommand;
import com.david.core.serial.incubator.command.module.ModuleSetCommand;
import com.david.core.serial.incubator.command.other.BackupSetupCommand;
import com.david.core.serial.incubator.command.other.FactoryCommand;
import com.david.core.serial.incubator.command.other.LedCommand;
import com.david.core.serial.incubator.command.other.RestoreSetupCommand;
import com.david.core.serial.incubator.command.other.VersionCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.functions.BiConsumer;

/**
 * author: Ling Lin
 * created on: 2017/7/8 12:23
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class IncubatorCommandSender {

    @Inject
    IncubatorCommandControl incubatorCommandControl;
    @Inject
    CtrlGetCommand ctrlGetCommand;

    @Inject
    public IncubatorCommandSender() {
    }

    public void getHardwareModule() {
        ModuleHardwareCommand moduleHardwareCommand = new ModuleHardwareCommand();
        incubatorCommandControl.produce(moduleHardwareCommand);
    }

    public void setLED(String ledId, boolean status, BiConsumer<Boolean, BaseCommand> onComplete) {
        LedCommand ledCommand = new LedCommand();
        ledCommand.set(ledId, status);
        ledCommand.setCallback(onComplete);
        incubatorCommandControl.produce(ledCommand);
    }

    public void setMute(String alarmWord, int time, BiConsumer<Boolean, BaseCommand> onComplete) {
        AlarmMuteCommand alarmMuteCommand = new AlarmMuteCommand();
        alarmMuteCommand.set(alarmWord, "AB", time);
        alarmMuteCommand.setCallback(onComplete);
        incubatorCommandControl.produce(alarmMuteCommand);
    }

    public void getCtrlGet() {
        incubatorCommandControl.produce(ctrlGetCommand);
    }

    public void setCtrlMode(String ctrlMode, BiConsumer<Boolean, BaseCommand> onComplete) {
        CtrlModeCommand controlModeCommand = new CtrlModeCommand();
        controlModeCommand.setMode(ctrlMode);
        controlModeCommand.setCallback(onComplete);
        incubatorCommandControl.produce(controlModeCommand);
    }

    public void setCtrlSet(String mode, String ctrlMode, int target, BiConsumer<Boolean, BaseCommand> onComplete) {
        if (target >= 0) {
            CtrlSetCommand ctrlSetCommand = new CtrlSetCommand();
            ctrlSetCommand.set(mode, ctrlMode, String.valueOf(target));
            ctrlSetCommand.setCallback(onComplete);
            incubatorCommandControl.produce(ctrlSetCommand);
        }
    }

    public void setModule(boolean status, String sensorName, BiConsumer<Boolean, BaseCommand> onComplete) {
        ModuleSetCommand moduleSetCommand = new ModuleSetCommand();
        moduleSetCommand.set(status, sensorName);
        moduleSetCommand.setCallback(onComplete);
        incubatorCommandControl.produce(moduleSetCommand);
    }

    public void setAlarmConfig(String alertSetting, int value, BiConsumer<Boolean, BaseCommand> onComplete) {
        AlarmSetCommand alarmSetCommand = new AlarmSetCommand();
        alarmSetCommand.set(alertSetting, value);
        alarmSetCommand.setCallback(onComplete);
        incubatorCommandControl.produce(alarmSetCommand);
    }

    public void setAlarmConfig(String alertSetting, int value1, int value2, BiConsumer<Boolean, BaseCommand> onComplete) {
        AlarmSetCommand alarmSetCommand = new AlarmSetCommand();
        alarmSetCommand.set(alertSetting, value1, value2);
        alarmSetCommand.setCallback(onComplete);
        incubatorCommandControl.produce(alarmSetCommand);
    }

    public void clearAlarm(String commandString, BiConsumer<Boolean, BaseCommand> onComplete) {
        AlarmDisableCommand alarmDisableCommand = new AlarmDisableCommand(commandString);
        alarmDisableCommand.setCallback(onComplete);
        incubatorCommandControl.produce(alarmDisableCommand);
    }


    public void clearAlarm120(String commandString, BiConsumer<Boolean, BaseCommand> onComplete) {
        AlarmDisable120Command alarmDisable120Command = new AlarmDisable120Command(commandString);
        alarmDisable120Command.setCallback(onComplete);
        incubatorCommandControl.produce(alarmDisable120Command);
    }

    public void resumeAlarm(String commandString) {
        AlarmResumeCommand alarmResumeCommand = new AlarmResumeCommand(commandString);
        incubatorCommandControl.produce(alarmResumeCommand);
    }

    public void setOxygen(int sensorId, int value, BiConsumer<Boolean, BaseCommand> onComplete) {
        CalibrateOxygenCommand calibrateO2Command = new CalibrateOxygenCommand();
        calibrateO2Command.set(sensorId, value);
        calibrateO2Command.setCallback(onComplete);
        incubatorCommandControl.produce(calibrateO2Command);
    }

    public void setScale(int value, BiConsumer<Boolean, BaseCommand> onComplete) {
        CalibrateScaleCommand calibrateScaleCommand = new CalibrateScaleCommand(value);
        calibrateScaleCommand.setCallback(onComplete);
        incubatorCommandControl.produce(calibrateScaleCommand);
    }

    public void getVersion(BiConsumer<Boolean, BaseCommand> onComplete) {
        VersionCommand versionCommand = new VersionCommand();
        versionCommand.setCallback(onComplete);
        incubatorCommandControl.produce(versionCommand);
    }

    public void setTempCalibration(String id, int value, BiConsumer<Boolean, BaseCommand> onComplete) {
        CalibrateTempCommand calibrateTempCommand = new CalibrateTempCommand();
        calibrateTempCommand.set(id, value);
        calibrateTempCommand.setCallback(onComplete);
        incubatorCommandControl.produce(calibrateTempCommand);
    }

    public void setHumCalibration(int value, BiConsumer<Boolean, BaseCommand> onComplete) {
        CalibrateHumCommand calibrateHumCommand = new CalibrateHumCommand();
        calibrateHumCommand.setValue(value);
        calibrateHumCommand.setCallback(onComplete);
        incubatorCommandControl.produce(calibrateHumCommand);
    }

    public void getCalibration(BiConsumer<Boolean, BaseCommand> onComplete) {
        ShowCalibrationCommand showCalibrationCommand = new ShowCalibrationCommand();
        showCalibrationCommand.setCallback(onComplete);
        incubatorCommandControl.produce(showCalibrationCommand);
    }

    public void getCalibration2(BiConsumer<Boolean, BaseCommand> onComplete) {
        ShowCalibration2Command showCalibration2Command = new ShowCalibration2Command();
        showCalibration2Command.setCallback(onComplete);
        incubatorCommandControl.produce(showCalibration2Command);
    }

    public void getAlert(AlarmWordEnum alarmWordEnum, BiConsumer<Boolean, BaseCommand> onComplete) {
        AlarmGetCommand alarmGetCommand = new AlarmGetCommand();
        alarmGetCommand.setMode(alarmWordEnum);
        alarmGetCommand.setCallback(onComplete);
        incubatorCommandControl.produce(alarmGetCommand);
    }

    public void setCtrlOverheat(String override, BiConsumer<Boolean, BaseCommand> onComplete) {
        CtrlOverrideCommand ctrlOverrideCommand = new CtrlOverrideCommand();
        ctrlOverrideCommand.setMode(override);
        ctrlOverrideCommand.setCallback(onComplete);
        incubatorCommandControl.produce(ctrlOverrideCommand);
    }

    public void factory(BiConsumer<Boolean, BaseCommand> onComplete) {
        FactoryCommand factoryCommand = new FactoryCommand();
        factoryCommand.setCallback(onComplete);
        incubatorCommandControl.produce(factoryCommand);
    }

    public void setStandBy(boolean status) {
        CtrlStandByCommand ctrlStandByCommand = new CtrlStandByCommand();
        ctrlStandByCommand.setStatus(status);
        incubatorCommandControl.produce(ctrlStandByCommand);
    }

    public void setDemo(boolean status) {
        CtrlDemoCommand ctrlDemoCommand = new CtrlDemoCommand(status);
        incubatorCommandControl.produce(ctrlDemoCommand);
    }

    public void setAlarmExcCommand(String group, String category, int value) {
        AlarmExcCommand alarmExcCommand = new AlarmExcCommand(group, category, value);
        incubatorCommandControl.produce(alarmExcCommand);
    }

    public void backupSetup(BiConsumer<Boolean, BaseCommand> onComplete) {
        BackupSetupCommand backupSetupCommand = new BackupSetupCommand();
        backupSetupCommand.setCallback(onComplete);
        incubatorCommandControl.produce(backupSetupCommand);
    }

    public void restoreSetup(BiConsumer<Boolean, BaseCommand> onComplete) {
        RestoreSetupCommand restoreSetupCommand = new RestoreSetupCommand();
        restoreSetupCommand.setCallback(onComplete);
        incubatorCommandControl.produce(restoreSetupCommand);
    }

    public void setAlarmVolume(int volume, BiConsumer<Boolean, BaseCommand> onComplete) {
        AlarmVolumeCommand alarmVolumeCommand = new AlarmVolumeCommand();
        alarmVolumeCommand.setVolume(volume);
        alarmVolumeCommand.setCallback(onComplete);
        incubatorCommandControl.produce(alarmVolumeCommand);
    }
}