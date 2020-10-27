package com.david.core.database.option;

import com.david.core.database.LastUser;
import com.david.core.database.dao.Spo2Dao;
import com.david.core.database.entity.Spo2Entity;
import com.david.core.util.Constant;

public class Spo2DaoOperation extends BaseDaoOperation<Spo2Entity> {

    private final Spo2Dao spo2Dao;

    public Spo2DaoOperation(Spo2Dao spo2Dao, LastUser lastUser) {
        super(Constant.MONITOR_IN_DATABASE, lastUser);
        this.spo2Dao = spo2Dao;
        super.init();
    }

    @Override
    protected Spo2Entity getMaxTimeStampRecord() {
        return spo2Dao.getMaxTimeStampRecord();
    }

    @Override
    protected void insertOrUpdate(Spo2Entity spo2Entity) {
        spo2Dao.insertOrUpdate(spo2Entity);
    }

    @Override
    protected Spo2Entity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit) {
        return spo2Dao.query(startTimeStamp, endTimeStamp, offset, limit);
    }
}