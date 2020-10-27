package com.david.core.buffer;

import com.david.core.util.Consumer3;

public class BufferArrayControl {

    private Consumer3<Integer, Integer, int[]> consumer;

    public BufferArrayControl() {
    }

    public synchronized void produce(Integer start, Integer length, int[] dataArray) {
        if (consumer != null) {
            consumer.accept(start, length, dataArray);
        }
    }

    public synchronized void start(Consumer3<Integer, Integer, int[]> consumer) {
        this.consumer = consumer;
    }

    public synchronized void stop() {
        consumer = null;
    }
}