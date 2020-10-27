package com.david.core.serial.incubator;

import com.david.core.serial.BaseSerialControl;

public abstract class BaseIncubatorCommandControl extends BaseSerialControl {

    private static final int INCUBATOR_COMMAND_LENGTH = 1024;

    private IncubatorCommandEnum incubatorCommandMode;
    protected final byte[] responseBuffer;
    protected int responseBufferLength;

    public BaseIncubatorCommandControl() {
        super();
        responseBuffer = new byte[INCUBATOR_COMMAND_LENGTH];
        responseBufferLength = 0;
        incubatorCommandMode = IncubatorCommandEnum.Data;
    }

    @Override
    protected boolean parseResponse(byte data) {
        if (responseBufferLength >= responseBuffer.length) {
            responseBufferLength = 0;
        }
        responseBuffer[responseBufferLength++] = data;
        switch (data) {
            case (0x0D):
                switch (incubatorCommandMode) {
                    case Data:
                    case FirstOD:
                    case Second0D:
                        incubatorCommandMode = IncubatorCommandEnum.FirstOD;
                        break;
                    case First0A:
                        incubatorCommandMode = IncubatorCommandEnum.Second0D;
                        break;
                }
                break;
            case (0x0A):
                switch (incubatorCommandMode) {
                    case Data:
                    case First0A:
                        incubatorCommandMode = IncubatorCommandEnum.Data;
                        break;
                    case FirstOD:
                        incubatorCommandMode = IncubatorCommandEnum.First0A;
                        break;
                    case Second0D:
                        incubatorCommandMode = IncubatorCommandEnum.Data;
                        return true;
                }
                break;
        }
        return false;
    }
}