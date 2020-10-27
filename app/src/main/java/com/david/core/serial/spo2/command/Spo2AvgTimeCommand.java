package com.david.core.serial.spo2.command;

public class Spo2AvgTimeCommand extends BaseSpo2SerialCommand {
    private final byte[] COMMAND = {(byte) 0xA1, 0x02, 0x01, 0x00, 0x07, (byte) 0xAF};

    public Spo2AvgTimeCommand() {
        super();
    }

    public void set(byte sensorId) {
        COMMAND[3] = sensorId;
    }

    @Override
    public byte[] getRequest() {
        super.setCrc(COMMAND);
        return COMMAND;
    }
}
