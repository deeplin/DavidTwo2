package com.david.core.database.option;

import com.david.core.database.LastUser;
import com.david.core.database.dao.NibpDao;
import com.david.core.database.entity.NibpEntity;
import com.david.core.util.Constant;

public class NibpDaoControl extends BaseDaoOperation<NibpEntity> {

    private final NibpDao nibpDao;

    public NibpDaoControl(NibpDao nibpDao, LastUser lastUser) {
        super(Constant.WEIGHT_IN_DATABASE, lastUser);
        this.nibpDao = nibpDao;
        super.init();
    }

    @Override
    protected NibpEntity getMaxTimeStampRecord() {
        return nibpDao.getMaxTimeStampRecord();
    }

    @Override
    protected void insertOrUpdate(NibpEntity nibpEntity) {
        nibpDao.insertOrUpdate(nibpEntity);
    }

    @Override
    protected NibpEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit) {
        return nibpDao.query(startTimeStamp, endTimeStamp, offset, limit);
    }
}