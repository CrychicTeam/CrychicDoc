package com.craisinlord.integrated_api.modinit.registry.forge;

import com.craisinlord.integrated_api.modinit.registry.CustomRegistryLookup;
import com.craisinlord.integrated_api.modinit.registry.ResourcefulRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.commons.lang3.tuple.Pair;

public class ResourcefulRegistriesImpl {

    private static final List<ResourcefulRegistriesImpl.CustomRegistryInfo<?>> CUSTOM_REGISTRIES = new ArrayList();

    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new ForgeResourcefulRegistry<>(registry.key(), id);
    }

    public static <T, K extends Registry<T>> Pair<Supplier<CustomRegistryLookup<T>>, ResourcefulRegistry<T>> createCustomRegistryInternal(String modId, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification) {
        ResourcefulRegistriesImpl.CustomRegistryInfo<T> info = new ResourcefulRegistriesImpl.CustomRegistryInfo<>(new ResourcefulRegistriesImpl.LateSupplier<>(), key, save, sync, allowModification);
        CUSTOM_REGISTRIES.add(info);
        return Pair.of(info.lookup(), new ForgeResourcefulRegistry(key, modId));
    }

    public static void onRegisterForgeRegistries(NewRegistryEvent event) {
        CUSTOM_REGISTRIES.forEach(registry -> registry.build(event));
    }

    public static record CustomRegistryInfo<T>(ResourcefulRegistriesImpl.LateSupplier<CustomRegistryLookup<T>> lookup, ResourceKey<? extends Registry<T>> key, boolean save, boolean sync, boolean allowModification) {

        public void build(NewRegistryEvent event) {
            this.lookup.set(new ForgeCustomRegistry<>(event.create(this.getBuilder())));
        }

        public RegistryBuilder<T> getBuilder() {
            RegistryBuilder<T> builder = new RegistryBuilder<>();
            builder.setName(this.key.location());
            if (!this.save) {
                builder.disableSaving();
            }
            if (!this.sync) {
                builder.disableSync();
            }
            if (this.allowModification) {
                builder.allowModification();
            }
            return builder;
        }
    }

    public static class LateSupplier<T> implements Supplier<T> {

        private T value;

        private boolean initialized = false;

        public void set(T value) {
            this.value = value;
            this.initialized = true;
        }

        public T get() {
            if (!this.initialized) {
                throw new IllegalStateException("LateSupplier not initialized");
            } else {
                return this.value;
            }
        }
    }
}