package com.david.core.model;

import androidx.arch.core.util.Function;

import com.david.core.control.ConfigRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.util.Constant;
import com.david.core.util.LazyLiveData;
import com.david.core.util.UnitUtil;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SystemModel {

    @Inject
    ConfigRepository configRepository;
    @Inject
    SensorModelRepository sensorModelRepository;

    public final LazyLiveData<Integer> systemInitState = new LazyLiveData<>(Constant.NA_VALUE);
    public final LazyLiveData<Boolean> darkMode = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> demo = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> lockScreen = new LazyLiveData<>(false);
    public final LazyLiveData<LayoutPageEnum> layoutPage = new LazyLiveData<>(LayoutPageEnum.LAYOUT_STANDARD);
    public final LazyLiveData<Boolean> freezeWave = new LazyLiveData<>(false);
    public final LazyLiveData<Boolean> selectHr = new LazyLiveData<>();
    public final LazyLiveData<Boolean> selectCo2 = new LazyLiveData<>(true);
    public Function<Integer, String> respUnitFunction;
    public Function<Integer, String> nibpUnitFunction;

    public int tagId;
    private LayoutPageEnum currentLayoutPage;
    private long lockTime;
    public boolean lockEnable = true;

    @Inject
    public SystemModel() {
        currentLayoutPage = layoutPage.getValue();
        lockTime = Constant.SCREEN_LOCK_SECOND;
    }

    public boolean isFreeze() {
        Boolean lockScreenValue = lockScreen.getValue();
        if (lockScreenValue != null && lockScreenValue) {
            return true;
        }
        return false;
    }

    public void showLayout(LayoutPageEnum layoutPageEnum) {
        if (isFreeze()) {
            return;
        }
        if (Objects.equals(layoutPageEnum, LayoutPageEnum.LAYOUT_NONE)) {
            layoutPage.post(currentLayoutPage);
        } else if (layoutPageEnum.ordinal() < LayoutPageEnum.MENU_HOME.ordinal()) {
            currentLayoutPage = layoutPageEnum;
            layoutPage.post(layoutPageEnum);
        } else if (layoutPage.getValue() != layoutPageEnum) {
            layoutPage.post(layoutPageEnum);
        } else {
            layoutPage.post(currentLayoutPage);
        }
    }

    public void setStandardLayout() {
        currentLayoutPage = LayoutPageEnum.LAYOUT_STANDARD;
    }

    public void closePopup() {
        if (lockEnable)
            layoutPage.post(currentLayoutPage);
    }

    public void showSetupPage(SetupPageEnum setupPageEnum) {
        showSetupPage(setupPageEnum, false);
    }

    public void showSetupPage(SetupPageEnum setupPageEnum, boolean reverseToFunction) {
        tagId = setupPageEnum.ordinal();
        if (reverseToFunction) {
            tagId += 100;
        }
        if (setupPageEnum.ordinal() < SetupPageEnum.Spo2.ordinal()) {
            showLayout(LayoutPageEnum.SETUP_HOME_INCUBATOR);
        } else if (setupPageEnum.ordinal() < SetupPageEnum.Spo2Alarm.ordinal()) {
            showLayout(LayoutPageEnum.SETUP_HOME);
        } else {
            showLayout(LayoutPageEnum.SETUP_ALARM);
        }
    }

    public void initializeTimeOut() {
        lockTime = Constant.SCREEN_LOCK_SECOND;
    }

    public void increaseLockTime() {
        if ((!lockScreen.getValue()) && lockEnable) {
            lockTime--;
            if (lockTime <= 0) {
                lockTime = Constant.SCREEN_LOCK_SECOND;
                lockScreen.post(true);
            }
        }
    }

    public void disableLock() {
        lockEnable = false;
    }

    public void enableLock() {
        lockEnable = true;
    }

    public LayoutPageEnum getCurrentLayoutPage() {
        return currentLayoutPage;
    }

    public void setRespUnit() {
        SensorModel co2Model = sensorModelRepository.getSensorModel(SensorModelEnum.Co2);
        SensorModel co2FiModel = sensorModelRepository.getSensorModel(SensorModelEnum.Co2Fi);
        switch (configRepository.getConfig(ConfigEnum.Co2Unit).getValue()) {
            case (0):
                respUnitFunction = UnitUtil::percentageToMmHgString;
                co2Model.unit.set("mmHg");
                co2FiModel.unit.set("mmHg");
                break;
            case (1):
                respUnitFunction = UnitUtil::percentageToKPaString;
                co2Model.unit.set("kPa");
                co2FiModel.unit.set("kPa");
                break;
            default:
                respUnitFunction = UnitUtil::percentageString;
                co2Model.unit.set("%");
                co2FiModel.unit.set("%");
                break;
        }
    }

    public void setNibpUnit() {
        SensorModel nibpModel = sensorModelRepository.getSensorModel(SensorModelEnum.Nibp);
        switch (configRepository.getConfig(ConfigEnum.NibpUnit).getValue()) {
            case (0):
                nibpUnitFunction = UnitUtil::intToString;
                nibpModel.unit.set("mmHg");
                break;
            case (1):
                nibpUnitFunction = UnitUtil::mmHgToKPa;
                nibpModel.unit.set("kPa");
                break;
        }
    }
}