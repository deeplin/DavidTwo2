package com.david.core.serial.incubator;

import com.david.core.serial.BaseCommand;
import com.david.core.util.Constant;
import com.david.core.util.ReflectionUtil;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/18 17:06
 * email: 10525677@qq.com
 * description:
 */


public class IncubatorCommandParser {

    public static void parse(BaseCommand serialCommand, byte[] responseBuffer, int length) throws Exception {
        /*检查返回指令状态，返回指令数据段*/
        String commandString = checkCommandStatus(responseBuffer, length);

        if (commandString != null) {
            parseCommandString(commandString, serialCommand);
        }
    }

    /*检查返回指令状态，返回指令数据段*/
    private static String checkCommandStatus(byte[] responseBuffer, int length) throws Exception {
        int statusIndex = getStatusIndex(responseBuffer, length);
        String status = new String(responseBuffer, 0, statusIndex);

        if (status.equals(CommandChar.OK)) {
            if (length - statusIndex - 1 > 0) {
                return new String(responseBuffer, statusIndex + 1, length - statusIndex - 2);
            } else {
                return null;
            }
        } else {
            throw new Exception(String.format(Locale.US, "Incubator response ERR: %s ", status));
        }
    }

    private static int getStatusIndex(byte[] responseBuffer, int length) throws Exception {
        for (int index = 0; index < length; index++) {
            //3B ;
            if (responseBuffer[index] == 0x3B) {
                return index;
            }
        }
        throw new Exception("Get incubator status index error.");
    }

    private static void parseCommandString(String commandString, BaseCommand baseCommand) throws Exception {
        String[] items = commandString.split(CommandChar.SEMICOLON);
        if (items.length <= 0) {
            throw new Exception("Too short incubator command length.");
        }

        for (String item : items) {
            String[] pair = item.split(CommandChar.EQUAL);
            if (pair.length == 2) {
                String method = pair[0];

                int value;
                if (pair[1].equals(Constant.NA_STRING)) {
                    value = Constant.SENSOR_NA_VALUE;
                    ReflectionUtil.setMethod(baseCommand, method, value);
                } else {
                    try {
                        value = Integer.parseInt(pair[1]);
                        ReflectionUtil.setMethod(baseCommand, method, value);
                    } catch (NumberFormatException e) {
                        ReflectionUtil.setMethod(baseCommand, method, pair[1]);
                    }
                }
            } else if (pair.length == 1) {
                String method = pair[0];
                ReflectionUtil.setMethod(baseCommand, method, "");
            } else {
                throw new Exception("Parse incubator command error. " + commandString);
            }
        }
    }
}