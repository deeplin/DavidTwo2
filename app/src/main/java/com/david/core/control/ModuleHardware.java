package com.david.core.control;

import com.david.core.enumeration.ModuleEnum;
import com.david.core.util.LazyLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ModuleHardware {

    public final LazyLiveData<Integer>[] moduleArray;
    private String deviceModel;

    @Inject
    public ModuleHardware() {
        ModuleEnum[] moduleEnums = ModuleEnum.values();
        moduleArray = new LazyLiveData[moduleEnums.length];
        for (int index = 0; index < moduleEnums.length; index++) {
            moduleArray[index] = new LazyLiveData<>(0);
        }
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void post(ModuleEnum moduleEnum, int value) {
        moduleArray[moduleEnum.ordinal()].post(value);
    }

    public LazyLiveData<Integer> getModule(ModuleEnum moduleEnum) {
        return moduleArray[moduleEnum.ordinal()];
    }

    public boolean isInstalled(ModuleEnum moduleEnum) {
        return moduleArray[moduleEnum.ordinal()].getValue() != 0;
    }

    public boolean isActive(ModuleEnum moduleEnum) {
        return moduleArray[moduleEnum.ordinal()].getValue() == 2;
    }

    public boolean isInActive(ModuleEnum moduleEnum) {
        return moduleArray[moduleEnum.ordinal()].getValue() == 1;
    }
}