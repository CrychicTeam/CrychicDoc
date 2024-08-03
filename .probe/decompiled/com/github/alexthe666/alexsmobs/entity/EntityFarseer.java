package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.message.MessageSendVisualFlagFromServer;
import com.github.alexthe666.alexsmobs.misc.AMDamageTypes;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityFarseer extends Monster implements IAnimatedEntity {

    public static final Animation ANIMATION_EMERGE = Animation.create(50);

    private static final int HANDS = 4;

    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(EntityFarseer.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_EMERGED = SynchedEntityData.defineId(EntityFarseer.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> MELEEING = SynchedEntityData.defineId(EntityFarseer.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> LASER_ENTITY_ID = SynchedEntityData.defineId(EntityFarseer.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> LASER_ATTACK_LVL = SynchedEntityData.defineId(EntityFarseer.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> LASER_DISTANCE = SynchedEntityData.defineId(EntityFarseer.class, EntityDataSerializers.FLOAT);

    public static final int LASER_ATTACK_DURATION = 10;

    public final double[][] positions = new double[64][4];

    public final float[] claspProgress = new float[4];

    public final float[] prevClaspProgress = new float[4];

    public final float[] strikeProgress = new float[4];

    public final float[] prevStrikeProgress = new float[4];

    public final boolean[] isStriking = new boolean[4];

    public int posPointer = -1;

    public float angryProgress;

    public float prevAngryProgress;

    public Vec3 angryShakeVec = Vec3.ZERO;

    public float prevLaserLvl;

    private float faceCameraProgress;

    private float prevFaceCameraProgress;

    private LivingEntity laserTargetEntity;

    private int claspingHand = -1;

    private int animationTick;

    private Animation currentAnimation;

    private int meleeCooldown = 0;

    protected EntityFarseer(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.f_21342_ = new EntityFarseer.MoveController();
        this.f_21364_ = 20;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 70.0).add(Attributes.ARMOR, 6.0).add(Attributes.FLYING_SPEED, 0.5).add(Attributes.ATTACK_DAMAGE, 4.5).add(Attributes.MOVEMENT_SPEED, 0.35F);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.farseerSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.7F;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, this.m_9236_());
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.m_7008_(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new EntityFarseer.AttackGoal());
        this.f_21345_.addGoal(3, new EntityFarseer.RandomFlyGoal(this));
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Player.class, 3, false, true, null));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("Emerged", this.hasEmerged());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setHasEmerged(compound.getBoolean("Emerged"));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.FARSEER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.FARSEER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.FARSEER_HURT.get();
    }

    public static boolean checkFarseerSpawnRules(EntityType<? extends Monster> animal, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_46791_() != Difficulty.PEACEFUL && m_219009_(worldIn, pos, random) && isFarseerArea(worldIn, pos);
    }

    private static boolean isFarseerArea(ServerLevelAccessor iServerWorld, BlockPos pos) {
        return !AMConfig.restrictFarseerSpawns || iServerWorld.m_6857_().getDistanceToBorder((double) pos.m_123341_(), (double) pos.m_123343_()) < (double) AMConfig.farseerBorderSpawnDistance;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(HAS_EMERGED, false);
        this.f_19804_.define(MELEEING, false);
        this.f_19804_.define(ANGRY, false);
        this.f_19804_.define(LASER_ENTITY_ID, -1);
        this.f_19804_.define(LASER_ATTACK_LVL, 0);
        this.f_19804_.define(LASER_DISTANCE, 0.0F);
    }

    public boolean isAngry() {
        return this.f_19804_.get(ANGRY);
    }

    public void setAngry(boolean angry) {
        this.f_19804_.set(ANGRY, angry);
    }

    public boolean hasLaser() {
        return this.f_19804_.get(LASER_ENTITY_ID) != -1 && this.getAnimation() != ANIMATION_EMERGE;
    }

    public int getLaserAttackLvl() {
        return this.f_19804_.get(LASER_ATTACK_LVL);
    }

    public float getLaserDistance() {
        return this.f_19804_.get(LASER_DISTANCE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        super.m_7350_(entityDataAccessor0);
        if (LASER_ENTITY_ID.equals(entityDataAccessor0)) {
            this.laserTargetEntity = null;
        }
    }

    @Nullable
    public LivingEntity getLaserTarget() {
        if (!this.hasLaser()) {
            return null;
        } else if (this.m_9236_().isClientSide) {
            if (this.laserTargetEntity != null) {
                return this.laserTargetEntity;
            } else {
                Entity fromID = this.m_9236_().getEntity(this.f_19804_.get(LASER_ENTITY_ID));
                if (fromID instanceof LivingEntity) {
                    this.laserTargetEntity = (LivingEntity) fromID;
                    return this.laserTargetEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.m_5448_();
        }
    }

    public boolean hasEmerged() {
        return this.f_19804_.get(HAS_EMERGED);
    }

    public void setHasEmerged(boolean emerged) {
        this.f_19804_.set(HAS_EMERGED, emerged);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevFaceCameraProgress = this.faceCameraProgress;
        this.prevLaserLvl = (float) this.getLaserAttackLvl();
        if (this.getAnimation() == ANIMATION_EMERGE) {
            this.setHasEmerged(true);
            this.faceCameraProgress = 1.0F;
        } else if (this.faceCameraProgress > 0.0F) {
            this.faceCameraProgress = Math.max(0.0F, this.faceCameraProgress - 0.2F);
        }
        this.prevAngryProgress = this.angryProgress;
        for (int i = 0; i < 4; i++) {
            this.prevClaspProgress[i] = this.claspProgress[i];
            this.prevStrikeProgress[i] = this.strikeProgress[i];
        }
        if (this.posPointer < 0) {
            for (int i = 0; i < this.positions.length; i++) {
                this.positions[i][0] = this.m_20185_();
                this.positions[i][1] = this.m_20186_();
                this.positions[i][2] = this.m_20189_();
                this.positions[i][3] = (double) this.f_20883_;
            }
        }
        if (++this.posPointer == this.positions.length) {
            this.posPointer = 0;
        }
        this.positions[this.posPointer][0] = this.m_20185_();
        this.positions[this.posPointer][1] = this.m_20186_();
        this.positions[this.posPointer][2] = this.m_20189_();
        this.positions[this.posPointer][3] = (double) this.f_20883_;
        if (this.isAngry() && this.angryProgress < 5.0F) {
            this.angryProgress++;
        }
        if (!this.isAngry() && this.angryProgress > 0.0F) {
            this.angryProgress--;
        }
        if (this.m_6084_()) {
            if (this.f_19796_.nextInt(this.isAngry() ? 12 : 40) == 0 && this.claspingHand == -1) {
                int i = Mth.clamp(this.f_19796_.nextInt(4), 0, 3);
                if (this.claspProgress[i] == 0.0F) {
                    this.claspingHand = i;
                }
            }
            if (this.claspingHand >= 0) {
                if (this.claspProgress[this.claspingHand] < 5.0F) {
                    this.claspProgress[this.claspingHand]++;
                } else {
                    this.claspingHand = -1;
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    if (this.claspProgress[i] > 0.0F) {
                        this.claspProgress[i]--;
                    }
                }
            }
            if (!this.hasEmerged()) {
                this.m_6842_(true);
                if (this.m_9236_().m_45914_(this.m_20185_(), this.m_20186_(), this.m_20189_(), 9.0)) {
                    this.setAnimation(ANIMATION_EMERGE);
                }
            } else {
                this.m_6842_(this.m_21023_(MobEffects.INVISIBILITY));
            }
            if (this.getAnimation() == ANIMATION_EMERGE) {
                if (this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(AMParticleRegistry.STATIC_SPARK.get(), this.m_20208_(0.75), this.m_20187_(), this.m_20262_(0.75), (double) ((this.m_217043_().nextFloat() - 0.5F) * 0.2F), (double) (this.m_217043_().nextFloat() * 0.2F), (double) ((this.m_217043_().nextFloat() - 0.5F) * 0.2F));
                }
                if (this.getAnimationTick() == 1) {
                    this.m_5496_(AMSoundRegistry.FARSEER_EMERGE.get(), this.m_6121_(), this.m_6100_());
                }
            }
            LivingEntity target = this.m_5448_();
            if (target != null && this.f_19804_.get(MELEEING) && this.meleeCooldown == 0) {
                this.meleeCooldown = 5;
                int ix = this.f_19796_.nextInt(4);
                this.isStriking[ix] = true;
                this.m_9236_().broadcastEntityEvent(this, (byte) (40 + ix));
            }
            if (this.meleeCooldown > 0) {
                this.meleeCooldown--;
            }
            for (int ix = 0; ix < 4; ix++) {
                if (!this.isStriking[ix] || !this.f_19804_.get(MELEEING)) {
                    if (this.strikeProgress[ix] > 0.0F) {
                        this.strikeProgress[ix]--;
                    }
                } else if (this.isStriking[ix]) {
                    if (this.strikeProgress[ix] < 5.0F) {
                        this.strikeProgress[ix]++;
                    }
                    if (this.strikeProgress[ix] == 5.0F) {
                        this.isStriking[ix] = false;
                        this.m_9236_().broadcastEntityEvent(this, (byte) (44 + ix));
                        if (target != null && this.m_20270_(target) <= 4.0F) {
                            target.hurt(this.m_269291_().mobAttack(this), (float) (5 + this.f_19796_.nextInt(5)));
                        }
                    }
                }
            }
            if (this.hasLaser()) {
                LivingEntity livingentity = this.getLaserTarget();
                if (livingentity != null) {
                    Vec3 hit = this.calculateLaserHit(livingentity.m_146892_());
                    this.f_19804_.set(LASER_DISTANCE, (float) hit.distanceTo(this.m_146892_()));
                    this.m_21563_().setLookAt(livingentity, 90.0F, 90.0F);
                    this.m_21563_().tick();
                    double d0 = hit.x - this.m_20185_();
                    double d1 = hit.y - this.m_20188_();
                    double d2 = hit.z - this.m_20189_();
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 /= d3;
                    d1 /= d3;
                    d2 /= d3;
                    float progress = (float) this.getLaserAttackLvl() / 10.0F;
                    double d4 = this.f_19796_.nextDouble();
                    while (d4 < d3 * (double) progress) {
                        d4 += 0.5 + 2.0 * this.f_19796_.nextDouble();
                        double width = d4 / (d3 * (double) progress);
                        double d5 = (this.f_19796_.nextDouble() - 0.5) * width;
                        double d6 = (this.f_19796_.nextDouble() - 0.5) * width;
                        this.m_9236_().addParticle(AMParticleRegistry.STATIC_SPARK.get(), this.m_20185_() + d0 * d4 + d5, this.m_20188_() + d1 * d4, this.m_20189_() + d2 * d4 + d6, (double) ((this.m_217043_().nextFloat() - 0.5F) * 0.2F), (double) (this.m_217043_().nextFloat() * 0.2F), (double) ((this.m_217043_().nextFloat() - 0.5F) * 0.2F));
                    }
                }
            }
        }
        if (this.isAngry()) {
            this.angryShakeVec = new Vec3((double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F));
        } else {
            this.angryShakeVec = Vec3.ZERO;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id >= 40 && id <= 43) {
            int i = id - 40;
            this.isStriking[i] = true;
        } else if (id >= 44 && id <= 48) {
            int i = id - 44;
            this.isStriking[i] = false;
        } else {
            super.m_7822_(id);
        }
    }

    public double getLatencyVar(int pointer, int index, float partialTick) {
        if (this.m_21224_()) {
            partialTick = 1.0F;
        }
        int i = this.posPointer - pointer & 63;
        int j = this.posPointer - pointer - 1 & 63;
        double d0 = this.positions[j][index];
        double d1 = Mth.wrapDegrees(this.positions[i][index] - d0);
        return d0 + d1 * (double) partialTick;
    }

    public Vec3 getLatencyOffsetVec(int offset, float partialTick) {
        double d0 = Mth.lerp((double) partialTick, this.f_19790_, this.m_20185_());
        double d1 = Mth.lerp((double) partialTick, this.f_19791_, this.m_20186_());
        double d2 = Mth.lerp((double) partialTick, this.f_19792_, this.m_20189_());
        float renderYaw = (float) this.getLatencyVar(offset, 3, partialTick);
        return new Vec3(this.getLatencyVar(offset, 0, partialTick) - d0, this.getLatencyVar(offset, 1, partialTick) - d1, this.getLatencyVar(offset, 2, partialTick) - d2).yRot(renderYaw * (float) (Math.PI / 180.0));
    }

    public Vec3 calculateAfterimagePos(float partialTick, boolean flip, float speed) {
        float f = (partialTick + (float) this.f_19797_) * speed;
        float f1 = 0.1F;
        Vec3 v = new Vec3((double) ((float) Math.sin((double) f) * f1), (double) ((float) Math.cos((double) f - (Math.PI / 2)) * f1), (double) (-((float) Math.cos((double) f)) * f1));
        return flip ? new Vec3(v.z, -v.y, v.x) : v;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
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
        return new Animation[] { ANIMATION_EMERGE };
    }

    public int getPortalFrame() {
        if (this.getAnimation() == ANIMATION_EMERGE) {
            if (this.getAnimationTick() < 10) {
                return 0;
            } else if (this.getAnimationTick() < 20) {
                return 1;
            } else if (this.getAnimationTick() < 30) {
                return 2;
            } else if (this.getAnimationTick() > 40) {
                int i = 50 - this.getAnimationTick();
                return i < 6 ? (i < 3 ? 0 : 1) : 2;
            } else {
                return 3;
            }
        } else {
            return 0;
        }
    }

    public float getPortalOpacity(float partialTicks) {
        if (this.getAnimation() == ANIMATION_EMERGE) {
            float tick = (float) (this.getAnimationTick() - 1) + partialTicks;
            return tick < 5.0F ? tick / 5.0F : 1.0F;
        } else {
            return 0.0F;
        }
    }

    public float getFarseerOpacity(float partialTicks) {
        if (this.getAnimation() == ANIMATION_EMERGE) {
            float tick = (float) (this.getAnimationTick() - 1) + partialTicks;
            float prog = tick / (float) ANIMATION_EMERGE.getDuration();
            return prog > 0.5F ? (prog - 0.5F) / 0.5F : 0.0F;
        } else {
            return 1.0F;
        }
    }

    public float getFacingCameraAmount(float partialTicks) {
        return this.prevFaceCameraProgress + (this.faceCameraProgress - this.prevFaceCameraProgress) * partialTicks;
    }

    @Override
    public boolean isEffectiveAi() {
        return super.m_21515_() && this.getAnimation() != ANIMATION_EMERGE && this.hasEmerged();
    }

    private Vec3 calculateLaserHit(Vec3 target) {
        Vec3 eyes = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        HitResult hitResult = this.m_9236_().m_45547_(new ClipContext(eyes, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return hitResult.getLocation();
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isEffectiveAi() || this.m_6109_()) {
            if (this.m_20069_()) {
                this.m_19920_(0.02F, vec3);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.8F));
            } else if (this.m_20077_()) {
                this.m_19920_(0.02F, vec3);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.5));
            } else {
                this.m_19920_(this.m_6113_(), vec3);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.91F));
            }
        }
        this.m_267651_(false);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource dmg) {
        return super.m_6673_(dmg) || this.getAnimation() == ANIMATION_EMERGE;
    }

    private boolean canUseLaser() {
        return !this.m_21023_(MobEffects.BLINDNESS);
    }

    private class AttackGoal extends Goal {

        private boolean attackDecision = true;

        private int timeSinceLastSuccessfulAttack = 0;

        private int laserCooldown = 0;

        private int laserUseTime = 0;

        private int lasersShot = 0;

        public AttackGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntityFarseer.this.m_5448_() != null && EntityFarseer.this.m_5448_().isAlive();
        }

        @Override
        public void stop() {
            this.lasersShot = 0;
            this.laserCooldown = 0;
            this.laserUseTime = 0;
            this.attackDecision = EntityFarseer.this.m_217043_().nextBoolean();
            EntityFarseer.this.f_19804_.set(EntityFarseer.LASER_ENTITY_ID, -1);
            this.timeSinceLastSuccessfulAttack = 0;
            EntityFarseer.this.setAngry(false);
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = EntityFarseer.this.m_5448_();
            if (this.laserCooldown > 0) {
                this.laserCooldown--;
            }
            this.timeSinceLastSuccessfulAttack++;
            if (this.timeSinceLastSuccessfulAttack > 100) {
                this.timeSinceLastSuccessfulAttack = 0;
                this.attackDecision = !this.attackDecision;
            }
            if (target != null) {
                double dist = (double) EntityFarseer.this.m_20270_(target);
                boolean canLaserHit = this.willLaserHit(target);
                if (this.laserCooldown == 0 && this.attackDecision && canLaserHit && dist > 2.0) {
                    EntityFarseer.this.setAngry(true);
                    EntityFarseer.this.f_19804_.set(EntityFarseer.LASER_ENTITY_ID, target.m_19879_());
                    if (this.laserUseTime == 0) {
                        EntityFarseer.this.m_5496_(AMSoundRegistry.FARSEER_BEAM.get(), EntityFarseer.this.m_6121_(), EntityFarseer.this.m_6100_());
                    }
                    this.laserUseTime++;
                    if (this.laserUseTime > 10) {
                        this.laserUseTime = 0;
                        if (canLaserHit) {
                            float healthTenth = target.getMaxHealth() * 0.1F;
                            if (target.hurt(AMDamageTypes.causeFarseerDamage(EntityFarseer.this), (float) EntityFarseer.this.f_19796_.nextInt(2) + Math.max(6.0F, healthTenth)) && !target.isAlive()) {
                                AlexsMobs.sendMSGToAll(new MessageSendVisualFlagFromServer(target.m_19879_(), 87));
                            }
                            this.timeSinceLastSuccessfulAttack = 0;
                        }
                        if (this.lasersShot++ > 5) {
                            this.lasersShot = 0;
                            this.laserCooldown = 80 + EntityFarseer.this.f_19796_.nextInt(40);
                            EntityFarseer.this.f_19804_.set(EntityFarseer.LASER_ENTITY_ID, -1);
                            this.attackDecision = EntityFarseer.this.m_217043_().nextBoolean();
                        }
                    }
                    EntityFarseer.this.f_19804_.set(EntityFarseer.LASER_ATTACK_LVL, this.laserUseTime);
                    EntityFarseer.this.m_21391_(target, 180.0F, 180.0F);
                    if (dist < 17.0 && canLaserHit) {
                        EntityFarseer.this.m_21573_().stop();
                    } else {
                        EntityFarseer.this.m_21573_().moveTo(target, 1.0);
                    }
                    EntityFarseer.this.f_19804_.set(EntityFarseer.MELEEING, false);
                } else {
                    if (!canLaserHit && dist > 10.0) {
                        EntityFarseer.this.setAngry(false);
                    }
                    if (EntityFarseer.this.hasLaser()) {
                        EntityFarseer.this.f_19804_.set(EntityFarseer.LASER_ENTITY_ID, -1);
                    }
                    EntityFarseer.this.f_19804_.set(EntityFarseer.MELEEING, dist < 4.0);
                    if (dist < 4.0) {
                        this.timeSinceLastSuccessfulAttack = 0;
                    } else {
                        EntityFarseer.this.m_21573_().moveTo(target, 1.0);
                        EntityFarseer.this.f_21342_.setWantedPosition(target.m_20185_(), target.m_20188_(), target.m_20189_(), 1.0);
                    }
                }
            }
        }

        private boolean willLaserHit(LivingEntity target) {
            Vec3 vec = EntityFarseer.this.calculateLaserHit(target.m_146892_());
            return vec.distanceTo(target.m_146892_()) < 1.0 && EntityFarseer.this.canUseLaser();
        }
    }

    class MoveController extends MoveControl {

        private final Mob parentEntity = EntityFarseer.this;

        public MoveController() {
            super(EntityFarseer.this);
        }

        @Override
        public void tick() {
            float angle = (float) (Math.PI / 180.0) * (this.parentEntity.f_20883_ + 90.0F);
            float radius = (float) Math.sin((double) ((float) this.parentEntity.f_19797_ * 0.2F)) * 2.0F;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraY = (double) radius * -Math.cos((double) angle - (Math.PI / 2));
            double extraZ = (double) (radius * Mth.cos(angle));
            Vec3 strafPlus = new Vec3(extraX, extraY, extraZ);
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                double width = this.parentEntity.m_20191_().getSize();
                Vec3 shimmy = Vec3.ZERO;
                LivingEntity attackTarget = this.parentEntity.getTarget();
                if (attackTarget != null && this.parentEntity.f_19862_) {
                    shimmy = new Vec3(0.0, 0.005, 0.0);
                }
                Vec3 vector3d1 = vector3d.scale(this.f_24978_ * 0.05 / d0);
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d1.add(strafPlus.scale(0.003 * Math.min(d0, 100.0)).add(shimmy))));
                if (d0 >= width) {
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    if (EntityFarseer.this.hasLaser()) {
                        this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                    }
                }
            } else if (this.f_24981_ == MoveControl.Operation.WAIT) {
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(strafPlus.scale(0.003)));
            }
        }
    }

    private static class RandomFlyGoal extends Goal {

        private final EntityFarseer parentEntity;

        private BlockPos target = null;

        private final float speed = 0.6F;

        public RandomFlyGoal(EntityFarseer mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.parentEntity.m_21573_().isDone() && this.parentEntity.m_5448_() == null && this.parentEntity.m_217043_().nextInt(4) == 0) {
                this.target = this.getBlockInViewFarseer();
                if (this.target != null) {
                    this.parentEntity.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 0.6F);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.parentEntity.m_5448_() == null;
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.target != null) {
                this.parentEntity.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 0.6F);
                if (this.parentEntity.m_20238_(Vec3.atCenterOf(this.target)) < 4.0 || this.parentEntity.f_19862_) {
                    this.target = null;
                }
            }
        }

        private BlockPos getFarseerGround(BlockPos in) {
            BlockPos position = new BlockPos(in.m_123341_(), (int) this.parentEntity.m_20186_(), in.m_123343_());
            while (position.m_123342_() < 256 && !this.parentEntity.m_9236_().getFluidState(position).isEmpty()) {
                position = position.above();
            }
            while (position.m_123342_() > 1 && this.parentEntity.m_9236_().m_46859_(position)) {
                position = position.below();
            }
            return position;
        }

        public BlockPos getBlockInViewFarseer() {
            float radius = (float) (5 + this.parentEntity.m_217043_().nextInt(10));
            float neg = this.parentEntity.m_217043_().nextBoolean() ? 1.0F : -1.0F;
            float renderYawOffset = this.parentEntity.m_146908_();
            float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F * this.parentEntity.m_217043_().nextFloat() * neg;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos radialPos = new BlockPos((int) (this.parentEntity.m_20185_() + extraX), (int) this.parentEntity.m_20186_(), (int) (this.parentEntity.m_20189_() + extraZ));
            BlockPos ground = this.getFarseerGround(radialPos).above(2 + this.parentEntity.f_19796_.nextInt(2));
            return !this.parentEntity.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? ground : null;
        }
    }
}