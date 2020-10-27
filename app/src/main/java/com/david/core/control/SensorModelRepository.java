package com.david.core.control;

import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SensorModelRepository {

    private final SensorModel[] viewModelList;

    @Inject
    public SensorModelRepository() {
        SensorModelEnum[] sensorModelEnums = SensorModelEnum.values();

        viewModelList = new SensorModel[sensorModelEnums.length];

        for (int index = 0; index < sensorModelEnums.length; index++) {
            SensorModel sensorModel = new SensorModel();
            sensorModel.setSensorModel(sensorModelEnums[index]);
            viewModelList[index] = sensorModel;
        }
    }

    public SensorModel getSensorModel(SensorModelEnum sensorModelEnum) {
        return viewModelList[sensorModelEnum.ordinal()];
    }

    public void setText() {
        SensorModelEnum[] sensorModelEnums = SensorModelEnum.values();
        for (int index = 0; index < sensorModelEnums.length; index++) {
            SensorModel sensorModel = viewModelList[sensorModelEnums[index].ordinal()];
            sensorModel.setSensorText(sensorModelEnums[index]);
        }
    }
}