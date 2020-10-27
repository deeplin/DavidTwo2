package com.david.core.database.entity;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(indices = {@Index("id"), @Index("timeStamp")})
public class Spo2Entity extends BaseEntity {
    public int spo2;
    public int pi;
    public int pr;
    public int pvi;
    public int spco;
    public int sphb;
    public int spmet;
    public int spoc;

    public Spo2Entity(){
    }
}