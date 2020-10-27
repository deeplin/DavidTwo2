package com.david.core.util;

public class UnsignedByte {

    public static int unsignedByteToInt(byte data) {
        if (data < 0) {
            return data + 256;
        } else {
            return data;
        }
    }
}