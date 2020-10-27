package com.david.incubator.serial.ecg.strategy;

import androidx.arch.core.util.Function;

import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.incubator.serial.ecg.EcgCommandSender;
import com.david.incubator.serial.ecg.EcgResponseCommand;

import javax.inject.Inject;

public class BootUpStrategy implements Function<EcgResponseCommand, Boolean> {

    @Inject
    EcgCommandSender ecgCommandSender;
    @Inject
    ConfigRepository configRepository;

    @Inject
    public BootUpStrategy() {
    }

    @Override
    public Boolean apply(EcgResponseCommand command) {
        ecgCommandSender.setBootUp();
        ecgCommandSender.setEnableResp();
        ecgCommandSender.setUser();
        ecgCommandSender.setPace(configRepository.getConfig(ConfigEnum.EcgPaceCheck).getValue() == 1);
        ecgCommandSender.setVF();
        ecgCommandSender.setTrap(configRepository.getConfig(ConfigEnum.EcgTrapMode).getValue());

        int filterMode = configRepository.getConfig(ConfigEnum.EcgFilterMode).getValue();
        switch (filterMode) {
            case (0):
            case (1):
                ecgCommandSender.setMonitor(true);
                ecgCommandSender.setFilter(filterMode);
                break;
            default:
                ecgCommandSender.setMonitor(false);
                ecgCommandSender.setFilter(filterMode);
                break;
        }

        ecgCommandSender.setLead5(configRepository.getConfig(ConfigEnum.EcgLeadSetting).getValue() == 0);
        return false;
    }
}