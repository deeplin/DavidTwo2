package com.david.incubator.ui.home.spo2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.ui.view.SensorRangeView;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutSpo2ListBinding;

import java.util.List;

import javax.inject.Inject;

public class Spo2ListLayout extends BindingBasicLayout<LayoutSpo2ListBinding> {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    SystemModel systemModel;
    @Inject
    ConfigRepository configRepository;

    private final SensorRangeView[] sensorRangeSmallViews;

    private final List<Integer> activeSpo2Module;

    public Spo2ListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        binding.spo2View.setUniqueColor();
        binding.piView.setUniqueColor();

        sensorRangeSmallViews = new SensorRangeView[5];
        sensorRangeSmallViews[0] = binding.spo2View1;
        sensorRangeSmallViews[1] = binding.spo2View2;
        sensorRangeSmallViews[2] = binding.spo2View3;
        sensorRangeSmallViews[3] = binding.spo2View4;
        sensorRangeSmallViews[4] = binding.spo2View5;
        for (int index = 0; index < 5; index++) {
            sensorRangeSmallViews[index].setUniqueColor();
        }

        SensorModel sensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Spo2);
        binding.spo2View.set(sensorModel);
        SensorModel piModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pi);
        binding.piView.set(piModel);

        activeSpo2Module = configRepository.getActiveSpo2Module();

        SensorModel prModel = sensorModelRepository.getSensorModel(SensorModelEnum.Pr);
        switch (activeSpo2Module.size()) {
            case (0):
                binding.spo2View1.set(prModel);
                break;
            case (1):
                binding.spo2View1.set(prModel);
                SensorModelEnum sensorModelEnum0 = SensorModelEnum.values()
                        [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(0)];
                binding.spo2View2.set(sensorModelRepository.getSensorModel(sensorModelEnum0));
                binding.background2.setVisibility(View.VISIBLE);
                binding.spo2View2.setVisibility(View.VISIBLE);
                break;
            default:
                binding.spo2View0.set(prModel);
                binding.spo2View0.setVisibility(View.VISIBLE);
                binding.background2.setVisibility(View.VISIBLE);
                for (int index = 0; index < activeSpo2Module.size(); index++) {
                    SensorRangeView view = sensorRangeSmallViews[index];
                    SensorModelEnum sensorModelEnum = SensorModelEnum.values()
                            [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(index)];
                    view.set(sensorModelRepository.getSensorModel(sensorModelEnum));
                    view.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_spo2_list;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.spo2View.attach(lifecycleOwner);
        binding.piView.attach(lifecycleOwner);
        binding.spo2View1.attach(lifecycleOwner);

        switch (activeSpo2Module.size()) {
            case (0):
                break;
            case (1):
                binding.spo2View2.attach(lifecycleOwner);
                break;
            default:
                binding.spo2View0.attach(lifecycleOwner);
                for (int index = 0; index < activeSpo2Module.size(); index++) {
                    sensorRangeSmallViews[index].attach(lifecycleOwner);
                }
                break;
        }
        if (systemModel.darkMode.getValue()) {
            binding.background0.setBackgroundResource(R.drawable.background_panel_dark);
            binding.background1.setBackgroundResource(R.drawable.background_panel_dark);
            binding.background2.setBackgroundResource(R.drawable.background_panel_dark);
            binding.background3.setBackgroundResource(R.drawable.background_panel_dark);
        } else {
            binding.background0.setBackgroundResource(R.drawable.background_panel_white);
            binding.background1.setBackgroundResource(R.drawable.background_panel_white);
            binding.background2.setBackgroundResource(R.drawable.background_panel_white);
            binding.background3.setBackgroundResource(R.drawable.background_panel_white);
        }
    }

    @Override
    public void detach() {
        super.detach();
        for (SensorRangeView view : sensorRangeSmallViews) {
            view.detach();
        }
        binding.spo2View0.detach();
        binding.piView.detach();
        binding.spo2View.detach();
    }
}