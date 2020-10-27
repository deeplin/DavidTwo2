package com.david.incubator.ui.home.standard.middle;

import android.content.Context;
import android.util.AttributeSet;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.databinding.ViewAngleBinding;

public class AngleView extends BindingBasicLayout<ViewAngleBinding> {


    public AngleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_angle;
    }

    public void set(SensorModel sensorModel) {
        binding.setViewModel(sensorModel);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
    }
}