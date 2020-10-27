package com.david.core.enumeration;

public enum ConfigEnum {

    /*Incubator*/
    /*ComfortZone*/
    ComfortZoneAge(6),
    ComfortZoneGestation(33),
    ComfortZoneWeight(1600),

    /*Hardware*/
    BlueTime(),
    Spo2ModuleConfig(0x0),

    /*Software*/
    ScreenLuminance(5),
    AlarmVolume(1),
    NotificationVolume(5),
    ApgarVolume(5),
    PulseVolume(5),

    /*Trend*/
    TrendIncubatorItem0(),
    TrendIncubatorItem1(1),
    TrendIncubatorItem2(2),

    /*Spo2*/
    TrendSpo2Item0(),
    TrendSpo2Item1(1),
    TrendSpo2Item2(2),

    Spo2Speed(),
    Spo2PulseSound(1),
    Spo2Gain(3),

    /*Ecg*/
    EcgHrPrSource(),
    EcgLeadSetting(),
    EcgSpeed(),
    EcgGain(3),
    Ecg0(),
    Ecg1(1),
    Ecg2(),
    EcgFilterMode(),
    EcgTrapMode(2),
    EcgSmartZone(1),
    EcgPaceCheck(1),

    /*Nibp*/
    NibpUnit(),
    NibpStat(),
    NibpMeasureMode(),
    NibpInterval(),
    NibpInitialSleevePressure(),

    /*Co2*/
    Co2Range(),
    Co2YRange(4),
    Co2ChokeDelay(),
    Co2O2Compensate(21),
    Co2N2oCompensate(),
    Co2Unit(2),
    Co2Speed(),

    /*Resp*/
    RespLeadSetting(),
    RespSource(),
    RespGain(3),
    RespSpeed(),

    /*Wake*/
    WakeVibrationIntensity(50),
    WakeRespStatus(1),
    WakeCo2Status(1),
    WakeHr(100),
    WakeSpo2(85),

    /*Print*/
    DataPrintInterval(),
    Wave0,
    Wave1(1),
    Wave2(2),
    PrintSpeed;

    private int value;

    ConfigEnum() {
        this(0);
    }

    ConfigEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}