package io.github.apace100.origins.origin;

import com.google.common.collect.ImmutableSet;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;

@Deprecated
public class OriginLayers {

    private static final Map<io.github.edwinmindcraft.origins.api.origin.OriginLayer, OriginLayer> CACHE_MAP = new ConcurrentHashMap();

    public static OriginLayer getLayer(ResourceLocation id) {
        return (OriginLayer) OriginsAPI.getLayersRegistry().m_6612_(id).map(OriginLayer::new).orElse(null);
    }

    public static Collection<OriginLayer> getLayers() {
        return (Collection<OriginLayer>) OriginsAPI.getLayersRegistry().m_123024_().map(OriginLayer::new).collect(ImmutableSet.toImmutableSet());
    }

    public static int size() {
        return OriginsAPI.getLayersRegistry().keySet().size();
    }

    public static void clear() {
        CACHE_MAP.clear();
    }

    @Deprecated
    public static void add(OriginLayer layer) {
    }

    public static OriginLayer get(io.github.edwinmindcraft.origins.api.origin.OriginLayer layer) {
        return (OriginLayer) CACHE_MAP.computeIfAbsent(layer, OriginLayer::new);
    }
}