package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class BuddingAmethystBlock extends AmethystBlock {

    public static final int GROWTH_CHANCE = 5;

    private static final Direction[] DIRECTIONS = Direction.values();

    public BuddingAmethystBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (randomSource3.nextInt(5) == 0) {
            Direction $$4 = DIRECTIONS[randomSource3.nextInt(DIRECTIONS.length)];
            BlockPos $$5 = blockPos2.relative($$4);
            BlockState $$6 = serverLevel1.m_8055_($$5);
            Block $$7 = null;
            if (canClusterGrowAtState($$6)) {
                $$7 = Blocks.SMALL_AMETHYST_BUD;
            } else if ($$6.m_60713_(Blocks.SMALL_AMETHYST_BUD) && $$6.m_61143_(AmethystClusterBlock.FACING) == $$4) {
                $$7 = Blocks.MEDIUM_AMETHYST_BUD;
            } else if ($$6.m_60713_(Blocks.MEDIUM_AMETHYST_BUD) && $$6.m_61143_(AmethystClusterBlock.FACING) == $$4) {
                $$7 = Blocks.LARGE_AMETHYST_BUD;
            } else if ($$6.m_60713_(Blocks.LARGE_AMETHYST_BUD) && $$6.m_61143_(AmethystClusterBlock.FACING) == $$4) {
                $$7 = Blocks.AMETHYST_CLUSTER;
            }
            if ($$7 != null) {
                BlockState $$8 = (BlockState) ((BlockState) $$7.defaultBlockState().m_61124_(AmethystClusterBlock.FACING, $$4)).m_61124_(AmethystClusterBlock.WATERLOGGED, $$6.m_60819_().getType() == Fluids.WATER);
                serverLevel1.m_46597_($$5, $$8);
            }
        }
    }

    public static boolean canClusterGrowAtState(BlockState blockState0) {
        return blockState0.m_60795_() || blockState0.m_60713_(Blocks.WATER) && blockState0.m_60819_().getAmount() == 8;
    }
}