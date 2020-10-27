package com.david.incubator.ui.home.standard.bottom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.databinding.LayoutBottomRightBinding;

public class BottomRightLayout extends BindingBasicLayout<LayoutBottomRightBinding> {

    public BottomRightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_bottom_right;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.respSensorView.attach(lifeCycleOwner);
        binding.nibpView.attach(lifeCycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
        binding.nibpView.detach();
        binding.respSensorView.detach();
    }

    public void setDarkMode(boolean darkMode) {
        binding.respSensorView.setSensorDarkMode(darkMode);
        binding.nibpView.setSensorDarkMode(darkMode);
    }
}