package com.david.core.database.option;

import com.david.core.database.LastUser;
import com.david.core.database.dao.WeightDao;
import com.david.core.database.entity.WeightEntity;
import com.david.core.util.Constant;

public class WeightDaoControl extends BaseDaoOperation<WeightEntity> {

    private final WeightDao weightDao;

    public WeightDaoControl(WeightDao weightDao, LastUser lastUser) {
        super(Constant.WEIGHT_IN_DATABASE, lastUser);
        this.weightDao = weightDao;
        super.init();
    }

    @Override
    protected WeightEntity getMaxTimeStampRecord() {
        return weightDao.getMaxTimeStampRecord();
    }

    @Override
    protected void insertOrUpdate(WeightEntity weightEntity) {
        weightDao.insertOrUpdate(weightEntity);
    }

    @Override
    protected WeightEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit) {
        return weightDao.query(startTimeStamp, endTimeStamp, offset, limit);
    }
}