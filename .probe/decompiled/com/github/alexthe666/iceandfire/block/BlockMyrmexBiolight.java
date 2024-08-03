package com.github.alexthe666.iceandfire.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

public class BlockMyrmexBiolight extends BushBlock {

    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("down");

    public BlockMyrmexBiolight() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noOcclusion().noCollission().dynamicShape().strength(0.0F).lightLevel(state -> 7).sound(SoundType.GRASS).randomTicks());
        this.m_49959_((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(CONNECTED_DOWN, Boolean.FALSE));
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return worldIn.m_8055_(blockpos).m_60734_() == this || worldIn.m_8055_(blockpos).m_60815_();
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, @NotNull BlockPos facingPos) {
        boolean flag3 = worldIn.m_8055_(currentPos.below()).m_60734_() == this;
        return (BlockState) stateIn.m_61124_(CONNECTED_DOWN, flag3);
    }

    @Override
    public void tick(@NotNull BlockState state, ServerLevel worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (!worldIn.f_46443_) {
            this.updateState(state, worldIn, pos, state.m_60734_());
        }
        if (!worldIn.m_8055_(pos.above()).m_60815_() && worldIn.m_8055_(pos.above()).m_60734_() != this) {
            worldIn.m_46961_(pos, true);
        }
    }

    public void updateState(BlockState state, Level worldIn, BlockPos pos, Block blockIn) {
        boolean flag2 = (Boolean) state.m_61143_(CONNECTED_DOWN);
        boolean flag3 = worldIn.getBlockState(pos.below()).m_60734_() == this;
        if (flag2 != flag3) {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(CONNECTED_DOWN, flag3), 3);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_DOWN);
    }
}