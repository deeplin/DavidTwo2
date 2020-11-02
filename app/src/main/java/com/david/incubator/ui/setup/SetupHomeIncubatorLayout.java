package com.david.incubator.ui.setup;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutTabBinding;

import java.util.Objects;

import javax.inject.Inject;

public class SetupHomeIncubatorLayout extends BindingBasicLayout<LayoutTabBinding> {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SystemModel systemModel;
    @Inject
    IncubatorModel incubatorModel;

    public SetupHomeIncubatorLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        binding.tabHomeLayout.setTabConsumer(binding.tabFrameLayout::show);
        binding.tabHomeLayout.init(3);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tab;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.titleView.set(R.string.setup, null, true);

        binding.tabFrameLayout.attach(lifecycleOwner);

        int rowId = 0;
        SetupPageEnum targetSetupPageEnum = SetupPageEnum.values()[systemModel.tagId];
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
        binding.tabHomeLayout.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
        binding.tabHomeLayout.detach();
        binding.tabFrameLayout.detach();
    }

    private void setSelectId(SetupPageEnum targetSetupPageEnum, SetupPageEnum setupPageEnum, int id) {
        if (Objects.equals(targetSetupPageEnum, setupPageEnum)) {
            binding.tabHomeLayout.setTab(id);
        }
    }
}