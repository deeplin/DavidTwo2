package com.david.core.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.david.core.database.entity.WeightEntity;

@Dao
public abstract class WeightDao extends BaseDao<WeightEntity> {

    @Query("SELECT * FROM WeightEntity WHERE timeStamp = (SELECT MAX(timeStamp) FROM WeightEntity) LIMIT 1")
    public abstract WeightEntity getMaxTimeStampRecord();

    @Query("SELECT * FROM WeightEntity WHERE timeStamp BETWEEN :startTimeStamp AND :endTimeStamp ORDER BY timeStamp DESC LIMIT :limit OFFSET :offset")
    public abstract WeightEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit);

    @Query("DELETE FROM WeightEntity")
    public abstract void deleteAll();
}
