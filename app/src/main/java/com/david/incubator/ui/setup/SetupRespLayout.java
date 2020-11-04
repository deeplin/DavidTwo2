package com.david.incubator.ui.setup;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.EcgChannelEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.ViewUtil;
import com.david.incubator.serial.ecg.EcgCommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class SetupRespLayout extends BaseLayout {

    @Inject
    ConfigRepository configRepository;
    @Inject
    EcgCommandSender ecgCommandSender;
    @Inject
    SystemModel systemModel;
    @Inject
    ModuleHardware moduleHardware;

    private final List<String> rrSourceList = new ArrayList<>();
    private final List<String> leadSettingList = new ArrayList<>();
    private final List<String> chokeDelayList = new ArrayList<>();

    private final KeyButtonView rrSourceView;
    private final KeyButtonView leadSettingView;
    private final KeyButtonView speedView;
    private final KeyButtonView gainView;
    private final KeyButtonView chokeDelayView;
    private final Button alarmSettingButton;

    public SetupRespLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init();

        rrSourceView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(0, rrSourceView);

        leadSettingView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(1, leadSettingView);

        speedView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(2, speedView);

        gainView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(3, gainView);

        chokeDelayView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(4, chokeDelayView);

        alarmSettingButton = ViewUtil.buildButton(getContext());
        addInnerButton(5, alarmSettingButton);

        rrSourceView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < rrSourceList.size(); index++) {
                optionPopupView.setOption(index, rrSourceList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.RespSource).getValue());
            optionPopupView.setCallback(this::rrSourceCallback);
            optionPopupView.show(this, rrSourceView.getId(), true);
        });

        leadSettingView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < leadSettingList.size(); index++) {
                optionPopupView.setOption(index, leadSettingList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.RespLeadSetting).getValue());
            optionPopupView.setCallback(this::leadSettingCallback);
            optionPopupView.show(this, leadSettingView.getId(), true);
        });

        speedView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.spo2SpeedList.size(); index++) {
                optionPopupView.setOption(index, ListUtil.spo2SpeedList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.RespSpeed).getValue());
            optionPopupView.setCallback(this::speedCallback);
            optionPopupView.show(this, speedView.getId(), true);
        });

        gainView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.ecgGainList.size(); index++) {
                optionPopupView.setOption(index, ListUtil.ecgGainList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.RespGain).getValue());
            optionPopupView.setCallback(this::gainCallback);
            optionPopupView.show(this, gainView.getId(), true);
        });

        chokeDelayView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < chokeDelayList.size(); index++) {
                optionPopupView.setOption(index, chokeDelayList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.Co2ChokeDelay).getValue());
            optionPopupView.setCallback(this::chokeDelayCallback);
            optionPopupView.show(this, rrSourceView.getId(), true);
        });

        alarmSettingButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Resp));
    }

    @Override
    public void attach() {
        super.attach();

        rrSourceView.setKeyId(R.string.hr_source);
        if (moduleHardware.isActive(ModuleEnum.Co2) && moduleHardware.isActive(ModuleEnum.Ecg)) {
            rrSourceList.clear();
            rrSourceList.add(ContextUtil.getString(R.string.ecg_id));
            rrSourceList.add(ContextUtil.getString(R.string.co2));
            rrSourceList.add(ContextUtil.getString(R.string.auto));

            rrSourceView.getValue().setText(rrSourceList.get(configRepository.getConfig(ConfigEnum.RespSource).getValue()));
            rrSourceView.setSelected(false);
            rrSourceView.setEnabled(true);
            rrSourceView.setVisibility(View.VISIBLE);
        } else if (moduleHardware.isActive(ModuleEnum.Co2)) {
            rrSourceView.getValue().setText(ContextUtil.getString(R.string.co2));
            rrSourceView.setSelected(false);
            rrSourceView.setEnabled(true);
            rrSourceView.setVisibility(View.VISIBLE);
        } else if (moduleHardware.isActive(ModuleEnum.Ecg)) {
            rrSourceView.getValue().setText(ContextUtil.getString(R.string.ecg_id));
            rrSourceView.setSelected(false);
            rrSourceView.setEnabled(true);
            rrSourceView.setVisibility(View.VISIBLE);
        } else {
            rrSourceView.setVisibility(View.INVISIBLE);
        }

        setLeadVisibility(configRepository.getConfig(ConfigEnum.RespSource).getValue());

        leadSettingList.clear();
        leadSettingList.add(ContextUtil.getString(R.string.auto));
        leadSettingList.add(EcgChannelEnum.I.toString());
        leadSettingList.add(EcgChannelEnum.II.toString());

        leadSettingView.setSelected(false);
        leadSettingView.setKeyId(R.string.lead_choice);
        leadSettingView.getValue().setText(leadSettingList.get(configRepository.getConfig(ConfigEnum.RespLeadSetting).getValue()));

        speedView.setSelected(false);
        speedView.setKeyId(R.string.wave_speed);
        speedView.getValue().setText(ListUtil.ecgSpeedList.get(configRepository.getConfig(ConfigEnum.RespSpeed).getValue()));

        gainView.setSelected(false);
        gainView.setKeyId(R.string.gain);
        gainView.getValue().setText(ListUtil.ecgGainList.get(configRepository.getConfig(ConfigEnum.RespGain).getValue()));

        chokeDelayList.clear();
        chokeDelayList.add(ContextUtil.getString(R.string.off));
        for (int index = 20; index <= 60; index += 5) {
            chokeDelayList.add(String.format(Locale.US, "%d s", index));
        }

        chokeDelayView.setSelected(false);
        chokeDelayView.setKeyId(R.string.chock_delay);
        chokeDelayView.getValue().setText(chokeDelayList.get(configRepository.getConfig(ConfigEnum.Co2ChokeDelay).getValue()));

        alarmSettingButton.setText(ContextUtil.getString(R.string.alarm_setup));
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void rrSourceCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.RespSource).getValue())) {
            configRepository.getConfig(ConfigEnum.RespSource).set(value);
            rrSourceView.getValue().setText(rrSourceList.get(value));
            rrSourceView.setSelected(true);
            setLeadVisibility(value);
        }
    }

    private void setLeadVisibility(Integer value) {
        leadSettingView.setVisibility(value != 1 ? View.VISIBLE : View.INVISIBLE);
    }

    private void leadSettingCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.RespLeadSetting).getValue())) {
            configRepository.getConfig(ConfigEnum.RespLeadSetting).set(value);
            leadSettingView.getValue().setText(leadSettingList.get(value));
            leadSettingView.setSelected(true);
            if (value != 0) {
                ecgCommandSender.setRespSource(value);
            }
        }
    }

    private void speedCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.RespSpeed).getValue())) {
            configRepository.getConfig(ConfigEnum.RespSpeed).set(value);
            speedView.getValue().setText(ListUtil.spo2SpeedList.get(value));
            speedView.setSelected(true);
        }
    }

    private void gainCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.RespGain).getValue())) {
            configRepository.getConfig(ConfigEnum.RespGain).set(value);
            gainView.getValue().setText(ListUtil.ecgGainList.get(value));
            gainView.setSelected(true);
        }
    }

    private void chokeDelayCallback(Integer position) {
        if (!position.equals(configRepository.getConfig(ConfigEnum.Co2ChokeDelay).getValue())) {
            configRepository.getConfig(ConfigEnum.Co2ChokeDelay).set(position);
            chokeDelayView.getValue().setText(chokeDelayList.get(position));
            chokeDelayView.setSelected(true);
        }
    }
}