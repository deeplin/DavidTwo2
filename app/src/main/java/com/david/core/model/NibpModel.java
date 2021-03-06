package com.david.core.model;

import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.AlarmCategoryEnum;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.LazyLiveData;
import com.david.core.util.TimeUtil;
import com.david.incubator.serial.nibp.NibpProcessMode;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class NibpModel extends BaseSensorModel implements ILifeCycle {

    @Inject
    ConfigRepository configRepository;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    SystemModel systemModel;

    private final Observer<Boolean> errorCallback;

    public final LazyLiveData<Boolean> error = new LazyLiveData<>();
    public final LazyLiveData<String> fieldString = new LazyLiveData<>(Constant.SENSOR_DEFAULT_ERROR_STRING);
    public final LazyLiveData<String> subFieldString = new LazyLiveData<>();

    public final LazyLiveData<Boolean> nibpCal = new LazyLiveData<>(false);

    public final LazyLiveData<String> functionTitle = new LazyLiveData<>();
    public final LazyLiveData<String> functionValue = new LazyLiveData<>();

    public NibpProcessMode processMode;

    //0 手动 1 自动 2 STAT
    private int measureModeId;

    public final LazyLiveData<Integer> currentPressure = new LazyLiveData<>(0);

    @Inject
    public NibpModel() {
        super(ModuleEnum.Nibp, AlarmCategoryEnum.Nibp_Range, 3);

        for (int index = 0; index < 3; index++) {
            SensorModelEnum sensorModelEnum = SensorModelEnum.values()[SensorModelEnum.Nibp.ordinal() + index];
            AlarmWordEnum upperAlarmEnum = AlarmWordEnum.values()[AlarmWordEnum.NIBP_SH.ordinal() + 2 * index];
            AlarmWordEnum lowerAlarmEnum = AlarmWordEnum.values()[AlarmWordEnum.NIBP_SL.ordinal() + 2 * index];
            loadRangeAlarm(sensorModelEnum, upperAlarmEnum, lowerAlarmEnum);
        }

        processMode = NibpProcessMode.Complete;
        errorCallback = aBoolean -> {
            if (aBoolean == null) {
                functionValue.set(null);
            } else if (aBoolean) {
                functionValue.set(ContextUtil.getString(R.string.error));
            }
        };
    }

    @Override
    public void attach() {
        systemModel.setNibpUnit();
        error.observeForever(errorCallback);

        int stat = configRepository.getConfig(ConfigEnum.NibpStat).getValue();
        int measureMode = configRepository.getConfig(ConfigEnum.NibpMeasureMode).getValue();
        if (stat != 0) {
            functionTitle.set("STAT");
            measureModeId = 2;
        } else if (measureMode == 0) {
            functionTitle.set(ContextUtil.getString(R.string.manual));
            measureModeId = 0;
        } else {
            functionTitle.set(ContextUtil.getString(R.string.auto));
            measureModeId = 1;
        }
    }

    @Override
    public void detach() {
        error.removeObserver(errorCallback);
    }

    public void set(int SSS, int DDD, int MAP) {
        fieldString.post(String.format(Locale.US, "%s/%s", systemModel.nibpUnitFunction.apply(SSS), systemModel.nibpUnitFunction.apply(DDD)));
        subFieldString.post(String.format(Locale.US, "(%s)", systemModel.nibpUnitFunction.apply(MAP)));
        functionValue.post(TimeUtil.getCurrentTime(TimeUtil.Time));
    }

    public void setCuffPressureValue(int cuffPressure) {
        if (processMode.equals(NibpProcessMode.CuffPressure)) {
            fieldString.post(systemModel.nibpUnitFunction.apply(cuffPressure));
        }
    }

    public int getMeasureModeId() {
        return measureModeId;
    }
}
