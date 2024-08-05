package org.violetmoon.zeta.multiloader;

import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus.Internal;

public enum Loader {

    FORGE;

    public static final Loader CURRENT = getCurrent();

    public boolean isCurrent() {
        return this == CURRENT;
    }

    public void runIfCurrent(Supplier<Runnable> run) {
        if (this.isCurrent()) {
            ((Runnable) run.get()).run();
        }
    }

    @Internal
    public static Loader getCurrent() {
        return FORGE;
    }
}