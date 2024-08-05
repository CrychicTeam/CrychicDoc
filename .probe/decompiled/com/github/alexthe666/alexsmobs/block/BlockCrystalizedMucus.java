package com.github.alexthe666.alexsmobs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;

public class BlockCrystalizedMucus extends AbstractGlassBlock {

    public static final int DECAY_DISTANCE = 7;

    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;

    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    private static final int TICK_DELAY = 1;

    protected BlockCrystalizedMucus() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.1F).sound(SoundType.GLASS).noOcclusion().isSuffocating((s, s1, s2) -> false).isViewBlocking((s, s1, s2) -> false));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(DISTANCE, 7)).m_61124_(PERSISTENT, false));
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(DISTANCE) == 7 && !(Boolean) blockState0.m_61143_(PERSISTENT);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (this.decaying(blockState0)) {
            m_49950_(blockState0, serverLevel1, blockPos2);
            serverLevel1.m_7471_(blockPos2, true);
        }
    }

    protected boolean decaying(BlockState blockState0) {
        return !(Boolean) blockState0.m_61143_(PERSISTENT) && (Integer) blockState0.m_61143_(DISTANCE) == 7;
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        serverLevel1.m_7731_(blockPos2, updateDistance(blockState0, serverLevel1, blockPos2), 3);
    }

    @Override
    public int getLightBlock(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return 1;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        int i = getDistanceAt(blockState2) + 1;
        if (i != 1 || (Integer) blockState0.m_61143_(DISTANCE) != i) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
        }
        return blockState0;
    }

    private static BlockState updateDistance(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
        int i = 7;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(blockPos2, direction);
            i = Math.min(i, getDistanceAt(levelAccessor1.m_8055_(blockpos$mutableblockpos)) + 1);
            if (i == 1) {
                break;
            }
        }
        return (BlockState) blockState0.m_61124_(DISTANCE, i);
    }

    private static int getDistanceAt(BlockState blockState0) {
        if (blockState0.m_60713_(AMBlockRegistry.BANANA_SLUG_SLIME_BLOCK.get())) {
            return 0;
        } else {
            return blockState0.m_60734_() instanceof BlockCrystalizedMucus ? (Integer) blockState0.m_61143_(DISTANCE) : 7;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(DISTANCE, PERSISTENT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState fluidstate = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        BlockState blockstate = (BlockState) this.m_49966_().m_61124_(PERSISTENT, true);
        return updateDistance(blockstate, blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos());
    }
}