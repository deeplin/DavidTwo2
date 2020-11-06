package com.david.incubator.ui.setup;

import android.content.Context;
import android.widget.Button;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.Co2Model;
import com.david.core.model.SystemModel;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.ViewUtil;
import com.david.incubator.serial.co2.Co2CommandControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class SetupCo2Layout extends BaseLayout {

    @Inject
    ConfigRepository configRepository;
    @Inject
    Co2Model co2Model;
    @Inject
    Co2CommandControl co2CommandControl;
    @Inject
    SystemModel systemModel;

    private final List<String> modeList = new ArrayList<>();
    private final List<String> chokeDelayList = new ArrayList<>();

    private final KeyButtonView rulerView;
    private final KeyButtonView modeView;
    private final KeyButtonView chokeDelayView;
    private final Button calibrationButton;
    private final Button alarmSettingButton;

    private List<String> rangeList;

    public SetupCo2Layout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init();

        modeList.add(ContextUtil.getString(R.string.co2_measurement));
        modeList.add(ContextUtil.getString(R.string.co2_standby));

        rulerView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(0, rulerView);

        modeView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(1, modeView);

        chokeDelayView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(2, chokeDelayView);

        calibrationButton = ViewUtil.buildButton(getContext());
        addInnerButton(5, calibrationButton);

        alarmSettingButton = ViewUtil.buildButton(getContext());
        addInnerButton(6, alarmSettingButton);

        rulerView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < rangeList.size(); index++) {
                optionPopupView.setOption(index, rangeList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.Co2Range).getValue());
            optionPopupView.setCallback(this::rangeCallback);
            optionPopupView.show(this, rulerView.getId(), true);
        });

        modeView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < modeList.size(); index++) {
                optionPopupView.setOption(index, modeList.get(index));
            }

            optionPopupView.setSelectedId(co2Model.mode.getValue());
            optionPopupView.setCallback(this::modeCallback);
            optionPopupView.show(this, modeView.getId(), true);
        });

        chokeDelayView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < chokeDelayList.size(); index++) {
                optionPopupView.setOption(index, chokeDelayList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.Co2ChokeDelay).getValue());
            optionPopupView.setCallback(this::chokeDelayCallback);
            optionPopupView.show(this, rulerView.getId(), true);
        });

        calibrationButton.setOnClickListener(v -> {
            co2CommandControl.sendCalibration();
            calibrationButton.setSelected(true);
        });

        addKeyButtonWithLiveData(0, 3);
        setKeyButtonEnum(0, KeyButtonEnum.SETUP_CO2_O2_COMPENSATE, null, null);
        setOriginValue(0, configRepository.getConfig(ConfigEnum.Co2O2Compensate));

        addKeyButtonWithLiveData(1, 4);
        setKeyButtonEnum(1, KeyButtonEnum.SETUP_CO2_N20_COMPENSATE, null, null);
        setOriginValue(1, configRepository.getConfig(ConfigEnum.Co2N2oCompensate));

        alarmSettingButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Co2Alarm));
    }

    @Override
    public void attach() {
        super.attach();

        int co2Unit = configRepository.getConfig(ConfigEnum.Co2Unit).getValue();
        switch (co2Unit) {
            case (0):
                rangeList = ListUtil.co2mmHgRangeList;
                break;
            case (1):
                rangeList = ListUtil.co2kPaRangeList;
                break;
            case (2):
                rangeList = ListUtil.co2PercentageRangeList;
                break;
        }

        rulerView.setSelected(false);
        rulerView.setKeyId(R.string.ruler);
        rulerView.getValue().setText(rangeList.get(configRepository.getConfig(ConfigEnum.Co2Range).getValue()));

        modeView.setSelected(false);
        modeView.setKeyId(R.string.co2_mode);
        modeView.getValue().setText(modeList.get(co2Model.mode.getValue()));

        chokeDelayList.clear();
        chokeDelayList.add(ContextUtil.getString(R.string.off));
        for (int index = 20; index <= 60; index += 5) {
            chokeDelayList.add(String.format(Locale.US, "%d s", index));
        }

        chokeDelayView.setSelected(false);
        chokeDelayView.setKeyId(R.string.chock_delay);
        chokeDelayView.getValue().setText(chokeDelayList.get(configRepository.getConfig(ConfigEnum.Co2ChokeDelay).getValue()));

        calibrationButton.setSelected(false);
        calibrationButton.setText(ContextUtil.getString(R.string.co2_calibration));

        alarmSettingButton.setText(ContextUtil.getString(R.string.alarm_setup));
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void rangeCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.Co2Range).getValue())) {
            configRepository.getConfig(ConfigEnum.Co2Range).set(value);
            rulerView.getValue().setText(rangeList.get(value));
            rulerView.setSelected(true);
        }
    }

    private void modeCallback(Integer value) {
        if (!value.equals(co2Model.mode.getValue())) {
            co2Model.mode.set(value);
            modeView.getValue().setText(modeList.get(value));
            modeView.setSelected(true);
        }
    }

    private void chokeDelayCallback(Integer position) {
        if (!position.equals(configRepository.getConfig(ConfigEnum.Co2ChokeDelay).getValue())) {
            configRepository.getConfig(ConfigEnum.Co2ChokeDelay).set(position);
            chokeDelayView.getValue().setText(chokeDelayList.get(position));
            chokeDelayView.setSelected(true);
            if (position != 0) {
                co2CommandControl.sendApneCommand((byte) (15 + 5 * position), null);
            }
        }
    }
}