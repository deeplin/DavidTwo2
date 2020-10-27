package com.david.core.database.option;

import com.david.core.database.dao.AlarmDao;
import com.david.core.database.entity.AlarmEntity;
import com.david.core.util.Constant;

public class AlarmDaoOperation {

    private final AlarmDao alarmDao;
    private final int numInDatabase;
    private int currentId;

    public AlarmDaoOperation(AlarmDao alarmDao) {
        this.numInDatabase = Constant.INCUBATOR_IN_DATABASE;
        this.alarmDao = alarmDao;

        AlarmEntity alarmEntity = alarmDao.getMaxTimeStampRecord();
        if (alarmEntity != null) {
            currentId = (alarmEntity.id + 1) % numInDatabase;
        } else {
            currentId = 1;
        }
    }

    public void insert(AlarmEntity alarmEntity) {
        alarmEntity.id = currentId;
        currentId = (currentId + 1) % numInDatabase;
        alarmDao.insertOrUpdate(alarmEntity);
    }

    public AlarmEntity[] get(long startTimeStamp, long endTimeStamp) {
        return alarmDao.query(startTimeStamp, endTimeStamp, 0, Integer.MAX_VALUE);
    }

    public AlarmEntity[] get(long startTimeStamp, long endTimeStamp, int offset, int limit) {
        return alarmDao.query(startTimeStamp, endTimeStamp, offset, limit);
    }
}