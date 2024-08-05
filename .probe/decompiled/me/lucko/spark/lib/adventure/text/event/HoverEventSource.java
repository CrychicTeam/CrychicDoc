package me.lucko.spark.lib.adventure.text.event;

import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HoverEventSource<V> {

    @Nullable
    static <V> HoverEvent<V> unbox(@Nullable final HoverEventSource<V> source) {
        return source != null ? source.asHoverEvent() : null;
    }

    @NotNull
    default HoverEvent<V> asHoverEvent() {
        return this.asHoverEvent(UnaryOperator.identity());
    }

    @NotNull
    HoverEvent<V> asHoverEvent(@NotNull final UnaryOperator<V> op);
}