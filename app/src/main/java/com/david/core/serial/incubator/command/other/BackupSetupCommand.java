package com.david.core.serial.incubator.command.other;

import com.david.core.serial.incubator.BaseIncubatorCommand;

public class BackupSetupCommand extends BaseIncubatorCommand {

    public BackupSetupCommand() {
        super();
    }

    @Override
    protected String getRequestString() {
        return "BACKUP_SET";
    }
}