package com.david.incubator.ui.menu;

import android.content.Context;
import android.widget.Button;

import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class MenuParameterSetupLayout extends BaseLayout {

    @Inject
    IncubatorModel incubatorModel;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SystemModel systemModel;

    private final Button tempButton;
    private final Button humButton;
    private final Button oxygenButton;
    private final Button blueButton;
    private final Button matButton;
    private final Button ecgButton;
    private final Button spo2Button;
    private final Button nibpButton;
    private final Button co2Button;
    private final Button respButton;
    private final Button wakeButton;

    private final Observer<SystemEnum> systemModeObserver;

    public MenuParameterSetupLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.MENU_PARAMETER_SETUP);

        tempButton = ViewUtil.buildButton(getContext());
        humButton = ViewUtil.buildButton(getContext());
        oxygenButton = ViewUtil.buildButton(getContext());
        blueButton = ViewUtil.buildButton(getContext());
        matButton = ViewUtil.buildButton(getContext());
        ecgButton = ViewUtil.buildButton(getContext());
        spo2Button = ViewUtil.buildButton(getContext());
        nibpButton = ViewUtil.buildButton(getContext());
        co2Button = ViewUtil.buildButton(getContext());
        respButton = ViewUtil.buildButton(getContext());
        wakeButton = ViewUtil.buildButton(getContext());

        tempButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Temp));
        humButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Humidity));
        oxygenButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Oxygen));
        blueButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Blue));
        matButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Mat));
        ecgButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Ecg));
        spo2Button.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Spo2));
        nibpButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Nibp));
        co2Button.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Co2));
        respButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Resp));
        wakeButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Wake));

        systemModeObserver = systemEnum -> {
            removeViews();
            reload();
        };
    }

    @Override
    public void attach() {
        super.attach();
        incubatorModel.systemMode.observeForever(systemModeObserver);
    }

    @Override
    public void detach() {
        super.detach();
        incubatorModel.systemMode.removeObserver(systemModeObserver);
        removeViews();
    }

    private void removeViews() {
        removeView(tempButton);
        removeView(humButton);
        removeView(oxygenButton);
        removeView(blueButton);
        removeView(matButton);
        removeView(ecgButton);
        removeView(spo2Button);
        removeView(nibpButton);
        removeView(co2Button);
        removeView(respButton);
        removeView(wakeButton);
    }

    private void reload() {
        int rowId = 0;
        tempButton.setText(ContextUtil.getString(R.string.temp_setting));
        addInnerButton(rowId++, tempButton);
        if (incubatorModel.isCabin()) {
            if (moduleHardware.isActive(ModuleEnum.Hum)) {
                humButton.setText(ContextUtil.getString(R.string.hum_setting));
                addInnerButton(rowId++, humButton);
            }
            if (moduleHardware.isActive(ModuleEnum.Oxygen)) {
                oxygenButton.setText(ContextUtil.getString(R.string.oxygen_setting));
                addInnerButton(rowId++, oxygenButton);
            }
        }

        if (moduleHardware.isActive(ModuleEnum.Ecg)) {
            ecgButton.setText(ContextUtil.getString(R.string.ecg_setting));
            addInnerButton(rowId++, ecgButton);
        }
        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            spo2Button.setText(ContextUtil.getString(R.string.spo2_setting));
            addInnerButton(rowId++, spo2Button);
        }
        if (moduleHardware.isActive(ModuleEnum.Nibp)) {
            nibpButton.setText(ContextUtil.getString(R.string.nibp_setting));
            addInnerButton(rowId++, nibpButton);
        }
        if (moduleHardware.isActive(ModuleEnum.Co2)) {
            co2Button.setText(ContextUtil.getString(R.string.co2_setting));
            addInnerButton(rowId++, co2Button);
        }
        respButton.setText(ContextUtil.getString(R.string.resp_setting));
        addInnerButton(rowId++, respButton);
        wakeButton.setText(ContextUtil.getString(R.string.wake_setting));
        addInnerButton(rowId, wakeButton);
    }
}