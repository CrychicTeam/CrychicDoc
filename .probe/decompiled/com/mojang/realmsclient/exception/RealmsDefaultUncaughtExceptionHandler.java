package com.mojang.realmsclient.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import org.slf4j.Logger;

public class RealmsDefaultUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private final Logger logger;

    public RealmsDefaultUncaughtExceptionHandler(Logger logger0) {
        this.logger = logger0;
    }

    public void uncaughtException(Thread thread0, Throwable throwable1) {
        this.logger.error("Caught previously unhandled exception", throwable1);
    }
}