package com.david.incubator.ui.system.print;

import com.david.core.enumeration.CommandEnum;
import com.david.core.serial.BaseCommand;
import com.david.core.util.LoggerUtil;

public class SystemPrintCommand extends BaseCommand {

    private final int commandId;
    private final String text;

    public SystemPrintCommand(int commandId, String text) {
        super(CommandEnum.NoResponse);
        this.commandId = commandId;
        this.text = text;
    }

    @Override
    protected String getCommandId() {
        return commandId + getClass().getSimpleName();
    }

    @Override
    public byte[] getRequest() {
        byte[] request;
        try {
            request = text.getBytes("GBK");
        } catch (Exception e) {
            LoggerUtil.e(e);
            request = new byte[0];
        }
        return request;
    }
}
