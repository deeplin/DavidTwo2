package com.david.incubator.ui.system;

import android.content.Context;
import android.view.View;

import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.serial.incubator.command.calibration.ShowCalibration2Command;
import com.david.core.serial.incubator.command.calibration.ShowCalibrationCommand;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

public class SystemSensorCalibrationLayout extends BaseLayout {

    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    IncubatorModel incubatorModel;

    public SystemSensorCalibrationLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.SYSTEM_SENSOR_CALIBRATION);

        loadLiveDataItems(KeyButtonEnum.SYSTEM_SENSOR_CALIBRATION_AIR, 5, null, null);

        setCallback(0, this::airCallback);
        setCallback(1, this::isoAirCallback);
        setCallback(2, this::skinCallback);
        setCallback(3, this::isoSkinCallback);
        setCallback(4, this::humidityCallback);
    }

    @Override
    public void attach() {
        super.attach();
        refresh();
        if (incubatorModel.isCabin()) {
            getIntegerPopupModel(0).getKeyButtonView().setVisibility(View.VISIBLE);
            getIntegerPopupModel(1).getKeyButtonView().setVisibility(View.VISIBLE);
            getIntegerPopupModel(4).getKeyButtonView().setVisibility(View.VISIBLE);
        } else if (incubatorModel.isWarmer()) {
            getIntegerPopupModel(0).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(1).getKeyButtonView().setVisibility(View.INVISIBLE);
            getIntegerPopupModel(4).getKeyButtonView().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void refresh() {
        incubatorCommandSender.getCalibration(
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        ShowCalibrationCommand showCalibrationCommand = (ShowCalibrationCommand) baseSerialMessage;
                        int skin1 = showCalibrationCommand.getS1A();
                        setOriginValue(2, skin1);
                        int isoSkin1 = showCalibrationCommand.getS1B();
                        setOriginValue(3, isoSkin1);
                    }
                });
        incubatorCommandSender.getCalibration2(
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        ShowCalibration2Command showCalibration2Command = (ShowCalibration2Command) baseSerialMessage;
                        int air = showCalibration2Command.getA1();
                        setOriginValue(0, air);
                        int isoAir = showCalibration2Command.getA2();
                        setOriginValue(1, isoAir);
                        int humidity = showCalibration2Command.getHUM();
                        setOriginValue(4, humidity);
                    }
                });
    }

    private void sendTempCommand(String sensor, int value) {
        incubatorCommandSender.setTempCalibration(sensor, value, (aBoolean, baseSerialMessage) -> refresh());
    }

    private void sendHumCommand(int value) {
        incubatorCommandSender.setHumCalibration(value, (aBoolean, baseSerialMessage) -> refresh());
    }

    private void airCallback(Integer value) {
        sendTempCommand("A1", value);
    }

    private void isoAirCallback(Integer value) {
        sendTempCommand("A2", value);
    }

    private void skinCallback(Integer value) {
        sendTempCommand("S1A", value);
    }

    private void isoSkinCallback(Integer value) {
        sendTempCommand("S1B", value);
    }

    private void humidityCallback(Integer value) {
        sendHumCommand(value);
    }

    public static String formatTemp(int value) {
        return formatValueUnit(SensorModelEnum.Skin1, value);
    }

    public static String formatHumidity(int value) {
        return formatValueUnit(SensorModelEnum.Humidity, value);
    }

    public static String formatValueUnit(SensorModelEnum sensorModelEnum, double value) {
        return String.format(Locale.US, "%s %s",
                formatValue(value, sensorModelEnum.getDenominator(), sensorModelEnum.getFormatString()), sensorModelEnum.getUnit());
    }

    public static String formatValue(double value, int denominator, String formatString) {
        DecimalFormat decimalFormat = new DecimalFormat(formatString);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(value / denominator);
    }
}