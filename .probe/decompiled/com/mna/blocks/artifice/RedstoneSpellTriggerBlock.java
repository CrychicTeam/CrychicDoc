package com.mna.blocks.artifice;

import com.mna.api.blocks.ISpellInteractibleBlock;
import com.mna.api.spells.base.ISpellDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class RedstoneSpellTriggerBlock extends Block implements ISpellInteractibleBlock<RedstoneSpellTriggerBlock> {

    public static final IntegerProperty OUTPUT_POWER = IntegerProperty.create("output_power", 0, 15);

    public RedstoneSpellTriggerBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F));
        this.m_49959_((BlockState) this.m_49966_().m_61124_(OUTPUT_POWER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OUTPUT_POWER);
    }

    @Override
    public boolean onHitBySpell(Level world, BlockPos pos, ISpellDefinition spell) {
        world.setBlockAndUpdate(pos, (BlockState) this.m_49966_().m_61124_(OUTPUT_POWER, 15));
        world.updateNeighborsAt(pos, this);
        if (!world.isClientSide) {
            world.m_186460_(new BlockPos(pos), this, 10);
        }
        return true;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        int power = (Integer) state.m_61143_(OUTPUT_POWER);
        if (power > 0) {
            worldIn.m_46597_(pos, (BlockState) state.m_61124_(OUTPUT_POWER, power - 1));
            worldIn.updateNeighborsAt(pos, this);
            worldIn.m_186460_(new BlockPos(pos), this, 10);
        }
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.m_60746_(blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return (Integer) blockState.m_61143_(OUTPUT_POWER);
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return true;
    }
}