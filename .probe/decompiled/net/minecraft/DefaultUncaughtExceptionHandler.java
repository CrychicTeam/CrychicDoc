package net.minecraft;

import java.lang.Thread.UncaughtExceptionHandler;
import org.slf4j.Logger;

public class DefaultUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private final Logger logger;

    public DefaultUncaughtExceptionHandler(Logger logger0) {
        this.logger = logger0;
    }

    public void uncaughtException(Thread thread0, Throwable throwable1) {
        this.logger.error("Caught previously unhandled exception :", throwable1);
    }
}