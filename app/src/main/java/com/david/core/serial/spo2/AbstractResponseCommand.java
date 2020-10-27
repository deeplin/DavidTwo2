package com.david.core.serial.spo2;

import com.david.core.util.StringUtil;

public abstract class AbstractResponseCommand {

    protected final byte[] responseBuffer;
    protected int index;

    public AbstractResponseCommand(int bufferLength) {
        responseBuffer = new byte[bufferLength];
        index = 0;
    }

    @Override
    public String toString() {
        return StringUtil.byteArrayToHex(index, responseBuffer);
    }

    public byte[] getResponseBuffer() {
        return responseBuffer;
    }

    public void addData(byte data) {
        responseBuffer[index++] = data;
    }

    public abstract boolean isCompleted();

    protected abstract boolean checkCRC();

    public void reset() {
        index = 0;
    }
}
