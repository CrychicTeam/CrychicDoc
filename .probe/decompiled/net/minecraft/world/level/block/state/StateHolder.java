package net.minecraft.world.level.block.state;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.properties.Property;

public abstract class StateHolder<O, S> {

    public static final String NAME_TAG = "Name";

    public static final String PROPERTIES_TAG = "Properties";

    private static final Function<Entry<Property<?>, Comparable<?>>, String> PROPERTY_ENTRY_TO_STRING_FUNCTION = new Function<Entry<Property<?>, Comparable<?>>, String>() {

        public String apply(@Nullable Entry<Property<?>, Comparable<?>> p_61155_) {
            if (p_61155_ == null) {
                return "<NULL>";
            } else {
                Property<?> $$1 = (Property<?>) p_61155_.getKey();
                return $$1.getName() + "=" + this.getName($$1, (Comparable<?>) p_61155_.getValue());
            }
        }

        private <T extends Comparable<T>> String getName(Property<T> p_61152_, Comparable<?> p_61153_) {
            return p_61152_.getName((T) p_61153_);
        }
    };

    protected final O owner;

    private final ImmutableMap<Property<?>, Comparable<?>> values;

    private Table<Property<?>, Comparable<?>, S> neighbours;

    protected final MapCodec<S> propertiesCodec;

    protected StateHolder(O o0, ImmutableMap<Property<?>, Comparable<?>> immutableMapPropertyComparable1, MapCodec<S> mapCodecS2) {
        this.owner = o0;
        this.values = immutableMapPropertyComparable1;
        this.propertiesCodec = mapCodecS2;
    }

    public <T extends Comparable<T>> S cycle(Property<T> propertyT0) {
        return this.setValue(propertyT0, findNextInCollection(propertyT0.getPossibleValues(), this.getValue(propertyT0)));
    }

    protected static <T> T findNextInCollection(Collection<T> collectionT0, T t1) {
        Iterator<T> $$2 = collectionT0.iterator();
        while ($$2.hasNext()) {
            if ($$2.next().equals(t1)) {
                if ($$2.hasNext()) {
                    return (T) $$2.next();
                }
                return (T) collectionT0.iterator().next();
            }
        }
        return (T) $$2.next();
    }

    public String toString() {
        StringBuilder $$0 = new StringBuilder();
        $$0.append(this.owner);
        if (!this.getValues().isEmpty()) {
            $$0.append('[');
            $$0.append((String) this.getValues().entrySet().stream().map(PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")));
            $$0.append(']');
        }
        return $$0.toString();
    }

    public Collection<Property<?>> getProperties() {
        return Collections.unmodifiableCollection(this.values.keySet());
    }

    public <T extends Comparable<T>> boolean hasProperty(Property<T> propertyT0) {
        return this.values.containsKey(propertyT0);
    }

    public <T extends Comparable<T>> T getValue(Property<T> propertyT0) {
        Comparable<?> $$1 = (Comparable<?>) this.values.get(propertyT0);
        if ($$1 == null) {
            throw new IllegalArgumentException("Cannot get property " + propertyT0 + " as it does not exist in " + this.owner);
        } else {
            return (T) propertyT0.getValueClass().cast($$1);
        }
    }

    public <T extends Comparable<T>> Optional<T> getOptionalValue(Property<T> propertyT0) {
        Comparable<?> $$1 = (Comparable<?>) this.values.get(propertyT0);
        return $$1 == null ? Optional.empty() : Optional.of((Comparable) propertyT0.getValueClass().cast($$1));
    }

    public <T extends Comparable<T>, V extends T> S setValue(Property<T> propertyT0, V v1) {
        Comparable<?> $$2 = (Comparable<?>) this.values.get(propertyT0);
        if ($$2 == null) {
            throw new IllegalArgumentException("Cannot set property " + propertyT0 + " as it does not exist in " + this.owner);
        } else if ($$2.equals(v1)) {
            return (S) this;
        } else {
            S $$3 = (S) this.neighbours.get(propertyT0, v1);
            if ($$3 == null) {
                throw new IllegalArgumentException("Cannot set property " + propertyT0 + " to " + v1 + " on " + this.owner + ", it is not an allowed value");
            } else {
                return $$3;
            }
        }
    }

    public <T extends Comparable<T>, V extends T> S trySetValue(Property<T> propertyT0, V v1) {
        Comparable<?> $$2 = (Comparable<?>) this.values.get(propertyT0);
        if ($$2 != null && !$$2.equals(v1)) {
            S $$3 = (S) this.neighbours.get(propertyT0, v1);
            if ($$3 == null) {
                throw new IllegalArgumentException("Cannot set property " + propertyT0 + " to " + v1 + " on " + this.owner + ", it is not an allowed value");
            } else {
                return $$3;
            }
        } else {
            return (S) this;
        }
    }

    public void populateNeighbours(Map<Map<Property<?>, Comparable<?>>, S> mapMapPropertyComparableS0) {
        if (this.neighbours != null) {
            throw new IllegalStateException();
        } else {
            Table<Property<?>, Comparable<?>, S> $$1 = HashBasedTable.create();
            UnmodifiableIterator var3 = this.values.entrySet().iterator();
            while (var3.hasNext()) {
                Entry<Property<?>, Comparable<?>> $$2 = (Entry<Property<?>, Comparable<?>>) var3.next();
                Property<?> $$3 = (Property<?>) $$2.getKey();
                for (Comparable<?> $$4 : $$3.getPossibleValues()) {
                    if (!$$4.equals($$2.getValue())) {
                        $$1.put($$3, $$4, mapMapPropertyComparableS0.get(this.makeNeighbourValues($$3, $$4)));
                    }
                }
            }
            this.neighbours = (Table<Property<?>, Comparable<?>, S>) ($$1.isEmpty() ? $$1 : ArrayTable.create($$1));
        }
    }

    private Map<Property<?>, Comparable<?>> makeNeighbourValues(Property<?> property0, Comparable<?> comparable1) {
        Map<Property<?>, Comparable<?>> $$2 = Maps.newHashMap(this.values);
        $$2.put(property0, comparable1);
        return $$2;
    }

    public ImmutableMap<Property<?>, Comparable<?>> getValues() {
        return this.values;
    }

    protected static <O, S extends StateHolder<O, S>> Codec<S> codec(Codec<O> codecO0, Function<O, S> functionOS1) {
        return codecO0.dispatch("Name", p_61121_ -> p_61121_.owner, p_187547_ -> {
            S $$2 = (S) functionOS1.apply(p_187547_);
            return $$2.getValues().isEmpty() ? Codec.unit($$2) : $$2.propertiesCodec.codec().optionalFieldOf("Properties").xmap(p_187544_ -> (StateHolder) p_187544_.orElse($$2), Optional::of).codec();
        });
    }
}