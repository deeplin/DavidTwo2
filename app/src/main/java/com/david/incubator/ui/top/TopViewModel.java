package com.david.incubator.ui.top;

import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.alarm.AlarmControl;
import com.david.core.alarm.AlarmModel;
import com.david.core.alarm.AlarmUtil;
import com.david.core.database.LastUser;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.enumeration.BatteryEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.rely.FormatUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.IntervalUtil;
import com.david.core.util.LazyLiveData;
import com.david.core.util.TimeUtil;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.functions.Consumer;

@Singleton
public class TopViewModel implements ILifeCycle {

    @Inject
    IntervalUtil intervalUtil;
    @Inject
    AlarmControl alarmControl;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    LastUser lastUser;
    @Inject
    public SystemModel systemModel;

    public final LazyLiveData<String> userId = new LazyLiveData<>();
    public final LazyLiveData<String> userName = new LazyLiveData<>();

    public final LazyLiveData<String> ambientString = new LazyLiveData<>();
    public final LazyLiveData<Integer> technicalAlarmTitleColor = new LazyLiveData<>();
    public final LazyLiveData<String> technicalAlarmString = new LazyLiveData<>();
    public final LazyLiveData<Integer> physiologicalAlarmTitleColor = new LazyLiveData<>();
    public final LazyLiveData<String> physiologicalAlarmString = new LazyLiveData<>();
    public final LazyLiveData<String> overheatString = new LazyLiveData<>();

    public final LazyLiveData<String> date = new LazyLiveData<>();
    public final LazyLiveData<String> time = new LazyLiveData<>();

    public final LazyLiveData<Integer> batteryImageId = new LazyLiveData<>(R.mipmap.battery5);
    private final LazyLiveData<BatteryEnum> batteryMode = new LazyLiveData<>(BatteryEnum.Full);

    public final LazyLiveData<Boolean> showSoundPause = new LazyLiveData<>(false);
    public final LazyLiveData<String> muteAlarmField = new LazyLiveData<>();

    private final Consumer<Long> timeConsumer;

    private int alarmTime;

    private final Observer<Boolean> technicalAlarmUpdateCallback;
    private final Observer<Boolean> physiologicalAlarmUpdateCallback;
    private final Observer<Boolean> ohTestCallback;
    private final Observer<Boolean> lastUserCallback;
    private final Observer<Boolean> gpioCallback;
    private final Observer<Integer> vuCallback;
    private final Observer<BatteryEnum> batteryModeCallback;
    private final Observer<Integer> ambientObserver;
    private final Observer<Integer> muteCallback;

    private long batteryStartTime;
    private AlarmModel topAlarmModel;

