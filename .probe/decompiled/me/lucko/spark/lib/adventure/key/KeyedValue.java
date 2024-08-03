package me.lucko.spark.lib.adventure.key;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public interface KeyedValue<T> extends Keyed {

    @NotNull
    static <T> KeyedValue<T> keyedValue(@NotNull final Key key, @NotNull final T value) {
        return new KeyedValueImpl<>(key, (T) Objects.requireNonNull(value, "value"));
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    static <T> KeyedValue<T> of(@NotNull final Key key, @NotNull final T value) {
        return new KeyedValueImpl<>(key, (T) Objects.requireNonNull(value, "value"));
    }

    @NotNull
    T value();
}