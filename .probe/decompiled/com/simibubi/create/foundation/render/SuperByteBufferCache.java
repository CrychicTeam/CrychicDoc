package com.simibubi.create.foundation.render;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SuperByteBufferCache {

    protected final Map<SuperByteBufferCache.Compartment<?>, Cache<Object, SuperByteBuffer>> caches = new HashMap();

    public synchronized void registerCompartment(SuperByteBufferCache.Compartment<?> compartment) {
        this.caches.put(compartment, CacheBuilder.newBuilder().removalListener(n -> ((SuperByteBuffer) n.getValue()).delete()).build());
    }

    public synchronized void registerCompartment(SuperByteBufferCache.Compartment<?> compartment, long ticksUntilExpired) {
        this.caches.put(compartment, CacheBuilder.newBuilder().expireAfterAccess(ticksUntilExpired * 50L, TimeUnit.MILLISECONDS).removalListener(n -> ((SuperByteBuffer) n.getValue()).delete()).build());
    }

    public <T> SuperByteBuffer get(SuperByteBufferCache.Compartment<T> compartment, T key, Callable<SuperByteBuffer> callable) {
        Cache<Object, SuperByteBuffer> cache = (Cache<Object, SuperByteBuffer>) this.caches.get(compartment);
        if (cache != null) {
            try {
                return (SuperByteBuffer) cache.get(key, callable);
            } catch (ExecutionException var6) {
                var6.printStackTrace();
            }
        }
        return null;
    }

    public <T> void invalidate(SuperByteBufferCache.Compartment<T> compartment, T key) {
        ((Cache) this.caches.get(compartment)).invalidate(key);
    }

    public <T> void invalidate(SuperByteBufferCache.Compartment<?> compartment) {
        ((Cache) this.caches.get(compartment)).invalidateAll();
    }

    public void invalidate() {
        this.caches.forEach((compartment, cache) -> cache.invalidateAll());
    }

    public static class Compartment<T> {
    }
}