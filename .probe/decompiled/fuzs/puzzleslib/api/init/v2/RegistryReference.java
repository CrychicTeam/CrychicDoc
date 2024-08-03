package fuzs.puzzleslib.api.init.v2;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface RegistryReference<T> {

    ResourceKey<? extends Registry<? super T>> getRegistryKey();

    ResourceKey<T> getResourceKey();

    ResourceLocation getResourceLocation();

    T get();

    Holder<T> holder();

    boolean isPresent();

    default boolean isEmpty() {
        return !this.isPresent();
    }

    static <T> RegistryReference<T> placeholder(ResourceKey<? extends Registry<? super T>> registryKey, String namespace, String path) {
        return placeholder(registryKey, new ResourceLocation(namespace, path));
    }

    static <T> RegistryReference<T> placeholder(final ResourceKey<? extends Registry<? super T>> registryKey, final ResourceLocation resourceLocation) {
        final Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
        if (registry == null) {
            throw new IllegalStateException(String.format("Unable to retrieve registry from key %s", registryKey));
        } else {
            final ResourceKey<T> resourceKey = ResourceKey.create((ResourceKey<? extends Registry<T>>) registryKey, resourceLocation);
            return new RegistryReference<T>() {

                @Nullable
                private T value;

                @Override
                public ResourceKey<? extends Registry<? super T>> getRegistryKey() {
                    return registryKey;
                }

                @Override
                public ResourceKey<T> getResourceKey() {
                    return resourceKey;
                }

                @Override
                public ResourceLocation getResourceLocation() {
                    return resourceLocation;
                }

                @Override
                public T get() {
                    if (this.value == null) {
                        if (!registry.containsKey(resourceLocation)) {
                            throw new IllegalStateException(String.format("Unable to retrieve placeholder %s from registry %s", resourceLocation, registryKey));
                        }
                        this.value = registry.get(resourceLocation);
                    }
                    return this.value;
                }

                @Override
                public Holder<T> holder() {
                    return registry.getHolderOrThrow(this.getResourceKey());
                }

                @Override
                public boolean isPresent() {
                    return this.value != null || registry.containsKey(resourceLocation);
                }
            };
        }
    }
}