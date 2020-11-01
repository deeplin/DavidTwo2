package com.david.incubator.print;

import android.graphics.Bitmap;

import androidx.lifecycle.LifecycleOwner;

import com.david.core.control.ConfigRepository;
import com.david.core.database.LastUser;
import com.david.core.database.entity.UserEntity;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycleOwner;
import com.david.core.util.IntervalUtil;
import com.david.core.util.PrintUtil;
import com.david.core.util.TimeUtil;
import com.david.incubator.print.buffer.PrintBufferRepository;
import com.david.incubator.print.ui.PrintHeadLayout;
import com.david.incubator.print.ui.PrintWaveView;
import com.david.incubator.serial.print.PrintCommandSender;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class PrintControl implements ILifeCycleOwner, Consumer<Long> {

    @Inject
    PrintCommandSender printCommandSender;
    @Inject
    IntervalUtil intervalUtil;
    @Inject
    LastUser lastUser;
    @Inject
    ConfigRepository configRepository;
    @Inject
    PrintBufferRepository printBufferRepository;

    private final PrintWaveView printWaveView;
    private Bitmap headTableBitmap;
    private int count = 0;
    private final int[] buffer = new int[1000];
    private final float[] lastY = new float[3];

    @Inject
    public PrintControl() {
        printWaveView = new PrintWaveView(ContextUtil.getApplicationContext());
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        int speed = configRepository.getConfig(ConfigEnum.PrintSpeed).getValue();
        printCommandSender.setSpeedCommand((byte) speed);

        int width = (int) (400 * Math.pow(2, speed));
        printWaveView.setWidth(width);

        count = 0;
        final PrintHeadLayout printHeadLayout = new PrintHeadLayout(ContextUtil.getApplicationContext());
        UserEntity userEntity = lastUser.getUserEntity();
        printHeadLayout.setUserModel(userEntity, TimeUtil.getTimeFromSecond(TimeUtil.getCurrentTimeInSecond(), TimeUtil.FullTime));
        headTableBitmap = PrintUtil.measureAndGetBitmapByView(printHeadLayout,
                printHeadLayout.getChildAt(0).getLayoutParams().width, printHeadLayout.getChildAt(0).getLayoutParams().height);

        printWaveView.attach(lifecycleOwner);

        printBufferRepository.getPrintBuffer(0).start();
        printBufferRepository.getPrintBuffer(1).start();
        printBufferRepository.getPrintBuffer(2).start();

        intervalUtil.addSecondConsumer(this.getClass(), this);
    }

    @Override
    public synchronized void detach() {
        intervalUtil.removeSecondConsumer(this.getClass());

        headTableBitmap = null;

        printBufferRepository.getPrintBuffer(0).stop();
        printBufferRepository.getPrintBuffer(1).stop();
        printBufferRepository.getPrintBuffer(2).stop();

        printWaveView.detach();
        printCommandSender.stopPrint();
        printCommandSender.forwardPaper();
    }

    @Override
    public synchronized void accept(Long aLong) {
        if (count == 0) {
            if (headTableBitmap != null) {
                printCommandSender.produce(headTableBitmap);
                headTableBitmap = null;
            }
        } else if (count == 4) {
            drawInitialBitmap(0);
            drawInitialBitmap(1);
            drawInitialBitmap(2);

            Bitmap waveBitmap = PrintUtil.getBitmapByView(printWaveView, printWaveView.getWidth(), 324);
            Observable.just(printWaveView).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(printWaveView -> {
                        printWaveView.clearInfo();
                        int speed = configRepository.getConfig(ConfigEnum.PrintSpeed).getValue();
                        int width = (int) (200 * Math.pow(2, speed));
                        printWaveView.setWidth(width);
                    });

            printCommandSender.produce(waveBitmap);

            //mnt/external_sd/image
//            BitmapUtil.compressImage(waveBitmap, waveBitmap.getWidth(), waveBitmap.getHeight(),
//                    Bitmap.CompressFormat.JPEG, Bitmap.Config.ARGB_8888, 80,
//                    CameraUtil.buildDirectory(CameraUtil.IMAGE_DIRECTORY), count + "");
        } else if (count > 4 && count % 2 == 0) {
            drawBitmap(0);
            drawBitmap(1);
            drawBitmap(2);

            Bitmap waveBitmap = PrintUtil.getBitmapByView(printWaveView, printWaveView.getWidth(), 324);
            printCommandSender.produce(waveBitmap);
//
////            BitmapUtil.compressImage(waveBitmap, waveBitmap.getWidth(),  waveBitmap.getHeight(),
////                    Bitmap.CompressFormat.JPEG, Bitmap.Config.ARGB_8888, 80,
////                    Camera2Config.buildDirectory(Camera2Config.IMAGE_DIRECTORY), count+"");
        }
        count++;
    }

    public PrintWaveView getPrintWaveView() {
        return printWaveView;
    }

    private void drawInitialBitmap(int index) {
        IPrintView printView = printWaveView.getPrintView(index);
        if (printView != null) {
            int length = (int) printView.getPacketLength() * 4;
            printBufferRepository.getPrintBuffer(index).consume(buffer, length);
            lastY[index] = printWaveView.getPrintView(index).drawInitialPrintPoint(0, length, buffer);
        }
    }

    private void drawBitmap(int index) {
        IPrintView printView = printWaveView.getPrintView(index);
        if (printView != null) {
            int length = (int) printView.getPacketLength() * 2;
            printBufferRepository.getPrintBuffer(index).consume(buffer, length);
            lastY[index] = printWaveView.getPrintView(index).drawPrintPoint(0, length, buffer, lastY[index]);
        }
    }
}
