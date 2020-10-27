package com.david.core.enumeration;

import com.david.R;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;

/**
 * author: Ling Lin
 * created on: 2017/12/27 20:59
 * email: 10525677@qq.com
 * description:
 */
public enum SensorModelEnum {

    /*Incubator*/
    Skin1("S1A", R.string.skin, "℃", 340, 380, 0, 700,
            20, 40, 4, 21, 10, "0.0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, ".-",
            R.mipmap.skin, R.color.skin1_color),
    Skin2("S2", R.string.skin2, "℃", 320, 380, 0, 700,
            20, 40, 4, 21, 10, "0.0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, ".-",
            R.mipmap.skin2, R.color.skin2_color),
    Air("A1", R.string.air, "℃", 250, 390, 0, 700,
            20, 40, 4, 21, 10, "0.0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, ".-",
            R.mipmap.air, R.color.air_color),

    Humidity("HUM", R.string.humidity, "%RH",
            0, 900, 0, 1000,
            0, 100, 5, 5, 10, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, R.color.humidity_color),
    Oxygen("O2", R.string.oxygen, "%",
            200, 650, 0, 1000,
            0, 100, 5, 5, 10, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, R.color.oxygen_color),

    Inc("HEATER", R.string.heater, "%",
            0, 100, 0, 100,
            0, 100, 5, 5, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, 0),

    Ambient("Amb", R.string.Ambient, "%",
            200, 650, 0, 1000,
            0, 100, 5, 5, 10, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, 0),
    Warmer("WARMER", R.string.heater, "%",
            0, 100, 0, 100,
            0, 100, 5, 5, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, 0),

    Weight("SCALE", R.string.weight_unit, "g",
            0, 10000, 0, 8000,
            0, 8000, 8, 5, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, 0),

    Angle("Angle", R.string.angle, "°",
            -200, 200, -200, 200,
            -200, 200, 5, 5, 10, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            R.mipmap.angle, 0),
    Blue("Blue", R.string.blue, "h",
            0, 3600, 0, 3600,
            0, 3600, 5, 5, 10, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, 0),

    MAT("MAT", R.string.mat, "℃", 250, 390, 0, 700,
            20, 40, 4, 21, 10, "0.0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, ".-",
            R.mipmap.skin, 0),

    Wake("WAKE", R.string.wake, "", 900, 1000, 0, 1000,
            0, 100, 5, 5, 10, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, 0),

    /*SPO2*/
    Spo2("SPO2", R.string.spo2_id, "%",
            900, 1000, 0, 1000,
            0, 100, 5, 5, 10, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, R.color.spo2_color),
    Pr("PR", R.string.pr, "bpm",
            75, 160, 25, 240,
            0, 240, 4, 10, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, R.color.pr_color),
    Pi("PI", R.string.pi, "%",
            5000, 10000, 20, 20000,
            0, 20, 5, 1, 1000, "0.00",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, R.color.pi_color),
    Sphb("SPHB", R.string.sphb, "g/dL",
            200, 2450, 0, 2500,
            0, 25, 5, 1, 100, "0.0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, R.color.sphb_color),
    Spoc("SPOC", R.string.spoc, "mL/dL",
            10, 340, 0, 350,
            0, 35, 5, 2, 10, "0.0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, R.color.spoc_color),
    Spmet("SPMET", R.string.spmet, "%",
            200, 1000, 0, 1000,
            0, 100, 5, 5, 10, "0.0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, R.color.spmet_color),
    Spco("SPCO", R.string.spco, "%",
            200, 1000, 0, 1000,
            0, 100, 5, 5, 10, "0.0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, R.color.spco_color),
    Pvi("PVI", R.string.pvi, "%",
            50, 100, 0, 100,
            0, 100, 5, 5, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "",
            0, R.color.pvi_color),

    /*Ecg*/
    EcgHr("HR", R.string.hr, "bpm",
            60, 120, 0, 350,
            0, 350, 5, 20, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "", 0, R.color.ecg_color),

    EcgRr("RR", R.string.resp, "bpm",
            10, 100, 0, 100,
            0, 100, 5, 5, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "", 0, R.color.ecg_color),

    /*CO2*/
    Co2("CO2", R.string.co2, "",
            0, 250, 0, 10000000,
            0, 100, 5, 5, 10, "0.0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "", 0, R.color.co2_color),

    Co2Rr("RR", R.string.awrr, "rpm",
            0, 20, 0, 100,
            0, 100, 5, 5, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "", 0, R.color.co2_color),

    Co2Fi("FI", R.string.Fi, "",
            0, 250, 0, 10000000,
            0, 100, 5, 5, 10, "0.0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "", 0, R.color.co2_color),

    /*Nibp*/
    Nibp("NIBP", R.string.nibp, "",
            50, 100, 0, 1000,
            0, 100, 5, 10, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "", 0, R.color.nibp_color),

    NibpDia("DIA", R.string.nibp, "",
            30, 70, 0, 1000,
            0, 100, 5, 10, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "", 0, R.color.nibp_color),

    NibpMean("MEAN", R.string.nibp, "",
            35, 80, 0, 1000,
            0, 100, 5, 10, 1, "0",
            Constant.SENSOR_DEFAULT_ERROR_STRING, "", 0, R.color.nibp_color);

    private final String commandName;
    private final int displayNameId;
    private final String unit;
    private final int lowerLimit;
    private final int upperLimit;
    private final int displayLower;
    private final int displayUpper;
    private final int coordinatorLower;
    private final int coordinatorUpper;
    private final int stepCount;
    private final int lowestYAxis;
    private final int denominator;
    private final String formatString;
    private final String errorString;
    private final String decimalErrorString;
    private final int imageIconId;
    private final int uniqueColor;

    SensorModelEnum(String commandName, int displayNameId, String unit,
                    int lowerLimit, int upperLimit, int displayLower, int displayUpper,
                    int coordinatorLower, int coordinatorUpper, int stepCount, int lowestYAxis,
                    int denominator, String formatString, String errorString, String decimalErrorString,
                    int imageIconId, int uniqueColorId) {
        this.commandName = commandName;
        this.displayNameId = displayNameId;
        this.unit = unit;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.displayLower = displayLower;
        this.displayUpper = displayUpper;
        this.stepCount = stepCount;
        this.lowestYAxis = lowestYAxis;
        this.coordinatorLower = coordinatorLower;
        this.coordinatorUpper = coordinatorUpper;
        this.denominator = denominator;
        this.formatString = formatString;
        this.errorString = errorString;
        this.decimalErrorString = decimalErrorString;
        this.imageIconId = imageIconId;
        if (uniqueColorId != 0)
            uniqueColor = ContextUtil.getColor(uniqueColorId);
        else {
            uniqueColor = 0;
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public int getDisplayNameId() {
        return displayNameId;
    }

    public String getUnit() {
        return unit;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public int getDisplayLower() {
        return displayLower;
    }

    public int getDisplayUpper() {
        return displayUpper;
    }

    public int getCoordinatorLower() {
        return coordinatorLower;
    }

    public int getCoordinatorUpper() {
        return coordinatorUpper;
    }

    public int getDenominator() {
        return denominator;
    }

    public String getFormatString() {
        return formatString;
    }

    public String getErrorString() {
        return errorString;
    }

    public String getDecimalErrorString() {
        return decimalErrorString;
    }

    public int getImageIconId() {
        return imageIconId;
    }

    public int getStepCount() {
        return stepCount;
    }

    public int getLowestYAxis() {
        return lowestYAxis;
    }

    public int getUniqueColor() {
        return uniqueColor;
    }

}