package com.david.incubator.ui.home.bodywave;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.buffer.BufferRepository;
import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.database.DaoControl;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.EcgChannelEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.model.Co2Model;
import com.david.core.model.EcgModel;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.IntervalUtil;
import com.david.core.util.ListUtil;
import com.david.databinding.LayoutBodyWaveBinding;

import java.util.List;

import javax.inject.Inject;

public class BodyWaveLayout extends BindingBasicLayout<LayoutBodyWaveBinding> {

    @Inject
    IntervalUtil intervalUtil;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    DaoControl daoControl;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ConfigRepository configRepository;
    @Inject
    BufferRepository bufferRepository;
    @Inject
    EcgModel ecgModel;
    @Inject
    SystemModel systemModel;
    @Inject
    Co2Model co2Model;

    private final Observer<Boolean> ecgLeadOffObserver;
    private final Observer<Boolean> selectCo2RrObserver;

    public BodyWaveLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        ecgLeadOffObserver = aBoolean -> {
            binding.ecgView0.setLeadOff(aBoolean);
            binding.ecgView1.setLeadOff(aBoolean);
        };

        selectCo2RrObserver = aBoolean -> {
            if (aBoolean != null) {
                int rangeValue;
                List<String> rangeList;
                switch (configRepository.getConfig(ConfigEnum.Co2Unit).getValue()) {
                    case (0):
                        rangeList = ListUtil.co2mmHgRangeList;
                        rangeValue = 30;
                        break;
                    case (1):
                        rangeList = ListUtil.co2kPaRangeList;
                        rangeValue = 4;
                        break;
                    default:
                        rangeList = ListUtil.co2PercentageRangeList;
                        rangeValue = 4;
                        break;
                }

                int rangeOption;
                if (aBoolean) {
                    binding.co2SurfaceView.setDenominator(100);
                    binding.co2Title.setText(ContextUtil.getString(R.string.co2));

                    rangeOption = configRepository.getConfig(ConfigEnum.Co2Range).getValue();
                    switch (rangeOption) {
                        case (0):
                            break;
                        case (1):
                            rangeValue *= 2;
                            break;
                        case (2):
                            rangeValue *= 3;
                            break;
                        default:
                            rangeValue *= 5;
                            break;
                    }
                    binding.co2SurfaceView.setResp(false);
                    binding.co2SurfaceView.setRange(rangeValue);
                    String rangeString = rangeList.get(rangeOption);
                    int index = rangeString.indexOf("-");
                    binding.co2RangeUpperView.setText(rangeString.substring(index + 1));
                    binding.co2RangeLowerView.setText(rangeString.substring(0, index));
                    binding.co2RangeLowerView.setVisibility(View.VISIBLE);
                } else {
                    binding.co2SurfaceView.setResp(true);
                    binding.co2SurfaceView.setRange(30);
                    binding.co2SurfaceView.setDenominator(10);
                    binding.co2Title.setText(ContextUtil.getString(R.string.resp));
                    binding.co2RangeUpperView.setText("rpm");
                    binding.co2RangeLowerView.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_body_wave;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.incubatorLayout.attach(lifeCycleOwner);
        binding.monitorListView.attach(lifeCycleOwner);

        if (moduleHardware.isInstalled(ModuleEnum.Ecg)) {
            int ecgGain = configRepository.getConfig(ConfigEnum.EcgGain).getValue();
            ecgModel.leadOff.observeForever(ecgLeadOffObserver);
            //Ecg
            if (configRepository.getConfig(ConfigEnum.EcgLeadSetting).getValue() == 0) {
                binding.ecgView1.setVisibility(View.VISIBLE);
                setEcg0(ConfigEnum.Ecg0, ecgGain);
                setEcg1(ecgGain);
                binding.ecgView1.attach(lifeCycleOwner);
            } else {
                binding.ecgView1.setVisibility(View.GONE);
                setEcg0(ConfigEnum.Ecg2, ecgGain);
            }
            binding.ecgView0.attach(lifeCycleOwner);
        } else {
            binding.ecgView0.setVisibility(View.GONE);
            binding.ecgView1.setVisibility(View.GONE);
        }

        //Spo2
        if (moduleHardware.isInstalled(ModuleEnum.Spo2)) {
            binding.spo2SurfaceView.attach();
            binding.spo2SurfaceView.setVisibility(View.VISIBLE);
            int spo2Gain = configRepository.getConfig(ConfigEnum.Spo2Gain).getValue();
            binding.spo2Gain.setText(ListUtil.ecgGainList.get(spo2Gain));
            int spo2Speed = configRepository.getConfig(ConfigEnum.Spo2Speed).getValue();
            binding.spo2Speed.setText(ListUtil.spo2SpeedList.get(spo2Speed));
        } else {
            binding.spo2SurfaceView.setVisibility(View.GONE);
        }

        //Co2
        if (moduleHardware.isInstalled(ModuleEnum.Ecg) || moduleHardware.isInstalled(ModuleEnum.Co2)) {
            systemModel.selectCo2.observeForever(selectCo2RrObserver);
            int respGain = configRepository.getConfig(ConfigEnum.RespGain).getValue();
            binding.co2Gain.setText(ListUtil.ecgGainList.get(respGain));
            int respSpeed = configRepository.getConfig(ConfigEnum.RespSpeed).getValue();
            binding.co2Speed.setText(ListUtil.spo2SpeedList.get(respSpeed));
            binding.co2SurfaceView.attach();
            binding.co2SurfaceView.setVisibility(View.VISIBLE);
        } else {
            binding.co2SurfaceView.setVisibility(View.GONE);
        }

//        if (systemModel.darkMode.getValue()) {
//            binding.backgroundView.setBackgroundResource(R.drawable.sensor_background_dark);
//        } else {
//            binding.backgroundView.setBackgroundResource(R.drawable.sensor_background);
//        }
    }

    @Override
    public void detach() {
        super.detach();
        systemModel.selectCo2.removeObserver(selectCo2RrObserver);
        binding.co2SurfaceView.detach();

        binding.spo2SurfaceView.detach();

        binding.ecgView1.detach();
        binding.ecgView0.detach();
        for (int index = 0; index <= EcgChannelEnum.V.ordinal(); index++) {
            bufferRepository.getEcgBuffer(index).stop();
        }
        ecgModel.leadOff.removeObserver(ecgLeadOffObserver);

        binding.monitorListView.detach();
        binding.incubatorLayout.detach();
    }

    private void setEcg0(ConfigEnum configEnum, int gain) {
        EcgChannelEnum ecgChannelEnum = EcgChannelEnum.values()[configRepository.getConfig(configEnum).getValue()];
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

    private void setEcg1(int gain) {
        EcgChannelEnum ecgChannelEnum = EcgChannelEnum.values()[configRepository.getConfig(ConfigEnum.Ecg1).getValue()];
        binding.ecgView1.setEcgChannel(ecgChannelEnum);
        binding.ecgView1.channel.set(ecgChannelEnum.toString());
        binding.ecgView1.setGain(gain);
    }
}