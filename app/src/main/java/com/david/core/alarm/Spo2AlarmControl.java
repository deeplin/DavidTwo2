package com.david.core.alarm;

public class Spo2AlarmControl extends BaseSensorAlarmControl {
    public Spo2AlarmControl() {
    }

    @Override
    protected int passThroughAlarmNum() {
        return 4;
    }
}
