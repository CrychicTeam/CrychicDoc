package me.lucko.spark.lib.adventure.builder;

import java.util.function.Consumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface AbstractBuilder<R> {

    @Contract(mutates = "param1")
    @NotNull
    static <R, B extends AbstractBuilder<R>> R configureAndBuild(@NotNull final B builder, @Nullable final Consumer<? super B> consumer) {
        if (consumer != null) {
            consumer.accept(builder);
        }
        return builder.build();
    }

    @Contract(value = "-> new", pure = true)
    @NotNull
    R build();
}