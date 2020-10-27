package com.david.core.database.option;

import com.david.core.database.LastUser;
import com.david.core.database.dao.IncubatorDao;
import com.david.core.database.entity.IncubatorEntity;
import com.david.core.util.Constant;

public class IncubatorDaoOperation extends BaseDaoOperation<IncubatorEntity> {

    private final IncubatorDao incubatorDao;

    public IncubatorDaoOperation(IncubatorDao incubatorDao, LastUser lastUser) {
        super(Constant.INCUBATOR_IN_DATABASE, lastUser);
        this.incubatorDao = incubatorDao;
        super.init();
    }

    @Override
    protected IncubatorEntity getMaxTimeStampRecord() {
        return incubatorDao.getMaxTimeStampRecord();
    }

    @Override
    protected void insertOrUpdate(IncubatorEntity incubatorEntity) {
        incubatorDao.insertOrUpdate(incubatorEntity);
    }

    @Override
    protected IncubatorEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit) {
        return incubatorDao.query(startTimeStamp, endTimeStamp, offset, limit);
    }
}
