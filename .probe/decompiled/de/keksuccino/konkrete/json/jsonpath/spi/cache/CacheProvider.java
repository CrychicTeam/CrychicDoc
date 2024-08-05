package de.keksuccino.konkrete.json.jsonpath.spi.cache;

import de.keksuccino.konkrete.json.jsonpath.JsonPathException;
import de.keksuccino.konkrete.json.jsonpath.internal.Utils;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class CacheProvider {

    private static final AtomicReferenceFieldUpdater<CacheProvider, Cache> UPDATER = AtomicReferenceFieldUpdater.newUpdater(CacheProvider.class, Cache.class, "cache");

    private static final CacheProvider instance = new CacheProvider();

    private volatile Cache cache;

    public static void setCache(Cache cache) {
        Utils.notNull(cache, "Cache may not be null");
        if (!UPDATER.compareAndSet(instance, null, cache)) {
            throw new JsonPathException("Cache provider must be configured before cache is accessed and must not be registered twice.");
        }
    }

    public static Cache getCache() {
        return CacheProvider.CacheHolder.CACHE;
    }

    private static Cache getDefaultCache() {
        return new LRUCache(400);
    }

    private static class CacheHolder {

        static final Cache CACHE;

        static {
            Cache cache = CacheProvider.instance.cache;
            if (cache == null) {
                cache = CacheProvider.getDefaultCache();
                if (!CacheProvider.UPDATER.compareAndSet(CacheProvider.instance, null, cache)) {
                    cache = CacheProvider.instance.cache;
                }
            }
            CACHE = cache;
        }
    }
}