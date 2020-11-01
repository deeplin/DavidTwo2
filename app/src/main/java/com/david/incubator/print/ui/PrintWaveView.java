package com.david.incubator.print.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.config.ConfigEnum;
import com.david.core.config.ConfigRepository;
import com.david.core.control.BufferRepository;
import com.david.core.enumeration.EcgChannelEnum;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.ViewPrintWaveBinding;
import com.david.incubator.print.IPrintView;
import com.david.incubator.print.buffer.PrintBufferRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.internal.operators.flowable.FlowableSwitchIfEmpty;

public class PrintWaveView extends BindingBasicLayout<ViewPrintWaveBinding> {

    @Inject
    BufferRepository bufferRepository;
    @Inject
    ConfigRepository configRepository;
    @Inject
    PrintBufferRepository printBufferRepository;

    private final IPrintView[] printViewArray;
    private final FrameLayout[] frameLayoutArray;

    public PrintWaveView(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        printViewArray = new IPrintView[3];
        frameLayoutArray = new FrameLayout[3];
        frameLayoutArray[0] = binding.flLayout0;
        frameLayoutArray[1] = binding.flLayout1;
        frameLayoutArray[2] = binding.flLayout2;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_print_wave;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        setWaveView(0, ConfigEnum.Wave0);
        setWaveView(1, ConfigEnum.Wave1);
        setWaveView(2, ConfigEnum.Wave2);
    }

    @Override
    public void detach() {
        for (int index = 0; index < 3; index++) {
            if (printViewArray[index] != null) {
                frameLayoutArray[index].removeAllViews();
                printViewArray[index].detach();
                printViewArray[index] = null;
            }
        }
    }

    private void setWaveView(int index, ConfigEnum configEnum) {
        EcgChannelEnum ecgChannelEnum = EcgChannelEnum.values()[configRepository.getConfig(configEnum).getValue()];
        if (EcgChannelEnum.isEcg(ecgChannelEnum)) {
            bufferRepository.getEcgBuffer(ecgChannelEnum.ordinal()).start(printBufferRepository.getPrintBuffer(index)::produce);
            printViewArray[index] = getEcgPanel(ecgChannelEnum);
            printViewArray[index].attach(binding.getLifecycleOwner());
            frameLayoutArray[index].setVisibility(VISIBLE);
            frameLayoutArray[index].addView((View) printViewArray[index]);
        } else if (EcgChannelEnum.isSpo2(ecgChannelEnum)) {
            bufferRepository.getSpo2Buffer().start(printBufferRepository.getPrintBuffer(index)::produceData);
            printViewArray[index] = getSpo2Panel();
            printViewArray[index].attach(binding.getLifecycleOwner());
            frameLayoutArray[index].setVisibility(VISIBLE);
            frameLayoutArray[index].addView((View) printViewArray[index]);
        } else {
            printViewArray[index] = null;
            frameLayoutArray[index].setVisibility(View.GONE);
        }
    }

    private IPrintView getEcgPanel(EcgChannelEnum ecgChannelEnum) {
        PrintEcgWaveView ecgWaveView = new PrintEcgWaveView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ecgWaveView.setLayoutParams(params);
        ecgWaveView.setChannel(ecgChannelEnum);
        return ecgWaveView;
    }

    private IPrintView getSpo2Panel() {
        PrintSpo2WaveView spo2WaveView = new PrintSpo2WaveView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        spo2WaveView.setLayoutParams(params);
        return spo2WaveView;
    }

//            case Spo2:
//                final Spo2WaveView spo2WaveView = new Spo2WaveView(getContext(), null);
//                spo2WaveView.setLayoutParams(params);
//
//                spo2WaveView.init(true, false, speedId);
//                spo2WaveView.spo2Speed.set(ListUtil.printSpeedList.get(speedId));
//
//                return spo2WaveView;
////            case Co2:
////                final Co2WaveView co2WaveView = new Co2WaveView(getContext(), null);
////                co2WaveView.init(true, speedId);
////                return co2WaveView;
//            default:
//                return null;
//        }

    public void clearInfo() {
        for (int index = 0; index < 3; index++) {
            if (printViewArray[index] != null) {
                printViewArray[index].clearInfo();
            }
        }
    }

    public IPrintView getPrintView(int index) {
        return printViewArray[index];
    }

    public void setWidth(int width) {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width, 324);
        setLayoutParams(layoutParams);
    }
}