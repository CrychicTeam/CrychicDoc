package me.jellysquid.mods.sodium.client.util.collections;

import org.jetbrains.annotations.NotNull;

public interface WriteQueue<E> {

    void ensureCapacity(int var1);

    void enqueue(@NotNull E var1);
}