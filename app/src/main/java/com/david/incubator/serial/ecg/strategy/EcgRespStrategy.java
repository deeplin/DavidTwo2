package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.core.buffer.BufferRepository;
import com.david.core.model.EcgModel;
import com.david.core.model.SystemModel;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class EcgRespStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    SystemModel systemModel;
    @Inject
    EcgModel ecgModel;
    @Inject
    BufferRepository bufferRepository;

    private final int[] ecgBuffer = new int[5];
    private int bufferId = 0;

    @Inject
    public EcgRespStrategy() {
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        if (!systemModel.demo.getValue()) {
            byte[] commandBuffer = command.getResponseBuffer();
            byte byte1 = commandBuffer[1];

            if (byte1 < 0) {
                ecgModel.respLeadOff.post(true);
                ecgBuffer[bufferId++] = 0;
            } else {
                ecgModel.respLeadOff.post(false);

                int byte0 = commandBuffer[0];
                if (byte0 < 0) {
                    byte0 += 256;
                }
                int data;
                if ((byte1 & (byte) 0x40) == 0) {
                    data = byte0 + byte1 * 256;
                } else {
                    data = byte0 + (byte1 & 0x3f) * 256;
                    data = data - 0x4000;
                }

                data += 4000;
                data /= 40;

                ecgBuffer[bufferId++] = data;
            }

            //125Hz
            if (bufferId >= 5) {
                bufferId = 0;
                bufferRepository.getEcgRrBuffer().produce(0, 5, ecgBuffer);
            }
        }
        return false;
    }
}