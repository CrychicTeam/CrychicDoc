package com.craisinlord.integrated_api.modinit.registry;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.tuple.Pair;

public interface CustomRegistry<T> extends ResourcefulRegistry<T> {

    static <T, K extends Registry<T>> CustomRegistry<T> of(String modId, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification) {
        final Pair<Supplier<CustomRegistryLookup<T>>, ResourcefulRegistry<T>> pair = ResourcefulRegistries.createCustomRegistryInternal(modId, key, save, sync, allowModification);
        return new CustomRegistry<T>() {

            @Override
            public CustomRegistryLookup<T> lookup() {
                return (CustomRegistryLookup<T>) ((Supplier) pair.getLeft()).get();
            }

            @Override
            public ResourcefulRegistry<T> registry() {
                return (ResourcefulRegistry<T>) pair.getRight();
            }
        };
    }

    CustomRegistryLookup<T> lookup();

    ResourcefulRegistry<T> registry();

    @Override
    default <I extends T> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        return this.registry().register(id, supplier);
    }

    @Override
    default Collection<RegistryEntry<T>> getEntries() {
        return this.registry().getEntries();
    }

    @Override
    default Stream<RegistryEntry<T>> stream() {
        return this.registry().stream();
    }

    @Override
    default void init() {
        this.registry().init();
    }
}