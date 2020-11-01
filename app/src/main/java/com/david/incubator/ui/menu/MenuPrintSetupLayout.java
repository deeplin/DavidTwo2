package com.david.incubator.ui.menu;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.EcgChannelEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.ViewUtil;
import com.david.incubator.print.PrintControl;
import com.david.incubator.serial.print.PrintCommandControl;
import com.david.incubator.serial.print.command.PrintSpeedCommand;

import javax.inject.Inject;

/*12.5mm/s 点距 0.125 每秒打印100点*/

public class MenuPrintSetupLayout extends BaseLayout {

    @Inject
    PrintCommandControl printCommandControl;
    @Inject
    ConfigRepository configRepository;
    @Inject
    SystemModel systemModel;
    @Inject
    PrintControl printControl;
    @Inject
    ModuleHardware moduleHardware;

    private final Button printButton;
    private boolean printing;

    private final EcgChannelEnum[] activeChannelEnums;
    private LifecycleOwner lifecycleOwner;

    public MenuPrintSetupLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.MENU_PRINT_SETUP);

        activeChannelEnums = new EcgChannelEnum[10];

        super.initPopup(4);

        super.setRowId(0, 0);
        super.setRowId(1, 1);
        super.setRowId(2, 2);
        super.setRowId(3, 3);

        super.setConfig(0, ConfigEnum.Wave0);
        super.setConfig(1, ConfigEnum.Wave1);
        super.setConfig(2, ConfigEnum.Wave2);
        super.setConfig(3, ConfigEnum.PrintSpeed);

        super.setPopup(0, activeChannelEnums, new ConfigEnum[]{ConfigEnum.Wave1, ConfigEnum.Wave2}, true, this::waveCallback);
        super.setPopup(1, 0, activeChannelEnums, new ConfigEnum[]{ConfigEnum.Wave0, ConfigEnum.Wave2}, true, this::waveCallback);
        super.setPopup(2, 0, activeChannelEnums, new ConfigEnum[]{ConfigEnum.Wave0, ConfigEnum.Wave1}, true, this::waveCallback);
        super.setPopup(3, ListUtil.printSpeedList.toArray(), null, true, this::setPrintSpeed);

        printButton = ViewUtil.buildButton(getContext());
        addInnerButton(4, printButton);

        printButton.setOnClickListener(v -> {
            keyButtonViews[0].getValue().setSelected(false);
            keyButtonViews[1].getValue().setSelected(false);
            keyButtonViews[2].getValue().setSelected(false);
            keyButtonViews[3].getValue().setSelected(false);
            keyButtonViews[0].getValue().setEnabled(printing);
            keyButtonViews[1].getValue().setEnabled(printing);
            keyButtonViews[2].getValue().setEnabled(printing);
            keyButtonViews[3].getValue().setEnabled(printing);
            if (printing) {
                printing = false;
                printButton.setText(ContextUtil.getString(R.string.start_print));

                printControl.detach();
                View view = printControl.getPrintWaveView();
                super.removeView(view);
            } else {
                printing = true;
                printButton.setText(ContextUtil.getString(R.string.stop_print));
                printControl.attach(lifecycleOwner);
                View view = printControl.getPrintWaveView();
                view.setVisibility(View.INVISIBLE);
                super.addView(view);
            }
        });
    }

    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach();
        this.lifecycleOwner = lifecycleOwner;
        systemModel.disableLock();

        EcgChannelEnum.init();
        if (moduleHardware.isActive(ModuleEnum.Ecg)) {
            for (int index = 0; index <= EcgChannelEnum.V.ordinal(); index++) {
                activeChannelEnums[index] = EcgChannelEnum.values()[index];
            }
        } else {
            for (int index = 0; index <= EcgChannelEnum.V.ordinal(); index++) {
                activeChannelEnums[index] = null;
            }
        }

        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            activeChannelEnums[EcgChannelEnum.Spo2.ordinal()] = EcgChannelEnum.values()[EcgChannelEnum.Spo2.ordinal()];
        } else {
            activeChannelEnums[EcgChannelEnum.Spo2.ordinal()] = null;
        }

//        if (moduleSoftware.co2.getValue()) {
//            activeChannelEnums[EcgChannelEnum.Co2.ordinal()] = EcgChannelEnum.values()[EcgChannelEnum.Co2.ordinal()];
//        } else {
//            activeChannelEnums[EcgChannelEnum.Co2.ordinal()] = null;
//        }
        activeChannelEnums[EcgChannelEnum.Close.ordinal()] = EcgChannelEnum.values()[EcgChannelEnum.Close.ordinal()];

        checkChannel(ConfigEnum.Wave0);
        checkChannel(ConfigEnum.Wave1);
        checkChannel(ConfigEnum.Wave2);

        super.setText(0, R.string.wave0, EcgChannelEnum.values());
        super.setText(1, R.string.wave1, EcgChannelEnum.values());
        super.setText(2, R.string.wave2, EcgChannelEnum.values());
        super.setText(3, R.string.print_speed, ListUtil.printSpeedList.toArray());

        printButton.setText(ContextUtil.getString(R.string.start_print));
        printing = false;

        waveCallback(0);
    }

    @Override
    public void detach() {
        super.detach();
        if (printing) {
            printing = false;
            printControl.detach();
            View view = printControl.getPrintWaveView();
            super.removeView(view);
        }
        systemModel.enableLock();
    }

    private void setPrintSpeed(Integer value) {
        PrintSpeedCommand printSpeedCommand = new PrintSpeedCommand(value.byteValue());
        printSpeedCommand.setCallback((aBoolean, baseCommand) -> {
            if (aBoolean) {
                configRepository.getConfig(ConfigEnum.PrintSpeed).post(value);
            }
        });
        printCommandControl.produce(printSpeedCommand);
    }

    private void waveCallback(Integer value) {
        if (configRepository.getConfig(ConfigEnum.Wave0).getValue().equals(EcgChannelEnum.Close.ordinal()) &&
                configRepository.getConfig(ConfigEnum.Wave1).getValue().equals(EcgChannelEnum.Close.ordinal()) &&
                configRepository.getConfig(ConfigEnum.Wave2).getValue().equals(EcgChannelEnum.Close.ordinal())) {
            printButton.setEnabled(false);
        } else {
            printButton.setEnabled(true);
        }
    }

    protected int acceptId() {
        return EcgChannelEnum.Close.ordinal();
    }

    private void checkChannel(ConfigEnum configEnum) {
        EcgChannelEnum ecgChannelEnum = EcgChannelEnum.values()[configRepository.getConfig(configEnum).getValue()];
        if (ecgChannelEnum.ordinal() <= EcgChannelEnum.V.ordinal()) {
            if (!moduleHardware.isActive(ModuleEnum.Ecg))
                configRepository.getConfig(configEnum).set(EcgChannelEnum.Close.ordinal());
        } else if (ecgChannelEnum.ordinal() == EcgChannelEnum.Spo2.ordinal()) {
            if (!moduleHardware.isActive(ModuleEnum.Spo2))
                configRepository.getConfig(configEnum).set(EcgChannelEnum.Close.ordinal());
        } else if (ecgChannelEnum.ordinal() == EcgChannelEnum.Co2.ordinal()) {
            if (!moduleHardware.isActive(ModuleEnum.Co2))
                configRepository.getConfig(configEnum).set(EcgChannelEnum.Close.ordinal());
        }
    }
}