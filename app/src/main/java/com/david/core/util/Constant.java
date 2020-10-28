package com.david.core.util;

/**
 * author: Ling Lin
 * created on: 2018/1/25 12:21
 * email: 10525677@qq.com
 * description:
 */

public class Constant {

    /*设置屏幕锁屏时间*/
    public static final int SCREEN_LOCK_SECOND = 15000000; //second

    /*是否新生儿*/
    public static final boolean IS_NEONATE = false;

    /*系统设置*/
    public static final boolean ENABLE_LOG_TO_FILE = false;
    public static final int TEMP_370 = 370;
    public static final int NA_VALUE = -3;
    public static final int SIQ_STEP = 100000;
    public static final int IS_SIQ = 70000;
    /*型号*/
    public static final String YP3100 = "YP-3100";

    /*数据结构*/
    public static final String SENSOR_DEFAULT_ERROR_STRING = "--";

    /*数据库*/
    public static final int INCUBATOR_IN_DATABASE = 86400; //60*24*30*2 per month 每分钟存储一次，共两月，秒为单位
    public static final int MONITOR_IN_DATABASE = 518400; //12*60*24*30 per month 每5秒存储一次，共一月，秒为单位
    public static final int WEIGHT_IN_DATABASE = 2000;
    public static final int USER_PER_PAGE = 15;
    public static final int USER_PAGE_COUNT = 3;
    public static final int IMAGE_MAX = 200;
    public static final long VIDEO_MAX_SIZE = 10 * 1024 * 1024;//兆

    /*DOT PITCH 图形设置*/
    public static float PRINT_DOT_PITCH = 0.125f;
    public static boolean IS_INCH_10 = false;
    private static float INCH_12_DOT_PITCH = 0.24f;
    private static float INCH_10_DOT_PITCH = 0.20625f;

    public static float getCurrentDotPitch() {
        if (IS_INCH_10) {
            return INCH_10_DOT_PITCH;
        } else {
            return INCH_12_DOT_PITCH;
        }
    }

    public static int getPixel(float mm) {
        return (int) (mm / getCurrentDotPitch());
    }

    /*按钮事件设置*/
    public static final int LONG_CLICK_DELAY = 100; //millisecond

    /*串口配置*/
    public static final int MAX_SERIAL_EXECUTE_TIME = 5;
    public static final int SERIAL_RESPONSE_BUFFER_SIZE = 10240;
    public static final String NA_STRING = "NA";
    public static final int SENSOR_NA_VALUE = -2;

    /*密码设置*/
    public static final String SYSTEM_PASSWORD = "78722";
    public static final String USER_PASSWORD = "36663";
    public static final String DEMO_PASSWORD = "11111";

    /*信息输入*/
    public static final int MAX_INPUT = 12;

    /*声音设置, 范围 1-15*/
    public static final int SYSTEM_VOLUME = 10;

    /*图表设置*/
    public static final int DOT_PER_CHART = 120;
    public static final int DOT_PER_WEIGHT_CHART = 12;

    /*串口设置 2000,3000,2008系列*/
    /*1. 培养箱*/
    public static final String INCUBATOR_COM_ID = "/dev/ttyS3";
    public static final int INCUBATOR_BAUDRATE = 115200;

    /*2. 血氧*/
    public static final String SPO2_COM_ID = "/dev/ttyS1";
    public static final int SPO2_BAUDRATE_SLOW_RATE = 9600;
    public static final int SPO2_BAUDRATE = 115200;

    /*3. ECG*/
    public static final String ECG_COM_ID = "/dev/ttyS0";
    public static final int ECG_BAUDRATE = 460800;

    /*4. CO2*/
    public static final String CO2_COM_ID = "/dev/ttyUSB0";
    public static final int CO2_BAUDRATE = 9600;

    /*5. 血压*/
    public static final String NIBP_COM_ID = "/dev/ttyS4";
    public static final int NIBP_BAUDRATE = 9600;

    /*6. 打印机*/
    public static final String PRINT_COM_ID = "/dev/ttyUSB1";
    public static final int PRINT_BAUDRATE = 115200;

    /*7. 窒息唤醒*/
    public static final String WAKE_COM_ID = "/dev/ttyUSB2";
    public static final int WAKE_BAUDRATE = 115200;
}