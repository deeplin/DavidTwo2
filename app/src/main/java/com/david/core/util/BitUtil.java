package com.david.core.util;

public class BitUtil {
    public static boolean getBit(int data, int index) {
        return ((data >> index) & 0x01) > 0;
    }

    public static int setBit(int data, int index, boolean status) {
        if (status) {
            return data | (0x01 << index);
        } else {
            return data & ~(0x01 << index);
        }
    }
}
