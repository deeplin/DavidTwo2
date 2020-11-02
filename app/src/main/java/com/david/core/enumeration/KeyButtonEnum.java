package com.david.core.enumeration;

import androidx.arch.core.util.Function;

import com.david.R;
import com.david.core.util.FormatUtil;
import com.david.incubator.ui.menu.MenuComfortZoneLayout;
import com.david.incubator.ui.system.SystemSensorCalibrationLayout;

public enum KeyButtonEnum {

//    SETUP_AIR(R.string.setup, 0, 1000, 1, FormatUtil::formatTemp),
//    SETUP_SKIN(R.string.setup, 0, 1000, 1, FormatUtil::formatTemp),
//    SETUP_MANUEL(R.string.setup, 0, 100, 5, FormatUtil::formatPercentage),
//    SETUP_HUMIDITY(R.string.setup, 0, 1000, 10, FormatUtil::formatHumidity),
//    SETUP_OXYGEN(R.string.setup, 0, 1000, 10, FormatUtil::formatOxygen),
//
//    SETUP_CO2_O2_COMPENSATE(R.string.o2_compensate, 0, 100, 1, FormatUtil::formatPercentage),
//    SETUP_CO2_N20_COMPENSATE(R.string.n2o_compensate, 0, 100, 1, FormatUtil::formatPercentage),
//
//    SETUP_WAKE_VIBRATION_INTENSITY(R.string.vibration_intensity, 50, 100, 1, String::valueOf),
//    SETUP_WAKE_HR(R.string.hr, 90, 110, 1, String::valueOf),
//    SETUP_WAKE_SPO2(R.string.spo2_id, 80, 90, 1, String::valueOf),
//
//    SETUP_SPO2_AVG_TIME(R.string.avg_time, 0, 6, 1, SetupSpo2Layout::convertAverageTime),
//
//    PATIENT_ADD_GESTATION(R.string.gestation, 0, 99, 1),
//    PATIENT_ADD_WEIGHT(R.string.birth_weight, 0, 10000, 10),
//    PATIENT_ADD_HEIGHT(R.string.height, 0, 100, 1),
//    PATIENT_ADD_AGE(R.string.age, 0, 999, 1),
//
    MENU_COMFORT_ZONE_AGE(R.string.age, 1, 35, 1),
    MENU_COMFORT_ZONE_GESTATION(R.string.gestation, 28, 37, 1, MenuComfortZoneLayout::getGestationString),
    MENU_COMFORT_ZONE_WEIGHT(R.string.weight_unit, 900, 2600, 100, MenuComfortZoneLayout::getWeightString),

