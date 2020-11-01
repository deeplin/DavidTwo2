package com.david.incubator.serial.print;

import android.content.Context;
import android.graphics.Bitmap;

import com.david.core.database.LastUser;
import com.david.core.database.entity.UserEntity;
import com.david.core.util.PrintUtil;
import com.david.core.util.TimeUtil;
import com.david.incubator.print.ui.PrintHeadLayout;
import com.david.incubator.serial.print.command.PrintAckCommand;
import com.david.incubator.serial.print.command.PrintBitmapCommand;
import com.david.incubator.serial.print.command.PrintForwardCommand;
import com.david.incubator.serial.print.command.PrintSpeedCommand;
import com.david.incubator.serial.print.command.PrintStopCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrintCommandSender {

    @Inject
    LastUser lastUser;

    private boolean running;
    private PrintCommandControl printCommandControl;

    @Inject
    PrintCommandSender() {
    }

    public void setPrintCommandControl(PrintCommandControl printCommandControl) {
        this.printCommandControl = printCommandControl;
    }

    public void printTableHead(Context context, long time) {
        final PrintHeadLayout printHeadLayout = new PrintHeadLayout(context);
        UserEntity userEntity = lastUser.getUserEntity();
        printHeadLayout.setUserModel(userEntity, TimeUtil.getTimeFromSecond(time, TimeUtil.FullTime));
        Bitmap headerBitmap = PrintUtil.measureAndGetBitmapByView(printHeadLayout,
                printHeadLayout.getChildAt(0).getLayoutParams().width, printHeadLayout.getChildAt(0).getLayoutParams().height);
        produce(headerBitmap);
    }

    public void produce(Bitmap bitmap) {
        running = true;
        for (int row = 0; row < bitmap.getWidth(); ) {
            PrintBitmapCommand command = new PrintBitmapCommand();
            command.setSequenceId((byte) row);
            for (int col = 0; col < bitmap.getHeight(); col++) {
                int color = bitmap.getPixel(row, col);
                if (color != 0) {
                    command.setBit(bitmap.getHeight() - 1 - col);
                }
            }
            if (!running) {
                return;
            }
            printCommandControl.produce(true, command);
            row++;
        }
    }

    public void setSpeedCommand(byte speed) {
        PrintSpeedCommand command = new PrintSpeedCommand(speed);
        printCommandControl.produce(command);
    }

    public void printInitialBitmap() {
        running = true;
        for (int row = 0; row < 150 && running; row++) {
            PrintBitmapCommand command = new PrintBitmapCommand();
            command.setSequenceId((byte) row);
            if (row < 100 && row % 10 < 5) {
                command.setBit(81);
                command.setBit(82);
                command.setBit(162);
                command.setBit(163);
                command.setBit(243);
                command.setBit(244);
            }
            printCommandControl.produce(command);
        }
    }

    public void forwardPaper() {
        Bitmap forwardPaperBitmap = Bitmap.createBitmap(200, 324, Bitmap.Config.ARGB_8888);
        produce(forwardPaperBitmap);
//        PrintForwardCommand printForwardCommand = new PrintForwardCommand();
//        printForwardCommand.setForward((byte) 200);
//        printCommandControl.produce(printForwardCommand);
    }

    public void stopPrint() {
        try {
            running = false;
            printCommandControl.clearCommand();
            PrintStopCommand printStopCommand = new PrintStopCommand();
            printCommandControl.produce(printStopCommand);
        } catch (Exception ignored) {
        }
    }

    public void sendAckCommand(byte type) {
        PrintAckCommand ackCommand = new PrintAckCommand();
        ackCommand.setType(type);
        printCommandControl.produce(ackCommand);
    }
}