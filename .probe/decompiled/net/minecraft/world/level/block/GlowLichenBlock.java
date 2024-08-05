package net.minecraft.world.level.block;

import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class GlowLichenBlock extends MultifaceBlock implements BonemealableBlock, SimpleWaterloggedBlock {

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public GlowLichenBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
    }

    public static ToIntFunction<BlockState> emission(int int0) {
        return p_181221_ -> MultifaceBlock.hasAnyFace(p_181221_) ? int0 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        super.createBlockStateDefinition(stateDefinitionBuilderBlockBlockState0);
        stateDefinitionBuilderBlockBlockState0.add(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        return !blockPlaceContext1.m_43722_().is(Items.GLOW_LICHEN) || super.canBeReplaced(blockState0, blockPlaceContext1);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return Direction.stream().anyMatch(p_153316_ -> this.spreader.canSpreadInAnyDirection(blockState2, levelReader0, blockPos1, p_153316_.getOpposite()));
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        this.spreader.spreadFromRandomFaceTowardRandomDirection(blockState3, serverLevel0, blockPos2, randomSource1);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_60819_().isEmpty();
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }
}