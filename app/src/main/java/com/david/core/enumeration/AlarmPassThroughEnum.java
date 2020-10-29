package com.david.core.enumeration;

public enum AlarmPassThroughEnum {
    Spo2_Con(0, AlarmGroupEnum.S, "SEN"),
    Spo2_Sys(1, AlarmGroupEnum.S, "SYS"),
    Spo2_Bf(2, AlarmGroupEnum.S, "BFC"),
    Spo2_Df(3, AlarmGroupEnum.S, "DF"),

    None;

    private final int index;
    private final AlarmGroupEnum alarmGroupEnum;
    private final String category;

    AlarmPassThroughEnum() {
        index = -2;
        alarmGroupEnum = null;
        category = null;
    }

    AlarmPassThroughEnum(int index, AlarmGroupEnum alarmGroupEnum, String category) {
        this.index = index;
        this.alarmGroupEnum = alarmGroupEnum;
        this.category = category;
    }

    public int getIndex() {
        return index;
    }

    public AlarmGroupEnum getAlarmGroupEnum() {
        return alarmGroupEnum;
    }

    public String getCategory() {
        return category;
    }
}
