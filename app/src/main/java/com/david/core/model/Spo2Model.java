package com.david.core.model;

import com.david.core.enumeration.AlarmCategoryEnum;
import com.david.core.enumeration.Spo2AverageTimeEnum;
import com.david.core.enumeration.Spo2SensEnum;
import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Spo2Model extends BaseSensorModel {

    public final LazyLiveData<Boolean> spo2Beep = new LazyLiveData<>(false);

    /*Spo2 Get*/
    public final LazyLiveData<Spo2SensEnum> sensEnum = new LazyLiveData<>(Spo2SensEnum.Normal);
    public final LazyLiveData<Spo2AverageTimeEnum> averageTimeEnum = new LazyLiveData<>(Spo2AverageTimeEnum.Zero);
    public final LazyLiveData<Boolean> fastsatValue = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> lfValue = new LazyLiveData<>(false);

    public final LazyLiveData<Boolean> smartToneValue = new LazyLiveData<>(false);

    public final LazyLiveData<Integer> sphbPrecision = new LazyLiveData<>(0);
    public final LazyLiveData<Boolean> sphbArterial = new LazyLiveData<>(false);
    public final LazyLiveData<Integer> sphbAverage = new LazyLiveData<>(0);
    public final LazyLiveData<Boolean> pviAverage = new LazyLiveData<>(false);

    @Inject
    public Spo2Model() {
        super(AlarmCategoryEnum.Spo2_Con, 6);
    }

    @Override
    protected int getRangeEnumId() {
        return AlarmCategoryEnum.Spo2_Range.getIndex();
    }
}