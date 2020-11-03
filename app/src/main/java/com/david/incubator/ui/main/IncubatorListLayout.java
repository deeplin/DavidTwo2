package com.david.incubator.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;
import com.david.databinding.LayoutIncubatorListBinding;

import javax.inject.Inject;

public class IncubatorListLayout extends BindingBasicLayout<LayoutIncubatorListBinding> {

    @Inject
    IncubatorModel incubatorModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    ModuleHardware moduleHardware;

    public IncubatorListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        SensorModel skin1SensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
        binding.skin1SensorView.set(skin1SensorModel);
        binding.skin1SensorView.setSensorDarkMode(false);
        SensorModel skin2SensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin2);
        binding.skin2SensorView.set(skin2SensorModel);
        binding.skin2SensorView.setSensorDarkMode(false);

        SensorModel humiditySensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Humidity);
        binding.humidityView.set(humiditySensorModel);
        binding.humidityView.setSensorDarkMode(false);
        SensorModel oxygenSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen);
        binding.oxygenView.set(oxygenSensorModel);
        binding.oxygenView.setSensorDarkMode(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_incubator_list;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.skin1SensorView.attach(lifecycleOwner);
        binding.skin2SensorView.attach(lifecycleOwner);
        binding.homeSetView.attach(lifecycleOwner);
        binding.oxygenView.attach(lifecycleOwner);
        binding.humidityView.attach(lifecycleOwner);

        if (incubatorModel.isCabin()) {
            ViewUtil.setDisable(moduleHardware, ModuleEnum.Hum, binding.humidityView, false);
            ViewUtil.setDisable(moduleHardware, ModuleEnum.Oxygen, binding.oxygenView, false);
        } else {
            binding.humidityView.setVisibility(View.GONE);
            binding.oxygenView.setVisibility(View.GONE);
        }
    }

    @Override
    public void detach() {
        super.detach();
        binding.skin1SensorView.detach();
        binding.skin2SensorView.detach();
        binding.homeSetView.detach();
        binding.humidityView.detach();
        binding.oxygenView.detach();
    }
}