package com.david.incubator.serial.wake.command;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.incubator.BaseIncubatorCommand;

public class AmbientCommand extends BaseIncubatorCommand {

    private int HUM;
    private int TEMP;
    private int STATE;

    public AmbientCommand() {
        super(CommandEnum.Repeated);
    }

    @Override
    protected String getRequestString() {
        return "AMBIENT";
    }

    public int getHUM() {
        return HUM;
    }

    public void setHUM(int HUM) {
        this.HUM = HUM;
    }

    public int getTEMP() {
        return TEMP;
    }

    public void setTEMP(int TEMP) {
        this.TEMP = TEMP;
    }

    public int getSTATE() {
        return STATE;
    }

    public void setSTATE(int STATE) {
        this.STATE = STATE;
    }
}