package com.david.core.util;

import com.david.core.util.rely.FormatUtil;

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

    public static int percentageToMmHg(int percentage) {
        return (int) (percentage * 7.5f * 1013 / 10000 + 0.5);
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

    public static int percentageToKPa(int percentage) {
        return (int) (percentage * 1013 / 1000f + 0.5);
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

    public static int kPaToPercentage(int percentage) {
        return (int) (percentage / 1013f * 1000 + 0.5);
    }

    public static int mmHgToPercentage(int percentage) {
        return (int) (percentage / 7.5f / 1013 * 10000 + 0.5);
    }

    public static int transparent(int value) {
        return value;
    }
}