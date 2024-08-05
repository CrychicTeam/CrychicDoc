package net.minecraft.core;

import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public interface HolderGetter<T> {

    Optional<Holder.Reference<T>> get(ResourceKey<T> var1);

    default Holder.Reference<T> getOrThrow(ResourceKey<T> resourceKeyT0) {
        return (Holder.Reference<T>) this.get(resourceKeyT0).orElseThrow(() -> new IllegalStateException("Missing element " + resourceKeyT0));
    }

    Optional<HolderSet.Named<T>> get(TagKey<T> var1);

    default HolderSet.Named<T> getOrThrow(TagKey<T> tagKeyT0) {
        return (HolderSet.Named<T>) this.get(tagKeyT0).orElseThrow(() -> new IllegalStateException("Missing tag " + tagKeyT0));
    }

    public interface Provider {

        <T> Optional<HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> var1);

        default <T> HolderGetter<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> resourceKeyExtendsRegistryExtendsT0) {
            return (HolderGetter<T>) this.lookup(resourceKeyExtendsRegistryExtendsT0).orElseThrow(() -> new IllegalStateException("Registry " + resourceKeyExtendsRegistryExtendsT0.location() + " not found"));
        }
    }
}