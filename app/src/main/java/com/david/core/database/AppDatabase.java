package com.david.core.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.david.core.database.dao.AlarmDao;
import com.david.core.database.dao.Co2Dao;
import com.david.core.database.dao.ConfigDao;
import com.david.core.database.dao.EcgDao;
import com.david.core.database.dao.IncubatorDao;
import com.david.core.database.dao.NibpDao;
import com.david.core.database.dao.Spo2Dao;
import com.david.core.database.dao.UserDao;
import com.david.core.database.dao.WeightDao;
import com.david.core.database.entity.AlarmEntity;
import com.david.core.database.entity.Co2Entity;
import com.david.core.database.entity.ConfigEntity;
import com.david.core.database.entity.EcgEntity;
import com.david.core.database.entity.IncubatorEntity;
import com.david.core.database.entity.NibpEntity;
import com.david.core.database.entity.Spo2Entity;
import com.david.core.database.entity.UserEntity;
import com.david.core.database.entity.WeightEntity;

@Database(entities = {IncubatorEntity.class, Spo2Entity.class, ConfigEntity.class, UserEntity.class,
        EcgEntity.class, NibpEntity.class, Co2Entity.class, WeightEntity.class, AlarmEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ConfigDao systemConfigDao();

    public abstract UserDao userDao();

    public abstract IncubatorDao incubatorDao();

    public abstract Spo2Dao spo2Dao();

    public abstract EcgDao ecgDao();

    public abstract Co2Dao co2Dao();

    public abstract NibpDao nibpDao();

    public abstract WeightDao weightDao();

    public abstract AlarmDao alarmDao();
}