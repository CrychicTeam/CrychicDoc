package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ScaffoldingBlock extends Block implements SimpleWaterloggedBlock {

    private static final int TICK_DELAY = 1;

    private static final VoxelShape STABLE_SHAPE;

    private static final VoxelShape UNSTABLE_SHAPE;

    private static final VoxelShape UNSTABLE_SHAPE_BOTTOM = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    private static final VoxelShape BELOW_BLOCK = Shapes.block().move(0.0, -1.0, 0.0);

    public static final int STABILITY_MAX_DISTANCE = 7;

    public static final IntegerProperty DISTANCE = BlockStateProperties.STABILITY_DISTANCE;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

    protected ScaffoldingBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(DISTANCE, 7)).m_61124_(WATERLOGGED, false)).m_61124_(BOTTOM, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(DISTANCE, WATERLOGGED, BOTTOM);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        if (!collisionContext3.isHoldingItem(blockState0.m_60734_().asItem())) {
            return blockState0.m_61143_(BOTTOM) ? UNSTABLE_SHAPE : STABLE_SHAPE;
        } else {
            return Shapes.block();
        }
    }

    @Override
    public VoxelShape getInteractionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return Shapes.block();
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        return blockPlaceContext1.m_43722_().is(this.m_5456_());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockPos $$1 = blockPlaceContext0.getClickedPos();
        Level $$2 = blockPlaceContext0.m_43725_();
        int $$3 = getDistance($$2, $$1);
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, $$2.getFluidState($$1).getType() == Fluids.WATER)).m_61124_(DISTANCE, $$3)).m_61124_(BOTTOM, this.isBottom($$2, $$1, $$3));
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!level1.isClientSide) {
            level1.m_186460_(blockPos2, this, 1);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        if (!levelAccessor3.m_5776_()) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
        }
        return blockState0;
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        int $$4 = getDistance(serverLevel1, blockPos2);
        BlockState $$5 = (BlockState) ((BlockState) blockState0.m_61124_(DISTANCE, $$4)).m_61124_(BOTTOM, this.isBottom(serverLevel1, blockPos2, $$4));
        if ((Integer) $$5.m_61143_(DISTANCE) == 7) {
            if ((Integer) blockState0.m_61143_(DISTANCE) == 7) {
                FallingBlockEntity.fall(serverLevel1, blockPos2, $$5);
            } else {
                serverLevel1.m_46961_(blockPos2, true);
            }
        } else if (blockState0 != $$5) {
            serverLevel1.m_7731_(blockPos2, $$5, 3);
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return getDistance(levelReader1, blockPos2) < 7;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        if (collisionContext3.isAbove(Shapes.block(), blockPos2, true) && !collisionContext3.isDescending()) {
            return STABLE_SHAPE;
        } else {
            return blockState0.m_61143_(DISTANCE) != 0 && blockState0.m_61143_(BOTTOM) && collisionContext3.isAbove(BELOW_BLOCK, blockPos2, true) ? UNSTABLE_SHAPE_BOTTOM : Shapes.empty();
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    private boolean isBottom(BlockGetter blockGetter0, BlockPos blockPos1, int int2) {
        return int2 > 0 && !blockGetter0.getBlockState(blockPos1.below()).m_60713_(this);
    }

    public static int getDistance(BlockGetter blockGetter0, BlockPos blockPos1) {
        BlockPos.MutableBlockPos $$2 = blockPos1.mutable().move(Direction.DOWN);
        BlockState $$3 = blockGetter0.getBlockState($$2);
        int $$4 = 7;
        if ($$3.m_60713_(Blocks.SCAFFOLDING)) {
            $$4 = (Integer) $$3.m_61143_(DISTANCE);
        } else if ($$3.m_60783_(blockGetter0, $$2, Direction.UP)) {
            return 0;
        }
        for (Direction $$5 : Direction.Plane.HORIZONTAL) {
            BlockState $$6 = blockGetter0.getBlockState($$2.setWithOffset(blockPos1, $$5));
            if ($$6.m_60713_(Blocks.SCAFFOLDING)) {
                $$4 = Math.min($$4, (Integer) $$6.m_61143_(DISTANCE) + 1);
                if ($$4 == 1) {
                    break;
                }
            }
        }
        return $$4;
    }

    static {
        VoxelShape $$0 = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
        VoxelShape $$1 = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 2.0);
        VoxelShape $$2 = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 2.0);
        VoxelShape $$3 = Block.box(0.0, 0.0, 14.0, 2.0, 16.0, 16.0);
        VoxelShape $$4 = Block.box(14.0, 0.0, 14.0, 16.0, 16.0, 16.0);
        STABLE_SHAPE = Shapes.or($$0, $$1, $$2, $$3, $$4);
        VoxelShape $$5 = Block.box(0.0, 0.0, 0.0, 2.0, 2.0, 16.0);
        VoxelShape $$6 = Block.box(14.0, 0.0, 0.0, 16.0, 2.0, 16.0);
        VoxelShape $$7 = Block.box(0.0, 0.0, 14.0, 16.0, 2.0, 16.0);
        VoxelShape $$8 = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 2.0);
        UNSTABLE_SHAPE = Shapes.or(ScaffoldingBlock.UNSTABLE_SHAPE_BOTTOM, STABLE_SHAPE, $$6, $$5, $$8, $$7);
    }
}