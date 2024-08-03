package me.lucko.spark.lib.adventure.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public abstract class Listenable<L> {

    private final List<L> listeners = new CopyOnWriteArrayList();

    protected final void forEachListener(@NotNull final Consumer<L> consumer) {
        for (L listener : this.listeners) {
            consumer.accept(listener);
        }
    }

    protected final void addListener0(@NotNull final L listener) {
        this.listeners.add(listener);
    }

    protected final void removeListener0(@NotNull final L listener) {
        this.listeners.remove(listener);
    }
}