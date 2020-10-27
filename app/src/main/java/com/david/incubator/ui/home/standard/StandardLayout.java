package com.david.incubator.ui.home.standard;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
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
import com.david.databinding.LayoutStandardBinding;

import javax.inject.Inject;

public class StandardLayout extends BindingBasicLayout<LayoutStandardBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    ModuleHardware moduleHardware;

    private final Observer<SystemEnum> systemEnumObserver;

    public StandardLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        binding.skin1SensorView.setOnClickListener(view -> {
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

        incubatorModel.KANG.observeForever(aBoolean -> {
            if (aBoolean) {
                binding.kang.setVisibility(View.VISIBLE);
            } else {
                binding.kang.setVisibility(View.GONE);
            }
        });

        binding.skin1SensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Skin1));
        binding.airSensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Air));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_standard;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.skin1SensorView.attach(lifeCycleOwner);
        binding.airSensorView.attach(lifeCycleOwner);
        binding.timingLayout.attach(lifeCycleOwner);
        binding.homeSetView.attach(lifeCycleOwner);

        binding.middleLeftSensorView.attach(lifeCycleOwner);
        binding.middleRightSensorView.attach(lifeCycleOwner);

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

        binding.bottomRightLayout.attach(lifeCycleOwner);

        incubatorModel.systemMode.observeForever(systemEnumObserver);

        boolean darkMode = systemModel.darkMode.getValue();
        binding.skin1SensorView.setSensorDarkMode(darkMode);
        binding.airSensorView.setSensorDarkMode(darkMode);
        binding.timingLayout.setSensorDarkMode(darkMode);
        binding.middleLeftSensorView.setDarkMode(darkMode);
        binding.middleRightSensorView.setDarkMode(darkMode);
        binding.standardSpo2Layout.setSensorDarkMode(darkMode);
        binding.bottomRightLayout.setDarkMode(darkMode);
    }

    @Override
    public void detach() {
        super.detach();
        incubatorModel.systemMode.removeObserver(systemEnumObserver);

        binding.bottomRightLayout.detach();
        binding.standardSpo2Layout.detach();
        binding.middleRightSensorView.detach();
        binding.middleLeftSensorView.detach();
        binding.homeSetView.detach();
        binding.timingLayout.detach();
        binding.airSensorView.detach();
        binding.skin1SensorView.detach();
    }
}