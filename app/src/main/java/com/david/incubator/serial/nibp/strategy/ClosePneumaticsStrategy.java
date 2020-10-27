package com.david.incubator.serial.nibp.strategy;

import com.david.core.model.NibpModel;
import com.david.core.serial.BaseCommand;
import com.david.incubator.serial.nibp.NibpResponseCommand;

import io.reactivex.rxjava3.functions.BiFunction;

public class ClosePneumaticsStrategy implements BiFunction<BaseCommand, NibpResponseCommand, Boolean> {

    private final NibpModel nibpModel;

    public ClosePneumaticsStrategy(NibpModel nibpModel) {
        this.nibpModel = nibpModel;
    }

    @Override
    public Boolean apply(BaseCommand requestCommand, NibpResponseCommand nibpResponseCommand) {
        byte[] response = nibpResponseCommand.getResponseBuffer();
        switch (response[2]) {
            case (0x4F):
                nibpModel.nibpCal.post(true);
                break;
            case (0x42):
                nibpModel.nibpCal.post(false);
                break;
        }
        return true;
    }
}
