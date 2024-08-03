package net.minecraft;

import java.lang.Thread.UncaughtExceptionHandler;
import org.slf4j.Logger;

public class DefaultUncaughtExceptionHandlerWithName implements UncaughtExceptionHandler {

    private final Logger logger;

    public DefaultUncaughtExceptionHandlerWithName(Logger logger0) {
        this.logger = logger0;
    }

    public void uncaughtException(Thread thread0, Throwable throwable1) {
        this.logger.error("Caught previously unhandled exception :");
        this.logger.error(thread0.getName(), throwable1);
    }
}