package com.david.core.model;

import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrintModel {

    public final LazyLiveData<Boolean> bufferFull = new LazyLiveData<>();
    public final LazyLiveData<Boolean> outOfPaper = new LazyLiveData<>();

    @Inject
    public PrintModel() {
    }
}
