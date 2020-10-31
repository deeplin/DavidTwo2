package com.david.incubator.ui.home.bodywave;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.EcgChannelEnum;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.Constant;
import com.david.core.util.LazyLiveData;
import com.david.databinding.ViewEcgWaveBinding;

public class EcgWaveView extends BindingBasicLayout<ViewEcgWaveBinding> {

    public final LazyLiveData<String> channel = new LazyLiveData<>();
    public final LazyLiveData<String> gainString = new LazyLiveData<>();
    public final LazyLiveData<String> info = new LazyLiveData<>();
    public final LazyLiveData<String> ecgSpeed = new LazyLiveData<>();
    public final LazyLiveData<String> voltage = new LazyLiveData<>();

    public EcgWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        binding.setViewModel(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_ecg_wave;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.ecgSurfaceView.attach();
    }

    @Override
    public void detach() {
        super.detach();
        binding.ecgSurfaceView.detach();
    }

    public void setEcgChannel(EcgChannelEnum ecgChannelEnum) {
        binding.ecgSurfaceView.setEcgChannel(ecgChannelEnum);
    }

    public void setLeadOff(boolean leadOff) {
        if (leadOff) {
            binding.tvLeadOff.setVisibility(View.VISIBLE);
        } else {
            binding.tvLeadOff.setVisibility(View.GONE);
        }
    }

    public void setGain(int gain) {
        switch (gain) {
            case (0): {
                ViewGroup.LayoutParams layoutParams = binding.ivStandard.getLayoutParams();
                layoutParams.height = Constant.getPixel(1.25f);
                binding.ivStandard.setLayoutParams(layoutParams);
                voltage.set("1mv");
            }
            break;
            case (1): {
                ViewGroup.LayoutParams layoutParams = binding.ivStandard.getLayoutParams();
                layoutParams.height = Constant.getPixel(2.5f);
                binding.ivStandard.setLayoutParams(layoutParams);
                voltage.set("1mv");
            }
            break;
            case (2): {
                ViewGroup.LayoutParams layoutParams = binding.ivStandard.getLayoutParams();
                layoutParams.height = Constant.getPixel(5f);
                binding.ivStandard.setLayoutParams(layoutParams);
                voltage.set("1mv");
            }
            break;
            case (3): {
                ViewGroup.LayoutParams layoutParams = binding.ivStandard.getLayoutParams();
                layoutParams.height = Constant.getPixel(10f);
                binding.ivStandard.setLayoutParams(layoutParams);
                voltage.set("1mv");
            }
            break;
            case (4): {
                ViewGroup.LayoutParams layoutParams = binding.ivStandard.getLayoutParams();
                layoutParams.height = Constant.getPixel(10f);
                binding.ivStandard.setLayoutParams(layoutParams);
                voltage.set("0.5mv");
            }
            break;
            case (5): {
                ViewGroup.LayoutParams layoutParams = binding.ivStandard.getLayoutParams();
                layoutParams.height = Constant.getPixel(10f);
                binding.ivStandard.setLayoutParams(layoutParams);
                voltage.set("0.25mv");
            }
            break;
        }
    }
}