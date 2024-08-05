package me.lucko.spark.lib.adventure.text.flattener;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import me.lucko.spark.lib.adventure.builder.AbstractBuilder;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ComponentFlattener extends Buildable<ComponentFlattener, ComponentFlattener.Builder> {

    @NotNull
    static ComponentFlattener.Builder builder() {
        return new ComponentFlattenerImpl.BuilderImpl();
    }

    @NotNull
    static ComponentFlattener basic() {
        return ComponentFlattenerImpl.BASIC;
    }

    @NotNull
    static ComponentFlattener textOnly() {
        return ComponentFlattenerImpl.TEXT_ONLY;
    }

    void flatten(@NotNull final Component input, @NotNull final FlattenerListener listener);

    public interface Builder extends AbstractBuilder<ComponentFlattener>, Buildable.Builder<ComponentFlattener> {

        @NotNull
        <T extends Component> ComponentFlattener.Builder mapper(@NotNull final Class<T> type, @NotNull final Function<T, String> converter);

        @NotNull
        <T extends Component> ComponentFlattener.Builder complexMapper(@NotNull final Class<T> type, @NotNull final BiConsumer<T, Consumer<Component>> converter);

        @NotNull
        ComponentFlattener.Builder unknownMapper(@Nullable final Function<Component, String> converter);
    }
}