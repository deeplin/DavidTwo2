package com.david.core.serial;

import com.david.core.enumeration.CommandEnum;
import com.david.core.util.Constant;

import io.reactivex.rxjava3.functions.BiConsumer;

public abstract class BaseCommand {

    private CommandEnum commandEnum;
    private int executeTime;
    private BiConsumer<Boolean, BaseCommand> callback;

    public BaseCommand() {
        this(CommandEnum.HasResponse);
    }

    public BaseCommand(CommandEnum commandEnum) {
        this.commandEnum = commandEnum;
        executeTime = Constant.MAX_SERIAL_EXECUTE_TIME;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseCommand) {
            return getCommandId().equals(((BaseCommand) obj).getCommandId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getCommandId().hashCode();
    }

    public abstract byte[] getRequest();

    protected String getCommandId() {
        return getClass().getSimpleName();
    }

    public boolean hasResponse() {
        return !commandEnum.equals(CommandEnum.NoResponse);
    }

    public boolean underExecuteTime() throws Throwable {
        if (commandEnum.equals(CommandEnum.Critical) || commandEnum.equals(CommandEnum.Repeated)) {
            return true;
        }
        executeTime--;
        if (executeTime >= 0) {
            return true;
        }
        executeCallback(false);
        return false;
    }

    public synchronized void setCallback(BiConsumer<Boolean, BaseCommand> callback) {
        this.callback = callback;
    }

    public synchronized BiConsumer<Boolean, BaseCommand> getCallback() {
        return callback;
    }

    public synchronized void executeCallback(boolean status) throws Throwable {
        if (callback != null) {
            callback.accept(status, this);
        }
    }
    public int getRequestLength() {
        return getRequest().length;
    }
}