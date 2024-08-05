package io.github.lightman314.lightmanscurrency.common.core.groups;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class RegistryObjectBundle<T, L> {

    private final Comparator<L> sorter;

    private boolean locked = false;

    private final Map<L, RegistryObject<T>> values = new HashMap();

    public RegistryObjectBundle<T, L> lock() {
        this.locked = true;
        return this;
    }

    public RegistryObjectBundle(@Nonnull Comparator<L> sorter) {
        this.sorter = sorter;
    }

    public void put(L key, RegistryObject<T> value) {
        if (this.locked) {
            LightmansCurrency.LogWarning("Attempted to put an object in the bundle after it's been locked.");
        } else if (this.values.containsKey(key)) {
            LightmansCurrency.LogWarning("Attempted to put a second object with key '" + key.toString() + "' into the registry bundle.");
        } else {
            this.values.put(key, value);
        }
    }

    public RegistryObject<T> getRegistryObject(L key) {
        return this.values.containsKey(key) ? (RegistryObject) this.values.get(key) : null;
    }

    public T get(L key) {
        RegistryObject<T> obj = this.getRegistryObject(key);
        return obj != null ? obj.get() : null;
    }

    public Collection<RegistryObject<T>> getAllRegistryObjects() {
        return this.values.values();
    }

    public List<T> getAll() {
        List<T> values = new ArrayList();
        for (RegistryObject<T> value : this.getAllRegistryObjects()) {
            values.add(value.get());
        }
        return values;
    }

    public List<ResourceLocation> getAllKeys() {
        return this.values.values().stream().map(RegistryObject::getId).toList();
    }

    @SafeVarargs
    public final List<T> getSome(L... keys) {
        List<T> values = new ArrayList();
        for (L key : keys) {
            values.add(this.get(key));
        }
        return values;
    }

    private List<L> getKeysSorted() {
        return this.getKeysSorted(this.sorter);
    }

    private List<L> getKeysSorted(Comparator<L> sorter) {
        List<L> keys = new ArrayList(this.values.keySet());
        keys.sort(sorter);
        return keys;
    }

    public List<T> getAllSorted() {
        return this.getAllSorted(BundleRequestFilter.ALL);
    }

    public List<T> getAllSorted(BundleRequestFilter filter) {
        return this.getAllSorted(filter, this.sorter);
    }

    public List<T> getAllSorted(Comparator<L> sorter) {
        return this.getAllSorted(BundleRequestFilter.ALL, sorter);
    }

    public List<T> getAllSorted(BundleRequestFilter filter, Comparator<L> sorter) {
        List<L> keys = this.getKeysSorted(sorter).stream().filter(filter::filterKey).toList();
        List<T> result = new ArrayList();
        for (L key : keys) {
            result.add(this.get(key));
        }
        return result;
    }

    public List<Supplier<T>> getSupplier() {
        List<Supplier<T>> result = new ArrayList();
        for (L key : this.values.keySet()) {
            result.add((Supplier) () -> this.get(key));
        }
        return result;
    }

    public void forEach(BiConsumer<L, RegistryObject<T>> consumer) {
        for (L key : this.getKeysSorted(this.sorter)) {
            consumer.accept(key, (RegistryObject) this.values.get(key));
        }
    }
}