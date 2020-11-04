package com.david.incubator.serial.wake;

import com.david.core.control.ConfigRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.model.WakeModel;
import com.david.core.serial.BaseCommand;
import com.david.core.serial.incubator.BaseIncubatorCommandControl;
import com.david.core.serial.incubator.IncubatorCommandParser;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.incubator.serial.wake.command.AmbientCommand;
import com.david.incubator.serial.wake.command.WakeCommand;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WakeCommandControl extends BaseIncubatorCommandControl {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    WakeModel wakeModel;
    @Inject
    ConfigRepository configRepository;

    private final AmbientCommand ambientCommand;
    private int count = 3;

    private int respChokeDelay = 0;
    private int co2ChokeDelay = 0;

    @Inject
    public WakeCommandControl() {
        super();
        ContextUtil.getComponent().inject(this);
        ambientCommand = new AmbientCommand();
        ambientCommand.setCallback((aBoolean, baseCommand) -> {
            if (aBoolean) {
                AmbientCommand ambientCommand = (AmbientCommand) baseCommand;
                incubatorModel.ambientHumidity.post(ambientCommand.getHUM());
                incubatorModel.ambientTemp.post(ambientCommand.getTEMP());
                wakeModel.chokeConnectionError.post(ambientCommand.getSTATE() == 0);
            }
        });

        wakeModel.sendChoke.observeForever(this::sendChoke);
        wakeModel.sendChokeTest.observeForever(this::sendChoke);
    }

    @Override
    public void accept(Long aLong) {
        super.accept(aLong);

        boolean choke = false;
        if (configRepository.getConfig(ConfigEnum.WakeRespStatus).getValue() > 0) {
            SensorModel ecgRrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr);
            if (ecgRrModel.textNumber.getValue() > 0 && ecgRrModel.textNumber.getValue() < 7) {
                respChokeDelay++;
            } else {
                respChokeDelay = 0;
            }

            if (respChokeDelay >= getDelay()) {
                choke = true;
            }
        }

        if (configRepository.getConfig(ConfigEnum.WakeCo2Status).getValue() > 0) {
            SensorModel co2RrModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Rr);
            if (co2RrModel.textNumber.getValue() > 0 && co2RrModel.textNumber.getValue() < 7) {
                co2ChokeDelay++;
            } else {
                co2ChokeDelay = 0;
            }
            if (co2ChokeDelay >= getDelay()) {
                choke = true;
            }
        }

        SensorModel hrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgHr);
        if (hrModel.textNumber.getValue() >= 0 &&
                hrModel.textNumber.getValue() < configRepository.getConfig(ConfigEnum.WakeHr).getValue()) {
            choke = true;
        }

        SensorModel spo2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Spo2);
        if (spo2Model.textNumber.getValue() >= 0 &&
                spo2Model.textNumber.getValue() < configRepository.getConfig(ConfigEnum.WakeSpo2).getValue() * 10) {
            choke = true;
        }

        if (choke) {
            if (Objects.equals(wakeModel.sendChoke.getValue(), true)) {
                wakeModel.sendChoke.notifyChange();
            } else {
                wakeModel.sendChoke.post(true);
            }
        } else {
            wakeModel.sendChoke.post(false);
        }

        if (count >= 3) {
            count = 0;
            produce(ambientCommand);
        }
        count++;
    }

    @Override
    protected void parseResponseCommand() throws Exception {
        BaseCommand baseCommand = commandList.get(0);
//        LoggerUtil.se(String.format(Locale.US, "WakeCommandControl receive: %s %s",
//                baseCommand.getClass().getSimpleName(),
//                new String(responseBuffer, 0, responseBufferLength)));
        int length = responseBufferLength -= 4;
        responseBufferLength = 0;
        IncubatorCommandParser.parse(baseCommand, responseBuffer, length);
    }

    @Override
    protected void setConnectionError(boolean status) {
        //todo deeplin
//        wakeModel.connectionError.post(status);
        if (status) {
            incubatorModel.ambientTemp.post(Constant.NA_VALUE);
            incubatorModel.ambientHumidity.post(Constant.NA_VALUE);
        }
    }

    protected int getResponseDelay() {
        return 200;
    }

    private void sendChoke(boolean aBoolean) {
        WakeCommand wakeCommand = new WakeCommand();
        if (aBoolean) {
            wakeCommand.set(configRepository.getConfig(ConfigEnum.WakeVibrationIntensity).getValue());
        } else {
            wakeCommand.set(-1);
        }
        produce(wakeCommand);
    }

    private int getDelay() {
        if (configRepository.getConfig(ConfigEnum.Co2ChokeDelay).getValue() > 0) {
            return 15 + 5 * configRepository.getConfig(ConfigEnum.Co2ChokeDelay).getValue();
        } else {
            return 0;
        }
    }
}