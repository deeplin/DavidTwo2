package com.david.core.alarm;

import com.david.core.enumeration.AlarmGroupEnum;
import com.david.core.enumeration.AlarmPriorityEnum;

public class AlarmModel implements Comparable<AlarmModel> {

    private int alarmId;
    private String alarmWord;
    private AlarmGroupEnum alarmGroupEnum;
    private boolean fromCabin;
    private boolean technicalMode;
    private AlarmPriorityEnum alarmPriorityEnum;
    private boolean activeInAndroid;
    private int alarmLastingTime;
    private long version;
    private long startTime;
    private String category;
    private int bitOffset;

    public AlarmModel() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AlarmModel) {
            return (((AlarmModel) obj).alarmId == alarmId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return alarmId;
    }

    @Override
    public int compareTo(AlarmModel alarmModel) {
        return alarmId - alarmModel.alarmId;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmWord() {
        return alarmWord;
    }

    public void setAlarmWord(String alarmWord) {
        this.alarmWord = alarmWord;
    }

    public AlarmGroupEnum getAlarmGroupEnum() {
        return alarmGroupEnum;
    }

    public void setAlarmGroupEnum(AlarmGroupEnum alarmGroupEnum) {
        this.alarmGroupEnum = alarmGroupEnum;
    }

    public boolean isFromCabin() {
        return fromCabin;
    }

    public void setFromCabin(boolean fromCabin) {
        this.fromCabin = fromCabin;
    }

    public boolean isTechnicalMode() {
        return technicalMode;
    }

    public void setTechnicalMode(boolean technicalMode) {
        this.technicalMode = technicalMode;
    }

    public AlarmPriorityEnum getAlarmPriorityEnum() {
        return alarmPriorityEnum;
    }

    public void setAlarmPriorityEnum(AlarmPriorityEnum alarmPriorityEnum) {
        this.alarmPriorityEnum = alarmPriorityEnum;
    }

    public synchronized boolean isActiveInAndroid() {
        return activeInAndroid;
    }

    public synchronized void setActiveInAndroid(boolean activeInAndroid) {
        this.activeInAndroid = activeInAndroid;
    }

    public int getAlarmLastingTime() {
        return alarmLastingTime;
    }

    public void setAlarmLastingTime(int alarmLastingTime) {
        this.alarmLastingTime = alarmLastingTime;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getBitOffset() {
        return bitOffset;
    }

    public void setBitOffset(int bitOffset) {
        this.bitOffset = bitOffset;
    }
}