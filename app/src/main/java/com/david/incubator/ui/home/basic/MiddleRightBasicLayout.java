package com.david.incubator.ui.home.basic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutMiddleRightBasicBinding;

import java.util.Objects;

import javax.inject.Inject;

public class MiddleRightBasicLayout extends BindingBasicLayout<LayoutMiddleRightBasicBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    IncubatorModel incubatorModel;

    private final Observer<SystemEnum> systemEnumObserver;

    public MiddleRightBasicLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        binding.humiditySensorView.setOnClickListener(view -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Humidity);
        });

        binding.oxygenSensorView.setOnClickListener(view -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Oxygen);
        });

        systemEnumObserver = systemEnum -> {
            if (Objects.equals(systemEnum, SystemEnum.Cabin)) {
                setHum();
                setOxygen();
                binding.blueSensorView.setVisibility(View.GONE);
                binding.matSensorView.setVisibility(View.GONE);
                binding.angleSensorView.setVisibility(View.GONE);
            } else if (Objects.equals(systemEnum, SystemEnum.Warmer)) {
//              setBlue();
//              setMat();
                binding.humiditySensorView.setVisibility(View.GONE);
                binding.oxygenSensorView.setVisibility(View.GONE);
                binding.blueSensorView.setVisibility(View.GONE);
                binding.matSensorView.setVisibility(View.GONE);
                binding.angleSensorView.setVisibility(View.GONE);
            } else {
                binding.humiditySensorView.setVisibility(View.GONE);
                binding.oxygenSensorView.setVisibility(View.GONE);
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_middle_right_basic;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);

        binding.humiditySensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Humidity));
        binding.humiditySensorView.attach(lifeCycleOwner);
        binding.oxygenSensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen));
        binding.oxygenSensorView.attach(lifeCycleOwner);

        binding.matSensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.MAT));
        binding.matSensorView.attach(lifeCycleOwner);
        binding.blueSensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Blue));
        binding.blueSensorView.attach(lifeCycleOwner);
        binding.angleSensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Angle));
        binding.angleSensorView.attach(lifeCycleOwner);

        incubatorModel.systemMode.observeForever(systemEnumObserver);
    }

    @Override
    public void detach() {
        super.detach();
        incubatorModel.systemMode.removeObserver(systemEnumObserver);
        binding.oxygenSensorView.detach();
        binding.humiditySensorView.detach();

        binding.blueSensorView.detach();
        binding.matSensorView.detach();
        binding.angleSensorView.detach();
    }

    private void setHum() {
//        if (moduleHardware.hum.getValue()) {
//            binding.humiditySensorView.setVisibility(View.VISIBLE);
//        } else {
//            binding.humiditySensorView.setVisibility(View.GONE);
//        }
    }

    private void setOxygen() {
//        if (moduleHardware.oxygen.getValue()) {
//            binding.oxygenSensorView.setVisibility(View.VISIBLE);
//        } else {
//            binding.oxygenSensorView.setVisibility(View.GONE);
//        }
    }

    //todo su
    //  private void setBlue() {
    //      binding.blueSensorView.setVisibility(View.VISIBLE);
    //  }

    //  private void setMat() {
    //     binding.matSensorView.setVisibility(View.VISIBLE);
    //  }

    public void setDarkMode(boolean darkMode) {
        binding.humiditySensorView.setSensorDarkMode(darkMode);
        binding.oxygenSensorView.setSensorDarkMode(darkMode);
        binding.angleSensorView.setSensorDarkMode(darkMode);
        binding.matSensorView.setSensorDarkMode(darkMode);
        binding.blueSensorView.setSensorDarkMode(darkMode);
    }
}