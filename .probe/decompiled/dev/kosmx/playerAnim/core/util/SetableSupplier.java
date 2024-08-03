package dev.kosmx.playerAnim.core.util;

import java.util.function.Supplier;

public class SetableSupplier<T> implements Supplier<T> {

    T object;

    public void set(T object) {
        this.object = object;
    }

    public T get() {
        return this.object;
    }
}