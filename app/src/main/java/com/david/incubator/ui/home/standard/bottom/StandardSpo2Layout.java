package com.david.incubator.ui.home.standard.bottom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.buffer.BufferRepository;
import com.david.core.control.ConfigRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.ui.view.SensorRangeView;
import com.david.core.util.BitUtil;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycleOwner;
import com.david.databinding.LayoutStandardSpo2Binding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class StandardSpo2Layout extends BindingBasicLayout<LayoutStandardSpo2Binding> {

    protected final int SPO2_WIDTH = 500;
    protected final int SPO2_HEIGHT = 220;

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    BufferRepository bufferRepository;
    @Inject
    ConfigRepository configRepository;

    public StandardSpo2Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        initLayout();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_standard_spo2;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        for (int index = 0; index < binding.spo2TopLayout.getChildCount(); index++) {
            View view = binding.spo2TopLayout.getChildAt(index);
            if (view instanceof ILifeCycleOwner) {
                ((ILifeCycleOwner) view).attach(lifeCycleOwner);
            }
        }

        for (int index = 0; index < binding.spo2BottomLayout.getChildCount(); index++) {
            View view = binding.spo2BottomLayout.getChildAt(index);
            if (view instanceof ILifeCycleOwner) {
                ((ILifeCycleOwner) view).attach(lifeCycleOwner);
            }
        }
    }

    @Override
    public void detach() {
        super.detach();

        for (int index = 0; index < binding.spo2BottomLayout.getChildCount(); index++) {
            View view = binding.spo2BottomLayout.getChildAt(index);
            if (view instanceof ILifeCycleOwner) {
                ((ILifeCycleOwner) view).detach();
            }
        }

        for (int index = 0; index < binding.spo2TopLayout.getChildCount(); index++) {
            View view = binding.spo2TopLayout.getChildAt(index);
            if (view instanceof ILifeCycleOwner) {
                ((ILifeCycleOwner) view).detach();
            }
        }
    }

    protected void initLayout() {
        List<SensorModelEnum> activeModuleList = new ArrayList<>();
        int moduleSum = 3;
        int spo2ModuleConfig = configRepository.getConfig(ConfigEnum.Spo2ModuleConfig).getValue();
        for (int index = 0; index < 5; index++) {
            if (BitUtil.getBit(spo2ModuleConfig, index)) {
                moduleSum++;
                activeModuleList.add(SensorModelEnum.values()[SensorModelEnum.Sphb.ordinal() + index]);
            }
        }
        int height = SPO2_HEIGHT / 2;
        if (moduleSum <= 4) {
            int width = SPO2_WIDTH / moduleSum;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            addViews(layoutParams);
            if (moduleSum == 4) {
                SensorRangeView sensorRangeView = buildView(activeModuleList.get(0));
                binding.spo2TopLayout.addView(sensorRangeView, layoutParams);
            }
            binding.spo2BottomLayout.setVisibility(View.GONE);
        } else if (moduleSum <= 6) {
            int width = SPO2_WIDTH / 3;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            addViews(layoutParams);
            binding.spo2BottomLayout.setVisibility(View.VISIBLE);
            for (int index = 0; index < activeModuleList.size(); index++) {
                SensorRangeView otherView = buildView(activeModuleList.get(index));
                binding.spo2BottomLayout.addView(otherView, layoutParams);
            }
        } else {
            int width = SPO2_WIDTH / 4;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            addViews(layoutParams);
            SensorRangeView sensorRangeView = buildView(activeModuleList.get(0));
            binding.spo2TopLayout.addView(sensorRangeView, layoutParams);
            binding.spo2BottomLayout.setVisibility(View.VISIBLE);
            for (int index = 1; index < activeModuleList.size(); index++) {
                SensorRangeView otherView = buildView(activeModuleList.get(index));
                binding.spo2BottomLayout.addView(otherView, layoutParams);
            }
        }
    }

    protected SensorRangeView buildView(SensorModelEnum sensorModelEnum) {
        SensorModel sensorModel = sensorModelRepository.getSensorModel(sensorModelEnum);
        SensorRangeView sensorRangeView = new SensorRangeView(getContext(), null);
        sensorRangeView.set(sensorModel);
        sensorRangeView.setSpo2SmallLayout();
        sensorRangeView.setOnClickListener(view -> {
            if (systemModel.isFreeze()) {
                return;
            }
            systemModel.showSetupPage(SetupPageEnum.Spo2);
        });
        return sensorRangeView;
    }

    private void addViews(LinearLayout.LayoutParams layoutParams) {
        SiqView siqView = new SiqView(getContext(), null);
        siqView.set(bufferRepository.getSpo2Buffer());
        binding.spo2TopLayout.addView(siqView, layoutParams);

        SensorRangeView spo2View = buildView(SensorModelEnum.Spo2);
        binding.spo2TopLayout.addView(spo2View, layoutParams);

        SensorRangeView piView = buildView(SensorModelEnum.Pi);
        binding.spo2TopLayout.addView(piView, layoutParams);
    }

    public void setDisable(boolean status) {
        for (int index = 0; index < binding.spo2TopLayout.getChildCount(); index++) {
            View view = binding.spo2TopLayout.getChildAt(index);
            if (view instanceof SensorRangeView) {
                ((SensorRangeView) view).setDisable(status);

            }
        }
        for (int index = 0; index < binding.spo2BottomLayout.getChildCount(); index++) {
            View view = binding.spo2BottomLayout.getChildAt(index);
            if (view instanceof SensorRangeView) {
                ((SensorRangeView) view).setDisable(status);
            }
        }
    }
}