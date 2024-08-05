package fuzs.puzzleslib.api.event.v1.core;

import fuzs.puzzleslib.impl.event.core.EventInvokerImpl;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface EventInvoker<T> {

    static <T> EventInvoker<T> lookup(Class<T> clazz) {
        return lookup(clazz, null);
    }

    static <T> EventInvoker<T> lookup(Class<T> clazz, @Nullable Object context) {
        return EventInvokerImpl.softLookup(clazz, context);
    }

    default void register(T callback) {
        this.register(EventPhase.DEFAULT, callback);
    }

    void register(EventPhase var1, T var2);
}