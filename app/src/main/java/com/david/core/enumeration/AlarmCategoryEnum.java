package com.david.core.enumeration;

public enum AlarmCategoryEnum {
    Spo2_Con(AlarmGroupEnum.S, "SEN"),
    Spo2_Sys(AlarmGroupEnum.S, "SYS"),
    Spo2_Bf(AlarmGroupEnum.S, "BF"),
    Spo2_Df(AlarmGroupEnum.S, "DF"),

    None;

    private final AlarmGroupEnum alarmGroupEnum;
    private final String category;

    AlarmCategoryEnum() {
        alarmGroupEnum = null;
        this.category = null;
    }

    AlarmCategoryEnum(AlarmGroupEnum alarmGroupEnum, String category) {
        this.alarmGroupEnum = alarmGroupEnum;
        this.category = category;
    }

    public AlarmGroupEnum getAlarmGroupEnum() {
        return alarmGroupEnum;
    }

    public String getCategory() {
        return category;
    }
}
