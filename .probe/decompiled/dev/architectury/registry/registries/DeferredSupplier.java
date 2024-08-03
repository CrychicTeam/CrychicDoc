package dev.architectury.registry.registries;

import dev.architectury.utils.OptionalSupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface DeferredSupplier<T> extends OptionalSupplier<T> {

    ResourceLocation getRegistryId();

    default ResourceKey<Registry<T>> getRegistryKey() {
        return ResourceKey.createRegistryKey(this.getRegistryId());
    }

    ResourceLocation getId();

    default ResourceKey<T> getKey() {
        return ResourceKey.create(this.getRegistryKey(), this.getId());
    }
}