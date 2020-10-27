package com.david.core.database.entity;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(indices = {@Index("id"), @Index("timeStamp")})
public class UserEntity extends BaseEntity {
    public long endTimeStamp;
    public String name;
    public String userId;
    public boolean sex;
    public int bloodType;
    public int gestation;
    public int weight;
    public int birthdayYear;
    public int birthdayMonth;
    public int birthdayDay;
    public int height;
    public int age;
    public boolean paceMaker;

    public UserEntity() {
    }
}