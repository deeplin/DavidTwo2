package com.david.incubator.serial.nibp;

import android.util.ArrayMap;

import com.david.core.alarm.AlarmRepository;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.model.NibpModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.BaseCommand;
import com.david.core.serial.BaseSerialControl;
import com.david.core.util.Constant;
import com.david.core.util.LazyLiveData;
import com.david.core.util.ListUtil;
import com.david.core.util.LoggerUtil;
import com.david.incubator.serial.nibp.command.AbortBpCommand;
import com.david.incubator.serial.nibp.command.ClosePneumaticsCommand;
import com.david.incubator.serial.nibp.command.GetBPCommand;
import com.david.incubator.serial.nibp.command.GetCuffPressureCommand;
import com.david.incubator.serial.nibp.command.GetFirstCuffPressureCommand;
import com.david.incubator.serial.nibp.command.NibpCalibrateCommand;
import com.david.incubator.serial.nibp.command.OpenPneumaticsCommand;
import com.david.incubator.serial.nibp.command.SetInitialCommand;
import com.david.incubator.serial.nibp.command.StartBpCommand;
import com.david.incubator.serial.nibp.strategy.AbortBpStrategy;
import com.david.incubator.serial.nibp.strategy.CalibrateStrategy;
import com.david.incubator.serial.nibp.strategy.ClosePneumaticsStrategy;
import com.david.incubator.serial.nibp.strategy.GetBpStrategy;
import com.david.incubator.serial.nibp.strategy.GetFirstCuffPressureStrategy;
import com.david.incubator.serial.nibp.strategy.OpenPneumaticsStrategy;
import com.david.incubator.serial.nibp.strategy.SetInitialStrategy;
import com.david.incubator.serial.nibp.strategy.StartBpStrategy;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.functions.BiFunction;

@Singleton
public class NibpCommandControl extends BaseSerialControl {

    @Inject
    NibpModel nibpModel;
    @Inject
    AlarmRepository alarmRepository;
    @Inject
    ConfigRepository configRepository;
    @Inject
    SystemModel systemModel;

    private final Map<Class, BiFunction<BaseCommand, NibpResponseCommand, Boolean>> strategyMap = new ArrayMap<>();

    private NibpResponseCommand nibpResponseCommand;
    private NibpCommandMode nibpCommandMode;
    private BaseCommand requestCommand;

    public final LazyLiveData<Boolean> running = new LazyLiveData<>(false);
    private int autoCount;
    private int autoInterval;
    private int delayCount;

    @Inject
    public NibpCommandControl() {
        super();
    }

    public void init() {
        super.init();
        nibpCommandMode = NibpCommandMode.Init;

        /*AbortBpCommand*/
        strategyMap.put(AbortBpCommand.class, new AbortBpStrategy());
        /*SetInitialCommand*/
        strategyMap.put(SetInitialCommand.class, new SetInitialStrategy(this));
        /*GetFirstCuffPressureCommand*/
        strategyMap.put(GetFirstCuffPressureCommand.class, new GetFirstCuffPressureStrategy(this, nibpModel));

        /*ClosePneumaticsCommand*/
        strategyMap.put(ClosePneumaticsCommand.class, new ClosePneumaticsStrategy(nibpModel));
        /*OpenPneumaticsCommand*/
        strategyMap.put(OpenPneumaticsCommand.class, new OpenPneumaticsStrategy(nibpModel));
        /*CalibrateCommand*/
        strategyMap.put(NibpCalibrateCommand.class, new CalibrateStrategy());
        /*StartBpCommand*/
        if (Constant.IS_NEONATE) {
            strategyMap.put(StartBpCommand.class, new StartBpStrategy(this, nibpModel));
        } else {
            strategyMap.put(StartBpCommand.class, new StartBpStrategy(this, nibpModel));
        }
        //GetBpCommand
        strategyMap.put(GetBPCommand.class, new GetBpStrategy(nibpModel));
    }

    public synchronized void press() {
        if (running.getValue()) {
            running.post(false);
            nibpModel.fieldString.post(null);
            nibpModel.functionValue.post(null);
            nibpModel.subFieldString.post(null);

            nibpModel.processMode = NibpProcessMode.Complete;
            AbortBpCommand abortBPCommand = new AbortBpCommand();
            produce(abortBPCommand);
        } else {
            running.post(true);
            nibpModel.processMode = NibpProcessMode.FirstRoundInitial;
            if (nibpModel.getMeasureModeId() == 1) {
                autoInterval = ListUtil.nibpAutoInterval[configRepository.getConfig(ConfigEnum.NibpInterval).getValue()];
                autoCount = autoInterval;
                delayCount = 10;
            } else if (nibpModel.getMeasureModeId() == 2) {
                autoCount = 300;
                delayCount = 10;
            }
            nibpModel.error.post(null);
            byte initialPressure = (byte) (configRepository.getConfig(ConfigEnum.NibpInitialSleevePressure).getValue() * 10 + 60);
            SetInitialCommand setInitialCommand = new SetInitialCommand(initialPressure);
            produce(setInitialCommand);
        }
    }

