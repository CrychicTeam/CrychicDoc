package net.minecraft.world.entity.animal.horse;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SkeletonHorse extends AbstractHorse {

    private final SkeletonTrapGoal skeletonTrapGoal = new SkeletonTrapGoal(this);

    private static final int TRAP_MAX_LIFE = 18000;

    private boolean isTrap;

    private int trapTime;

    public SkeletonHorse(EntityType<? extends SkeletonHorse> entityTypeExtendsSkeletonHorse0, Level level1) {
        super(entityTypeExtendsSkeletonHorse0, level1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return m_30627_().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void randomizeAttributes(RandomSource randomSource0) {
        this.m_21051_(Attributes.JUMP_STRENGTH).setBaseValue(m_272017_(randomSource0::m_188500_));
    }

    @Override
    protected void addBehaviourGoals() {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_204029_(FluidTags.WATER) ? SoundEvents.SKELETON_HORSE_AMBIENT_WATER : SoundEvents.SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.SKELETON_HORSE_HURT;
    }

    @Override
    protected SoundEvent getSwimSound() {
        if (this.m_20096_()) {
            if (!this.m_20160_()) {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }
            this.f_30524_++;
            if (this.f_30524_ > 5 && this.f_30524_ % 3 == 0) {
                return SoundEvents.SKELETON_HORSE_GALLOP_WATER;
            }
            if (this.f_30524_ <= 5) {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }
        }
        return SoundEvents.SKELETON_HORSE_SWIM;
    }

    @Override
    protected void playSwimSound(float float0) {
        if (this.m_20096_()) {
            super.m_5625_(0.3F);
        } else {
            super.m_5625_(Math.min(0.1F, float0 * 25.0F));
        }
    }

    @Override
    protected void playJumpSound() {
        if (this.m_20069_()) {
            this.m_5496_(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
        } else {
            super.playJumpSound();
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.m_6048_() - 0.1875;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isTrap() && this.trapTime++ >= 18000) {
            this.m_146870_();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putBoolean("SkeletonTrap", this.isTrap());
        compoundTag0.putInt("SkeletonTrapTime", this.trapTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setTrap(compoundTag0.getBoolean("SkeletonTrap"));
        this.trapTime = compoundTag0.getInt("SkeletonTrapTime");
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.96F;
    }

    public boolean isTrap() {
        return this.isTrap;
    }

    public void setTrap(boolean boolean0) {
        if (boolean0 != this.isTrap) {
            this.isTrap = boolean0;
            if (boolean0) {
                this.f_21345_.addGoal(1, this.skeletonTrapGoal);
            } else {
                this.f_21345_.removeGoal(this.skeletonTrapGoal);
            }
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.SKELETON_HORSE.create(serverLevel0);
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        return !this.m_30614_() ? InteractionResult.PASS : super.mobInteract(player0, interactionHand1);
    }
}