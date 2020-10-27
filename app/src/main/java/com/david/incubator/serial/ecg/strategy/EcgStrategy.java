package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.core.buffer.BufferRepository;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.model.EcgModel;
import com.david.core.model.SystemModel;
import com.david.core.util.Constant;
import com.david.core.util.NumberUtil;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class EcgStrategy implements Function<EcgResponseCommand, Boolean> {
    @Inject
    SystemModel systemModel;
    @Inject
    EcgModel ecgModel;
    @Inject
    BufferRepository bufferRepository;
    @Inject
    ConfigRepository configRepository;

    private final int[][] dataArray;

    @Inject
    public EcgStrategy() {
        dataArray = new int[EcgModel.ECG_SUM][10];
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        if (!systemModel.demo.getValue()) {
            byte[] buffer = command.getResponseBuffer();
            int bufferIndex = 4;
            for (int ecgIndex = 0; ecgIndex < 10; ecgIndex++) {
                byte mark = (byte) (buffer[bufferIndex] | buffer[bufferIndex + 28]);
                byte leadOff = (byte) (buffer[bufferIndex + 1] | buffer[bufferIndex + 29]);

                ecgModel.leadOff.post((mark & 0x1) > 0 || (leadOff & 0xc0) > 0);
                if (configRepository.getConfig(ConfigEnum.EcgLeadSetting).getValue() == 0) {
                    if (ecgModel.leadOff.getValue()) {
                        ecgModel.v1LeadOff.post(true);
                    } else {
                        ecgModel.v1LeadOff.post((mark & 0x02) > 0 || (leadOff & 0x20) > 0);
                    }
                } else {
                    ecgModel.v1LeadOff.post(false);
                }

                ecgModel.overload.post(((mark & 0x20) > 0));

                if ((!ecgModel.leadOff.getValue() && !ecgModel.v1LeadOff.getValue()) ||
                        (!ecgModel.leadOff.getValue())) {
                    boolean iPace = (mark & 0x08) > 0;
                    boolean tone = (mark & 0x40) > 0;
                    for (int bufferId = 0; bufferId < EcgModel.ECG_SUM; bufferId++) {
                        int data = NumberUtil.getShort(bufferIndex + 2 + 2 * bufferId, buffer);
                        if (iPace) {
                            data += Constant.SIQ_STEP;
                        }
                        if (tone) {
                            if (bufferId == 0) {
                                ecgModel.ecgBeep.notifyChange();
                            }
                        }
                        dataArray[bufferId][ecgIndex] = data;
                    }
                } else {
                    for (int bufferId = 0; bufferId < EcgModel.ECG_SUM; bufferId++) {
                        dataArray[bufferId][ecgIndex] = 0;
                    }
                }
                bufferIndex += 56;
            }
            for (int index = 0; index < EcgModel.ECG_SUM; index++) {
                bufferRepository.getEcgBuffer(index).produce(0, 10, dataArray[index]);
            }
        }
        return false;
    }
}