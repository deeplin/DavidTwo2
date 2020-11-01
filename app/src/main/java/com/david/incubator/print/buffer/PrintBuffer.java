package com.david.incubator.print.buffer;

import com.david.core.util.LoggerUtil;

import java.nio.IntBuffer;

public class PrintBuffer {

    private final IntBuffer intBuffer;
    private boolean running;

    public PrintBuffer() {
        intBuffer = IntBuffer.allocate(10240);
    }

    public synchronized void produce(int start, int length, int[] buffer) {
        if (running) {
            try {
                intBuffer.put(buffer, start, length);
            } catch (Exception e) {
                LoggerUtil.e(e);
            }
        }
    }

    public synchronized void produceData(int data) {
        if (running) {
            try {
                intBuffer.put(data);
            } catch (Exception e) {
                LoggerUtil.e(e);
            }
        }
    }

    public synchronized int consume(int[] buffer, int bufferLength) {
        intBuffer.flip();

        int length;
        if (bufferLength < intBuffer.remaining()) {
            length = bufferLength;
        } else {
            length = intBuffer.remaining();
        }

        intBuffer.get(buffer, 0, length);
        intBuffer.compact();
        return length;
    }

    public synchronized void start() {
        running = true;
    }

    public synchronized void stop() {
        running = false;
        intBuffer.clear();
    }
}