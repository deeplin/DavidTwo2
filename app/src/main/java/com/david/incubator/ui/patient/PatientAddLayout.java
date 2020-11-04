package com.david.incubator.ui.patient;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.database.DaoControl;
import com.david.core.database.entity.UserEntity;
import com.david.core.enumeration.BloodTypeEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.component.TitleView;
import com.david.core.util.ContextUtil;
import com.david.core.util.TimeUtil;
import com.david.incubator.ui.keyboard.InputModel;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PatientAddLayout extends BasePatientLayout {

    @Inject
    SystemModel systemModel;
    @Inject
    InputModel inputModel;
    @Inject
    DaoControl daoControl;

    private int bloodTypeOptionId;

    public PatientAddLayout(Context context) {
        super(context, LayoutPageEnum.PATIENT_ADD);
        ContextUtil.getComponent().inject(this);

        super.initPatient();

        setKeyButtonEnum(0, KeyButtonEnum.PATIENT_ADD_GESTATION, null, null);
        setKeyButtonEnum(1, KeyButtonEnum.PATIENT_ADD_WEIGHT, null, null);
        setKeyButtonEnum(2, KeyButtonEnum.PATIENT_ADD_HEIGHT, null, null);
        setKeyButtonEnum(3, KeyButtonEnum.PATIENT_ADD_AGE, null, null);

        binding.idView.getValue().setOnClickListener(v -> {
            closePopup();
            systemModel.tagId++;
            inputModel.title.set(ContextUtil.getString(R.string.id));
            inputModel.inputText.set(binding.idView.getValue().getText().toString());
            systemModel.showLayout(LayoutPageEnum.KEYBOARD_ALPHABET);
        });

        binding.nameView.getValue().setOnClickListener(v -> {
            closePopup();
            systemModel.tagId += 2;
            inputModel.title.set(ContextUtil.getString(R.string.name));
            inputModel.inputText.set(binding.nameView.getValue().getText().toString());
            systemModel.showLayout(LayoutPageEnum.KEYBOARD_ALPHABET);
        });

        binding.bloodTypeView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (BloodTypeEnum bloodTypeEnum : BloodTypeEnum.values()) {
                optionPopupView.setOption(bloodTypeEnum.ordinal(), bloodTypeEnum.name());
            }
            optionPopupView.setSelectedId(bloodTypeOptionId);
            optionPopupView.setCallback(this::bloodTypeConfirm);
            optionPopupView.show(binding.rootView, R.id.nameView, true);
        });

        setClosePopup(binding.idView.getKey());
        setClosePopup(binding.bloodTypeView.getKey());

        binding.year.setOnClickListener(v -> {
            closePopup();
            numberPopupView.set(yearSpinnerModel);
            numberPopupView.show(binding.rootView, R.id.year, false);
        });

        binding.month.setOnClickListener(v -> {
            closePopup();
            numberPopupView.set(monthSpinnerModel);
            numberPopupView.show(binding.rootView, R.id.month, false);
        });

        binding.day.setOnClickListener(v -> {
            closePopup();
            numberPopupView.set(daySpinnerModel);
            numberPopupView.show(binding.rootView, R.id.day, false);
        });

        binding.confirm.setOnClickListener(v ->
                Observable.just(this).observeOn(Schedulers.io())
                        .subscribe(obj -> {
                            UserEntity userEntity;
                            if (systemModel.tagId == PatientAddEnum.PatientAdd.ordinal()) {
                                //添加患者
                                userEntity = fillLastUserEntity();
                                fillUserEntity(userEntity);
                                daoControl.getUserDaoOperation().insert(userEntity);
                                lastUser.setUserEntity(userEntity);
                                lastUser.setEditUserEntity(userEntity);
                                lastUser.updated.notifyChange();
                                systemModel.showLayout(LayoutPageEnum.PATIENT_INFO);
                            } else if (systemModel.tagId == PatientAddEnum.PatientEdit.ordinal()) {
                                //修改患者
                                userEntity = lastUser.getEditUserEntity();
                                fillUserEntity(userEntity);
                                daoControl.getUserDaoOperation().insertOrUpdate(userEntity);
                                daoControl.refreshLastUser();
                                lastUser.updated.notifyChange();
                                systemModel.showLayout(LayoutPageEnum.PATIENT_INFO);
                            } else if (systemModel.tagId == PatientAddEnum.PatientList.ordinal()) {
                                //修改患者
                                userEntity = lastUser.getEditUserEntity();
                                fillUserEntity(userEntity);
                                daoControl.getUserDaoOperation().insertOrUpdate(userEntity);
                                daoControl.refreshLastUser();
                                lastUser.updated.notifyChange();
                                systemModel.showLayout(LayoutPageEnum.PATIENT_LIST_INFO);
                            }
                        }));

        binding.addPatient.setVisibility(View.GONE);
        binding.editPatient.setVisibility(View.GONE);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        if (systemModel.tagId == PatientAddEnum.PatientAdd.ordinal()) {
            //添加患者
            loadDefault();
            ((TitleView) findViewById(titleId)).set(R.string.add_patient, LayoutPageEnum.PATIENT_INFO, true);
        } else if (systemModel.tagId == PatientAddEnum.PatientAddIdOK.ordinal()) {
            systemModel.tagId = PatientAddEnum.PatientAdd.ordinal();
            binding.idView.setValue(inputModel.inputText.getValue());
            enableFunction(inputModel.inputText.getValue().length() > 0);
        } else if (systemModel.tagId == PatientAddEnum.PatientAddUserNameOK.ordinal()) {
            systemModel.tagId = PatientAddEnum.PatientAdd.ordinal();
            binding.nameView.setValue(inputModel.inputText.getValue());
        } else if (systemModel.tagId == PatientAddEnum.PatientEdit.ordinal()) {
            //修改患者
            loadDefaultFromUserEntity(lastUser.getEditUserEntity());
            enableFunction(true);
            ((TitleView) findViewById(titleId)).set(R.string.edit_patient, LayoutPageEnum.PATIENT_INFO, true);
        } else if (systemModel.tagId == PatientAddEnum.PatientEditIdOK.ordinal()) {
            systemModel.tagId = PatientAddEnum.PatientEdit.ordinal();
            binding.idView.setValue(inputModel.inputText.getValue());
            enableFunction(inputModel.inputText.getValue().length() > 0);
        } else if (systemModel.tagId == PatientAddEnum.PatientEditUserNameOK.ordinal()) {
            systemModel.tagId = PatientAddEnum.PatientEdit.ordinal();
            binding.nameView.setValue(inputModel.inputText.getValue());
        } else if (systemModel.tagId == PatientAddEnum.PatientList.ordinal()) {
            //列表修改患者
            loadDefaultFromUserEntity(lastUser.getEditUserEntity());
            enableFunction(true);
            ((TitleView) findViewById(titleId)).set(R.string.edit_patient, LayoutPageEnum.PATIENT_LIST_INFO, true);
        } else if (systemModel.tagId == PatientAddEnum.PatientListIdOK.ordinal()) {
            systemModel.tagId = PatientAddEnum.PatientList.ordinal();
            binding.idView.setValue(inputModel.inputText.getValue());
            enableFunction(inputModel.inputText.getValue().length() > 0);
        } else if (systemModel.tagId == PatientAddEnum.PatientListUserNameOK.ordinal()) {
            systemModel.tagId = PatientAddEnum.PatientList.ordinal();
            binding.nameView.setValue(inputModel.inputText.getValue());
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    private UserEntity fillLastUserEntity() {
        UserEntity userEntity = lastUser.getUserEntity();
        long currentSecond = TimeUtil.getCurrentTimeInSecond();
        if (userEntity != null) {
            userEntity.endTimeStamp = currentSecond;
            daoControl.getUserDaoOperation().insertOrUpdate(userEntity);
        } else {
            userEntity = new UserEntity();
        }
        userEntity.timeStamp = currentSecond;
        userEntity.endTimeStamp = Long.MAX_VALUE;
        return userEntity;
    }

    private void fillUserEntity(UserEntity userEntity) {
        userEntity.userId = binding.idView.getValue().getText().toString();
        userEntity.sex = binding.sexView.getSelect() == 1;
        userEntity.gestation = getIntegerPopupModel(0).getOriginValue();
        userEntity.weight = getIntegerPopupModel(1).getOriginValue();
        userEntity.height = getIntegerPopupModel(2).getOriginValue();
        userEntity.age = getIntegerPopupModel(3).getOriginValue();
        userEntity.paceMaker = binding.paceMakerView.getSelect() == 1;

        userEntity.name = binding.nameView.getValue().getText().toString();
        String bloodType = binding.bloodTypeView.getValue().getText().toString();
        BloodTypeEnum bloodTypeEnum = BloodTypeEnum.valueOf(bloodType);
        userEntity.bloodType = bloodTypeEnum.ordinal();

        userEntity.birthdayYear = Integer.parseInt(binding.year.getText().toString());
        userEntity.birthdayMonth = Integer.parseInt(binding.month.getText().toString());
        userEntity.birthdayDay = Integer.parseInt(binding.day.getText().toString());
    }

    private void loadDefault() {
        binding.idView.setValue("");
        binding.idView.setSelected(false);
        binding.sexView.select(0);
        binding.sexView.setSelected(false);

        setCurrentDate();
        binding.year.setSelected(false);
        binding.month.setSelected(false);
        binding.day.setSelected(false);

        setOriginValue(0, 40);
        setOriginValue(1, 3000);
        setOriginValue(2, 50);
        setOriginValue(3, 0);

        getIntegerPopupModel(0).getKeyButtonView().setSelected(false);
        getIntegerPopupModel(1).getKeyButtonView().setSelected(false);
        getIntegerPopupModel(2).getKeyButtonView().setSelected(false);
        getIntegerPopupModel(3).getKeyButtonView().setSelected(false);

        binding.paceMakerView.select(0);
        binding.paceMakerView.setSelected(false);

        binding.nameView.setValue("");
        binding.nameView.setSelected(false);

        bloodTypeOptionId = 0;
        binding.bloodTypeView.setValue(BloodTypeEnum.A.name());
        binding.bloodTypeView.setSelected(false);

        enableFunction(false);
    }

    private void bloodTypeConfirm(int index) {
        if (index != bloodTypeOptionId) {
            bloodTypeOptionId = index;
            binding.bloodTypeView.setValue(BloodTypeEnum.values()[index].name());
            binding.bloodTypeView.setSelected(true);
        }
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        yearSpinnerModel.setOriginValue(calendar.get(Calendar.YEAR));
        monthSpinnerModel.setOriginValue(calendar.get(Calendar.MONTH) + 1);
        daySpinnerModel.setOriginValue(calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void enableFunction(boolean status) {
        if (status) {
            binding.confirm.setEnabled(true);
            binding.confirm.setImageResource(R.mipmap.confirm);
        } else {
            binding.confirm.setEnabled(false);
            binding.confirm.setImageResource(R.mipmap.confirm_disabled);
        }
    }

    private void setClosePopup(View view) {
        view.setOnClickListener(v -> closePopup());
    }
}