package com.david.incubator.ui.patient;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.util.ContextUtil;
import com.david.incubator.ui.keyboard.InputModel;

import javax.inject.Inject;

public class PatientInfoLayout extends BasePatientLayout {

    @Inject
    SystemModel systemModel;

    public PatientInfoLayout(Context context) {
        this(context, LayoutPageEnum.PATIENT_INFO);
    }

    public PatientInfoLayout(Context context, LayoutPageEnum layoutPageEnum) {
        super(context, layoutPageEnum);
        ContextUtil.getComponent().inject(this);
        super.init();

        binding.confirm.setVisibility(View.GONE);
        binding.sexView.disable();
        binding.paceMakerView.disable();

        binding.editPatient.setOnClickListener(v -> {
            systemModel.tagId = getEditEnumId();
            systemModel.showLayout(LayoutPageEnum.PATIENT_ADD);
        });

        binding.addPatient.setOnClickListener(v -> addPatient());
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        loadDefaultFromUserEntity(lastUser.getEditUserEntity());
        initText();
    }

    @Override
    public void detach() {
        super.detach();
    }

    protected void initText() {
        binding.addPatient.setText(ContextUtil.getString(R.string.add_patient));
    }

    protected int getEditEnumId() {
        return PatientAddEnum.PatientEdit.ordinal();
    }

    protected void addPatient() {
        systemModel.tagId = PatientAddEnum.PatientAdd.ordinal();
        systemModel.showLayout(LayoutPageEnum.PATIENT_ADD);
    }
}
