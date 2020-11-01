package com.david.incubator.ui.user;

import android.content.Context;
import android.view.View;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.UnitEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class UserUnitSetupLayout extends BaseLayout {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SystemModel systemModel;

    private final UnitEnum[] nibpUnitEnums = new UnitEnum[2];

    public UserUnitSetupLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.USER_UNIT_SETUP);

        super.initPopup(2);

        super.setRowId(0, 0);
        super.setRowId(1, 1);

        nibpUnitEnums[0] = UnitEnum.mmHg;
        nibpUnitEnums[1] = UnitEnum.kPa;
        super.setPopup(0, nibpUnitEnums, null, true, null);
        super.setPopup(1, UnitEnum.values(), null, true, this::respCallback);
    }

    @Override
    public void attach() {
        super.attach();

        int index = 0;
        if (moduleHardware.isActive(ModuleEnum.Nibp)) {
            super.setConfig(index, ConfigEnum.NibpUnit);
            super.setText(index++, R.string.nibp, nibpUnitEnums);
        }

        if (moduleHardware.isActive(ModuleEnum.Co2)) {
            super.setConfig(index, ConfigEnum.Co2Unit);
            super.setText(index++, R.string.co2, UnitEnum.values());
        }

        for (; index < keyButtonViews.length; index++) {
            keyButtonViews[index].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void respCallback(Integer num) {
        systemModel.setRespUnit();
    }
}