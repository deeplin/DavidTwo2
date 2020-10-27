package com.david.core.util;

import java.lang.reflect.Method;

/**
 * author: Ling Lin
 * created on: 2017/7/19 10:02
 * email: 10525677@qq.com
 * description:
 */

public class ReflectionUtil {

    public static void setMethod(Object obj, String method, int value) throws Exception {
        try {
            Method m = obj.getClass().getDeclaredMethod("set" + method, int.class);
            m.invoke(obj, value);
        } catch (Exception e) {
            LoggerUtil.se("Unknown field: " + method + " " + value);
        }
    }

    public static void setMethod(Object obj, String method, String value) throws Exception {
        Method m = obj.getClass().getDeclaredMethod("set" + method, String.class);
        m.invoke(obj, value);
    }
}