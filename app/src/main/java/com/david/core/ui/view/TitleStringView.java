package com.david.core.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.databinding.ViewTitleStringBinding;

public class TitleStringView extends BindingBasicLayout<ViewTitleStringBinding> {

    public TitleStringView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_title_string;
    }

    public void set(SensorModel sensorModel) {
        binding.setSensorModel(sensorModel);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
    }

    public void setText(String text) {
        binding.integerPart.setText(text);
    }

    public void setUnit(String text) {
        binding.unit.setText(text);
    }

    public void setSmallLayout() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        constraintSet.setMargin(binding.integerPart.getId(), ConstraintSet.TOP, 100);
        constraintSet.applyTo(binding.rootView);

        binding.integerPart.setTextSize(40);
        binding.unit.setTextSize(24);
    }
}