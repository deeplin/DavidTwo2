package com.david.incubator.serial.ecg;

import android.util.SparseArray;

import androidx.arch.core.util.Function;

import com.david.core.util.LoggerUtil;
import com.david.incubator.serial.ecg.strategy.AlarmRespStrategy;
import com.david.incubator.serial.ecg.strategy.AsyStrategy;
import com.david.incubator.serial.ecg.strategy.BootUpStrategy;
import com.david.incubator.serial.ecg.strategy.ConfirmStrategy;
import com.david.incubator.serial.ecg.strategy.EcgHrStrategy;
import com.david.incubator.serial.ecg.strategy.EcgRespStrategy;
import com.david.incubator.serial.ecg.strategy.EcgRrStrategy;
import com.david.incubator.serial.ecg.strategy.EcgStrategy;
import com.david.incubator.serial.ecg.strategy.FilterStrategy;
import com.david.incubator.serial.ecg.strategy.SkipStrategy;
import com.david.incubator.serial.ecg.strategy.TrapStrategy;
import com.david.incubator.serial.ecg.strategy.VfStrategy;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EcgCommandParser implements Function<EcgResponseCommand, Boolean> {

    @Inject
    FilterStrategy filterStrategy;
    @Inject
    TrapStrategy trapStrategy;
    @Inject
    ConfirmStrategy confirmStrategy;
    @Inject
    SkipStrategy skipStrategy;
    @Inject
    BootUpStrategy bootUpStrategy;
    @Inject
    EcgStrategy ecgStrategy;
    @Inject
    EcgHrStrategy ecgHrStrategy;
    @Inject
    EcgRespStrategy ecgRespStrategy;
    @Inject
    EcgRrStrategy ecgRrStrategy;
    @Inject
    AlarmRespStrategy alarmRespStrategy;
    @Inject
    VfStrategy vfStrategy;
    @Inject
    AsyStrategy asyStrategy;

    protected final SparseArray<Function<EcgResponseCommand, Boolean>> strategyMap;

    @Inject
    public EcgCommandParser() {
        strategyMap = new SparseArray<>();
    }

    public void init() {
        //17 response to Lead Mode
        strategyMap.put(0x17, confirmStrategy);
        //1F response to ComputeLead
        strategyMap.put(0x1F, confirmStrategy);
        //19 response to Filter
        strategyMap.put(0x19, filterStrategy);
        //1B response to TRAP
        strategyMap.put(0x1B, trapStrategy);
        //21 response to PACE
        strategyMap.put(0x21, confirmStrategy);
        //23 Response to USER
        strategyMap.put(0x23, confirmStrategy);
        //27 Response to Monitor
        strategyMap.put(0x27, confirmStrategy);
        //33 Response to VF
        strategyMap.put(0x33, confirmStrategy);
        //51 Response to RESP Source
        strategyMap.put(0x51, confirmStrategy);
        //61 Response to ENABLE resp
        strategyMap.put(0x61, confirmStrategy);

        //A0 VF ALARM
        strategyMap.put((byte) 0xA0, skipStrategy);
        //A1 ASYS ALARM
        strategyMap.put((byte) 0xA1, skipStrategy);
        //B0 KEEP ALIVE
        strategyMap.put((byte) 0xB0, skipStrategy);

        strategyMap.put((byte) 0x80, bootUpStrategy);
        strategyMap.put((byte) 0xBA, ecgStrategy);
        strategyMap.put((byte) 0XBB, ecgHrStrategy);
        strategyMap.put((byte) 0xD0, ecgRespStrategy);
        strategyMap.put((byte) 0xD1, ecgRrStrategy);

        strategyMap.put((byte) 0xD2, alarmRespStrategy);
        strategyMap.put((byte) 0xA0, vfStrategy);
        strategyMap.put((byte) 0xA1, asyStrategy);
    }

    @Override
    public Boolean apply(EcgResponseCommand ecgResponseCommand) {
        Function<EcgResponseCommand, Boolean> function = strategyMap.get(ecgResponseCommand.getType());
//        if (this instanceof PrintCommandParser) {
//            LoggerUtil.se("Ecg type " + StringUtil.byteToHex(ecgResponseCommand.getType()));
//        }

        if (function != null) {
            return function.apply(ecgResponseCommand);
        } else {
            if (ecgResponseCommand.getType() != 0xD3 - 256) {
                LoggerUtil.e(String.format(Locale.US, "Ecg unknown command %s %02x ", this.getClass().getSimpleName(), ecgResponseCommand.getType()));
            }
            return false;
        }
    }
}