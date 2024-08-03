package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BaseRailBlock extends Block implements SimpleWaterloggedBlock {

    protected static final VoxelShape FLAT_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    protected static final VoxelShape HALF_BLOCK_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final boolean isStraight;

    public static boolean isRail(Level level0, BlockPos blockPos1) {
        return isRail(level0.getBlockState(blockPos1));
    }

    public static boolean isRail(BlockState blockState0) {
        return blockState0.m_204336_(BlockTags.RAILS) && blockState0.m_60734_() instanceof BaseRailBlock;
    }

    protected BaseRailBlock(boolean boolean0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.isStraight = boolean0;
    }

    public boolean isStraight() {
        return this.isStraight;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        RailShape $$4 = blockState0.m_60713_(this) ? (RailShape) blockState0.m_61143_(this.getShapeProperty()) : null;
        return $$4 != null && $$4.isAscending() ? HALF_BLOCK_AABB : FLAT_AABB;
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return m_49936_(levelReader1, blockPos2.below());
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState3.m_60713_(blockState0.m_60734_())) {
            this.updateState(blockState0, level1, blockPos2, boolean4);
        }
    }

    protected BlockState updateState(BlockState blockState0, Level level1, BlockPos blockPos2, boolean boolean3) {
        blockState0 = this.updateDir(level1, blockPos2, blockState0, true);
        if (this.isStraight) {
            level1.neighborChanged(blockState0, blockPos2, this, blockPos2, boolean3);
        }
        return blockState0;
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (!level1.isClientSide && level1.getBlockState(blockPos2).m_60713_(this)) {
            RailShape $$6 = (RailShape) blockState0.m_61143_(this.getShapeProperty());
            if (shouldBeRemoved(blockPos2, level1, $$6)) {
                m_49950_(blockState0, level1, blockPos2);
                level1.removeBlock(blockPos2, boolean5);
            } else {
                this.updateState(blockState0, level1, blockPos2, block3);
            }
        }
    }

    private static boolean shouldBeRemoved(BlockPos blockPos0, Level level1, RailShape railShape2) {
        if (!m_49936_(level1, blockPos0.below())) {
            return true;
        } else {
            switch(railShape2) {
                case ASCENDING_EAST:
                    return !m_49936_(level1, blockPos0.east());
                case ASCENDING_WEST:
                    return !m_49936_(level1, blockPos0.west());
                case ASCENDING_NORTH:
                    return !m_49936_(level1, blockPos0.north());
                case ASCENDING_SOUTH:
                    return !m_49936_(level1, blockPos0.south());
                default:
                    return false;
            }
        }
    }

    protected void updateState(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3) {
    }

    protected BlockState updateDir(Level level0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        if (level0.isClientSide) {
            return blockState2;
        } else {
            RailShape $$4 = (RailShape) blockState2.m_61143_(this.getShapeProperty());
            return new RailState(level0, blockPos1, blockState2).place(level0.m_276867_(blockPos1), boolean3, $$4).getState();
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!boolean4) {
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
            if (((RailShape) blockState0.m_61143_(this.getShapeProperty())).isAscending()) {
                level1.updateNeighborsAt(blockPos2.above(), this);
            }
            if (this.isStraight) {
                level1.updateNeighborsAt(blockPos2, this);
                level1.updateNeighborsAt(blockPos2.below(), this);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState $$1 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        boolean $$2 = $$1.getType() == Fluids.WATER;
        BlockState $$3 = super.defaultBlockState();
        Direction $$4 = blockPlaceContext0.m_8125_();
        boolean $$5 = $$4 == Direction.EAST || $$4 == Direction.WEST;
        return (BlockState) ((BlockState) $$3.m_61124_(this.getShapeProperty(), $$5 ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH)).m_61124_(WATERLOGGED, $$2);
    }

    public abstract Property<RailShape> getShapeProperty();

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }
}