    MENU_FUNCTION_SETUP_SCREEN_LUMINANCE(R.string.screen_luminance, 1, 5, 1),
    MENU_FUNCTION_SETUP_NOTIFICATION_VOLUME(R.string.notification_volume, 1, 5, 1),
    MENU_FUNCTION_SETUP_PULSE_VOLUME(R.string.pulse_volume, 1, 5, 1),
    MENU_FUNCTION_SETUP_APGAR_VOLUME(R.string.apgar_volume, 1, 5, 1),
//
//    ALARM_SPO2_UPPER(R.string.spo2_alarm_upper, 510, 1000, 10, FormatUtil::formatOxygen),
//    ALARM_SPO2_LOWER(R.string.spo2_alarm_lower, 500, 990, 10, FormatUtil::formatOxygen),
//    ALARM_PR_UPPER(R.string.pr_alarm_upper, 26, 240, 1, FormatUtil::formatPr),
//    ALARM_PR_LOWER(R.string.pr_alarm_lower, 25, 239, 1, FormatUtil::formatPr),
//    ALARM_PI_UPPER(R.string.pi_alarm_upper, 200, 20000, 1000, FormatUtil::formatPi),
//    ALARM_PI_LOWER(R.string.pi_alarm_lower, 100, 18000, 1000, FormatUtil::formatPi),
//    ALARM_SPHB_UPPER(R.string.sphb_alarm_upper, 110, 2450, 10, FormatUtil::formatSphb),
//    ALARM_SPHB_LOWER(R.string.sphb_alarm_lower, 100, 2440, 10, FormatUtil::formatSphb),
//    ALARM_SPOC_UPPER(R.string.spoc_alarm_upper, 20, 340, 10, FormatUtil::formatSpoc),
//    ALARM_SPOC_LOWER(R.string.spoc_alarm_lower, 10, 330, 10, FormatUtil::formatSpoc),
//    ALARM_SPMET_UPPER(R.string.spmet_alarm_upper, 210, 1000, 10, FormatUtil::formatOxygen),
//    ALARM_SPMET_LOWER(R.string.spmet_alarm_lower, 200, 990, 10, FormatUtil::formatOxygen),
//    ALARM_SPCO_UPPER(R.string.spco_alarm_upper, 210, 1000, 10, FormatUtil::formatOxygen),
//    ALARM_SPCO_LOWER(R.string.spco_alarm_lower, 200, 990, 10, FormatUtil::formatOxygen),
//    ALARM_PVI_UPPER(R.string.pvi_alarm_upper, 2, 100, 1, FormatUtil::formatPvi),
//    ALARM_PVI_LOWER(R.string.pvi_alarm_lower, 1, 99, 1, FormatUtil::formatPvi),
//
//    ALARM_HR_UPPER(R.string.hr_alarm_upper, 16, 350, 1, FormatUtil::formatPr),
//    ALARM_HR_LOWER(R.string.hr_alarm_lower, 15, 349, 1, FormatUtil::formatPr),
//
//    ALARM_CO2_UPPER(R.string.etco2_alarm_upper, 1, 250, 1, null),
//    ALARM_CO2_LOWER(R.string.etco2_alarm_lower, 0, 249, 2, null),
//    ALARM_CO2_FI_UPPER(R.string.fico2_alarm_upper, 1, 250, 1, FormatUtil::formatOxygen),
//    ALARM_CO2_FI_LOWER(R.string.fico2_alarm_lower, 0, 249, 1, FormatUtil::formatOxygen),
//    ALARM_CO2_RR_UPPER(R.string.awrr_alarm_upper, 1, 190, 1, FormatUtil::formatRr),
//    ALARM_CO2_RR_LOWER(R.string.awrr_alarm_lower, 0, 189, 1, FormatUtil::formatRr),
//
//    ALARM_ECG_RR_UPPER(R.string.rr_alarm_upper, 21, 150, 1, FormatUtil::formatRr),
//    ALARM_ECG_RR_LOWER(R.string.rr_alarm_lower, 0, 119, 1, FormatUtil::formatRr),
//
//    ALARM_NIBP_UPPER(R.string.sys_alarm_upper, 41, 130, 1, String::valueOf),
//    ALARM_NIBP_LOWER(R.string.sys_alarm_lower, 40, 129, 1, String::valueOf),
//    ALARM_NIBP_DIA_UPPER(R.string.dia_alarm_upper, 21, 100, 1, String::valueOf),
//    ALARM_NIBP_DIA_LOWER(R.string.dia_alarm_lower, 20, 99, 1, String::valueOf),
//    ALARM_NIBP_MEAN_UPPER(R.string.map_alarm_upper, 28, 110, 1, String::valueOf),
//    ALARM_NIBP_MEAN_LOWER(R.string.map_alarm_lower, 27, 109, 1, String::valueOf),
//
    SYSTEM_DEVIATION_AIR_UPPER(R.string.air_upper_limit, 0, 30, 1, FormatUtil::formatTemp),
    SYSTEM_DEVIATION_AIR_LOWER(R.string.air_lower_limit, 0, 30, 1, FormatUtil::formatTemp),
    SYSTEM_DEVIATION_SKIN_UPPER(R.string.skin_upper_limit, 0, 10, 1, FormatUtil::formatTemp),
    SYSTEM_DEVIATION_SKIN_LOWER(R.string.skin_lower_limit, 0, 10, 1, FormatUtil::formatTemp),
    SYSTEM_DEVIATION_HUMIDITY_UPPER(R.string.humidity_upper_limit, 0, 200, 10, FormatUtil::formatHumidity),
    SYSTEM_DEVIATION_HUMIDITY_LOWER(R.string.humidity_lower_limit, 0, 200, 10, FormatUtil::formatHumidity),
    SYSTEM_DEVIATION_OXYGEN_UPPER(R.string.oxygen_upper_limit, 0, 100, 10, FormatUtil::formatOxygen),
    SYSTEM_DEVIATION_OXYGEN_LOWER(R.string.oxygen_lower_limit, 0, 100, 10, FormatUtil::formatOxygen),

