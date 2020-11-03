package com.david.incubator.ui.main;

import android.content.Context;
import android.util.AttributeSet;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_incubator_list;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.homeSetView.attach(lifecycleOwner);
//
//        if (incubatorModel.isCabin()) {
//            SensorModel airSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
//            binding.skin2SensorView.set(airSensorModel, machineSensorModel);
//
//            if (moduleHardware.hum.getValue()) {
//                setInnerView(binding.humidityView, SensorModelEnum.Humidity, machineSensorModel, moduleSoftware.hum, false);
//                binding.humidityView.setVisibility(View.VISIBLE);
//            } else {
//                binding.humidityView.setVisibility(View.GONE);
//            }
//
//            if (moduleHardware.oxygen.getValue()) {
//                setInnerView(binding.oxygenView, SensorModelEnum.Oxygen, machineSensorModel, moduleSoftware.oxygen, false);
//                binding.oxygenView.setVisibility(View.VISIBLE);
//            } else {
//                binding.oxygenView.setVisibility(View.GONE);
//            }
//        } else {
//            SensorModel skin2SensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin2);
//            binding.skin2SensorView.set(skin2SensorModel, bodySensorModel);
//
//            binding.humidityView.setVisibility(View.GONE);
//            binding.oxygenView.setVisibility(View.GONE);
//        }
    }

    @Override
    public void detach() {
        super.detach();
        binding.homeSetView.detach();
    }

//    private void setInnerView(IncubatorListView incubatorListView, SensorModelEnum sensorModelEnum,
//                              ThemeSensorModel themeSensorModel, LazyLiveData<Boolean> moduleStatus, boolean hideObjective) {
//        SensorModel sensorModel = sensorModelRepository.getSensorModel(sensorModelEnum);
//        incubatorListView.set(sensorModel, themeSensorModel, moduleStatus, hideObjective);
//    }
}