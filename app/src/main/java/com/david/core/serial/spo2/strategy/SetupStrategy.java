package com.david.core.serial.spo2.strategy;

import com.david.core.serial.spo2.Spo2CommandControl;
import com.david.core.serial.spo2.command.SetExceptionOutputCommand;
import com.david.core.serial.spo2.command.SetupCommand;

import io.reactivex.rxjava3.functions.Consumer;

public class SetupStrategy implements Consumer<byte[]> {

    private final Spo2CommandControl spo2CommandControl;

    public SetupStrategy(Spo2CommandControl spo2CommandControl) {
        this.spo2CommandControl = spo2CommandControl;
    }

    @Override
    public void accept(byte[] buffer) {
        SetupCommand setupCommand = SetupCommand.getInstance();
        byte sensorId = setupCommand.getCommandSensorId();
        if (sensorId < 5) {
            sensorId++;
        } else if (sensorId == 5) {
            sensorId = 7;
        } else if (sensorId == 7) {
            sensorId = 0x2B;
        } else if (sensorId == 0x2B) {
            sensorId = 0x32;
        } else if (sensorId == 0x32) {
            sensorId = 0x33;
        } else if (sensorId > 0x32) {
            setupCommand.setCommandSensorId((byte) 1);
            SetExceptionOutputCommand setExceptionOutputCommand = new SetExceptionOutputCommand();
            spo2CommandControl.produce(setExceptionOutputCommand);
            return;
        }
        setupCommand.setCommandSensorId(sensorId);
        spo2CommandControl.produce(true, setupCommand);
    }
}
