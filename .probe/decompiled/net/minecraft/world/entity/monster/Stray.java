package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class Stray extends AbstractSkeleton {

    public Stray(EntityType<? extends Stray> entityTypeExtendsStray0, Level level1) {
        super(entityTypeExtendsStray0, level1);
    }

    public static boolean checkStraySpawnRules(EntityType<Stray> entityTypeStray0, ServerLevelAccessor serverLevelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        BlockPos $$5 = blockPos3;
        do {
            $$5 = $$5.above();
        } while (serverLevelAccessor1.m_8055_($$5).m_60713_(Blocks.POWDER_SNOW));
        return m_219013_(entityTypeStray0, serverLevelAccessor1, mobSpawnType2, blockPos3, randomSource4) && (mobSpawnType2 == MobSpawnType.SPAWNER || serverLevelAccessor1.m_45527_($$5.below()));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.STRAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    @Override
    SoundEvent getStepSound() {
        return SoundEvents.STRAY_STEP;
    }

    @Override
    protected AbstractArrow getArrow(ItemStack itemStack0, float float1) {
        AbstractArrow $$2 = super.getArrow(itemStack0, float1);
        if ($$2 instanceof Arrow) {
            ((Arrow) $$2).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
        }
        return $$2;
    }
}