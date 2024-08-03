package fuzs.puzzleslib.api.event.v1.core;

import java.util.NoSuchElementException;

public enum EventResult {

    PASS(false, false), INTERRUPT(true, true), ALLOW(true, true), DENY(true, false);

    private final boolean interrupt;

    private final boolean value;

    private EventResult(boolean interrupt, boolean value) {
        this.interrupt = interrupt;
        this.value = value;
    }

    public boolean getAsBoolean() {
        if (!this.interrupt) {
            throw new NoSuchElementException("No value present");
        } else {
            return this.value;
        }
    }

    public boolean isInterrupt() {
        return this.interrupt;
    }

    public boolean isPass() {
        return !this.interrupt;
    }
}