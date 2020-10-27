package com.david.incubator.serial.ecg;

import com.david.core.serial.spo2.AbstractResponseCommand;
import com.david.core.util.Crc8Util;

import javax.inject.Inject;

public class EcgResponseCommand extends AbstractResponseCommand {

    private byte type;
    private int commandLength;

    @Inject
    public EcgResponseCommand() {
        super(1024);
    }

    @Override
    public boolean isCompleted() {
        if (index < commandLength) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean checkCRC() {
        int crc = Crc8Util.computeBestCrc(responseBuffer, commandLength, index + 4, type);
        return crc == responseBuffer[commandLength - 1];
    }

    public void reset(int commandLength, byte type) {
        super.reset();
        this.commandLength = commandLength;
        this.type = type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }

    public int getCommandLength() {
        return commandLength;
    }
}