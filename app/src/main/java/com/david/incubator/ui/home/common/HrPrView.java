package com.david.incubator.ui.home.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.EcgModel;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.AnimationUtil;
import com.david.core.util.ContextUtil;
import com.david.databinding.ViewHrPrBinding;

import javax.inject.Inject;

public class HrPrView extends BindingBasicLayout<ViewHrPrBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    EcgModel ecgModel;
    @Inject
    ModuleHardware moduleHardware;

    private final Observer<Boolean> ecgBeepCallback;
    private final Observer<Boolean> selectHrObserver;

    public HrPrView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        binding.hrPrView.setOnClickListener(v -> {
            if (systemModel.isFreeze()) {
                return;
            }
            if (systemModel.selectHr.getValue() != null) {
                if (systemModel.selectHr.getValue()) {
                    systemModel.showSetupPage(SetupPageEnum.Ecg);
                } else {
                    systemModel.showSetupPage(SetupPageEnum.Spo2);
                }
            }
        });

        AlphaAnimation alphaAnimation = AnimationUtil.getAlphaAnimation(250, 1, 0);

        ecgBeepCallback = aBoolean -> binding.ivSet.startAnimation(alphaAnimation);

        selectHrObserver = aBoolean -> {
            if (aBoolean == null) {
                displayNoneSelection();
            } else if (aBoolean) {
                binding.ivSet.setVisibility(View.VISIBLE);
                SensorModel ecgHrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgHr);
                binding.hrPrView.set(ecgHrModel);
                binding.rootView.setVisibility(View.VISIBLE);
                setDisable(false);
            } else {
                binding.ivSet.setVisibility(View.GONE);
                SensorModel prModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pr);
                binding.hrPrView.set(prModel);
                binding.rootView.setVisibility(View.VISIBLE);
                setDisable(false);
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_hr_pr;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.hrPrView.attach(lifecycleOwner);
        ecgModel.ecgBeep.observeForever(ecgBeepCallback);
        systemModel.selectHr.observeForever(selectHrObserver);
        if (systemModel.selectHr.getValue() == null) {
            displayNoneSelection();
        }
    }

    @Override
    public void detach() {
        super.detach();
        systemModel.selectHr.removeObserver(selectHrObserver);
        ecgModel.ecgBeep.removeObserver(ecgBeepCallback);
        binding.hrPrView.detach();
    }

    private void displayNoneSelection() {
        if (moduleHardware.isInActive(ModuleEnum.Spo2)) {
            SensorModel spo2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Spo2);
            binding.hrPrView.set(spo2Model);
            binding.rootView.setVisibility(View.VISIBLE);
            setDisable(true);
        } else if (moduleHardware.isActive(ModuleEnum.Ecg)) {
            SensorModel ecgHrModel = sensorModelRepository.getSensorModel(SensorModelEnum.EcgHr);
            binding.hrPrView.set(ecgHrModel);
            binding.rootView.setVisibility(View.VISIBLE);
            setDisable(true);
        } else {
            binding.hrPrView.set(null);
            binding.rootView.setVisibility(View.GONE);
        }
        binding.ivSet.setVisibility(View.GONE);
    }

    private void setDisable(boolean status) {
        binding.hrPrView.setDisable(status);
    }

    public void setTitleBackground(int backgroundResource) {
        binding.hrPrView.setTitleBackground(backgroundResource);
    }

    public void setSmallLayout() {
        binding.hrPrView.setSpo2SmallLayout();
        binding.hrPrView.setUniqueColor();
    }
}