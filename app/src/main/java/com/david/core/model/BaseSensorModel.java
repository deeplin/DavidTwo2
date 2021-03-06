package com.david.core.model;

import androidx.lifecycle.Observer;

import com.david.core.alarm.AlarmModel;
import com.david.core.alarm.AlarmRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.AlarmCategoryEnum;
import com.david.core.enumeration.AlarmGroupEnum;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.util.BitUtil;
import com.david.core.util.ContextUtil;
import com.david.core.util.LazyLiveData;

import java.util.Objects;

import javax.inject.Inject;

public class BaseSensorModel {

    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    AlarmRepository alarmRepository;

    private boolean alarmEnabled = false;

    private final ModuleEnum moduleEnum;
    private final LazyLiveData<Integer> systemAlarm = new LazyLiveData<>(0);
    private final LazyLiveData<Integer>[] passThroughAlarmArray;

    public BaseSensorModel(ModuleEnum moduleEnum, AlarmCategoryEnum rangeCategoryEnum, int passThroughAlarmNum) {
        ContextUtil.getComponent().inject(this);
        this.moduleEnum = moduleEnum;
        passThroughAlarmArray = new LazyLiveData[passThroughAlarmNum];
        for (int index = 0; index < passThroughAlarmNum; index++) {
            final int enumIndex = rangeCategoryEnum.ordinal() + index;
            passThroughAlarmArray[index] = new LazyLiveData<>(0);
            passThroughAlarmArray[index].observeForever(integer -> {
                AlarmCategoryEnum alarmCategoryEnum = AlarmCategoryEnum.values()[enumIndex];
                AlarmGroupEnum alarmGroupEnum = alarmCategoryEnum.getAlarmGroupEnum();
                if (!Objects.equals(alarmCategoryEnum, rangeCategoryEnum) || systemAlarm.getValue() == 0) {
                    incubatorCommandSender.setAlarmExcCommand(alarmGroupEnum.name(), alarmCategoryEnum.getCategory(), integer);
                }
            });
        }

        systemAlarm.observeForever(integer -> {
            AlarmGroupEnum alarmGroupEnum = rangeCategoryEnum.getAlarmGroupEnum();
            if (integer == 0) {
                incubatorCommandSender.setAlarmExcCommand(alarmGroupEnum.name(),
                        "HL", passThroughAlarmArray[0].getValue());
            } else {
                incubatorCommandSender.setAlarmExcCommand(alarmGroupEnum.name(),
                        "HL", 0);
            }
        });
    }

    public boolean isAlarmEnabled() {
        return alarmEnabled;
    }

    public void setAlarmEnabled() {
        alarmEnabled = true;
    }

    public void setSystemAlarm(AlarmCategoryEnum alarmCategoryEnum, int value) {
        int index = alarmCategoryEnum.getIndex();
        passThroughAlarmArray[index].post(value);
        systemAlarm.post(BitUtil.setBit(systemAlarm.getValue(), index, value > 0));
    }

    public void setSenAlarm(AlarmWordEnum alarmWordEnum, boolean value) {
        AlarmModel alarmModel = alarmRepository.getAlarmModel(alarmWordEnum);
        int bitOffset = alarmModel.getBitOffset();
        passThroughAlarmArray[1].post(BitUtil.setBit(passThroughAlarmArray[1].getValue(), bitOffset, value));
    }

    public void loadRangeAlarm(SensorModelEnum sensorModelEnum, AlarmWordEnum upperAlarmEnum, AlarmWordEnum lowerAlarmEnum) {
        SensorModel sensorModel = sensorModelRepository.getSensorModel(sensorModelEnum);
        Observer<Integer> observer = integer -> testAlarm(sensorModel, upperAlarmEnum, lowerAlarmEnum);
        sensorModel.textNumber.observeForever(observer);
        sensorModel.upperLimit.observeForever(observer);
        sensorModel.lowerLimit.observeForever(observer);
    }

    private void testAlarm(SensorModel sensorModel, AlarmWordEnum upperAlarmEnum, AlarmWordEnum lowerAlarmEnum) {
        if (moduleHardware.isActive(moduleEnum)) {
            int data = sensorModel.textNumber.getValue();
            if (data > sensorModel.upperLimit.getValue()) {
                AlarmModel upperAlarmModel = alarmRepository.getAlarmModel(upperAlarmEnum);
                setRangeBit(upperAlarmModel, true);
                AlarmModel lowerAlarmModel = alarmRepository.getAlarmModel(lowerAlarmEnum);
                setRangeBit(lowerAlarmModel, false);
            } else if (data < sensorModel.lowerLimit.getValue()) {
                AlarmModel upperAlarmModel = alarmRepository.getAlarmModel(upperAlarmEnum);
                setRangeBit(upperAlarmModel, false);
                AlarmModel lowerAlarmModel = alarmRepository.getAlarmModel(lowerAlarmEnum);
                setRangeBit(lowerAlarmModel, true);
            } else {
                AlarmModel upperAlarmModel = alarmRepository.getAlarmModel(upperAlarmEnum);
                setRangeBit(upperAlarmModel, false);
                AlarmModel lowerAlarmModel = alarmRepository.getAlarmModel(lowerAlarmEnum);
                setRangeBit(lowerAlarmModel, false);
            }
        } else {
            AlarmModel upperAlarmModel = alarmRepository.getAlarmModel(upperAlarmEnum);
            setRangeBit(upperAlarmModel, false);
            AlarmModel lowerAlarmModel = alarmRepository.getAlarmModel(lowerAlarmEnum);
            setRangeBit(lowerAlarmModel, false);
        }
    }

    private void setRangeBit(AlarmModel alarmModel, boolean value) {
        if (alarmEnabled) {
            int bitOffset = alarmModel.getBitOffset();
            passThroughAlarmArray[0].post(BitUtil.setBit(passThroughAlarmArray[0].getValue(), bitOffset, value));
        }
    }
}
