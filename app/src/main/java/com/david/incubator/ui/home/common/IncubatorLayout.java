package com.david.incubator.ui.home.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.view.IncubatorListView;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycleOwner;
import com.david.core.util.ViewUtil;
import com.david.incubator.ui.home.standard.top.HomeSetView;

import javax.inject.Inject;

public class IncubatorLayout extends LinearLayout implements ILifeCycleOwner {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SystemModel systemModel;

    private final IncubatorListView skin1View;
    private final IncubatorListView skin2View;
    private final HomeSetView homeSetView;
    private final IncubatorListView airView;
    private final IncubatorListView humidityView;
    private final IncubatorListView oxygenView;
    private final TimingSmallLayout timingLayout;

    public IncubatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        setOrientation(LinearLayout.HORIZONTAL);
        skin1View = new IncubatorListView(getContext(), null);
        skin2View = new IncubatorListView(getContext(), null);
        homeSetView = new HomeSetView(getContext(), null);
        airView = new IncubatorListView(getContext(), null);
        humidityView = new IncubatorListView(getContext(), null);
        humidityView.setSmallMode();
        oxygenView = new IncubatorListView(getContext(), null);
        oxygenView.setSmallMode();
        timingLayout = new TimingSmallLayout(getContext(), null);

        addInnerView(skin1View, SensorModelEnum.Skin1, true);
        addInnerView(skin2View, SensorModelEnum.Skin2, true);
        LayoutParams layoutParams = new LayoutParams(130, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(homeSetView, layoutParams);

        addInnerView(humidityView, SensorModelEnum.Humidity, false);
        addInnerView(oxygenView, SensorModelEnum.Oxygen, false);

        addView(timingLayout, new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        skin1View.attach(lifecycleOwner);
        skin2View.attach(lifecycleOwner);
        homeSetView.attach(lifecycleOwner);

        if (incubatorModel.isCabin()) {
            airView.attach(lifecycleOwner);
            airView.setVisibility(View.VISIBLE);

            ViewUtil.setDisable(moduleHardware, ModuleEnum.Hum, humidityView, false);
            ViewUtil.setDisable(moduleHardware, ModuleEnum.Oxygen, oxygenView, false);

            timingLayout.setVisibility(View.GONE);
        } else {
            airView.setVisibility(View.GONE);
            humidityView.setVisibility(View.GONE);
            oxygenView.setVisibility(View.GONE);

            timingLayout.attach(lifecycleOwner);
            timingLayout.setVisibility(View.VISIBLE);
        }

        if (systemModel.darkMode.getValue()) {
            setBackgroundResource(R.drawable.background_panel_dark);
        } else {
            setBackgroundResource(R.drawable.background_panel_white);
        }
    }

    @Override
    public void detach() {
        timingLayout.detach();
        oxygenView.detach();
        humidityView.detach();
        airView.detach();
        homeSetView.detach();
        skin2View.detach();
        skin1View.detach();
    }

    private void addInnerView(IncubatorListView incubatorListView,
                              SensorModelEnum sensorModelEnum, boolean hideObjective) {
        SensorModel sensorModel = sensorModelRepository.getSensorModel(sensorModelEnum);
        incubatorListView.set(sensorModel, hideObjective);
        addView(incubatorListView, new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
    }
}