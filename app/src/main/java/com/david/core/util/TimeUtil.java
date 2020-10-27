package com.david.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/15 15:32
 * email: 10525677@qq.com
 * description:
 */

public class TimeUtil {
    public static final String Date0 = "yyyy-MM-dd";
//    public static final String Date1 = "MM-dd-yyyy";
//    public static final String Date2 = "dd-MM-yyyy";

    public static final String MonthDayHourMinute = "MM-dd\nHH:mm";
    public static final String HourMinute = "HH:mm";
    public static final String MonthDay = "MM-dd";
    public static final String FullTime = "yyyy-MM-dd HH:mm:ss";
    public static final String Time = "HH:mm:ss";
    public static final String FILENAME = "yyMMddHHmmss";

    public static String getCurrentTime(String format) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.US);
        return dateFormatter.format(date);
    }

    public static String getTimeFromSecond(long time, String format) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.US);
        return dateFormatter.format(time * 1000);
    }

    public static long getCurrentTimeInSecond() {
        return System.currentTimeMillis() / 1000;
    }

//    public static long getRoundTimeInDay() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return calendar.getTime().getTime() / 1000;
//    }

    public static long getRoundTimeInSecond() {
        return getCurrentTimeInSecond() / 60 * 60;
    }

    public static int getDaysByYearMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }
}