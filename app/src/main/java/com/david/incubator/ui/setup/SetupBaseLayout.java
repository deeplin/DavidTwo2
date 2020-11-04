package com.david.incubator.ui.setup;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.databinding.LayoutTabBinding;

import java.util.Objects;

import javax.inject.Inject;

public class SetupBaseLayout extends BindingBasicLayout<LayoutTabBinding> {

    @Inject
    SystemModel systemModel;

    public SetupBaseLayout(Context context, int tabNum) {
        super(context);
        binding.tabHomeLayout.setTabConsumer(binding.tabFrameLayout::show);
        binding.tabHomeLayout.init(tabNum);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tab;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        if (systemModel.tagId < 100) {
            binding.titleView.set(R.string.setup, null, true);
        } else {
            binding.titleView.set(R.string.setup, LayoutPageEnum.MENU_PARAMETER_SETUP, true);
        }

        binding.tabFrameLayout.attach(lifecycleOwner);
        binding.tabHomeLayout.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
        binding.tabHomeLayout.detach();
        binding.tabFrameLayout.detach();
    }

    protected void setSelectId(SetupPageEnum targetSetupPageEnum, SetupPageEnum setupPageEnum, int id) {
        if (Objects.equals(targetSetupPageEnum, setupPageEnum)) {
            binding.tabHomeLayout.setTab(id);
        }
    }
}