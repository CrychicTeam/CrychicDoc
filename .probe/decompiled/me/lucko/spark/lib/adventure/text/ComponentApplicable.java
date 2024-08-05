package me.lucko.spark.lib.adventure.text;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ComponentApplicable {

    @NotNull
    Component componentApply(@NotNull final Component component);
}