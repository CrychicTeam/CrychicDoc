package com.craisinlord.integrated_api.modinit.registry;

import java.util.EnumMap;
import java.util.function.Supplier;

public class EnumResourcefulRegistryChild<E extends Enum<E>, T> extends ResourcefulRegistryChild<T> {

    private final EnumMap<E, RegistryEntries<T>> entries;

    public EnumResourcefulRegistryChild(Class<E> enumClass, ResourcefulRegistry<T> parent) {
        super(parent);
        this.entries = new EnumMap(enumClass);
    }

    public <I extends T> RegistryEntry<I> register(E enumValue, String id, Supplier<I> supplier) {
        return ((RegistryEntries) this.entries.computeIfAbsent(enumValue, a -> new RegistryEntries())).add(super.register(id, supplier));
    }

    public RegistryEntries<T> getEntries(E enumValue) {
        return (RegistryEntries<T>) this.entries.get(enumValue);
    }
}