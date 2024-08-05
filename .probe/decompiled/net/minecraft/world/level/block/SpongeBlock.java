package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SpongeBlock extends Block {

    public static final int MAX_DEPTH = 6;

    public static final int MAX_COUNT = 64;

    private static final Direction[] ALL_DIRECTIONS = Direction.values();

    protected SpongeBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState3.m_60713_(blockState0.m_60734_())) {
            this.tryAbsorbWater(level1, blockPos2);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        this.tryAbsorbWater(level1, blockPos2);
        super.m_6861_(blockState0, level1, blockPos2, block3, blockPos4, boolean5);
    }

    protected void tryAbsorbWater(Level level0, BlockPos blockPos1) {
        if (this.removeWaterBreadthFirstSearch(level0, blockPos1)) {
            level0.setBlock(blockPos1, Blocks.WET_SPONGE.defaultBlockState(), 2);
            level0.m_46796_(2001, blockPos1, Block.getId(Blocks.WATER.defaultBlockState()));
        }
    }

    private boolean removeWaterBreadthFirstSearch(Level level0, BlockPos blockPos1) {
        return BlockPos.breadthFirstTraversal(blockPos1, 6, 65, (p_277519_, p_277492_) -> {
            for (Direction $$2 : ALL_DIRECTIONS) {
                p_277492_.accept(p_277519_.relative($$2));
            }
        }, p_279054_ -> {
            if (p_279054_.equals(blockPos1)) {
                return true;
            } else {
                BlockState $$3 = level0.getBlockState(p_279054_);
                FluidState $$4 = level0.getFluidState(p_279054_);
                if (!$$4.is(FluidTags.WATER)) {
                    return false;
                } else {
                    if ($$3.m_60734_() instanceof BucketPickup $$6 && !$$6.pickupBlock(level0, p_279054_, $$3).isEmpty()) {
                        return true;
                    }
                    if ($$3.m_60734_() instanceof LiquidBlock) {
                        level0.setBlock(p_279054_, Blocks.AIR.defaultBlockState(), 3);
                    } else {
                        if (!$$3.m_60713_(Blocks.KELP) && !$$3.m_60713_(Blocks.KELP_PLANT) && !$$3.m_60713_(Blocks.SEAGRASS) && !$$3.m_60713_(Blocks.TALL_SEAGRASS)) {
                            return false;
                        }
                        BlockEntity $$7 = $$3.m_155947_() ? level0.getBlockEntity(p_279054_) : null;
                        m_49892_($$3, level0, p_279054_, $$7);
                        level0.setBlock(p_279054_, Blocks.AIR.defaultBlockState(), 3);
                    }
                    return true;
                }
            }
        }) > 1;
    }
}