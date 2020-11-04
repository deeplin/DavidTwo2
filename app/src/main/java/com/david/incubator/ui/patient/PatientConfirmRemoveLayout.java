package com.david.incubator.ui.patient;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.database.DaoControl;
import com.david.core.database.LastUser;
import com.david.core.database.entity.UserEntity;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.util.ContextUtil;
import com.david.incubator.ui.menu.sensorcalibration.ConfirmLayout;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PatientConfirmRemoveLayout extends ConfirmLayout {

    @Inject
    SystemModel systemModel;
    @Inject
    LastUser lastUser;
    @Inject
    DaoControl daoControl;

    public PatientConfirmRemoveLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.MENU_CONFIRM_SENSOR_CALIBRATION);

        binding.cancel.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.PATIENT_LIST_INFO));

        binding.confirm.setOnClickListener(v -> {
            UserEntity userEntity = lastUser.getEditUserEntity();
            Observable.just(userEntity).observeOn(Schedulers.io())
                    .subscribe(userEntity1 -> {
                        daoControl.getUserDaoOperation().delete(userEntity1);
                        daoControl.refreshLastUser();
                        lastUser.updated.notifyChange();
                        systemModel.showLayout(LayoutPageEnum.PATIENT_LIST);
                    });
        });
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.confirmText.setText(ContextUtil.getString(R.string.confirm_remove_patient));
    }

    @Override
    public void detach() {
        super.detach();
    }
}
