package com.david.incubator.serial.ecg;

import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.EcgChannelEnum;
import com.david.incubator.serial.ecg.command.BootUpCommand;
import com.david.incubator.serial.ecg.command.ComputeLeadCommand;
import com.david.incubator.serial.ecg.command.EnableRespCommand;
import com.david.incubator.serial.ecg.command.FilterCommand;
import com.david.incubator.serial.ecg.command.MonitorCommand;
import com.david.incubator.serial.ecg.command.PaceCommand;
import com.david.incubator.serial.ecg.command.RespSourceCommand;
import com.david.incubator.serial.ecg.command.SetLeadCommand;
import com.david.incubator.serial.ecg.command.TrapCommand;
import com.david.incubator.serial.ecg.command.UserCommand;
import com.david.incubator.serial.ecg.command.VFCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EcgCommandSender {

    @Inject
    EcgCommandControl ecgCommandControl;
    @Inject
    ConfigRepository configRepository;

    @Inject
    public EcgCommandSender() {
    }

    public void setUser() {
        UserCommand userCommand = new UserCommand();
        ecgCommandControl.produce(userCommand);
    }

    public void setEnableResp() {
        EnableRespCommand enableRespCommand = new EnableRespCommand();
        ecgCommandControl.produce(enableRespCommand);
    }

    public void setBootUp() {
        BootUpCommand bootUpCommand = new BootUpCommand();
        ecgCommandControl.produce(bootUpCommand);
    }

    public void setMonitor(boolean isMonitor) {
        MonitorCommand monitorCommand = new MonitorCommand(isMonitor);
        ecgCommandControl.produce(monitorCommand);
    }

    public void setFilter(int mode) {
        FilterCommand filterCommand = new FilterCommand(mode);
        ecgCommandControl.produce(filterCommand);
    }

    public void setTrap(int mode) {
        TrapCommand trapCommand = new TrapCommand(mode);
        ecgCommandControl.produce(trapCommand);
    }

    public void setPace(boolean status) {
        PaceCommand paceCommand = new PaceCommand(status);
        ecgCommandControl.produce(paceCommand);
    }

    public void setVF() {
        VFCommand vfCommand = new VFCommand();
        ecgCommandControl.produce(vfCommand);
    }

    public void setRespSource(int source) {
        RespSourceCommand respSourceCommand = new RespSourceCommand(source);
        ecgCommandControl.produce(respSourceCommand);
    }

    public void setLead5(boolean lead5) {
        SetLeadCommand setLeadCommand = new SetLeadCommand();
        setLeadCommand.setLead(lead5);
        ecgCommandControl.produce(setLeadCommand);

        int leadSetting = configRepository.getConfig(ConfigEnum.EcgLeadSetting).getValue();
        EcgChannelEnum channelMode;
        if (leadSetting == 0) {
            channelMode = EcgChannelEnum.values()[configRepository.getConfig(ConfigEnum.Ecg0).getValue()];
        } else {
            channelMode = EcgChannelEnum.values()[configRepository.getConfig(ConfigEnum.Ecg2).getValue()];
        }
        setComputeLead(channelMode);
    }

    public void setComputeLead(EcgChannelEnum channelMode) {
        ComputeLeadCommand computeLeadCommand = new ComputeLeadCommand();
        switch (channelMode) {
            case I:
                computeLeadCommand.setLead(0);
                break;
            case II:
                computeLeadCommand.setLead(1);
                break;
            case III:
                computeLeadCommand.setLead(2);
                break;
            case aVR:
                computeLeadCommand.setLead(9);
                break;
            case aVL:
                computeLeadCommand.setLead(10);
                break;
            case aVF:
                computeLeadCommand.setLead(11);
                break;
            case V:
                computeLeadCommand.setLead(3);
                break;
        }
        ecgCommandControl.produce(computeLeadCommand);
    }
}