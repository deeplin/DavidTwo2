package com.david.core.enumeration;

import com.david.R;

/**
 * author: Ling Lin
 * created on: 2017/7/16 15:01
 * email: 10525677@qq.com
 * description:
 */

public enum LayoutPageEnum {

    LAYOUT_STANDARD,
    LAYOUT_BASIC,
    LAYOUT_TEMP_CURVE,
    LAYOUT_WEIGHT_CURVE,
    LAYOUT_BODY_WAVE,
    LAYOUT_SAME_SCREEN,
    LAYOUT_SPO2,
    LAYOUT_CAMERA,
    LAYOUT_POPUP,

    MENU_HOME,
    //    MENU_COMFORT_ZONE(R.string.comfort_zone, LayoutPageEnum.MENU_HOME, true, true),
//    MENU_SENSOR_CALIBRATION(R.string.sensor_calibration, LayoutPageEnum.MENU_HOME, true, true),
//    MENU_CONFIRM_SENSOR_CALIBRATION(R.layout.layout_confirm, R.string.confirm, LayoutPageEnum.MENU_SENSOR_CALIBRATION, false, true),
//    MENU_VERSION(R.string.version, LayoutPageEnum.MENU_HOME, true, true),
//    MENU_FUNCTION_SETUP(R.string.function_setup, LayoutPageEnum.MENU_HOME, true, true),
//    MENU_PARAMETER_SETUP(R.string.parameter_setup, LayoutPageEnum.MENU_HOME, true, true),
    MENU_PRINT_SETUP(R.string.print_setup, LayoutPageEnum.MENU_HOME, true, true),
    //
    SWITCH_SCREEN(R.string.screen_choice, null, false, true),
    //
//    USER_HOME(R.string.user_setup, LayoutPageEnum.MENU_HOME, true, true),
//    USER_LANGUAGE(R.string.language, LayoutPageEnum.USER_HOME, true, true),
//    USER_TIME,
//    USER_OVERHEAT_EXPERIMENT(R.string.overheat_experiment, LayoutPageEnum.USER_HOME, true, true),
//    USER_MODULE_SETUP(R.string.module_setup, LayoutPageEnum.USER_HOME, true, true),
//    USER_UNIT_SETUP(R.string.unit_setup, LayoutPageEnum.USER_HOME, true, true),
//
//    SYSTEM_HOME(R.string.system_setup, LayoutPageEnum.MENU_HOME, true, true),
//    SYSTEM_DEVIATION_ALARM(R.string.deviation_alarm, LayoutPageEnum.SYSTEM_HOME, true, true),
//    SYSTEM_OVERHEAT_ALARM(R.string.overheat_alarm, LayoutPageEnum.SYSTEM_HOME, true, true),
//    SYSTEM_RANGE_SETUP(R.string.range_setup, LayoutPageEnum.SYSTEM_HOME, true, true),
//    SYSTEM_SENSOR_CALIBRATION(R.string.sensor_calibration, LayoutPageEnum.SYSTEM_HOME, true, true),
//    SYSTEM_DEBUG_INFO,
//    SYSTEM_FACTORY(R.string.factory_setting, LayoutPageEnum.SYSTEM_HOME, true, true),
//    SYSTEM_CONFIRM_FACTORY(R.layout.layout_confirm, R.string.confirm, LayoutPageEnum.SYSTEM_FACTORY, false, true),
//    SYSTEM_MODULE_CALIBRATION(R.string.module_calibration, LayoutPageEnum.SYSTEM_HOME, true, true),
//    SYSTEM_NIBP_CALIBRATION(R.string.nibp_calibration, LayoutPageEnum.SYSTEM_MODULE_CALIBRATION, true, true),
//    SYSTEM_ALARM_LIST,
//    SYSTEM_Image_List(R.string.all_image, LayoutPageEnum.SYSTEM_HOME, true, true),
//    SYSTEM_Video_List(R.string.all_video, LayoutPageEnum.SYSTEM_HOME, true, true),
//    SYSTEM_Image(R.string.image, LayoutPageEnum.SYSTEM_Image_List, true, true),
//    SYSTEM_Video(R.string.video, LayoutPageEnum.SYSTEM_Video_List, true, true),
//
    SETUP_HOME,

    ALARM_LIST_PHYSIOLOGICAL,
    ALARM_LIST_TECHNICAL,
    //    ALARM_SETUP(R.layout.layout_common, R.string.alarm_setup, LayoutPageEnum.SETUP_HOME, true, true),
//
//    PATIENT_LIST,
//    PATIENT_LIST_INFO(R.layout.layout_patient, R.string.patient_list, LayoutPageEnum.PATIENT_LIST, true, true),
//    PATIENT_INFO(R.layout.layout_patient, R.string.patient_info, LayoutPageEnum.MENU_HOME, true, true),
//    PATIENT_ADD(R.layout.layout_patient, R.string.add_patient, LayoutPageEnum.PATIENT_INFO, true, true),
//    PATIENT_CONFIRM_REMOVE,
//
//    KEYBOARD_ALPHABET,
//    KEYBOARD_LOGIN_USER,
//    KEYBOARD_LOGIN_SYSTEM,
//    KEYBOARD_LOGIN_DEMO,
//
//    TREND_CHART,
//    TREND_TABLE,
//    TREND_PRINT(R.layout.layout_print, R.string.data_print, LayoutPageEnum.TREND_TABLE, true, true),
    LAYOUT_NONE;

    private final int layoutId;
    private final int titleId;
    private final LayoutPageEnum parentPageEnum;
    private final boolean showReverse;
    private final boolean showClose;

    LayoutPageEnum() {
        this(0, 0, null, false, false);
    }

    LayoutPageEnum(int titleId, LayoutPageEnum parentPageEnum, boolean showReverse, boolean showClose) {
        this(R.layout.layout_common, titleId, parentPageEnum, showReverse, showClose);
    }

    LayoutPageEnum(int layoutId, int titleId, LayoutPageEnum parentPageEnum, boolean showReverse, boolean showClose) {
        this.layoutId = layoutId;
        this.titleId = titleId;
        this.parentPageEnum = parentPageEnum;
        this.showReverse = showReverse;
        this.showClose = showClose;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getTitleId() {
        return titleId;
    }

    public LayoutPageEnum getParentPageEnum() {
        return parentPageEnum;
    }

    public boolean isShowReverse() {
        return showReverse;
    }

    public boolean isShowClose() {
        return showClose;
    }
}