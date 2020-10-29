package com.david.core.model;

import com.david.core.util.LazyLiveData;

public class BaseSensorModel {

    private boolean alarmEnabled;

    public final LazyLiveData<Boolean> systemAlarmed = new LazyLiveData<>(false);

    public BaseSensorModel() {
    }

    public boolean isAlarmEnabled() {
        return alarmEnabled;
    }

    public void setAlarmEnabled() {
        this.alarmEnabled = true;
    }
}
