package me.lucko.spark.lib.adventure.text.flattener;

import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FlattenerListener {

    default void pushStyle(@NotNull final Style style) {
    }

    void component(@NotNull final String text);

    default void popStyle(@NotNull final Style style) {
    }
}