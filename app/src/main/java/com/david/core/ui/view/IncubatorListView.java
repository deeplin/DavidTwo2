package com.david.core.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.IDisableView;
import com.david.databinding.ViewIncubatorListBinding;

public class IncubatorListView extends BindingBasicLayout<ViewIncubatorListBinding> implements IDisableView {

    public IncubatorListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_incubator_list;
    }

    public void set(SensorModel sensorModel, boolean hideObjective) {
        binding.setViewModel(sensorModel);
        binding.integerPart.setTextColor(sensorModel.uniqueColor.getValue());
        binding.decimalPart.setTextColor(sensorModel.uniqueColor.getValue());
        if (hideObjective) {
            binding.set.setVisibility(View.GONE);
            binding.objective.setVisibility(View.GONE);
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

    public void setDisable(boolean status, int backgroundColorId) {
        if (status) {
            binding.title.setBackgroundResource(R.drawable.background_panel_dark);
            binding.set.setVisibility(View.INVISIBLE);
            binding.objective.setVisibility(View.INVISIBLE);
            binding.integerPart.setVisibility(View.INVISIBLE);
            binding.unit.setVisibility(View.INVISIBLE);
        } else {
            binding.title.setBackgroundResource(backgroundColorId);
            binding.set.setVisibility(View.VISIBLE);
            binding.objective.setVisibility(View.VISIBLE);
            binding.integerPart.setVisibility(View.VISIBLE);
            binding.unit.setVisibility(View.VISIBLE);
        }
    }

    public void setSmallMode() {
        binding.integerPart.setTextSize(40);
        binding.decimalPart.setTextSize(30);
    }
}