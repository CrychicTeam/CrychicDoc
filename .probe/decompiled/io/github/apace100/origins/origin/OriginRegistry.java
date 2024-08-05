package io.github.apace100.origins.origin;

import io.github.edwinmindcraft.origins.api.OriginsAPI;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@Deprecated
public class OriginRegistry {

    private static final Map<io.github.edwinmindcraft.origins.api.origin.Origin, Origin> CACHE_MAP = new ConcurrentHashMap();

    @Deprecated
    public static Origin register(Origin origin) {
        return register(origin.getIdentifier(), origin);
    }

    @Deprecated
    public static Origin register(ResourceLocation id, Origin origin) {
        return origin;
    }

    @Deprecated
    protected static Origin update(ResourceLocation id, Origin origin) {
        return register(id, origin);
    }

    public static int size() {
        return OriginsAPI.getOriginsRegistry().keySet().size();
    }

    public static Stream<ResourceLocation> identifiers() {
        return OriginsAPI.getOriginsRegistry().keySet().stream();
    }

    public static Iterable<Entry<ResourceLocation, Origin>> entries() {
        return () -> {
            final Iterator<Entry<ResourceKey<io.github.edwinmindcraft.origins.api.origin.Origin>, io.github.edwinmindcraft.origins.api.origin.Origin>> iterator = OriginsAPI.getOriginsRegistry().entrySet().iterator();
            return new Iterator<Entry<ResourceLocation, Origin>>() {

                public boolean hasNext() {
                    return iterator.hasNext();
                }

                public Entry<ResourceLocation, Origin> next() {
                    final Entry<ResourceKey<io.github.edwinmindcraft.origins.api.origin.Origin>, io.github.edwinmindcraft.origins.api.origin.Origin> next = (Entry<ResourceKey<io.github.edwinmindcraft.origins.api.origin.Origin>, io.github.edwinmindcraft.origins.api.origin.Origin>) iterator.next();
                    return new Entry<ResourceLocation, Origin>() {

                        public ResourceLocation getKey() {
                            return ((ResourceKey) next.getKey()).location();
                        }

                        public Origin getValue() {
                            return OriginRegistry.get((io.github.edwinmindcraft.origins.api.origin.Origin) next.getValue());
                        }

                        public Origin setValue(Origin value) {
                            return null;
                        }
                    };
                }
            };
        };
    }

    public static Iterable<Origin> values() {
        return () -> {
            final Iterator<io.github.edwinmindcraft.origins.api.origin.Origin> iterator = OriginsAPI.getOriginsRegistry().iterator();
            return new Iterator<Origin>() {

                public boolean hasNext() {
                    return iterator.hasNext();
                }

                public Origin next() {
                    return OriginRegistry.get((io.github.edwinmindcraft.origins.api.origin.Origin) iterator.next());
                }
            };
        };
    }

    public static Origin get(ResourceLocation id) {
        return (Origin) OriginsAPI.getOriginsRegistry().m_6612_(id).map(OriginRegistry::get).orElseThrow(() -> new IllegalArgumentException("Could not get origin from id '" + id.toString() + "', as it was not registered!"));
    }

    public static Origin get(io.github.edwinmindcraft.origins.api.origin.Origin origin) {
        return (Origin) CACHE_MAP.computeIfAbsent(origin, o -> new Origin(() -> o));
    }

    public static boolean contains(ResourceLocation id) {
        return OriginsAPI.getOriginsRegistry().containsKey(id);
    }

    public static boolean contains(Origin origin) {
        return contains(origin.getIdentifier());
    }

    public static void clear() {
        CACHE_MAP.clear();
    }

    public static void reset() {
        clear();
    }

    public static void remove(ResourceLocation id) {
        throw new UnsupportedOperationException("Remove origins by cancelling a registration event.");
    }
}