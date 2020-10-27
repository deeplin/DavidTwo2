package com.david.incubator.ui.home.standard.middle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutMiddleLeftBinding;

import javax.inject.Inject;

public class MiddleLeftLayout extends BindingBasicLayout<LayoutMiddleLeftBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    ModuleHardware moduleHardware;

    public MiddleLeftLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        binding.skin2SensorView.setOnClickListener(view -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Temp);
        });

        binding.skin2SensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Skin2));

        if (!moduleHardware.isInstalled(ModuleEnum.Angle)) {
            binding.angleSensorView.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_middle_left;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.skin2SensorView.attach(lifeCycleOwner);
        binding.angleSensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Angle));
        binding.angleSensorView.attach(lifeCycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
        binding.angleSensorView.detach();
        binding.skin2SensorView.detach();
    }

    public void setDarkMode(boolean darkMode) {
        binding.skin2SensorView.setSensorDarkMode(darkMode);
        binding.angleSensorView.setSensorDarkMode(darkMode);
    }
}