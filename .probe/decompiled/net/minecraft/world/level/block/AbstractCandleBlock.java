package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractCandleBlock extends Block {

    public static final int LIGHT_PER_CANDLE = 3;

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected AbstractCandleBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    protected abstract Iterable<Vec3> getParticleOffsets(BlockState var1);

    public static boolean isLit(BlockState blockState0) {
        return blockState0.m_61138_(LIT) && (blockState0.m_204336_(BlockTags.CANDLES) || blockState0.m_204336_(BlockTags.CANDLE_CAKES)) && (Boolean) blockState0.m_61143_(LIT);
    }

    @Override
    public void onProjectileHit(Level level0, BlockState blockState1, BlockHitResult blockHitResult2, Projectile projectile3) {
        if (!level0.isClientSide && projectile3.m_6060_() && this.canBeLit(blockState1)) {
            setLit(level0, blockState1, blockHitResult2.getBlockPos(), true);
        }
    }

    protected boolean canBeLit(BlockState blockState0) {
        return !(Boolean) blockState0.m_61143_(LIT);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(LIT)) {
            this.getParticleOffsets(blockState0).forEach(p_220695_ -> addParticlesAndSound(level1, p_220695_.add((double) blockPos2.m_123341_(), (double) blockPos2.m_123342_(), (double) blockPos2.m_123343_()), randomSource3));
        }
    }

    private static void addParticlesAndSound(Level level0, Vec3 vec1, RandomSource randomSource2) {
        float $$3 = randomSource2.nextFloat();
        if ($$3 < 0.3F) {
            level0.addParticle(ParticleTypes.SMOKE, vec1.x, vec1.y, vec1.z, 0.0, 0.0, 0.0);
            if ($$3 < 0.17F) {
                level0.playLocalSound(vec1.x + 0.5, vec1.y + 0.5, vec1.z + 0.5, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + randomSource2.nextFloat(), randomSource2.nextFloat() * 0.7F + 0.3F, false);
            }
        }
        level0.addParticle(ParticleTypes.SMALL_FLAME, vec1.x, vec1.y, vec1.z, 0.0, 0.0, 0.0);
    }

    public static void extinguish(@Nullable Player player0, BlockState blockState1, LevelAccessor levelAccessor2, BlockPos blockPos3) {
        setLit(levelAccessor2, blockState1, blockPos3, false);
        if (blockState1.m_60734_() instanceof AbstractCandleBlock) {
            ((AbstractCandleBlock) blockState1.m_60734_()).getParticleOffsets(blockState1).forEach(p_151926_ -> levelAccessor2.addParticle(ParticleTypes.SMOKE, (double) blockPos3.m_123341_() + p_151926_.x(), (double) blockPos3.m_123342_() + p_151926_.y(), (double) blockPos3.m_123343_() + p_151926_.z(), 0.0, 0.1F, 0.0));
        }
        levelAccessor2.playSound(null, blockPos3, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        levelAccessor2.gameEvent(player0, GameEvent.BLOCK_CHANGE, blockPos3);
    }

    private static void setLit(LevelAccessor levelAccessor0, BlockState blockState1, BlockPos blockPos2, boolean boolean3) {
        levelAccessor0.m_7731_(blockPos2, (BlockState) blockState1.m_61124_(LIT, boolean3), 11);
    }
}