package com.david.incubator.ui.user;

import android.content.Context;
import android.widget.Button;

import com.david.R;
import com.david.core.database.LastUser;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class UserHomeLayout extends BaseLayout {

    @Inject
    SystemModel systemModel;
    @Inject
    LastUser lastUser;

    private final Button languageButton;
    private final Button timeButton;
    private final Button overheatExperimentButton;
    private final Button patientListButton;
    private final Button moduleSetupButton;
    private final Button unitButton;

    public UserHomeLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.USER_HOME);

        languageButton = ViewUtil.buildButton(getContext());
        addInnerButton(0, languageButton);

        timeButton = ViewUtil.buildButton(getContext());
        addInnerButton(1, timeButton);

        overheatExperimentButton = ViewUtil.buildButton(getContext());
        addInnerButton(2, overheatExperimentButton);

        patientListButton = ViewUtil.buildButton(getContext());
        addInnerButton(3, patientListButton);

        moduleSetupButton = ViewUtil.buildButton(getContext());
        addInnerButton(4, moduleSetupButton);

        unitButton = ViewUtil.buildButton(getContext());
        addInnerButton(5, unitButton);

        languageButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.USER_LANGUAGE));
//        timeButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.USER_TIME));
//        overheatExperimentButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.USER_OVERHEAT_EXPERIMENT));
//        patientListButton.setOnClickListener(v -> {
//            lastUser.userOffset = 0;
//            systemModel.showLayout(LayoutPageEnum.PATIENT_LIST);
//        });
//        moduleSetupButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.USER_MODULE_SETUP));
//        unitButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.USER_UNIT_SETUP));
    }

    @Override
    public void attach() {
        super.attach();
        systemModel.tagId = Constant.NA_VALUE;


        languageButton.setText(ContextUtil.getString(R.string.language));
        timeButton.setText(ContextUtil.getString(R.string.time));
        overheatExperimentButton.setText(ContextUtil.getString(R.string.overheat_experiment));
        patientListButton.setText(ContextUtil.getString(R.string.patient_list));
        moduleSetupButton.setText(ContextUtil.getString(R.string.module_setup));
        unitButton.setText(ContextUtil.getString(R.string.unit_setup));
    }

    @Override
    public void detach() {
        super.detach();
    }
}