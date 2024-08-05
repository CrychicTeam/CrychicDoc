package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class StandingSignBlock extends SignBlock {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    public StandingSignBlock(BlockBehaviour.Properties blockBehaviourProperties0, WoodType woodType1) {
        super(blockBehaviourProperties0.sound(woodType1.soundType()), woodType1);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(ROTATION, 0)).m_61124_(f_56268_, false));
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return levelReader1.m_8055_(blockPos2.below()).m_280296_();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState $$1 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(ROTATION, RotationSegment.convertToSegment(blockPlaceContext0.m_7074_() + 180.0F))).m_61124_(f_56268_, $$1.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1 == Direction.DOWN && !this.canSurvive(blockState0, levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public float getYRotationDegrees(BlockState blockState0) {
        return RotationSegment.convertToDegrees((Integer) blockState0.m_61143_(ROTATION));
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(ROTATION, rotation1.rotate((Integer) blockState0.m_61143_(ROTATION), 16));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return (BlockState) blockState0.m_61124_(ROTATION, mirror1.mirror((Integer) blockState0.m_61143_(ROTATION), 16));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(ROTATION, f_56268_);
    }
}