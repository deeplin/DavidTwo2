package com.david.core.model;

import com.david.core.enumeration.AlarmCategoryEnum;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Co2Model extends BaseSensorModel {

    public final LazyLiveData<Integer> atmospheric = new LazyLiveData<>(1013);

    public final LazyLiveData<Integer> mode = new LazyLiveData<>();
    public final LazyLiveData<Integer> serException = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> asrException = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> dvrException = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> ssrException = new LazyLiveData<>(0);

    @Inject
    public Co2Model() {
        super(ModuleEnum.Co2, AlarmCategoryEnum.Co2_Range, 6);

        for (int index = 0; index < 3; index++) {
            SensorModelEnum sensorModelEnum = SensorModelEnum.values()[SensorModelEnum.Co2.ordinal() + index];
            AlarmWordEnum upperAlarmEnum = AlarmWordEnum.values()[AlarmWordEnum.SP_OVH.ordinal() + 2 * index];
            AlarmWordEnum lowerAlarmEnum = AlarmWordEnum.values()[AlarmWordEnum.SP_OVL.ordinal() + 2 * index];
            loadRangeAlarm(sensorModelEnum, upperAlarmEnum, lowerAlarmEnum);
        }
    }
}