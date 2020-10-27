package com.david.incubator.ui.home.basic;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.buffer.BufferRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutBasicBinding;

import javax.inject.Inject;

public class BasicLayout extends BindingBasicLayout<LayoutBasicBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    BufferRepository bufferRepository;
    @Inject
    ModuleHardware moduleHardware;

    private final Observer<SystemEnum> systemEnumObserver;

    public BasicLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        binding.skin1SensorView.setOnClickListener(view -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Temp);
        });
        binding.skin2SensorView.setOnClickListener(view -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Temp);
        });
        binding.airSensorView.setOnClickListener(view -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Temp);
        });
        binding.homeSetView.setOnClickListener(view -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Temp);
        });

        systemEnumObserver = systemEnum -> {
            if (systemEnum.equals(SystemEnum.Cabin)) {
                binding.airSensorView.setVisibility(View.VISIBLE);
                binding.timingLayout.setVisibility(View.INVISIBLE);
            } else if (systemEnum.equals(SystemEnum.Warmer)) {
                binding.airSensorView.setVisibility(View.INVISIBLE);
                binding.timingLayout.setVisibility(View.VISIBLE);
            } else if (systemEnum.equals(systemEnum.Transit)) {
                binding.airSensorView.setVisibility(View.INVISIBLE);
                binding.timingLayout.setVisibility(View.INVISIBLE);
            }
        };

        binding.skin1SensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Skin1));
        binding.skin2SensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Skin2));
        binding.airSensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Air));
        binding.angleSensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Angle));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_basic;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.skin1SensorView.attach(lifeCycleOwner);
        binding.skin2SensorView.attach(lifeCycleOwner);
        binding.airSensorView.attach(lifeCycleOwner);
        binding.timingLayout.attach(lifeCycleOwner);
        binding.homeSetView.attach(lifeCycleOwner);

        binding.angleSensorView.attach(lifeCycleOwner);

        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            binding.standardSpo2Layout.setVisibility(View.VISIBLE);
            binding.standardSpo2Layout.setDisable(false);
            binding.standardSpo2Layout.attach(lifeCycleOwner);
        } else if (moduleHardware.isInActive(ModuleEnum.Spo2)) {
            binding.standardSpo2Layout.setVisibility(View.VISIBLE);
            binding.standardSpo2Layout.setDisable(true);
        } else {
            binding.standardSpo2Layout.setVisibility(View.GONE);
        }

        //        binding.middleRightSensorView.attach(lifeCycleOwner);

        incubatorModel.systemMode.observeForever(systemEnumObserver);

        boolean darkMode = systemModel.darkMode.getValue();
        binding.skin1SensorView.setSensorDarkMode(darkMode);
        binding.airSensorView.setSensorDarkMode(darkMode);
        binding.timingLayout.setSensorDarkMode(darkMode);
//        binding.middleRightSensorView.setDarkMode(darkMode);
        binding.standardSpo2Layout.setSensorDarkMode(darkMode);
    }

    @Override
    public void detach() {
        super.detach();
        incubatorModel.systemMode.removeObserver(systemEnumObserver);

        binding.standardSpo2Layout.detach();
//        binding.middleRightSensorView.detach();
//
        binding.angleSensorView.detach();
        binding.homeSetView.detach();
        binding.timingLayout.detach();
        binding.airSensorView.detach();
        binding.skin2SensorView.detach();
        binding.skin1SensorView.detach();
    }
}