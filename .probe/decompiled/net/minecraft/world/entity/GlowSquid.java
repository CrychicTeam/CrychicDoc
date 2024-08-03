package net.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class GlowSquid extends Squid {

    private static final EntityDataAccessor<Integer> DATA_DARK_TICKS_REMAINING = SynchedEntityData.defineId(GlowSquid.class, EntityDataSerializers.INT);

    public GlowSquid(EntityType<? extends GlowSquid> entityTypeExtendsGlowSquid0, Level level1) {
        super(entityTypeExtendsGlowSquid0, level1);
    }

    @Override
    protected ParticleOptions getInkParticle() {
        return ParticleTypes.GLOW_SQUID_INK;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_DARK_TICKS_REMAINING, 0);
    }

    @Override
    protected SoundEvent getSquirtSound() {
        return SoundEvents.GLOW_SQUID_SQUIRT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GLOW_SQUID_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.GLOW_SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GLOW_SQUID_DEATH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putInt("DarkTicksRemaining", this.getDarkTicksRemaining());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.setDarkTicks(compoundTag0.getInt("DarkTicksRemaining"));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        int $$0 = this.getDarkTicksRemaining();
        if ($$0 > 0) {
            this.setDarkTicks($$0 - 1);
        }
        this.m_9236_().addParticle(ParticleTypes.GLOW, this.m_20208_(0.6), this.m_20187_(), this.m_20262_(0.6), 0.0, 0.0, 0.0);
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        boolean $$2 = super.hurt(damageSource0, float1);
        if ($$2) {
            this.setDarkTicks(100);
        }
        return $$2;
    }

    private void setDarkTicks(int int0) {
        this.f_19804_.set(DATA_DARK_TICKS_REMAINING, int0);
    }

    public int getDarkTicksRemaining() {
        return this.f_19804_.get(DATA_DARK_TICKS_REMAINING);
    }

    public static boolean checkGlowSquideSpawnRules(EntityType<? extends LivingEntity> entityTypeExtendsLivingEntity0, ServerLevelAccessor serverLevelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return blockPos3.m_123342_() <= serverLevelAccessor1.m_5736_() - 33 && serverLevelAccessor1.m_45524_(blockPos3, 0) == 0 && serverLevelAccessor1.m_8055_(blockPos3).m_60713_(Blocks.WATER);
    }
}