package com.david.core.database.entity;

import androidx.room.Entity;
import androidx.room.Index;

import javax.inject.Inject;

@Entity(indices = {@Index("id"), @Index("timeStamp")})
public class EcgEntity extends BaseEntity {
    public int hr;
    public int rr;

    @Inject
    public EcgEntity() {
    }
}
