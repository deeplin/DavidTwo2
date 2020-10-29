package com.david.core.alarm;

import com.david.core.enumeration.AlarmPassThroughEnum;
import com.david.core.util.LazyLiveData;

public abstract class BaseSensorAlarmControl {

    public final LazyLiveData<Integer>[] passThroughAlarmArray;

    public BaseSensorAlarmControl() {
        passThroughAlarmArray = new LazyLiveData[passThroughAlarmNum()];
        for (int index = 0; index < passThroughAlarmNum(); index++) {
            passThroughAlarmArray[index] = new LazyLiveData<>(0);
        }
    }

    protected abstract int passThroughAlarmNum();

    public void setAlarm(AlarmPassThroughEnum alarmPassThroughEnum, boolean status) {
        int index = alarmPassThroughEnum.getIndex();

    }
}