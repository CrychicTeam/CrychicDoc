package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SeaPickleBlock extends BushBlock implements BonemealableBlock, SimpleWaterloggedBlock {

    public static final int MAX_PICKLES = 4;

    public static final IntegerProperty PICKLES = BlockStateProperties.PICKLES;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape ONE_AABB = Block.box(6.0, 0.0, 6.0, 10.0, 6.0, 10.0);

    protected static final VoxelShape TWO_AABB = Block.box(3.0, 0.0, 3.0, 13.0, 6.0, 13.0);

    protected static final VoxelShape THREE_AABB = Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0);

    protected static final VoxelShape FOUR_AABB = Block.box(2.0, 0.0, 2.0, 14.0, 7.0, 14.0);

    protected SeaPickleBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(PICKLES, 1)).m_61124_(WATERLOGGED, true));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = blockPlaceContext0.m_43725_().getBlockState(blockPlaceContext0.getClickedPos());
        if ($$1.m_60713_(this)) {
            return (BlockState) $$1.m_61124_(PICKLES, Math.min(4, (Integer) $$1.m_61143_(PICKLES) + 1));
        } else {
            FluidState $$2 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
            boolean $$3 = $$2.getType() == Fluids.WATER;
            return (BlockState) super.m_5573_(blockPlaceContext0).m_61124_(WATERLOGGED, $$3);
        }
    }

    public static boolean isDead(BlockState blockState0) {
        return !(Boolean) blockState0.m_61143_(WATERLOGGED);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return !blockState0.m_60812_(blockGetter1, blockPos2).getFaceShape(Direction.UP).isEmpty() || blockState0.m_60783_(blockGetter1, blockPos2, Direction.UP);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.below();
        return this.mayPlaceOn(levelReader1.m_8055_($$3), levelReader1, $$3);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (!blockState0.m_60710_(levelAccessor3, blockPos4)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
                levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
            }
            return super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        }
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        return !blockPlaceContext1.m_7078_() && blockPlaceContext1.m_43722_().is(this.m_5456_()) && blockState0.m_61143_(PICKLES) < 4 ? true : super.m_6864_(blockState0, blockPlaceContext1);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        switch(blockState0.m_61143_(PICKLES)) {
            case 1:
            default:
                return ONE_AABB;
            case 2:
                return TWO_AABB;
            case 3:
                return THREE_AABB;
            case 4:
                return FOUR_AABB;
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(PICKLES, WATERLOGGED);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        if (!isDead(blockState3) && serverLevel0.m_8055_(blockPos2.below()).m_204336_(BlockTags.CORAL_BLOCKS)) {
            int $$4 = 5;
            int $$5 = 1;
            int $$6 = 2;
            int $$7 = 0;
            int $$8 = blockPos2.m_123341_() - 2;
            int $$9 = 0;
            for (int $$10 = 0; $$10 < 5; $$10++) {
                for (int $$11 = 0; $$11 < $$5; $$11++) {
                    int $$12 = 2 + blockPos2.m_123342_() - 1;
                    for (int $$13 = $$12 - 2; $$13 < $$12; $$13++) {
                        BlockPos $$14 = new BlockPos($$8 + $$10, $$13, blockPos2.m_123343_() - $$9 + $$11);
                        if ($$14 != blockPos2 && randomSource1.nextInt(6) == 0 && serverLevel0.m_8055_($$14).m_60713_(Blocks.WATER)) {
                            BlockState $$15 = serverLevel0.m_8055_($$14.below());
                            if ($$15.m_204336_(BlockTags.CORAL_BLOCKS)) {
                                serverLevel0.m_7731_($$14, (BlockState) Blocks.SEA_PICKLE.defaultBlockState().m_61124_(PICKLES, randomSource1.nextInt(4) + 1), 3);
                            }
                        }
                    }
                }
                if ($$7 < 2) {
                    $$5 += 2;
                    $$9++;
                } else {
                    $$5 -= 2;
                    $$9--;
                }
                $$7++;
            }
            serverLevel0.m_7731_(blockPos2, (BlockState) blockState3.m_61124_(PICKLES, 4), 2);
        }
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}