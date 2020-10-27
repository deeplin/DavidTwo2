package com.david.core.util.rely;

import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.EcgModel;
import com.david.core.model.SensorModel;
import com.david.core.model.Spo2Model;
import com.david.core.model.SystemModel;
import com.david.core.util.ILifeCycle;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class BeepUtil implements ILifeCycle {

    @Inject
    SoundUtil soundUtil;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    Spo2Model spo2Model;
    @Inject
    EcgModel ecgModel;
    @Inject
    SystemModel systemModel;

    private final Observer<Boolean> playSpo2Observer;
    private final Observer<Boolean> playHrObserver;
    private final Observer<Boolean> selectHrObserver;

    @Inject
    public BeepUtil() {
        playSpo2Observer = aBoolean -> {
            if (spo2Model.smartToneValue.getValue()) {
                SensorModel spo2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Spo2);
                int spo2 = spo2Model.textNumber.getValue();
                switch (spo2) {
                    case (100):
                        soundUtil.playPulse(R.raw.pulse_100);
                        break;
                    case (99):
                        soundUtil.playPulse(R.raw.pulse_99);
                        break;
                    case (98):
                        soundUtil.playPulse(R.raw.pulse_98);
                        break;
                    case (97):
                        soundUtil.playPulse(R.raw.pulse_97);
                        break;
                    case (96):
                        soundUtil.playPulse(R.raw.pulse_96);
                        break;
                    case (95):
                        soundUtil.playPulse(R.raw.pulse_95);
                        break;
                    case (94):
                        soundUtil.playPulse(R.raw.pulse_94);
                        break;
                    case (93):
                        soundUtil.playPulse(R.raw.pulse_93);
                        break;
                    case (92):
                        soundUtil.playPulse(R.raw.pulse_92);
                        break;
                    case (91):
                        soundUtil.playPulse(R.raw.pulse_91);
                        break;
                    case (90):
                        soundUtil.playPulse(R.raw.pulse_90);
                        break;
                    default:
                        if (spo2 >= 85) {
                            soundUtil.playPulse(R.raw.pulse_85);
                        } else {
                            soundUtil.playPulse(R.raw.pulse_80);
                        }
                        break;
                }
            } else {
                soundUtil.playPulse(R.raw.pulse_100);
            }
        };

        selectHrObserver = aBoolean -> {
            if (aBoolean) {
                spo2Model.spo2Beep.removeObserver(playSpo2Observer);
            } else {
                spo2Model.spo2Beep.observeForever(playSpo2Observer);
            }
        };

        playHrObserver = aBoolean -> soundUtil.playPulse(R.raw.heart_beep);
    }

    @Override
    public void attach() {
        systemModel.selectHr.observeForever(selectHrObserver);
        ecgModel.ecgBeep.observeForever(playHrObserver);
    }

    @Override
    public void detach() {
        ecgModel.ecgBeep.removeObserver(playHrObserver);
        systemModel.selectHr.removeObserver(selectHrObserver);
        spo2Model.spo2Beep.removeObserver(playSpo2Observer);
    }
}
