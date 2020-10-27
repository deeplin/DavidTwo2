package com.david.incubator.ui.home.basic;

import android.content.Context;
import android.util.AttributeSet;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.buffer.BufferRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutStandardSpo2BasicBinding;

import javax.inject.Inject;

public class StandardSpo2BasicLayout extends BindingBasicLayout<LayoutStandardSpo2BasicBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    BufferRepository bufferRepository;

    public StandardSpo2BasicLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        SensorModel spo2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Spo2);
        binding.spo2View.set(spo2Model);
        SensorModel prModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pr);
        binding.prView.set(prModel);
        SensorModel piModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pi);
        binding.piView.set(piModel);

        binding.spo2View.setOnClickListener(v -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Spo2);
        });

        binding.prView.setOnClickListener(v -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Spo2);
        });

        binding.piView.setOnClickListener(v -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Spo2);
        });

        binding.siqView.set(bufferRepository.getSpo2Buffer());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_standard_spo2_basic;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.siqView.attach(lifeCycleOwner);
        binding.spo2View.attach(lifeCycleOwner);
        binding.prView.attach(lifeCycleOwner);
        binding.piView.attach(lifeCycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
        binding.spo2View.detach();
        binding.prView.detach();
        binding.piView.detach();
        binding.siqView.detach();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getWidth() > 500) {
            binding.spo2View.setFontSize(100, 70);
            binding.prView.setFontSize(100, 70);
            binding.piView.setFontSize(100, 70);

            binding.spo2View.setIntegerTop(80);
            binding.prView.setIntegerTop(80);
            binding.piView.setIntegerTop(80);
        } else {
            binding.spo2View.setFontSize(70, 40);
            binding.prView.setFontSize(70, 40);
            binding.piView.setFontSize(70, 40);

            binding.spo2View.setIntegerTop(110);
            binding.prView.setIntegerTop(110);
            binding.piView.setIntegerTop(110);
        }
    }

    public void setDisable(boolean status) {
        binding.spo2View.setDisable(status);
        binding.prView.setDisable(status);
        binding.piView.setDisable(status);
    }
}