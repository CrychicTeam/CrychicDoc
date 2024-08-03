package me.lucko.spark.lib.adventure.text;

import me.lucko.spark.lib.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;

public interface BuildableComponent<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> extends Buildable<C, B>, Component {

    @NotNull
    B toBuilder();
}