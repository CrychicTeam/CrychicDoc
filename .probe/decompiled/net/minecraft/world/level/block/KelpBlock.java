package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

public class KelpBlock extends GrowingPlantHeadBlock implements LiquidBlockContainer {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);

    private static final double GROW_PER_TICK_PROBABILITY = 0.14;

    protected KelpBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0, Direction.UP, SHAPE, true, 0.14);
    }

    @Override
    protected boolean canGrowInto(BlockState blockState0) {
        return blockState0.m_60713_(Blocks.WATER);
    }

    @Override
    protected Block getBodyBlock() {
        return Blocks.KELP_PLANT;
    }

    @Override
    protected boolean canAttachTo(BlockState blockState0) {
        return !blockState0.m_60713_(Blocks.MAGMA_BLOCK);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, Fluid fluid3) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, FluidState fluidState3) {
        return false;
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource randomSource0) {
        return 1;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState $$1 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        return $$1.is(FluidTags.WATER) && $$1.getAmount() == 8 ? super.m_5573_(blockPlaceContext0) : null;
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return Fluids.WATER.getSource(false);
    }
}