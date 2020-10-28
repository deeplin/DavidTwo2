package com.david.incubator.ui.home.common;

import android.content.Context;
import android.util.AttributeSet;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.buffer.BufferRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;
import com.david.databinding.ViewSpo2ListBinding;

import javax.inject.Inject;

public class Spo2ListView extends BindingBasicLayout<ViewSpo2ListBinding> {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    SystemModel systemModel;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    BufferRepository bufferRepository;

    public Spo2ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        binding.spo2View.setOnClickListener(v -> {
            if (systemModel.isFreeze()) {
                return;
            }
            if (moduleHardware.isActive(ModuleEnum.Spo2))
                systemModel.showSetupPage(SetupPageEnum.Spo2);
        });

        binding.piView.setOnClickListener(v -> {
            if (systemModel.isFreeze()) {
                return;
            }
            if (moduleHardware.isActive(ModuleEnum.Spo2))
                systemModel.showSetupPage(SetupPageEnum.Spo2);
        });

        binding.siqView.set(bufferRepository.getSpo2Buffer());

        SensorModel spo2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Spo2);
        binding.spo2View.set(spo2Model);
        binding.spo2View.setUniqueColor();
        binding.spo2View.setSpo2SmallLayout();

        SensorModel piModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pi);
        binding.piView.set(piModel);
        binding.piView.setUniqueColor();
        binding.piView.setSpo2SmallLayout();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_spo2_list;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.siqView.attach(lifeCycleOwner);
        binding.spo2View.attach(lifeCycleOwner);
        binding.piView.attach(lifeCycleOwner);

        ViewUtil.setDisable(moduleHardware, ModuleEnum.Spo2, binding.spo2View, true);
        ViewUtil.setDisable(moduleHardware, ModuleEnum.Spo2, binding.piView, true);
    }

    @Override
    public void detach() {
        super.detach();
        binding.spo2View.detach();
        binding.piView.detach();
        binding.siqView.detach();
    }
}