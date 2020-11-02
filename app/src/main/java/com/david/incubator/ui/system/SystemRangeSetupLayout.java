package com.david.incubator.ui.system;

import android.content.Context;
import android.view.View;

import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class SystemRangeSetupLayout extends BaseLayout {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    ModuleHardware moduleHardware;

    public SystemRangeSetupLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.SYSTEM_RANGE_SETUP);

        loadLiveDataItems(KeyButtonEnum.SYSTEM_RANGE_SETUP_AIR_UPPER, 8, this::minCondition, this::maxCondition);

        SensorModel airModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
        SensorModel skinModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
        SensorModel humidityModel = sensorModelRepository.getSensorModel(SensorModelEnum.Humidity);
        SensorModel oxygenModel = sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen);

        setOriginValue(0, airModel.upperLimit);
        setOriginValue(1, airModel.lowerLimit);
        setOriginValue(2, skinModel.upperLimit);
        setOriginValue(3, skinModel.lowerLimit);
        setOriginValue(4, humidityModel.upperLimit);
        setOriginValue(5, humidityModel.lowerLimit);
        setOriginValue(6, oxygenModel.upperLimit);
        setOriginValue(7, oxygenModel.lowerLimit);
    }

    @Override
    public void attach() {
        super.attach();

        if (incubatorModel.isCabin()) {
            getIntegerPopupModel(0).getKeyButtonView().setVisibility(View.VISIBLE);
            getIntegerPopupModel(1).getKeyButtonView().setVisibility(View.VISIBLE);

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

    private Integer minCondition(KeyButtonEnum keyButtonEnum) {
        switch (keyButtonEnum) {
            case SYSTEM_RANGE_SETUP_AIR_UPPER:
                SensorModel airModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
                return airModel.objective.getValue();
            case SYSTEM_RANGE_SETUP_SKIN_UPPER:
                SensorModel skinModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
                return skinModel.objective.getValue();
            case SYSTEM_RANGE_SETUP_HUMIDITY_UPPER:
                SensorModel humidityModel = sensorModelRepository.getSensorModel(SensorModelEnum.Humidity);
                if (humidityModel.objective.getValue() >= humidityModel.lowerLimit.getValue()) {
                    return humidityModel.objective.getValue();
                } else {
                    return humidityModel.lowerLimit.getValue();
                }
            case SYSTEM_RANGE_SETUP_OXYGEN_UPPER:
                SensorModel oxygenModel = sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen);
                return oxygenModel.objective.getValue();
            default:
                return Integer.MIN_VALUE;
        }
    }

    private Integer maxCondition(KeyButtonEnum keyButtonEnum) {
        switch (keyButtonEnum) {
            case SYSTEM_RANGE_SETUP_AIR_LOWER:
                SensorModel airModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
                return airModel.objective.getValue();
            case SYSTEM_RANGE_SETUP_SKIN_LOWER:
                SensorModel skinModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
                return skinModel.objective.getValue();
            case SYSTEM_RANGE_SETUP_HUMIDITY_LOWER:
                SensorModel humidityModel = sensorModelRepository.getSensorModel(SensorModelEnum.Humidity);
                if (humidityModel.objective.getValue() <= humidityModel.upperLimit.getValue()) {
                    return humidityModel.objective.getValue();
                } else {
                    return humidityModel.upperLimit.getValue();
                }
            case SYSTEM_RANGE_SETUP_OXYGEN_LOWER:
                SensorModel oxygenModel = sensorModelRepository.getSensorModel(SensorModelEnum.Oxygen);
                return oxygenModel.objective.getValue();
            default:
                return Integer.MAX_VALUE;
        }
    }
}