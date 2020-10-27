package com.david.incubator.serial.nibp.strategy;

import com.david.core.serial.BaseCommand;
import com.david.incubator.serial.nibp.NibpCommandControl;
import com.david.incubator.serial.nibp.NibpResponseCommand;
import com.david.incubator.serial.nibp.command.GetFirstCuffPressureCommand;

import io.reactivex.rxjava3.functions.BiFunction;

public class SetInitialStrategy implements BiFunction<BaseCommand, NibpResponseCommand, Boolean> {

    private final NibpCommandControl nibpCommandControl;

    public SetInitialStrategy(NibpCommandControl nibpCommandControl) {
        this.nibpCommandControl = nibpCommandControl;
    }

    @Override
    public Boolean apply(BaseCommand requestCommand, NibpResponseCommand nibpResponseCommand) {
        switch (nibpResponseCommand.getResponseBuffer()[2]) {
            case (0x4F):
                return false;
            case (0x4B):
                GetFirstCuffPressureCommand getFirstCuffPressureCommand =
                        new GetFirstCuffPressureCommand();
                nibpCommandControl.produce(getFirstCuffPressureCommand);
                return true;
        }
        return false;
    }
}