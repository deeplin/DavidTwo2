package com.david.core.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.david.core.database.entity.Co2Entity;

@Dao
public abstract class Co2Dao extends BaseDao<Co2Entity> {

    @Query("SELECT * FROM Co2Entity WHERE timeStamp = (SELECT MAX(timeStamp) FROM Co2Entity) LIMIT 1")
    public abstract Co2Entity getMaxTimeStampRecord();

    @Query("SELECT * FROM Co2Entity WHERE timeStamp BETWEEN :startTimeStamp AND :endTimeStamp ORDER BY timeStamp DESC LIMIT :limit OFFSET :offset")
    public abstract Co2Entity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit);

    @Query("DELETE FROM Co2Entity")
    public abstract void deleteAll();
}