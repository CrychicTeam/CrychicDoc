package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class ChorusPlantBlock extends PipeBlock {

    protected ChorusPlantBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(0.3125F, blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_55148_, false)).m_61124_(f_55149_, false)).m_61124_(f_55150_, false)).m_61124_(f_55151_, false)).m_61124_(f_55152_, false)).m_61124_(f_55153_, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return this.getStateForPlacement(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos());
    }

    public BlockState getStateForPlacement(BlockGetter blockGetter0, BlockPos blockPos1) {
        BlockState $$2 = blockGetter0.getBlockState(blockPos1.below());
        BlockState $$3 = blockGetter0.getBlockState(blockPos1.above());
        BlockState $$4 = blockGetter0.getBlockState(blockPos1.north());
        BlockState $$5 = blockGetter0.getBlockState(blockPos1.east());
        BlockState $$6 = blockGetter0.getBlockState(blockPos1.south());
        BlockState $$7 = blockGetter0.getBlockState(blockPos1.west());
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_55153_, $$2.m_60713_(this) || $$2.m_60713_(Blocks.CHORUS_FLOWER) || $$2.m_60713_(Blocks.END_STONE))).m_61124_(f_55152_, $$3.m_60713_(this) || $$3.m_60713_(Blocks.CHORUS_FLOWER))).m_61124_(f_55148_, $$4.m_60713_(this) || $$4.m_60713_(Blocks.CHORUS_FLOWER))).m_61124_(f_55149_, $$5.m_60713_(this) || $$5.m_60713_(Blocks.CHORUS_FLOWER))).m_61124_(f_55150_, $$6.m_60713_(this) || $$6.m_60713_(Blocks.CHORUS_FLOWER))).m_61124_(f_55151_, $$7.m_60713_(this) || $$7.m_60713_(Blocks.CHORUS_FLOWER));
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (!blockState0.m_60710_(levelAccessor3, blockPos4)) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
            return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        } else {
            boolean $$6 = blockState2.m_60713_(this) || blockState2.m_60713_(Blocks.CHORUS_FLOWER) || direction1 == Direction.DOWN && blockState2.m_60713_(Blocks.END_STONE);
            return (BlockState) blockState0.m_61124_((Property) f_55154_.get(direction1), $$6);
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!blockState0.m_60710_(serverLevel1, blockPos2)) {
            serverLevel1.m_46961_(blockPos2, true);
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockState $$3 = levelReader1.m_8055_(blockPos2.below());
        boolean $$4 = !levelReader1.m_8055_(blockPos2.above()).m_60795_() && !$$3.m_60795_();
        for (Direction $$5 : Direction.Plane.HORIZONTAL) {
            BlockPos $$6 = blockPos2.relative($$5);
            BlockState $$7 = levelReader1.m_8055_($$6);
            if ($$7.m_60713_(this)) {
                if ($$4) {
                    return false;
                }
                BlockState $$8 = levelReader1.m_8055_($$6.below());
                if ($$8.m_60713_(this) || $$8.m_60713_(Blocks.END_STONE)) {
                    return true;
                }
            }
        }
        return $$3.m_60713_(this) || $$3.m_60713_(Blocks.END_STONE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_55148_, f_55149_, f_55150_, f_55151_, f_55152_, f_55153_);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}