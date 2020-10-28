package com.david.core.util.rely;

import com.david.core.database.entity.BaseEntity;
import com.david.core.database.entity.Co2Entity;
import com.david.core.database.entity.EcgEntity;
import com.david.core.database.entity.IncubatorEntity;
import com.david.core.database.entity.NibpEntity;
import com.david.core.database.entity.Spo2Entity;
import com.david.core.database.entity.WeightEntity;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.util.Constant;

public class RangeUtil {

    public static float getYInRange(float data, int Y_AXIS) {
        float y = data + Y_AXIS / 2f;
        if (y >= Y_AXIS - 1) {
            y = Y_AXIS - 2;
        } else if (y < 2) {
            y = 2;
        }
        return y;
    }

    public static float getYInRange(float data, SensorModelEnum sensorModelEnum, int height, int marginTop, int marginBottom) {
        return getYInRange(data, sensorModelEnum.getCoordinatorLower(), sensorModelEnum.getCoordinatorUpper(),
                sensorModelEnum.getDenominator(), height, marginTop, marginBottom);
    }

    public static float getYInRange(float data, int lowerLimit, int upperLimit, int denominator, int height, int marginTop, int marginBottom) {
        float realData = data / denominator;
        if (realData > upperLimit - 1) {
            realData = upperLimit - 1;
        } else if (realData < lowerLimit) {
            realData = lowerLimit;
        }
        float yRadio = (upperLimit - realData) / (upperLimit - lowerLimit);
        return (height - marginTop - marginBottom) * yRadio + marginTop;
    }

    public static int getSensorData(SensorModelEnum sensorModelEnum, BaseEntity baseEntity) {
        int data = 0;
        switch (sensorModelEnum) {
            case Skin1:
                data = ((IncubatorEntity) baseEntity).skin1;
                break;
            case Skin2:
                data = ((IncubatorEntity) baseEntity).skin2;
                break;
            case Air:
                data = ((IncubatorEntity) baseEntity).air;
                break;
            case Humidity:
                data = ((IncubatorEntity) baseEntity).humidity;
                break;
            case Oxygen:
                data = ((IncubatorEntity) baseEntity).oxygen;
                break;
            case Inc:
                data = ((IncubatorEntity) baseEntity).inc;
                break;
            case Warmer:
                data = ((IncubatorEntity) baseEntity).warmer;
                break;
            case Weight:
                data = ((WeightEntity) baseEntity).weight;
                break;
            case Spo2:
                data = ((Spo2Entity) baseEntity).spo2;
                break;
            case Pr:
                data = ((Spo2Entity) baseEntity).pr;
                break;
            case Pi:
                data = ((Spo2Entity) baseEntity).pi;
                break;
            case Sphb:
                data = ((Spo2Entity) baseEntity).sphb;
                break;
            case Spoc:
                data = ((Spo2Entity) baseEntity).spoc;
                break;
            case Spmet:
                data = ((Spo2Entity) baseEntity).spmet;
                break;
            case Spco:
                data = ((Spo2Entity) baseEntity).spco;
                break;
            case Pvi:
                data = ((Spo2Entity) baseEntity).pvi;
                break;
            case EcgHr:
                data = ((EcgEntity) baseEntity).hr;
                break;
            case EcgRr:
                data = ((EcgEntity) baseEntity).rr;
                break;
            case Co2:
                data = ((Co2Entity) baseEntity).co2;
                break;
            case Co2Rr:
                data = ((Co2Entity) baseEntity).rr;
                break;
            case Co2Fi:
                data = ((Co2Entity) baseEntity).fi;
                break;
            case Nibp:
                data = ((NibpEntity) baseEntity).sys;
                break;
        }
        return data;
    }

    public static void fillData(int sum, long currentLastTime, BaseEntity[] srcList, int[] destList,
                                int interval, SensorModelEnum sensorModelEnum) {
//        for (int index = 0; index < srcList.length; index++) {
//            Spo2Entity baseEntity = (Spo2Entity) srcList[index];
//            LoggerUtil.se(index + " " + baseEntity.spo2 + " " +
//                    TimeUtil.getTimeFromSecond(baseEntity.timeStamp, TimeUtil.Time));
//        }

        int srcListId = 0;
        int destListId = 0;
        for (; destListId < sum; destListId++) {
            long startTimeStamp = currentLastTime - destListId * interval;
            long endTimeStamp = startTimeStamp + interval;
            int data = Constant.SENSOR_NA_VALUE;
            if (srcListId >= srcList.length) {
                break;
            }
            while (srcListId < srcList.length) {
                BaseEntity baseEntity = srcList[srcListId];
                if (baseEntity.timeStamp >= startTimeStamp && baseEntity.timeStamp < endTimeStamp) {
                    //找到数据点，跳出
                    data = getSensorData(sensorModelEnum, baseEntity);
                    srcListId++;
                } else if (baseEntity.timeStamp >= endTimeStamp) {
                    srcListId++;
                } else {
                    break;
                }
            }
//            LoggerUtil.se(destListId + ": " + TimeUtil.getTimeFromSecond(startTimeStamp, TimeUtil.Time)
//                    + " " + TimeUtil.getTimeFromSecond(endTimeStamp, TimeUtil.Time) + " " + data);
            destList[destListId] = data;
        }
        for (; destListId < sum; destListId++) {
            destList[destListId] = Constant.NA_VALUE;
        }
    }
}