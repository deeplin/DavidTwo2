package com.david.incubator.ui.setup;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class SetupHomeIncubatorLayout extends SetupBaseLayout {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SystemModel systemModel;
    @Inject
    IncubatorModel incubatorModel;

    public SetupHomeIncubatorLayout(Context context) {
        super(context, 3);
        ContextUtil.getComponent().inject(this);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        int rowId = 0;
        SetupPageEnum targetSetupPageEnum = SetupPageEnum.values()[systemModel.tagId % 100];
        setSelectId(targetSetupPageEnum, SetupPageEnum.Temp, rowId);
        binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.temp), SetupPageEnum.Temp.ordinal());

        if (incubatorModel.isCabin()) {
            if (moduleHardware.isActive(ModuleEnum.Hum)) {
                setSelectId(targetSetupPageEnum, SetupPageEnum.Humidity, rowId);
                binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.humidity), SetupPageEnum.Humidity.ordinal());
            }
            if (moduleHardware.isActive(ModuleEnum.Oxygen)) {
                setSelectId(targetSetupPageEnum, SetupPageEnum.Oxygen, rowId);
                binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.oxygen), SetupPageEnum.Oxygen.ordinal());
            }
        } else {
            if (moduleHardware.isInstalled(ModuleEnum.Blue)) {
                setSelectId(targetSetupPageEnum, SetupPageEnum.Blue, rowId);
                binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.blue), SetupPageEnum.Blue.ordinal());
            }
            if (moduleHardware.isInstalled(ModuleEnum.Mat)) {
                setSelectId(targetSetupPageEnum, SetupPageEnum.Mat, rowId);
                binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.mat), SetupPageEnum.Mat.ordinal());
            }
        }
        super.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
    }
}