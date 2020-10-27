package com.david.core.database;

import com.david.core.database.entity.UserEntity;
import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LastUser {

    public final LazyLiveData<Boolean> updated = new LazyLiveData<>(false);

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getEditUserEntity() {
        return editUserEntity;
    }

    public void setEditUserEntity(UserEntity editUserEntity) {
        this.editUserEntity = editUserEntity;
    }

    private UserEntity userEntity;

    private UserEntity editUserEntity;

    public int userOffset;

    @Inject
    public LastUser() {
    }
}
