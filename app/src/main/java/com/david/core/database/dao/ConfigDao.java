package com.david.core.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.david.core.database.entity.ConfigEntity;

@Dao
public abstract class ConfigDao extends BaseDao<ConfigEntity> {

    @Query("SELECT * FROM ConfigEntity WHERE id = :id")
    public abstract ConfigEntity getValue(int id);

    @Query("DELETE FROM ConfigEntity")
    public abstract void deleteAll();
}