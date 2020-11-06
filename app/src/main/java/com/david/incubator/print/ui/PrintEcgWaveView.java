package com.david.incubator.print.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.EcgChannelEnum;
import com.david.core.model.EcgModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.LazyLiveData;
import com.david.core.util.ListUtil;
import com.david.databinding.ViewPrintEcgWaveBinding;
import com.david.incubator.print.IPrintView;

import javax.inject.Inject;

public class PrintEcgWaveView extends BindingBasicLayout<ViewPrintEcgWaveBinding> implements IPrintView {

    @Inject
    ConfigRepository configRepository;
    @Inject
    EcgModel ecgModel;

    public final LazyLiveData<String> channel = new LazyLiveData<>();
    public final LazyLiveData<String> gainString = new LazyLiveData<>();
    public final LazyLiveData<String> info = new LazyLiveData<>();
    public final LazyLiveData<String> ecgSpeed = new LazyLiveData<>();
    public final LazyLiveData<String> voltage = new LazyLiveData<>();

    private final Observer<Boolean> leadOffObserver;

    public PrintEcgWaveView(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        binding.setViewModel(this);

        leadOffObserver = aBoolean -> {
            binding.printEcgCurveView.setLeadOff(aBoolean);
            if (aBoolean) {
                binding.tvLeadOff.setVisibility(View.VISIBLE);
            } else {
                binding.tvLeadOff.setVisibility(View.GONE);
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_print_ecg_wave;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.printEcgCurveView.attach();
        binding.standardImage.setVisibility(View.VISIBLE);
        ecgModel.leadOff.observeForever(leadOffObserver);
        setGain(configRepository.getConfig(ConfigEnum.EcgGain).getValue());

        ecgSpeed.set(ListUtil.printSpeedList.get(configRepository.getConfig(ConfigEnum.PrintSpeed).getValue()));
        gainString.set(ListUtil.ecgGainList.get(configRepository.getConfig(ConfigEnum.EcgGain).getValue()));

        switch (configRepository.getConfig(ConfigEnum.EcgFilterMode).getValue()) {
            case (0):
                info.set(ContextUtil.getString(R.string.monitor));
                break;
            case (1):
                info.set(ContextUtil.getString(R.string.operation));
                break;
            default:
                info.set(ContextUtil.getString(R.string.diagnostic));
                break;
        }
    }

    @Override
    public void detach() {
        super.detach();
        ecgModel.leadOff.removeObserver(leadOffObserver);
        binding.printEcgCurveView.detach();
    }

    public void setChannel(EcgChannelEnum ecgChannelEnum) {
        channel.set(ecgChannelEnum.toString());
    }

    private void setGain(int gain) {
        switch (gain) {
            case (0): {
                ViewGroup.LayoutParams layoutParams = binding.standardImage.getLayoutParams();
                layoutParams.height = Constant.getPrintPixel(1.25f);
                binding.standardImage.setLayoutParams(layoutParams);
                voltage.set("1mv");
            }
            break;
            case (1): {
                ViewGroup.LayoutParams layoutParams = binding.standardImage.getLayoutParams();
                layoutParams.height = Constant.getPrintPixel(2.5f);
                binding.standardImage.setLayoutParams(layoutParams);
                voltage.set("1mv");
            }
            break;
            case (2): {
                ViewGroup.LayoutParams layoutParams = binding.standardImage.getLayoutParams();
                layoutParams.height = Constant.getPrintPixel(5f);
                binding.standardImage.setLayoutParams(layoutParams);
                voltage.set("1mv");
            }
            break;
            case (3): {
                ViewGroup.LayoutParams layoutParams = binding.standardImage.getLayoutParams();
                layoutParams.height = Constant.getPrintPixel(10f);
                binding.standardImage.setLayoutParams(layoutParams);
                voltage.set("1mv");
            }
            break;
            case (4): {
                ViewGroup.LayoutParams layoutParams = binding.standardImage.getLayoutParams();
                layoutParams.height = Constant.getPrintPixel(10f);
                binding.standardImage.setLayoutParams(layoutParams);
                voltage.set("0.5mv");
            }
            break;
            case (5): {
                ViewGroup.LayoutParams layoutParams = binding.standardImage.getLayoutParams();
                layoutParams.height = Constant.getPrintPixel(10f);
                binding.standardImage.setLayoutParams(layoutParams);
                voltage.set("0.25mv");
            }
            break;
        }
    }

    @Override
    public float drawInitialPrintPoint(Integer start, Integer length, int[] dataArray) {
        return binding.printEcgCurveView.drawInitialPrintPoints(start, length, dataArray);
    }

    @Override
    public float drawPrintPoint(Integer start, Integer length, int[] dataSeries, float previousY) {
        return binding.printEcgCurveView.drawPrintPoints(start, length, dataSeries, previousY);
    }

    @Override
    public float getPacketLength() {
        return binding.printEcgCurveView.getPacketLength();
    }

    @Override
    public void clearInfo() {
        binding.standardImage.setVisibility(View.GONE);
        channel.post(null);
        gainString.post(null);
        info.post(null);
        ecgSpeed.post(null);
        voltage.post(null);
    }
}