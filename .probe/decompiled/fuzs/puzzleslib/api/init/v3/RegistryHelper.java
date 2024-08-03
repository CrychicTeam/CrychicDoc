package fuzs.puzzleslib.api.init.v3;

import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

public final class RegistryHelper {

    private RegistryHelper() {
    }

    public static <T> Registry<T> findRegistry(ResourceKey<? extends Registry<? super T>> registryKey) {
        return findRegistry(registryKey, false);
    }

    public static <T> Registry<T> findBuiltInRegistry(ResourceKey<? extends Registry<? super T>> registryKey) {
        return findRegistry(registryKey, true);
    }

    private static <T> Registry<T> findRegistry(ResourceKey<? extends Registry<? super T>> registryKey, boolean onlyBuiltIn) {
        Objects.requireNonNull(registryKey, "registry key is null");
        Optional<Registry<T>> registry = Optional.empty();
        if (!onlyBuiltIn) {
            MinecraftServer minecraftServer = CommonAbstractions.INSTANCE.getMinecraftServer();
            if (minecraftServer != null) {
                registry = minecraftServer.registryAccess().m_6632_(registryKey);
            }
        }
        if (registry.isEmpty()) {
            registry = (Optional<Registry<T>>) BuiltInRegistries.REGISTRY.getOptional(registryKey);
        }
        return (Registry<T>) registry.orElseThrow(() -> new IllegalArgumentException("Registry for key %s not found".formatted(registryKey)));
    }

    public static <T> Optional<ResourceKey<T>> getResourceKey(ResourceKey<? extends Registry<? super T>> registryKey, T object) {
        return findRegistry(registryKey).getResourceKey(object);
    }

    public static <T> ResourceKey<T> getResourceKeyOrThrow(ResourceKey<? extends Registry<? super T>> registryKey, T object) {
        return (ResourceKey<T>) getResourceKey(registryKey, object).orElseThrow(() -> new IllegalStateException("Missing object in " + registryKey + ": " + object));
    }

    public static <T> Optional<Holder.Reference<T>> getHolder(ResourceKey<? extends Registry<? super T>> registryKey, T object) {
        Registry<T> registry = findRegistry(registryKey);
        return registry.getResourceKey(object).flatMap(registry::m_203636_);
    }

    public static <T> Holder.Reference<T> getHolderOrThrow(ResourceKey<? extends Registry<? super T>> registryKey, T object) {
        return (Holder.Reference<T>) getHolder(registryKey, object).orElseThrow(() -> new IllegalStateException("Missing object in " + registryKey + ": " + object));
    }

    public static <T> Holder<T> wrapAsHolder(ResourceKey<? extends Registry<? super T>> registryKey, T object) {
        return findRegistry(registryKey).wrapAsHolder(object);
    }

    public static <T> boolean is(TagKey<T> tagKey, T object) {
        if (object instanceof Block block) {
            return block.builtInRegistryHolder().is(tagKey);
        } else if (object instanceof Item item) {
            return item.builtInRegistryHolder().is(tagKey);
        } else if (object instanceof EntityType<?> entityType) {
            return entityType.builtInRegistryHolder().is(tagKey);
        } else if (object instanceof GameEvent gameEvent) {
            return gameEvent.builtInRegistryHolder().is(tagKey);
        } else if (object instanceof Fluid fluid) {
            return fluid.builtInRegistryHolder().is(tagKey);
        } else {
            Registry<T> registry = findRegistry(tagKey.registry());
            return tagKey.isFor(registry.key()) && registry.wrapAsHolder(object).is(tagKey);
        }
    }
}