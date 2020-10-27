package com.david.incubator.serial.nibp.strategy;

import com.david.core.model.NibpModel;
import com.david.core.serial.BaseCommand;
import com.david.core.util.Constant;
import com.david.core.util.NumberUtil;
import com.david.incubator.serial.nibp.NibpProcessMode;
import com.david.incubator.serial.nibp.NibpResponseCommand;

import io.reactivex.rxjava3.functions.BiFunction;

public class GetBpStrategy implements BiFunction<BaseCommand, NibpResponseCommand, Boolean> {

    private final NibpModel nibpViewModel;

    public GetBpStrategy(NibpModel nibpViewModel) {
        this.nibpViewModel = nibpViewModel;
    }

    @Override
    public Boolean apply(BaseCommand requestCommand, NibpResponseCommand nibpResponseCommand) {
        switch (nibpResponseCommand.getResponseBuffer()[1]) {
            case (0x18):
                byte[] response = nibpResponseCommand.getResponseBuffer();
                if (response != null && response.length == 24 && response[20] == 0) {
                    nibpViewModel.set(NumberUtil.getShort(2, response),
                            NumberUtil.getShort(4, response), NumberUtil.getShort(18, response));
                } else {
                    nibpViewModel.set(Constant.NA_VALUE, Constant.NA_VALUE, Constant.NA_VALUE);
                    nibpViewModel.error.post(true);
                }
                nibpViewModel.processMode = NibpProcessMode.Complete;
                return true;
        }
        return false;
    }
}
