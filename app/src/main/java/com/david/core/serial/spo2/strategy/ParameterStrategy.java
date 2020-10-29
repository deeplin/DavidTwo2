package com.david.core.serial.spo2.strategy;

import com.david.core.alarm.AlarmControl;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.Spo2AverageTimeEnum;
import com.david.core.enumeration.Spo2SensEnum;
import com.david.core.model.Spo2Model;
import com.david.core.util.ContextUtil;
import com.david.core.util.LazyLiveData;
import com.david.core.util.NumberUtil;

import javax.inject.Inject;

import io.reactivex.rxjava3.functions.Consumer;

public class ParameterStrategy implements Consumer<byte[]> {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    Spo2Model spo2Model;
    @Inject
    AlarmControl alarmControl;
    @Inject
    ModuleHardware moduleHardware;

    private final LazyLiveData<Integer> spo2TextNumber;
    private final LazyLiveData<Integer> prTextNumber;
    private final LazyLiveData<Integer> piTextNumber;
    private final LazyLiveData<Integer> spcoTextNumber;
    private final LazyLiveData<Integer> spmetTextNumber;
    private final LazyLiveData<Integer> sphbTextNumber;
    private final LazyLiveData<Integer> spocTextNumber;
    private final LazyLiveData<Integer> pviTextNumber;

    @Inject
    public ParameterStrategy() {
        ContextUtil.getComponent().inject(this);
        spo2TextNumber = sensorModelRepository.getSensorModel(SensorModelEnum.Spo2).textNumber;
        prTextNumber = sensorModelRepository.getSensorModel(SensorModelEnum.Pr).textNumber;
        piTextNumber = sensorModelRepository.getSensorModel(SensorModelEnum.Pi).textNumber;
        spcoTextNumber = sensorModelRepository.getSensorModel(SensorModelEnum.Spco).textNumber;
        spmetTextNumber = sensorModelRepository.getSensorModel(SensorModelEnum.Spmet).textNumber;
        sphbTextNumber = sensorModelRepository.getSensorModel(SensorModelEnum.Sphb).textNumber;
        spocTextNumber = sensorModelRepository.getSensorModel(SensorModelEnum.Spoc).textNumber;
        pviTextNumber = sensorModelRepository.getSensorModel(SensorModelEnum.Pvi).textNumber;
    }

