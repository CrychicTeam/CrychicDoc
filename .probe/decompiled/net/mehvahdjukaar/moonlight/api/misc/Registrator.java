package net.mehvahdjukaar.moonlight.api.misc;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface Registrator<T> {

    void register(ResourceLocation var1, T var2);

    default void register(String name, T instance) {
        this.register(new ResourceLocation(name), instance);
    }
}