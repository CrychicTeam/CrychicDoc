package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.GroundPathNavigatorNoSpin;
import com.github.alexmodguy.alexscaves.server.entity.item.ThrownWasteDrumEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BrainiacEntity extends Monster implements IAnimatedEntity {

    private Animation currentAnimation;

    private int animationTick;

    public static final Animation ANIMATION_THROW_BARREL = Animation.create(30);

    public static final Animation ANIMATION_DRINK_BARREL = Animation.create(75);

    public static final Animation ANIMATION_BITE = Animation.create(25);

    public static final Animation ANIMATION_SMASH = Animation.create(20);

    private static final EntityDataAccessor<Boolean> HAS_BARREL = SynchedEntityData.defineId(BrainiacEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> TONGUE_TARGET_ID = SynchedEntityData.defineId(BrainiacEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TONGUE_SHOOT_TICK = SynchedEntityData.defineId(BrainiacEntity.class, EntityDataSerializers.INT);

    private float prevRaiseArmsAmount = 0.0F;

    private float raiseArmsAmount = 0.0F;

    private float prevLeftArmAmount = 0.0F;

    private float raiseLeftArmAmount = 0.0F;

    private float prevShootTongueAmount = 0.0F;

    private float shootTongueAmount = 0.0F;

    private float prevLastTongueDistance = 0.0F;

    private float lastTongueDistance = 0.0F;

    public BrainiacEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(HAS_BARREL, true);
        this.f_19804_.define(TONGUE_TARGET_ID, -1);
        this.f_19804_.define(TONGUE_SHOOT_TICK, 0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new BrainiacEntity.MeleeGoal());
        this.f_21345_.addGoal(2, new BrainiacEntity.PickupBarrelGoal());
        this.f_21345_.addGoal(3, new RandomStrollGoal(this, 1.0, 45));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(3, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true, false));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, true, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 40.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ARMOR, 8.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigatorNoSpin(this, level);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevRaiseArmsAmount = this.raiseArmsAmount;
        this.prevLeftArmAmount = this.raiseLeftArmAmount;
        this.prevShootTongueAmount = this.shootTongueAmount;
        this.prevLastTongueDistance = this.lastTongueDistance;
        if (this.getAnimation() == ANIMATION_SMASH && this.raiseArmsAmount < 5.0F) {
            this.raiseArmsAmount++;
        }
        if (this.getAnimation() != ANIMATION_SMASH && this.raiseArmsAmount > 0.0F) {
            this.raiseArmsAmount--;
        }
        if (this.raisingLeftArm() && this.raiseLeftArmAmount < 5.0F) {
            this.raiseLeftArmAmount++;
        }
        if (!this.raisingLeftArm() && this.raiseLeftArmAmount > 0.0F) {
            this.raiseLeftArmAmount--;
        }
        if (this.getLickTicks() > 0 && this.shootTongueAmount < 10.0F) {
            this.shootTongueAmount++;
        }
        if (this.getLickTicks() <= 0 && this.shootTongueAmount > 0.0F) {
            this.shootTongueAmount--;
        }
        if (!this.m_9236_().isClientSide && this.hasBarrel()) {
            if (this.getAnimation() == ANIMATION_DRINK_BARREL && this.getAnimationTick() >= 60) {
                this.setHasBarrel(false);
                this.m_5634_(10.0F);
                this.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0));
                this.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0));
                this.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, 400, 4));
            }
            if (this.getAnimation() == ANIMATION_THROW_BARREL && this.getAnimationTick() >= 15) {
                LivingEntity attackTarget = this.m_5448_();
                if (attackTarget != null && attackTarget.isAlive()) {
                    this.setHasBarrel(false);
                    Vec3 hand = new Vec3(0.65F, -0.3F, 0.9F).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_6080_() * (float) (Math.PI / 180.0));
                    Vec3 handOnBody = this.m_146892_().add(hand);
                    ThrownWasteDrumEntity wasteDrumEntity = ACEntityRegistry.THROWN_WASTE_DRUM.get().create(this.m_9236_());
                    wasteDrumEntity.m_146884_(handOnBody);
                    Vec3 toss = attackTarget.m_146892_().subtract(handOnBody).multiply(0.35F, 0.0, 0.35F).add(0.0, 0.4, 0.0);
                    wasteDrumEntity.m_146922_(-((float) Mth.atan2(toss.x, toss.z)) * 180.0F / (float) Math.PI);
                    this.m_9236_().m_7967_(wasteDrumEntity);
                    wasteDrumEntity.m_20256_(toss.normalize().scale((double) (attackTarget.m_20270_(this) * 0.2F)));
                }
            }
        }
        Entity tongueTarget = this.getTongueTarget();
        if (this.m_9236_().isClientSide) {
            if (tongueTarget != null && tongueTarget.isAlive()) {
                this.lastTongueDistance = this.m_20270_(tongueTarget) - 0.5F;
            }
        } else {
            LivingEntity attackTarget = this.m_5448_();
            if (this.getLickTicks() > 0) {
                this.setLickTicks(this.getLickTicks() - 1);
                if (attackTarget != null && attackTarget.isAlive() && this.m_142582_(attackTarget) && attackTarget.m_20270_(this) < 20.0F) {
                    this.setTongueTargetId(attackTarget.m_19879_());
                    this.m_7618_(EntityAnchorArgument.Anchor.EYES, attackTarget.m_146892_());
                } else {
                    this.setTongueTargetId(-1);
                }
            } else {
                this.setTongueTargetId(-1);
            }
        }
        if (tongueTarget instanceof LivingEntity living && this.shootTongueAmount >= 5.0F) {
            this.postAttackEffect(living);
            tongueTarget.hurt(this.m_269291_().mobAttack(this), 4.0F);
            living.knockback(0.3, living.m_20185_() - this.m_20185_(), tongueTarget.getZ() - this.m_20189_());
            this.setLickTicks(0);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_THROW_BARREL, ANIMATION_DRINK_BARREL, ANIMATION_SMASH, ANIMATION_BITE };
    }

    public float getRaiseArmsAmount(float partialTick) {
        return (this.prevRaiseArmsAmount + (this.raiseArmsAmount - this.prevRaiseArmsAmount) * partialTick) * 0.2F;
    }

    public boolean raisingLeftArm() {
        return this.getAnimation() == ANIMATION_DRINK_BARREL || this.getAnimation() == ANIMATION_BITE || this.getAnimation() == ANIMATION_THROW_BARREL;
    }

    public float getRaiseLeftArmAmount(float partialTick) {
        return (this.prevLeftArmAmount + (this.raiseLeftArmAmount - this.prevLeftArmAmount) * partialTick) * 0.2F;
    }

    public float getLastTongueDistance(float partialTick) {
        return this.prevLastTongueDistance + (this.lastTongueDistance - this.prevLastTongueDistance) * partialTick;
    }

    public float getShootTongueAmount(float partialTick) {
        return (this.prevShootTongueAmount + (this.shootTongueAmount - this.prevShootTongueAmount) * partialTick) * 0.1F;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setHasBarrel(compound.getBoolean("HasBarrel"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("HasBarrel", this.hasBarrel());
    }

    public Entity getTongueTarget() {
        int id = this.f_19804_.get(TONGUE_TARGET_ID);
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.hasBarrel() && this.shouldDropBlocks()) {
            this.m_19983_(new ItemStack(ACBlockRegistry.WASTE_DRUM.get()));
        }
    }

    private boolean shouldDropBlocks() {
        DamageSource lastDamageSource = this.m_21225_();
        return lastDamageSource == null ? false : lastDamageSource.getEntity() != null || lastDamageSource.getDirectEntity() != null;
    }

    public void postAttackEffect(LivingEntity entity) {
        if (entity != null && entity.isAlive()) {
            entity.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 400));
        }
    }

    public void setTongueTargetId(int id) {
        this.f_19804_.set(TONGUE_TARGET_ID, id);
    }

    public int getLickTicks() {
        return this.f_19804_.get(TONGUE_SHOOT_TICK);
    }

    public void setLickTicks(int ticks) {
        this.f_19804_.set(TONGUE_SHOOT_TICK, ticks);
    }

    public boolean hasBarrel() {
        return this.f_19804_.get(HAS_BARREL);
    }

    public void setHasBarrel(boolean barrel) {
        this.f_19804_.set(HAS_BARREL, barrel);
    }

    public float getStepHeight() {
        return 1.1F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.BRAINIAC_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.BRAINIAC_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.BRAINIAC_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.m_5496_(ACSoundRegistry.BRAINIAC_STEP.get(), 1.0F, 1.0F);
    }

    private class MeleeGoal extends Goal {

        private int tongueCooldown = 0;

        public MeleeGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = BrainiacEntity.this.m_5448_();
            return target != null && target.isAlive();
        }

        @Override
        public void tick() {
            LivingEntity target = BrainiacEntity.this.m_5448_();
            if (this.tongueCooldown > 0) {
                this.tongueCooldown--;
            }
            if (target != null && target.isAlive()) {
                double dist = (double) BrainiacEntity.this.m_20270_(target);
                if (BrainiacEntity.this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    if (BrainiacEntity.this.m_21223_() < BrainiacEntity.this.m_21233_() * 0.5F && BrainiacEntity.this.hasBarrel() && BrainiacEntity.this.f_19796_.nextInt(20) == 0) {
                        BrainiacEntity.this.setAnimation(BrainiacEntity.ANIMATION_DRINK_BARREL);
                    }
                    BrainiacEntity.this.m_21573_().moveTo(target, 1.2);
                    if (BrainiacEntity.this.m_142582_(target)) {
                        if (BrainiacEntity.this.m_21223_() < BrainiacEntity.this.m_21233_() * 0.75F && dist < 20.0 && BrainiacEntity.this.hasBarrel() && BrainiacEntity.this.f_19796_.nextInt(30) == 0) {
                            BrainiacEntity.this.setAnimation(BrainiacEntity.ANIMATION_THROW_BARREL);
                            BrainiacEntity.this.m_216990_(ACSoundRegistry.BRAINIAC_THROW.get());
                        }
                        if (dist < (double) (BrainiacEntity.this.m_20205_() + target.m_20205_()) + 3.5) {
                            BrainiacEntity.this.setAnimation(BrainiacEntity.this.f_19796_.nextBoolean() ? BrainiacEntity.ANIMATION_SMASH : BrainiacEntity.ANIMATION_BITE);
                            BrainiacEntity.this.m_216990_(ACSoundRegistry.BRAINIAC_ATTACK.get());
                            return;
                        }
                        if (this.tongueCooldown == 0 && BrainiacEntity.this.f_19796_.nextInt(16) == 0 && dist < 25.0) {
                            BrainiacEntity.this.m_216990_(ACSoundRegistry.BRAINIAC_LICK.get());
                            BrainiacEntity.this.setLickTicks(20);
                            this.tongueCooldown = 15 + BrainiacEntity.this.f_19796_.nextInt(15);
                        }
                    } else {
                        BrainiacEntity.this.setLickTicks(0);
                    }
                }
                if (BrainiacEntity.this.getAnimation() == BrainiacEntity.ANIMATION_SMASH && BrainiacEntity.this.getAnimationTick() >= 10 && BrainiacEntity.this.getAnimationTick() <= 15) {
                    this.checkAndDealDamage(target, 2.0F);
                }
                if (BrainiacEntity.this.getAnimation() == BrainiacEntity.ANIMATION_BITE && BrainiacEntity.this.getAnimationTick() >= 10 && BrainiacEntity.this.getAnimationTick() <= 15) {
                    this.checkAndDealDamage(target, 1.0F);
                }
            }
        }

        private void checkAndDealDamage(LivingEntity target, float damageMultiplier) {
            if (BrainiacEntity.this.m_142582_(target) && (double) BrainiacEntity.this.m_20270_(target) < (double) (BrainiacEntity.this.m_20205_() + target.m_20205_()) + 2.0) {
                target.hurt(BrainiacEntity.this.m_269291_().mobAttack(BrainiacEntity.this), (float) BrainiacEntity.this.m_21051_(Attributes.ATTACK_DAMAGE).getValue() * damageMultiplier);
                BrainiacEntity.this.postAttackEffect(target);
                target.knockback(0.3, BrainiacEntity.this.m_20185_() - target.m_20185_(), BrainiacEntity.this.m_20189_() - target.m_20189_());
            }
        }
    }

    private class PickupBarrelGoal extends MoveToBlockGoal {

        public PickupBarrelGoal() {
            super(BrainiacEntity.this, 1.0, 20, 6);
        }

        @Override
        protected int nextStartTick(PathfinderMob mob) {
            return m_186073_(40 + BrainiacEntity.this.m_217043_().nextInt(40));
        }

        @Override
        protected BlockPos getMoveToTarget() {
            return this.f_25602_;
        }

        @Override
        protected void moveMobToBlock() {
            BlockPos pos = this.getMoveToTarget();
            this.f_25598_.m_21573_().moveTo((double) ((float) pos.m_123341_()) + 0.5, (double) (pos.m_123342_() + 1), (double) ((float) pos.m_123343_()) + 0.5, this.f_25599_);
        }

        @Override
        public boolean canUse() {
            return BrainiacEntity.this.hasBarrel() ? false : super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !BrainiacEntity.this.hasBarrel();
        }

        @Override
        public double acceptedDistance() {
            return (double) (BrainiacEntity.this.m_20205_() + 1.0F);
        }

        @Override
        public void tick() {
            super.tick();
            if (this.f_25602_ != null) {
                BrainiacEntity.this.m_7618_(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(this.f_25602_));
                if (this.m_25625_()) {
                    BrainiacEntity.this.m_21573_().stop();
                    if (BrainiacEntity.this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                        BrainiacEntity.this.setAnimation(BrainiacEntity.ANIMATION_BITE);
                    }
                    if (BrainiacEntity.this.getAnimation() == BrainiacEntity.ANIMATION_BITE && BrainiacEntity.this.getAnimationTick() >= 10 && BrainiacEntity.this.getAnimationTick() <= 15 && this.isValidTarget(BrainiacEntity.this.m_9236_(), this.f_25602_)) {
                        BrainiacEntity.this.m_9236_().m_46961_(this.f_25602_, false);
                        BrainiacEntity.this.setHasBarrel(true);
                    }
                }
            }
        }

        @Override
        public void stop() {
            super.m_8041_();
            this.f_25602_ = BlockPos.ZERO;
        }

        @Override
        protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
            return pos != null && worldIn.m_8055_(pos).m_60713_(ACBlockRegistry.WASTE_DRUM.get());
        }
    }
}