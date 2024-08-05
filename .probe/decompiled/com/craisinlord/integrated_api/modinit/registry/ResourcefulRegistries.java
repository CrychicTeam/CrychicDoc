package com.craisinlord.integrated_api.modinit.registry;

import com.craisinlord.integrated_api.modinit.registry.forge.ResourcefulRegistriesImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.tuple.Pair;

public class ResourcefulRegistries {

    public static <T> ResourcefulRegistry<T> create(ResourcefulRegistry<T> parent) {
        return new ResourcefulRegistryChild<>(parent);
    }

    @ExpectPlatform
    @Transformed
    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return ResourcefulRegistriesImpl.create(registry, id);
    }

    @ExpectPlatform
    @Transformed
    public static <T, K extends Registry<T>> Pair<Supplier<CustomRegistryLookup<T>>, ResourcefulRegistry<T>> createCustomRegistryInternal(String modId, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification) {
        return ResourcefulRegistriesImpl.createCustomRegistryInternal(modId, key, save, sync, allowModification);
    }
}