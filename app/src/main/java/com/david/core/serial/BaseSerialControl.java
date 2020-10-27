package com.david.core.serial;

import com.david.core.serial.incubator.IncubatorCommandControl;
import com.david.core.util.Constant;
import com.david.core.util.IntervalUtil;
import com.david.core.util.LoggerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

import io.reactivex.rxjava3.functions.Consumer;

public abstract class BaseSerialControl implements Consumer<Long> {

    @Inject
    SerialPortControl serialPortControl;
    @Inject
    IntervalUtil intervalUtil;

    protected final Lock controlLock;
    private final Condition queueEmptyCondition;
    private final Condition commandCompletedCondition;
    protected final List<BaseCommand> commandList;

    private SendThread sendThread;
    private ReceiveThread receiveThread;

    private int responseCount;
    private final int responseDelay;

    public BaseSerialControl() {
        controlLock = new ReentrantLock();
        queueEmptyCondition = controlLock.newCondition();
        commandCompletedCondition = controlLock.newCondition();
        commandList = new ArrayList<>();
        responseCount = 0;
        responseDelay = getResponseDelay();
    }

    protected int getResponseDelay() {
        return 5;
    }

    @Override
    public void accept(Long aLong) {
        if (aLong % Constant.MAX_SERIAL_EXECUTE_TIME == 0) {
            if (responseCount > 0) {
                setConnectionError(false);
                responseCount = 0;
            } else {
                setConnectionError(true);
            }
        }
    }

    public synchronized void init() {
        sendThread = new SendThread(getClass().getName() + "_send");
        sendThread.start();
        receiveThread = new ReceiveThread(getClass().getName() + "_receive");
        receiveThread.start();
        intervalUtil.addSecondConsumer(getClass(), this);
    }

    public synchronized void open(String deviceName, int baudrate) throws SecurityException, IOException {
        serialPortControl.open(deviceName, baudrate);
    }

    public synchronized void close() {
        intervalUtil.removeSecondConsumer(getClass());
        receiveThread.close();
        sendThread.close();
        try {
            serialPortControl.close();
        } catch (Exception e) {
            LoggerUtil.e(e);
        }
        try {
            controlLock.lock();
            commandCompletedCondition.signalAll();
        } finally {
            controlLock.unlock();
        }
        commandList.clear();
    }

    public void produce(BaseCommand baseCommand) {
        produce(false, baseCommand);
    }

    public void produce(boolean force, BaseCommand baseCommand) {
        try {
            controlLock.lock();
            if (!commandList.contains(baseCommand) || force) {
                commandList.add(baseCommand);
                queueEmptyCondition.signal();
            }
        } finally {
            controlLock.unlock();
        }
    }

    protected abstract boolean parseResponse(byte data) throws Throwable;

    protected void parseResponseCommand() throws Exception {
    }

    protected void parseRequest(BaseCommand baseCommand) {
    }

    protected abstract void setConnectionError(boolean status);

    protected boolean sleepSendThread() {
        return false;
    }

    class SendThread extends Thread {

        private boolean running = true;

        public SendThread(String threadName) {
            super(threadName);
        }

        @Override
        public void run() {
            while (running) {
                try {
                    controlLock.lock();

                    while (sleepSendThread()) {
                        Thread.sleep(200);
                    }

                    while (commandList.isEmpty()) {
                        queueEmptyCondition.await();
                    }
                    BaseCommand baseCommand = commandList.get(0);

                    if (baseCommand.underExecuteTime()) {
                        parseRequest(baseCommand);
                        serialPortControl.write(baseCommand.getRequest(), baseCommand.getRequestLength());

                        if (BaseSerialControl.this instanceof IncubatorCommandControl) {
                            LoggerUtil.i(String.format(Locale.US, "IncubatorCommandControl send: %s",
                                    new String(baseCommand.getRequest())));
                        }

//                        if (BaseSerialControl.this instanceof EcgCommandControl && !(BaseSerialControl.this instanceof PrintCommandControl)) {
//                            LoggerUtil.se(String.format(Locale.US, "EcgCommandControl send: %s %s",
//                                    baseCommand.getClass().getSimpleName(), StringUtil.byteArrayToHex(baseCommand.getRequest())));
//                        }

//                        if (BaseSerialControl.this instanceof NibpCommandControl) {
//                            LoggerUtil.se(String.format(Locale.US, "send: %s",
//                                    baseCommand.getClass()));
//                        }

                        if (baseCommand.hasResponse()) {
                            commandCompletedCondition.await(1, TimeUnit.SECONDS);
                        } else {
                            commandList.remove(0);
                        }
                    } else {
                        commandList.remove(0);
                    }
                } catch (Throwable e) {
                    LoggerUtil.e(e);
                } finally {
                    controlLock.unlock();
                }
            }
        }

        public void close() {
            running = false;
        }
    }

    class ReceiveThread extends Thread {

        private boolean running = true;

        public ReceiveThread(String threadName) {
            super(threadName);
        }

        @Override
        public void run() {
            byte[] responseBuffer = new byte[Constant.SERIAL_RESPONSE_BUFFER_SIZE];
            while (running) {
                try {
                    int bufferLength = serialPortControl.read(responseBuffer, 0, Constant.SERIAL_RESPONSE_BUFFER_SIZE);
                    responseCount += bufferLength;

//                    if (BaseSerialControl.this instanceof NibpCommandControl) {
//                        LoggerUtil.se(String.format(Locale.US, "NibpCommandControl receive: %s",
//                                new String(responseBuffer, 0, bufferLength)));
//                    }

//                    if (BaseSerialControl.this instanceof NibpCommandControl) {
//                        LoggerUtil.se("Receive: " + bufferLength + " " + StringUtil.byteArrayToHex(bufferLength, responseBuffer));
//                    }
//                    if (BaseSerialControl.this instanceof EcgCommandControl && !(BaseSerialControl.this instanceof PrintCommandControl)) {
//                        LoggerUtil.se("ecg length " + bufferLength);
//                    }
                    for (int index = 0; index < bufferLength; index++) {
                        if (parseResponse(responseBuffer[index])) {
                            try {
                                setConnectionError(false);
                                controlLock.lock();
                                if (!commandList.isEmpty()) {
                                    parseResponseCommand();
                                    BaseCommand baseCommand = commandList.remove(0);
                                    baseCommand.executeCallback(true);
                                    commandCompletedCondition.signal();
                                }
                            } finally {
                                controlLock.unlock();
                            }
                        }
                    }
                    Thread.sleep(responseDelay);
                } catch (Throwable e) {
                    LoggerUtil.e(e);
                }
            }
        }

        public void close() {
            running = false;
        }
    }
}