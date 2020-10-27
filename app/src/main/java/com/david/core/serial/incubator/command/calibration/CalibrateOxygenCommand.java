package com.david.core.serial.incubator.command.calibration;

import com.david.core.serial.incubator.BaseIncubatorCommand;

import java.util.Locale;

public class CalibrateOxygenCommand extends BaseIncubatorCommand {

    private int sensorId;
    private int targetValue;

    public CalibrateOxygenCommand() {
        super();
    }

    public void set(int sensorId, int targetValue) {
        this.sensorId = sensorId;
        this.targetValue = targetValue;
    }

    @Override
    protected String getRequestString() {
        return String.format(Locale.US, "CAL O2 %d %d", sensorId, targetValue);
    }

    protected String getCommandId() {
        return String.format(Locale.US, "%s%d", getClass().getSimpleName(), sensorId);
    }
}