package com.david.core.util;

/**
 * author: Ling Lin
 * created on: 2017/8/2 20:49
 * email: 10525677@qq.com
 * description:
 */

public class StringUtil {

    public static String byteArrayToHex(byte[] buffer) {
        return byteArrayToHex(buffer.length, buffer);
    }

    public static String byteArrayToHex(int bufferLength, byte[] buffer) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < bufferLength; index++) {
            String message = StringUtil.byteToHex(buffer[index]) + " ";
            stringBuilder.append(message);
        }
        return stringBuilder.toString();
    }

    public static String byteToHex(byte data) {
        StringBuilder stringBuilder = new StringBuilder();
        String hv;
        if (data >= 0) {
            hv = Integer.toHexString(data);
        } else {
            hv = Integer.toHexString(data + 256);
        }
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        return stringBuilder.append(hv).toString().toUpperCase();
    }

    /*0x80: -128
     * 0xFF: -1
     * 0x7F: 127
     * */
    public static byte hexToByte(String hexString) {
        if (hexString == null || hexString.length() == 0) {
            return 0;
        }
        int result = Integer.parseInt(hexString, 16);
        if (result >= 128) {
            return (byte) (result - 256);
        } else {
            return (byte) result;
        }
    }
}