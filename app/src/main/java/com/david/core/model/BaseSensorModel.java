package com.david.core.model;

import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.AlarmCategoryEnum;
import com.david.core.enumeration.AlarmGroupEnum;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.enumeration.ModuleEnum;
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

    private boolean alarmEnabled = false;

    private final ModuleEnum moduleEnum;
    private final LazyLiveData<Integer> systemAlarm = new LazyLiveData<>(0);
    private final LazyLiveData<Integer>[] passThroughAlarmArray;
    private final AlarmCategoryEnum rangeCategoryEnum;

    public BaseSensorModel(ModuleEnum moduleEnum, AlarmCategoryEnum rangeCategoryEnum, int passThroughAlarmNum) {
        ContextUtil.getComponent().inject(this);
        this.moduleEnum = moduleEnum;
        this.rangeCategoryEnum = rangeCategoryEnum;
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
            if (integer == 0) {
                AlarmGroupEnum alarmGroupEnum = rangeCategoryEnum.getAlarmGroupEnum();
                incubatorCommandSender.setAlarmExcCommand(alarmGroupEnum.name(),
                        rangeCategoryEnum.getCategory(), passThroughAlarmArray[rangeCategoryEnum.getIndex()].getValue());
            }
        });
    }

    public boolean isAlarmEnabled() {
        return alarmEnabled;
    }

    public void setAlarmEnabled() {
        alarmEnabled = true;
    }

    public void setAlarm(AlarmCategoryEnum alarmCategoryEnum, int value) {
        if (alarmEnabled) {
            int index = alarmCategoryEnum.getIndex();
            passThroughAlarmArray[index].post(value);
            systemAlarm.post(BitUtil.setBit(systemAlarm.getValue(), index, value > 0));
        }
    }

    private void produceAlarm(SensorModel sensorModel, AlarmWordEnum upperAlarmEnum, AlarmWordEnum lowerAlarmEnum) {


//        if (moduleHardware.isActive(moduleEnum)) {
//            int data = sensorModel.textNumber.getValue();
//            if (data > sensorModel.upperLimit.getValue()) {
//
//                upperAlarmEnum.toString()
//
//                alarmRepository.produceAlarmFromAndroid(upperAlarmEnum, true);
//                alarmRepository.produceAlarmFromAndroid(lowerAlarmEnum, false);
//            } else if (data < sensorModel.lowerLimit.getValue()) {
//                alarmRepository.produceAlarmFromAndroid(upperAlarmEnum, false);
//                alarmRepository.produceAlarmFromAndroid(lowerAlarmEnum, true);
//            } else {
//                alarmRepository.produceAlarmFromAndroid(upperAlarmEnum, false);
//                alarmRepository.produceAlarmFromAndroid(lowerAlarmEnum, false);
//            }
//        } else {
////            alarmRepository.produceAlarmFromAndroid(upperAlarmEnum, false);
////            alarmRepository.produceAlarmFromAndroid(lowerAlarmEnum, false);
//        }
    }

    private void setRangeAlarm() {

    }
}
