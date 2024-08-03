package com.craisinlord.integrated_api.modinit.registry;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public class BasicRegistryEntry<T> implements RegistryEntry<T> {

    private final ResourceLocation id;

    private final T value;

    public BasicRegistryEntry(ResourceLocation id, T value) {
        this.id = (ResourceLocation) Objects.requireNonNull(id);
        this.value = (T) Objects.requireNonNull(value);
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }
}