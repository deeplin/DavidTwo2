package com.david.incubator.serial.nibp.strategy;

import com.david.R;
import com.david.core.serial.BaseCommand;
import com.david.core.util.ContextUtil;
import com.david.core.util.rely.ViewUtil;
import com.david.incubator.serial.nibp.NibpResponseCommand;

import io.reactivex.rxjava3.functions.BiFunction;

public class CalibrateStrategy implements BiFunction<BaseCommand, NibpResponseCommand, Boolean> {

    public CalibrateStrategy() {
    }

    @Override
    public Boolean apply(BaseCommand requestCommand, NibpResponseCommand nibpResponseCommand) {
        switch (nibpResponseCommand.getResponseBuffer()[2]) {
            case (0x4F):
                ViewUtil.showToast(ContextUtil.getString(R.string.calibrate_success));
                return true;
            case (0x4B):
                return false;
            default:
                ViewUtil.showToast(ContextUtil.getString(R.string.calibrate_failed));
        }
        return false;
    }
}
