package com.david.core.serial.spo2;

import android.util.ArrayMap;

import com.david.core.alarm.AlarmRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.AlarmCategoryEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.model.Spo2Model;
import com.david.core.model.SystemModel;
import com.david.core.serial.BaseCommand;
import com.david.core.serial.BaseSerialControl;
import com.david.core.serial.spo2.command.ActivateCommand;
import com.david.core.serial.spo2.command.BoardInfoCommand;
import com.david.core.serial.spo2.command.SetBoardCommand;
import com.david.core.serial.spo2.command.SetExceptionOutputCommand;
import com.david.core.serial.spo2.command.SetupCommand;
import com.david.core.serial.spo2.command.UnlockCommand;
import com.david.core.serial.spo2.command.WaveFormCommand;
import com.david.core.serial.spo2.strategy.ActivateStrategy;
import com.david.core.serial.spo2.strategy.BoardInfoStrategy;
import com.david.core.serial.spo2.strategy.ParameterStrategy;
import com.david.core.serial.spo2.strategy.SetBoardStrategy;
import com.david.core.serial.spo2.strategy.SetExceptionStrategy;
import com.david.core.serial.spo2.strategy.SetupStrategy;
import com.david.core.serial.spo2.strategy.UnlockStrategy;
import com.david.core.serial.spo2.strategy.WaveFormStrategy;
import com.david.core.serial.spo2.strategy.WaveStrategy;
import com.david.core.util.Constant;
import com.david.core.util.LazyLiveData;
import com.david.core.util.LoggerUtil;
import com.david.core.util.StringUtil;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.functions.Consumer;

@Singleton
public class Spo2CommandControl extends BaseSerialControl {

    @Inject
    Spo2ResponseCommand spo2ResponseCommand;
    @Inject
    AlarmRepository alarmRepository;
    @Inject
    ParameterStrategy parameterStrategy;
    @Inject
    WaveStrategy waveStrategy;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    SystemModel systemModel;
    @Inject
    Spo2Model spo2Model;

    private final Map<String, Consumer<byte[]>> strategyMap;

    public final LazyLiveData<Boolean> errorOccur = new LazyLiveData<>();

    @Inject
    public Spo2CommandControl() {
        super();
        strategyMap = new ArrayMap<>();

        strategyMap.put(BoardInfoCommand.class.getName(), new BoardInfoStrategy(this));
        strategyMap.put(UnlockCommand.class.getName(), new UnlockStrategy(this));
        strategyMap.put(ActivateCommand.class.getName(), new ActivateStrategy(this));
        strategyMap.put(WaveFormCommand.class.getName(), new WaveFormStrategy(this));
        strategyMap.put(SetupCommand.class.getName(), new SetupStrategy(this));

        strategyMap.put(SetExceptionOutputCommand.class.getName(), new SetExceptionStrategy(this));
        strategyMap.put(SetBoardCommand.class.getName(), new SetBoardStrategy(this));
    }

    @Override
    protected boolean parseResponse(byte data) throws Throwable {
        boolean status = false;
        spo2ResponseCommand.addData(data);
        if (spo2ResponseCommand.isCompleted()) {
//            LoggerUtil.e(String.format(Locale.US, "Spo2CommandControl receive: %s",
//                    StringUtil.byteArrayToHex(spo2ResponseCommand.getBuffer()[1] + 4, spo2ResponseCommand.getBuffer())));
            status = handleCommand(spo2ResponseCommand);
            spo2ResponseCommand.reset();
        }
        return status;
    }

    @Override
    protected void setConnectionError(boolean status) {
        spo2Model.setAlarm(AlarmCategoryEnum.Spo2_Con, status ? 1 : 0);
        if (status) {
            errorOccur.notifyChange();
            if (!systemModel.demo.getValue()) {
                for (int index = SensorModelEnum.Spo2.ordinal(); index <= SensorModelEnum.Pvi.ordinal(); index++) {
                    SensorModel sensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.values()[index]);
                    sensorModel.textNumber.post(Constant.NA_VALUE);
                }
            }
        }
    }

    private boolean handleCommand(Spo2ResponseCommand spo2ResponseCommand) throws Throwable {
        if (spo2ResponseCommand.checkCRC()) {
            byte[] buffer = spo2ResponseCommand.getResponseBuffer();
            switch (buffer[2]) {
                case (0x01):
                    BaseCommand baseCommand = commandList.get(0);
                    Consumer<byte[]> strategy = strategyMap.get(baseCommand.getClass().getName());
                    if (strategy != null) {
                        strategy.accept(buffer);
                    }
                    return true;
                case (0x02):
//                    LoggerUtil.e(String.format(Locale.US, "Spo2CommandControl id 0x02: %s",
//                            StringUtil.byteArrayToHex(buffer[1] + 4, buffer)));
                    break;
                case (0x10):
                    parameterStrategy.accept(buffer);
                    break;
                case (0x11):
                    waveStrategy.accept(buffer);
                    break;
                case (0x70):
                    BoardInfoStrategy boardInfoStrategy = new BoardInfoStrategy(this);
                    boardInfoStrategy.accept(buffer);
                    return true;
                default:
                    LoggerUtil.e(String.format(Locale.US, "Spo2CommandControl unknown response %d: %s",
                            buffer[2], StringUtil.byteArrayToHex(buffer.length, buffer)));
                    break;
            }
        } else {
            LoggerUtil.e(String.format(Locale.US, "Spo2CommandControl CRC error.: %s",
                    StringUtil.byteArrayToHex(spo2ResponseCommand.getResponseBuffer()[1] + 4, spo2ResponseCommand.responseBuffer)));
        }
        return false;
    }
}