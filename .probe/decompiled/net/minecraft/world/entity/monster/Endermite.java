package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class Endermite extends Monster {

    private static final int MAX_LIFE = 2400;

    private int life;

    public Endermite(EntityType<? extends Endermite> entityTypeExtendsEndermite0, Level level1) {
        super(entityTypeExtendsEndermite0, level1);
        this.f_21364_ = 3;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.m_9236_()));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.f_21345_.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 0.13F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMITE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.ENDERMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMITE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.ENDERMITE_STEP, 0.15F, 1.0F);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.life = compoundTag0.getInt("Lifetime");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putInt("Lifetime", this.life);
    }

    @Override
    public void tick() {
        this.f_20883_ = this.m_146908_();
        super.m_8119_();
    }

    @Override
    public void setYBodyRot(float float0) {
        this.m_146922_(float0);
        super.m_5618_(float0);
    }

    @Override
    public double getMyRidingOffset() {
        return 0.1;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.m_9236_().isClientSide) {
            for (int $$0 = 0; $$0 < 2; $$0++) {
                this.m_9236_().addParticle(ParticleTypes.PORTAL, this.m_20208_(0.5), this.m_20187_(), this.m_20262_(0.5), (this.f_19796_.nextDouble() - 0.5) * 2.0, -this.f_19796_.nextDouble(), (this.f_19796_.nextDouble() - 0.5) * 2.0);
            }
        } else {
            if (!this.m_21532_()) {
                this.life++;
            }
            if (this.life >= 2400) {
                this.m_146870_();
            }
        }
    }

    public static boolean checkEndermiteSpawnRules(EntityType<Endermite> entityTypeEndermite0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        if (m_219019_(entityTypeEndermite0, levelAccessor1, mobSpawnType2, blockPos3, randomSource4)) {
            Player $$5 = levelAccessor1.m_45924_((double) blockPos3.m_123341_() + 0.5, (double) blockPos3.m_123342_() + 0.5, (double) blockPos3.m_123343_() + 0.5, 5.0, true);
            return $$5 == null;
        } else {
            return false;
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }
}