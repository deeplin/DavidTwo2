package com.david.incubator.ui.system;

import android.content.Context;
import android.view.View;

import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.serial.incubator.command.alarm.AlarmGetCommand;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class SystemOverheatAlarmLayout extends BaseLayout {

    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    IncubatorModel incubatorModel;

    private int airAbove37Value;
    private int airBelow37Value;
    private int airFlowAbove37Value;
    private int airFlowBelow37Value;

    public SystemOverheatAlarmLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.SYSTEM_OVERHEAT_ALARM);

        loadLiveDataItems(KeyButtonEnum.SYSTEM_OVERHEAT_ALARM_AIR_ABOVE_37, 4, null, null);
        addKeyButtonWithLiveData(4, 4);
        setKeyButtonEnum(4, KeyButtonEnum.SYSTEM_OVERHEAT_ALARM_AIR_FLOW_ABOVE_37, this::minCondition, null);
        addKeyButtonWithLiveData(5, 5);
        setKeyButtonEnum(5, KeyButtonEnum.SYSTEM_OVERHEAT_ALARM_AIR_FLOW_ABOVE_37, null, this::maxCondition);

        setCallback(0, this::setAirAbove37Value);
        setCallback(1, this::setAirBelow7Value);
        setCallback(2, this::setSkin);
        setCallback(3, this::setFanSpeed);
        setCallback(4, this::setAirFlowAbove37Value);
        setCallback(5, this::setAirFlowBelow7Value);
    }

    @Override
    public void attach() {
        super.attach();

        getSkin();
        if (incubatorModel.isCabin()) {
            getAir();
            getAirFlow();
            getFanSpeed();
            getIntegerPopupModel(0).getKeyButtonView().setVisibility(View.VISIBLE);
            getIntegerPopupModel(1).getKeyButtonView().setVisibility(View.VISIBLE);
            getIntegerPopupModel(3).getKeyButtonView().setVisibility(View.VISIBLE);
            getIntegerPopupModel(4).getKeyButtonView().setVisibility(View.VISIBLE);
            getIntegerPopupModel(5).getKeyButtonView().setVisibility(View.VISIBLE);
        } else {
            getIntegerPopupModel(0).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(1).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(3).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(4).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(5).getKeyButtonView().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    private Integer minCondition(KeyButtonEnum keyButtonEnum) {
        return airFlowBelow37Value;
    }

    private Integer maxCondition(KeyButtonEnum keyButtonEnum) {
        return airFlowAbove37Value;
    }

    private void getAir() {
        incubatorCommandSender.getAlert(AlarmWordEnum.AIR_OVH,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        AlarmGetCommand alarmGetCommand = (AlarmGetCommand) baseSerialMessage;
                        airBelow37Value = alarmGetCommand.getT1();
                        setOriginValue(1, airBelow37Value);
                        airAbove37Value = alarmGetCommand.getT2();
                        setOriginValue(0, airAbove37Value);
                    }
                });
    }

    private void getAirFlow() {
        incubatorCommandSender.getAlert(AlarmWordEnum.FLOW_OVH,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        AlarmGetCommand alarmGetCommand = (AlarmGetCommand) baseSerialMessage;
                        airFlowBelow37Value = alarmGetCommand.getT1();
                        setOriginValue(5, airFlowBelow37Value);
                        airFlowAbove37Value = alarmGetCommand.getT2();
                        setOriginValue(4, airFlowAbove37Value);
                    }
                });
    }

    private void getSkin() {
        incubatorCommandSender.getAlert(AlarmWordEnum.SKIN_OVH,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        AlarmGetCommand alarmGetCommand = (AlarmGetCommand) baseSerialMessage;
                        int skin = alarmGetCommand.getT();
                        setOriginValue(2, skin);
                    }
                });
    }

    private void getFanSpeed() {
        incubatorCommandSender.getAlert(AlarmWordEnum.SYS_FAN,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        AlarmGetCommand alarmGetCommand = (AlarmGetCommand) baseSerialMessage;
                        int fanSpeed = alarmGetCommand.getRPM() / 10 * 10;
                        setOriginValue(3, fanSpeed);
                    }
                });
    }

    private void setAirAbove37Value(Integer value) {
        airAbove37Value = value;
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.AIR_OVH.toString(), airBelow37Value, airAbove37Value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getAir();
                    }
                });
    }

    private void setAirBelow7Value(Integer value) {
        airBelow37Value = value;
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.AIR_OVH.toString(), airBelow37Value, airAbove37Value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getAir();
                    }
                });
    }

    private void setAirFlowAbove37Value(Integer value) {
        airFlowAbove37Value = value;
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.FLOW_OVH.toString(), airFlowBelow37Value, airFlowAbove37Value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getAir();
                    }
                });
    }

    private void setAirFlowBelow7Value(Integer value) {
        airFlowBelow37Value = value;
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.FLOW_OVH.toString(), airFlowBelow37Value, airFlowAbove37Value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getAir();
                    }
                });
    }

    private void setSkin(Integer value) {
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.SKIN_OVH.toString(), value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getSkin();
                    }
                });
    }

    private void setFanSpeed(Integer value) {
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.SYS_FAN.toString(), value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getFanSpeed();
                    }
                });
    }
}