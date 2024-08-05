package me.lucko.spark.common.command;

import java.util.function.Consumer;

public interface CommandModule extends AutoCloseable {

    void registerCommands(Consumer<Command> var1);

    default void close() {
    }
}