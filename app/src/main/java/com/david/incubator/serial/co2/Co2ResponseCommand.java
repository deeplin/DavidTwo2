package com.david.incubator.serial.co2;

import com.david.incubator.serial.nibp.NibpResponseCommand;

import javax.inject.Inject;

public class Co2ResponseCommand extends NibpResponseCommand {

    @Inject
    public Co2ResponseCommand() {
        super(21);
    }

    public void addData(byte data) {
        if (index == 0 && data != (0xAA - 256)) {
            return;
        }
        if (index == 1 && data != 0X55) {
            index = 0;
            return;
        }
        responseBuffer[index++] = data;
    }
}