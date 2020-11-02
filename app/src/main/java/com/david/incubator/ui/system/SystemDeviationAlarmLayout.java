package com.david.incubator.ui.system;

import android.content.Context;
import android.view.View;

import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.serial.incubator.command.alarm.AlarmGetCommand;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class SystemDeviationAlarmLayout extends BaseLayout {

    @Inject
    IncubatorModel incubatorModel;
    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    ModuleHardware moduleHardware;

    private int airUpperLimit;
    private int airLowerLimit;
    private int skinUpperLimit;
    private int skinLowerLimit;

    public SystemDeviationAlarmLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.SYSTEM_DEVIATION_ALARM);

        loadLiveDataItems(KeyButtonEnum.SYSTEM_DEVIATION_AIR_UPPER, 8, null, null);
        setCallback(0, this::setAirUpper);
        setCallback(1, this::setAirLower);
        setCallback(2, this::setSkinUpper);
        setCallback(3, this::setSkinLower);
        setCallback(4, this::setHumUpper);
        setCallback(5, this::setHumLower);
        setCallback(6, this::setOxygenUpper);
        setCallback(7, this::setOxygenLower);
    }

    @Override
    public void attach() {
        super.attach();
        getTempDevh();
        getTempDevl();

        if (incubatorModel.isCabin()) {
            getIntegerPopupModel(0).getKeyButtonView().setVisibility(View.VISIBLE);
            getIntegerPopupModel(1).getKeyButtonView().setVisibility(View.VISIBLE);

            getHumDevh();
            getHumDevl();
            getO2Devh();
            getO2Devl();

            if (moduleHardware.isInstalled(ModuleEnum.Hum)) {
                getIntegerPopupModel(4).getKeyButtonView().setVisibility(View.VISIBLE);
                getIntegerPopupModel(5).getKeyButtonView().setVisibility(View.VISIBLE);
            } else {
                getIntegerPopupModel(4).getKeyButtonView().setVisibility(View.INVISIBLE);
                getIntegerPopupModel(5).getKeyButtonView().setVisibility(View.INVISIBLE);
            }

            if (moduleHardware.isInstalled(ModuleEnum.Oxygen)) {
                getIntegerPopupModel(6).getKeyButtonView().setVisibility(View.VISIBLE);
                getIntegerPopupModel(7).getKeyButtonView().setVisibility(View.VISIBLE);
            } else {
                getIntegerPopupModel(6).getKeyButtonView().setVisibility(View.INVISIBLE);
                getIntegerPopupModel(7).getKeyButtonView().setVisibility(View.INVISIBLE);
            }
        } else {
            getIntegerPopupModel(0).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(1).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(4).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(5).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(6).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(7).getKeyButtonView().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void getTempDevh() {
        incubatorCommandSender.getAlert(AlarmWordEnum.TEMP_DEVH, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                AlarmGetCommand alarmGetCommand = (AlarmGetCommand) baseSerialMessage;
                airUpperLimit = alarmGetCommand.getADevH();
                setOriginValue(0, airUpperLimit);
                skinUpperLimit = alarmGetCommand.getSDevH();
                setOriginValue(2, skinUpperLimit);
            }
        });
    }

    private void getTempDevl() {
        incubatorCommandSender.getAlert(AlarmWordEnum.TEMP_DEVL, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                AlarmGetCommand alarmGetCommand = (AlarmGetCommand) baseSerialMessage;
                airLowerLimit = alarmGetCommand.getADevL();
                setOriginValue(1, airLowerLimit);
                skinLowerLimit = alarmGetCommand.getSDevL();
                setOriginValue(3, skinLowerLimit);
            }
        });
    }

    private void getHumDevh() {
        incubatorCommandSender.getAlert(AlarmWordEnum.HUM_DEVH, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                AlarmGetCommand alarmGetCommand = (AlarmGetCommand) baseSerialMessage;
                setOriginValue(4, alarmGetCommand.getHDevH() / 10 * 10);
            }
        });
    }

    private void getHumDevl() {
        incubatorCommandSender.getAlert(AlarmWordEnum.HUM_DEVL, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                AlarmGetCommand alarmGetCommand = (AlarmGetCommand) baseSerialMessage;
                setOriginValue(5, alarmGetCommand.getHDevL() / 10 * 10);
            }
        });
    }

    private void getO2Devh() {
        incubatorCommandSender.getAlert(AlarmWordEnum.O2_DEVH, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                AlarmGetCommand alarmGetCommand = (AlarmGetCommand) baseSerialMessage;
                setOriginValue(6, alarmGetCommand.getODevH());
            }
        });
    }

    private void getO2Devl() {
        incubatorCommandSender.getAlert(AlarmWordEnum.O2_DEVL, (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                AlarmGetCommand alarmGetCommand = (AlarmGetCommand) baseSerialMessage;
                setOriginValue(7, alarmGetCommand.getODevL());
            }
        });
    }

    private void setAirUpper(Integer value) {
        airUpperLimit = value;
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.TEMP_DEVH.toString(), airUpperLimit,
                skinUpperLimit,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getTempDevh();
                    }
                });
    }

    private void setSkinUpper(Integer value) {
        skinUpperLimit = value;
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.TEMP_DEVH.toString(), airUpperLimit,
                skinUpperLimit,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getTempDevh();
                    }
                });
    }

    private void setAirLower(Integer value) {
        airLowerLimit = value;
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.TEMP_DEVL.toString(), airLowerLimit,
                skinLowerLimit,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getTempDevl();
                    }
                });
    }

    private void setSkinLower(Integer value) {
        skinLowerLimit = value;
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.TEMP_DEVL.toString(), airLowerLimit,
                skinLowerLimit,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getTempDevl();
                    }
                });
    }

    private void setHumUpper(Integer value) {
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.HUM_DEVH.toString(), value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getHumDevh();
                    }
                });
    }

    private void setHumLower(Integer value) {
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.HUM_DEVL.toString(), value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getHumDevl();
                    }
                });
    }

    private void setOxygenUpper(Integer value) {
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.O2_DEVH.toString(), value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getO2Devh();
                    }
                });
    }

    private void setOxygenLower(Integer value) {
        incubatorCommandSender.setAlarmConfig(AlarmWordEnum.O2_DEVL.toString(), value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        getO2Devl();
                    }
                });
    }
}