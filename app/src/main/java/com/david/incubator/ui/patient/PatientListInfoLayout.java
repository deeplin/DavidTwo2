package com.david.incubator.ui.patient;

import android.content.Context;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.util.ContextUtil;

public class PatientListInfoLayout extends PatientInfoLayout {

    public PatientListInfoLayout(Context context) {
        super(context, LayoutPageEnum.PATIENT_LIST_INFO);
    }

    @Override
    protected void initText() {
        binding.addPatient.setText(ContextUtil.getString(R.string.delete_patient));
    }

    @Override
    protected int getEditEnumId() {
        return PatientAddEnum.PatientList.ordinal();
    }

    @Override
    protected void addPatient() {
        //删除患者
        systemModel.showLayout(LayoutPageEnum.PATIENT_CONFIRM_REMOVE);
    }
}
