package harmonised.pmmo.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;

public class Functions<X, Y> {

    public static <X, Y> Function<X, Y> memoize(Function<X, Y> func) {
        Map<X, Y> cache = new ConcurrentHashMap();
        return a -> cache.computeIfAbsent(a, func);
    }

    public static <T> void biPermutation(T one, T two, boolean isOneTrue, boolean isTwoTrue, BiConsumer<T, T> either, BiConsumer<T, T> neither, BiConsumer<T, T> both) {
        if (isOneTrue && !isTwoTrue) {
            either.accept(one, two);
        } else if (!isOneTrue && isTwoTrue) {
            either.accept(two, one);
        } else if (!isOneTrue && !isTwoTrue) {
            neither.accept(one, two);
        } else {
            both.accept(one, two);
        }
    }

    @SafeVarargs
    public static <K, V extends Number> Map<K, V> mergeMaps(Map<K, V>... maps) {
        Map<K, V> outMap = (Map<K, V>) (maps.length >= 2 ? mergeMaps(maps[0], maps[1]) : (maps.length >= 1 ? maps[0] : new HashMap()));
        for (int i = 2; i < maps.length; i++) {
            outMap = mergeMaps(outMap, maps[i]);
        }
        return outMap;
    }

    public static <K, V extends Number> Map<K, V> mergeMaps(Map<K, V> one, Map<K, V> two) {
        Map<K, V> outMap = new HashMap(one);
        two.forEach((key, value) -> outMap.merge(key, value, (v1, v2) -> v1.longValue() > v2.longValue() ? v1 : v2));
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.EVENT, "Merged Map: {}", MsLoggy.mapToString(outMap));
        return outMap;
    }

    public static UUID getReliableUUID(String str) {
        String hashedString = String.valueOf(Math.abs(str.hashCode()));
        while (hashedString.length() < 36) {
            hashedString = (hashedString.length() != 12 && hashedString.length() != 16 && hashedString.length() != 20 && hashedString.length() != 24 ? "0" : "-") + hashedString;
        }
        return UUID.fromString(hashedString);
    }

    public static ResourceLocation pathPrepend(ResourceLocation original, String prepend) {
        return new ResourceLocation(original.getNamespace(), prepend + "/" + original.getPath());
    }
}