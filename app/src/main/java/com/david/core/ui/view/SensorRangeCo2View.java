package com.david.core.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.databinding.ViewSensorRangeCo2Binding;

public class SensorRangeCo2View extends BindingBasicLayout<ViewSensorRangeCo2Binding> {

    private boolean uniqueColor;

    public SensorRangeCo2View(Context context) {
        super(context);
    }

    public SensorRangeCo2View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int titleBackgroundResource = R.drawable.background_panel_pink;

    @Override
    protected int getLayoutId() {
        return R.layout.view_sensor_range_co2;
    }

    public void setCo2(SensorModel sensorModel, SystemModel systemModel) {
        binding.setViewModel(sensorModel);
        if (sensorModel != null) {
            binding.upperLimit.setText(systemModel.respUnitFunction.apply(sensorModel.upperLimit.getValue()));
            binding.lowerLimit.setText(systemModel.respUnitFunction.apply(sensorModel.lowerLimit.getValue()));
        }
        if (uniqueColor) {
            binding.integerPart.setTextColor(sensorModel.getSensorModelEnum().getUniqueColor());
            binding.decimalPart.setTextColor(sensorModel.getSensorModelEnum().getUniqueColor());
        }
    }

    public void setResp(SensorModel sensorModel) {
        binding.setViewModel(sensorModel);
        if (sensorModel != null) {
            binding.upperLimit.setText(sensorModel.formatValue(sensorModel.upperLimit.getValue()));
            binding.lowerLimit.setText(sensorModel.formatValue(sensorModel.lowerLimit.getValue()));
        }
        if (uniqueColor) {
            binding.integerPart.setTextColor(sensorModel.getSensorModelEnum().getUniqueColor());
            binding.decimalPart.setTextColor(sensorModel.getSensorModelEnum().getUniqueColor());
        }
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
    }

    public void setDisable(boolean status) {
        if (status) {
            binding.title.setBackgroundResource(R.drawable.background_panel_dark);
            binding.unit.setVisibility(View.INVISIBLE);
            binding.integerPart.setVisibility(View.INVISIBLE);
            binding.decimalPart.setVisibility(View.INVISIBLE);
            binding.upperLimit.setVisibility(View.INVISIBLE);
            binding.lowerLimit.setVisibility(View.INVISIBLE);
        } else {
            binding.title.setBackgroundResource(titleBackgroundResource);
            binding.unit.setVisibility(View.VISIBLE);
            binding.integerPart.setVisibility(View.VISIBLE);
            binding.decimalPart.setVisibility(View.VISIBLE);
            binding.upperLimit.setVisibility(View.VISIBLE);
            binding.lowerLimit.setVisibility(View.VISIBLE);
        }
    }

    public void setUniqueColor() {
        uniqueColor = true;
    }

    public void setCo2SmallLayout() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        constraintSet.setMargin(binding.title.getId(), ConstraintSet.TOP, 8);
        constraintSet.setMargin(binding.title.getId(), ConstraintSet.START, 4);
        constraintSet.setMargin(binding.upperLimit.getId(), ConstraintSet.END, 8);
        constraintSet.applyTo(binding.rootView);

        binding.title.setTextSize(16);
        binding.unit.setTextSize(16);
        binding.integerPart.setTextSize(50);
        binding.decimalPart.setTextSize(34);
        binding.upperLimit.setTextSize(16);
        binding.lowerLimit.setTextSize(16);
    }

    public void setFiSmallLayout() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        constraintSet.setMargin(binding.title.getId(), ConstraintSet.TOP, 8);
        constraintSet.setMargin(binding.title.getId(), ConstraintSet.START, 4);
        constraintSet.setMargin(binding.upperLimit.getId(), ConstraintSet.END, 8);
        constraintSet.applyTo(binding.rootView);

        binding.title.setTextSize(16);
        binding.unit.setTextSize(16);
        binding.integerPart.setTextSize(46);
        binding.decimalPart.setTextSize(34);
        binding.upperLimit.setTextSize(16);
        binding.lowerLimit.setTextSize(16);
    }

    public void setCo2TinyLayout() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        constraintSet.setMargin(binding.title.getId(), ConstraintSet.TOP, 4);
        constraintSet.setMargin(binding.title.getId(), ConstraintSet.START, 4);
        constraintSet.setMargin(binding.upperLimit.getId(), ConstraintSet.END, 4);
        constraintSet.applyTo(binding.rootView);

        binding.title.setTextSize(16);
        binding.unit.setTextSize(16);
        binding.integerPart.setTextSize(40);
        binding.decimalPart.setTextSize(30);
        binding.upperLimit.setTextSize(16);
        binding.lowerLimit.setTextSize(16);
    }

    public void setFiTinyLayout() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        constraintSet.constrainHeight(R.id.title, 22);
        constraintSet.setMargin(binding.title.getId(), ConstraintSet.TOP, 4);
        constraintSet.setMargin(binding.title.getId(), ConstraintSet.START, 4);
        constraintSet.setMargin(binding.upperLimit.getId(), ConstraintSet.END, 4);
        constraintSet.applyTo(binding.rootView);

        binding.title.setTextSize(16);
        binding.unit.setTextSize(16);

        binding.integerPart.setTextSize(26);
        binding.decimalPart.setTextSize(20);
        binding.upperLimit.setTextSize(16);
        binding.lowerLimit.setTextSize(16);
    }

    public void displayEt(boolean status) {
        binding.etPre.setVisibility(status ? View.VISIBLE : View.GONE);
    }
}