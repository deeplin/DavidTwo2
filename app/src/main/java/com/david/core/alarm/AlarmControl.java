package com.david.core.alarm;

import com.david.core.control.SensorModelRepository;
import com.david.core.database.DaoControl;
import com.david.core.database.entity.AlarmEntity;
import com.david.core.model.Spo2Model;
import com.david.core.serial.BaseCommand;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.serial.incubator.command.repeated.AlarmListCommand;
import com.david.core.util.ILifeCycle;
import com.david.core.util.LazyLiveData;
import com.david.core.util.LoggerUtil;
import com.david.core.util.TimeUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

//1. 来源 I: Incubator S: Spo2 C:Co2 E:Ecg N:Nibp P:Printer W:Wake
//2. 类别 T: 技术 P: 生理
//3. 优先级：H：高级 M：中级 L：低级 I：信息

@Singleton
public class AlarmControl implements ILifeCycle {

    @Inject
    AlarmRepository alarmRepository;
    @Inject
    AlarmListCommand alarmListCommand;
    @Inject
    AlarmEntity alarmEntity;
    @Inject
    DaoControl daoControl;
    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    Spo2Model spo2Model;

    public final LazyLiveData<Boolean> technicalUpdate = new LazyLiveData<>();
    public final LazyLiveData<Boolean> physiologicalUpdate = new LazyLiveData<>();

    private final List<AlarmModel> technicalAlarmList = new ArrayList<>();
    private final List<AlarmModel> physiologicalAlarmList = new ArrayList<>();
    private boolean technicalChanged;
    private boolean physiologicalChanged;
    private long currentVersion;

    @Inject
    AlarmControl() {
        technicalChanged = false;
        physiologicalChanged = false;
        currentVersion = 1;
    }

    @Override
    public void attach() {
        alarmListCommand.setCallback(this::reportAlarmFromCabin);
    }

    @Override
    public void detach() {
    }

    private void reportAlarmFromCabin(boolean status, BaseCommand baseCommand) {
        String alarmString = ((AlarmListCommand) baseCommand).getAlert();
        if (alarmString != null && alarmString.length() > 0) {
            String[] alertWords = alarmString.split(":");
            reportAlarmFromCabin(alertWords);
        } else {
            reportAlarmFromCabin(null);
        }
    }

    private void reportAlarmFromCabin(String[] alarmWords) {
        currentVersion++;
        technicalChanged = false;
        physiologicalChanged = false;
        if (alarmWords != null && alarmWords.length > 0) {
            for (String alarmWord : alarmWords) {
                AlarmModel alarmModel = alarmRepository.getAlarmMap().get(alarmWord);
                if (alarmModel == null) {
                    LoggerUtil.se("Unsupported alarm: " + alarmWord);
                    continue;
                }
                reportAlarm(alarmModel);
            }
        }
        checkAlarmInMap();
        if (technicalChanged) {
            fillTechnicalAlarm();
            technicalUpdate.notifyChange();
        }
        if (physiologicalChanged) {
            fillPhysiologicalAlarm();
            physiologicalUpdate.notifyChange();
        }
    }

    private void reportAlarm(AlarmModel alarmModel) {
        if (alarmModel.getVersion() < currentVersion - 1) {
            alarmModel.setStartTime(TimeUtil.getCurrentTimeInSecond());
            if (alarmModel.isTechnicalMode()) {
                technicalChanged = true;
            } else {
                physiologicalChanged = true;
            }
            alarmEntity.timeStamp = alarmModel.getStartTime();
            alarmEntity.alarmId = alarmModel.getAlarmId();
            daoControl.getAlarmDaoOperation().insert(alarmEntity);
        }
        alarmModel.setVersion(currentVersion);
    }

    private void checkAlarmInMap() {
        Iterator<Map.Entry<String, AlarmModel>> iterator = alarmRepository.getAlarmMap().entrySet().iterator();
        while (iterator.hasNext()) {
            AlarmModel alarmModel = iterator.next().getValue();
            if (alarmModel.getVersion() == currentVersion - 1) {
                if (alarmModel.isTechnicalMode()) {
                    technicalChanged = true;
                } else {
                    physiologicalChanged = true;
                }
            }
        }
    }

    private void fillTechnicalAlarm() {
        technicalAlarmList.clear();
        for (AlarmModel alarmModel : alarmRepository.getTechnicalAlarmList()) {
            if (alarmModel.getVersion() == currentVersion) {
                technicalAlarmList.add(alarmModel);
            }
        }
    }

    private void fillPhysiologicalAlarm() {
        physiologicalAlarmList.clear();
        for (AlarmModel alarmModel : alarmRepository.getPhysiologicalAlarmList()) {
            if (alarmModel.getVersion() == currentVersion) {
                physiologicalAlarmList.add(alarmModel);
            }
        }
    }

    public AlarmModel getTopTechnicalAlarm() {
        if (technicalAlarmList.size() > 0) {
            return technicalAlarmList.get(0);
        }
        return null;
    }

    public AlarmModel getTopPhysiologicalAlarm() {
        if (physiologicalAlarmList.size() > 0) {
            return physiologicalAlarmList.get(0);
        }
        return null;
    }

    public int getTechnicalSize() {
        return technicalAlarmList.size();
    }

    public int getPhysiologicalSize() {
        return physiologicalAlarmList.size();
    }

    public List<AlarmModel> getTechnicalAll() {
        return technicalAlarmList;
    }

    public List<AlarmModel> getPhysiologicalAll() {
        return physiologicalAlarmList;
    }

    public boolean isAlarm() {
        return technicalAlarmList.size() > 0 || physiologicalAlarmList.size() > 0;
    }
}