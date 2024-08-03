package malte0811.ferritecore.fastmap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class FastMap<Value> {

    private static final int INVALID_INDEX = -1;

    private final List<FastMapKey<?>> keys;

    private final List<Value> valueMatrix;

    private final Object2IntMap<Property<?>> toKeyIndex;

    private final ImmutableSet<Property<?>> propertySet;

    public FastMap(Collection<Property<?>> properties, Map<Map<Property<?>, Comparable<?>>, Value> valuesMap, boolean compact) {
        List<FastMapKey<?>> keys = new ArrayList(properties.size());
        int factorUpTo = 1;
        this.toKeyIndex = new Object2IntOpenHashMap();
        this.toKeyIndex.defaultReturnValue(-1);
        for (Property<?> prop : properties) {
            this.toKeyIndex.put(prop, keys.size());
            FastMapKey<?> nextKey;
            if (compact) {
                nextKey = new CompactFastMapKey<>(prop, factorUpTo);
            } else {
                nextKey = new BinaryFastMapKey<>(prop, factorUpTo);
            }
            keys.add(nextKey);
            factorUpTo *= nextKey.getFactorToNext();
        }
        this.keys = ImmutableList.copyOf(keys);
        List<Value> valuesList = new ArrayList(factorUpTo);
        for (int i = 0; i < factorUpTo; i++) {
            valuesList.add(null);
        }
        for (Entry<Map<Property<?>, Comparable<?>>, Value> state : valuesMap.entrySet()) {
            valuesList.set(this.getIndexOf((Map<Property<?>, Comparable<?>>) state.getKey()), state.getValue());
        }
        this.valueMatrix = Collections.unmodifiableList(valuesList);
        this.propertySet = ImmutableSet.copyOf(properties);
    }

    @Nullable
    public <T extends Comparable<T>> Value with(int oldIndex, Property<T> prop, T value) {
        FastMapKey<T> keyToChange = this.getKeyFor(prop);
        if (keyToChange == null) {
            return null;
        } else {
            int newIndex = keyToChange.replaceIn(oldIndex, value);
            return (Value) (newIndex < 0 ? null : this.valueMatrix.get(newIndex));
        }
    }

    public int getIndexOf(Map<Property<?>, Comparable<?>> state) {
        int id = 0;
        for (FastMapKey<?> k : this.keys) {
            id += k.toPartialMapIndex((Comparable<?>) state.get(k.getProperty()));
        }
        return id;
    }

    @Nullable
    public <T extends Comparable<T>> T getValue(int stateIndex, Property<T> property) {
        FastMapKey<T> propId = this.getKeyFor(property);
        return propId == null ? null : propId.getValue(stateIndex);
    }

    @Nullable
    public Comparable<?> getValue(int stateIndex, Object key) {
        return key instanceof Property ? this.getValue(stateIndex, (Property<Comparable<?>>) key) : null;
    }

    public Entry<Property<?>, Comparable<?>> getEntry(int propertyIndex, int stateIndex) {
        return new SimpleImmutableEntry(this.getKey(propertyIndex).getProperty(), this.getKey(propertyIndex).getValue(stateIndex));
    }

    public <T extends Comparable<T>> Value withUnsafe(int globalTableIndex, Property<T> property, Object newValue) {
        return this.with(globalTableIndex, property, (T) newValue);
    }

    public int numProperties() {
        return this.keys.size();
    }

    public FastMapKey<?> getKey(int keyIndex) {
        return (FastMapKey<?>) this.keys.get(keyIndex);
    }

    @Nullable
    private <T extends Comparable<T>> FastMapKey<T> getKeyFor(Property<T> prop) {
        int index = this.toKeyIndex.getInt(prop);
        return (FastMapKey<T>) (index == -1 ? null : this.getKey(index));
    }

    public boolean isSingleState() {
        return this.valueMatrix.size() == 1;
    }

    public ImmutableSet<Property<?>> getPropertySet() {
        return this.propertySet;
    }
}