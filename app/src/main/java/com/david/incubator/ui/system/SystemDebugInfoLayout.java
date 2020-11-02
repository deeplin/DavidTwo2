package com.david.incubator.ui.system;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.serial.BaseCommand;
import com.david.core.serial.incubator.IncubatorCommandControl;
import com.david.core.serial.incubator.command.other.DigitalCommand;
import com.david.core.serial.incubator.command.repeated.AnalogCommand;
import com.david.core.serial.incubator.command.repeated.StatusCommand;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.IntervalUtil;
import com.david.databinding.LayoutSystemDebugInfoBinding;

import java.util.Locale;

import javax.inject.Inject;

public class SystemDebugInfoLayout extends BindingBasicLayout<LayoutSystemDebugInfoBinding> {

    @Inject
    IncubatorCommandControl incubatorCommandControl;
    @Inject
    IntervalUtil intervalUtil;
    @Inject
    DigitalCommand digitalCommand;
    @Inject
    SystemModel systemModel;

    private static final String analogFormat = "S1A=%s;S1B=%s;S2=%s;S3=%s;A1=%s;A2=%s;A3=%s;F1=%s;H1=%s;O1=%s;O2=%s;O3=%s;SP=%s;PR=%s;PI=%s;VB=%s;VR=%s;VU=%s;T1=%s;T2=%s;T3=%s;SC=%s" +
            ";G1=%s;W1=%s";
    private static final String statusFormat = "Status=%s;Ctrl=%s;Time=%s;CTime=%s;Warm=%s;Inc=%s;Hum=%s;O2=%s";
    private final String digitalFormat = "Do=%s;Dc=%s;Ho=%s;Hc=%s;Mo=%s;Ms=%s;Ds=%s;Ps=%s;Ts=%s;WTs=%s;Hs=%s;Is1=%s;Is2=%s;WMs=%s;Fan=%s;Other=%s";

    public SystemDebugInfoLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        digitalCommand.setCallback(this::displayDigital);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_system_debug_info;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        systemModel.disableLock();
        binding.titleView.set(R.string.debug_info, LayoutPageEnum.SYSTEM_HOME, true);
        intervalUtil.addSecondConsumer(SystemDebugInfoLayout.class, this::display);
    }

    @Override
    public void detach() {
        super.detach();
        systemModel.enableLock();
        intervalUtil.removeSecondConsumer(SystemDebugInfoLayout.class);
    }

    private void display(long l) {
        incubatorCommandControl.produce(digitalCommand);
        binding.analogCommand.post(() -> {
            AnalogCommand analogCommand = incubatorCommandControl.analogCommand;
            binding.analogCommand.setText(String.format(Locale.US, analogFormat,
                    analogCommand.getS1A(), analogCommand.getS1B(), analogCommand.getS2(), analogCommand.getS3(),
                    analogCommand.getA1(), analogCommand.getA2(), analogCommand.getA3(), analogCommand.getF1(), analogCommand.getH1(),
                    analogCommand.getO1(), analogCommand.getO2(), analogCommand.getO3(), analogCommand.getSP(), analogCommand.getPR(),
                    analogCommand.getPI(), analogCommand.getVB(), analogCommand.getVR(), analogCommand.getVU(),
                    analogCommand.getT1(), analogCommand.getT2(), analogCommand.getT3(), analogCommand.getSC(), analogCommand.getG1(),
                    analogCommand.getW1()));

            StatusCommand statusCommand = incubatorCommandControl.statusCommand;
            binding.statusCommand.setText(String.format(Locale.US, statusFormat,
                    statusCommand.getMode(), statusCommand.getCtrl(), statusCommand.getTime(), statusCommand.getCTime(),
                    statusCommand.getWarm(), statusCommand.getInc(), statusCommand.getHum(), statusCommand.getO2()));
        });
    }

    private void displayDigital(Boolean status, BaseCommand command) {
        binding.digitalCommand.post(() -> binding.digitalCommand.setText(String.format(Locale.US, digitalFormat,
                digitalCommand.getDo(), digitalCommand.getDc(), digitalCommand.getHo(), digitalCommand.getHc(),
                digitalCommand.getMo(), digitalCommand.getMs(), digitalCommand.getDs(), digitalCommand.getPs(),
                digitalCommand.getTs(), digitalCommand.getWTs(), digitalCommand.getHs(), digitalCommand.getIs1(),
                digitalCommand.getIs2(), digitalCommand.getWMs(), digitalCommand.getFan(), digitalCommand.getOthers())));
    }
}