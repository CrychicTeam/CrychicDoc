package fuzs.puzzleslib.api.event.v1.core;

import fuzs.puzzleslib.impl.event.core.EventInvokerImpl;
import java.util.Objects;
import java.util.function.BiConsumer;

public interface EventInvokerRegistry {

    default <T> void register(Class<T> clazz, BiConsumer<T, Object> converter) {
        this.register(clazz, converter, false);
    }

    default <T> void register(Class<T> clazz, BiConsumer<T, Object> converter, boolean joinInvokers) {
        Objects.requireNonNull(clazz, "type is null");
        Objects.requireNonNull(converter, "converter is null");
        EventInvokerImpl.register(clazz, context -> (phase, callback) -> {
            if (phase != EventPhase.DEFAULT) {
                throw new IllegalStateException("implementation does not support event phases");
            } else {
                converter.accept(callback, context);
            }
        }, joinInvokers);
    }
}