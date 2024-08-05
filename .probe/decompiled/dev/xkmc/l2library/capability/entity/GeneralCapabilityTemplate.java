package dev.xkmc.l2library.capability.entity;

import dev.xkmc.l2serial.util.Wrappers;

public class GeneralCapabilityTemplate<E, T extends GeneralCapabilityTemplate<E, T>> {

    public T getThis() {
        return (T) Wrappers.cast(this);
    }

    public T check() {
        return this.getThis();
    }
}