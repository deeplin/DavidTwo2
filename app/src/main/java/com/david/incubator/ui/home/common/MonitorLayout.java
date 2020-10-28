package com.david.incubator.ui.home.common;

import android.content.Context;
import android.util.AttributeSet;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutMonitorBinding;

import javax.inject.Inject;

public class MonitorLayout extends BindingBasicLayout<LayoutMonitorBinding> {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SystemModel systemModel;

    public MonitorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        binding.hrPrView.setTitleBackground(R.drawable.background_panel_blue);
        binding.hrPrView.setSmallLayout();
        //        binding.co2View.setSmallLayout();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_monitor;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.hrPrView.attach(lifecycleOwner);
        binding.spo2View.attach(lifecycleOwner);
        binding.co2View.attach(lifecycleOwner);
//
//        if (moduleSoftware.nibp.getValue()) {
//            binding.nibpView.setVisibility(View.VISIBLE);
//            binding.nibpView.attach(lifecycleOwner);
//            binding.nibpView.setUniqueColor();
//        } else {
//            binding.nibpView.setVisibility(View.GONE);
//        }
//
        boolean darkStatus = systemModel.darkMode.getValue();
        binding.hrPrView.setSensorDarkMode(darkStatus);
        binding.spo2View.setSensorDarkMode(darkStatus);
        binding.co2View.setSensorDarkMode(darkStatus);
//        binding.nibpView.setSensorDarkMode(darkStatus);
    }

    @Override
    public void detach() {
        super.detach();
//        binding.nibpView.detach();
        binding.co2View.detach();
        binding.spo2View.detach();
        binding.hrPrView.detach();
    }
}