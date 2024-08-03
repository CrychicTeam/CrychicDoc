package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

public class MagmaCube extends Slime {

    public MagmaCube(EntityType<? extends MagmaCube> entityTypeExtendsMagmaCube0, Level level1) {
        super(entityTypeExtendsMagmaCube0, level1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    public static boolean checkMagmaCubeSpawnRules(EntityType<MagmaCube> entityTypeMagmaCube0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return levelAccessor1.getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        return levelReader0.m_45784_(this) && !levelReader0.containsAnyLiquid(this.m_20191_());
    }

    @Override
    public void setSize(int int0, boolean boolean1) {
        super.setSize(int0, boolean1);
        this.m_21051_(Attributes.ARMOR).setBaseValue((double) (int0 * 3));
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    protected ParticleOptions getParticleType() {
        return ParticleTypes.FLAME;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected int getJumpDelay() {
        return super.getJumpDelay() * 4;
    }

    @Override
    protected void decreaseSquish() {
        this.f_33581_ *= 0.9F;
    }

    @Override
    protected void jumpFromGround() {
        Vec3 $$0 = this.m_20184_();
        float $$1 = (float) this.m_33632_() * 0.1F;
        this.m_20334_($$0.x, (double) (this.m_6118_() + $$1), $$0.z);
        this.f_19812_ = true;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> tagKeyFluid0) {
        if (tagKeyFluid0 == FluidTags.LAVA) {
            Vec3 $$1 = this.m_20184_();
            this.m_20334_($$1.x, (double) (0.22F + (float) this.m_33632_() * 0.05F), $$1.z);
            this.f_19812_ = true;
        } else {
            super.m_203347_(tagKeyFluid0);
        }
    }

    @Override
    protected boolean isDealsDamage() {
        return this.m_21515_();
    }

    @Override
    protected float getAttackDamage() {
        return super.getAttackDamage() + 2.0F;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return this.m_33633_() ? SoundEvents.MAGMA_CUBE_HURT_SMALL : SoundEvents.MAGMA_CUBE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.m_33633_() ? SoundEvents.MAGMA_CUBE_DEATH_SMALL : SoundEvents.MAGMA_CUBE_DEATH;
    }

    @Override
    protected SoundEvent getSquishSound() {
        return this.m_33633_() ? SoundEvents.MAGMA_CUBE_SQUISH_SMALL : SoundEvents.MAGMA_CUBE_SQUISH;
    }

    @Override
    protected SoundEvent getJumpSound() {
        return SoundEvents.MAGMA_CUBE_JUMP;
    }
}