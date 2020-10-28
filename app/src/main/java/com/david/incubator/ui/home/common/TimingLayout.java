package com.david.incubator.ui.home.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.IntervalUtil;
import com.david.databinding.LayoutTimingBinding;
import com.david.incubator.ui.home.standard.top.TimingData;

import javax.inject.Inject;

public class TimingLayout extends BindingBasicLayout<LayoutTimingBinding> {

    @Inject
    IntervalUtil intervalUtil;
    @Inject
    TimingData timingData;

    public TimingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        binding.setViewModel(timingData);

        binding.startButton.setOnClickListener(v -> {
            binding.startButton.setVisibility(INVISIBLE);
            binding.timeString.setVisibility(VISIBLE);
            binding.timeString.setTextSize(timingData.isApgar() ? 60 : 36);
            binding.timingView.setVisibility(timingData.isApgar() ? View.INVISIBLE : View.VISIBLE);

            intervalUtil.addMilliSecond500Consumer(TimingLayout.class, this::count);
            timingData.init();
        });

        binding.getRoot().setOnClickListener(v -> {
            if (timingData.getCount() == 0) {
                timingData.setApgar(!timingData.isApgar());
                timingData.titleString.set(timingData.isApgar() ? "APGAR" : "CPR");
            } else {
                timingData.initCount();
                intervalUtil.removeMillisecond500Consumer(TimingLayout.class);
                binding.startButton.setVisibility(VISIBLE);
                binding.timeString.setVisibility(INVISIBLE);
                binding.timingView.setVisibility(INVISIBLE);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_timing;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        if (timingData.getCount() == 0) {
            binding.timeString.setVisibility(INVISIBLE);
            binding.timingView.setVisibility(INVISIBLE);
            binding.startButton.setVisibility(VISIBLE);
        } else {
            binding.startButton.setVisibility(INVISIBLE);
            binding.timeString.setVisibility(VISIBLE);
            binding.timeString.setTextSize(timingData.isApgar() ? 60 : 36);
            binding.timingView.setVisibility(timingData.isApgar() ? View.INVISIBLE : View.VISIBLE);
        }
        timingData.titleString.set(timingData.isApgar() ? "APGAR" : "CPR");
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void count(Long obj) {
        timingData.increaseCount();
        timingData.accept(binding.timingView);
    }
}