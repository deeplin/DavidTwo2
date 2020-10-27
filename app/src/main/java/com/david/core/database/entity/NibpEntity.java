package com.david.core.database.entity;

import androidx.room.Entity;
import androidx.room.Index;

import javax.inject.Inject;

@Entity(indices = {@Index("id"), @Index("timeStamp")})
public class NibpEntity extends BaseEntity {
    public int sys;
    public int dia;
    public int map;

    @Inject
    public NibpEntity() {
    }
}