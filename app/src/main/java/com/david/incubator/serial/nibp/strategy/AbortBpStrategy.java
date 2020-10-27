package com.david.incubator.serial.nibp.strategy;

import com.david.core.serial.BaseCommand;
import com.david.incubator.serial.nibp.NibpResponseCommand;

import javax.inject.Inject;

import io.reactivex.rxjava3.functions.BiFunction;

public class AbortBpStrategy implements BiFunction<BaseCommand, NibpResponseCommand, Boolean> {

    public AbortBpStrategy() {
    }

    @Override
    public Boolean apply(BaseCommand requestCommand, NibpResponseCommand nibpResponseCommand) {
        switch (nibpResponseCommand.getResponseBuffer()[2]) {
            case (0x41):
                return true;
            case (0x4B):
                return false;
        }
        return false;
    }
}
