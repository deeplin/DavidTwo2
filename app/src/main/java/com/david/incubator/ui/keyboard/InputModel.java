package com.david.incubator.ui.keyboard;

import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InputModel {

    public final LazyLiveData<String> inputText = new LazyLiveData<>();
    public final LazyLiveData<String> title = new LazyLiveData<>();

    @Inject
    public InputModel() {
    }
}
