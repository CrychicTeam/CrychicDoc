package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.DefaultStateBlockMethod;
import dev.xkmc.l2modularblock.one.LightBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BaseTraitMethod implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, LightBlockMethod {

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TraitSpawnerBlock.STATE);
    }

    public BlockState getDefaultState(BlockState state) {
        return (BlockState) state.m_61124_(TraitSpawnerBlock.STATE, TraitSpawnerBlock.State.IDLE);
    }

    public int getLightValue(BlockState state, BlockGetter level, BlockPos pos) {
        return ((TraitSpawnerBlock.State) state.m_61143_(TraitSpawnerBlock.STATE)).light();
    }
}