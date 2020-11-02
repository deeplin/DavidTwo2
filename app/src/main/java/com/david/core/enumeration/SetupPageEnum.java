package com.david.core.enumeration;

public enum SetupPageEnum {
    Temp(0),
    Humidity(1),
    Oxygen(2),
    Mat(3),
    Blue(4),
    Ecg(0),
    Spo2(1),
    Nibp(2),
    Co2(3),
    Resp(4),
    Wake(5);

    private final int layoutId;

    SetupPageEnum(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getLayoutId() {
        return layoutId;
    }
}