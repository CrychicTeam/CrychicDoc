package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FrostedIceBlock extends IceBlock {

    public static final int MAX_AGE = 3;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private static final int NEIGHBORS_TO_AGE = 4;

    private static final int NEIGHBORS_TO_MELT = 2;

    public FrostedIceBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AGE, 0));
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        this.tick(blockState0, serverLevel1, blockPos2, randomSource3);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((randomSource3.nextInt(3) == 0 || this.fewerNeigboursThan(serverLevel1, blockPos2, 4)) && serverLevel1.m_46803_(blockPos2) > 11 - (Integer) blockState0.m_61143_(AGE) - blockState0.m_60739_(serverLevel1, blockPos2) && this.slightlyMelt(blockState0, serverLevel1, blockPos2)) {
            BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
            for (Direction $$5 : Direction.values()) {
                $$4.setWithOffset(blockPos2, $$5);
                BlockState $$6 = serverLevel1.m_8055_($$4);
                if ($$6.m_60713_(this) && !this.slightlyMelt($$6, serverLevel1, $$4)) {
                    serverLevel1.m_186460_($$4, this, Mth.nextInt(randomSource3, 20, 40));
                }
            }
        } else {
            serverLevel1.m_186460_(blockPos2, this, Mth.nextInt(randomSource3, 20, 40));
        }
    }

    private boolean slightlyMelt(BlockState blockState0, Level level1, BlockPos blockPos2) {
        int $$3 = (Integer) blockState0.m_61143_(AGE);
        if ($$3 < 3) {
            level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(AGE, $$3 + 1), 2);
            return false;
        } else {
            this.m_54168_(blockState0, level1, blockPos2);
            return true;
        }
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (block3.defaultBlockState().m_60713_(this) && this.fewerNeigboursThan(level1, blockPos2, 2)) {
            this.m_54168_(blockState0, level1, blockPos2);
        }
        super.m_6861_(blockState0, level1, blockPos2, block3, blockPos4, boolean5);
    }

    private boolean fewerNeigboursThan(BlockGetter blockGetter0, BlockPos blockPos1, int int2) {
        int $$3 = 0;
        BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
        for (Direction $$5 : Direction.values()) {
            $$4.setWithOffset(blockPos1, $$5);
            if (blockGetter0.getBlockState($$4).m_60713_(this)) {
                if (++$$3 >= int2) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return ItemStack.EMPTY;
    }
}