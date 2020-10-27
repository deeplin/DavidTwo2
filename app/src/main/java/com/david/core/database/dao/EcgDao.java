package com.david.core.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.david.core.database.entity.EcgEntity;

@Dao
public abstract class EcgDao extends BaseDao<EcgEntity> {

    @Query("SELECT * FROM EcgEntity WHERE timeStamp = (SELECT MAX(timeStamp) FROM EcgEntity) LIMIT 1")
    public abstract EcgEntity getMaxTimeStampRecord();

    @Query("SELECT * FROM EcgEntity WHERE timeStamp BETWEEN :startTimeStamp AND :endTimeStamp ORDER BY timeStamp DESC LIMIT :limit OFFSET :offset")
    public abstract EcgEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit);

    @Query("DELETE FROM EcgEntity")
    public abstract void deleteAll();
}