package com.david.incubator.ui.alarm;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.util.ContextUtil;
import com.david.incubator.ui.setup.SetupBaseLayout;

import javax.inject.Inject;

public class SetupAlarmLayout extends SetupBaseLayout {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SystemModel systemModel;

    public SetupAlarmLayout(Context context) {
        super(context, 5);
        ContextUtil.getComponent().inject(this);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        int rowId = 0;
        SetupPageEnum targetPageEnum = SetupPageEnum.values()[systemModel.tagId % 100];

        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            setSelectId(targetPageEnum, SetupPageEnum.Spo2Alarm, rowId);
            binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.spo2_id), SetupPageEnum.Spo2Alarm.ordinal());
        }

        if (moduleHardware.isActive(ModuleEnum.Ecg)) {
            setSelectId(targetPageEnum, SetupPageEnum.EcgAlarm, rowId);
            binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.ecg_id), SetupPageEnum.EcgAlarm.ordinal());

            setSelectId(targetPageEnum, SetupPageEnum.RespAlarm, rowId);
            binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.resp), SetupPageEnum.RespAlarm.ordinal());
        }

        if (moduleHardware.isActive(ModuleEnum.Co2)) {
            setSelectId(targetPageEnum, SetupPageEnum.Co2Alarm, rowId);
            binding.tabHomeLayout.setText(rowId++, ContextUtil.getString(R.string.co2), SetupPageEnum.Co2Alarm.ordinal());
        }

        if (moduleHardware.isActive(ModuleEnum.Nibp)) {
            setSelectId(targetPageEnum, SetupPageEnum.NibpAlarm, rowId);
            binding.tabHomeLayout.setText(rowId, ContextUtil.getString(R.string.nibp), SetupPageEnum.NibpAlarm.ordinal());
        }

        super.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
    }
}