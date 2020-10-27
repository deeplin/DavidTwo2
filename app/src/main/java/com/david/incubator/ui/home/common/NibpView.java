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
import com.david.databinding.ViewNibpBinding;

import javax.inject.Inject;

public class NibpView extends BindingBasicLayout<ViewNibpBinding> {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    NibpModel nibpModel;
    @Inject
    SystemModel systemModel;

    public NibpView(Context context, AttributeSet attrs) {
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
        return R.layout.view_nibp;
    }

    public void set(SensorModel nibpSensorModel, NibpModel nibpModel) {
        binding.setViewModel(nibpSensorModel);
        binding.setNibpView(nibpModel);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        nibpModel.attach();

        if (moduleHardware.isActive(ModuleEnum.Nibp)) {
            this.setVisibility(View.VISIBLE);
            setDisable(false);
        } else if (moduleHardware.isInActive(ModuleEnum.Nibp)) {
            this.setVisibility(View.VISIBLE);
            setDisable(true);
        } else {
            this.setVisibility(View.GONE);
        }
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

    public void setDisable(boolean status) {
        if (status) {
            binding.title.setBackgroundResource(R.drawable.background_panel_dark);
            binding.unit.setVisibility(View.INVISIBLE);
            binding.integerPart.setVisibility(View.INVISIBLE);
            binding.upperLimit.setVisibility(View.INVISIBLE);
            binding.lowerLimit.setVisibility(View.INVISIBLE);
            binding.functionTitle.setVisibility(View.INVISIBLE);
            binding.functionValue.setVisibility(View.INVISIBLE);
            binding.subFieldString.setVisibility(View.INVISIBLE);
        } else {
            binding.title.setBackgroundResource(R.drawable.background_panel_pink);
            binding.unit.setVisibility(View.VISIBLE);
            binding.integerPart.setVisibility(View.VISIBLE);
            binding.upperLimit.setVisibility(View.VISIBLE);
            binding.lowerLimit.setVisibility(View.VISIBLE);
            binding.functionTitle.setVisibility(View.VISIBLE);
            binding.functionValue.setVisibility(View.VISIBLE);
            binding.subFieldString.setVisibility(View.VISIBLE);
        }
    }
}