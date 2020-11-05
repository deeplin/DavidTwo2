package com.david.core.control;

import com.david.core.database.DaoControl;
import com.david.core.database.entity.Co2Entity;
import com.david.core.database.entity.EcgEntity;
import com.david.core.database.entity.IncubatorEntity;
import com.david.core.database.entity.Spo2Entity;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.util.BitUtil;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.IntervalUtil;
import com.david.core.util.LazyLiveData;
import com.david.core.util.MyLifecycleOwner;
import com.david.core.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class ConfigRepository implements ILifeCycle, Consumer<Long> {

    private final int LIMIT_START = 100;

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IntervalUtil intervalUtil;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    DaoControl daoControl;

    private MyLifecycleOwner myLifecycleOwner;

    private List<LazyLiveData<Integer>> configList;
    private List<LazyLiveData<Integer>> limitList;

    private IncubatorEntity incubatorEntity;
    private Spo2Entity spo2Entity;
    private EcgEntity ecgEntity;
    private Co2Entity co2Entity;

    private SensorModel skin1Model;
    private SensorModel skin2Model;
    private SensorModel airModel;
    private SensorModel humidityModel;
    private SensorModel oxygenModel;
    private SensorModel incModel;
    private SensorModel warmerModel;

    private SensorModel spo2Model;
    private SensorModel prModel;
    private SensorModel piModel;
    private SensorModel sphbModel;
    private SensorModel spocModel;
    private SensorModel spmetModel;
    private SensorModel spcoModel;
    private SensorModel pviModel;

    private SensorModel ecgHrModel;
    private SensorModel ecgRrModel;
    private SensorModel co2Model;
    private SensorModel co2FiModel;
    private SensorModel co2RrModel;

    @Inject
    public ConfigRepository() {
        ContextUtil.getComponent().inject(this);
        myLifecycleOwner = new MyLifecycleOwner();

        configList = new ArrayList<>();
        ConfigEnum[] configEnums = ConfigEnum.values();
        for (int index = 0; index < configEnums.length; index++) {
            addConfig(configEnums[index]);
        }

        limitList = new ArrayList<>();
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Skin1));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Skin2));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Air));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Humidity));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Wake));

        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Spo2));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Pr));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Pi));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Sphb));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Spoc));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Spmet));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Spco));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Pvi));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.EcgHr));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Co2));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Co2Rr));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Co2Fi));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.Nibp));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.NibpDia));
        addLimit(sensorModelRepository.getSensorModel(SensorModelEnum.NibpMean));

        for (int index = 0; index < limitList.size(); index++) {
            LazyLiveData<Integer> lazyLiveData = limitList.get(index);
            final int id = index + LIMIT_START;
            lazyLiveData.observe(myLifecycleOwner, integer ->
                    Observable.just(integer).observeOn(Schedulers.io())
                            .subscribe(integer1 -> daoControl.updateConfig(id, integer1)));
        }

        skin1Model = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
        skin2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Skin2);
        airModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
        humidityModel = sensorModelRepository.getSensorModel(SensorModelEnum.Humidity);
        oxygenModel = sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen);
        incModel = sensorModelRepository.getSensorModel(SensorModelEnum.Inc);
        warmerModel = sensorModelRepository.getSensorModel(SensorModelEnum.Warmer);

        spo2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Spo2);
        prModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pr);
        piModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pi);
        sphbModel = sensorModelRepository.getSensorModel(SensorModelEnum.Sphb);
        spocModel = sensorModelRepository.getSensorModel(SensorModelEnum.Spoc);
        spmetModel = sensorModelRepository.getSensorModel(SensorModelEnum.Spmet);
        spcoModel = sensorModelRepository.getSensorModel(SensorModelEnum.Spco);
        pviModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pvi);

        ecgHrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgHr);
        ecgRrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr);
        co2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Co2);
        co2FiModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Fi);
        co2RrModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Rr);

        incubatorEntity = new IncubatorEntity();
        spo2Entity = new Spo2Entity();
        ecgEntity = new EcgEntity();
        co2Entity = new Co2Entity();
    }

    private void addLimit(SensorModel sensorModel) {
        limitList.add(sensorModel.upperLimit);
        limitList.add(sensorModel.lowerLimit);
    }

    private void addConfig(ConfigEnum configEnum) {
        LazyLiveData<Integer> lazyLiveData = new LazyLiveData<>();
        lazyLiveData.set(configEnum.getValue());
        configList.add(lazyLiveData);
        lazyLiveData.observe(myLifecycleOwner, integer ->
                Observable.just(integer).observeOn(Schedulers.io())
                        .subscribe(integer1 -> daoControl.updateConfig(configEnum.ordinal(), integer1)));
    }

    @Override
    public void attach() {
        for (int index = 0; index < configList.size(); index++) {
            LazyLiveData<Integer> lazyLiveData = configList.get(index);
            lazyLiveData.post(daoControl.initConfig(index, lazyLiveData.getValue()));
        }

        for (int index = 0; index < limitList.size(); index++) {
            LazyLiveData<Integer> lazyLiveData = limitList.get(index);
            lazyLiveData.post(daoControl.initConfig(index + LIMIT_START, lazyLiveData.getValue()));
        }

        myLifecycleOwner.start();

        intervalUtil.addSecondConsumer(ConfigRepository.class, this);
    }

    @Override
    public void detach() {
        intervalUtil.removeSecondConsumer(ConfigRepository.class);
        myLifecycleOwner.stop();
    }

    @Override
    public void accept(Long aLong) {
        long currentTime = TimeUtil.getCurrentTimeInSecond();

        if (currentTime % 5 == 0) {
            if (moduleHardware.isActive(ModuleEnum.Spo2)) {
                spo2Entity.spo2 = spo2Model.textNumber.getValue();
                spo2Entity.pi = piModel.textNumber.getValue();
                spo2Entity.pr = prModel.textNumber.getValue();
                spo2Entity.sphb = sphbModel.textNumber.getValue();
                spo2Entity.spoc = spocModel.textNumber.getValue();
                spo2Entity.spmet = spmetModel.textNumber.getValue();
                spo2Entity.spco = spcoModel.textNumber.getValue();
                spo2Entity.pvi = pviModel.textNumber.getValue();
                spo2Entity.timeStamp = currentTime;
                daoControl.getSpo2DaoOperation().insert(spo2Entity);
            }

            if (moduleHardware.isActive(ModuleEnum.Ecg)) {
                ecgEntity.hr = ecgHrModel.textNumber.getValue();
                ecgEntity.rr = ecgRrModel.textNumber.getValue();
                ecgEntity.timeStamp = currentTime;
                daoControl.getEcgDaoControl().insert(ecgEntity);
            }

            if (moduleHardware.isActive(ModuleEnum.Co2)) {
                co2Entity.co2 = co2Model.textNumber.getValue();
                co2Entity.fi = co2FiModel.textNumber.getValue();
                co2Entity.rr = co2RrModel.textNumber.getValue();
                co2Entity.timeStamp = currentTime;
                daoControl.getCo2DaoControl().insert(co2Entity);
            }
        }

        if (currentTime % 60 == 0) {
            incubatorEntity.skin1 = skin1Model.textNumber.getValue();
            incubatorEntity.skin2 = skin2Model.textNumber.getValue();
            incubatorEntity.air = airModel.textNumber.getValue();

            incubatorEntity.humidity = humidityModel.textNumber.getValue();
            incubatorEntity.oxygen = oxygenModel.textNumber.getValue();
            incubatorEntity.inc = incModel.textNumber.getValue();
            incubatorEntity.warmer = warmerModel.textNumber.getValue();
            incubatorEntity.timeStamp = currentTime;
            daoControl.getIncubatorDaoOperation().insert(incubatorEntity);
        }
    }

    public LazyLiveData<Integer> getConfig(ConfigEnum configEnum) {
        return configList.get(configEnum.ordinal());
    }

    public List<Integer> getActiveSpo2Module() {
        List<Integer> activeSpo2Module = new ArrayList<>();
        int spo2ModuleConfig = getConfig(ConfigEnum.Spo2ModuleConfig).getValue();
        for (int index = 0; index < 5; index++) {
            if (BitUtil.getBit(spo2ModuleConfig, index)) {
                activeSpo2Module.add(index);
            }
        }
        return activeSpo2Module;
    }

    public List<SensorModelEnum> getActiveSpo2Enum() {
        List<SensorModelEnum> activeSpo2Module = new ArrayList<>();
        activeSpo2Module.add(SensorModelEnum.Spo2);
        activeSpo2Module.add(SensorModelEnum.Pr);
        activeSpo2Module.add(SensorModelEnum.Pi);
        int spo2ModuleConfig = getConfig(ConfigEnum.Spo2ModuleConfig).getValue();
        for (int index = 0; index < 5; index++) {
            if (BitUtil.getBit(spo2ModuleConfig, index)) {
                activeSpo2Module.add(SensorModelEnum.values()[SensorModelEnum.Sphb.ordinal() + index]);
            }
        }
        return activeSpo2Module;
    }
}