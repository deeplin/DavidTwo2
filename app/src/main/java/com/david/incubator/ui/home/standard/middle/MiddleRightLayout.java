package com.david.incubator.ui.home.standard.middle;

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
import com.david.databinding.LayoutMiddleRightBinding;

import java.util.Objects;

import javax.inject.Inject;

public class MiddleRightLayout extends BindingBasicLayout<LayoutMiddleRightBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    IncubatorModel incubatorModel;

    private final Observer<SystemEnum> systemEnumObserver;

    public MiddleRightLayout(Context context, AttributeSet attrs) {
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
                ViewUtil.setDisable(moduleHardware, ModuleEnum.Hum, binding.humiditySensorView, false);
                ViewUtil.setDisable(moduleHardware, ModuleEnum.Oxygen, binding.oxygenSensorView, false);
            } else {
                binding.humiditySensorView.setVisibility(View.GONE);
                binding.oxygenSensorView.setVisibility(View.GONE);
            }
        };

        binding.prHrView.setTitleBackground(R.drawable.background_panel_blue);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_middle_right;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.humiditySensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Humidity));
        binding.humiditySensorView.attach(lifeCycleOwner);
        binding.oxygenSensorView.set(sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen));
        binding.oxygenSensorView.attach(lifeCycleOwner);

        binding.prHrView.attach(lifeCycleOwner);
        incubatorModel.systemMode.observeForever(systemEnumObserver);
    }

    @Override
    public void detach() {
        super.detach();
        incubatorModel.systemMode.removeObserver(systemEnumObserver);
        binding.prHrView.detach();
        binding.oxygenSensorView.detach();
        binding.humiditySensorView.detach();
    }

    public void setDarkMode(boolean darkMode) {
        binding.prHrView.setSensorDarkMode(darkMode);
        binding.humiditySensorView.setSensorDarkMode(darkMode);
        binding.oxygenSensorView.setSensorDarkMode(darkMode);
    }
}