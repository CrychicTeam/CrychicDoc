package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WitherRoseBlock extends FlowerBlock {

    public WitherRoseBlock(MobEffect mobEffect0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(mobEffect0, 8, blockBehaviourProperties1);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return super.m_6266_(blockState0, blockGetter1, blockPos2) || blockState0.m_60713_(Blocks.NETHERRACK) || blockState0.m_60713_(Blocks.SOUL_SAND) || blockState0.m_60713_(Blocks.SOUL_SOIL);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        VoxelShape $$4 = this.m_5940_(blockState0, level1, blockPos2, CollisionContext.empty());
        Vec3 $$5 = $$4.bounds().getCenter();
        double $$6 = (double) blockPos2.m_123341_() + $$5.x;
        double $$7 = (double) blockPos2.m_123343_() + $$5.z;
        for (int $$8 = 0; $$8 < 3; $$8++) {
            if (randomSource3.nextBoolean()) {
                level1.addParticle(ParticleTypes.SMOKE, $$6 + randomSource3.nextDouble() / 5.0, (double) blockPos2.m_123342_() + (0.5 - randomSource3.nextDouble()), $$7 + randomSource3.nextDouble() / 5.0, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (!level1.isClientSide && level1.m_46791_() != Difficulty.PEACEFUL) {
            if (entity3 instanceof LivingEntity $$4 && !$$4.m_6673_(level1.damageSources().wither())) {
                $$4.addEffect(new MobEffectInstance(MobEffects.WITHER, 40));
            }
        }
    }
}