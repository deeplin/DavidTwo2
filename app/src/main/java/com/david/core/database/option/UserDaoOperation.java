package com.david.core.database.option;

import com.david.core.database.LastUser;
import com.david.core.database.dao.UserDao;
import com.david.core.database.entity.UserEntity;
import com.david.core.util.Constant;

public class UserDaoOperation extends BaseDaoOperation<UserEntity> {

    private final UserDao userDao;

    public UserDaoOperation(UserDao userDao, LastUser lastUser) {
        super(Constant.USER_PAGE_COUNT * Constant.USER_PER_PAGE, lastUser);
        this.userDao = userDao;
        super.init();
    }

    @Override
    protected UserEntity getMaxTimeStampRecord() {
        return userDao.getMaxTimeStampRecord();
    }

    @Override
    public void insertOrUpdate(UserEntity userEntity) {
        userDao.insertOrUpdate(userEntity);
    }

    @Override
    public UserEntity[] query(long startTimeStamp, long endTimeStamp, int offset, int limit) {
        return userDao.query(startTimeStamp, endTimeStamp, offset, limit);
    }

    public void delete(UserEntity userEntity) {
        userDao.delete(userEntity);
    }
}