package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class Husk extends Zombie {

    public Husk(EntityType<? extends Husk> entityTypeExtendsHusk0, Level level1) {
        super(entityTypeExtendsHusk0, level1);
    }

    public static boolean checkHuskSpawnRules(EntityType<Husk> entityTypeHusk0, ServerLevelAccessor serverLevelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return m_219013_(entityTypeHusk0, serverLevelAccessor1, mobSpawnType2, blockPos3, randomSource4) && (mobSpawnType2 == MobSpawnType.SPAWNER || serverLevelAccessor1.m_45527_(blockPos3));
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.HUSK_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.HUSK_STEP;
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        boolean $$1 = super.doHurtTarget(entity0);
        if ($$1 && this.m_21205_().isEmpty() && entity0 instanceof LivingEntity) {
            float $$2 = this.m_9236_().getCurrentDifficultyAt(this.m_20183_()).getEffectiveDifficulty();
            ((LivingEntity) entity0).addEffect(new MobEffectInstance(MobEffects.HUNGER, 140 * (int) $$2), this);
        }
        return $$1;
    }

    @Override
    protected boolean convertsInWater() {
        return true;
    }

    @Override
    protected void doUnderWaterConversion() {
        this.m_34310_(EntityType.ZOMBIE);
        if (!this.m_20067_()) {
            this.m_9236_().m_5898_(null, 1041, this.m_20183_(), 0);
        }
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}