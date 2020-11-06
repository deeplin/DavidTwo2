package com.david.core.enumeration;

public enum AlarmCategoryEnum {
    Spo2_Range(0, AlarmGroupEnum.S, "HL"),
    Spo2_Con(1, AlarmGroupEnum.S, "SEN"),
    Spo2_Sys(2, AlarmGroupEnum.S, "SYS"),
    Spo2_Bfc(3, AlarmGroupEnum.S, "BFC"),
    Spo2_Df(4, AlarmGroupEnum.S, "DF"),
    Spo2_Sp(5, AlarmGroupEnum.S, "SP"),

    Ecg_Range(0, AlarmGroupEnum.E, "HL"),
    Ecg_Con(1, AlarmGroupEnum.E, "SEN"),

    Co2_Range(0, AlarmGroupEnum.C, "HL"),
    Co2_Sen(1, AlarmGroupEnum.C, "SEN"),
    Co2_Ser(2, AlarmGroupEnum.C, "SER"),
    Co2_Asr(3, AlarmGroupEnum.C, "ASR"),
    Co2_Dvr(4, AlarmGroupEnum.C, "DVR"),
    Co2_Ssr(5, AlarmGroupEnum.C, "SSR"),

    Nibp_Range(0, AlarmGroupEnum.N, "HL"),
    Nibp_Con(0, AlarmGroupEnum.N, "SEN"),
    Nibp_Sys(0, AlarmGroupEnum.N, "SYS");

    private final int index;
    private final AlarmGroupEnum alarmGroupEnum;
    private final String category;

    AlarmCategoryEnum(int index, AlarmGroupEnum alarmGroupEnum, String category) {
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
