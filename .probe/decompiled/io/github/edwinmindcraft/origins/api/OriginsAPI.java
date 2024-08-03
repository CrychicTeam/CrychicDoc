package io.github.edwinmindcraft.origins.api;

import io.github.edwinmindcraft.calio.api.CalioAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class OriginsAPI {

    private static final ConcurrentHashMap<ResourceLocation, ResourceLocation> POWER_SOURCE_CACHE = new ConcurrentHashMap();

    public static final String MODID = "origins";

    public static final Capability<IOriginContainer> ORIGIN_CONTAINER = CapabilityManager.get(new CapabilityToken<IOriginContainer>() {
    });

    @Contract(pure = true)
    public static MappedRegistry<Origin> getOriginsRegistry(@Nullable MinecraftServer server) {
        return CalioAPI.getDynamicRegistries(server).get(OriginsDynamicRegistries.ORIGINS_REGISTRY);
    }

    @Contract(pure = true)
    public static MappedRegistry<Origin> getOriginsRegistry() {
        return CalioAPI.getDynamicRegistries().get(OriginsDynamicRegistries.ORIGINS_REGISTRY);
    }

    @Contract(pure = true)
    public static MappedRegistry<OriginLayer> getLayersRegistry(@Nullable MinecraftServer server) {
        return CalioAPI.getDynamicRegistries(server).get(OriginsDynamicRegistries.LAYERS_REGISTRY);
    }

    @Contract(pure = true)
    public static MappedRegistry<OriginLayer> getLayersRegistry() {
        return CalioAPI.getDynamicRegistries().get(OriginsDynamicRegistries.LAYERS_REGISTRY);
    }

    @Contract(pure = true)
    public static List<Holder.Reference<OriginLayer>> getActiveLayers() {
        return getLayersRegistry().holders().filter(x -> x.isBound() && ((OriginLayer) x.value()).enabled()).sorted(Comparator.comparing(Holder::get)).toList();
    }

    public static ResourceLocation getPowerSource(ResourceKey<Origin> origin) {
        Validate.notNull(origin, "Unregistered origins cannot provide powers.", new Object[0]);
        return (ResourceLocation) POWER_SOURCE_CACHE.computeIfAbsent(origin.location(), OriginsAPI::createPowerSource);
    }

    public static ResourceLocation getPowerSource(Holder<Origin> origin) {
        ResourceKey<Origin> key = (ResourceKey<Origin>) ((Optional) origin.unwrap().map(Optional::of, getOriginsRegistry()::m_7854_)).orElse(null);
        Validate.notNull(key, "Unregistered origins cannot provide powers.", new Object[0]);
        return getPowerSource(key);
    }

    public static ResourceLocation getPowerSource(ResourceLocation origin) {
        Validate.notNull(origin, "Unregistered origins cannot provide powers.", new Object[0]);
        return (ResourceLocation) POWER_SOURCE_CACHE.computeIfAbsent(origin, OriginsAPI::createPowerSource);
    }

    @Contract(pure = true)
    private static ResourceLocation createPowerSource(ResourceLocation key) {
        return new ResourceLocation(key.getNamespace(), key.getPath());
    }
}