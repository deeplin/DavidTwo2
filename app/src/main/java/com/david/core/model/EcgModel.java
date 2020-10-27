package com.david.core.model;

import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EcgModel {

    public static final int ECG_SUM = 7;

    public final LazyLiveData<Boolean> ecgBeep = new LazyLiveData<>(false);

    public final LazyLiveData<Boolean> leadOff = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> v1LeadOff = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> overload = new LazyLiveData<>(false);

    public final LazyLiveData<Boolean> respLeadOff = new LazyLiveData<>(false);

    @Inject
    public EcgModel() {
    }
}