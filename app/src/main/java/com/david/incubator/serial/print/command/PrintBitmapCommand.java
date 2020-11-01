package com.david.incubator.serial.print.command;

import com.david.core.serial.BaseCommand;
import com.david.core.util.Crc8Util;
import com.david.core.util.LoggerUtil;

import java.util.Locale;

import javax.inject.Inject;

public class PrintBitmapCommand extends BaseCommand {

    private final byte[] src = new byte[256];
    private final byte[] dest = new byte[256];

    private int srcIndex;
    private int destIndex;

    public PrintBitmapCommand() {
    }

    public void setSequenceId(byte sequenceId) {
        dest[0] = 1;
        dest[3] = 0x1A;
        dest[4] = sequenceId;
    }

    public void setBit(int y) {
        int HEIGHT = 384;
        int BOTTOM_MARGIN = 40;
        int TOP_MARGIN = 20;
        if (y >= HEIGHT - BOTTOM_MARGIN - TOP_MARGIN) {
            LoggerUtil.e("Print illegal bit: " + y);
            return;
        }
        y += BOTTOM_MARGIN;
        int byteId = y / 8;
        int bitId = y % 8;
        byte temp = 1;
        temp <<= bitId;
        src[byteId] |= temp;
    }

    private void compress() {
        int srcIndex = 1;

        byte lastChar = src[0];
        int count = 1;
        destIndex = 5;

        byte RLE_LEAD_CHAR = (byte) 0xAA;
        int SRC_LENGTH = 48;
        while (srcIndex < SRC_LENGTH) {
            if (src[srcIndex] == lastChar) {
                count++;
            } else {
                if (count >= 3 || lastChar == RLE_LEAD_CHAR) {
                    pushChar(RLE_LEAD_CHAR);
                    pushChar(lastChar);
                    pushChar((byte) count);
                } else {
                    for (int i = 0; i < count; i++) {
                        pushChar(lastChar);
                    }
                }
                lastChar = src[srcIndex];
                count = 1;
            }
            srcIndex++;
        }

        //handle last byte
        if (count >= 3 || lastChar == RLE_LEAD_CHAR) {
            pushChar(RLE_LEAD_CHAR);
            pushChar(lastChar);
            pushChar((byte) count);
        } else {
            for (int i = 0; i < count; i++) {
                pushChar(lastChar);
            }
        }
        destIndex++;
    }

    private void pushChar(byte data) {
        dest[destIndex++] = data;
    }

    private void compute() {
        compress();
        dest[1] = (byte) ((destIndex) % 256);
        dest[2] = (byte) ((destIndex) / 256);
        dest[destIndex - 1] = Crc8Util.computeBestCrc(dest, destIndex);

        src[0] = 0x01;
        srcIndex = 1;
        for (int index = 1; index < destIndex; index++) {
            if (dest[index] != 0x01) {
                src[srcIndex++] = dest[index];
            } else {
                src[srcIndex++] = 0x01;
                src[srcIndex++] = 0x01;
            }
        }
    }

    @Override
    public byte[] getRequest() {
        compute();
        return src;
    }

    @Override
    protected String getCommandId() {
        return String.format(Locale.US, "%s%d", getClass().getSimpleName(), dest[4]);
    }

    public int getRequestLength() {
        return srcIndex;
    }
}