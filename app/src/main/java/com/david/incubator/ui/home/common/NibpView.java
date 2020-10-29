package com.david.incubator.ui.home.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ConfigEnum;
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
    @Inject
    ConfigRepository configRepository;

    private final Observer<Integer> unitObserver;

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

        unitObserver = integer -> {
            SensorModel nibpModel = sensorModelRepository.getSensorModel(SensorModelEnum.Nibp);
            binding.upperLimit.setText(systemModel.nibpUnitFunction.apply(nibpModel.upperLimit.getValue()));
            binding.lowerLimit.setText(systemModel.nibpUnitFunction.apply(nibpModel.lowerLimit.getValue()));
        };
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

        configRepository.getConfig(ConfigEnum.NibpUnit).observeForever(unitObserver);
    }

    @Override
    public void detach() {
        super.detach();
        configRepository.getConfig(ConfigEnum.NibpUnit).removeObserver(unitObserver);

        nibpModel.detach();
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