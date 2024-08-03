package com.github.alexthe666.citadel.server.world;

public interface ModifiableTickRateServer {

    void setGlobalTickLengthMs(long var1);

    long getMasterMs();

    default void resetGlobalTickLengthMs() {
        this.setGlobalTickLengthMs(-1L);
    }
}