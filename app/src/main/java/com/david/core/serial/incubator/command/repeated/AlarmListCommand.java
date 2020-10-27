package com.david.core.serial.incubator.command.repeated;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.incubator.BaseIncubatorCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AlarmListCommand extends BaseIncubatorCommand {

    private String alert;

    @Inject
    public AlarmListCommand() {
        super(CommandEnum.Repeated);
    }

    @Override
    protected String getRequestString() {
        return "ALERT";
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getAlert() {
        return alert;
    }
}