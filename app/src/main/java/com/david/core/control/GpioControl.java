package com.david.core.control;

import com.david.core.model.IncubatorModel;
import com.david.core.util.GpioUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.IntervalUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.functions.Consumer;

@Singleton
public class GpioControl implements ILifeCycle, Consumer<Long> {

    @Inject
    IntervalUtil intervalUtil;
    @Inject
    IncubatorModel incubatorModel;

    @Inject
    public GpioControl() {
    }

    @Override
    public void attach() {
        intervalUtil.addMilliSecond500Consumer(GpioControl.class, this);
    }

    @Override
    public void detach() {
        intervalUtil.removeMillisecond500Consumer(GpioControl.class);
    }

    @Override
    public void accept(Long aLong) {
        incubatorModel.gpio.post(GpioUtil.read());
    }
}
