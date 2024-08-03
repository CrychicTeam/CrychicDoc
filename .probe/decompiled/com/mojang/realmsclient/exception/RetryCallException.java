package com.mojang.realmsclient.exception;

public class RetryCallException extends RealmsServiceException {

    public static final int DEFAULT_DELAY = 5;

    public final int delaySeconds;

    public RetryCallException(int int0, int int1) {
        super(int1, "Retry operation");
        if (int0 >= 0 && int0 <= 120) {
            this.delaySeconds = int0;
        } else {
            this.delaySeconds = 5;
        }
    }
}