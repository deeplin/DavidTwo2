package com.david.core.enumeration;

public enum AlarmWordEnum {

    /*Incubator*/
    SYS_CON(),
    SYS_TANK(),
    SYS_UPS(),
    SYS_BAT(),

    TEMP_DEVH(),
    TEMP_DEVL(),
    HUM_DEVH(),
    HUM_DEVL(),
    O2_DEVH(),
    O2_DEVL(),

    AIR_OVH(),
    FLOW_OVH(),
    SKIN_OVH(),
    SYS_FAN(),

    /*Spo2*/
    SP_OVH(),
    SP_OVL(),
    PR_OVH(),
    PR_OVL(),
    HB_OVH(),
    HB_OVL(),
    OC_OVH(),
    OC_OVL(),
    MET_OVH(),
    MET_OVL(),
    CO_OVH(),
    CO_OVL(),
    PI_OVH(),
    PI_OVL(),
    PVI_OVH(),
    PVI_OVL(),
    SPO2_LOW(),

    /*Ecg*/
    ECG_CON(),
    ECG_DROP(),
    ECG_VDROP(),
    ECG_OV(),

    ECG_APN(),

    ECG_ASY(),

    ECG_VEN(),

    /*Co2*/
    CO2_CON(),

    /*Nibp*/
    NIBP_CON(),

    /*Printer*/
    PRINT_ERR(),
    PRINT_PAPER(),

    /*Wake*/
    WAKE_CON(),
    WAKE_ERR();

    private final String alarmWord;

    AlarmWordEnum() {
        this.alarmWord = name().replace("_", ".");
    }

    @Override
    public String toString() {
        return alarmWord;
    }
}