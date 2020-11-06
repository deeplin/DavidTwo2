package com.david.incubator.ui.setup;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.EcgChannelEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.ViewUtil;
import com.david.incubator.serial.ecg.EcgCommandSender;
import com.david.incubator.serial.ecg.command.TrapCommand;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SetupEcgLayout extends BaseLayout {

    @Inject
    ConfigRepository configRepository;
    @Inject
    EcgCommandSender ecgCommandSender;
    @Inject
    SystemModel systemModel;
    @Inject
    ModuleHardware moduleHardware;

    private final KeyButtonView hrPrSourceView;
    private final List<String> hrPrSourceList = new ArrayList<>();
    private final KeyButtonView leadSettingView;

    private final KeyButtonView ecg0View;
    private final KeyButtonView ecg1View;
    private final KeyButtonView ecg2View;
    private final List<EcgChannelEnum> sourceList = new ArrayList<>();

    private final KeyButtonView speedView;
    private final KeyButtonView gainView;

    private final KeyButtonView filterModeView;
    private final List<String> filterModeList = new ArrayList<>();
    private final KeyButtonView filterDiagnosticModeView;
    private final KeyButtonView filterMonitorModeView;
    private final List<String> trapDiagnosticFilterList = new ArrayList<>();
//    private final List<String> trapMonitorFilterList = new ArrayList<>();

    private final Observer<Integer> ecgTrapModeObserver;
    private int trapDiagnosticFilterId;
    private int trapMonitorFilterId;

    private final KeyButtonView paceCheckView;

    private final Button alarmSettingButton;

    public SetupEcgLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init();

        sourceList.add(EcgChannelEnum.I);
        sourceList.add(EcgChannelEnum.II);
        sourceList.add(EcgChannelEnum.III);
        sourceList.add(EcgChannelEnum.aVR);
        sourceList.add(EcgChannelEnum.aVL);
        sourceList.add(EcgChannelEnum.aVF);
        sourceList.add(EcgChannelEnum.V);

        filterModeList.add(ContextUtil.getString(R.string.monitor));
        filterModeList.add(ContextUtil.getString(R.string.operation));
        filterModeList.add(ContextUtil.getString(R.string.diagnostic));

        trapDiagnosticFilterList.add(ContextUtil.getString(R.string.no_trap));
        trapDiagnosticFilterList.add("50Hz");
        trapDiagnosticFilterList.add("60Hz");

//        trapMonitorFilterList.add("50Hz");
//        trapMonitorFilterList.add("60Hz");
//        trapMonitorFilterList.add("50Hz & 60Hz");

        hrPrSourceView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(0, hrPrSourceView);
        leadSettingView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(1, leadSettingView);

        ecg0View = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(2, ecg0View);
        ecg1View = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(3, ecg1View);
        ecg2View = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(2, ecg2View);

        speedView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(4, speedView);

        gainView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(5, gainView);

        filterModeView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(6, filterModeView);

        filterDiagnosticModeView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(7, filterDiagnosticModeView);

        filterMonitorModeView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(7, filterMonitorModeView);

        paceCheckView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(8, paceCheckView);

        hrPrSourceView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < hrPrSourceList.size(); index++) {
                optionPopupView.setOption(index, hrPrSourceList.get(index));
            }
            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.EcgHrPrSource).getValue());
            optionPopupView.setCallback(this::hrPrSourceCallback);
            optionPopupView.show(this, hrPrSourceView.getId(), true);
        });

        leadSettingView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            optionPopupView.setOption(0, ContextUtil.getString(R.string.lead_5));
            optionPopupView.setOption(1, ContextUtil.getString(R.string.lead_3));

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.EcgLeadSetting).getValue());
            optionPopupView.setCallback(this::leadSettingCallback);
            optionPopupView.show(this, leadSettingView.getId(), true);
        });

        ecg0View.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            fillButtonValue(configRepository.getConfig(ConfigEnum.Ecg1).getValue());

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.Ecg0).getValue());
            optionPopupView.setCallback(this::ecg0Callback);
            optionPopupView.show(this, ecg0View.getId(), true);
        });

        ecg1View.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            fillButtonValue(configRepository.getConfig(ConfigEnum.Ecg0).getValue());

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.Ecg1).getValue());
            optionPopupView.setCallback(this::ecg1Callback);
            optionPopupView.show(this, ecg0View.getId(), true);
        });

        ecg2View.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < 3; index++) {
                optionPopupView.setOption(index, sourceList.get(index).toString());
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.Ecg2).getValue());
            optionPopupView.setCallback(this::ecg2Callback);
            optionPopupView.show(this, ecg2View.getId(), true);
        });

        speedView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.ecgSpeedList.size(); index++) {
                optionPopupView.setOption(index, ListUtil.ecgSpeedList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.EcgSpeed).getValue());
            optionPopupView.setCallback(this::speedCallback);
            optionPopupView.show(this, speedView.getId(), true);
        });

        gainView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.ecgGainList.size(); index++) {
                optionPopupView.setOption(index, ListUtil.ecgGainList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.EcgGain).getValue());
            optionPopupView.setCallback(this::gainCallback);
            optionPopupView.show(this, gainView.getId(), false);
        });

        filterModeView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < filterModeList.size(); index++) {
                optionPopupView.setOption(index, filterModeList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.EcgFilterMode).getValue());
            optionPopupView.setCallback(this::filterModeCallback);
            optionPopupView.show(this, filterModeView.getId(), false);
        });

        filterDiagnosticModeView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < trapDiagnosticFilterList.size(); index++) {
                optionPopupView.setOption(index, trapDiagnosticFilterList.get(index));
            }

            optionPopupView.setSelectedId(trapDiagnosticFilterId);
            optionPopupView.setCallback(this::filterDiagnosticCallback);
            optionPopupView.show(this, filterDiagnosticModeView.getId(), false);
        });

        filterMonitorModeView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < trapDiagnosticFilterList.size(); index++) {
                optionPopupView.setOption(index, trapDiagnosticFilterList.get(index));
            }

            optionPopupView.setSelectedId(trapMonitorFilterId);
            optionPopupView.setCallback(this::filterMonitorCallback);
            optionPopupView.show(this, filterMonitorModeView.getId(), false);
        });

        ecgTrapModeObserver = integer -> {
            switch (integer) {
                case (TrapCommand.TRAP_DIAGNOSTIC_NONE):
                    trapDiagnosticFilterId = 0;
                    break;
                case (TrapCommand.TRAP_DIAGNOSTIC_50):
                    trapDiagnosticFilterId = 1;
                    break;
                case (TrapCommand.TRAP_DIAGNOSTIC_60):
                    trapDiagnosticFilterId = 2;
                    break;
                case (TrapCommand.TRAP_MONITOR_50):
                    trapMonitorFilterId = 0;
                    break;
                case (TrapCommand.TRAP_MONITOR_60):
                    trapMonitorFilterId = 1;
                    break;
                case (TrapCommand.TRAP_MONITOR_5060):
                    trapMonitorFilterId = 2;
                    break;
            }
        };

        paceCheckView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.statusList.size(); index++) {
                optionPopupView.setOption(index, ContextUtil.getString(ListUtil.statusList.get(index)));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.EcgPaceCheck).getValue());
            optionPopupView.setCallback(this::paceCheckCallback);
            optionPopupView.show(this, paceCheckView.getId(), true);
        });

        alarmSettingButton = ViewUtil.buildButton(getContext());
        addInnerButton(9, alarmSettingButton);
        alarmSettingButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.EcgAlarm));
    }

    @Override
    public void attach() {
        super.attach();

        hrPrSourceView.setKeyId(R.string.hr_source);
        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            hrPrSourceList.clear();
            hrPrSourceList.add(ContextUtil.getString(R.string.ecg_id));
            hrPrSourceList.add(ContextUtil.getString(SensorModelEnum.Spo2.getDisplayNameId()));
            hrPrSourceList.add(ContextUtil.getString(R.string.auto));
            hrPrSourceCallback(configRepository.getConfig(ConfigEnum.EcgHrPrSource).getValue(), false);
            hrPrSourceView.getValue().setEnabled(true);
        } else {
            hrPrSourceView.getValue().setText(ContextUtil.getString(R.string.ecg_id));
            hrPrSourceView.getValue().setEnabled(false);
        }

        leadSettingView.setKeyId(R.string.lead_setting);
        leadSettingCallback(configRepository.getConfig(ConfigEnum.EcgLeadSetting).getValue(), false);

        ecg0View.setSelected(false);
        ecg0View.setKey("ECG 1");
        ecg0View.getValue().setText(sourceList.get(configRepository.getConfig(ConfigEnum.Ecg0).getValue()).toString());
        ecg1View.setSelected(false);
        ecg1View.setKey("ECG 2");
        ecg1View.getValue().setText(sourceList.get(configRepository.getConfig(ConfigEnum.Ecg1).getValue()).toString());
        ecg2View.setSelected(false);
        ecg2View.setKey("ECG");
        ecg2View.getValue().setText(sourceList.get(configRepository.getConfig(ConfigEnum.Ecg2).getValue()).toString());

        speedView.setSelected(false);
        speedView.setKeyId(R.string.wave_speed);
        speedView.getValue().setText(ListUtil.ecgSpeedList.get(configRepository.getConfig(ConfigEnum.EcgSpeed).getValue()));

        gainView.setSelected(false);
        gainView.setKeyId(R.string.gain);
        gainView.getValue().setText(ListUtil.ecgGainList.get(configRepository.getConfig(ConfigEnum.EcgGain).getValue()));

        filterModeView.setSelected(false);
        filterModeView.setKeyId(R.string.filter_mode);
        int filterMode = configRepository.getConfig(ConfigEnum.EcgFilterMode).getValue();
        filterModeView.getValue().setText(filterModeList.get(filterMode));
        if (filterMode < 2) {
            filterDiagnosticModeView.setVisibility(View.INVISIBLE);
            filterMonitorModeView.setVisibility(View.VISIBLE);
        } else {
            filterDiagnosticModeView.setVisibility(View.VISIBLE);
            filterMonitorModeView.setVisibility(View.INVISIBLE);
        }

        configRepository.getConfig(ConfigEnum.EcgTrapMode).observeForever(ecgTrapModeObserver);
        filterDiagnosticModeView.setSelected(false);
        filterDiagnosticModeView.setKeyId(R.string.trap_filter);
        filterDiagnosticModeView.getValue().setText(trapDiagnosticFilterList.get(trapDiagnosticFilterId));

        filterMonitorModeView.setSelected(false);
        filterMonitorModeView.setKeyId(R.string.trap_filter);
        filterMonitorModeView.getValue().setText(trapDiagnosticFilterList.get(trapMonitorFilterId));

        paceCheckView.setSelected(false);
        paceCheckView.setKeyId(R.string.pace_check);
        paceCheckView.getValue().setText(ContextUtil.getString(ListUtil.statusList
                .get(configRepository.getConfig(ConfigEnum.EcgPaceCheck).getValue())));

        alarmSettingButton.setText(ContextUtil.getString(R.string.alarm_setup));
    }

    @Override
    public void detach() {
        super.detach();
        configRepository.getConfig(ConfigEnum.EcgTrapMode).removeObserver(ecgTrapModeObserver);
    }

    private void hrPrSourceCallback(Integer value, boolean status) {
        hrPrSourceView.getValue().setText(hrPrSourceList.get(value));
        hrPrSourceView.setSelected(status);
    }

    private void hrPrSourceCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.EcgHrPrSource).getValue())) {
            configRepository.getConfig(ConfigEnum.EcgHrPrSource).set(value);
            hrPrSourceCallback(value, true);
        }
    }

    private void leadSettingCallback(Integer value, boolean status) {
        if (value == 0) {
            leadSettingView.getValue().setText(ContextUtil.getString(R.string.lead_5));
            ecg0View.setVisibility(View.VISIBLE);
            ecg1View.setVisibility(View.VISIBLE);
            ecg2View.setVisibility(View.INVISIBLE);
        } else {
            leadSettingView.getValue().setText(ContextUtil.getString(R.string.lead_3));
            ecg0View.setVisibility(View.INVISIBLE);
            ecg1View.setVisibility(View.INVISIBLE);
            ecg2View.setVisibility(View.VISIBLE);
        }
        leadSettingView.setSelected(status);
    }

    private void leadSettingCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.EcgLeadSetting).getValue())) {
            configRepository.getConfig(ConfigEnum.EcgLeadSetting).set(value);
            leadSettingCallback(value, true);
            ecgCommandSender.setLead5(value == 0);
        }
    }

    private void fillButtonValue(int existingId0) {
        for (int index = 0; index < sourceList.size(); index++) {
            if (index != existingId0) {
                optionPopupView.setOption(index, sourceList.get(index).toString());
            }
        }
    }

    private void ecg0Callback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.Ecg0).getValue())) {
            configRepository.getConfig(ConfigEnum.Ecg0).set(value);
            ecg0View.getValue().setText(sourceList.get(value).toString());
            ecg0View.setSelected(true);
            ecgCommandSender.setComputeLead(EcgChannelEnum.values()[value]);
        }
    }

    private void ecg1Callback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.Ecg1).getValue())) {
            configRepository.getConfig(ConfigEnum.Ecg1).set(value);
            ecg1View.getValue().setText(sourceList.get(value).toString());
            ecg1View.setSelected(true);
            ecgCommandSender.setComputeLead(EcgChannelEnum.values()[value]);
        }
    }

    private void ecg2Callback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.Ecg2).getValue())) {
            configRepository.getConfig(ConfigEnum.Ecg2).set(value);
            ecg2View.getValue().setText(sourceList.get(value).toString());
            ecg2View.setSelected(true);
            ecgCommandSender.setComputeLead(EcgChannelEnum.values()[value]);
        }
    }

    private void speedCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.EcgSpeed).getValue())) {
            configRepository.getConfig(ConfigEnum.EcgSpeed).set(value);
            speedView.getValue().setText(ListUtil.ecgSpeedList.get(value));
            speedView.setSelected(true);
        }
    }

    private void gainCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.EcgGain).getValue())) {
            configRepository.getConfig(ConfigEnum.EcgGain).set(value);
            gainView.getValue().setText(ListUtil.ecgGainList.get(value));
            gainView.setSelected(true);
        }
    }

    private void filterDiagnosticCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.EcgTrapMode).getValue())) {
            configRepository.getConfig(ConfigEnum.EcgTrapMode).set(value);
            filterDiagnosticModeView.getValue().setText(trapDiagnosticFilterList.get(trapDiagnosticFilterId));
            filterDiagnosticModeView.setSelected(true);
        }
    }

    private void filterMonitorCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.EcgTrapMode).getValue())) {
            configRepository.getConfig(ConfigEnum.EcgTrapMode).set(value);
            filterMonitorModeView.getValue().setText(trapDiagnosticFilterList.get(trapMonitorFilterId));
            filterMonitorModeView.setSelected(true);
        }
    }

    private void paceCheckCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.EcgPaceCheck).getValue())) {
            configRepository.getConfig(ConfigEnum.EcgPaceCheck).set(value);
            paceCheckView.getValue().setText(ContextUtil.getString(ListUtil.statusList.get(value)));
            paceCheckView.setSelected(true);
            ecgCommandSender.setPace(value == 1);
        }
    }

    private void filterModeCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.EcgFilterMode).getValue())) {
            configRepository.getConfig(ConfigEnum.EcgFilterMode).set(value);
            filterModeView.getValue().setText(filterModeList.get(value));
            filterModeView.setSelected(true);
            switch (value) {
                case (0):
                case (1):
                    ecgCommandSender.setMonitor(true);
                    ecgCommandSender.setFilter(value);
                    ecgCommandSender.setTrap(TrapCommand.TRAP_MONITOR_5060);
                    break;
                case (2):
                    ecgCommandSender.setMonitor(false);
                    ecgCommandSender.setFilter(2);
                    ecgCommandSender.setTrap(TrapCommand.TRAP_DIAGNOSTIC_NONE);
                    break;
            }
        }
    }
}