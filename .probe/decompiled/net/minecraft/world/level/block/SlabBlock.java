package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SlabBlock extends Block implements SimpleWaterloggedBlock {

    public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    protected static final VoxelShape TOP_AABB = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);

    public SlabBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(TYPE, SlabType.BOTTOM)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return blockState0.m_61143_(TYPE) != SlabType.DOUBLE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(TYPE, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        SlabType $$4 = (SlabType) blockState0.m_61143_(TYPE);
        switch($$4) {
            case DOUBLE:
                return Shapes.block();
            case TOP:
                return TOP_AABB;
            default:
                return BOTTOM_AABB;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockPos $$1 = blockPlaceContext0.getClickedPos();
        BlockState $$2 = blockPlaceContext0.m_43725_().getBlockState($$1);
        if ($$2.m_60713_(this)) {
            return (BlockState) ((BlockState) $$2.m_61124_(TYPE, SlabType.DOUBLE)).m_61124_(WATERLOGGED, false);
        } else {
            FluidState $$3 = blockPlaceContext0.m_43725_().getFluidState($$1);
            BlockState $$4 = (BlockState) ((BlockState) this.m_49966_().m_61124_(TYPE, SlabType.BOTTOM)).m_61124_(WATERLOGGED, $$3.getType() == Fluids.WATER);
            Direction $$5 = blockPlaceContext0.m_43719_();
            return $$5 != Direction.DOWN && ($$5 == Direction.UP || !(blockPlaceContext0.m_43720_().y - (double) $$1.m_123342_() > 0.5)) ? $$4 : (BlockState) $$4.m_61124_(TYPE, SlabType.TOP);
        }
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        ItemStack $$2 = blockPlaceContext1.m_43722_();
        SlabType $$3 = (SlabType) blockState0.m_61143_(TYPE);
        if ($$3 == SlabType.DOUBLE || !$$2.is(this.m_5456_())) {
            return false;
        } else if (blockPlaceContext1.replacingClickedOnBlock()) {
            boolean $$4 = blockPlaceContext1.m_43720_().y - (double) blockPlaceContext1.getClickedPos().m_123342_() > 0.5;
            Direction $$5 = blockPlaceContext1.m_43719_();
            return $$3 == SlabType.BOTTOM ? $$5 == Direction.UP || $$4 && $$5.getAxis().isHorizontal() : $$5 == Direction.DOWN || !$$4 && $$5.getAxis().isHorizontal();
        } else {
            return true;
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, FluidState fluidState3) {
        return blockState2.m_61143_(TYPE) != SlabType.DOUBLE ? SimpleWaterloggedBlock.super.placeLiquid(levelAccessor0, blockPos1, blockState2, fluidState3) : false;
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, Fluid fluid3) {
        return blockState2.m_61143_(TYPE) != SlabType.DOUBLE ? SimpleWaterloggedBlock.super.canPlaceLiquid(blockGetter0, blockPos1, blockState2, fluid3) : false;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        switch(pathComputationType3) {
            case LAND:
                return false;
            case WATER:
                return blockGetter1.getFluidState(blockPos2).is(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }
}