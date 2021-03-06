package com.david.core.alarm;

import com.david.R;
import com.david.core.enumeration.AlarmGroupEnum;
import com.david.core.enumeration.AlarmPriorityEnum;
import com.david.core.util.ContextUtil;

import java.util.Objects;

public class AlarmUtil {
    public static String getAlarmString(String alarmWord) {
        String alertDetail;
        try {
            int resourceId = ContextUtil.getStringsId(alarmWord);
            alertDetail = ContextUtil.getString(resourceId);
        } catch (Exception e) {
            alertDetail = alarmWord;
        }
        return alertDetail;
    }

    public static int getAlarmLastingTime(AlarmGroupEnum alarmGroupEnum, String alarmWord) {
        if (Objects.equals(alarmGroupEnum, AlarmGroupEnum.I)) {
            if (alarmWord.contains("O2") && (!alarmWord.contains("SPO2") && (!alarmWord.contains("CO2")))) {
                return 115;
            } else {
                return 240;
            }
        } else {
            return 120;
        }
    }

    public static int getAlarmColor(AlarmPriorityEnum alarmPriorityEnum) {
        if (Objects.equals(alarmPriorityEnum, AlarmPriorityEnum.H)) {
            return R.color.alarm_high;
        } else if (Objects.equals(alarmPriorityEnum, AlarmPriorityEnum.M)) {
            return R.color.alarm_middle;
        } else if (Objects.equals(alarmPriorityEnum, AlarmPriorityEnum.L)) {
            return R.color.alarm_low;
        } else {
            return R.color.alarm_info;
        }
    }
}
