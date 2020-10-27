package com.david.core.database.dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import androidx.room.Update;

public abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(T t);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract int update(T t);

    @Transaction
    public void insertOrUpdate(T t) {
        if (update(t) == 0) {
            insert(t);
        }
    }
}