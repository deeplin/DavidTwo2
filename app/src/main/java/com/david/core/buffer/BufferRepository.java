package com.david.core.buffer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BufferRepository {

    private final BufferControl spo2BufferControl;
    private final BufferArrayControl ecgRrControl;
    private final BufferArrayControl co2Control;
    private final BufferArrayControl[] ecgBufferArray;

    @Inject
    BufferRepository() {
        spo2BufferControl = new BufferControl();
        ecgRrControl = new BufferArrayControl();
        co2Control = new BufferArrayControl();
        ecgBufferArray = new BufferArrayControl[7];
        for (int index = 0; index < ecgBufferArray.length; index++) {
            ecgBufferArray[index] = new BufferArrayControl();
        }
    }

    public BufferControl getSpo2Buffer() {
        return spo2BufferControl;
    }

    public BufferArrayControl getEcgRrBuffer() {
        return ecgRrControl;
    }

    public BufferArrayControl getCo2Buffer() {
        return co2Control;
    }

    public BufferArrayControl getEcgBuffer(int id) {
        return ecgBufferArray[id];
    }
}