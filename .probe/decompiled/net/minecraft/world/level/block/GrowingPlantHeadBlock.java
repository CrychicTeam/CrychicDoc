package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class GrowingPlantHeadBlock extends GrowingPlantBlock implements BonemealableBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_25;

    public static final int MAX_AGE = 25;

    private final double growPerTickProbability;

    protected GrowingPlantHeadBlock(BlockBehaviour.Properties blockBehaviourProperties0, Direction direction1, VoxelShape voxelShape2, boolean boolean3, double double4) {
        super(blockBehaviourProperties0, direction1, voxelShape2, boolean3);
        this.growPerTickProbability = double4;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AGE, 0));
    }

    @Override
    public BlockState getStateForPlacement(LevelAccessor levelAccessor0) {
        return (BlockState) this.m_49966_().m_61124_(AGE, levelAccessor0.getRandom().nextInt(25));
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(AGE) < 25;
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Integer) blockState0.m_61143_(AGE) < 25 && randomSource3.nextDouble() < this.growPerTickProbability) {
            BlockPos $$4 = blockPos2.relative(this.f_53859_);
            if (this.canGrowInto(serverLevel1.m_8055_($$4))) {
                serverLevel1.m_46597_($$4, this.getGrowIntoState(blockState0, serverLevel1.f_46441_));
            }
        }
    }

    protected BlockState getGrowIntoState(BlockState blockState0, RandomSource randomSource1) {
        return (BlockState) blockState0.m_61122_(AGE);
    }

    public BlockState getMaxAgeState(BlockState blockState0) {
        return (BlockState) blockState0.m_61124_(AGE, 25);
    }

    public boolean isMaxAge(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(AGE) == 25;
    }

    protected BlockState updateBodyAfterConvertedFromHead(BlockState blockState0, BlockState blockState1) {
        return blockState1;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (direction1 == this.f_53859_.getOpposite() && !blockState0.m_60710_(levelAccessor3, blockPos4)) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
        }
        if (direction1 != this.f_53859_ || !blockState2.m_60713_(this) && !blockState2.m_60713_(this.m_7777_())) {
            if (this.f_53860_) {
                levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
            }
            return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        } else {
            return this.updateBodyAfterConvertedFromHead(blockState0, this.m_7777_().defaultBlockState());
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return this.canGrowInto(levelReader0.m_8055_(blockPos1.relative(this.f_53859_)));
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        BlockPos $$4 = blockPos2.relative(this.f_53859_);
        int $$5 = Math.min((Integer) blockState3.m_61143_(AGE) + 1, 25);
        int $$6 = this.getBlocksToGrowWhenBonemealed(randomSource1);
        for (int $$7 = 0; $$7 < $$6 && this.canGrowInto(serverLevel0.m_8055_($$4)); $$7++) {
            serverLevel0.m_46597_($$4, (BlockState) blockState3.m_61124_(AGE, $$5));
            $$4 = $$4.relative(this.f_53859_);
            $$5 = Math.min($$5 + 1, 25);
        }
    }

    protected abstract int getBlocksToGrowWhenBonemealed(RandomSource var1);

    protected abstract boolean canGrowInto(BlockState var1);

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return this;
    }
}