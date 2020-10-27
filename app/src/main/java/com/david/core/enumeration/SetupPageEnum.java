package com.david.core.enumeration;

public enum SetupPageEnum {
    Temp(0),
    Humidity(1),
    Oxygen(2),
    Blue(3),
    Mat(4),
    Ecg(5),
    Spo2(6),
    Nibp(7),
    Co2(8),
    Resp(9),
    Wake(10);

    private final int layoutId;

    SetupPageEnum(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getLayoutId() {
        return layoutId;
    }
}