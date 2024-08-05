package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
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

public class BaseCoralPlantTypeBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape AABB = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);

    protected BaseCoralPlantTypeBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, true));
    }

    protected void tryScheduleDieTick(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
        if (!scanForWater(blockState0, levelAccessor1, blockPos2)) {
            levelAccessor1.scheduleTick(blockPos2, this, 60 + levelAccessor1.getRandom().nextInt(40));
        }
    }

    protected static boolean scanForWater(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            return true;
        } else {
            for (Direction $$3 : Direction.values()) {
                if (blockGetter1.getFluidState(blockPos2.relative($$3)).is(FluidTags.WATER)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState $$1 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        return (BlockState) this.m_49966_().m_61124_(WATERLOGGED, $$1.is(FluidTags.WATER) && $$1.getAmount() == 8);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return AABB;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return direction1 == Direction.DOWN && !this.canSurvive(blockState0, levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.below();
        return levelReader1.m_8055_($$3).m_60783_(levelReader1, $$3, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }
}