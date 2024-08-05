package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoDismount;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoMountPlayer;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityEnderiophage extends Animal implements Enemy, FlyingAnimal {

    private static final EntityDataAccessor<Float> PHAGE_PITCH = SynchedEntityData.defineId(EntityEnderiophage.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityEnderiophage.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> MISSING_EYE = SynchedEntityData.defineId(EntityEnderiophage.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> PHAGE_SCALE = SynchedEntityData.defineId(EntityEnderiophage.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityEnderiophage.class, EntityDataSerializers.INT);

    private static final Predicate<LivingEntity> ENDERGRADE_OR_INFECTED = entity -> entity instanceof EntityEndergrade || entity.hasEffect(AMEffectRegistry.ENDER_FLU.get());

    public float prevPhagePitch;

    public float tentacleAngle;

    public float lastTentacleAngle;

    public float phageRotation;

    public float prevFlyProgress;

    public float flyProgress;

    public int passengerIndex = 0;

    public float prevEnderiophageScale = 1.0F;

    private float rotationVelocity;

    private int slowDownTicks = 0;

    private float randomMotionSpeed;

    private boolean isLandNavigator;

    private int timeFlying = 0;

    private int fleeAfterStealTime = 0;

    private int attachTime = 0;

    private int dismountCooldown = 0;

    private int squishCooldown = 0;

    private PathfinderMob angryEnderman = null;

    protected EntityEnderiophage(EntityType type, Level world) {
        super(type, world);
        this.rotationVelocity = 1.0F / (this.f_19796_.nextFloat() + 1.0F) * 0.2F;
        this.switchNavigator(false);
        this.f_21364_ = 5;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.15F).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    public static boolean canEnderiophageSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return true;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.enderiophageSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos down = this.getPhageGround(this.m_20183_());
        this.m_6034_((double) ((float) down.m_123341_() + 0.5F), (double) (down.m_123342_() + 1), (double) ((float) down.m_123343_() + 0.5F));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing(worldIn);
        }
        this.setSkinForDimension();
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    public float getPhageScale() {
        return this.f_19804_.get(PHAGE_SCALE);
    }

    public void setPhageScale(float scale) {
        this.f_19804_.set(PHAGE_SCALE, scale);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new EntityEnderiophage.FlyTowardsTarget(this));
        this.f_21345_.addGoal(2, new EntityEnderiophage.AIWalkIdle());
        this.f_21346_.addGoal(1, new EntityAINearestTarget3D(this, EnderMan.class, 15, true, true, null) {

            @Override
            public boolean canUse() {
                return EntityEnderiophage.this.isMissingEye() && super.m_8036_();
            }

            @Override
            public boolean canContinueToUse() {
                return EntityEnderiophage.this.isMissingEye() && super.m_8045_();
            }
        });
        this.f_21346_.addGoal(1, new EntityAINearestTarget3D(this, LivingEntity.class, 15, true, true, ENDERGRADE_OR_INFECTED) {

            @Override
            public boolean canUse() {
                return !EntityEnderiophage.this.isMissingEye() && EntityEnderiophage.this.fleeAfterStealTime == 0 && super.m_8036_();
            }

            @Override
            public boolean canContinueToUse() {
                return !EntityEnderiophage.this.isMissingEye() && super.m_8045_();
            }
        });
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this, EnderMan.class));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new FlightMoveController(this, 1.0F, false, true);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(PHAGE_PITCH, 0.0F);
        this.f_19804_.define(PHAGE_SCALE, 1.0F);
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(MISSING_EYE, false);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean isInOverworld() {
        return this.m_9236_().dimension() == Level.OVERWORLD && !this.m_21525_();
    }

    public boolean isInNether() {
        return this.m_9236_().dimension() == Level.NETHER && !this.m_21525_();
    }

    public void setStandardFleeTime() {
        this.fleeAfterStealTime = 20;
    }

    @Override
    public void rideTick() {
        Entity entity = this.m_20202_();
        if (this.m_20159_() && !entity.isAlive()) {
            this.m_8127_();
        } else {
            this.m_20334_(0.0, 0.0, 0.0);
            this.tick();
            if (this.m_20159_()) {
                this.attachTime++;
                Entity mount = this.m_20202_();
                if (mount instanceof LivingEntity) {
                    this.passengerIndex = mount.getPassengers().indexOf(this);
                    this.f_20883_ = ((LivingEntity) mount).yBodyRot;
                    this.m_146922_(((LivingEntity) mount).m_146908_());
                    this.f_20885_ = ((LivingEntity) mount).yHeadRot;
                    this.f_19859_ = ((LivingEntity) mount).yHeadRot;
                    float radius = mount.getBbWidth();
                    float angle = (float) (Math.PI / 180.0) * (((LivingEntity) mount).yBodyRot + (float) this.passengerIndex * 90.0F);
                    double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
                    double extraZ = (double) (radius * Mth.cos(angle));
                    this.m_6034_(mount.getX() + extraX, Math.max(mount.getY() + (double) (mount.getEyeHeight() * 0.25F), mount.getY()), mount.getZ() + extraZ);
                    if (!mount.isAlive() || mount instanceof Player && ((Player) mount).isCreative()) {
                        this.m_6038_();
                    }
                    this.setPhagePitch(0.0F);
                    if (!this.m_9236_().isClientSide && this.attachTime > 15) {
                        LivingEntity target = (LivingEntity) mount;
                        float dmg = 1.0F;
                        if (target.getHealth() > target.getMaxHealth() * 0.2F) {
                            dmg = 6.0F;
                        }
                        if (((double) target.getHealth() < 1.5 || mount.hurt(this.m_269291_().mobAttack(this), dmg)) && mount instanceof LivingEntity) {
                            this.dismountCooldown = 100;
                            if (mount instanceof EnderMan) {
                                this.setMissingEye(false);
                                this.m_146850_(GameEvent.EAT);
                                this.m_5496_(SoundEvents.ENDER_EYE_DEATH, this.m_6121_(), this.m_6100_());
                                this.m_5634_(5.0F);
                                ((EnderMan) mount).m_7292_(new MobEffectInstance(MobEffects.BLINDNESS, 400));
                                this.fleeAfterStealTime = 400;
                                this.setFlying(true);
                                this.angryEnderman = (PathfinderMob) mount;
                            } else if (this.f_19796_.nextInt(3) == 0) {
                                if (!this.isMissingEye()) {
                                    if (target.getEffect(AMEffectRegistry.ENDER_FLU.get()) == null) {
                                        target.addEffect(new MobEffectInstance(AMEffectRegistry.ENDER_FLU.get(), 12000));
                                    } else {
                                        MobEffectInstance inst = target.getEffect(AMEffectRegistry.ENDER_FLU.get());
                                        int duration = 12000;
                                        int level = 0;
                                        if (inst != null) {
                                            duration = inst.getDuration();
                                            level = inst.getAmplifier();
                                        }
                                        target.removeEffect(AMEffectRegistry.ENDER_FLU.get());
                                        target.addEffect(new MobEffectInstance(AMEffectRegistry.ENDER_FLU.get(), duration, Math.min(level + 1, 4)));
                                    }
                                    this.m_5634_(5.0F);
                                    this.m_146850_(GameEvent.ENTITY_ROAR);
                                    this.m_5496_(SoundEvents.ITEM_BREAK, this.m_6121_(), this.m_6100_());
                                    this.setMissingEye(true);
                                }
                                if (!this.m_9236_().isClientSide) {
                                    this.m_6710_(null);
                                    this.m_21335_(null);
                                    this.m_6703_(null);
                                    this.f_21345_.getRunningGoals().forEach(Goal::m_8041_);
                                    this.f_21346_.getRunningGoals().forEach(Goal::m_8041_);
                                }
                            }
                        }
                        if (((LivingEntity) mount).getHealth() <= 0.0F || this.fleeAfterStealTime > 0 || this.isMissingEye() && !(mount instanceof EnderMan) || !this.isMissingEye() && mount instanceof EnderMan) {
                            this.m_6038_();
                            this.m_6710_(null);
                            this.dismountCooldown = 100;
                            AlexsMobs.sendMSGToAll(new MessageMosquitoDismount(this.m_19879_(), mount.getId()));
                            this.setFlying(true);
                        }
                    }
                }
            }
        }
    }

    public boolean canRiderInteract() {
        return true;
    }

    public void onSpawnFromEffect() {
        this.prevEnderiophageScale = 0.2F;
        this.setPhageScale(0.2F);
    }

    public void setSkinForDimension() {
        if (this.isInNether()) {
            this.setVariant(2);
        } else if (this.isInOverworld()) {
            this.setVariant(1);
        } else {
            this.setVariant(0);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.ENDERIOPHAGE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.ENDERIOPHAGE_HURT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.m_5496_(AMSoundRegistry.ENDERIOPHAGE_WALK.get(), 0.4F, 1.0F);
    }

    @Override
    protected float nextStep() {
        return this.f_19788_ + 0.3F;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevEnderiophageScale = this.getPhageScale();
        float extraMotionSlow = 1.0F;
        float extraMotionSlowY = 1.0F;
        if (this.slowDownTicks > 0) {
            this.slowDownTicks--;
            extraMotionSlow = 0.33F;
            extraMotionSlowY = 0.1F;
        }
        if (this.dismountCooldown > 0) {
            this.dismountCooldown--;
        }
        if (this.squishCooldown > 0) {
            this.squishCooldown--;
        }
        if (!this.m_9236_().isClientSide) {
            if (!this.m_20159_() && this.attachTime != 0) {
                this.attachTime = 0;
            }
            if (this.fleeAfterStealTime > 0) {
                if (this.angryEnderman != null) {
                    Vec3 vec = this.getBlockInViewAway(this.angryEnderman.m_20182_(), 10.0F);
                    if (this.fleeAfterStealTime < 5) {
                        if (this.angryEnderman instanceof NeutralMob) {
                            ((NeutralMob) this.angryEnderman).stopBeingAngry();
                        }
                        try {
                            this.angryEnderman.f_21345_.getRunningGoals().forEach(Goal::m_8041_);
                            this.angryEnderman.f_21346_.getRunningGoals().forEach(Goal::m_8041_);
                        } catch (Exception var18) {
                            var18.printStackTrace();
                        }
                        this.angryEnderman = null;
                    }
                    if (vec != null) {
                        this.setFlying(true);
                        this.m_21566_().setWantedPosition(vec.x, vec.y, vec.z, 1.3F);
                    }
                }
                this.fleeAfterStealTime--;
            }
        }
        this.f_20883_ = this.m_146908_();
        this.f_20885_ = this.m_146908_();
        this.setPhagePitch(-90.0F);
        if (this.m_6084_() && this.isFlying() && this.randomMotionSpeed > 0.75F && this.m_20184_().lengthSqr() > 0.02 && this.m_9236_().isClientSide) {
            float pitch = -this.getPhagePitch() / 90.0F;
            float radius = this.m_20205_() * 0.2F * -pitch;
            float angle = (float) (Math.PI / 180.0) * this.m_146908_();
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraY = (double) (0.2F - (1.0F - pitch) * 0.15F);
            double extraZ = (double) (radius * Mth.cos(angle));
            double motX = extraX * 8.0 + this.f_19796_.nextGaussian() * 0.05F;
            double motY = -0.1F;
            double motZ = extraZ + this.f_19796_.nextGaussian() * 0.05F;
            this.m_9236_().addParticle(AMParticleRegistry.DNA.get(), this.m_20185_() + extraX, this.m_20186_() + extraY, this.m_20189_() + extraZ, motX, motY, motZ);
        }
        this.prevPhagePitch = this.getPhagePitch();
        this.prevFlyProgress = this.flyProgress;
        if (this.isFlying()) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        this.lastTentacleAngle = this.tentacleAngle;
        this.phageRotation = this.phageRotation + this.rotationVelocity;
        if ((double) this.phageRotation > Math.PI * 2) {
            if (this.m_9236_().isClientSide) {
                this.phageRotation = (float) (Math.PI * 2);
            } else {
                this.phageRotation = (float) ((double) this.phageRotation - (Math.PI * 2));
                if (this.f_19796_.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0F / (this.f_19796_.nextFloat() + 1.0F) * 0.2F;
                }
                this.m_9236_().broadcastEntityEvent(this, (byte) 19);
            }
        }
        if (this.phageRotation < (float) Math.PI) {
            float f = this.phageRotation / (float) Math.PI;
            this.tentacleAngle = Mth.sin(f * f * (float) Math.PI) * 4.275F;
            if ((double) f > 0.75) {
                if (this.squishCooldown == 0 && this.isFlying()) {
                    this.squishCooldown = 20;
                    this.m_5496_(AMSoundRegistry.ENDERIOPHAGE_SQUISH.get(), 3.0F, this.m_6100_());
                }
                this.randomMotionSpeed = 1.0F;
            } else {
                this.randomMotionSpeed = 0.01F;
            }
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isFlying() && this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (!this.isFlying() && !this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.isFlying()) {
                this.m_20334_(this.m_20184_().x * (double) this.randomMotionSpeed * (double) extraMotionSlow, this.m_20184_().y * (double) this.randomMotionSpeed * (double) extraMotionSlowY, this.m_20184_().z * (double) this.randomMotionSpeed * (double) extraMotionSlow);
                this.timeFlying++;
                if (this.m_20096_() && this.timeFlying > 100) {
                    this.setFlying(false);
                }
            } else {
                this.timeFlying = 0;
            }
            if (this.isMissingEye() && this.m_5448_() != null && !(this.m_5448_() instanceof EnderMan)) {
                this.m_6710_(null);
            }
        }
        if (!this.m_20096_() && this.m_20184_().y < 0.0) {
            this.m_20256_(this.m_20184_().multiply(1.0, 0.6, 1.0));
        }
        if (this.isFlying()) {
            float phageDist = -((float) ((Math.abs(this.m_20184_().x()) + Math.abs(this.m_20184_().z())) * 6.0));
            this.incrementPhagePitch(phageDist * 1.0F);
            this.setPhagePitch(Mth.clamp(this.getPhagePitch(), -90.0F, 10.0F));
            float plateau = 2.0F;
            if (this.getPhagePitch() > plateau) {
                this.decrementPhagePitch(phageDist * Math.abs(this.getPhagePitch()) / 90.0F);
            }
            if (this.getPhagePitch() < -plateau) {
                this.incrementPhagePitch(phageDist * Math.abs(this.getPhagePitch()) / 90.0F);
            }
            if (this.getPhagePitch() > 2.0F) {
                this.decrementPhagePitch(1.0F);
            } else if (this.getPhagePitch() < -2.0F) {
                this.incrementPhagePitch(1.0F);
            }
            if (this.f_19862_) {
                this.m_20256_(this.m_20184_().add(0.0, 0.2F, 0.0));
            }
        } else {
            if (this.getPhagePitch() > 0.0F) {
                float decrease = Math.min(2.0F, this.getPhagePitch());
                this.decrementPhagePitch(decrease);
            }
            if (this.getPhagePitch() < 0.0F) {
                float decrease = Math.min(2.0F, -this.getPhagePitch());
                this.incrementPhagePitch(decrease);
            }
        }
        if (this.getPhageScale() < 1.0F) {
            this.setPhageScale(this.getPhageScale() + 0.05F);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putBoolean("MissingEye", this.isMissingEye());
        compound.putInt("Variant", this.getVariant());
        compound.putInt("SlowDownTicks", this.slowDownTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.setMissingEye(compound.getBoolean("MissingEye"));
        this.setVariant(compound.getInt("Variant"));
        this.slowDownTicks = compound.getInt("SlowDownTicks");
    }

    public boolean isMissingEye() {
        return this.f_19804_.get(MISSING_EYE);
    }

    public void setMissingEye(boolean missingEye) {
        this.f_19804_.set(MISSING_EYE, missingEye);
    }

    @Override
    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
    }

    public float getPhagePitch() {
        return this.f_19804_.get(PHAGE_PITCH);
    }

    public void setPhagePitch(float pitch) {
        this.f_19804_.set(PHAGE_PITCH, pitch);
    }

    public void incrementPhagePitch(float pitch) {
        this.f_19804_.set(PHAGE_PITCH, this.getPhagePitch() + pitch);
    }

    public void decrementPhagePitch(float pitch) {
        this.f_19804_.set(PHAGE_PITCH, this.getPhagePitch() - pitch);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 1.8F;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return null;
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -63 && !this.m_9236_().getBlockState(position).m_280296_()) {
            position = position.below();
        }
        return !this.m_9236_().getFluidState(position).isEmpty() || position.m_123342_() < -63;
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = -9.45F - (float) this.m_217043_().nextInt(24) - radiusAdd;
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), 0, (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getPhageGround(radialPos);
        int distFromGround = (int) this.m_20186_() - ground.m_123342_();
        int flightHeight = 6 + this.m_217043_().nextInt(10);
        BlockPos newPos = ground.above(distFromGround <= 8 && this.fleeAfterStealTime <= 0 ? this.m_217043_().nextInt(6) + 5 : flightHeight);
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.m_20238_(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
    }

    private BlockPos getPhageGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int) this.m_20186_(), in.m_123343_());
        while (position.m_123342_() > -63 && !this.m_9236_().getBlockState(position).m_280296_()) {
            position = position.below();
        }
        return position.m_123342_() < -62 ? position.above(120 + this.f_19796_.nextInt(5)) : position;
    }

    public Vec3 getBlockGrounding(Vec3 fleePos) {
        float radius = -9.45F - (float) this.m_217043_().nextInt(24);
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, this.m_20186_(), fleePos.z() + extraZ);
        BlockPos ground = this.getPhageGround(radialPos);
        if (ground.m_123342_() <= -63) {
            return Vec3.upFromBottomCenterOf(ground, (double) (110 + this.f_19796_.nextInt(20)));
        } else {
            ground = this.m_20183_();
            while (ground.m_123342_() > -63 && !this.m_9236_().getBlockState(ground).m_280296_()) {
                ground = ground.below();
            }
            return !this.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? Vec3.atCenterOf(ground) : null;
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            if (entity instanceof EnderMan) {
                amount = (amount + 1.0F) * 0.35F;
                this.angryEnderman = (EnderMan) entity;
            }
            return super.hurt(source, amount);
        }
    }

    private class AIWalkIdle extends Goal {

        protected final EntityEnderiophage phage;

        protected double x;

        protected double y;

        protected double z;

        private boolean flightTarget = false;

        public AIWalkIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.phage = EntityEnderiophage.this;
        }

        @Override
        public boolean canUse() {
            if (this.phage.m_20160_() || this.phage.m_5448_() != null && this.phage.m_5448_().isAlive() || this.phage.m_20159_()) {
                return false;
            } else if (this.phage.m_217043_().nextInt(30) != 0 && !this.phage.isFlying() && this.phage.fleeAfterStealTime == 0) {
                return false;
            } else {
                if (this.phage.m_20096_()) {
                    this.flightTarget = EntityEnderiophage.this.f_19796_.nextInt(12) == 0;
                } else {
                    this.flightTarget = EntityEnderiophage.this.f_19796_.nextInt(5) > 0 && this.phage.timeFlying < 100;
                }
                if (this.phage.fleeAfterStealTime > 0) {
                    this.flightTarget = true;
                }
                Vec3 lvt_1_1_ = this.getPosition();
                if (lvt_1_1_ == null) {
                    return false;
                } else {
                    this.x = lvt_1_1_.x;
                    this.y = lvt_1_1_.y;
                    this.z = lvt_1_1_.z;
                    return true;
                }
            }
        }

        @Override
        public void tick() {
            if (this.flightTarget) {
                this.phage.m_21566_().setWantedPosition(this.x, this.y, this.z, EntityEnderiophage.this.fleeAfterStealTime == 0 ? 1.3F : 1.0);
            } else {
                this.phage.m_21573_().moveTo(this.x, this.y, this.z, EntityEnderiophage.this.fleeAfterStealTime == 0 ? 1.3F : 1.0);
            }
            if (!this.flightTarget && EntityEnderiophage.this.isFlying() && this.phage.m_20096_()) {
                this.phage.setFlying(false);
            }
            if (EntityEnderiophage.this.isFlying() && this.phage.m_20096_() && this.phage.timeFlying > 100 && this.phage.fleeAfterStealTime == 0) {
                this.phage.setFlying(false);
            }
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = this.phage.m_20182_();
            if (this.phage.isOverWaterOrVoid()) {
                this.flightTarget = true;
            }
            if (this.flightTarget) {
                return this.phage.timeFlying >= 50 && EntityEnderiophage.this.fleeAfterStealTime <= 0 && !this.phage.isOverWaterOrVoid() ? this.phage.getBlockGrounding(vector3d) : this.phage.getBlockInViewAway(vector3d, 0.0F);
            } else {
                return LandRandomPos.getPos(this.phage, 10, 7);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.flightTarget ? this.phage.isFlying() && this.phage.m_20275_(this.x, this.y, this.z) > 2.0 : !this.phage.m_21573_().isDone() && !this.phage.m_20160_();
        }

        @Override
        public void start() {
            if (this.flightTarget) {
                this.phage.setFlying(true);
                this.phage.m_21566_().setWantedPosition(this.x, this.y, this.z, EntityEnderiophage.this.fleeAfterStealTime == 0 ? 1.3F : 1.0);
            } else {
                this.phage.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
        }

        @Override
        public void stop() {
            this.phage.m_21573_().stop();
            super.stop();
        }
    }

    public static class FlyTowardsTarget extends Goal {

        private final EntityEnderiophage parentEntity;

        public FlyTowardsTarget(EntityEnderiophage phage) {
            this.parentEntity = phage;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.parentEntity.m_20159_() && this.parentEntity.m_5448_() != null && !this.isBittenByPhage(this.parentEntity.m_5448_()) && this.parentEntity.fleeAfterStealTime == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.parentEntity.m_5448_() != null && !this.isBittenByPhage(this.parentEntity.m_5448_()) && !this.parentEntity.f_19862_ && !this.parentEntity.m_20159_() && this.parentEntity.isFlying() && this.parentEntity.m_21566_().hasWanted() && this.parentEntity.fleeAfterStealTime == 0 && (this.parentEntity.m_5448_() instanceof EnderMan || !this.parentEntity.isMissingEye());
        }

        public boolean isBittenByPhage(Entity entity) {
            int phageCount = 0;
            for (Entity e : entity.getPassengers()) {
                if (e instanceof EntityEnderiophage) {
                    phageCount++;
                }
            }
            return phageCount > 3;
        }

        @Override
        public void stop() {
        }

        @Override
        public void tick() {
            if (this.parentEntity.m_5448_() != null) {
                float width = this.parentEntity.m_5448_().m_20205_() + this.parentEntity.m_20205_() + 2.0F;
                boolean isWithinReach = this.parentEntity.m_20280_(this.parentEntity.m_5448_()) < (double) (width * width);
                if (!this.parentEntity.isFlying() && !isWithinReach) {
                    this.parentEntity.m_21573_().moveTo(this.parentEntity.m_5448_().m_20185_(), this.parentEntity.m_5448_().m_20186_(), this.parentEntity.m_5448_().m_20189_(), 1.2);
                } else {
                    this.parentEntity.m_21566_().setWantedPosition(this.parentEntity.m_5448_().m_20185_(), this.parentEntity.m_5448_().m_20186_(), this.parentEntity.m_5448_().m_20189_(), isWithinReach ? 1.6 : 1.0);
                }
                if (this.parentEntity.m_5448_().m_20186_() > this.parentEntity.m_20186_() + 1.2F) {
                    this.parentEntity.setFlying(true);
                }
                if (this.parentEntity.dismountCooldown == 0 && this.parentEntity.m_20191_().inflate(0.3, 0.3, 0.3).intersects(this.parentEntity.m_5448_().m_20191_()) && !this.isBittenByPhage(this.parentEntity.m_5448_())) {
                    this.parentEntity.m_7998_(this.parentEntity.m_5448_(), true);
                    if (!this.parentEntity.m_9236_().isClientSide) {
                        AlexsMobs.sendMSGToAll(new MessageMosquitoMountPlayer(this.parentEntity.m_19879_(), this.parentEntity.m_5448_().m_19879_()));
                    }
                }
            }
        }
    }
}