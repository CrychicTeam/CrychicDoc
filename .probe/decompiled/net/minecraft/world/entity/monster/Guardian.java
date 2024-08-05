package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class Guardian extends Monster {

    protected static final int ATTACK_TIME = 80;

    private static final EntityDataAccessor<Boolean> DATA_ID_MOVING = SynchedEntityData.defineId(Guardian.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(Guardian.class, EntityDataSerializers.INT);

    private float clientSideTailAnimation;

    private float clientSideTailAnimationO;

    private float clientSideTailAnimationSpeed;

    private float clientSideSpikesAnimation;

    private float clientSideSpikesAnimationO;

    @Nullable
    private LivingEntity clientSideCachedAttackTarget;

    private int clientSideAttackTime;

    private boolean clientSideTouchedGround;

    @Nullable
    protected RandomStrollGoal randomStrollGoal;

    public Guardian(EntityType<? extends Guardian> entityTypeExtendsGuardian0, Level level1) {
        super(entityTypeExtendsGuardian0, level1);
        this.f_21364_ = 10;
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.f_21342_ = new Guardian.GuardianMoveControl(this);
        this.clientSideTailAnimation = this.f_19796_.nextFloat();
        this.clientSideTailAnimationO = this.clientSideTailAnimation;
    }

    @Override
    protected void registerGoals() {
        MoveTowardsRestrictionGoal $$0 = new MoveTowardsRestrictionGoal(this, 1.0);
        this.randomStrollGoal = new RandomStrollGoal(this, 1.0, 80);
        this.f_21345_.addGoal(4, new Guardian.GuardianAttackGoal(this));
        this.f_21345_.addGoal(5, $$0);
        this.f_21345_.addGoal(7, this.randomStrollGoal);
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Guardian.class, 12.0F, 0.01F));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
        this.randomStrollGoal.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        $$0.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, new Guardian.GuardianAttackSelector(this)));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MAX_HEALTH, 30.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        return new WaterBoundPathNavigation(this, level0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_ID_MOVING, false);
        this.f_19804_.define(DATA_ID_ATTACK_TARGET, 0);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    public boolean isMoving() {
        return this.f_19804_.get(DATA_ID_MOVING);
    }

    void setMoving(boolean boolean0) {
        this.f_19804_.set(DATA_ID_MOVING, boolean0);
    }

    public int getAttackDuration() {
        return 80;
    }

    void setActiveAttackTarget(int int0) {
        this.f_19804_.set(DATA_ID_ATTACK_TARGET, int0);
    }

    public boolean hasActiveAttackTarget() {
        return this.f_19804_.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    @Nullable
    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        } else if (this.m_9236_().isClientSide) {
            if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            } else {
                Entity $$0 = this.m_9236_().getEntity(this.f_19804_.get(DATA_ID_ATTACK_TARGET));
                if ($$0 instanceof LivingEntity) {
                    this.clientSideCachedAttackTarget = (LivingEntity) $$0;
                    return this.clientSideCachedAttackTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.m_5448_();
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        super.m_7350_(entityDataAccessor0);
        if (DATA_ID_ATTACK_TARGET.equals(entityDataAccessor0)) {
            this.clientSideAttackTime = 0;
            this.clientSideCachedAttackTarget = null;
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 160;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_20072_() ? SoundEvents.GUARDIAN_AMBIENT : SoundEvents.GUARDIAN_AMBIENT_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return this.m_20072_() ? SoundEvents.GUARDIAN_HURT : SoundEvents.GUARDIAN_HURT_LAND;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.m_20072_() ? SoundEvents.GUARDIAN_DEATH : SoundEvents.GUARDIAN_DEATH_LAND;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height * 0.5F;
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        return levelReader1.m_6425_(blockPos0).is(FluidTags.WATER) ? 10.0F + levelReader1.getPathfindingCostFromLightLevels(blockPos0) : super.getWalkTargetValue(blockPos0, levelReader1);
    }

    @Override
    public void aiStep() {
        if (this.m_6084_()) {
            if (this.m_9236_().isClientSide) {
                this.clientSideTailAnimationO = this.clientSideTailAnimation;
                if (!this.m_20069_()) {
                    this.clientSideTailAnimationSpeed = 2.0F;
                    Vec3 $$0 = this.m_20184_();
                    if ($$0.y > 0.0 && this.clientSideTouchedGround && !this.m_20067_()) {
                        this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.getFlopSound(), this.m_5720_(), 1.0F, 1.0F, false);
                    }
                    this.clientSideTouchedGround = $$0.y < 0.0 && this.m_9236_().loadedAndEntityCanStandOn(this.m_20183_().below(), this);
                } else if (this.isMoving()) {
                    if (this.clientSideTailAnimationSpeed < 0.5F) {
                        this.clientSideTailAnimationSpeed = 4.0F;
                    } else {
                        this.clientSideTailAnimationSpeed = this.clientSideTailAnimationSpeed + (0.5F - this.clientSideTailAnimationSpeed) * 0.1F;
                    }
                } else {
                    this.clientSideTailAnimationSpeed = this.clientSideTailAnimationSpeed + (0.125F - this.clientSideTailAnimationSpeed) * 0.2F;
                }
                this.clientSideTailAnimation = this.clientSideTailAnimation + this.clientSideTailAnimationSpeed;
                this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
                if (!this.m_20072_()) {
                    this.clientSideSpikesAnimation = this.f_19796_.nextFloat();
                } else if (this.isMoving()) {
                    this.clientSideSpikesAnimation = this.clientSideSpikesAnimation + (0.0F - this.clientSideSpikesAnimation) * 0.25F;
                } else {
                    this.clientSideSpikesAnimation = this.clientSideSpikesAnimation + (1.0F - this.clientSideSpikesAnimation) * 0.06F;
                }
                if (this.isMoving() && this.m_20069_()) {
                    Vec3 $$1 = this.m_20252_(0.0F);
                    for (int $$2 = 0; $$2 < 2; $$2++) {
                        this.m_9236_().addParticle(ParticleTypes.BUBBLE, this.m_20208_(0.5) - $$1.x * 1.5, this.m_20187_() - $$1.y * 1.5, this.m_20262_(0.5) - $$1.z * 1.5, 0.0, 0.0, 0.0);
                    }
                }
                if (this.hasActiveAttackTarget()) {
                    if (this.clientSideAttackTime < this.getAttackDuration()) {
                        this.clientSideAttackTime++;
                    }
                    LivingEntity $$3 = this.getActiveAttackTarget();
                    if ($$3 != null) {
                        this.m_21563_().setLookAt($$3, 90.0F, 90.0F);
                        this.m_21563_().tick();
                        double $$4 = (double) this.getAttackAnimationScale(0.0F);
                        double $$5 = $$3.m_20185_() - this.m_20185_();
                        double $$6 = $$3.m_20227_(0.5) - this.m_20188_();
                        double $$7 = $$3.m_20189_() - this.m_20189_();
                        double $$8 = Math.sqrt($$5 * $$5 + $$6 * $$6 + $$7 * $$7);
                        $$5 /= $$8;
                        $$6 /= $$8;
                        $$7 /= $$8;
                        double $$9 = this.f_19796_.nextDouble();
                        while ($$9 < $$8) {
                            $$9 += 1.8 - $$4 + this.f_19796_.nextDouble() * (1.7 - $$4);
                            this.m_9236_().addParticle(ParticleTypes.BUBBLE, this.m_20185_() + $$5 * $$9, this.m_20188_() + $$6 * $$9, this.m_20189_() + $$7 * $$9, 0.0, 0.0, 0.0);
                        }
                    }
                }
            }
            if (this.m_20072_()) {
                this.m_20301_(300);
            } else if (this.m_20096_()) {
                this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.4F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.4F)));
                this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
                this.m_6853_(false);
                this.f_19812_ = true;
            }
            if (this.hasActiveAttackTarget()) {
                this.m_146922_(this.f_20885_);
            }
        }
        super.aiStep();
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.GUARDIAN_FLOP;
    }

    public float getTailAnimation(float float0) {
        return Mth.lerp(float0, this.clientSideTailAnimationO, this.clientSideTailAnimation);
    }

    public float getSpikesAnimation(float float0) {
        return Mth.lerp(float0, this.clientSideSpikesAnimationO, this.clientSideSpikesAnimation);
    }

    public float getAttackAnimationScale(float float0) {
        return ((float) this.clientSideAttackTime + float0) / (float) this.getAttackDuration();
    }

    public float getClientSideAttackTime() {
        return (float) this.clientSideAttackTime;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        return levelReader0.m_45784_(this);
    }

    public static boolean checkGuardianSpawnRules(EntityType<? extends Guardian> entityTypeExtendsGuardian0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return (randomSource4.nextInt(20) == 0 || !levelAccessor1.m_46861_(blockPos3)) && levelAccessor1.getDifficulty() != Difficulty.PEACEFUL && (mobSpawnType2 == MobSpawnType.SPAWNER || levelAccessor1.m_6425_(blockPos3).is(FluidTags.WATER)) && levelAccessor1.m_6425_(blockPos3.below()).is(FluidTags.WATER);
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_9236_().isClientSide) {
            return false;
        } else {
            if (!this.isMoving() && !damageSource0.is(DamageTypeTags.AVOIDS_GUARDIAN_THORNS) && !damageSource0.is(DamageTypes.THORNS) && damageSource0.getDirectEntity() instanceof LivingEntity $$2) {
                $$2.hurt(this.m_269291_().thorns(this), 2.0F);
            }
            if (this.randomStrollGoal != null) {
                this.randomStrollGoal.trigger();
            }
            return super.m_6469_(damageSource0, float1);
        }
    }

    @Override
    public int getMaxHeadXRot() {
        return 180;
    }

    @Override
    public void travel(Vec3 vec0) {
        if (this.m_6109_() && this.m_20069_()) {
            this.m_19920_(0.1F, vec0);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (!this.isMoving() && this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(vec0);
        }
    }

    static class GuardianAttackGoal extends Goal {

        private final Guardian guardian;

        private int attackTime;

        private final boolean elder;

        public GuardianAttackGoal(Guardian guardian0) {
            this.guardian = guardian0;
            this.elder = guardian0 instanceof ElderGuardian;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity $$0 = this.guardian.m_5448_();
            return $$0 != null && $$0.isAlive();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && (this.elder || this.guardian.m_5448_() != null && this.guardian.m_20280_(this.guardian.m_5448_()) > 9.0);
        }

        @Override
        public void start() {
            this.attackTime = -10;
            this.guardian.m_21573_().stop();
            LivingEntity $$0 = this.guardian.m_5448_();
            if ($$0 != null) {
                this.guardian.m_21563_().setLookAt($$0, 90.0F, 90.0F);
            }
            this.guardian.f_19812_ = true;
        }

        @Override
        public void stop() {
            this.guardian.setActiveAttackTarget(0);
            this.guardian.m_6710_(null);
            this.guardian.randomStrollGoal.trigger();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity $$0 = this.guardian.m_5448_();
            if ($$0 != null) {
                this.guardian.m_21573_().stop();
                this.guardian.m_21563_().setLookAt($$0, 90.0F, 90.0F);
                if (!this.guardian.m_142582_($$0)) {
                    this.guardian.m_6710_(null);
                } else {
                    this.attackTime++;
                    if (this.attackTime == 0) {
                        this.guardian.setActiveAttackTarget($$0.m_19879_());
                        if (!this.guardian.m_20067_()) {
                            this.guardian.m_9236_().broadcastEntityEvent(this.guardian, (byte) 21);
                        }
                    } else if (this.attackTime >= this.guardian.getAttackDuration()) {
                        float $$1 = 1.0F;
                        if (this.guardian.m_9236_().m_46791_() == Difficulty.HARD) {
                            $$1 += 2.0F;
                        }
                        if (this.elder) {
                            $$1 += 2.0F;
                        }
                        $$0.hurt(this.guardian.m_269291_().indirectMagic(this.guardian, this.guardian), $$1);
                        $$0.hurt(this.guardian.m_269291_().mobAttack(this.guardian), (float) this.guardian.m_21133_(Attributes.ATTACK_DAMAGE));
                        this.guardian.m_6710_(null);
                    }
                    super.tick();
                }
            }
        }
    }

    static class GuardianAttackSelector implements Predicate<LivingEntity> {

        private final Guardian guardian;

        public GuardianAttackSelector(Guardian guardian0) {
            this.guardian = guardian0;
        }

        public boolean test(@Nullable LivingEntity livingEntity0) {
            return (livingEntity0 instanceof Player || livingEntity0 instanceof Squid || livingEntity0 instanceof Axolotl) && livingEntity0.m_20280_(this.guardian) > 9.0;
        }
    }

    static class GuardianMoveControl extends MoveControl {

        private final Guardian guardian;

        public GuardianMoveControl(Guardian guardian0) {
            super(guardian0);
            this.guardian = guardian0;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.guardian.m_21573_().isDone()) {
                Vec3 $$0 = new Vec3(this.f_24975_ - this.guardian.m_20185_(), this.f_24976_ - this.guardian.m_20186_(), this.f_24977_ - this.guardian.m_20189_());
                double $$1 = $$0.length();
                double $$2 = $$0.x / $$1;
                double $$3 = $$0.y / $$1;
                double $$4 = $$0.z / $$1;
                float $$5 = (float) (Mth.atan2($$0.z, $$0.x) * 180.0F / (float) Math.PI) - 90.0F;
                this.guardian.m_146922_(this.m_24991_(this.guardian.m_146908_(), $$5, 90.0F));
                this.guardian.f_20883_ = this.guardian.m_146908_();
                float $$6 = (float) (this.f_24978_ * this.guardian.m_21133_(Attributes.MOVEMENT_SPEED));
                float $$7 = Mth.lerp(0.125F, this.guardian.m_6113_(), $$6);
                this.guardian.m_7910_($$7);
                double $$8 = Math.sin((double) (this.guardian.f_19797_ + this.guardian.m_19879_()) * 0.5) * 0.05;
                double $$9 = Math.cos((double) (this.guardian.m_146908_() * (float) (Math.PI / 180.0)));
                double $$10 = Math.sin((double) (this.guardian.m_146908_() * (float) (Math.PI / 180.0)));
                double $$11 = Math.sin((double) (this.guardian.f_19797_ + this.guardian.m_19879_()) * 0.75) * 0.05;
                this.guardian.m_20256_(this.guardian.m_20184_().add($$8 * $$9, $$11 * ($$10 + $$9) * 0.25 + (double) $$7 * $$3 * 0.1, $$8 * $$10));
                LookControl $$12 = this.guardian.m_21563_();
                double $$13 = this.guardian.m_20185_() + $$2 * 2.0;
                double $$14 = this.guardian.m_20188_() + $$3 / $$1;
                double $$15 = this.guardian.m_20189_() + $$4 * 2.0;
                double $$16 = $$12.getWantedX();
                double $$17 = $$12.getWantedY();
                double $$18 = $$12.getWantedZ();
                if (!$$12.isLookingAtTarget()) {
                    $$16 = $$13;
                    $$17 = $$14;
                    $$18 = $$15;
                }
                this.guardian.m_21563_().setLookAt(Mth.lerp(0.125, $$16, $$13), Mth.lerp(0.125, $$17, $$14), Mth.lerp(0.125, $$18, $$15), 10.0F, 40.0F);
                this.guardian.setMoving(true);
            } else {
                this.guardian.m_7910_(0.0F);
                this.guardian.setMoving(false);
            }
        }
    }
}