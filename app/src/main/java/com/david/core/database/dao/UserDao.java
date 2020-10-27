package com.david.core.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;

import com.david.core.database.entity.UserEntity;

@Dao
public abstract class UserDao extends BaseDao<UserEntity> {

    @Query("SELECT * FROM UserEntity WHERE timeStamp = (SELECT MAX(timeStamp) FROM UserEntity) LIMIT 1")
    public abstract UserEntity getMaxTimeStampRecord();

    @Query("SELECT * FROM UserEntity WHERE timeStamp BETWEEN :startTimeStamp AND :endTimeStamp ORDER BY timeStamp DESC LIMIT :limit OFFSET :offset")
    public abstract UserEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit);

    @Query("DELETE FROM UserEntity")
    public abstract void deleteAll();

    @Delete
    public abstract void delete(UserEntity userEntity);
}