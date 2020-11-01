package com.david.incubator.serial.print;

import androidx.arch.core.util.Function;

import com.david.core.alarm.AlarmRepository;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.model.PrintModel;
import com.david.core.util.LoggerUtil;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import java.util.Locale;

import javax.inject.Inject;

public class PrintCommandParser implements Function<EcgResponseCommand, Boolean> {

    @Inject
    PrintCommandSender printCommandSender;
    @Inject
    PrintModel printModel;
    @Inject
    AlarmRepository alarmRepository;

    @Inject
    public PrintCommandParser() {
    }

    @Override
    public Boolean apply(EcgResponseCommand ecgResponseCommand) {
//        LoggerUtil.se(String.format(Locale.US, "PRINT command %02x ", ecgResponseCommand.getType()));
        switch (ecgResponseCommand.getType()) {
            case (0x15):
                //打印速度控制帧
            case (0x17):
                //打印停止帧
            case (0x19):
                //走纸帧
            case (0x1B):
                //位图帧
                return true;
            case (0x40):
                //启动帧
                printCommandSender.sendAckCommand((byte) 0x40);
                return false;
            case (0x41):
                //状态帧
                printCommandSender.sendAckCommand((byte) 0x41);
                byte statusByte = ecgResponseCommand.getResponseBuffer()[0];
                boolean status = ((statusByte & 0x01) > 0);
                printModel.outOfPaper.post(status);
//                LoggerUtil.se(String.format("Print Status: %x", statusByte));

                boolean printError = (statusByte & 0x16) > 0;
                alarmRepository.produceAlarmFromAndroid(AlarmWordEnum.PRINT_ERR, printError);
                return false;
            case (0x42):
                //缓冲区状态帧
                byte busyByte = ecgResponseCommand.getResponseBuffer()[0];
                LoggerUtil.se(String.format("Print busy status: %x", busyByte));
                printModel.bufferFull.post(busyByte > 0);
                printCommandSender.sendAckCommand((byte) 0x42);
                return false;
            case (0x5B):
                //保活帧
                return false;
            default:
                LoggerUtil.e(String.format(Locale.US, "Print unknown command %02x ", ecgResponseCommand.getType()));
                return false;
        }
    }
}