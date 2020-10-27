package com.david.core.serial.spo2.command;

import com.david.core.enumeration.CommandEnum;

import java.util.Locale;

public class SetupCommand extends BaseSpo2SerialCommand {

    private final byte[] command = {(byte) 0xA1, 0x04, 0x20, 0x01, 0x03, (byte) 0xE8, (byte) 0x0C, (byte) 0xAF};

    private static SetupCommand instance = null;

    public static SetupCommand getInstance() {
        if (instance == null) {
            instance = new SetupCommand();
        }
        return instance;
    }

    private SetupCommand() {
        super(CommandEnum.Critical);
    }

    public void setCommandSensorId(byte sensorId) {
        command[3] = sensorId;
    }

    public byte getCommandSensorId() {
        return command[3];
    }

    @Override
    public byte[] getRequest() {
        super.setCrc(command);
        return command;
    }

    @Override
    protected String getCommandId() {
        return String.format(Locale.US, "%s%s", getClass().getSimpleName(), command[3]);
    }
}