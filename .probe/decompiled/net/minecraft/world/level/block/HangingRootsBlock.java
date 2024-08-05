package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HangingRootsBlock extends Block implements SimpleWaterloggedBlock {

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.box(2.0, 10.0, 2.0, 14.0, 16.0, 14.0);

    protected HangingRootsBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = super.getStateForPlacement(blockPlaceContext0);
        if ($$1 != null) {
            FluidState $$2 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
            return (BlockState) $$1.m_61124_(WATERLOGGED, $$2.getType() == Fluids.WATER);
        } else {
            return null;
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.above();
        BlockState $$4 = levelReader1.m_8055_($$3);
        return $$4.m_60783_(levelReader1, $$3, Direction.DOWN);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (direction1 == Direction.UP && !this.canSurvive(blockState0, levelAccessor3, blockPos4)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
                levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
            }
            return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        }
    }
}