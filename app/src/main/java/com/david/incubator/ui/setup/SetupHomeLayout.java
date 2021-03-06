package com.david.incubator.ui.setup;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class SetupHomeLayout extends SetupBaseLayout {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SystemModel systemModel;

    public SetupHomeLayout(Context context) {
        super(context, 6);
        ContextUtil.getComponent().inject(this);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        int rowId = 0;
        SetupPageEnum setupPageEnum = SetupPageEnum.values()[systemModel.tagId % 100];
        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            setSelectId(setupPageEnum, SetupPageEnum.Spo2, rowId);
            binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.spo2_id), SetupPageEnum.Spo2.ordinal());
        }

        if (moduleHardware.isActive(ModuleEnum.Ecg)) {
            setSelectId(setupPageEnum, SetupPageEnum.Ecg, rowId);
            binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.ecg_id), SetupPageEnum.Ecg.ordinal());

            setSelectId(setupPageEnum, SetupPageEnum.Resp, rowId);
            binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.resp), SetupPageEnum.Resp.ordinal());
        }

        if (moduleHardware.isActive(ModuleEnum.Co2)) {
            setSelectId(setupPageEnum, SetupPageEnum.Co2, rowId);
            binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.co2), SetupPageEnum.Co2.ordinal());
        }

        if (moduleHardware.isActive(ModuleEnum.Nibp)) {
            setSelectId(setupPageEnum, SetupPageEnum.Nibp, rowId);
            binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.nibp), SetupPageEnum.Nibp.ordinal());
        }

        if (moduleHardware.isActive(ModuleEnum.Wake)) {
            setSelectId(setupPageEnum, SetupPageEnum.Wake, rowId);
            binding.tabHomeLayout.setText(rowId, ContextUtil.getString(R.string.wake), SetupPageEnum.Wake.ordinal());
        }

        super.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
    }
}