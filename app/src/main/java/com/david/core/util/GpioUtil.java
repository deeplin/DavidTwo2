package com.david.core.util;

import java.io.FileInputStream;

public class GpioUtil {

    private static int count = 0;

    public static void init() {
        SU.exec("cat /sys/debuggpio/debug_tp1");
    }

    private static boolean readFile() {
        try {
            FileInputStream stream = new FileInputStream("/sys/debuggpio/debug_tp1");
            int status = stream.read();
            stream.close();
            return '0' != status;
        } catch (Exception e) {
            LoggerUtil.e(e);
            return false;
        }
    }

    public static boolean read() {
        if (readFile()) {
            count++;
            if (count == 2) {
                return true;
            }
        } else {
            count = 0;
        }
        return false;
    }
}