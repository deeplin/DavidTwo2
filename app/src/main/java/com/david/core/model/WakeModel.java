package com.david.core.model;

import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WakeModel {

    public final LazyLiveData<Boolean> chokeConnectionError = new LazyLiveData<>(false);

    public final LazyLiveData<Boolean> sendChoke = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> sendChokeTest = new LazyLiveData<>(false);

    @Inject
    public WakeModel() {
    }
}
