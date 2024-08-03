package fuzs.puzzleslib.api.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface RegistryEntryAddedCallback<T> {

    static <T> EventInvoker<RegistryEntryAddedCallback<T>> registryEntryAdded(ResourceKey<? extends Registry<T>> resourceKey) {
        Objects.requireNonNull(resourceKey, "resource key is null");
        return EventInvoker.lookup(RegistryEntryAddedCallback.class, resourceKey);
    }

    void onRegistryEntryAdded(Registry<T> var1, ResourceLocation var2, T var3, BiConsumer<ResourceLocation, Supplier<T>> var4);
}