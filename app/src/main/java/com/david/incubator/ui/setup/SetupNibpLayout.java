package com.david.incubator.ui.setup;

import android.content.Context;
import android.widget.Button;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.UnitUtil;
import com.david.core.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class SetupNibpLayout extends BaseLayout {

    @Inject
    ConfigRepository configRepository;
    @Inject
    SystemModel systemModel;

    private final List<String> measureModeList = new ArrayList<>();
    private final List<String> initialPressureList = new ArrayList<>();

    private final KeyButtonView measureModeView;
    private final KeyButtonView intervalModeView;
    private final KeyButtonView initialPressureView;
    private final KeyButtonView statView;
    private final Button alarmSettingButton;

    public SetupNibpLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init();

        measureModeView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(0, measureModeView);

        intervalModeView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(1, intervalModeView);

        initialPressureView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(2, initialPressureView);

        statView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(3, statView);

        alarmSettingButton = ViewUtil.buildButton(getContext());
        addInnerButton(4, alarmSettingButton);

        measureModeView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < measureModeList.size(); index++) {
                optionPopupView.setOption(index, measureModeList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.NibpMeasureMode).getValue());
            optionPopupView.setCallback(this::measureModeCallback);
            optionPopupView.show(this, measureModeView.getId(), true);
        });

        intervalModeView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.nibpAutoIntervalString.size(); index++) {
                optionPopupView.setOption(index, ListUtil.nibpAutoIntervalString.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.NibpInterval).getValue());
            optionPopupView.setCallback(this::intervalCallback);
            optionPopupView.show(this, measureModeView.getId(), true);
        });

        initialPressureView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < initialPressureList.size(); index++) {
                optionPopupView.setOption(index, initialPressureList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.NibpInitialSleevePressure).getValue());
            optionPopupView.setCallback(this::initialPressureCallback);
            optionPopupView.show(this, measureModeView.getId(), true);
        });

        statView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.statusList.size(); index++) {
                optionPopupView.setOption(index, ContextUtil.getString(ListUtil.statusList.get(index)));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.NibpStat).getValue());
            optionPopupView.setCallback(this::statCallback);
            optionPopupView.show(this, statView.getId(), true);
        });

        alarmSettingButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.NibpAlarm));
    }

    @Override
    public void attach() {
        super.attach();

        measureModeList.clear();

        measureModeList.add(ContextUtil.getString(R.string.manual));
        measureModeList.add(ContextUtil.getString(R.string.auto));

        measureModeView.setSelected(false);
        measureModeView.setKeyId(R.string.measurement_mode);
        measureModeView.getValue().setText(measureModeList.get(configRepository.getConfig(ConfigEnum.NibpMeasureMode).getValue()));

        intervalModeView.setSelected(false);
        intervalModeView.setKeyId(R.string.cycle);
        intervalModeView.getValue().setText(ListUtil.nibpAutoIntervalString.get(configRepository.getConfig(ConfigEnum.NibpInterval).getValue()));

        initialPressureList.clear();
        int unit = configRepository.getConfig(ConfigEnum.NibpUnit).getValue();
        for (int index = 0; index < ListUtil.nibpInitialPressure.length; index++) {
            int data = ListUtil.nibpInitialPressure[index];
            if (unit == 0) {
                initialPressureList.add(String.format(Locale.US, "%d mmHg", data));
            } else {
                initialPressureList.add(String.format(Locale.US, "%s kPa", UnitUtil.mmHgToKPa(data)));
            }
        }
        initialPressureView.setSelected(false);
        initialPressureView.setKeyId(R.string.initial_sleeve_pressure);
        initialPressureView.getValue().setText(initialPressureList.get(configRepository.getConfig(ConfigEnum.NibpInitialSleevePressure).getValue()));

        statView.setSelected(false);
        statView.setKey("STAT");
        statView.getValue().setText(ContextUtil.getString(
                ListUtil.statusList.get(configRepository.getConfig(ConfigEnum.NibpStat).getValue())));

        alarmSettingButton.setText(ContextUtil.getString(R.string.alarm_setup));
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void measureModeCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.NibpMeasureMode).getValue())) {
            configRepository.getConfig(ConfigEnum.NibpMeasureMode).set(value);
            measureModeView.getValue().setText(measureModeList.get(value));
            measureModeView.setSelected(true);
        }
    }

    private void intervalCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.NibpInterval).getValue())) {
            configRepository.getConfig(ConfigEnum.NibpInterval).set(value);
            intervalModeView.getValue().setText(ListUtil.nibpAutoIntervalString.get(value));
            intervalModeView.setSelected(true);
        }
    }

    private void initialPressureCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.NibpInitialSleevePressure).getValue())) {
            configRepository.getConfig(ConfigEnum.NibpInitialSleevePressure).set(value);
            initialPressureView.getValue().setText(initialPressureList.get(value));
            initialPressureView.setSelected(true);
        }
    }

    private void statCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.NibpStat).getValue())) {
            configRepository.getConfig(ConfigEnum.NibpStat).set(value);
            statView.getValue().setText(ContextUtil.getString(ListUtil.statusList.get(value)));
            statView.setSelected(true);
        }
    }
}