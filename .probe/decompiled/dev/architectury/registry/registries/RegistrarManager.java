package dev.architectury.registry.registries;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.registries.forge.RegistrarManagerImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class RegistrarManager {

    private static final Map<String, RegistrarManager> MANAGER = new ConcurrentHashMap();

    private final RegistrarManager.RegistryProvider provider;

    private final String modId;

    public static RegistrarManager get(String modId) {
        return (RegistrarManager) MANAGER.computeIfAbsent(modId, RegistrarManager::new);
    }

    private RegistrarManager(String modId) {
        this.provider = _get(modId);
        this.modId = modId;
    }

    public <T> Registrar<T> get(ResourceKey<Registry<T>> key) {
        return this.provider.get(key);
    }

    @Deprecated
    public <T> Registrar<T> get(Registry<T> registry) {
        return this.provider.get(registry);
    }

    public <T> void forRegistry(ResourceKey<Registry<T>> key, Consumer<Registrar<T>> callback) {
        this.provider.forRegistry(key, callback);
    }

    @SafeVarargs
    public final <T> RegistrarBuilder<T> builder(ResourceLocation registryId, T... typeGetter) {
        if (typeGetter.length != 0) {
            throw new IllegalStateException("array must be empty!");
        } else {
            return this.provider.builder(typeGetter.getClass().getComponentType(), registryId);
        }
    }

    @Nullable
    public static <T> ResourceLocation getId(T object, @Nullable ResourceKey<Registry<T>> fallback) {
        return fallback == null ? null : getId(object, (Registry<?>) BuiltInRegistries.REGISTRY.get(fallback.location()));
    }

    @Deprecated
    @Nullable
    public static <T> ResourceLocation getId(T object, @Nullable Registry<T> fallback) {
        return fallback == null ? null : fallback.getKey(object);
    }

    @ExpectPlatform
    @Transformed
    private static RegistrarManager.RegistryProvider _get(String modId) {
        return RegistrarManagerImpl._get(modId);
    }

    public String getModId() {
        return this.modId;
    }

    @Internal
    public interface RegistryProvider {

        <T> Registrar<T> get(ResourceKey<Registry<T>> var1);

        @Deprecated
        <T> Registrar<T> get(Registry<T> var1);

        <T> void forRegistry(ResourceKey<Registry<T>> var1, Consumer<Registrar<T>> var2);

        <T> RegistrarBuilder<T> builder(Class<T> var1, ResourceLocation var2);
    }
}