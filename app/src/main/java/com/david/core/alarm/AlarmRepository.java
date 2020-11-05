package com.david.core.alarm;

import com.david.core.enumeration.AlarmGroupEnum;
import com.david.core.enumeration.AlarmPriorityEnum;
import com.david.core.enumeration.AlarmWordEnum;
import com.david.core.util.FileUtil;
import com.david.core.util.LoggerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AlarmRepository {

    private final Map<String, AlarmModel> alarmMap = new HashMap<>();
    private final List<AlarmModel> technicalAlarmList = new ArrayList<>();
    private final List<AlarmModel> physiologicalAlarmList = new ArrayList<>();

    @Inject
    public AlarmRepository() {
        loadAsset();
    }

    public AlarmModel getAlarmModel(AlarmWordEnum alarmWordEnum) {
        return alarmMap.get(alarmWordEnum.toString());
    }

    //    //触发下位机报警
    public synchronized void produceAlarmFromAndroid(AlarmWordEnum alarmWordEnum, boolean status) {

//        AlarmModel alarmModel = alarmMap.get(alarmWordEnum.toString());
//        if (alarmModel != null) {
//            produceAlarmFromAndroid(alarmModel, status);
//        } else {
//            LoggerUtil.se("Unknown alarm word: " + alarmWordEnum.getAlarmWord() + " " + status);
//        }
    }

    public Map<String, AlarmModel> getAlarmMap() {
        return alarmMap;
    }

    public List<AlarmModel> getTechnicalAlarmList() {
        return technicalAlarmList;
    }

    public List<AlarmModel> getPhysiologicalAlarmList() {
        return physiologicalAlarmList;
    }

    private void loadAsset() {
        try {
            String alarmAssert = FileUtil.readTextFileFromAssets("Alarm.txt");
            String[] alarmArray = alarmAssert.split("\t");

            int alarmId = 0;

            for (int index = 0; index < alarmArray.length; index++) {
                String alarmString = alarmArray[index].trim();
                String[] alarmItems = alarmString.split(" ");
                AlarmGroupEnum alarmGroupEnum;
                try {
                    alarmGroupEnum = AlarmGroupEnum.valueOf(alarmItems[0]);
                } catch (Exception e) {
                    continue;
                }

                AlarmModel alarmModel = new AlarmModel();
                alarmModel.setAlarmId(alarmId);
                alarmModel.setAlarmGroupEnum(alarmGroupEnum);

                boolean isTechnicalMode = Objects.equals("T", alarmItems[1]);
                alarmModel.setTechnicalMode(isTechnicalMode);
                if (isTechnicalMode) {
                    technicalAlarmList.add(alarmModel);
                } else {
                    physiologicalAlarmList.add(alarmModel);
                }

                AlarmPriorityEnum alarmPriorityEnum = AlarmPriorityEnum.valueOf(alarmItems[2]);
                alarmModel.setAlarmPriorityEnum(alarmPriorityEnum);

                String alarmWord = alarmItems[3];
                alarmModel.setAlarmWord(alarmWord);

                if (alarmItems.length > 4) {
                    String alarmCategory = alarmItems[4];
                    alarmModel.setCategory(alarmCategory);
                    alarmModel.setBitOffset(Integer.parseInt(alarmItems[5]));
                }

                alarmModel.setAlarmLastingTime(AlarmUtil.getAlarmLastingTime(alarmGroupEnum, alarmWord));

                alarmMap.put(alarmModel.getAlarmWord(), alarmModel);
                alarmId++;
            }
        } catch (Exception e) {
            LoggerUtil.e(e);
        }
    }

    public String getAlarmStringById(int alarmId) {
        for (AlarmModel alarmModel : alarmMap.values()) {
            if (alarmModel.getAlarmId() == alarmId) {
                return AlarmUtil.getAlarmString(alarmModel.getAlarmWord());
            }
        }
        return String.valueOf(alarmId);
    }
}