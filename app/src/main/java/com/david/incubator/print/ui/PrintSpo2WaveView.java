package com.david.incubator.print.ui;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.LazyLiveData;
import com.david.core.util.ListUtil;
import com.david.databinding.ViewPrintSpo2WaveBinding;
import com.david.incubator.print.IPrintView;

import javax.inject.Inject;

public class PrintSpo2WaveView extends BindingBasicLayout<ViewPrintSpo2WaveBinding> implements IPrintView {

    @Inject
    ConfigRepository configRepository;

    public final LazyLiveData<String> spo2Speed = new LazyLiveData<>();
    public final LazyLiveData<String> spo2Gain = new LazyLiveData<>();

    public PrintSpo2WaveView(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        binding.setViewModel(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_print_spo2_wave;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.titleView.setVisibility(VISIBLE);
        binding.printSpo2CurveView.attach();
        spo2Speed.set(ListUtil.printSpeedList.get(configRepository.getConfig(ConfigEnum.PrintSpeed).getValue()));
        spo2Gain.set(ListUtil.ecgGainList.get(configRepository.getConfig(ConfigEnum.Spo2Gain).getValue()));
    }

    @Override
    public void detach() {
        super.detach();
        binding.printSpo2CurveView.detach();
    }

    @Override
    public float drawInitialPrintPoint(Integer start, Integer length, int[] dataArray) {
        return binding.printSpo2CurveView.drawInitialPrintPoints(start, length, dataArray);
    }

    @Override
    public float drawPrintPoint(Integer start, Integer length, int[] dataSeries, float previousY) {
        return binding.printSpo2CurveView.drawPrintPoints(start, length, dataSeries, previousY);
    }

    @Override
    public float getPacketLength() {
        return binding.printSpo2CurveView.getPacketLength();
    }

    @Override
    public void clearInfo() {
        binding.titleView.setVisibility(GONE);
        spo2Gain.set(null);
        spo2Speed.set(null);
    }
}