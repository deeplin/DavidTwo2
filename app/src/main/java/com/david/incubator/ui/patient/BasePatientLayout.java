package com.david.incubator.ui.patient;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.database.LastUser;
import com.david.core.database.entity.UserEntity;
import com.david.core.enumeration.BloodTypeEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.ui.layout.BindingLayout;
import com.david.core.ui.model.IntegerButtonModel;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.TimeUtil;
import com.david.databinding.LayoutPatientBinding;

import java.util.Locale;

import javax.inject.Inject;

public abstract class BasePatientLayout extends BindingLayout<LayoutPatientBinding> {

    @Inject
    LastUser lastUser;

    protected IntegerButtonModel yearSpinnerModel;
    protected IntegerButtonModel monthSpinnerModel;
    protected IntegerButtonModel daySpinnerModel;

    public BasePatientLayout(Context context) {
        super(context);
    }

    protected void initPatient(LayoutPageEnum layoutPageEnum) {
        super.init(layoutPageEnum);
        addKeyButtonWithLiveData(0, 3);
        addKeyButtonWithLiveData(1, 4);
        addKeyButtonWithLiveData(2, 5);
        addKeyButtonWithLiveData(3, 6);

        yearSpinnerModel = new IntegerButtonModel(binding.year);
        yearSpinnerModel.setMin(1950);
        yearSpinnerModel.setMax(2050);
        yearSpinnerModel.setCallback(this::checkDay);
        monthSpinnerModel = new IntegerButtonModel(binding.month);
        monthSpinnerModel.setMin(1);
        monthSpinnerModel.setMax(12);
        monthSpinnerModel.setConverter(this::formatValue);
        monthSpinnerModel.setCallback(this::checkDay);
        daySpinnerModel = new IntegerButtonModel(binding.day);
        daySpinnerModel.setMin(1);
        daySpinnerModel.setMax(31);
        daySpinnerModel.setConverter(this::formatValue);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        getIntegerPopupModel(0).getKeyButtonView().setKeyId(R.string.gestation);
        getIntegerPopupModel(1).getKeyButtonView().setKeyId(R.string.birth_weight);
        getIntegerPopupModel(2).getKeyButtonView().setKeyId(R.string.height);
        getIntegerPopupModel(3).getKeyButtonView().setKeyId(R.string.age);
        super.attach(lifecycleOwner);

        binding.idView.setKeyId(R.string.id);
        binding.nameView.setKeyId(R.string.name);
        binding.bloodTypeView.setKeyId(R.string.blood_type);

        binding.sexView.setKeyId(R.string.sex);
        binding.sexView.setValue(ContextUtil.getString(R.string.male), ContextUtil.getString(R.string.female));
        binding.sexView.setCallback(this::sexCallback);

        binding.paceMakerView.setKeyId(R.string.pace_maker);
        binding.paceMakerView.setValue(ContextUtil.getString(ListUtil.statusList.get(0)), ContextUtil.getString(ListUtil.statusList.get(1)));

        binding.paceMakerView.setCallback(this::paceMakerCallback);
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void sexCallback(int optionId) {
        binding.sexView.select(optionId);
    }

    private void paceMakerCallback(int optionId) {
        binding.paceMakerView.select(optionId);
    }

    private int getDaysByYearMonth() {
        return TimeUtil.getDaysByYearMonth(yearSpinnerModel.getOriginValue(), monthSpinnerModel.getOriginValue());
    }

    private void checkDay(Integer value) {
        int maxDay = getDaysByYearMonth();
        daySpinnerModel.setMax(maxDay);
        if (daySpinnerModel.getOriginValue() > maxDay) {
            daySpinnerModel.setOriginValue(maxDay);
        }
    }

    private String formatValue(int value) {
        return String.format(Locale.US, "%02d", value);
    }

    protected void loadDefaultFromUserEntity(UserEntity userEntity) {
        if (userEntity != null) {
            binding.idView.setValue(userEntity.userId);
            binding.sexView.select(userEntity.sex ? 1 : 0);
            setOriginValue(0, userEntity.gestation);
            setOriginValue(1, userEntity.weight);
            setOriginValue(2, userEntity.height);
            setOriginValue(3, userEntity.age);
            binding.paceMakerView.select(userEntity.paceMaker ? 1 : 0);

            binding.nameView.setValue(userEntity.name);
            binding.bloodTypeView.setValue(BloodTypeEnum.values()[userEntity.bloodType].name());

            yearSpinnerModel.setOriginValue(userEntity.birthdayYear);
            monthSpinnerModel.setOriginValue(userEntity.birthdayMonth);
            daySpinnerModel.setOriginValue(userEntity.birthdayDay);
            binding.editPatient.setEnabled(true);

            binding.idView.setSelected(false);
            binding.sexView.setSelected(false);
            getIntegerPopupModel(0).getKeyButtonView().setSelected(false);
            getIntegerPopupModel(1).getKeyButtonView().setSelected(false);
            getIntegerPopupModel(2).getKeyButtonView().setSelected(false);
            getIntegerPopupModel(3).getKeyButtonView().setSelected(false);
            binding.year.setSelected(false);
            binding.month.setSelected(false);
            binding.day.setSelected(false);
            binding.paceMakerView.setSelected(false);

            binding.nameView.setSelected(false);
            binding.bloodTypeView.setSelected(false);
        } else {
            binding.editPatient.setEnabled(false);
        }
    }
}