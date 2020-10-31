package com.david.incubator.ui.home.samescreen;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.buffer.BufferRepository;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.EcgChannelEnum;
import com.david.core.model.EcgModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.databinding.LayoutSameScreenBinding;
import com.david.incubator.ui.home.bodywave.EcgWaveView;

import javax.inject.Inject;

public class SameScreenLayout extends BindingBasicLayout<LayoutSameScreenBinding> {

    @Inject
    ConfigRepository configRepository;
    @Inject
    BufferRepository bufferRepository;
    @Inject
    EcgModel ecgModel;
    @Inject
    SystemModel systemModel;

    private final Observer<Boolean> ecgLeadOffObserver;

    private final EcgWaveView[] ecgWaveViewArray;

    public SameScreenLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        ecgWaveViewArray = new EcgWaveView[7];
        ecgWaveViewArray[0] = binding.ecgView0;
        ecgWaveViewArray[1] = binding.ecgView1;
        ecgWaveViewArray[2] = binding.ecgView2;
        ecgWaveViewArray[3] = binding.ecgView3;
        ecgWaveViewArray[4] = binding.ecgView4;
        ecgWaveViewArray[5] = binding.ecgView5;
        ecgWaveViewArray[6] = binding.ecgView6;

        ecgLeadOffObserver = aBoolean -> {
            for (EcgWaveView ecgWaveView : ecgWaveViewArray) {
                ecgWaveView.setLeadOff(aBoolean);
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_same_screen;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.incubatorLayout.attach(lifeCycleOwner);
        binding.monitorListView.attach(lifeCycleOwner);

        int ecgGain = configRepository.getConfig(ConfigEnum.EcgGain).getValue();
        ecgModel.leadOff.observeForever(ecgLeadOffObserver);

        EcgChannelEnum ecgChannelEnum0 = EcgChannelEnum.values()[configRepository.getConfig(ConfigEnum.Ecg0).getValue()];
        EcgChannelEnum ecgChannelEnum1 = EcgChannelEnum.values()[configRepository.getConfig(ConfigEnum.Ecg1).getValue()];

        setEcg0(ecgChannelEnum0, ecgGain);
        setEcg(ecgChannelEnum1, binding.ecgView1, ecgGain);

        int viewId = 2;
        for (int index = 0; index < 7; index++) {
            if (index != ecgChannelEnum0.ordinal() && index != ecgChannelEnum1.ordinal()) {
                setEcg(EcgChannelEnum.values()[index], ecgWaveViewArray[viewId++], ecgGain);
            }
        }

        for (EcgWaveView ecgWaveView : ecgWaveViewArray) {
            ecgWaveView.attach(lifeCycleOwner);
        }

        if (systemModel.darkMode.getValue()) {
            binding.backgroundView.setBackgroundResource(R.drawable.background_panel_dark);
        } else {
            binding.backgroundView.setBackgroundResource(R.drawable.background_panel_white);
        }
    }

    @Override
    public void detach() {
        super.detach();
        for (EcgWaveView ecgWaveView : ecgWaveViewArray) {
            ecgWaveView.detach();
        }
        for (int index = 0; index <= EcgChannelEnum.V.ordinal(); index++) {
            bufferRepository.getEcgBuffer(index).stop();
        }

        ecgModel.leadOff.removeObserver(ecgLeadOffObserver);

        binding.monitorListView.detach();
        binding.incubatorLayout.detach();
    }

    private void setEcg0(EcgChannelEnum ecgChannelEnum, int gain) {
        binding.ecgView0.setEcgChannel(ecgChannelEnum);
        binding.ecgView0.channel.set(ecgChannelEnum.toString());
        binding.ecgView0.setGain(gain);
        binding.ecgView0.gainString.set(ListUtil.ecgGainList.get(gain));

        int filterMode = configRepository.getConfig(ConfigEnum.EcgFilterMode).getValue();
        switch (filterMode) {
            case (0):
                binding.ecgView0.info.set(ContextUtil.getString(R.string.monitor));
                break;
            case (1):
                binding.ecgView0.info.set(ContextUtil.getString(R.string.operation));
                break;
            default:
                binding.ecgView0.info.set(ContextUtil.getString(R.string.diagnostic));
                break;
        }

        int speed = configRepository.getConfig(ConfigEnum.EcgSpeed).getValue();
        binding.ecgView0.ecgSpeed.set(ListUtil.ecgSpeedList.get(speed));
    }

    private void setEcg(EcgChannelEnum ecgChannelEnum, EcgWaveView ecgWaveView, int gain) {
        ecgWaveView.setEcgChannel(ecgChannelEnum);
        ecgWaveView.channel.set(ecgChannelEnum.toString());
        ecgWaveView.setGain(gain);
    }
}