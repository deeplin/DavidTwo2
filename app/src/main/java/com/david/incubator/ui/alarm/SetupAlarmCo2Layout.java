package com.david.incubator.ui.alarm;

import android.content.Context;

import androidx.arch.core.util.Function;

import com.david.core.control.ConfigRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.rely.FormatUtil;
import com.david.core.util.LazyLiveData;
import com.david.core.util.UnitUtil;

import javax.inject.Inject;

public class SetupAlarmCo2Layout extends BaseLayout {

    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    ConfigRepository configRepository;

    private final LazyLiveData<Integer>[] liveDataArray = new LazyLiveData[4];

    private Function<Integer, Integer> reUnitFunction;

    public SetupAlarmCo2Layout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init();

        for (int index = 0; index < 4; index++) {
            liveDataArray[index] = new LazyLiveData<>();
        }

        setupLimit(0, sensorModelRepository.getSensorModel(SensorModelEnum.Co2));
        setupLimit(2, sensorModelRepository.getSensorModel(SensorModelEnum.Co2Fi));
    }

    @Override
    public void attach() {
        super.attach();

        Function<Integer, Integer> unitFunction;
        Function<Integer, String> converter;
        int step;
        int upperLimit;
        switch (configRepository.getConfig(ConfigEnum.Co2Unit).getValue()) {
            case (0):
                unitFunction = UnitUtil::percentageToMmHg;
                reUnitFunction = UnitUtil::mmHgToPercentage;
                converter = FormatUtil::formatMmHg;
                step = 1;
                upperLimit = 190;
                break;
            case (1):
                unitFunction = UnitUtil::percentageToKPa;
                reUnitFunction = UnitUtil::kPaToPercentage;
                converter = FormatUtil::formatKPa;
                step = 1;
                upperLimit = 253;
                break;
            default:
                unitFunction = UnitUtil::transparent;
                reUnitFunction = UnitUtil::transparent;
                converter = FormatUtil::formatPercentageBy10;
                step = 1;
                upperLimit = 250;
                break;
        }
        SensorModel co2EtModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2);
        setupLimit(0, co2EtModel, unitFunction);
        addCo2KeyButton(0, KeyButtonEnum.ALARM_CO2_UPPER, KeyButtonEnum.ALARM_CO2_LOWER,
                converter, step, upperLimit);

        SensorModel co2FiModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Fi);
        setupLimit(2, co2FiModel, unitFunction);
        addCo2KeyButton(2, KeyButtonEnum.ALARM_CO2_FI_UPPER, KeyButtonEnum.ALARM_CO2_FI_LOWER,
                converter, step, upperLimit);

        SensorModel co2RrModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Rr);
        addKeyButton(4, co2RrModel, KeyButtonEnum.ALARM_CO2_RR_UPPER, KeyButtonEnum.ALARM_CO2_RR_LOWER,
                this::minRrCondition, this::maxRrCondition);
    }

    @Override
    public void detach() {
        super.detach();
        removeItemViews();
    }

    private Integer minRrCondition(KeyButtonEnum keyButtonEnum) {
        int index = keyButtonEnum.ordinal() - KeyButtonEnum.ALARM_CO2_UPPER.ordinal();
        if (index % 2 == 0) {
            SensorModel sensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.values()[SensorModelEnum.Co2.ordinal() + index / 2]);
            return sensorModel.lowerLimit.getValue();
        } else {
            return Integer.MIN_VALUE;
        }
    }

    private Integer maxRrCondition(KeyButtonEnum keyButtonEnum) {
        int index = keyButtonEnum.ordinal() - KeyButtonEnum.ALARM_CO2_UPPER.ordinal();
        if (index % 2 == 1) {
            SensorModel sensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.values()[SensorModelEnum.Co2.ordinal() + index / 2]);
            return sensorModel.upperLimit.getValue();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    private Integer minCondition(KeyButtonEnum keyButtonEnum) {
        int index = keyButtonEnum.ordinal() - KeyButtonEnum.ALARM_CO2_UPPER.ordinal();
        if (index % 2 == 0) {
            return liveDataArray[index + 1].getValue() + 1;
        } else {
            return Integer.MIN_VALUE;
        }
    }

    private Integer maxCondition(KeyButtonEnum keyButtonEnum) {
        int index = keyButtonEnum.ordinal() - KeyButtonEnum.ALARM_CO2_UPPER.ordinal();
        if (index % 2 == 1) {
            return liveDataArray[index - 1].getValue() - 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    protected void addCo2KeyButton(int rowId, KeyButtonEnum upperKeyButtonEnum, KeyButtonEnum lowerKeyButtonEnum,
                                   Function<Integer, String> converter, int step, int upperLimit) {
        upperKeyButtonEnum.setConverter(converter);
        upperKeyButtonEnum.setUpperLimit(upperLimit);
        upperKeyButtonEnum.setStep(step);


        addKeyButtonWithLiveData(rowId, rowId);
        setKeyButtonEnum(rowId, upperKeyButtonEnum, this::minCondition, this::maxCondition);
        setOriginValue(rowId, liveDataArray[rowId]);
        rowId++;

        lowerKeyButtonEnum.setConverter(converter);
        lowerKeyButtonEnum.setUpperLimit(upperLimit);
        lowerKeyButtonEnum.setStep(step);


        addKeyButtonWithLiveData(rowId, rowId);
        setKeyButtonEnum(rowId, lowerKeyButtonEnum, this::minCondition, this::maxCondition);
        setOriginValue(rowId, liveDataArray[rowId]);
    }

    private void setupLimit(int rowId, SensorModel sensorModel) {
        liveDataArray[rowId].observeForever(integer -> sensorModel.upperLimit.set(reUnitFunction.apply(integer)));
        rowId++;
        liveDataArray[rowId].observeForever(integer -> sensorModel.lowerLimit.set(reUnitFunction.apply(integer)));
    }

    private void setupLimit(int rowId, SensorModel sensorModel, Function<Integer, Integer> unitFunction) {
        liveDataArray[rowId].set(unitFunction.apply(sensorModel.upperLimit.getValue()));
        rowId++;
        liveDataArray[rowId].set(unitFunction.apply(sensorModel.lowerLimit.getValue()));
    }
}