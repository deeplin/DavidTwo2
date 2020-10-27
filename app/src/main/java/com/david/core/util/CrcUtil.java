package com.david.core.util;

public class CrcUtil {
    public static byte computeSumCrc(byte[] buffer, int start, int end) {
        byte crc = 0;
        for (int index = start; index < end; index++) {
            crc += buffer[index];
        }
        return crc;
    }

    public static boolean check(final byte[] data, final int length) {
        return compute(data, length) == 0;
    }

    public static byte compute(final byte[] data, final int length) {
        byte result = (byte) 0x100;
        for (int index = 0; index < length; index++) {
            result -= data[index];
        }
        return result;
    }
}