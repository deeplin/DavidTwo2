package com.david.incubator.serial.nibp;

import com.david.core.serial.spo2.AbstractResponseCommand;
import com.david.core.util.CrcUtil;

public class NibpResponseCommand extends AbstractResponseCommand {

    public NibpResponseCommand(int bufferLength) {
        super(bufferLength);
    }

    public boolean isCompleted() {
        if (index < responseBuffer.length || index < 3) {
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkCRC() {
        return CrcUtil.check(responseBuffer, responseBuffer.length);
    }
}