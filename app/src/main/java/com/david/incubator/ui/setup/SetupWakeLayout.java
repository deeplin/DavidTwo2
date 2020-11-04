package com.david.incubator.ui.setup;

import android.content.Context;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.model.WakeModel;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.ViewUtil;
import com.david.incubator.serial.wake.WakeCommandControl;

import javax.inject.Inject;

public class SetupWakeLayout extends BaseLayout {

    @Inject
    ConfigRepository configRepository;
    @Inject
    WakeCommandControl wakeCommandControl;
    @Inject
    WakeModel wakeModel;

    private final KeyButtonView selfTestView;
    private final KeyButtonView respView;
    private final KeyButtonView co2View;

    private int selfTestId;

    public SetupWakeLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init();

        selfTestView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(0, selfTestView);

        respView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(2, respView);

        co2View = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(3, co2View);

        selfTestView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.statusList.size(); index++) {
                optionPopupView.setOption(index, ContextUtil.getString(ListUtil.statusList.get(index)));
            }

            optionPopupView.setSelectedId(selfTestId);
            optionPopupView.setCallback(this::selfTestCallback);
            optionPopupView.show(this, selfTestView.getId(), true);
        });

        respView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.statusList.size(); index++) {
                optionPopupView.setOption(index, ContextUtil.getString(ListUtil.statusList.get(index)));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.WakeRespStatus).getValue());
            optionPopupView.setCallback(this::respCallback);
            optionPopupView.show(this, respView.getId(), true);
        });

        co2View.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            for (int index = 0; index < ListUtil.statusList.size(); index++) {
                optionPopupView.setOption(index, ContextUtil.getString(ListUtil.statusList.get(index)));
            }

            optionPopupView.setSelectedId(configRepository.getConfig(ConfigEnum.WakeCo2Status).getValue());
            optionPopupView.setCallback(this::co2Callback);
            optionPopupView.show(this, co2View.getId(), true);
        });

        addKeyButtonWithLiveData(0, 1);
        setKeyButtonEnum(0, KeyButtonEnum.SETUP_WAKE_VIBRATION_INTENSITY, null, null);
        setOriginValue(0, configRepository.getConfig(ConfigEnum.WakeVibrationIntensity));

        addKeyButtonWithLiveData(1, 4);
        setKeyButtonEnum(1, KeyButtonEnum.SETUP_WAKE_HR, null, null);
        setOriginValue(1, configRepository.getConfig(ConfigEnum.WakeHr));

        addKeyButtonWithLiveData(2, 5);
        setKeyButtonEnum(2, KeyButtonEnum.SETUP_WAKE_SPO2, null, null);
        setOriginValue(2, configRepository.getConfig(ConfigEnum.WakeSpo2));
    }

    @Override
    public void attach() {
        super.attach();

        selfTestId = 0;
        selfTestView.setSelected(false);
        selfTestView.setKeyId(R.string.self_test);
        selfTestView.getValue().setText(ListUtil.statusList.get(selfTestId));

        respView.setSelected(false);
        respView.setKeyId(R.string.resp);
        respView.getValue().setText(ListUtil.statusList.get(configRepository.getConfig(ConfigEnum.WakeRespStatus).getValue()));

        co2View.setSelected(false);
        co2View.setKeyId(R.string.co2);
        co2View.getValue().setText(ListUtil.statusList.get(configRepository.getConfig(ConfigEnum.WakeCo2Status).getValue()));
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void selfTestCallback(Integer value) {
        if (value != selfTestId) {
            selfTestId = value;
            wakeModel.sendChokeTest.set(value > 0);
            getIntegerPopupModel(0).getKeyButtonView().getValue().setEnabled(true);
            selfTestView.getValue().setText(ListUtil.statusList.get(selfTestId));
            selfTestView.setSelected(true);
        }
    }

    private void respCallback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.WakeRespStatus).getValue())) {
            configRepository.getConfig(ConfigEnum.WakeRespStatus).set(value);
            respView.getValue().setText(ListUtil.statusList.get(value));
            respView.setSelected(true);
        }
    }

    private void co2Callback(Integer value) {
        if (!value.equals(configRepository.getConfig(ConfigEnum.WakeCo2Status).getValue())) {
            configRepository.getConfig(ConfigEnum.WakeCo2Status).set(value);
            co2View.getValue().setText(ListUtil.statusList.get(value));
            co2View.setSelected(true);
        }
    }
}