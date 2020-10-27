package com.david.core.database.entity;

import androidx.room.PrimaryKey;

public class BaseEntity {
    @PrimaryKey
    public int id;
    public long timeStamp;
}