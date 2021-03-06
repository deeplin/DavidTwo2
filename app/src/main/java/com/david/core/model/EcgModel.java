package com.david.core.model;

import com.david.core.enumeration.AlarmCategoryEnum;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EcgModel extends BaseSensorModel {

    public static final int ECG_SUM = 7;

    public final LazyLiveData<Boolean> ecgBeep = new LazyLiveData<>(false);

    public final LazyLiveData<Boolean> leadOff = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> v1LeadOff = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> overload = new LazyLiveData<>(false);

    public final LazyLiveData<Boolean> respLeadOff = new LazyLiveData<>(false);

    @Inject
    public EcgModel() {
        super(ModuleEnum.Ecg, AlarmCategoryEnum.Ecg_Range, 2);
        for (int index = 0; index < 3; index++) {
            SensorModelEnum sensorModelEnum = SensorModelEnum.values()[SensorModelEnum.Nibp.ordinal() + index];
            AlarmWordEnum upperAlarmEnum = AlarmWordEnum.values()[AlarmWordEnum.ECG_HRH.ordinal() + 2 * index];
            AlarmWordEnum lowerAlarmEnum = AlarmWordEnum.values()[AlarmWordEnum.ECG_HRL.ordinal() + 2 * index];
            loadRangeAlarm(sensorModelEnum, upperAlarmEnum, lowerAlarmEnum);
        }
        leadOff.observeForever(aBoolean -> setSenAlarm(AlarmWordEnum.ECG_DROP, aBoolean));
        v1LeadOff.observeForever(aBoolean -> setSenAlarm(AlarmWordEnum.ECG_VDROP, aBoolean));
        overload.observeForever(aBoolean -> setSenAlarm(AlarmWordEnum.ECG_OV, aBoolean));
    }
}