package com.david.core.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.david.core.database.entity.IncubatorEntity;

@Dao
public abstract class IncubatorDao extends BaseDao<IncubatorEntity> {

    @Query("SELECT * FROM IncubatorEntity WHERE timeStamp = (SELECT MAX(timeStamp) FROM IncubatorEntity) LIMIT 1")
    public abstract IncubatorEntity getMaxTimeStampRecord();

    @Query("SELECT * FROM IncubatorEntity WHERE timeStamp BETWEEN :startTimeStamp AND :endTimeStamp ORDER BY timeStamp DESC LIMIT :limit OFFSET :offset")
    public abstract IncubatorEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit);

    @Query("DELETE FROM IncubatorEntity")
    public abstract void deleteAll();
}