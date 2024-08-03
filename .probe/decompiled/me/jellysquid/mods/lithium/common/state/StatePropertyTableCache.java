package me.jellysquid.mods.lithium.common.state;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class StatePropertyTableCache {

    public static final FastImmutableTableCache<Property<?>, Comparable<?>, BlockState> BLOCK_STATE_TABLE = new FastImmutableTableCache<>();

    public static final FastImmutableTableCache<Property<?>, Comparable<?>, FluidState> FLUID_STATE_TABLE = new FastImmutableTableCache<>();

    public static <S, O> FastImmutableTableCache<Property<?>, Comparable<?>, S> getTableCache(O owner) {
        if (owner instanceof Block) {
            return (FastImmutableTableCache<Property<?>, Comparable<?>, S>) BLOCK_STATE_TABLE;
        } else if (owner instanceof Fluid) {
            return (FastImmutableTableCache<Property<?>, Comparable<?>, S>) FLUID_STATE_TABLE;
        } else {
            throw new IllegalArgumentException("");
        }
    }
}