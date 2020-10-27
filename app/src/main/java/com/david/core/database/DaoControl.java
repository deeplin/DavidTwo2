package com.david.core.database;

import android.content.Context;

import androidx.room.Room;

import com.david.core.database.dao.ConfigDao;
import com.david.core.database.entity.ConfigEntity;
import com.david.core.database.option.AlarmDaoOperation;
import com.david.core.database.option.Co2DaoControl;
import com.david.core.database.option.EcgDaoControl;
import com.david.core.database.option.IncubatorDaoOperation;
import com.david.core.database.option.NibpDaoControl;
import com.david.core.database.option.Spo2DaoOperation;
import com.david.core.database.option.UserDaoOperation;
import com.david.core.database.option.WeightDaoControl;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class DaoControl {

    @Inject
    LastUser lastUser;

    private AppDatabase database;

    public UserDaoOperation getUserDaoOperation() {
        return userDaoOperation;
    }

    public IncubatorDaoOperation getIncubatorDaoOperation() {
        return incubatorDaoOperation;
    }

    public Spo2DaoOperation getSpo2DaoOperation() {
        return spo2DaoOperation;
    }

    public EcgDaoControl getEcgDaoControl() {
        return ecgDaoControl;
    }

    public Co2DaoControl getCo2DaoControl() {
        return co2DaoControl;
    }

    public WeightDaoControl getWeightDaoControl() {
        return weightDaoControl;
    }

    public NibpDaoControl getNibpDaoControl() {
        return nibpDaoControl;
    }

    public AlarmDaoOperation getAlarmDaoOperation() {
        return alarmDaoOperation;
    }

    private ConfigDao configDao;
    private UserDaoOperation userDaoOperation;
    private IncubatorDaoOperation incubatorDaoOperation;
    private Spo2DaoOperation spo2DaoOperation;
    private EcgDaoControl ecgDaoControl;
    private Co2DaoControl co2DaoControl;
    private WeightDaoControl weightDaoControl;
    private NibpDaoControl nibpDaoControl;
    private AlarmDaoOperation alarmDaoOperation;

    @Inject
    public DaoControl() {
    }

    public void start(Context applicationContext) {
        database = Room.databaseBuilder(applicationContext,
                AppDatabase.class, "DavidData.db").build();

        configDao = database.systemConfigDao();
        userDaoOperation = new UserDaoOperation(database.userDao(), lastUser);
        incubatorDaoOperation = new IncubatorDaoOperation(database.incubatorDao(), lastUser);
        spo2DaoOperation = new Spo2DaoOperation(database.spo2Dao(), lastUser);
        ecgDaoControl = new EcgDaoControl(database.ecgDao(), lastUser);
        co2DaoControl = new Co2DaoControl(database.co2Dao(), lastUser);
        weightDaoControl = new WeightDaoControl(database.weightDao(), lastUser);
        nibpDaoControl = new NibpDaoControl(database.nibpDao(), lastUser);
        alarmDaoOperation = new AlarmDaoOperation(database.alarmDao());

        refreshLastUser();
    }

    public void stop() {
        if (database != null) {
            database.close();
            database = null;
        }
    }

    public void refreshLastUser() {
        lastUser.setUserEntity(database.userDao().getMaxTimeStampRecord());
    }

    public int initConfig(int configId, int defaultValue) {
        ConfigEntity configEntity = configDao.getValue(configId);

        if (configEntity != null) {
            return configEntity.value;
        }
        configEntity = new ConfigEntity();
        configEntity.id = configId;
        configEntity.value = defaultValue;
        configDao.insert(configEntity);
        return defaultValue;
    }

    public synchronized void updateConfig(int configId, int value) {
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.id = configId;
        configEntity.value = value;
        configDao.update(configEntity);
    }

    public void deleteTables() {
        Observable.just(this).observeOn(Schedulers.io()).subscribe(daoOperation -> {
            database.userDao().deleteAll();
            database.incubatorDao().deleteAll();
            database.spo2Dao().deleteAll();
            database.alarmDao().deleteAll();
        });
    }

    public void deleteConfigTable() {
        Observable.just(this).observeOn(Schedulers.io()).subscribe(daoOperation -> configDao.deleteAll());
    }
}