package com.david.incubator.control;

import androidx.arch.core.util.Function;

import com.david.core.buffer.BufferRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.EcgModel;
import com.david.core.model.NibpModel;
import com.david.core.model.SensorModel;
import com.david.core.model.Spo2Model;
import com.david.core.util.Constant;
import com.david.core.util.Consumer3;
import com.david.core.util.FileUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*  demo文件每个字节表示一个点
    Ecg 采样率 250Hz 实际采样率 250Hz
    数据范围(0-0XFF) 中间零点 0X80
    0XC0 表示 +0.5mv 0X40表示 -0.5mv
    Spo2 采样率 250Hz 实际采样率 62.5Hz
    数据范围(0-0X80) 中间零点 0X40
    Ecg Resp 采样率 250Hz 实际采样率 125Hz
    数据范围(0-0XFF) 中间零点 0X80
    Co2 采样率 100Hz 实际采样率 20Hz
    数据范围(0-0XC8) 中间零点 0X64
* */
public class DemoControl implements Consumer<Long> {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    BufferRepository bufferRepository;
    @Inject
    EcgModel ecgModel;
    @Inject
    Spo2Model spo2Model;
    @Inject
    NibpModel nibpModel;
    @Inject
    ModuleHardware moduleHardware;

    private Disposable disposable;

    private final int[][] ecgBuffer = new int[7][506];
    private final int[] ecgRespBuffer = new int[960];
    private final int[] spo2Buffer = new int[500];
    private final int[] co2Buffer = new int[536];

    @Inject
    public DemoControl() {
    }

    public void attach() {
        if (moduleHardware.isActive(ModuleEnum.Ecg)) {
            Function<Integer, Integer> ecgDataFunction = aInt -> (aInt - 0X80) * 8;
            FileUtil.readSensorData("ECG-I", ecgBuffer[0], ecgDataFunction);
            FileUtil.readSensorData("ECG-II", ecgBuffer[1], ecgDataFunction);
            FileUtil.readSensorData("ECG-III", ecgBuffer[2], ecgDataFunction);
            FileUtil.readSensorData("ECG-aVR", ecgBuffer[3], ecgDataFunction);
            FileUtil.readSensorData("ECG-aVL", ecgBuffer[4], ecgDataFunction);
            FileUtil.readSensorData("ECG-aVF", ecgBuffer[5], ecgDataFunction);
            FileUtil.readSensorData("ECG-V1", ecgBuffer[6], ecgDataFunction);

            FileUtil.readSensorData("RESP", ecgRespBuffer, aShort -> (aShort - 0x80) * 2);
        }

        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            FileUtil.readSensorData("SPO2", spo2Buffer, aInt -> (aInt - 0X40) * -100);
            for (int index = 0; index < 16; index++) {
                spo2Buffer[75 + index] += Constant.SIQ_STEP;
            }
        }

        if (moduleHardware.isActive(ModuleEnum.Co2)) {
            FileUtil.readSensorData("CO2", co2Buffer, aShort -> aShort * 2);
        }

        disposable = Observable.interval(100, 64, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(this::accept);

        nibpModel.fieldString.set("111/77");
        nibpModel.subFieldString.set("95");
        nibpModel.functionValue.set("@11:18");
    }

    public void detach() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        nibpModel.fieldString.set(Constant.SENSOR_DEFAULT_ERROR_STRING);
        nibpModel.subFieldString.set(null);
        nibpModel.functionValue.set(null);
    }

    @Override
    public void accept(Long aLong) {
//        /*ECG*/
        if (moduleHardware.isActive(ModuleEnum.Ecg)) {
            SensorModel hrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgHr);
            hrModel.textNumber.post(60);

            SensorModel ecgRrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr);
            ecgRrModel.textNumber.post(20);

            for (int index = 0; index < 7; index++) {
                produceEcg(250 * 0.064f, aLong, ecgBuffer[index], bufferRepository.getEcgBuffer(index)::produce);
            }
            produceEcg(250 * 0.064f, aLong, ecgRespBuffer, bufferRepository.getEcgRrBuffer()::produce);

            ecgModel.leadOff.post(false);
            ecgModel.v1LeadOff.post(false);
        }

        /*SPO2*/
        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            SensorModel spo2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Spo2);
            spo2Model.textNumber.post(1000);
            SensorModel prModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pr);
            prModel.textNumber.post(100);
            SensorModel piModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pi);
            piModel.textNumber.post(20000);
            SensorModel sphbModel = sensorModelRepository.getSensorModel(SensorModelEnum.Sphb);
            sphbModel.textNumber.post(2350);
            SensorModel spocModel = sensorModelRepository.getSensorModel(SensorModelEnum.Spoc);
            spocModel.textNumber.post(255);
            SensorModel spmetModel = sensorModelRepository.getSensorModel(SensorModelEnum.Spmet);
            spmetModel.textNumber.post(1000);
            SensorModel spcoModel = sensorModelRepository.getSensorModel(SensorModelEnum.Spco);
            spcoModel.textNumber.post(1000);
            SensorModel pviModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pvi);
            pviModel.textNumber.post(100);
            produce(250 * 0.064f, aLong, spo2Buffer, bufferRepository.getSpo2Buffer()::produce);
        }

        /*CO2*/
        if (moduleHardware.isActive(ModuleEnum.Co2)) {
            SensorModel co2etModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2);
            co2etModel.textNumber.post(100);
            SensorModel co2RrModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Rr);
            co2RrModel.textNumber.post(100);
            SensorModel co2FiModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Fi);
            co2FiModel.textNumber.post(100);
            produceEcg(100 * 0.064f, aLong, co2Buffer, bufferRepository.getCo2Buffer()::produce);
        }

        if (aLong % 15 == 0) {
            ecgModel.ecgBeep.notifyChange();
            spo2Model.spo2Beep.notifyChange();
        }
    }

    private void produceEcg(float dataPerTurn, long aLong, int[] buffer, Consumer3<Integer, Integer, int[]> bufferControl) {
        int startId = (int) (aLong * dataPerTurn);
        int length = (int) ((aLong + 1) * dataPerTurn) - (int) (aLong * dataPerTurn);
        startId %= buffer.length / 2;
        bufferControl.accept(startId, length, buffer);
    }

    private void produce(float dataPerTurn, long aLong, int[] buffer, androidx.core.util.Consumer<Integer> bufferControl) {
        int startId = (int) (aLong * dataPerTurn);
        startId %= buffer.length / 2;
        bufferControl.accept(buffer[startId]);
    }
}