package com.david.core.database.entity;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(indices = {@Index("id"), @Index("timeStamp")})
public class IncubatorEntity extends BaseEntity {
    public int air;
    public int skin1;
    public int skin2;
    public int humidity;
    public int oxygen;
    public int inc;
    public int warmer;

    public IncubatorEntity(){
    }
}