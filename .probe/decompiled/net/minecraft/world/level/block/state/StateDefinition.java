package net.minecraft.world.level.block.state;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.properties.Property;

public class StateDefinition<O, S extends StateHolder<O, S>> {

    static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");

    private final O owner;

    private final ImmutableSortedMap<String, Property<?>> propertiesByName;

    private final ImmutableList<S> states;

    protected StateDefinition(Function<O, S> functionOS0, O o1, StateDefinition.Factory<O, S> stateDefinitionFactoryOS2, Map<String, Property<?>> mapStringProperty3) {
        this.owner = o1;
        this.propertiesByName = ImmutableSortedMap.copyOf(mapStringProperty3);
        Supplier<S> $$4 = () -> (StateHolder) functionOS0.apply(o1);
        MapCodec<S> $$5 = MapCodec.of(Encoder.empty(), Decoder.unit($$4));
        UnmodifiableIterator $$7 = this.propertiesByName.entrySet().iterator();
        while ($$7.hasNext()) {
            Entry<String, Property<?>> $$6 = (Entry<String, Property<?>>) $$7.next();
            $$5 = appendPropertyCodec($$5, $$4, (String) $$6.getKey(), (Property) $$6.getValue());
        }
        MapCodec<S> $$7x = $$5;
        Map<Map<Property<?>, Comparable<?>>, S> $$8 = Maps.newLinkedHashMap();
        List<S> $$9 = Lists.newArrayList();
        Stream<List<Pair<Property<?>, Comparable<?>>>> $$10 = Stream.of(Collections.emptyList());
        UnmodifiableIterator var11 = this.propertiesByName.values().iterator();
        while (var11.hasNext()) {
            Property<?> $$11 = (Property<?>) var11.next();
            $$10 = $$10.flatMap(p_61072_ -> $$11.getPossibleValues().stream().map(p_155961_ -> {
                List<Pair<Property<?>, Comparable<?>>> $$3 = Lists.newArrayList(p_61072_);
                $$3.add(Pair.of($$11, p_155961_));
                return $$3;
            }));
        }
        $$10.forEach(p_61063_ -> {
            ImmutableMap<Property<?>, Comparable<?>> $$6 = (ImmutableMap<Property<?>, Comparable<?>>) p_61063_.stream().collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
            S $$7xx = stateDefinitionFactoryOS2.create(o1, $$6, $$7);
            $$8.put($$6, $$7xx);
            $$9.add($$7xx);
        });
        for (S $$12 : $$9) {
            $$12.populateNeighbours($$8);
        }
        this.states = ImmutableList.copyOf($$9);
    }

    private static <S extends StateHolder<?, S>, T extends Comparable<T>> MapCodec<S> appendPropertyCodec(MapCodec<S> mapCodecS0, Supplier<S> supplierS1, String string2, Property<T> propertyT3) {
        return Codec.mapPair(mapCodecS0, propertyT3.valueCodec().fieldOf(string2).orElseGet(p_187541_ -> {
        }, () -> propertyT3.value((StateHolder<?, ?>) supplierS1.get()))).xmap(p_187536_ -> (StateHolder) ((StateHolder) p_187536_.getFirst()).setValue(propertyT3, ((Property.Value) p_187536_.getSecond()).value()), p_187533_ -> Pair.of(p_187533_, propertyT3.value(p_187533_)));
    }

    public ImmutableList<S> getPossibleStates() {
        return this.states;
    }

    public S any() {
        return (S) this.states.get(0);
    }

    public O getOwner() {
        return this.owner;
    }

    public Collection<Property<?>> getProperties() {
        return this.propertiesByName.values();
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("block", this.owner).add("properties", this.propertiesByName.values().stream().map(Property::m_61708_).collect(Collectors.toList())).toString();
    }

    @Nullable
    public Property<?> getProperty(String string0) {
        return (Property<?>) this.propertiesByName.get(string0);
    }

    public static class Builder<O, S extends StateHolder<O, S>> {

        private final O owner;

        private final Map<String, Property<?>> properties = Maps.newHashMap();

        public Builder(O o0) {
            this.owner = o0;
        }

        public StateDefinition.Builder<O, S> add(Property<?>... property0) {
            for (Property<?> $$1 : property0) {
                this.validateProperty($$1);
                this.properties.put($$1.getName(), $$1);
            }
            return this;
        }

        private <T extends Comparable<T>> void validateProperty(Property<T> propertyT0) {
            String $$1 = propertyT0.getName();
            if (!StateDefinition.NAME_PATTERN.matcher($$1).matches()) {
                throw new IllegalArgumentException(this.owner + " has invalidly named property: " + $$1);
            } else {
                Collection<T> $$2 = propertyT0.getPossibleValues();
                if ($$2.size() <= 1) {
                    throw new IllegalArgumentException(this.owner + " attempted use property " + $$1 + " with <= 1 possible values");
                } else {
                    for (T $$3 : $$2) {
                        String $$4 = propertyT0.getName($$3);
                        if (!StateDefinition.NAME_PATTERN.matcher($$4).matches()) {
                            throw new IllegalArgumentException(this.owner + " has property: " + $$1 + " with invalidly named value: " + $$4);
                        }
                    }
                    if (this.properties.containsKey($$1)) {
                        throw new IllegalArgumentException(this.owner + " has duplicate property: " + $$1);
                    }
                }
            }
        }

        public StateDefinition<O, S> create(Function<O, S> functionOS0, StateDefinition.Factory<O, S> stateDefinitionFactoryOS1) {
            return new StateDefinition<>(functionOS0, this.owner, stateDefinitionFactoryOS1, this.properties);
        }
    }

    public interface Factory<O, S> {

        S create(O var1, ImmutableMap<Property<?>, Comparable<?>> var2, MapCodec<S> var3);
    }
}