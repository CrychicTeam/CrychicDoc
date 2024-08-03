package org.embeddedt.modernfix.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemMesherMap<K> implements Map<K, ModelResourceLocation> {

    private final Function<K, ModelResourceLocation> getLocation;

    public ItemMesherMap(Function<K, ModelResourceLocation> getLocation) {
        this.getLocation = getLocation;
    }

    public int size() {
        return BuiltInRegistries.ITEM.m_6566_().size();
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(Object key) {
        return true;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public ModelResourceLocation get(Object key) {
        return (ModelResourceLocation) this.getLocation.apply(key);
    }

    @Nullable
    public ModelResourceLocation put(K key, ModelResourceLocation value) {
        throw new UnsupportedOperationException();
    }

    public ModelResourceLocation remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public void putAll(@NotNull Map<? extends K, ? extends ModelResourceLocation> m) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public Collection<ModelResourceLocation> values() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public Set<Entry<K, ModelResourceLocation>> entrySet() {
        throw new UnsupportedOperationException();
    }
}