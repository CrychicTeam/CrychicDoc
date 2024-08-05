package com.simibubi.create.foundation.utility;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.world.level.LevelAccessor;

public class WorldAttached<T> {

    static List<WeakReference<Map<LevelAccessor, ?>>> allMaps = new ArrayList();

    private final Map<LevelAccessor, T> attached;

    private final Function<LevelAccessor, T> factory;

    public WorldAttached(Function<LevelAccessor, T> factory) {
        this.factory = factory;
        this.attached = new WeakHashMap();
        allMaps.add(new WeakReference(this.attached));
    }

    public static void invalidateWorld(LevelAccessor world) {
        Iterator<WeakReference<Map<LevelAccessor, ?>>> i = allMaps.iterator();
        while (i.hasNext()) {
            Map<LevelAccessor, ?> map = (Map<LevelAccessor, ?>) ((WeakReference) i.next()).get();
            if (map == null) {
                i.remove();
            } else {
                map.remove(world);
            }
        }
    }

    @Nonnull
    public T get(LevelAccessor world) {
        T t = (T) this.attached.get(world);
        if (t != null) {
            return t;
        } else {
            T entry = (T) this.factory.apply(world);
            this.put(world, entry);
            return entry;
        }
    }

    public void put(LevelAccessor world, T entry) {
        this.attached.put(world, entry);
    }

    @Nonnull
    public T replace(LevelAccessor world) {
        this.attached.remove(world);
        return this.get(world);
    }

    @Nonnull
    public T replace(LevelAccessor world, Consumer<T> finalizer) {
        T remove = (T) this.attached.remove(world);
        if (remove != null) {
            finalizer.accept(remove);
        }
        return this.get(world);
    }

    public void empty(BiConsumer<LevelAccessor, T> finalizer) {
        this.attached.forEach(finalizer);
        this.attached.clear();
    }

    public void empty(Consumer<T> finalizer) {
        this.attached.values().forEach(finalizer);
        this.attached.clear();
    }
}