package net.mehvahdjukaar.moonlight.api.misc;

@FunctionalInterface
public interface QuadConsumer<K, V, S, T> {

    void accept(K var1, V var2, S var3, T var4);
}