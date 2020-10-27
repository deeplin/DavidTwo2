package com.david.core.serial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import android_serialport_api.SerialPort;

public class SerialPortControl {

    private SerialPort serialPort;
    private FileOutputStream outputStream;
    private FileInputStream inputStream;

    @Inject
    public SerialPortControl() {
    }

    public synchronized void open(String deviceName, int baudrate) throws SecurityException, IOException {
        serialPort = new SerialPort(new File(deviceName), baudrate, 0);
        outputStream = serialPort.getOutputStream();
        inputStream = serialPort.getInputStream();
    }

    public synchronized void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
            outputStream = null;
        }
        if (inputStream != null) {
            inputStream.close();
            inputStream = null;
        }
        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
    }

    public void write(byte[] request, int length) throws IOException {
        if (outputStream != null) {
            outputStream.write(request, 0, length);
            outputStream.flush();
        }
    }

    public int read(byte[] response, int offset, int length) throws IOException {
        if (inputStream != null) {
            return inputStream.read(response, offset, length);
        }
        return 0;
    }
}