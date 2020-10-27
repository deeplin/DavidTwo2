package com.david.core.database.option;

import com.david.core.database.LastUser;
import com.david.core.database.dao.Co2Dao;
import com.david.core.database.entity.Co2Entity;
import com.david.core.util.Constant;

public class Co2DaoControl extends BaseDaoOperation<Co2Entity> {

    private final Co2Dao co2Dao;

    public Co2DaoControl(Co2Dao co2Dao, LastUser lastUser) {
        super(Constant.MONITOR_IN_DATABASE, lastUser);
        this.co2Dao = co2Dao;
        super.init();
    }

    @Override
    protected Co2Entity getMaxTimeStampRecord() {
        return co2Dao.getMaxTimeStampRecord();
    }

    @Override
    protected void insertOrUpdate(Co2Entity co2Entity) {
        co2Dao.insertOrUpdate(co2Entity);
    }

    @Override
    protected Co2Entity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit) {
        return co2Dao.query(startTimeStamp, endTimeStamp, offset, limit);
    }
}