package com.david.core.database.option;

import com.david.core.database.LastUser;
import com.david.core.database.dao.EcgDao;
import com.david.core.database.entity.EcgEntity;
import com.david.core.util.Constant;

public class EcgDaoControl extends BaseDaoOperation<EcgEntity> {

    private final EcgDao ecgDao;

    public EcgDaoControl(EcgDao ecgDao, LastUser lastUser) {
        super(Constant.MONITOR_IN_DATABASE, lastUser);
        this.ecgDao = ecgDao;
        super.init();
    }

    @Override
    protected EcgEntity getMaxTimeStampRecord() {
        return ecgDao.getMaxTimeStampRecord();
    }

    @Override
    protected void insertOrUpdate(EcgEntity ecgEntity) {
        ecgDao.insertOrUpdate(ecgEntity);
    }

    @Override
    protected EcgEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit) {
        return ecgDao.query(startTimeStamp, endTimeStamp, offset, limit);
    }
}