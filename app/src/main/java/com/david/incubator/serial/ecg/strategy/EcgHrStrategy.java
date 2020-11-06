package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.EcgModel;
import com.david.core.model.SystemModel;
import com.david.core.util.NumberUtil;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class EcgHrStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    EcgModel ecgModel;

    @Inject
    public EcgHrStrategy() {
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        if (!systemModel.demo.getValue()) {
            int hr = NumberUtil.getShort(0, command.getResponseBuffer());
            if (hr > 5 && !ecgModel.isAlarmEnabled()) {
                ecgModel.setAlarmEnabled();
                sensorModelRepository.getSensorModel(SensorModelEnum.EcgHr).textNumber.notifyChange();
                sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr).textNumber.notifyChange();
            }
            sensorModelRepository.getSensorModel(SensorModelEnum.EcgHr).textNumber.post(hr);
        }
        return false;
    }
}