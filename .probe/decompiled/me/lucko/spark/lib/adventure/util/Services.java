package me.lucko.spark.lib.adventure.util;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;
import me.lucko.spark.lib.adventure.internal.properties.AdventureProperties;
import org.jetbrains.annotations.NotNull;

public final class Services {

    private static final boolean SERVICE_LOAD_FAILURES_ARE_FATAL = Boolean.TRUE.equals(AdventureProperties.SERVICE_LOAD_FAILURES_ARE_FATAL.value());

    private Services() {
    }

    @NotNull
    public static <P> Optional<P> service(@NotNull final Class<P> type) {
        ServiceLoader<P> loader = Services0.loader(type);
        Iterator<P> it = loader.iterator();
        while (it.hasNext()) {
            P instance;
            try {
                instance = (P) it.next();
            } catch (Throwable var5) {
                if (!SERVICE_LOAD_FAILURES_ARE_FATAL) {
                    continue;
                }
                throw new IllegalStateException("Encountered an exception loading service " + type, var5);
            }
            if (it.hasNext()) {
                throw new IllegalStateException("Expected to find one service " + type + ", found multiple");
            }
            return Optional.of(instance);
        }
        return Optional.empty();
    }
}