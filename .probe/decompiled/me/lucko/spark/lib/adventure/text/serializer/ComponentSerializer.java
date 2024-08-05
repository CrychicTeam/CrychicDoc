package me.lucko.spark.lib.adventure.text.serializer;

import me.lucko.spark.lib.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public interface ComponentSerializer<I extends Component, O extends Component, R> {

    @NotNull
    O deserialize(@NotNull final R input);

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(value = "!null -> !null; null -> null", pure = true)
    @Nullable
    default O deseializeOrNull(@Nullable final R input) {
        return this.deserializeOrNull(input);
    }

    @Contract(value = "!null -> !null; null -> null", pure = true)
    @Nullable
    default O deserializeOrNull(@Nullable final R input) {
        return this.deserializeOr(input, null);
    }

    @Contract(value = "!null, _ -> !null; null, _ -> param2", pure = true)
    @Nullable
    default O deserializeOr(@Nullable final R input, @Nullable final O fallback) {
        return input == null ? fallback : this.deserialize(input);
    }

    @NotNull
    R serialize(@NotNull final I component);

    @Contract(value = "!null -> !null; null -> null", pure = true)
    @Nullable
    default R serializeOrNull(@Nullable final I component) {
        return this.serializeOr(component, null);
    }

    @Contract(value = "!null, _ -> !null; null, _ -> param2", pure = true)
    @Nullable
    default R serializeOr(@Nullable final I component, @Nullable final R fallback) {
        return component == null ? fallback : this.serialize(component);
    }
}