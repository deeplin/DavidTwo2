package com.david.core.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.david.core.database.entity.AlarmEntity;

@Dao
public abstract class AlarmDao extends BaseDao<AlarmEntity> {

    @Query("SELECT * FROM AlarmEntity WHERE timeStamp = (SELECT MAX(timeStamp) FROM AlarmEntity) LIMIT 1")
    public abstract AlarmEntity getMaxTimeStampRecord();

    @Query("SELECT * FROM AlarmEntity WHERE timeStamp BETWEEN :startTimeStamp AND :endTimeStamp ORDER BY timeStamp DESC LIMIT :limit OFFSET :offset")
    public abstract AlarmEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit);

    @Query("DELETE FROM AlarmEntity")
    public abstract void deleteAll();
}