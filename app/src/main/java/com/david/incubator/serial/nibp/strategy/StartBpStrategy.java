package com.david.incubator.serial.nibp.strategy;

import com.david.core.model.NibpModel;
import com.david.core.serial.BaseCommand;
import com.david.core.util.NumberUtil;
import com.david.incubator.serial.nibp.NibpCommandControl;
import com.david.incubator.serial.nibp.NibpProcessMode;
import com.david.incubator.serial.nibp.NibpResponseCommand;
import com.david.incubator.serial.nibp.command.GetBPCommand;

import io.reactivex.rxjava3.functions.BiFunction;

public class StartBpStrategy implements BiFunction<BaseCommand, NibpResponseCommand, Boolean> {

    private final NibpCommandControl nibpCommandControl;
    private final NibpModel nibpViewModel;

    public StartBpStrategy(NibpCommandControl nibpCommandControl, NibpModel nibpViewModel) {
        this.nibpCommandControl = nibpCommandControl;
        this.nibpViewModel = nibpViewModel;
    }

    @Override
    public Boolean apply(BaseCommand requestCommand, NibpResponseCommand nibpResponseCommand) {
        byte[] responseBuffer = nibpResponseCommand.getResponseBuffer();
        if (responseBuffer.length == 4) {
            switch (nibpResponseCommand.getResponseBuffer()[2]) {
                case (0x4F):
                    nibpViewModel.processMode = NibpProcessMode.CuffPressure;
                    return true;
                case (0x4B):
                    nibpViewModel.processMode = NibpProcessMode.Measure;

                    GetBPCommand getBPCommand = new GetBPCommand((aBoolean, baseCommand) -> {
                        if (!aBoolean) {
                            nibpViewModel.error.post(true);
                        }
                    });
                    nibpCommandControl.produce(getBPCommand);
                    return false;
            }
        } else if (responseBuffer.length == 5) {
            byte[] response = nibpResponseCommand.getResponseBuffer();
            if (response.length == 5) {
                nibpViewModel.setCuffPressureValue(NumberUtil.getShort(2, response));
            } else {
                nibpViewModel.error.post(true);
            }
            return true;
        }
        return false;
    }
}