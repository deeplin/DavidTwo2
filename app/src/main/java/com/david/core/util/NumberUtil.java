package com.david.core.util;

public class NumberUtil {
    public static short getShortHighFirst(int index, byte[] buffer) {
        int low = buffer[index + 1];
        int high = buffer[index];
        high = high << 8;
        low = low & 0xff;
        int result = high | low;
        return (short) result;
    }

    public static int getLongHighFirst(byte[] src, int offset) {
        return (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
    }

    public static short getShort(int index, byte[] buffer) {
        int low = buffer[index];
        int high = buffer[index + 1];
        high = high << 8;
        low = low & 0xff;
        int result = high | low;
        return (short) result;
    }
}