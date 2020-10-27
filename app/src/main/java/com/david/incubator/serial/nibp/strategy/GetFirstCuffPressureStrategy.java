package com.david.incubator.serial.nibp.strategy;

import com.david.R;
import com.david.core.model.NibpModel;
import com.david.core.serial.BaseCommand;
import com.david.core.util.ContextUtil;
import com.david.core.util.LoggerUtil;
import com.david.core.util.NumberUtil;
import com.david.incubator.serial.nibp.NibpCommandControl;
import com.david.incubator.serial.nibp.NibpProcessMode;
import com.david.incubator.serial.nibp.NibpResponseCommand;
import com.david.incubator.serial.nibp.command.StartBpCommand;

import java.util.Objects;

import io.reactivex.rxjava3.functions.BiFunction;

public class GetFirstCuffPressureStrategy implements BiFunction<BaseCommand, NibpResponseCommand, Boolean> {

    private final NibpCommandControl nibpCommandControl;
    private final NibpModel nibpModel;

    public GetFirstCuffPressureStrategy(NibpCommandControl nibpCommandControl, NibpModel nibpModel) {
        this.nibpCommandControl = nibpCommandControl;
        this.nibpModel = nibpModel;
    }

    @Override
    public Boolean apply(BaseCommand requestCommand, NibpResponseCommand nibpResponseCommand) {
        byte[] response = nibpResponseCommand.getResponseBuffer();
        if (response.length == 5) {
            int pressure = NumberUtil.getShort(2, response);
            if (Objects.equals(nibpModel.processMode, NibpProcessMode.FirstRoundInitial)) {
                if (pressure < 5) {
                    nibpModel.setCuffPressureValue(pressure);
                    nibpModel.functionValue.post(null);
                    nibpModel.subFieldString.post("");
                    StartBpCommand startBpCommand =
                            new StartBpCommand();
                    nibpCommandControl.produce(startBpCommand);
                } else {
                    nibpModel.fieldString.post(ContextUtil.getString(R.string.nibp_delay));
                    nibpModel.functionValue.post(null);
                    nibpModel.subFieldString.post("");
                    nibpModel.processMode = NibpProcessMode.Complete;
                }
            } else if (Objects.equals(nibpModel.processMode, NibpProcessMode.PressureTest)) {
                nibpModel.currentPressure.post(pressure);
            } else {
                nibpCommandControl.setDelay(pressure < 5);
            }
            return true;
        } else {
            nibpModel.error.post(true);
            nibpCommandControl.running.set(false);
            return false;
        }
    }
}