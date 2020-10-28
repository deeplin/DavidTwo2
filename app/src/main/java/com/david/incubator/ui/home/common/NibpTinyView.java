package com.david.incubator.ui.home.common;

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
import com.david.core.model.NibpModel;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.ViewNibpTinyBinding;

import javax.inject.Inject;

public class NibpTinyView extends BindingBasicLayout<ViewNibpTinyBinding> {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    NibpModel nibpModel;
    @Inject
    SystemModel systemModel;

    public NibpTinyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        SensorModel nibpSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Nibp);
        set(nibpSensorModel, nibpModel);

        binding.rootView.setOnClickListener(v -> {
            if (systemModel.isFreeze()) {
                return;
            }
            if (moduleHardware.isActive(ModuleEnum.Nibp))
                systemModel.showSetupPage(SetupPageEnum.Nibp);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_nibp_tiny;
    }

    public void set(SensorModel nibpSensorModel, NibpModel nibpModel) {
        binding.setViewModel(nibpSensorModel);
        binding.setNibpView(nibpModel);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        nibpModel.attach();

//        if (moduleHardware.isActive(ModuleEnum.Nibp))
//            if (moduleSoftware.nibp.getValue()) {
//
//            }
//        } else {
//            this.setVisibility(View.GONE);
//        }
    }

    @Override
    public void detach() {
        super.detach();
        nibpModel.detach();
    }

    public void setUniqueColor() {
        //todo deeplin
//        SensorModel sensorModel = binding.getViewModel();
//        binding.integerPart.setTextColor(sensorModel.getSensorModelEnum().getUniqueColor());
//        binding.decimalPart.setTextColor(sensorModel.getSensorModelEnum().getUniqueColor());
    }
}