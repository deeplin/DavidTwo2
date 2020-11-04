package com.david.core.util;

import java.text.DecimalFormat;

public class UnitUtil {

    //Co2
    public static int percentageToMmHg(int percentage, int atmospheric) {
        if (percentage > 0) {
            return (int) (percentage * 7.5f * atmospheric / 1000);
        } else {
            return Constant.NA_VALUE;
        }
    }

    public static String percentageToMmHgString(int percentage) {
        float value = percentage * 7.5f * 1013 / 1000;
        return FormatUtil.formatValue(value, 10, "0");
    }

    public static int percentageToKPa(int percentage, int atmospheric) {
        if (percentage > 0) {
            return percentage * atmospheric / 1000;
        } else {
            return Constant.NA_VALUE;
        }
    }

    public static String percentageToKPaString(int percentage) {
        float value = percentage * 1013 / 1000f;
        return FormatUtil.formatValue(value, 10, "0.0");
    }

    public static String percentageString(int percentage) {
        return FormatUtil.formatValue(percentage, 10, "0.0");
    }

    public static String mmHgToKPa(int mmHg) {
        if (mmHg > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            return decimalFormat.format(mmHg / 7.5);
        } else {
            return Constant.SENSOR_DEFAULT_ERROR_STRING;
        }
    }

    public static String intToString(int mmHg) {
        if (mmHg > 0) {
            return String.valueOf(mmHg);
        }
        return Constant.SENSOR_DEFAULT_ERROR_STRING;
    }
}