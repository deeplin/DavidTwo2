package com.david.core.model;

import com.david.core.enumeration.CtrlEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.util.Constant;
import com.david.core.util.LazyLiveData;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IncubatorModel {

    /*status Command*/
    public final LazyLiveData<SystemEnum> systemMode = new LazyLiveData<>();
    public final LazyLiveData<Boolean> humidityPower = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> oxygenPower = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> ohTest = new LazyLiveData<>(false);
    public final LazyLiveData<Integer> cTime = new LazyLiveData<>(0);

    /*Analog Command*/
    public final LazyLiveData<Integer> VU = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> M1 = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> M2 = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> VB = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> E1 = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> W1 = new LazyLiveData<>(0);
    public final LazyLiveData<Boolean> KANG = new LazyLiveData<>(false);


    /*Ctrl Get Command*/
    public final LazyLiveData<CtrlEnum> ctrlMode = new LazyLiveData<>();
    public final LazyLiveData<Boolean> above37 = new LazyLiveData<>(false);
    public final LazyLiveData<Integer> manualObjective = new LazyLiveData<>(0);
    public final LazyLiveData<Integer> ambientTemp = new LazyLiveData<>(Constant.NA_VALUE);
    public final LazyLiveData<Integer> ambientHumidity = new LazyLiveData<>(Constant.NA_VALUE);

    /*Hardware*/
    public final LazyLiveData<Boolean> gpio = new LazyLiveData<>(false);

    @Inject
    public IncubatorModel() {
    }

    public boolean isCabin() {
        return Objects.equals(systemMode.getValue(), SystemEnum.Cabin);
    }

    public boolean isWarmer() {
        return Objects.equals(systemMode.getValue(), SystemEnum.Warmer);
    }

    public boolean isTransit() {
        return Objects.equals(systemMode.getValue(), SystemEnum.Transit);
    }

    public boolean isAir() {
        return Objects.equals(ctrlMode.getValue(), CtrlEnum.Air);
    }

    public boolean isSkin() {
        return Objects.equals(ctrlMode.getValue(), CtrlEnum.Skin);
    }

    public boolean isManual() {
        return Objects.equals(ctrlMode.getValue(), CtrlEnum.Manual);
    }

    public boolean isPrewarm() {
        return Objects.equals(ctrlMode.getValue(), CtrlEnum.Prewarm);
    }
}