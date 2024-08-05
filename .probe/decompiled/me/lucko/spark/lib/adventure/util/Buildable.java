package me.lucko.spark.lib.adventure.util;

import java.util.function.Consumer;
import me.lucko.spark.lib.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Buildable<R, B extends Buildable.Builder<R>> {

    @Deprecated
    @Contract(mutates = "param1")
    @NotNull
    static <R extends Buildable<R, B>, B extends Buildable.Builder<R>> R configureAndBuild(@NotNull final B builder, @Nullable final Consumer<? super B> consumer) {
        return AbstractBuilder.configureAndBuild(builder, consumer);
    }

    @Contract(value = "-> new", pure = true)
    @NotNull
    B toBuilder();

    @Deprecated
    public interface Builder<R> extends AbstractBuilder<R> {

        @Contract(value = "-> new", pure = true)
        @NotNull
        @Override
        R build();
    }
}