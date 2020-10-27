package com.david.core.serial.spo2.command;

public class Spo2SphbAveragingCommand extends BaseSpo2SerialCommand {
    private final byte[] COMMAND = {(byte) 0xA1, 0x02, 0x0E, 0x00, 0x00, (byte) 0xAF};

    public Spo2SphbAveragingCommand() {
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