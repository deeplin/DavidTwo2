package com.david.incubator.ui.home.camera;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.databinding.LayoutCameraBinding;

public class CameraLayout extends BindingBasicLayout<LayoutCameraBinding> {

    public CameraLayout(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_camera;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.cameraView.attach(lifeCycleOwner);
        binding.incubatorLayout.attach(lifeCycleOwner);
        binding.monitorListView.attach(lifeCycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
        binding.monitorListView.detach();
        binding.incubatorLayout.detach();
        binding.cameraView.detach();
    }
}