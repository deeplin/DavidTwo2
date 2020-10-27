package com.david.core.database.option;

import com.david.core.database.LastUser;
import com.david.core.database.entity.BaseEntity;
import com.david.core.database.entity.UserEntity;

public abstract class BaseDaoOperation<U extends BaseEntity> {

    private final LastUser lastUser;
    private final int numInDatabase;
    private int currentId;

    public BaseDaoOperation(int numInDatabase, LastUser lastUser) {
        this.numInDatabase = numInDatabase;
        this.lastUser = lastUser;
    }

    protected void init() {
        U u = getMaxTimeStampRecord();
        if (u != null) {
            currentId = (u.id + 1) % numInDatabase;
        } else {
            currentId = 1;
        }
    }

    public void insert(U u) {
        u.id = currentId;
        currentId = (currentId + 1) % numInDatabase;
        insertOrUpdate(u);
    }

    public U[] get(long startTimeStamp, long endTimeStamp) {
        return get(startTimeStamp, endTimeStamp, 0, Integer.MAX_VALUE);
    }

    public U[] get(long startTimeStamp, long endTimeStamp, int offset, int limit) {
        UserEntity lastUerEntity = lastUser.getUserEntity();
        if (lastUerEntity != null) {
            if (lastUerEntity.timeStamp > 0 && lastUerEntity.timeStamp > startTimeStamp) {
                startTimeStamp = lastUerEntity.timeStamp;
            }
            if (lastUerEntity.endTimeStamp > 0 && lastUerEntity.endTimeStamp < endTimeStamp) {
                endTimeStamp = lastUerEntity.endTimeStamp;
            }
        }
        return query(startTimeStamp, endTimeStamp, offset, limit);
    }

    protected abstract U getMaxTimeStampRecord();

    protected abstract void insertOrUpdate(U u);

    protected abstract U[] query(long startTimeStamp, long endTimeStamp, int offset, int limit);
}