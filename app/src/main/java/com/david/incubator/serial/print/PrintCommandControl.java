package com.david.incubator.serial.print;

import androidx.lifecycle.Observer;

import com.david.core.alarm.AlarmRepository;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.model.PrintModel;
import com.david.core.util.LoggerUtil;
import com.david.incubator.serial.ecg.EcgCommandControl;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrintCommandControl extends EcgCommandControl {

    @Inject
    PrintCommandParser printCommandParser;
    @Inject
    PrintModel printModel;
    @Inject
    PrintCommandSender printCommandSender;
    @Inject
    AlarmRepository alarmRepository;

    private final Observer<Boolean> outOfPaperCallback;
    private final Observer<Boolean> printBusyCallback;

    private int sleepSendThread;

    @Inject
    public PrintCommandControl() {
        super();
        outOfPaperCallback = aBoolean -> {
            alarmRepository.produceAlarmFromAndroid(AlarmWordEnum.PRINT_PAPER, aBoolean);
            LoggerUtil.se("Out of Paper " + aBoolean);
            if (aBoolean) {
                sleepSendThread = Integer.MAX_VALUE;
            } else {
                sleepSendThread = 0;
            }
        };

        printBusyCallback = aBoolean -> {
            LoggerUtil.se("Print busy. " + aBoolean);
            sleepSendThread = 1;
        };
    }

    @Override
    protected boolean sleepSendThread() {
        if (sleepSendThread <= 0) {
            return false;
        } else {
            sleepSendThread--;
            return true;
        }
    }

    @Override
    protected void setConnectionError(boolean status) {
    }

    public void attach() {
        printModel.outOfPaper.observeForever(outOfPaperCallback);
        printModel.bufferFull.observeForever(printBusyCallback);
        printCommandSender.setPrintCommandControl(this);
        printCommandSender.printInitialBitmap();
//        Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> printCommandSender.printInitialBitmap());
    }

    public void detach() {
        printModel.bufferFull.removeObserver(printBusyCallback);
        printModel.outOfPaper.removeObserver(outOfPaperCallback);
    }

    public void init() {
        super.init();
        setFunction(printCommandParser);
    }

    private void pause() {
        controlLock.lock();
    }

    private void resume() {
        controlLock.unlock();
    }

    public void clearCommand() {
        try {
            controlLock.lock();
            commandList.clear();
        } finally {
            controlLock.unlock();
        }
    }
}