    @Override
    protected void parseRequest(BaseCommand baseCommand) {
        if (!(baseCommand instanceof GetCuffPressureCommand)) {
            requestCommand = baseCommand;
        }
    }

    @Override
    public void accept(Long aLong) {
        if (running.getValue()) {
            autoCount--;
            switch (nibpModel.processMode) {
                case FirstRoundInitial:
                    break;
                case CuffPressure:
                    GetCuffPressureCommand getCuffPressureCommand = new GetCuffPressureCommand();
                    produce(getCuffPressureCommand);
                    break;
                case OtherRoundInitial:
                    if (delayCount > 0) {
                        GetFirstCuffPressureCommand getFirstCuffPressureCommand =
                                new GetFirstCuffPressureCommand();
                        produce(getFirstCuffPressureCommand);
                    } else {
                        delayCount = 10;
                        nibpModel.processMode = NibpProcessMode.CuffPressure;
                        StartBpCommand startBpCommand = new StartBpCommand();
                        produce(startBpCommand);
                    }
                    break;
                case Complete: {
                    switch (nibpModel.getMeasureModeId()) {
                        case 0:
                            running.post(false);
                            break;
                        case 1:
                            if (nibpModel.getMeasureModeId() == 1) {
                                if (autoCount < 0) {
                                    nibpModel.processMode = NibpProcessMode.OtherRoundInitial;
                                    autoCount = ListUtil.nibpAutoInterval[configRepository.getConfig(ConfigEnum.NibpInterval).getValue()];
                                }
                            }
                            break;
                        case 2:
                            if (autoCount > 0) {
                                nibpModel.processMode = NibpProcessMode.OtherRoundInitial;
                            } else {
                                running.post(false);
                            }
                            break;
                    }
                }
                break;
            }
        }
    }

    @Override
    protected boolean parseResponse(byte data) throws Throwable {
        switch (nibpCommandMode) {
            case Init:
                if (data == 0x3E) {
                    nibpCommandMode = NibpCommandMode.Length;
                }
                break;
            case Length:
                nibpResponseCommand = new NibpResponseCommand(data);
                nibpResponseCommand.addData((byte) 0x3E);
                nibpResponseCommand.addData(data);
                nibpCommandMode = NibpCommandMode.Command;
                break;
            case Command:
                nibpResponseCommand.addData(data);
                if (nibpResponseCommand.isCompleted()) {
                    nibpCommandMode = NibpCommandMode.Init;
                    return handleCommand(nibpResponseCommand);
                }
                break;
        }
        return false;
    }

    @Override
    protected void setConnectionError(boolean status) {
    }

    public void setDelay(boolean success) {
        if (success) {
            delayCount--;
        } else {
            delayCount = 10;
        }
    }

    private boolean handleCommand(NibpResponseCommand nibpResponseCommand) throws Throwable {
        if (nibpResponseCommand.checkCRC()) {
            BiFunction<BaseCommand, NibpResponseCommand, Boolean> consumer =
                    strategyMap.get(requestCommand.getClass());
            if (consumer != null) {
                return consumer.apply(requestCommand, nibpResponseCommand);
            }

//            switch (requestCommand.getRequest()[1]) {
            //CalibrateTransducer
//                case (0x04):
//                    //ClosePneumaticsCommand
//                case (0x0C):
//                    //SetInitialCommand
//                case (0x17):
//                    switch (nibpResponseCommand.getBuffer()[2]) {
//                        case (0x4F):
//                            return true;
//                        case (0x4B):
//                            requestCommand.getOnCompleted().accept(true, requestCommand);
//                            break;
//                    }
//                    break;

//                case (0x79):
//                    switch (nibpResponseCommand.getBuffer()[1]) {
//                        //GetCalibrationResult
//                        case (0x06): {
//                            byte[] response = nibpResponseCommand.getBuffer();
//                            if (response != null && response.length == 6 && response[4] == 0) {
//                                requestCommand.getOnCompleted().accept(true, requestCommand);
//                            } else {
//                                requestCommand.getOnCompleted().accept(false, requestCommand);
//                            }
//                        }
//                        return true;

//                    }
//            }
        } else {
            LoggerUtil.se("Nibp CRC error.");
        }
        return false;
    }

    public void calibrate(boolean zero) {
        NibpCalibrateCommand nibpCalibrateCommand = new NibpCalibrateCommand(zero);
        produce(nibpCalibrateCommand);
    }

    public void setPneumatics(boolean open) {
        if (open) {
            OpenPneumaticsCommand openPneumaticsCommand = new OpenPneumaticsCommand();
            produce(openPneumaticsCommand);
        } else {
            ClosePneumaticsCommand closePneumaticsCommand = new ClosePneumaticsCommand(null);
            produce(closePneumaticsCommand);
        }
    }
}