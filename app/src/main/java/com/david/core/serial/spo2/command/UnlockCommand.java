package com.david.core.serial.spo2.command;

public class UnlockCommand extends BaseSpo2SerialCommand {

    private final byte[] command = {(byte) 0xA1, 0x0B, 0x71,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            (byte) 0x90, (byte) 0xAF};

    public UnlockCommand() {
        super();
    }

    @Override
    public byte[] getRequest() {
        super.setCrc(command);
        return command;
    }

    public void fillData(byte[] buffer, int startPosition, int length) {
        System.arraycopy(buffer, startPosition, command, 3, length);
        command[3] ^= (byte) 0x55;
        command[4] ^= (byte) 0xD9;
        command[5] ^= (byte) 0xB5;
        command[6] ^= (byte) 0x82;
    }
}