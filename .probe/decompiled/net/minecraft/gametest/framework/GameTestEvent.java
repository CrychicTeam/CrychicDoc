package net.minecraft.gametest.framework;

import javax.annotation.Nullable;

class GameTestEvent {

    @Nullable
    public final Long expectedDelay;

    public final Runnable assertion;

    private GameTestEvent(@Nullable Long long0, Runnable runnable1) {
        this.expectedDelay = long0;
        this.assertion = runnable1;
    }

    static GameTestEvent create(Runnable runnable0) {
        return new GameTestEvent(null, runnable0);
    }

    static GameTestEvent create(long long0, Runnable runnable1) {
        return new GameTestEvent(long0, runnable1);
    }
}