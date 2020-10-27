package com.david.core.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.david.core.database.entity.NibpEntity;

@Dao
public abstract class NibpDao extends BaseDao<NibpEntity> {

    @Query("SELECT * FROM NibpEntity WHERE timeStamp = (SELECT MAX(timeStamp) FROM NibpEntity) LIMIT 1")
    public abstract NibpEntity getMaxTimeStampRecord();

    @Query("SELECT * FROM NibpEntity WHERE timeStamp BETWEEN :startTimeStamp AND :endTimeStamp ORDER BY timeStamp DESC LIMIT :limit OFFSET :offset")
    public abstract NibpEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit);

    @Query("DELETE FROM NibpEntity")
    public abstract void deleteAll();
}