    @Inject
    public TopViewModel() {
        batteryStartTime = TimeUtil.getCurrentTimeInSecond() + 300;

        physiologicalAlarmUpdateCallback = aBoolean -> {
            AlarmModel alarmModel = alarmControl.getTopPhysiologicalAlarm();
            displayPhysiologicalTitle(alarmModel);

            if (topAlarmModel != null && (!topAlarmModel.isTechnicalMode())) {
                if (!Objects.equals(alarmModel, topAlarmModel)) {
                    topAlarmModel = null;
                    stopSoundPause();
                    muteAlarmField.post(null);
                }
            }

            if (!alarmControl.isAlarm()) {
                clearAlarm();
            }
        };

        technicalAlarmUpdateCallback = aBoolean -> {
            AlarmModel alarmModel = alarmControl.getTopTechnicalAlarm();
            displayTechnicalTitle(alarmModel);

            if (alarmModel != null && Objects.equals(alarmModel.getAlarmWord(), AlarmWordEnum.SYS_TANK.toString())) {
                muteAlarm();
            }

            if (topAlarmModel != null && topAlarmModel.isTechnicalMode()) {
                if (!Objects.equals(alarmModel, topAlarmModel)) {
                    topAlarmModel = null;
                    stopSoundPause();
                    muteAlarmField.post(null);
                }
            }

            if (!alarmControl.isAlarm()) {
                clearAlarm();
            }
        };

        ohTestCallback = aBoolean -> {
            if (aBoolean) {
                overheatString.set(ContextUtil.getString(R.string.overheat_experiment));
            } else {
                overheatString.set(null);
            }
            incubatorCommandSender.getCtrlGet();
        };

        lastUserCallback = aBoolean -> {
            if (lastUser.getUserEntity() != null) {
                userId.set(lastUser.getUserEntity().userId);
                userName.set(lastUser.getUserEntity().name);
            } else {
                userId.set(ContextUtil.getString(R.string.default_user));
                userName.set(null);
            }
            clearAlarm();
        };

        gpioCallback = aBoolean -> {
            if (aBoolean) {
                muteAlarm();
            }
        };

        timeConsumer = aLong -> {
            synchronized (TopViewModel.this) {
                displayCurrentTime(aLong);
                if (alarmTime > 0) {
                    muteAlarmField.post(String.format(Locale.US, "%ds", alarmTime));
                    showSoundPause.post(true);
                    alarmTime--;
                } else if (alarmTime == 0) {
                    clearAlarm();
                    alarmTime--;
                }
            }
        };

        vuCallback = vu -> {
            if (Objects.equals(incubatorModel.systemMode.getValue(), SystemEnum.Transit)) {
                return;
            }

            long now = TimeUtil.getCurrentTimeInSecond();
            if (now < batteryStartTime) {
                batteryMode.set(BatteryEnum.Charging);
                setBatteryCharging();
                return;
            }

            if (isBatteryAlert()) {
                batteryMode.set(BatteryEnum.Failure);
                return;
            }

            if (vu < 9000) {
                batteryMode.set(BatteryEnum.Charging);
                setBatteryCharging();
            } else if (vu > 9400) {
                batteryMode.set(BatteryEnum.Full);
            } else {
                if (Objects.equals(batteryMode.getValue(), BatteryEnum.Charging)) {
                    setBatteryCharging();
                } else if (Objects.equals(batteryMode.getValue(), BatteryEnum.Failure)) {
                    batteryMode.set(BatteryEnum.Failure);
                }
            }
        };

        batteryModeCallback = batteryMode -> {
            if (Objects.equals(batteryMode, BatteryEnum.Full)) {
                batteryImageId.set(R.mipmap.battery5);
            } else if (Objects.equals(batteryMode, BatteryEnum.Failure)) {
                batteryImageId.set(R.mipmap.battery_fault);
            }
        };

        ambientObserver = integer -> ambientString.set(FormatUtil.formatValueUnit(SensorModelEnum.Skin1, incubatorModel.ambientTemp.getValue())
                + "     " + FormatUtil.formatValueUnit(SensorModelEnum.Humidity, incubatorModel.ambientHumidity.getValue()));

        muteCallback = integer -> {
            if (integer == 1) {
                muteAlarm();
            }
        };
    }

    @Override
    public void attach() {
        displayCurrentTime(0);
        alarmControl.physiologicalUpdate.observeForever(physiologicalAlarmUpdateCallback);
        alarmControl.technicalUpdate.observeForever(technicalAlarmUpdateCallback);
        incubatorModel.ohTest.observeForever(ohTestCallback);
        incubatorModel.gpio.observeForever(gpioCallback);
        incubatorModel.VU.observeForever(vuCallback);
        incubatorModel.E1.observeForever(muteCallback);
        batteryMode.observeForever(batteryModeCallback);
        lastUser.updated.observeForever(lastUserCallback);
        intervalUtil.addSecondConsumer(TopViewModel.class, timeConsumer);

        incubatorModel.ambientTemp.observeForever(ambientObserver);
        incubatorModel.ambientHumidity.observeForever(ambientObserver);
    }

    @Override
    public void detach() {
        incubatorModel.ambientTemp.removeObserver(ambientObserver);
        incubatorModel.ambientHumidity.removeObserver(ambientObserver);

        intervalUtil.removeSecondConsumer(TopViewModel.class);
        lastUser.updated.removeObserver(lastUserCallback);
        batteryMode.removeObserver(batteryModeCallback);
        incubatorModel.VU.removeObserver(vuCallback);
        incubatorModel.gpio.removeObserver(gpioCallback);
        incubatorModel.ohTest.removeObserver(ohTestCallback);
        incubatorModel.E1.removeObserver(muteCallback);
        alarmControl.technicalUpdate.removeObserver(technicalAlarmUpdateCallback);
        alarmControl.physiologicalUpdate.removeObserver(physiologicalAlarmUpdateCallback);
    }

