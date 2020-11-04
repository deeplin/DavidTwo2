package com.david.incubator.ui.setup;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.databinding.LayoutTabBinding;

import java.util.Objects;

public class SetupBaseLayout extends BindingBasicLayout<LayoutTabBinding> {

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
        binding.titleView.set(R.string.setup, null, true);
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