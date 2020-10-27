package com.david.incubator.ui.home.standard.bottom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.buffer.BufferControl;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.Constant;
import com.david.databinding.ViewSiqBinding;

public class SiqView extends BindingBasicLayout<ViewSiqBinding> {

    private static final int SPO2_PPG_MAX = 10;
    private static final int HIDE_HEIGHT = 76;

    private BufferControl bufferControl;

    public SiqView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void set(BufferControl spo2BufferControl) {
        bufferControl = spo2BufferControl;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_siq;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        bufferControl.start(this::draw);
    }

    @Override
    public void detach() {
        super.detach();
        bufferControl.stop();
    }

    private int count = 0;

    public void draw(Integer data) {
        count++;
        if (count < 6) {
            return;
        } else {
            count = 0;
        }
        if (data > Constant.IS_SIQ) {
            data -= Constant.SIQ_STEP;
        }
        int height = (data / 1024 + SPO2_PPG_MAX) * HIDE_HEIGHT / 2 / SPO2_PPG_MAX;
        if (height < 0) {
            height = 0;
        } else if (height > HIDE_HEIGHT) {
            height = HIDE_HEIGHT;
        }
        final ViewGroup.LayoutParams layoutParams = binding.ivSetHide.getLayoutParams();
        layoutParams.height = height;
        binding.ivSetHide.post(() -> binding.ivSetHide.requestLayout());
    }
}