    private int sequenceId = -1;

    private void setBatteryCharging() {
        sequenceId++;
        int imageId;
        switch (sequenceId % 5) {
            case (0):
                imageId = R.mipmap.battery0;
                break;
            case (1):
                imageId = R.mipmap.battery1;
                break;
            case (2):
                imageId = R.mipmap.battery2;
                break;
            case (3):
                imageId = R.mipmap.battery3;
                break;
            default:
                imageId = R.mipmap.battery4;
                break;
        }
        batteryImageId.post(imageId);
    }

    private void displayCurrentTime(long aLong) {
        this.date.post(String.format(Locale.US, "%s",
                TimeUtil.getCurrentTime(TimeUtil.Date0)));
        this.time.post(String.format(Locale.US, "%s",
                TimeUtil.getCurrentTime(TimeUtil.Time)));
    }

    private void displayTechnicalTitle(AlarmModel alarmModel) {
        if (alarmModel != null) {
            String alarmWord = alarmModel.getAlarmWord();
            technicalAlarmString.set(String.format(Locale.US, "%s (%d)",
                    AlarmUtil.getAlarmString(alarmWord), alarmControl.getTechnicalSize()));
            technicalAlarmTitleColor.set(alarmModel.getAlarmPriorityEnum().getRightColorId());
        } else {
            technicalAlarmString.set(null);
            technicalAlarmTitleColor.set(null);
        }
    }

    private void displayPhysiologicalTitle(AlarmModel alarmModel) {
        if (alarmModel != null) {
            String alarmWord = alarmModel.getAlarmWord();
            physiologicalAlarmString.set(String.format(Locale.US, "%s (%d)",
                    AlarmUtil.getAlarmString(alarmWord), alarmControl.getPhysiologicalSize()));
            physiologicalAlarmTitleColor.set(alarmModel.getAlarmPriorityEnum().getLeftColorId());
        } else {
            physiologicalAlarmString.set(null);
            physiologicalAlarmTitleColor.set(null);
        }
    }

    public synchronized void clearAlarm() {
        stopSoundPause();
        muteAlarmField.post(null);
        alarmTime = Constant.NA_VALUE;
    }

    public synchronized void muteAlarm() {
        if (alarmControl.isAlarm() && alarmTime < 0) {
            AlarmModel physiologicalModel = alarmControl.getTopPhysiologicalAlarm();
            AlarmModel technicalModel = alarmControl.getTopTechnicalAlarm();

            if (physiologicalModel != null && technicalModel != null) {
                if (physiologicalModel.getAlarmId() < technicalModel.getAlarmId()) {
                    topAlarmModel = physiologicalModel;
                } else {
                    topAlarmModel = technicalModel;
                }
            } else if (physiologicalModel != null) {
                topAlarmModel = physiologicalModel;
            } else if (technicalModel != null) {
                topAlarmModel = technicalModel;
            } else {
                return;
            }

            alarmTime = topAlarmModel.getAlarmLastingTime();
            if (alarmTime > 0) {
                incubatorCommandSender.setMute(topAlarmModel.getAlarmWord(), alarmTime, (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        if (!alarmControl.isAlarm()) {
                            stopSoundPause();
                        }
                    } else {
                        stopSoundPause();
                    }
                });
            }
        }
    }

    private boolean isBatteryAlert() {
        List<AlarmModel> alarmModels = alarmControl.getTechnicalAll();
        for (int index = 0; index < alarmModels.size(); index++) {
            AlarmModel alarmModel = alarmModels.get(index);
            if (alarmModel != null && (Objects.equals(alarmModel.getAlarmWord(), AlarmWordEnum.SYS_UPS.toString())
                    || Objects.equals(alarmModel.getAlarmWord(), AlarmWordEnum.SYS_BAT.toString()))) {
                return true;
            }
        }
        return false;
    }

    private synchronized void stopSoundPause() {
        alarmTime = Constant.NA_VALUE;
        showSoundPause.post(false);
    }
}