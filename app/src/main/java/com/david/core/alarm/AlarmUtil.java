package com.david.core.alarm;

import com.david.core.enumeration.AlarmGroupEnum;
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

    public static boolean isFromCabin(AlarmGroupEnum alarmGroupEnum, AlarmModel alarmModel) {
        switch (alarmGroupEnum) {
            case I:
            case P:
            case W:
                return true;
            case S:
                //todo deeplin
                return alarmModel.getCategory() == null;
            default:
                return false;
        }
    }
}
