package com.david.incubator.print.buffer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrintBufferRepository {

    private final PrintBuffer[] printBuffers;

    @Inject
    public PrintBufferRepository() {
        printBuffers = new PrintBuffer[3];
        for (int index = 0; index < 3; index++) {
            printBuffers[index] = new PrintBuffer();
        }
    }

    public PrintBuffer getPrintBuffer(int bufferId) {
        return printBuffers[bufferId];
    }
}
