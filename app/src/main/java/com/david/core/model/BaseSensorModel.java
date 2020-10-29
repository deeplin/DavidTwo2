package com.david.core.model;

import com.david.core.enumeration.AlarmGroupEnum;
import com.david.core.enumeration.AlarmPassThroughEnum;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.util.BitUtil;
import com.david.core.util.ContextUtil;
import com.david.core.util.LazyLiveData;

import javax.inject.Inject;

public class BaseSensorModel {

    @Inject
    IncubatorCommandSender incubatorCommandSender;

    private boolean alarmEnabled = true;

    private final LazyLiveData<Integer> systemAlarm = new LazyLiveData<>(0);
    private final LazyLiveData<Integer>[] passThroughAlarmArray;

    public BaseSensorModel(AlarmPassThroughEnum startAlarmPassThroughEnum, int passThroughAlarmNum) {
        ContextUtil.getComponent().inject(this);
        passThroughAlarmArray = new LazyLiveData[passThroughAlarmNum];
        for (int index = 0; index < passThroughAlarmNum; index++) {
            final int currentIndex = startAlarmPassThroughEnum.ordinal() + index;
            passThroughAlarmArray[index] = new LazyLiveData<>(0);
            passThroughAlarmArray[index].observeForever(integer -> {
                AlarmPassThroughEnum alarmPassThroughEnum = AlarmPassThroughEnum.values()[currentIndex];
                AlarmGroupEnum alarmGroupEnum = alarmPassThroughEnum.getAlarmGroupEnum();
                incubatorCommandSender.setAlarmExcCommand(alarmGroupEnum.name(), alarmPassThroughEnum.getCategory(), integer);
            });
        }
    }

    public boolean isAlarmEnabled() {
        return alarmEnabled;
    }

    public void setAlarmEnabled() {
        this.alarmEnabled = true;
    }

    public void setAlarm(AlarmPassThroughEnum alarmPassThroughEnum, int value) {
        if (alarmEnabled) {
            int index = alarmPassThroughEnum.getIndex();
            passThroughAlarmArray[index].post(value);
            systemAlarm.post(BitUtil.setBit(systemAlarm.getValue(), index, value > 0));
        }
    }
}
