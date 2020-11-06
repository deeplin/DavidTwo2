package com.david.incubator.ui.setup;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.enumeration.Spo2AverageTimeEnum;
import com.david.core.enumeration.Spo2SensEnum;
import com.david.core.model.Spo2Model;
import com.david.core.model.SystemModel;
import com.david.core.serial.spo2.Spo2CommandControl;
import com.david.core.serial.spo2.command.Spo2AvgTimeCommand;
import com.david.core.serial.spo2.command.Spo2FastSatCommand;
import com.david.core.serial.spo2.command.Spo2LFCommand;
import com.david.core.serial.spo2.command.Spo2PviAveragingCommand;
import com.david.core.serial.spo2.command.Spo2SenCommand;
import com.david.core.serial.spo2.command.Spo2SmartToneCommand;
import com.david.core.serial.spo2.command.Spo2SphbArterialCommand;
import com.david.core.serial.spo2.command.Spo2SphbAveragingCommand;
import com.david.core.serial.spo2.command.Spo2SphbPrecisionCommand;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.component.TextTwoButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.BitUtil;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class SetupSpo2Layout extends BaseLayout {

    private final KeyButtonView sensView;
    private final TextTwoButtonView fastSatView;
    private final TextTwoButtonView lineFrequencyView;
    private final TextTwoButtonView spo2SmartToneView;
    private final KeyButtonView spo2SpeedView;
    private final KeyButtonView spo2GainView;
    private final Button alarmSettingButton;
    private final Observer<Boolean> pviAverageObserver;
    private final Observer<Integer> sphbPrecisionObserver;
    private final Observer<Boolean> sphbArterialObserver;
    private final Observer<Integer> sphbAverageObserver;
    @Inject
    Spo2CommandControl spo2CommandControl;
    @Inject
    Spo2Model spo2Model;
    @Inject
    ConfigRepository configRepository;
    @Inject
    SystemModel systemModel;
    private TextTwoButtonView pviAverageView;
    private KeyButtonView sphbPrecisionView;
    private TextTwoButtonView sphbArterialView;
    private KeyButtonView sphbAverageView;

    public SetupSpo2Layout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init();

        //第一列 第零行
        sensView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(0, sensView);

        addKeyButtonWithLiveData(0, 1);
        setKeyButtonEnum(0, KeyButtonEnum.SETUP_SPO2_AVG_TIME, null, null);
        setCallback(0, this::setAverageTime);

        fastSatView = new TextTwoButtonView(getContext(), null);
        fastSatView.setId(View.generateViewId());
        addInnerView(2, fastSatView);

        lineFrequencyView = new TextTwoButtonView(getContext(), null);
        lineFrequencyView.setId(View.generateViewId());
        addInnerView(3, lineFrequencyView);

        sensView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (Spo2SensEnum spo2SensEnum : Spo2SensEnum.values()) {
                optionPopupView.setOption(spo2SensEnum.ordinal(), spo2SensEnum.name());
            }

            optionPopupView.setSelectedId(spo2Model.sensEnum.getValue().ordinal());
            optionPopupView.setCallback(this::setSens);
            optionPopupView.show(this, sensView.getId(), true);
        });

        //第一列 第四行
        spo2SmartToneView = new TextTwoButtonView(getContext(), null);
        spo2SmartToneView.setId(View.generateViewId());
        addInnerView(4, spo2SmartToneView);

        spo2SpeedView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(5, spo2SpeedView);

        spo2GainView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(6, spo2GainView);

        spo2SpeedView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.spo2SpeedList.size(); index++) {
                optionPopupView.setOption(index, ListUtil.spo2SpeedList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.Spo2Speed).getValue());
            optionPopupView.setCallback(this::setSpeed);
            optionPopupView.show(this, spo2SpeedView.getId(), true);
        });

        spo2GainView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.ecgGainList.size(); index++) {
                optionPopupView.setOption(index, ListUtil.ecgGainList.get(index));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.Spo2Gain).getValue());
            optionPopupView.setCallback(this::setGain);
            optionPopupView.show(this, spo2GainView.getId(), false);
        });

        int rowId = 7;
        int spo2ModuleConfig = configRepository.getConfig(ConfigEnum.Spo2ModuleConfig).getValue();
        if (BitUtil.getBit(spo2ModuleConfig, SensorModelEnum.Pvi.ordinal() - SensorModelEnum.Sphb.ordinal())) {
            pviAverageView = new TextTwoButtonView(getContext(), null);
            pviAverageView.setId(View.generateViewId());
            addInnerView(rowId++, pviAverageView);
        }
        if (BitUtil.getBit(spo2ModuleConfig, SensorModelEnum.Sphb.ordinal() - SensorModelEnum.Sphb.ordinal())) {
            sphbPrecisionView = ViewUtil.buildKeyButtonView(getContext());
            addInnerView(rowId++, sphbPrecisionView);

            sphbArterialView = new TextTwoButtonView(getContext(), null);
            sphbArterialView.setId(View.generateViewId());
            addInnerView(rowId++, sphbArterialView);

            sphbAverageView = ViewUtil.buildKeyButtonView(getContext());
            addInnerView(rowId++, sphbAverageView);

            sphbPrecisionView.getValue().setOnClickListener(v -> {
                optionPopupView.init();

                optionPopupView.setOption(0, ContextUtil.getString(R.string.precision_0));
                optionPopupView.setOption(1, ContextUtil.getString(R.string.precision_1));
                optionPopupView.setOption(2, ContextUtil.getString(R.string.precision_2));

                optionPopupView.setSelectedId(spo2Model.sphbPrecision.getValue());
                optionPopupView.setCallback(this::setSphbPrecision);
                optionPopupView.show(this, sphbPrecisionView.getId(), true);
                setClosePopup(pviAverageView.getTextView());
            });

            sphbAverageView.getValue().setOnClickListener(v -> {
                optionPopupView.init();

                optionPopupView.setOption(0, ContextUtil.getString(R.string.forever));
                optionPopupView.setOption(1, ContextUtil.getString(R.string.middle));
                optionPopupView.setOption(2, ContextUtil.getString(R.string.brief));

                optionPopupView.setSelectedId(spo2Model.sphbAverage.getValue());
                optionPopupView.setCallback(this::setSphbAverage);
                optionPopupView.show(this, sphbAverageView.getId(), true);

                setClosePopup(sphbPrecisionView.getKey());
                setClosePopup(sphbArterialView.getTextView());
                setClosePopup(sphbAverageView.getKey());
            });
        }

        alarmSettingButton = ViewUtil.buildButton(getContext());
        addInnerButton(rowId, alarmSettingButton);
        alarmSettingButton.setOnClickListener(v -> systemModel.showSetupPage(SetupPageEnum.Spo2Alarm));

        setClosePopup(this);
        setClosePopup(sensView.getKey());
        setClosePopup(fastSatView.getTextView());
        setClosePopup(lineFrequencyView.getTextView());
        setClosePopup(spo2SmartToneView.getTextView());
        setClosePopup(spo2SpeedView.getKey());
        setClosePopup(spo2GainView.getKey());

        sensView.setKey("SENS");
        fastSatView.setKey("FastSAT");
        lineFrequencyView.setKey("Line Freq.");

        pviAverageObserver = aBoolean -> pviAverageView.select(aBoolean ? 1 : 0);
        sphbPrecisionObserver = integer -> sphbPrecisionView.getValue().setText(getSphbPrecisionString(integer));
        sphbArterialObserver = aBoolean -> sphbArterialView.select(aBoolean ? 1 : 0);
        sphbAverageObserver = integer -> sphbAverageView.getValue().setText(getSphbAverageString(integer));
    }

    public static String convertAverageTime(Integer enumId) {
        Spo2AverageTimeEnum spo2AverageTimeEnum = Spo2AverageTimeEnum.values()[enumId];
        return spo2AverageTimeEnum.toString();
    }

    @Override
    public void attach() {
        super.attach();
        sensView.getValue().setSelected(false);
        spo2SpeedView.getValue().setSelected(false);
        spo2GainView.getValue().setSelected(false);

        sensView.getValue().setText(spo2Model.sensEnum.getValue().name());
        setOriginValue(0, spo2Model.averageTimeEnum.getValue().ordinal());

        fastSatView.setValue(ContextUtil.getString(ListUtil.statusList.get(0)), ContextUtil.getString(ListUtil.statusList.get(1)));
        fastSatView.setCallback(this::setFastSat);
        fastSatView.select(spo2Model.fastsatValue.getValue() ? 1 : 0);

        lineFrequencyView.setValue("50", "60");
        lineFrequencyView.setCallback(this::setLineFrequency);
        lineFrequencyView.select(spo2Model.lfValue.getValue() ? 1 : 0);

        spo2SmartToneView.setKeyId(R.string.smart_tone);
        spo2SmartToneView.setValue(ContextUtil.getString(ListUtil.statusList.get(0)), ContextUtil.getString(ListUtil.statusList.get(1)));
        spo2SmartToneView.setCallback(this::setSmartTone);
        spo2SmartToneView.select(spo2Model.smartToneValue.getValue() ? 1 : 0);

        spo2SpeedView.setKeyId(R.string.wave_speed);
        spo2SpeedView.getValue().setText(ListUtil.spo2SpeedList.get(configRepository.getConfig(ConfigEnum.Spo2Speed).getValue()));

        spo2GainView.setKeyId(R.string.gain);
        spo2GainView.getValue().setText(ListUtil.ecgGainList.get(configRepository.getConfig(ConfigEnum.Spo2Gain).getValue()));

        int spo2ModuleConfig = configRepository.getConfig(ConfigEnum.Spo2ModuleConfig).getValue();
        if (BitUtil.getBit(spo2ModuleConfig, SensorModelEnum.Pvi.ordinal() - SensorModelEnum.Sphb.ordinal())) {
            pviAverageView.setKeyId(R.string.pvi_average);
            pviAverageView.setValue(ContextUtil.getString(R.string.normal), ContextUtil.getString(R.string.fast));
            pviAverageView.setCallback(this::setPviAverage);
            spo2Model.pviAverage.observeForever(pviAverageObserver);
            pviAverageView.setVisibility(View.VISIBLE);
        }

        if (BitUtil.getBit(spo2ModuleConfig, SensorModelEnum.Sphb.ordinal() - SensorModelEnum.Sphb.ordinal())) {
            sphbPrecisionView.setKeyId(R.string.sphb_precision);
            sphbPrecisionView.setVisibility(View.VISIBLE);
            spo2Model.sphbPrecision.observeForever(sphbPrecisionObserver);

            sphbArterialView.setKeyId(R.string.sphb_avm);
            sphbArterialView.setValue(ContextUtil.getString(R.string.arterial), ContextUtil.getString(R.string.venous));
            sphbArterialView.setCallback(this::setSphbArterial);
            spo2Model.sphbArterial.observeForever(sphbArterialObserver);
            sphbArterialView.setVisibility(View.VISIBLE);

            sphbAverageView.setKeyId(R.string.sphb_average);
            spo2Model.sphbAverage.observeForever(sphbAverageObserver);
            sphbAverageView.setVisibility(View.VISIBLE);
        }

        alarmSettingButton.setText(ContextUtil.getString(R.string.alarm_setup));
    }

    @Override
    public void detach() {
        super.detach();

        int spo2ModuleConfig = configRepository.getConfig(ConfigEnum.Spo2ModuleConfig).getValue();
        if (BitUtil.getBit(spo2ModuleConfig, SensorModelEnum.Pvi.ordinal() - SensorModelEnum.Sphb.ordinal())) {
            spo2Model.pviAverage.removeObserver(pviAverageObserver);
        }

        if (BitUtil.getBit(spo2ModuleConfig, SensorModelEnum.Sphb.ordinal() - SensorModelEnum.Sphb.ordinal())) {
            spo2Model.sphbPrecision.removeObserver(sphbPrecisionObserver);
            spo2Model.sphbArterial.removeObserver(sphbArterialObserver);
            spo2Model.sphbAverage.removeObserver(sphbAverageObserver);
        }
    }

    @Override
    protected void closePopup() {
        super.closePopup();
    }

    private void setClosePopup(View view) {
        view.setOnClickListener(v -> {
            closePopup();
        });
    }

    private void setSens(int optionId) {
        Spo2SensEnum spo2SensEnum = Spo2SensEnum.values()[optionId];
        spo2Model.sensEnum.set(spo2SensEnum);
        sensView.setValue(spo2SensEnum.name());
        sensView.setSelected(true);
        Spo2SenCommand command = new Spo2SenCommand();
        command.set((byte) (2 - optionId));
        spo2CommandControl.produce(command);
    }

    private void setAverageTime(Integer value) {
        Spo2AvgTimeCommand spo2AvgTimeCommand = new Spo2AvgTimeCommand();
        spo2AvgTimeCommand.set(value.byteValue());
        spo2CommandControl.produce(spo2AvgTimeCommand);
    }

    private void setFastSat(Integer optionId) {
        fastSatView.select(optionId);
        Spo2FastSatCommand spo2FastSatCommand = new Spo2FastSatCommand();
        spo2FastSatCommand.set(optionId.byteValue());
        spo2CommandControl.produce(spo2FastSatCommand);
    }

    private void setLineFrequency(Integer optionId) {
        lineFrequencyView.select(optionId);
        Spo2LFCommand spo2LFCommand = new Spo2LFCommand();
        spo2LFCommand.set(optionId.byteValue());
        spo2CommandControl.produce(spo2LFCommand);
    }

    private void setSmartTone(Integer optionId) {
        spo2SmartToneView.select(optionId);
        Spo2SmartToneCommand spo2SmartToneCommand = new Spo2SmartToneCommand();
        spo2SmartToneCommand.set((byte) ((1 + optionId) % 2));
        spo2CommandControl.produce(spo2SmartToneCommand);
    }

    private void setSpeed(Integer speedId) {
        spo2SpeedView.setValue(ListUtil.spo2SpeedList.get(speedId));
        spo2SpeedView.setSelected(true);
        configRepository.getConfig(ConfigEnum.Spo2Speed).set(speedId);
    }

    private void setGain(Integer gainId) {
        spo2GainView.setValue(ListUtil.ecgGainList.get(gainId));
        spo2GainView.setSelected(true);
        configRepository.getConfig(ConfigEnum.Spo2Gain).set(gainId);
    }

    private void setSphbPrecision(Integer value) {
        spo2Model.sphbPrecision.set(value);
        sphbPrecisionView.getValue().setText(getSphbPrecisionString(value));
        Spo2SphbPrecisionCommand spo2SphbPrecisionCommand = new Spo2SphbPrecisionCommand();
        spo2SphbPrecisionCommand.set(value.byteValue());
        spo2CommandControl.produce(spo2SphbPrecisionCommand);
    }

    private String getSphbPrecisionString(Integer value) {
        switch (value) {
            case (0):
                return ContextUtil.getString(R.string.precision_0);
            case (1):
                return ContextUtil.getString(R.string.precision_1);
            case (2):
                return ContextUtil.getString(R.string.precision_2);
            default:
                return null;
        }
    }

    private void setSphbArterial(Integer value) {
        sphbArterialView.select(value);
        Spo2SphbArterialCommand spo2SphbArterialCommand = new Spo2SphbArterialCommand();
        spo2SphbArterialCommand.set(value.byteValue());
        spo2CommandControl.produce(spo2SphbArterialCommand);
    }

    private void setSphbAverage(Integer value) {
        spo2Model.sphbAverage.set(value);
        sphbAverageView.getValue().setText(getSphbAverageString(value));
        Spo2SphbAveragingCommand spo2SphbAveragingCommand = new Spo2SphbAveragingCommand();
        spo2SphbAveragingCommand.set(value.byteValue());
        spo2CommandControl.produce(spo2SphbAveragingCommand);
    }

    private String getSphbAverageString(Integer value) {
        switch (value) {
            case (0):
                return ContextUtil.getString(R.string.forever);
            case (1):
                return ContextUtil.getString(R.string.middle);
            case (2):
                return ContextUtil.getString(R.string.brief);
            default:
                return null;
        }
    }

    public void setPviAverage(Integer value) {
        pviAverageView.select(value);
        Spo2PviAveragingCommand spo2PviAveragingCommand = new Spo2PviAveragingCommand();
        spo2PviAveragingCommand.set(value.byteValue());
        spo2CommandControl.produce(spo2PviAveragingCommand);
    }
}