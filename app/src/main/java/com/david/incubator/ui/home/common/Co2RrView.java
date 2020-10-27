package com.david.incubator.ui.home.common;

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
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.ViewCo2RrBinding;

import javax.inject.Inject;

public class Co2RrView extends BindingBasicLayout<ViewCo2RrBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    ModuleHardware moduleHardware;

    private final Observer<Boolean> selectCo2RrObserver;

    public Co2RrView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        binding.co2View.setOnClickListener(v -> gotoObjective());
        binding.rrView.setOnClickListener(v -> gotoObjective());
        binding.fiView.setOnClickListener(v -> gotoObjective());

        selectCo2RrObserver = aBoolean -> {
            aBoolean = true;
            if (aBoolean == null) {
                displayNoneSelection();
                binding.rootView.setVisibility(View.GONE);
            } else if (aBoolean) {
                SensorModel co2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Co2);
                binding.co2View.setCo2(co2Model, systemModel);
                binding.co2View.displayEt(true);
                binding.co2View.setDisable(false);
                SensorModel co2RrModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Rr);
                binding.rrView.set(co2RrModel);
                SensorModel co2FiModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Fi);
                binding.fiView.setCo2(co2FiModel, systemModel);
                binding.rrView.setVisibility(View.VISIBLE);
                binding.fiView.setVisibility(View.VISIBLE);
                binding.rootView.setVisibility(View.VISIBLE);
            } else {
                SensorModel ecgRrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr);
                binding.co2View.setResp(ecgRrModel);
                binding.co2View.displayEt(false);
                binding.co2View.setDisable(false);
                binding.rrView.set(null);
                binding.fiView.setResp(null);
                binding.rrView.setVisibility(View.GONE);
                binding.fiView.setVisibility(View.GONE);
                binding.rootView.setVisibility(View.VISIBLE);
            }
        };

        binding.co2View.setSmallLayout();
        binding.rrView.setSmallLayout();
        binding.fiView.setSmallLayout();
        binding.fiView.displayEt(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_co2_rr;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        systemModel.setRespUnit();
        binding.co2View.attach(lifecycleOwner);
        binding.rrView.attach(lifecycleOwner);
        binding.fiView.attach(lifecycleOwner);
        systemModel.selectCo2.observeForever(selectCo2RrObserver);
        if (systemModel.selectCo2.getValue() == null) {
            displayNoneSelection();
        }
    }

    @Override
    public void detach() {
        super.detach();
        systemModel.selectCo2.removeObserver(selectCo2RrObserver);
        binding.fiView.detach();
        binding.rrView.detach();
        binding.co2View.detach();
    }

    public void setUniqueColor() {
        binding.fiView.setUniqueColor();
        binding.rrView.setUniqueColor();
        binding.co2View.setUniqueColor();
    }

    private void gotoObjective() {
        if (systemModel.isFreeze()) {
            return;
        }
        if (systemModel.selectCo2.getValue() != null) {
            if (systemModel.selectCo2.getValue()) {
                systemModel.showSetupPage(SetupPageEnum.Co2);
            } else {
                systemModel.showSetupPage(SetupPageEnum.Resp);
            }
        }
    }

    private void displayNoneSelection() {
        if (moduleHardware.isActive(ModuleEnum.Co2)) {
            SensorModel co2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Co2);
            binding.co2View.setCo2(co2Model, systemModel);
            binding.rrView.set(null);
            binding.fiView.setResp(null);
            binding.rrView.setVisibility(View.GONE);
            binding.fiView.setVisibility(View.GONE);
            binding.rootView.setVisibility(View.VISIBLE);
            binding.co2View.setDisable(true);
        } else if (moduleHardware.isInActive(ModuleEnum.Ecg)) {
            SensorModel spo2Model = sensorModelRepository.getSensorModel(SensorModelEnum.EcgRr);
            binding.co2View.setResp(spo2Model);
            binding.rrView.set(null);
            binding.fiView.setResp(null);
            binding.rrView.setVisibility(View.GONE);
            binding.fiView.setVisibility(View.GONE);
            binding.rootView.setVisibility(View.VISIBLE);
            binding.co2View.setDisable(true);
        } else {
            binding.co2View.setResp(null);
            binding.rrView.set(null);
            binding.fiView.setResp(null);
            binding.rootView.setVisibility(View.GONE);
        }
    }
}