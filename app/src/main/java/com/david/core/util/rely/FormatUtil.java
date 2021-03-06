package com.david.core.util.rely;

import com.david.core.enumeration.SensorModelEnum;
import com.david.core.util.Constant;
import com.david.core.util.LazyLiveData;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

public class FormatUtil {

    public static void formatSensorValue(float value, LazyLiveData<String> textIntegerPart, LazyLiveData<String> textDecimalPart,
                                         SensorModelEnum sensorModelEnum) {
        String valueString = basicFormatValue(sensorModelEnum, value);
        if (valueString != null) {
            int index = valueString.indexOf(".");

            if (index > 0) {
                textIntegerPart.set(valueString.substring(0, index));
                textDecimalPart.set(valueString.substring(index));
            } else {
                textIntegerPart.set(valueString);
                textDecimalPart.set(null);
            }
        } else {
            textIntegerPart.set(sensorModelEnum.getErrorString());
            textDecimalPart.set(sensorModelEnum.getDecimalErrorString());
        }
    }

    private static String basicFormatValue(SensorModelEnum sensorModelEnum, float value) {
        if (value <= sensorModelEnum.getDisplayUpper() && value >= sensorModelEnum.getDisplayLower()) {
            DecimalFormat decimalFormat = new DecimalFormat(sensorModelEnum.getFormatString());
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            return decimalFormat.format(value / sensorModelEnum.getDenominator());
        }
        return null;
    }

    public static String formatValue(SensorModelEnum sensorModelEnum, float value) {
        String valueString = basicFormatValue(sensorModelEnum, value);
        if (valueString != null) {
            return valueString;
        }
        return Constant.SENSOR_DEFAULT_ERROR_STRING;
    }

    public static String formatValueUnit(SensorModelEnum sensorModelEnum, float value) {
        String valueString = basicFormatValue(sensorModelEnum, value);
        if (valueString != null) {
            return String.format(Locale.US, "%s %s", valueString, sensorModelEnum.getUnit());
        }
        return Constant.SENSOR_DEFAULT_ERROR_STRING;
    }

    public static String formatValue(double value, int denominator, String formatString) {
        if (value >= 0) {
            DecimalFormat decimalFormat = new DecimalFormat(formatString);
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            return decimalFormat.format(value / denominator);
        } else {
            return Constant.SENSOR_DEFAULT_ERROR_STRING;
        }
    }

    public static String formatBasicValue(double value) {
        return formatValue(value, 1, "0");
    }

    public static String formatTemp(int value) {
        return formatValueUnit(SensorModelEnum.Skin1, value);
    }

    public static String formatHumidity(int value) {
        return formatValueUnit(SensorModelEnum.Humidity, value);
    }

    public static String formatOxygen(int value) {
        return formatValueUnit(SensorModelEnum.Oxygen, value);
    }

    public static String formatSpeed(Integer value) {
        return value + " r/min";
    }

    public static String formatPercentage(int value) {
        return String.format(Locale.US, "%s %%", value);
    }

    public static String formatPr(int value) {
        return formatValueUnit(SensorModelEnum.Pr, value);
    }

    public static String formatPi(int value) {
        return formatValueUnit(SensorModelEnum.Pi, value);
    }

    public static String formatSphb(int value) {
        return formatValueUnit(SensorModelEnum.Sphb, value);
    }

    public static String formatSpoc(int value) {
        return formatValueUnit(SensorModelEnum.Spoc, value);
    }

    public static String formatPvi(int value) {
        return formatValueUnit(SensorModelEnum.Pvi, value);
    }

    public static String formatEcgRr(int value) {
        return formatValueUnit(SensorModelEnum.EcgRr, value);
    }

    public static String formatMmHg(Integer value) {
        return formatValue(value, 1, "0 mmHg");
    }

    public static String formatKPa(Integer value) {
        return formatValue(value, 10, "0.0 kPa");
    }

    public static String formatPercentageBy10(int value) {
        return formatValue(value, 10, "0.0") + " %";
    }
}