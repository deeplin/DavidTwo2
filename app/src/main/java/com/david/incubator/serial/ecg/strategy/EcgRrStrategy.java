package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SystemModel;
import com.david.core.util.NumberUtil;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class EcgRrStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;

    @Inject
    public EcgRrStrategy() {
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        if (!systemModel.demo.getValue()) {
            sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr).textNumber
                    .post((int) NumberUtil.getShort(0, command.getResponseBuffer()));
        }
        return false;
    }
}