    SYSTEM_OVERHEAT_ALARM_AIR_ABOVE_37(R.string.overheat_air_above_37, 385, 400, 1, FormatUtil::formatTemp),
    SYSTEM_OVERHEAT_ALARM_AIR_BELOW_37(R.string.overheat_air_below_37, 375, 390, 1, FormatUtil::formatTemp),
    SYSTEM_OVERHEAT_ALARM_SKIN(R.string.skin, 370, 410, 1, FormatUtil::formatTemp),
    SYSTEM_OVERHEAT_ALARM_FAN_SPEED(R.string.fan_speed, 400, 5100, 10, FormatUtil::formatSpeed),
    SYSTEM_OVERHEAT_ALARM_AIR_FLOW_ABOVE_37(R.string.overheat_airflow_above_37, 385, 700, 1, FormatUtil::formatTemp),
    SYSTEM_OVERHEAT_ALARM_AIR_FLOW_BELOW_37(R.string.overheat_airflow_below_37, 375, 700, 1, FormatUtil::formatTemp),
    SYSTEM_RANGE_SETUP_AIR_UPPER(R.string.air_upper_limit, 370, 390, 1, FormatUtil::formatTemp),
    SYSTEM_RANGE_SETUP_AIR_LOWER(R.string.air_lower_limit, 200, 340, 1, FormatUtil::formatTemp),
    SYSTEM_RANGE_SETUP_SKIN_UPPER(R.string.skin_upper_limit, 370, 390, 1, FormatUtil::formatTemp),
    SYSTEM_RANGE_SETUP_SKIN_LOWER(R.string.skin_lower_limit, 300, 340, 1, FormatUtil::formatTemp),
    SYSTEM_RANGE_SETUP_HUMIDITY_UPPER(R.string.humidity_upper_limit, 300, 990, 10, FormatUtil::formatHumidity),
    SYSTEM_RANGE_SETUP_HUMIDITY_LOWER(R.string.humidity_lower_limit, 0, 500, 10, FormatUtil::formatHumidity),
    SYSTEM_RANGE_SETUP_OXYGEN_UPPER(R.string.oxygen_upper_limit, 300, 650, 10, FormatUtil::formatOxygen),
    SYSTEM_RANGE_SETUP_OXYGEN_LOWER(R.string.oxygen_lower_limit, 200, 300, 10, FormatUtil::formatOxygen),
    SYSTEM_SENSOR_CALIBRATION_AIR(R.string.air, -50, 50, 1, SystemSensorCalibrationLayout::formatTemp),
    SYSTEM_SENSOR_CALIBRATION_ISO_AIR(R.string.iso_air, -50, 50, 1, SystemSensorCalibrationLayout::formatTemp),
    SYSTEM_SENSOR_CALIBRATION_SKIN(R.string.skin1, -50, 50, 1, SystemSensorCalibrationLayout::formatTemp),
    SYSTEM_SENSOR_CALIBRATION_ISO_SKIN(R.string.iso_skin1, -50, 50, 1, SystemSensorCalibrationLayout::formatTemp),
    SYSTEM_SENSOR_CALIBRATION_HUMIDITY(R.string.humidity, -50, 50, 10, SystemSensorCalibrationLayout::formatHumidity);

    public int getTitleId() {
        return titleId;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public int getStep() {
        return step;
    }

    public Function<Integer, String> getConverter() {
        return converter;
    }

    private final int titleId;
    private final int lowerLimit;
    private int upperLimit;
    private int step;
    private Function<Integer, String> converter;

    KeyButtonEnum(int titleId, int lowerLimit, int upperLimit, int step) {
        this(titleId, lowerLimit, upperLimit, step, String::valueOf);
    }

    KeyButtonEnum(int titleId, int lowerLimit, int upperLimit, int step, Function<Integer, String> converter) {
        this.titleId = titleId;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.step = step;
        this.converter = converter;
    }

    public void setConverter(Function<Integer, String> converter) {
        this.converter = converter;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
