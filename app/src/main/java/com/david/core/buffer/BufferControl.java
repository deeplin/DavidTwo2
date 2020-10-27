package com.david.core.buffer;

import androidx.core.util.Consumer;

public class BufferControl {

    private Consumer<Integer> consumer;
    private Consumer<Integer> consumer2;

    public BufferControl() {
    }

    public synchronized void produce(Integer data) {
        if (consumer != null) {
            consumer.accept(data);
        }
        if (consumer2 != null) {
            consumer2.accept(data);
        }
    }

    public synchronized void start(Consumer<Integer> consumer) {
        this.consumer = consumer;
    }

    public synchronized void start2(Consumer<Integer> consumer) {
        this.consumer2 = consumer;
    }

    public synchronized void stop() {
        consumer = null;
        consumer2 = null;
    }
}