    @Override
    public void accept(byte[] buffer) {
        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            switch (buffer[3]) {
                //todo deeplin
                case (0x01):
                    spo2TextNumber.post((int) NumberUtil.getShortHighFirst(4, buffer));
//                    spo2Model.spo2Alarm.post((int) NumberUtil.getShortHighFirst(8, buffer));
                    break;
                case (0x02):
                    prTextNumber.post((int) NumberUtil.getShortHighFirst(4, buffer));
//                    spo2Model.prAlarm.post((int) NumberUtil.getShortHighFirst(8, buffer));
                    break;
                case (0x03):
                    piTextNumber.post((int) NumberUtil.getShortHighFirst(4, buffer));
//                    spo2Model.piAlarm.post((int) NumberUtil.getShortHighFirst(8, buffer));
                    break;
                case (0x04):
                    spcoTextNumber.post((int) NumberUtil.getShortHighFirst(4, buffer));
//                    spo2Model.spcoAlarm.post((int) NumberUtil.getShortHighFirst(8, buffer));
                    break;
                case (0x05):
                    spmetTextNumber.post((int) NumberUtil.getShortHighFirst(4, buffer));
//                    spo2Model.spmetAlarm.post((int) NumberUtil.getShortHighFirst(8, buffer));
                    break;
                case (0x07):
                    sphbTextNumber.post((int) NumberUtil.getShortHighFirst(4, buffer));
//                    spo2Model.sphbAlarm.post((int) NumberUtil.getShortHighFirst(8, buffer));
                    break;
                case (0x0C):
                    int data = NumberUtil.getLongHighFirst(buffer, 4);
//                    spo2Model.spo2SysAlarm.post(data);
                    if ((data & 1) == 0 && !spo2Model.isAlarmEnabled()) {
                        spo2Model.setAlarmEnabled();
                        spo2TextNumber.notifyChange();
                        prTextNumber.notifyChange();
                        piTextNumber.notifyChange();
                        spcoTextNumber.notifyChange();
                        spmetTextNumber.notifyChange();
                        sphbTextNumber.notifyChange();
                        spocTextNumber.notifyChange();
                        pviTextNumber.notifyChange();
                    }
                    break;
                case (0x0D):
//                    spo2Model.spo2BfcAlarm.post((int) NumberUtil.getShortHighFirst(4, buffer));
//                    spo2Model.spo2DfAlarm.post((int) NumberUtil.getShortHighFirst(6, buffer));
                    break;
                case (0x2B):
//                shareMemory.PiDelta.post(NumberUtil.getShortHighFirst(4, buffer));
                    break;
                case (0x32):
                    spocTextNumber.post((int) NumberUtil.getShortHighFirst(4, buffer));
//                    spo2Model.spocAlarm.post((int) NumberUtil.getShortHighFirst(8, buffer));
                    break;
                case (0x33):
                    pviTextNumber.post((int) NumberUtil.getShortHighFirst(4, buffer));
//                    spo2Model.pviAlarm.post((int) NumberUtil.getShortHighFirst(8, buffer));
                    break;
                case (0x10):
                    switch (buffer[4]) {
                        case (0x00):
                            spo2Model.averageTimeEnum.post(Spo2AverageTimeEnum.Zero);
                            break;
                        case (0x01):
                            spo2Model.averageTimeEnum.post(Spo2AverageTimeEnum.One);
                            break;
                        case (0x02):
                            spo2Model.averageTimeEnum.post(Spo2AverageTimeEnum.Two);
                            break;
                        case (0x03):
                            spo2Model.averageTimeEnum.post(Spo2AverageTimeEnum.Three);
                            break;
                        case (0x04):
                            spo2Model.averageTimeEnum.post(Spo2AverageTimeEnum.Four);
                            break;
                        case (0x05):
                            spo2Model.averageTimeEnum.post(Spo2AverageTimeEnum.Five);
                            break;
                        case (0x06):
                            spo2Model.averageTimeEnum.post(Spo2AverageTimeEnum.Six);
                            break;
                    }
                    switch (buffer[5]) {
                        case (0x00):
                            spo2Model.sensEnum.post(Spo2SensEnum.Max);
                            break;
                        case (0x01):
                            spo2Model.sensEnum.post(Spo2SensEnum.Normal);
                            break;
                        case (0x02):
                            spo2Model.sensEnum.post(Spo2SensEnum.APOD);
                            break;
                    }
                    switch (buffer[6]) {
                        case (0x00):
                            spo2Model.fastsatValue.post(false);
                            break;
                        case (0x01):
                            spo2Model.fastsatValue.post(true);
                            break;
                    }
                    switch (buffer[7]) {
                        case (0x00):
                            spo2Model.lfValue.post(false);
                            break;
                        case (0x01):
                            spo2Model.lfValue.post(true);
                            break;
                    }
                    switch (buffer[8]) {
                        case (0x00):
                            spo2Model.smartToneValue.post(true);
                            break;
                        case (0x01):
                            spo2Model.smartToneValue.post(false);
                            break;
                    }
                    switch (buffer[11]) {
                        case (0x00):
                            spo2Model.sphbPrecision.post(0);
                            break;
                        case (0x01):
                            spo2Model.sphbPrecision.post(1);
                            break;
                        case (0x02):
                            spo2Model.sphbPrecision.post(2);
                            break;
                    }
                    switch (buffer[12]) {
                        case (0x00):
                            spo2Model.pviAverage.post(false);
                            break;
                        case (0x01):
                            spo2Model.pviAverage.post(true);
                            break;

                    }
                    switch (buffer[13]) {
                        case (0x00):
                            spo2Model.sphbArterial.post(false);
                            break;
                        case (0x01):
                            spo2Model.sphbArterial.post(true);
                            break;
                    }
                    switch (buffer[14]) {
                        case (0x00):
                            spo2Model.sphbAverage.post(0);
                            break;
                        case (0x01):
                            spo2Model.sphbAverage.post(1);
                            break;
                        case (0x02):
                            spo2Model.sphbAverage.post(2);
                            break;
                    }
                    break;
            }
        }
    }
}