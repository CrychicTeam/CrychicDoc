package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class FallingBlock extends Block implements Fallable {

    public FallingBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        level1.m_186460_(blockPos2, this, this.getDelayAfterPlace());
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        levelAccessor3.scheduleTick(blockPos4, this, this.getDelayAfterPlace());
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (isFree(serverLevel1.m_8055_(blockPos2.below())) && blockPos2.m_123342_() >= serverLevel1.m_141937_()) {
            FallingBlockEntity $$4 = FallingBlockEntity.fall(serverLevel1, blockPos2, blockState0);
            this.falling($$4);
        }
    }

    protected void falling(FallingBlockEntity fallingBlockEntity0) {
    }

    protected int getDelayAfterPlace() {
        return 2;
    }

    public static boolean isFree(BlockState blockState0) {
        return blockState0.m_60795_() || blockState0.m_204336_(BlockTags.FIRE) || blockState0.m_278721_() || blockState0.m_247087_();
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if (randomSource3.nextInt(16) == 0) {
            BlockPos $$4 = blockPos2.below();
            if (isFree(level1.getBlockState($$4))) {
                ParticleUtils.spawnParticleBelow(level1, blockPos2, randomSource3, new BlockParticleOption(ParticleTypes.FALLING_DUST, blockState0));
            }
        }
    }

    public int getDustColor(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return -16777216;
    }
}