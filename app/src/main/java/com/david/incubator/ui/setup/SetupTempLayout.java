package com.david.incubator.ui.setup;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.CtrlEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.ui.model.IntegerPopupModel;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.LazyLiveData;
import com.david.core.util.ListUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class SetupTempLayout extends BaseLayout {

    @Inject
    IncubatorModel incubatorModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    SystemModel systemModel;

    private final KeyButtonView modeView;
    private final KeyButtonView above37View;

    private final Observer<CtrlEnum> getCtrlObserver;

    private final KeyButtonView[] keyButtonViewArray;

    private final Button kangButton;

    public SetupTempLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.initLargeFont();

        modeView = ViewUtil.buildKeyButtonView(getContext());
        modeView.setBigFont();
        above37View = ViewUtil.buildKeyButtonView(getContext());
        above37View.setBigFont();
        addInnerView(0, modeView);
        addInnerView(2, above37View);

        keyButtonViewArray = new KeyButtonView[3];
        for (int index = 0; index < keyButtonViewArray.length; index++) {
            keyButtonViewArray[index] = addKeyButtonWithLiveData(index, 1);
            keyButtonViewArray[index].setBigFont();
        }

        setKeyButtonEnum(0, KeyButtonEnum.SETUP_AIR, null, this::increaseObjectiveCondition);
        setKeyButtonEnum(1, KeyButtonEnum.SETUP_SKIN, null, this::increaseObjectiveCondition);
        setKeyButtonEnum(2, KeyButtonEnum.SETUP_MANUEL, null, null);

        LazyLiveData<Integer> airModelObjective = sensorModelRepository.getSensorModel(SensorModelEnum.Air).objective;
        setOriginValue(0, airModelObjective);
        LazyLiveData<Integer> skinModelObjective = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1).objective;
        setOriginValue(1, skinModelObjective);

        setOriginValue(2, incubatorModel.manualObjective);

        setCallback(0, this::setAirObjective);
        setCallback(1, this::setSkinObjective);
        setCallback(2, this::setManualObjective);

        kangButton = ViewUtil.buildButton(getContext());
        addInnerButton(4, kangButton);
        kangButton.setText(ContextUtil.getString(R.string.kang));
        kangButton.setTextSize(40);
        kangButton.setOnClickListener(view ->
                incubatorCommandSender.clearAlarm120("ALL", (aBoolean, baseSerialMessage) -> {
                    incubatorModel.KANG.post(true);
                    systemModel.showLayout(LayoutPageEnum.LAYOUT_STANDARD);
                    systemModel.lockScreen.post(true);
                    if (incubatorModel.isCabin()) {
                        setCtrl(CtrlEnum.Air.ordinal());
                    } else if (incubatorModel.isWarmer()) {
                        setManualObjective(30);
                    }
                }));

        modeView.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            if (incubatorModel.isCabin()) {
                optionPopupView.setOption(0, ContextUtil.getString(CtrlEnum.Skin.getTextId()));
                optionPopupView.setOption(1, ContextUtil.getString(CtrlEnum.Air.getTextId()));
            } else {
                optionPopupView.setOption(0, ContextUtil.getString(CtrlEnum.Skin.getTextId()));
                optionPopupView.setOption(2, ContextUtil.getString(CtrlEnum.Manual.getTextId()));
                optionPopupView.setOption(3, ContextUtil.getString(CtrlEnum.Prewarm.getTextId()));
            }

            CtrlEnum ctrlEnum = incubatorModel.ctrlMode.getValue();
            optionPopupView.setSelectedId(ctrlEnum.ordinal());
            optionPopupView.setCallback(this::setCtrl);
            optionPopupView.show(this, modeView.getId(), true);
        });

        above37View.getValue().setOnClickListener(v -> {
            closePopup();
            optionPopupView.init();

            optionPopupView.setOption(0, ContextUtil.getString(ListUtil.statusList.get(0)));
            optionPopupView.setOption(1, ContextUtil.getString(ListUtil.statusList.get(1)));

            optionPopupView.setSelectedId(incubatorModel.above37.getValue() ? 1 : 0);
            optionPopupView.setCallback(this::above37Confirm);
            optionPopupView.show(this, above37View.getId(), true);
        });

        getCtrlObserver = ctrlEnum -> {
            closePopup();
            modeView.getValue().setText(ContextUtil.getString(ctrlEnum.getTextId()));
            if (incubatorModel.isCabin()) {
                if (incubatorModel.isAir()) {
                    setVisible(0, true);
                    setVisible(1, false);
                    setVisible(2, false);
                    SensorModel airModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
                    setAbove37Visible(airModel.objective.getValue());
                    above37View.setVisibility(View.VISIBLE);
                } else if (incubatorModel.isSkin()) {
                    setVisible(0, false);
                    setVisible(1, true);
                    setVisible(2, false);
                    SensorModel skinModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
                    setAbove37Visible(skinModel.objective.getValue());
                    above37View.setVisibility(View.VISIBLE);
                }
            } else if (incubatorModel.isWarmer()) {
                if (incubatorModel.isSkin()) {
                    setVisible(0, false);
                    setVisible(1, true);
                    setVisible(2, false);
                    SensorModel skinModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin2);
                    setAbove37Visible(skinModel.objective.getValue());
                    above37View.setVisibility(View.VISIBLE);
                } else if (incubatorModel.isPrewarm()) {
                    setVisible(0, false);
                    setVisible(1, false);
                    setVisible(2, false);
                    above37View.setVisibility(View.INVISIBLE);
                } else if (incubatorModel.isManual()) {
                    setVisible(0, false);
                    setVisible(1, false);
                    setVisible(2, true);
                    above37View.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    @Override
    public void attach() {
        super.attach();
        optionPopupView.setBigFont(true);
        numberPopupView.setBigFont(true);
        modeView.setSelected(false);

        modeView.setKeyId(R.string.mode);
        above37View.setKey(">37℃");

        SensorModel airModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
        IntegerPopupModel airPopupModel = getIntegerPopupModel(0);
        airPopupModel.setMin(airModel.lowerLimit.getValue());
        airPopupModel.setMax(airModel.upperLimit.getValue());

        SensorModel skinModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
        IntegerPopupModel skinPopupModel = getIntegerPopupModel(1);
        skinPopupModel.setMin(skinModel.lowerLimit.getValue());
        skinPopupModel.setMax(skinModel.upperLimit.getValue());

        incubatorModel.ctrlMode.observeForever(getCtrlObserver);
    }

    @Override
    public void detach() {
        optionPopupView.setBigFont(false);
        numberPopupView.setBigFont(false);
        super.detach();
        incubatorModel.ctrlMode.removeObserver(getCtrlObserver);
    }

    private Integer increaseObjectiveCondition(KeyButtonEnum keyButtonEnum) {
        if (incubatorModel.above37.getValue()) {
            return Integer.MAX_VALUE;
        } else {
            return Constant.TEMP_370 + 1;
        }
    }

    private void setCtrl(int value) {
        CtrlEnum ctrlEnum = CtrlEnum.values()[value];
        incubatorCommandSender.setCtrlMode(ctrlEnum.name(), (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                incubatorModel.ctrlMode.post(ctrlEnum);
                incubatorCommandSender.getCtrlGet();
            }
        });
    }

    private void setAirObjective(Integer value) {
        setValue(CtrlEnum.Air.name(), value);
    }

    private void setSkinObjective(Integer value) {
        setValue(CtrlEnum.Skin.name(), value);
    }

    private void setManualObjective(Integer value) {
        setValue(CtrlEnum.Manual.name(), value);
    }

    private void setValue(String ctrlEnum, Integer value) {
        SystemEnum systemEnum = incubatorModel.systemMode.getValue();
        incubatorCommandSender.setCtrlSet(systemEnum.name(), ctrlEnum, value, (aBoolean, baseSerialMessage) -> {
            incubatorCommandSender.getCtrlGet();
            if (aBoolean) {
                SetupTempLayout.this.post(() -> setAbove37Visible(value));
            }
        });
    }

    private void above37Confirm(Integer index) {
        incubatorModel.above37.set(index != 0);
        above37View.getValue().setText(ContextUtil.getString(ListUtil.statusList.get(index)));
    }

    private void setAbove37Visible(int value) {
        if (value == Constant.TEMP_370) {
            incubatorModel.above37.set(false);
            above37View.getValue().setEnabled(true);
            above37View.getValue().setText(ContextUtil.getString(R.string.off));
        } else if (value < Constant.TEMP_370) {
            incubatorModel.above37.set(false);
            above37View.getValue().setEnabled(false);
            above37View.getValue().setText(ContextUtil.getString(R.string.off));
        } else {
            above37View.getValue().setEnabled(false);
            above37View.getValue().setText(ContextUtil.getString(R.string.on));
        }
    }

    private void setVisible(int index, boolean visible) {
        if (visible) {
            keyButtonViewArray[index].setVisibility(View.VISIBLE);
        } else {
            keyButtonViewArray[index].setVisibility(View.INVISIBLE);
        }
    }
}