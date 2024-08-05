package net.blay09.mods.defaultoptions;

import net.blay09.mods.defaultoptions.api.DefaultOptionsHandler;

public class DefaultOptionsHandlerException extends Exception {

    private final DefaultOptionsHandler handler;

    public DefaultOptionsHandlerException(DefaultOptionsHandler handler) {
        this.handler = handler;
    }

    public DefaultOptionsHandlerException(DefaultOptionsHandler handler, String message) {
        super(message);
        this.handler = handler;
    }

    public DefaultOptionsHandlerException(DefaultOptionsHandler handler, String message, Throwable cause) {
        super(message, cause);
        this.handler = handler;
    }

    public DefaultOptionsHandlerException(DefaultOptionsHandler handler, Throwable cause) {
        super(cause);
        this.handler = handler;
    }

    public DefaultOptionsHandlerException(DefaultOptionsHandler handler, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.handler = handler;
    }

    public String getHandlerId() {
        return this.handler.getId();
    }
}