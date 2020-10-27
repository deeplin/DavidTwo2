package com.david.core.serial.spo2.strategy;

import com.david.core.buffer.BufferRepository;
import com.david.core.model.Spo2Model;
import com.david.core.model.SystemModel;
import com.david.core.util.Constant;
import com.david.core.util.NumberUtil;

import javax.inject.Inject;

import io.reactivex.rxjava3.functions.Consumer;

public class WaveStrategy implements Consumer<byte[]> {

    @Inject
    SystemModel systemModel;
    @Inject
    Spo2Model spo2Model;
    @Inject
    BufferRepository bufferRepository;

    @Inject
    public WaveStrategy() {
    }

    @Override
    public void accept(byte[] buffer) {
        if (!systemModel.demo.getValue()) {
            if (buffer[6] < 0) {
                spo2Model.spo2Beep.notifyChange();
            }

            int ppg = NumberUtil.getShortHighFirst(3, buffer);
            boolean siq = buffer[5] != 0;
            if (siq) {
                ppg += Constant.SIQ_STEP;
            }
            bufferRepository.getSpo2Buffer().produce(ppg);
        }
    }
}