package com.david.incubator.ui.home.basic;

import android.content.Context;
import android.util.AttributeSet;
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
import com.david.core.util.ViewUtil;
import com.david.databinding.LayoutMiddleRightBasicBinding;

import java.util.Locale;
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
    private final Observer<Integer> cTimeObserver;

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

        binding.matSensorView.setOnClickListener(view -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Mat);
        });

        binding.blueSensorView.setOnClickListener(view -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Blue);
        });

        systemEnumObserver = systemEnum -> {
            if (Objects.equals(systemEnum, SystemEnum.Cabin)) {
                ViewUtil.setDisable(moduleHardware, ModuleEnum.Hum, binding.humiditySensorView, true);
                ViewUtil.setDisable(moduleHardware, ModuleEnum.Oxygen, binding.oxygenSensorView, true);
                if (binding.humiditySensorView.getVisibility() == View.GONE &&
                        binding.oxygenSensorView.getVisibility() == View.GONE) {
                    this.setVisibility(View.GONE);
                } else {
                    this.setVisibility(View.VISIBLE);
                }
                binding.blueSensorView.setVisibility(View.GONE);
                binding.matSensorView.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
            } else if (Objects.equals(systemEnum, SystemEnum.Warmer)) {
                binding.humiditySensorView.setVisibility(View.GONE);
                binding.oxygenSensorView.setVisibility(View.GONE);

                if (moduleHardware.isInstalled(ModuleEnum.Mat)) {
                    binding.matSensorView.setVisibility(View.VISIBLE);
                } else {
                    binding.matSensorView.setVisibility(View.GONE);
                }

                if (moduleHardware.isInstalled(ModuleEnum.Blue)) {
                    binding.blueSensorView.setVisibility(View.VISIBLE);
                } else {
                    binding.blueSensorView.setVisibility(View.GONE);
                }

                if (binding.matSensorView.getVisibility() == View.GONE &&
                        binding.blueSensorView.getVisibility() == View.GONE) {
                    this.setVisibility(View.GONE);
                } else {
                    this.setVisibility(View.VISIBLE);
                }
            } else {
                this.setVisibility(View.GONE);
            }
        };

        cTimeObserver = integer -> {
            String heatString = String.format(Locale.US, "%02d:%02d", (integer / 60) % 60, integer % 60);
            binding.blueSensorView.setText(heatString);
        };
        binding.blueSensorView.setUnit("h");
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

        incubatorModel.systemMode.observeForever(systemEnumObserver);
        incubatorModel.cTime.observeForever(cTimeObserver);
    }

    @Override
    public void detach() {
        super.detach();
        incubatorModel.cTime.removeObserver(cTimeObserver);
        incubatorModel.systemMode.removeObserver(systemEnumObserver);
        binding.oxygenSensorView.detach();
        binding.humiditySensorView.detach();

        binding.blueSensorView.detach();
        binding.matSensorView.detach();
    }

    public void setDarkMode(boolean darkMode) {
        binding.humiditySensorView.setSensorDarkMode(darkMode);
        binding.oxygenSensorView.setSensorDarkMode(darkMode);
        binding.matSensorView.setSensorDarkMode(darkMode);
        binding.blueSensorView.setSensorDarkMode(darkMode);
    }
}