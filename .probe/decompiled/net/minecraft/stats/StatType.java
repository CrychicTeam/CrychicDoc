package net.minecraft.stats;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

public class StatType<T> implements Iterable<Stat<T>> {

    private final Registry<T> registry;

    private final Map<T, Stat<T>> map = new IdentityHashMap();

    @Nullable
    private Component displayName;

    public StatType(Registry<T> registryT0) {
        this.registry = registryT0;
    }

    public boolean contains(T t0) {
        return this.map.containsKey(t0);
    }

    public Stat<T> get(T t0, StatFormatter statFormatter1) {
        return (Stat<T>) this.map.computeIfAbsent(t0, p_12896_ -> new Stat<>(this, (T) p_12896_, statFormatter1));
    }

    public Registry<T> getRegistry() {
        return this.registry;
    }

    public Iterator<Stat<T>> iterator() {
        return this.map.values().iterator();
    }

    public Stat<T> get(T t0) {
        return this.get(t0, StatFormatter.DEFAULT);
    }

    public String getTranslationKey() {
        return "stat_type." + BuiltInRegistries.STAT_TYPE.getKey(this).toString().replace(':', '.');
    }

    public Component getDisplayName() {
        if (this.displayName == null) {
            this.displayName = Component.translatable(this.getTranslationKey());
        }
        return this.displayName;
    }
}