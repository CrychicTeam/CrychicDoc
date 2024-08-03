package com.simibubi.create.foundation.utility;

import com.simibubi.create.Create;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

public class AttachedRegistry<K, V> {

    private static final List<AttachedRegistry<?, ?>> ALL = new ArrayList();

    protected final IForgeRegistry<K> objectRegistry;

    protected final Map<ResourceLocation, V> idMap = new HashMap();

    protected final Map<K, V> objectMap = new IdentityHashMap();

    protected final Map<ResourceLocation, Function<K, V>> deferredRegistrations = new HashMap();

    protected boolean unwrapped = false;

    public AttachedRegistry(IForgeRegistry<K> objectRegistry) {
        this.objectRegistry = objectRegistry;
        ALL.add(this);
    }

    public void register(ResourceLocation id, V value) {
        if (!this.unwrapped) {
            this.idMap.put(id, value);
        } else {
            K object = this.objectRegistry.getValue(id);
            if (object != null) {
                this.objectMap.put(object, value);
            } else {
                Create.LOGGER.warn("Could not get object for id '" + id + "' in AttachedRegistry after unwrapping!");
            }
        }
    }

    public void register(K object, V value) {
        if (this.unwrapped) {
            this.objectMap.put(object, value);
        } else {
            ResourceLocation id = this.objectRegistry.getKey(object);
            if (id != null) {
                this.idMap.put(id, value);
            } else {
                Create.LOGGER.warn("Could not get id of object '" + object + "' in AttachedRegistry before unwrapping!");
            }
        }
    }

    public void registerDeferred(ResourceLocation id, Function<K, V> func) {
        if (!this.unwrapped) {
            this.deferredRegistrations.put(id, func);
        } else {
            K object = this.objectRegistry.getValue(id);
            if (object != null) {
                this.objectMap.put(object, func.apply(object));
            } else {
                Create.LOGGER.warn("Could not get object for id '" + id + "' in AttachedRegistry after unwrapping!");
            }
        }
    }

    public void registerDeferred(K object, Function<K, V> func) {
        if (this.unwrapped) {
            this.objectMap.put(object, func.apply(object));
        } else {
            ResourceLocation id = this.objectRegistry.getKey(object);
            if (id != null) {
                this.deferredRegistrations.put(id, func);
            } else {
                Create.LOGGER.warn("Could not get id of object '" + object + "' in AttachedRegistry before unwrapping!");
            }
        }
    }

    @Nullable
    public V get(ResourceLocation id) {
        if (!this.unwrapped) {
            return (V) this.idMap.get(id);
        } else {
            K object = this.objectRegistry.getValue(id);
            if (object != null) {
                return (V) this.objectMap.get(object);
            } else {
                Create.LOGGER.warn("Could not get object for id '" + id + "' in AttachedRegistry after unwrapping!");
                return null;
            }
        }
    }

    @Nullable
    public V get(K object) {
        if (this.unwrapped) {
            return (V) this.objectMap.get(object);
        } else {
            ResourceLocation id = this.objectRegistry.getKey(object);
            if (id != null) {
                return (V) this.idMap.get(id);
            } else {
                Create.LOGGER.warn("Could not get id of object '" + object + "' in AttachedRegistry before unwrapping!");
                return null;
            }
        }
    }

    public boolean isUnwrapped() {
        return this.unwrapped;
    }

    protected void unwrap() {
        this.deferredRegistrations.forEach((id, func) -> {
            K object = this.objectRegistry.getValue(id);
            if (object != null) {
                this.objectMap.put(object, func.apply(object));
            } else {
                Create.LOGGER.warn("Could not get object for id '" + id + "' in AttachedRegistry during unwrapping!");
            }
        });
        this.idMap.forEach((id, value) -> {
            K object = this.objectRegistry.getValue(id);
            if (object != null) {
                this.objectMap.put(object, value);
            } else {
                Create.LOGGER.warn("Could not get object for id '" + id + "' in AttachedRegistry during unwrapping!");
            }
        });
        this.deferredRegistrations.clear();
        this.idMap.clear();
        this.unwrapped = true;
    }

    public static void unwrapAll() {
        for (AttachedRegistry<?, ?> registry : ALL) {
            registry.unwrap();
        }
    }
}