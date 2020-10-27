package com.david.core.database.entity;

import androidx.room.Entity;
import androidx.room.Index;

import javax.inject.Inject;

@Entity(indices = {@Index("id"), @Index("timeStamp")})
public class Co2Entity extends BaseEntity {

    public int co2;
    public int fi;
    public int rr;

    @Inject
    public Co2Entity() {
    }
}