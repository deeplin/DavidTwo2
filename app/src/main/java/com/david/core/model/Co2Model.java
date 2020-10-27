package com.david.core.model;

import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Co2Model {

    public final LazyLiveData<Integer> atmospheric = new LazyLiveData<>(1013);

    public final LazyLiveData<Integer> mode = new LazyLiveData<>();
    public final LazyLiveData<Integer> serException = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> asrException = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> dvrException = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> ssrException = new LazyLiveData<>(0);

    @Inject
    public Co2Model() {
    }
}