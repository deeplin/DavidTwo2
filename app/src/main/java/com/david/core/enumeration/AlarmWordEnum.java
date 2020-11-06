package com.david.core.enumeration;

public enum AlarmWordEnum {

    /*Incubator*/
    SYS_CON,
    SYS_TANK,
    SYS_UPS,
    SYS_BAT,

    TEMP_DEVH,
    TEMP_DEVL,
    HUM_DEVH,
    HUM_DEVL,
    O2_DEVH,
    O2_DEVL,

    AIR_OVH,
    FLOW_OVH,
    SKIN_OVH,
    SYS_FAN,

    /*Spo2*/
    SP_OVH,
    SP_OVL,
    PR_OVH,
    PR_OVL,
    PI_OVH,
    PI_OVL,
    HB_OVH,
    HB_OVL,
    OC_OVH,
    OC_OVL,
    MET_OVH,
    MET_OVL,
    CO_OVH,
    CO_OVL,
    PVI_OVH,
    PVI_OVL,
    SPO2_LOW,

    /*Ecg*/
    ECG_CON,
    ECG_DROP,
    ECG_VDROP,
    ECG_OV,

    ECG_APN,
    ECG_ASY,
    ECG_VEN,

    ECG_HRH,
    ECG_HRL,
    ECG_RRH,
    ECG_RRL,

    /*Co2*/
    CO2_CON,

    CO2_ETH,
    CO2_ETL,
    CO2_BRH,
    CO2_BRL,
    CO2_FIH,
    CO2_FIL,

    /*Nibp*/
    NIBP_CON,
    NIBP_SH,
    NIBP_SL,
    NIBP_DH,
    NIBP_DL,
    NIBP_MH,
    NIBP_ML,

    /*Printer*/
    PRINT_ERR,
    PRINT_PAPER,

    /*Wake*/
    WAKE_CON,
    WAKE_ERR;

    private final String alarmWord;

    AlarmWordEnum() {
        this.alarmWord = name().replace("_", ".");
    }

    @Override
    public String toString() {
        return alarmWord;
    }
}