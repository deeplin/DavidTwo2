package com.david.core.database.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("id")})
public class ConfigEntity {
    @PrimaryKey
    public int id;
    public int value;
}