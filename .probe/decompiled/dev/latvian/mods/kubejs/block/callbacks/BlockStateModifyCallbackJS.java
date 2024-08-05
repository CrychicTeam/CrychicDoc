package dev.latvian.mods.kubejs.block.callbacks;

import com.google.common.collect.ImmutableMap;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockStateModifyCallbackJS {

    private BlockState state;

    public BlockStateModifyCallbackJS(BlockState state) {
        this.state = state;
    }

    @Info("Cycles the property")
    public <T extends Comparable<T>> BlockStateModifyCallbackJS cycle(Property<T> property) {
        this.state = (BlockState) this.state.m_61122_(property);
        return this;
    }

    @Info("Gets the state. If it has been modified, gets the new state")
    public BlockState getState() {
        return this.state;
    }

    public String toString() {
        return this.state.toString();
    }

    @Info("Get the properties this block has that can be changed")
    public Collection<Property<?>> getProperties() {
        return this.state.m_61147_();
    }

    @Info("Checks if this block has the specified property")
    public <T extends Comparable<T>> boolean hasProperty(Property<T> property) {
        return this.state.m_61138_(property);
    }

    @Info("Gets the value of the passed in property")
    public <T extends Comparable<T>> T getValue(Property<T> property) {
        return (T) this.state.m_61143_(property);
    }

    @Info("Gets the value of the pased in property")
    public <T extends Comparable<T>> T get(Property<T> property) {
        return (T) this.state.m_61143_(property);
    }

    @Info("Gets the value of the passed in property as an Optional. If the property does not exist in this block the Optional will be empty")
    public <T extends Comparable<T>> Optional<T> getOptionalValue(Property<T> property) {
        return this.state.m_61145_(property);
    }

    @Info("Sets the value of the specified property")
    public <T extends Comparable<T>, V extends T> BlockStateModifyCallbackJS setValue(Property<T> property, V comparable) {
        this.state = (BlockState) this.state.m_61124_(property, comparable);
        return this;
    }

    @Info("Sets the value of the specified boolean property")
    public BlockStateModifyCallbackJS set(BooleanProperty property, boolean value) {
        this.state = (BlockState) this.state.m_61124_(property, value);
        return this;
    }

    @Info("Sets the value of the specified integer property")
    public BlockStateModifyCallbackJS set(IntegerProperty property, Integer value) {
        this.state = (BlockState) this.state.m_61124_(property, value);
        return this;
    }

    @Info("Sets the value of the specified enum property")
    public <T extends Enum<T> & StringRepresentable> BlockStateModifyCallbackJS set(EnumProperty<T> property, String value) {
        this.state = (BlockState) this.state.m_61124_(property, (Enum) property.getValue(value).get());
        return this;
    }

    public BlockStateModifyCallbackJS populateNeighbours(Map<Map<Property<?>, Comparable<?>>, BlockState> map) {
        this.state.m_61133_(map);
        return this;
    }

    @Info("Get a map of this blocks properties to it's value")
    public ImmutableMap<Property<?>, Comparable<?>> getValues() {
        return this.state.m_61148_();
    }

    @Info("Rotate the block using the specified Rotation")
    public BlockStateModifyCallbackJS rotate(Rotation rotation) {
        this.state = this.state.m_60717_(rotation);
        return this;
    }

    @Info("Mirror the block using the specified Mirror")
    public BlockStateModifyCallbackJS mirror(Mirror mirror) {
        this.state = this.state.m_60715_(mirror);
        return this;
    }

    @Info("Updates the shape of this block. Mostly used in waterloggable blocks to update the water flow")
    public BlockStateModifyCallbackJS updateShape(Direction direction, BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        this.state = this.state.m_60728_(direction, blockState, levelAccessor, blockPos, blockPos2);
        return this;
    }
}