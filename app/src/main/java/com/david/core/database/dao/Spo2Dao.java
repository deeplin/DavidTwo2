package com.david.core.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.david.core.database.entity.Spo2Entity;

@Dao
public abstract class Spo2Dao extends BaseDao<Spo2Entity> {

    @Query("SELECT * FROM Spo2Entity WHERE timeStamp = (SELECT MAX(timeStamp) FROM Spo2Entity) LIMIT 1")
    public abstract Spo2Entity getMaxTimeStampRecord();

    @Query("SELECT * FROM Spo2Entity WHERE timeStamp BETWEEN :startTimeStamp AND :endTimeStamp ORDER BY timeStamp DESC LIMIT :limit OFFSET :offset")
    public abstract Spo2Entity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit);

    @Query("DELETE FROM Spo2Entity")
    public abstract void deleteAll();
}