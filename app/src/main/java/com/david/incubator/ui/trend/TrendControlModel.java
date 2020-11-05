package com.david.incubator.ui.trend;

import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.util.Constant;
import com.david.core.util.LazyLiveData;
import com.david.core.util.ListUtil;
import com.david.core.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TrendControlModel {

    @Inject
    ConfigRepository configRepository;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    ModuleHardware moduleHardware;

    public final LazyLiveData<Integer> trendCategory = new LazyLiveData<>();

    public final LazyLiveData<Integer>[] intervalArray;

    private long endTime;
    private long currentLastTime;

    private int samplePoint;

    public final List<SensorModelEnum> activeIncubatorItemList;

    @Inject
    TrendControlModel() {
        intervalArray = new LazyLiveData[4];
        intervalArray[0] = new LazyLiveData<>();
        intervalArray[1] = new LazyLiveData<>();
        intervalArray[2] = new LazyLiveData<>();
        intervalArray[3] = new LazyLiveData<>();
        activeIncubatorItemList = new ArrayList<>();
    }

    public void init() {
        endTime = TimeUtil.getRoundTimeInSecond();
        intervalArray[0].set(0);
        intervalArray[1].set(0);
        intervalArray[2].set(0);
        intervalArray[3].set(0);
        trendCategory.set(0);
        samplePoint = 0;

        configRepository.getConfig(ConfigEnum.TrendIncubatorItem0).set(0);
        configRepository.getConfig(ConfigEnum.TrendIncubatorItem1).set(1);
        configRepository.getConfig(ConfigEnum.TrendIncubatorItem2).set(2);

        activeIncubatorItemList.clear();
        activeIncubatorItemList.add(SensorModelEnum.Skin1);
        activeIncubatorItemList.add(SensorModelEnum.Skin2);
        if (incubatorModel.isCabin()) {
            activeIncubatorItemList.add(SensorModelEnum.Air);
            activeIncubatorItemList.add(SensorModelEnum.Inc);
            if (moduleHardware.isActive(ModuleEnum.Hum))
                activeIncubatorItemList.add(SensorModelEnum.Humidity);
            if (moduleHardware.isActive(ModuleEnum.Oxygen))
                activeIncubatorItemList.add(SensorModelEnum.Oxygen);
        } else {
            activeIncubatorItemList.add(SensorModelEnum.Warmer);
        }
    }

    public void initLastTime() {
        currentLastTime = endTime;
    }

    public LazyLiveData<Integer> getIntervalId() {
        return intervalArray[trendCategory.getValue()];
    }

    public int getIntervalValue() {
        int intervalId = getIntervalId().getValue();
        if (chooseIncubator()) {
            return getCycleInSecond(ListUtil.incubatorIntervalList.get(intervalId));
        } else {
            return getCycleInSecond(ListUtil.monitorIntervalList.get(intervalId));
        }
    }

    public boolean chooseIncubator() {
        return TrendCategoryEnum.Incubator.ordinal() == trendCategory.getValue();
    }

    public TrendCategoryEnum getTrendCategoryEnum() {
        return TrendCategoryEnum.values()[trendCategory.getValue()];
    }

    public LazyLiveData<Integer> getItemOption(int itemId) {
        switch (trendCategory.getValue()) {
            case 0:
                if (itemId == 0) {
                    return configRepository.getConfig(ConfigEnum.TrendIncubatorItem0);
                } else if (itemId == 1) {
                    return configRepository.getConfig(ConfigEnum.TrendIncubatorItem1);
                } else {
                    return configRepository.getConfig(ConfigEnum.TrendIncubatorItem2);
                }
            case 2:
                if (itemId == 0) {
                    return configRepository.getConfig(ConfigEnum.TrendSpo2Item0);
                } else if (itemId == 1) {
                    return configRepository.getConfig(ConfigEnum.TrendSpo2Item1);
                } else {
                    return configRepository.getConfig(ConfigEnum.TrendSpo2Item2);
                }
            default:
                return null;
        }
    }

    public SensorModelEnum getItemSensorModel(int itemId) {
        switch (trendCategory.getValue()) {
            case 0:
                LazyLiveData<Integer> incubatorLazyLiveData = getItemOption(itemId);
                return activeIncubatorItemList.get(incubatorLazyLiveData.getValue());
            case 1:
                return SensorModelEnum.values()[itemId + SensorModelEnum.EcgHr.ordinal()];
            case 2:
                LazyLiveData<Integer> spo2LazyLiveData = getItemOption(itemId);
                switch (spo2LazyLiveData.getValue()) {
                    case (0):
                        return SensorModelEnum.Spo2;
                    case (1):
                        return SensorModelEnum.Pr;
                    case (2):
                        return SensorModelEnum.Pi;
                    default:
                        configRepository.getActiveSpo2Module().get(spo2LazyLiveData.getValue());
                }
            default:
                return SensorModelEnum.values()[itemId + SensorModelEnum.Co2.ordinal()];
        }
    }

    public long getLastTime() {
        return currentLastTime;
    }

    public void setLastTime(long lastTime) {
        this.currentLastTime = lastTime;
    }

    public boolean isLastPage() {
        return currentLastTime >= endTime;
    }

    private static int getCycleInSecond(String intervalText) {
        int interval = Integer.parseInt(intervalText.substring(0, intervalText.length() - 1));
        if (intervalText.charAt(intervalText.length() - 1) == 'm') {
            interval *= 60.0 / Constant.DOT_PER_CHART;
        } else if (intervalText.charAt(intervalText.length() - 1) == 'h') {
            interval *= 3600 / Constant.DOT_PER_CHART;
        } else {
            interval /= Constant.DOT_PER_CHART;
        }
        return interval;
    }

    public int getSamplePoint() {
        return samplePoint;
    }

    public void setSamplePoint(int samplePoint) {
        this.samplePoint = samplePoint;
    }
}