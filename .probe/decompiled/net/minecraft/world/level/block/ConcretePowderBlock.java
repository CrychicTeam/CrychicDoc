package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ConcretePowderBlock extends FallingBlock {

    private final BlockState concrete;

    public ConcretePowderBlock(Block block0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.concrete = block0.defaultBlockState();
    }

    @Override
    public void onLand(Level level0, BlockPos blockPos1, BlockState blockState2, BlockState blockState3, FallingBlockEntity fallingBlockEntity4) {
        if (shouldSolidify(level0, blockPos1, blockState3)) {
            level0.setBlock(blockPos1, this.concrete, 3);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockGetter $$1 = blockPlaceContext0.m_43725_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        return shouldSolidify($$1, $$2, $$3) ? this.concrete : super.m_5573_(blockPlaceContext0);
    }

    private static boolean shouldSolidify(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return canSolidify(blockState2) || touchesLiquid(blockGetter0, blockPos1);
    }

    private static boolean touchesLiquid(BlockGetter blockGetter0, BlockPos blockPos1) {
        boolean $$2 = false;
        BlockPos.MutableBlockPos $$3 = blockPos1.mutable();
        for (Direction $$4 : Direction.values()) {
            BlockState $$5 = blockGetter0.getBlockState($$3);
            if ($$4 != Direction.DOWN || canSolidify($$5)) {
                $$3.setWithOffset(blockPos1, $$4);
                $$5 = blockGetter0.getBlockState($$3);
                if (canSolidify($$5) && !$$5.m_60783_(blockGetter0, blockPos1, $$4.getOpposite())) {
                    $$2 = true;
                    break;
                }
            }
        }
        return $$2;
    }

    private static boolean canSolidify(BlockState blockState0) {
        return blockState0.m_60819_().is(FluidTags.WATER);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return touchesLiquid(levelAccessor3, blockPos4) ? this.concrete : super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public int getDustColor(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_284242_(blockGetter1, blockPos2).col;
    }
}