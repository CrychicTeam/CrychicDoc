package org.embeddedt.modernfix.blockstate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeStateMap<S> implements Map<Map<Property<?>, Comparable<?>>, S> {

    private final Map<Property<?>, Comparable<?>>[] keys;

    private final Object[] values;

    private int usedSlots;

    public FakeStateMap(int numStates) {
        this.keys = new Map[numStates];
        this.values = new Object[numStates];
        this.usedSlots = 0;
    }

    public int size() {
        return this.usedSlots;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean containsKey(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object o) {
        throw new UnsupportedOperationException();
    }

    public S get(Object o) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public S put(Map<Property<?>, Comparable<?>> propertyComparableMap, S s) {
        this.keys[this.usedSlots] = propertyComparableMap;
        this.values[this.usedSlots] = s;
        this.usedSlots++;
        return null;
    }

    public S remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public void putAll(@NotNull Map<? extends Map<Property<?>, Comparable<?>>, ? extends S> map) {
        for (Entry<? extends Map<Property<?>, Comparable<?>>, ? extends S> entry : map.entrySet()) {
            this.put((Map<Property<?>, Comparable<?>>) entry.getKey(), (S) entry.getValue());
        }
    }

    public void clear() {
        for (int i = 0; i < this.keys.length; i++) {
            this.keys[i] = null;
            this.values[i] = null;
        }
        this.usedSlots = 0;
    }

    @NotNull
    public Set<Map<Property<?>, Comparable<?>>> keySet() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public Collection<S> values() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public Set<Entry<Map<Property<?>, Comparable<?>>, S>> entrySet() {
        return new Set<Entry<Map<Property<?>, Comparable<?>>, S>>() {

            public int size() {
                return FakeStateMap.this.usedSlots;
            }

            public boolean isEmpty() {
                return FakeStateMap.this.isEmpty();
            }

            public boolean contains(Object o) {
                throw new UnsupportedOperationException();
            }

            @NotNull
            public Iterator<Entry<Map<Property<?>, Comparable<?>>, S>> iterator() {
                return new Iterator<Entry<Map<Property<?>, Comparable<?>>, S>>() {

                    int currentIdx = 0;

                    public boolean hasNext() {
                        return this.currentIdx < FakeStateMap.this.usedSlots;
                    }

                    public Entry<Map<Property<?>, Comparable<?>>, S> next() {
                        if (this.currentIdx >= FakeStateMap.this.usedSlots) {
                            throw new IndexOutOfBoundsException();
                        } else {
                            Entry<Map<Property<?>, Comparable<?>>, S> entry = new SimpleImmutableEntry(FakeStateMap.this.keys[this.currentIdx], FakeStateMap.this.values[this.currentIdx]);
                            this.currentIdx++;
                            return entry;
                        }
                    }
                };
            }

            @NotNull
            public Object[] toArray() {
                throw new UnsupportedOperationException();
            }

            @NotNull
            public <T> T[] toArray(@NotNull T[] ts) {
                throw new UnsupportedOperationException();
            }

            public boolean add(Entry<Map<Property<?>, Comparable<?>>, S> mapSEntry) {
                throw new UnsupportedOperationException();
            }

            public boolean remove(Object o) {
                throw new UnsupportedOperationException();
            }

            public boolean containsAll(@NotNull Collection<?> collection) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(@NotNull Collection<? extends Entry<Map<Property<?>, Comparable<?>>, S>> collection) {
                throw new UnsupportedOperationException();
            }

            public boolean retainAll(@NotNull Collection<?> collection) {
                throw new UnsupportedOperationException();
            }

            public boolean removeAll(@NotNull Collection<?> collection) {
                throw new UnsupportedOperationException();
            }

            public void clear() {
                throw new UnsupportedOperationException();
            }
        };
    }
}