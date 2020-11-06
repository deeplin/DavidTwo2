package com.david.core.model;

import com.david.core.enumeration.AlarmCategoryEnum;
import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrintModel extends BaseSensorModel {

    public final LazyLiveData<Boolean> bufferFull = new LazyLiveData<>();
    public final LazyLiveData<Boolean> outOfPaper = new LazyLiveData<>();
    public final LazyLiveData<Boolean> printError = new LazyLiveData<>();

    @Inject
    public PrintModel() {
        super(null, AlarmCategoryEnum.Print_Sen, 1);
    }
}
