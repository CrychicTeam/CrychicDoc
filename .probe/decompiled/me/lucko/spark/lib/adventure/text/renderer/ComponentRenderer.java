package me.lucko.spark.lib.adventure.text.renderer;

import java.util.function.Function;
import me.lucko.spark.lib.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface ComponentRenderer<C> {

    @NotNull
    Component render(@NotNull final Component component, @NotNull final C context);

    default <T> ComponentRenderer<T> mapContext(final Function<T, C> transformer) {
        return (component, ctx) -> this.render(component, (C) transformer.apply(ctx));
    }
}