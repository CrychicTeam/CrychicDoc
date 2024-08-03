package net.minecraftforge.client.model.generators;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.google.gson.JsonObject;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class VariantBlockStateBuilder implements IGeneratedBlockState {

    private final Block owner;

    private final Map<VariantBlockStateBuilder.PartialBlockstate, BlockStateProvider.ConfiguredModelList> models = new LinkedHashMap();

    private final Set<BlockState> coveredStates = new HashSet();

    VariantBlockStateBuilder(Block owner) {
        this.owner = owner;
    }

    public Map<VariantBlockStateBuilder.PartialBlockstate, BlockStateProvider.ConfiguredModelList> getModels() {
        return this.models;
    }

    public Block getOwner() {
        return this.owner;
    }

    @Override
    public JsonObject toJson() {
        List<BlockState> missingStates = Lists.newArrayList(this.owner.getStateDefinition().getPossibleStates());
        missingStates.removeAll(this.coveredStates);
        Preconditions.checkState(missingStates.isEmpty(), "Blockstate for block %s does not cover all states. Missing: %s", this.owner, missingStates);
        JsonObject variants = new JsonObject();
        this.getModels().entrySet().stream().sorted(Entry.comparingByKey(VariantBlockStateBuilder.PartialBlockstate.comparingByProperties())).forEach(entry -> variants.add(((VariantBlockStateBuilder.PartialBlockstate) entry.getKey()).toString(), ((BlockStateProvider.ConfiguredModelList) entry.getValue()).toJSON()));
        JsonObject main = new JsonObject();
        main.add("variants", variants);
        return main;
    }

    public VariantBlockStateBuilder addModels(VariantBlockStateBuilder.PartialBlockstate state, ConfiguredModel... models) {
        Preconditions.checkNotNull(state, "state must not be null");
        Preconditions.checkArgument(models.length > 0, "Cannot set models to empty array");
        Preconditions.checkArgument(state.getOwner() == this.owner, "Cannot set models for a different block. Found: %s, Current: %s", state.getOwner(), this.owner);
        if (!this.models.containsKey(state)) {
            Preconditions.checkArgument(this.disjointToAll(state), "Cannot set models for a state for which a partial match has already been configured");
            this.models.put(state, new BlockStateProvider.ConfiguredModelList(models));
            UnmodifiableIterator var3 = this.owner.getStateDefinition().getPossibleStates().iterator();
            while (var3.hasNext()) {
                BlockState fullState = (BlockState) var3.next();
                if (state.test(fullState)) {
                    this.coveredStates.add(fullState);
                }
            }
        } else {
            this.models.compute(state, ($, cml) -> cml.append(models));
        }
        return this;
    }

    public VariantBlockStateBuilder setModels(VariantBlockStateBuilder.PartialBlockstate state, ConfiguredModel... model) {
        Preconditions.checkArgument(!this.models.containsKey(state), "Cannot set models for a state that has already been configured: %s", state);
        this.addModels(state, model);
        return this;
    }

    private boolean disjointToAll(VariantBlockStateBuilder.PartialBlockstate newState) {
        return this.coveredStates.stream().noneMatch(newState);
    }

    public VariantBlockStateBuilder.PartialBlockstate partialState() {
        return new VariantBlockStateBuilder.PartialBlockstate(this.owner, this);
    }

    public VariantBlockStateBuilder forAllStates(Function<BlockState, ConfiguredModel[]> mapper) {
        return this.forAllStatesExcept(mapper);
    }

    public VariantBlockStateBuilder forAllStatesExcept(Function<BlockState, ConfiguredModel[]> mapper, Property<?>... ignored) {
        Set<VariantBlockStateBuilder.PartialBlockstate> seen = new HashSet();
        UnmodifiableIterator var4 = this.owner.getStateDefinition().getPossibleStates().iterator();
        while (var4.hasNext()) {
            BlockState fullState = (BlockState) var4.next();
            Map<Property<?>, Comparable<?>> propertyValues = Maps.newLinkedHashMap(fullState.m_61148_());
            for (Property<?> p : ignored) {
                propertyValues.remove(p);
            }
            VariantBlockStateBuilder.PartialBlockstate partialState = new VariantBlockStateBuilder.PartialBlockstate(this.owner, propertyValues, this);
            if (seen.add(partialState)) {
                this.setModels(partialState, (ConfiguredModel[]) mapper.apply(fullState));
            }
        }
        return this;
    }

    public static class PartialBlockstate implements Predicate<BlockState> {

        private final Block owner;

        private final SortedMap<Property<?>, Comparable<?>> setStates;

        @Nullable
        private final VariantBlockStateBuilder outerBuilder;

        PartialBlockstate(Block owner, @Nullable VariantBlockStateBuilder outerBuilder) {
            this(owner, ImmutableMap.of(), outerBuilder);
        }

        PartialBlockstate(Block owner, Map<Property<?>, Comparable<?>> setStates, @Nullable VariantBlockStateBuilder outerBuilder) {
            this.owner = owner;
            this.outerBuilder = outerBuilder;
            for (Entry<Property<?>, Comparable<?>> entry : setStates.entrySet()) {
                Property<?> prop = (Property<?>) entry.getKey();
                Comparable<?> value = (Comparable<?>) entry.getValue();
                Preconditions.checkArgument(owner.getStateDefinition().getProperties().contains(prop), "Property %s not found on block %s", entry, this.owner);
                Preconditions.checkArgument(prop.getPossibleValues().contains(value), "%s is not a valid value for %s", value, prop);
            }
            this.setStates = Maps.newTreeMap(Comparator.comparing(Property::m_61708_));
            this.setStates.putAll(setStates);
        }

        public <T extends Comparable<T>> VariantBlockStateBuilder.PartialBlockstate with(Property<T> prop, T value) {
            Preconditions.checkArgument(!this.setStates.containsKey(prop), "Property %s has already been set", prop);
            Map<Property<?>, Comparable<?>> newState = new HashMap(this.setStates);
            newState.put(prop, value);
            return new VariantBlockStateBuilder.PartialBlockstate(this.owner, newState, this.outerBuilder);
        }

        private void checkValidOwner() {
            Preconditions.checkNotNull(this.outerBuilder, "Partial blockstate must have a valid owner to perform this action");
        }

        public ConfiguredModel.Builder<VariantBlockStateBuilder> modelForState() {
            this.checkValidOwner();
            return ConfiguredModel.builder(this.outerBuilder, this);
        }

        public VariantBlockStateBuilder.PartialBlockstate addModels(ConfiguredModel... models) {
            this.checkValidOwner();
            this.outerBuilder.addModels(this, models);
            return this;
        }

        public VariantBlockStateBuilder setModels(ConfiguredModel... models) {
            this.checkValidOwner();
            return this.outerBuilder.setModels(this, models);
        }

        public VariantBlockStateBuilder.PartialBlockstate partialState() {
            this.checkValidOwner();
            return this.outerBuilder.partialState();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                VariantBlockStateBuilder.PartialBlockstate that = (VariantBlockStateBuilder.PartialBlockstate) o;
                return this.owner.equals(that.owner) && this.setStates.equals(that.setStates);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.owner, this.setStates });
        }

        public Block getOwner() {
            return this.owner;
        }

        public SortedMap<Property<?>, Comparable<?>> getSetStates() {
            return this.setStates;
        }

        public boolean test(BlockState blockState) {
            if (blockState.m_60734_() != this.getOwner()) {
                return false;
            } else {
                for (Entry<Property<?>, Comparable<?>> entry : this.setStates.entrySet()) {
                    if (blockState.m_61143_((Property) entry.getKey()) != entry.getValue()) {
                        return false;
                    }
                }
                return true;
            }
        }

        public String toString() {
            StringBuilder ret = new StringBuilder();
            for (Entry<Property<?>, Comparable<?>> entry : this.setStates.entrySet()) {
                if (ret.length() > 0) {
                    ret.append(',');
                }
                ret.append(((Property) entry.getKey()).getName()).append('=').append(((Property) entry.getKey()).getName((Comparable) entry.getValue()));
            }
            return ret.toString();
        }

        public static Comparator<VariantBlockStateBuilder.PartialBlockstate> comparingByProperties() {
            return (s1, s2) -> {
                SortedSet<Property<?>> propUniverse = new TreeSet(s1.getSetStates().comparator().reversed());
                propUniverse.addAll(s1.getSetStates().keySet());
                propUniverse.addAll(s2.getSetStates().keySet());
                for (Property<?> prop : propUniverse) {
                    Comparable val1 = (Comparable) s1.getSetStates().get(prop);
                    Comparable val2 = (Comparable) s2.getSetStates().get(prop);
                    if (val1 == null && val2 != null) {
                        return -1;
                    }
                    if (val2 == null && val1 != null) {
                        return 1;
                    }
                    if (val1 != null && val2 != null) {
                        int cmp = val1.compareTo(val2);
                        if (cmp != 0) {
                            return cmp;
                        }
                    }
                }
                return 0;
            };
        }
    }
}