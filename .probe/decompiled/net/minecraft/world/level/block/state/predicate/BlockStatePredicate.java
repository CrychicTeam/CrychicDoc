package net.minecraft.world.level.block.state.predicate;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockStatePredicate implements Predicate<BlockState> {

    public static final Predicate<BlockState> ANY = p_61299_ -> true;

    private final StateDefinition<Block, BlockState> definition;

    private final Map<Property<?>, Predicate<Object>> properties = Maps.newHashMap();

    private BlockStatePredicate(StateDefinition<Block, BlockState> stateDefinitionBlockBlockState0) {
        this.definition = stateDefinitionBlockBlockState0;
    }

    public static BlockStatePredicate forBlock(Block block0) {
        return new BlockStatePredicate(block0.getStateDefinition());
    }

    public boolean test(@Nullable BlockState blockState0) {
        if (blockState0 != null && blockState0.m_60734_().equals(this.definition.getOwner())) {
            if (this.properties.isEmpty()) {
                return true;
            } else {
                for (Entry<Property<?>, Predicate<Object>> $$1 : this.properties.entrySet()) {
                    if (!this.applies(blockState0, (Property) $$1.getKey(), (Predicate<Object>) $$1.getValue())) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    protected <T extends Comparable<T>> boolean applies(BlockState blockState0, Property<T> propertyT1, Predicate<Object> predicateObject2) {
        T $$3 = (T) blockState0.m_61143_(propertyT1);
        return predicateObject2.test($$3);
    }

    public <V extends Comparable<V>> BlockStatePredicate where(Property<V> propertyV0, Predicate<Object> predicateObject1) {
        if (!this.definition.getProperties().contains(propertyV0)) {
            throw new IllegalArgumentException(this.definition + " cannot support property " + propertyV0);
        } else {
            this.properties.put(propertyV0, predicateObject1);
            return this;
        }
    }
}