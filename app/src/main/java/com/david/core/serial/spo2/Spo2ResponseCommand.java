package com.david.core.serial.spo2;

import com.david.core.util.CrcUtil;

import javax.inject.Inject;

public class Spo2ResponseCommand extends AbstractResponseCommand {

    @Inject
    public Spo2ResponseCommand() {
        super(260);
    }

    public boolean isCompleted() {
        if (index < 4) {
            return false;
        }
        int length = responseBuffer[1];
        if (index >= length + 4) {
            return true;
        }
        return false;
    }

    public void addData(byte data) {
        /*0xA1*/
        if (index == 0 && data != -95) {
            return;
        }
        super.addData(data);
    }

    protected boolean checkCRC() {
        if (index < 4) {
            return false;
        }
        byte crc = CrcUtil.computeSumCrc(responseBuffer, 2, index - 2);
        if (crc == responseBuffer[index - 2]) {
            return true;
        }
        return false;
    }
}