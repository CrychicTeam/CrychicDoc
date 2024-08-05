package org.embeddedt.modernfix.entity;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import org.embeddedt.modernfix.ModernFix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityRendererMap implements Map<EntityType<?>, EntityRenderer<?>> {

    private final Map<EntityType<?>, EntityRendererProvider<?>> rendererProviders;

    private final LoadingCache<EntityType<?>, EntityRenderer<?>> rendererMap;

    private final EntityRendererProvider.Context context;

    public EntityRendererMap(Map<EntityType<?>, EntityRendererProvider<?>> rendererProviders, EntityRendererProvider.Context context) {
        this.rendererProviders = rendererProviders;
        this.context = context;
        this.rendererMap = CacheBuilder.newBuilder().build(new EntityRendererMap.RenderConstructor());
    }

    public int size() {
        return this.rendererProviders.size();
    }

    public boolean isEmpty() {
        return this.rendererProviders.isEmpty();
    }

    public boolean containsKey(Object o) {
        return this.rendererProviders.containsKey(o);
    }

    public boolean containsValue(Object o) {
        return false;
    }

    public EntityRenderer<?> get(Object o) {
        try {
            EntityRenderer<?> renderer = (EntityRenderer<?>) this.rendererMap.get((EntityType) o);
            if (renderer == null) {
                throw new AssertionError("Returned entity renderer should never be null");
            } else {
                return renderer;
            }
        } catch (IllegalStateException var3) {
            return null;
        } catch (ExecutionException var4) {
            throw new RuntimeException(var4);
        }
    }

    @Nullable
    public EntityRenderer<?> put(EntityType<?> entityType, EntityRenderer<?> entityRenderer) {
        EntityRenderer<?> old = (EntityRenderer<?>) this.rendererMap.getIfPresent(entityType);
        this.rendererMap.put(entityType, entityRenderer);
        return old;
    }

    public EntityRenderer<?> remove(Object o) {
        EntityRenderer<?> r = (EntityRenderer<?>) this.rendererMap.getIfPresent(o);
        this.rendererMap.invalidate(o);
        return r;
    }

    public void putAll(@NotNull Map<? extends EntityType<?>, ? extends EntityRenderer<?>> map) {
        this.rendererMap.putAll(map);
    }

    public void clear() {
        this.rendererMap.invalidateAll();
    }

    @NotNull
    public Set<EntityType<?>> keySet() {
        return this.rendererProviders.keySet();
    }

    @NotNull
    public Collection<EntityRenderer<?>> values() {
        return this.rendererMap.asMap().values();
    }

    @NotNull
    public Set<Entry<EntityType<?>, EntityRenderer<?>>> entrySet() {
        return this.rendererMap.asMap().entrySet();
    }

    class RenderConstructor extends CacheLoader<EntityType<?>, EntityRenderer<?>> {

        public EntityRenderer<?> load(EntityType<?> key) throws Exception {
            EntityRendererProvider<?> provider = (EntityRendererProvider<?>) EntityRendererMap.this.rendererProviders.get(key);
            synchronized (EntityRenderers.class) {
                EntityRenderer<?> renderer;
                try {
                    if (provider == null) {
                        throw new RuntimeException("Provider not registered");
                    }
                    renderer = provider.create(EntityRendererMap.this.context);
                    ModernFix.LOGGER.info("Loaded entity {}", BuiltInRegistries.ENTITY_TYPE.getKey(key));
                } catch (RuntimeException var7) {
                    ModernFix.LOGGER.error("Failed to create entity model for " + BuiltInRegistries.ENTITY_TYPE.getKey(key) + ":", var7);
                    renderer = new ErroredEntityRenderer(EntityRendererMap.this.context);
                }
                return renderer;
            }
        }
    }
}