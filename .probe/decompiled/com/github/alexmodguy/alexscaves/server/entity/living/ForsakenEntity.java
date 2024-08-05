package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.ForsakenAttackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.ForsakenRandomlyJumpGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.GroundPathNavigatorNoSpin;
import com.github.alexmodguy.alexscaves.server.entity.util.ShakesScreen;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.animation.LegSolverQuadruped;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class ForsakenEntity extends Monster implements IAnimatedEntity, ShakesScreen {

    private static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(ForsakenEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> LEAPING = SynchedEntityData.defineId(ForsakenEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SONIC_CHARGE = SynchedEntityData.defineId(ForsakenEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> SONAR_ID = SynchedEntityData.defineId(ForsakenEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> HELD_MOB_ID = SynchedEntityData.defineId(ForsakenEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DARKNESS_TIME = SynchedEntityData.defineId(ForsakenEntity.class, EntityDataSerializers.INT);

    public static final Animation ANIMATION_SUMMON = Animation.create(50);

    public static final Animation ANIMATION_PREPARE_JUMP = Animation.create(15);

    public static final Animation ANIMATION_BITE = Animation.create(15);

    public static final Animation ANIMATION_LEFT_SLASH = Animation.create(33);

    public static final Animation ANIMATION_RIGHT_SLASH = Animation.create(33);

    public static final Animation ANIMATION_GROUND_SMASH = Animation.create(30);

    public static final Animation ANIMATION_SONIC_ATTACK = Animation.create(35);

    public static final Animation ANIMATION_SONIC_BLAST = Animation.create(45);

    public static final Animation ANIMATION_LEFT_PICKUP = Animation.create(48);

    public static final Animation ANIMATION_RIGHT_PICKUP = Animation.create(48);

    private static final int LIGHT_THRESHOLD = 4;

    private Animation currentAnimation = IAnimatedEntity.NO_ANIMATION;

    private int animationTick;

    public LegSolverQuadruped legSolver = new LegSolverQuadruped(-0.4F, 1.4F, 1.0F, 0.75F, 1.0F);

    private float runProgress;

    private float prevRunProgress;

    private float leapProgress;

    private float prevLeapProgress;

    private float leapPitch;

    private float prevLeapPitch;

    private float prevScreenShakeAmount;

    private float screenShakeAmount;

    private int timeLeaping = 0;

    private float raiseLeftArmProgress;

    private float prevRaiseLeftArmProgress;

    private float raiseRightArmProgress;

    private float prevRaiseRightArmProgress;

    private float darknessProgress;

    private float prevDarknessProgress;

    private boolean hasRunningAttributes = false;

    private int destroyBlocksTick = 10;

    public static final Predicate<LivingEntity> TARGETING = mob -> !mob.m_6095_().is(ACTagRegistry.FORSAKEN_IGNORES);

    public ForsakenEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 250.0).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.6);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(RUNNING, false);
        this.f_19804_.define(LEAPING, false);
        this.f_19804_.define(SONIC_CHARGE, false);
        this.f_19804_.define(DARKNESS_TIME, 0);
        this.f_19804_.define(SONAR_ID, -1);
        this.f_19804_.define(HELD_MOB_ID, -1);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new ForsakenAttackGoal(this));
        this.f_21345_.addGoal(2, new ForsakenRandomlyJumpGoal(this));
        this.f_21345_.addGoal(3, new RandomStrollGoal(this, 1.0, 30));
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, ForsakenEntity.class));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true, false));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 120, false, true, TARGETING));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigatorNoSpin(this, level);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevRunProgress = this.runProgress;
        this.prevLeapProgress = this.leapProgress;
        this.prevRaiseLeftArmProgress = this.raiseLeftArmProgress;
        this.prevRaiseRightArmProgress = this.raiseRightArmProgress;
        this.prevDarknessProgress = this.darknessProgress;
        this.prevLeapPitch = this.leapPitch;
        this.prevScreenShakeAmount = this.screenShakeAmount;
        this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.f_20883_, (float) this.m_21529_());
        if (this.isRunning() && this.runProgress < 5.0F) {
            this.runProgress++;
        }
        if (!this.isRunning() && this.runProgress > 0.0F) {
            this.runProgress--;
        }
        if (this.isLeaping() && this.leapProgress < 5.0F) {
            this.leapProgress++;
        }
        if (!this.isLeaping() && this.leapProgress > 0.0F) {
            this.leapProgress--;
        }
        if (this.getDarknessTime() > 0 && this.darknessProgress < 5.0F) {
            this.darknessProgress++;
        }
        if (this.getDarknessTime() <= 0 && this.darknessProgress > 0.0F) {
            this.darknessProgress--;
        }
        if (this.isLeaping()) {
            if (this.m_20096_() && this.leapProgress >= 5.0F) {
                this.setLeaping(false);
            }
            this.timeLeaping++;
            Vec3 vec3 = this.m_20184_();
            float f2 = (float) (-(Mth.atan2(vec3.y, vec3.horizontalDistance()) * 180.0F / (float) Math.PI));
            this.leapPitch = Mth.approachDegrees(this.leapPitch, f2, 5.0F);
        } else {
            this.timeLeaping = 0;
            this.leapPitch = Mth.approachDegrees(this.leapPitch, 0.0F, 5.0F);
            if (this.getAnimation() == ANIMATION_PREPARE_JUMP && this.m_20096_() && !this.m_9236_().isClientSide && this.getAnimationTick() >= 8 && this.getAnimationTick() <= 10) {
                this.setLeaping(true);
                this.m_5496_(ACSoundRegistry.FORSAKEN_LEAP.get(), this.getSoundVolume(), this.m_6100_());
            }
        }
        if (this.isRunning() && !this.hasRunningAttributes) {
            this.hasRunningAttributes = true;
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.45);
        }
        if (!this.isRunning() && this.hasRunningAttributes) {
            this.hasRunningAttributes = false;
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
        }
        boolean raisingLeftArm = this.isRaisingArm(true);
        boolean raisingRightArm = this.isRaisingArm(false);
        if (raisingLeftArm && this.raiseLeftArmProgress < 10.0F) {
            this.raiseLeftArmProgress++;
        }
        if (!raisingLeftArm && this.raiseLeftArmProgress > 0.0F) {
            this.raiseLeftArmProgress--;
        }
        if (raisingRightArm && this.raiseRightArmProgress < 10.0F) {
            this.raiseRightArmProgress++;
        }
        if (!raisingRightArm && this.raiseRightArmProgress > 0.0F) {
            this.raiseRightArmProgress--;
        }
        if (this.screenShakeAmount > 0.0F) {
            this.screenShakeAmount = Math.max(0.0F, this.screenShakeAmount - 0.34F);
        }
        this.legSolver.update(this, this.f_20883_, this.m_6134_());
        if (this.m_9236_().isClientSide) {
            if (this.f_19796_.nextInt(6) == 0) {
                this.m_9236_().addParticle(ACParticleRegistry.FORSAKEN_SPIT.get(), this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), (double) this.m_19879_(), 0.0, 0.0);
            }
            if (this.darknessProgress > 0.0F) {
                for (int i = 0; i < 1; i++) {
                    if (this.f_19796_.nextBoolean()) {
                        this.m_9236_().addParticle(ACParticleRegistry.UNDERZEALOT_MAGIC.get(), this.m_20208_(1.0), this.m_20187_(), this.m_20262_(1.0), this.m_20185_(), this.m_20188_(), this.m_20189_());
                    } else {
                        this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20208_(1.0), this.m_20187_(), this.m_20262_(1.0), 0.0, 0.0, 0.0);
                    }
                }
            }
            if (this.getAnimation() == ANIMATION_SONIC_ATTACK && this.getAnimationTick() > 10 && this.getAnimationTick() < 30) {
                if (this.getAnimationTick() % 4 == 0) {
                    this.m_9236_().addAlwaysVisibleParticle(ACParticleRegistry.FORSAKEN_SONAR.get(), true, this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), (double) this.m_19879_(), (double) this.m_146909_(), (double) this.m_6080_());
                }
                this.screenShakeAmount = 1.0F;
            }
            if (this.getAnimation() == ANIMATION_SONIC_BLAST && this.getAnimationTick() > 10 && this.getAnimationTick() < 30) {
                if (this.getAnimationTick() % 4 == 0) {
                    this.m_9236_().addAlwaysVisibleParticle(ACParticleRegistry.FORSAKEN_SONAR_LARGE.get(), true, this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), (double) this.m_19879_(), 90.0, 0.0);
                }
                this.screenShakeAmount = 1.0F;
            }
            if (this.getAnimation() == ANIMATION_GROUND_SMASH) {
                if (this.getAnimationTick() >= 10 && this.getAnimationTick() <= 15) {
                    this.screenShakeAmount = 1.0F;
                }
                if (this.getAnimationTick() == 12) {
                    Vec3 smashPos = this.m_20182_().add(new Vec3(0.0, 0.0, 3.5).yRot((float) (-Math.toRadians((double) this.f_20883_))));
                    float radius = 1.4F;
                    float particleCount = (float) (20 + this.f_19796_.nextInt(12));
                    for (int i1 = 0; (float) i1 < particleCount; i1++) {
                        double motionX = (double) (this.m_217043_().nextFloat() - 0.5F) * 0.7;
                        double motionY = (double) this.m_217043_().nextFloat() * 0.7 + 1.8F;
                        double motionZ = (double) (this.m_217043_().nextFloat() - 0.5F) * 0.7;
                        float angle = (float) (Math.PI / 180.0) * (this.f_20883_ + (float) i1 / particleCount * 360.0F);
                        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                        double extraY = 1.2F;
                        double extraZ = (double) (radius * Mth.cos(angle));
                        BlockPos ground = BlockPos.containing(ACMath.getGroundBelowPosition(this.m_9236_(), new Vec3((double) Mth.floor(smashPos.x + extraX), (double) (Mth.floor(smashPos.y + extraY) + 2), (double) Mth.floor(smashPos.z + extraZ))));
                        BlockState groundState = this.m_9236_().getBlockState(ground);
                        if (groundState.m_280296_() && this.m_9236_().isClientSide) {
                            this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, groundState), true, smashPos.x + extraX, (double) ground.m_123342_() + extraY, smashPos.z + extraZ, motionX, motionY, motionZ);
                        }
                    }
                }
            }
        } else {
            LivingEntity target = this.m_5448_();
            if (target != null && target.isAlive() && target.m_20270_(this) < 10.0F && this.m_142582_(target) && (this.getAnimation() == ANIMATION_RIGHT_PICKUP || this.getAnimation() == ANIMATION_LEFT_PICKUP)) {
                if (this.getHeldMobId() == -1) {
                    this.m_5496_(ACSoundRegistry.FORSAKEN_GRAB.get(), this.getSoundVolume(), this.m_6100_());
                }
                this.setHeldMobId(target.m_19879_());
            } else if (this.getHeldMobId() != -1) {
                this.setHeldMobId(-1);
            }
            if (this.m_21223_() < this.m_21233_() * 0.5F && !this.m_9236_().isClientSide) {
                int lightLevel = this.getLightLevel();
                if (lightLevel <= 4) {
                    this.setDarknessTime(30);
                } else if (this.getDarknessTime() > 0) {
                    this.setDarknessTime(this.getDarknessTime() - 1);
                }
                if (this.getDarknessTime() > 0 && this.f_19797_ % 30 == 0) {
                    this.m_5634_(1.0F);
                }
            } else {
                this.setDarknessTime(0);
            }
        }
        Entity grabbedEntity = this.getHeldMob();
        if (grabbedEntity != null && grabbedEntity.isAlive() && grabbedEntity.distanceTo(this) < 10.0F) {
            grabbedEntity.fallDistance = 0.0F;
            if ((this.getAnimation() == ANIMATION_RIGHT_PICKUP || this.getAnimation() == ANIMATION_LEFT_PICKUP) && this.getAnimationTick() >= 10 && this.getAnimationTick() <= 38) {
                Vec3 grabPos = this.getPickupPos();
                Vec3 minus = new Vec3(grabPos.x - grabbedEntity.getX(), grabPos.y - grabbedEntity.getY(), grabPos.z - grabbedEntity.getZ()).scale(0.33F);
                grabbedEntity.setDeltaMovement(minus);
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private int getLightLevel() {
        BlockPos blockPos = this.m_20183_().above();
        return Math.max(this.m_9236_().m_45517_(LightLayer.BLOCK, blockPos), this.m_9236_().m_46803_(blockPos));
    }

    private Vec3 getPickupPos() {
        Vec3 handRotated = this.getHandPos(this.animationTick).yRot(-this.f_20883_ * (float) (Math.PI / 180.0));
        return this.m_20182_().add(handRotated);
    }

    public int getDarknessTime() {
        return this.f_19804_.get(DARKNESS_TIME);
    }

    public void setDarknessTime(int time) {
        this.f_19804_.set(DARKNESS_TIME, time);
    }

    private Vec3 getHandPos(int animationTick) {
        float sideOffset = this.getAnimation() == ANIMATION_LEFT_PICKUP ? 1.0F : -1.0F;
        Vec3 hand;
        if (animationTick <= 10) {
            hand = new Vec3(0.0, 0.0, 4.0);
        } else if (animationTick <= 15) {
            hand = new Vec3(0.0, 1.0, 3.7F);
        } else if (animationTick <= 25) {
            hand = new Vec3((double) (sideOffset * 2.75F), 4.65F, 1.9F);
        } else {
            hand = new Vec3((double) (sideOffset * 1.2F), 3.15F, 2.4F);
        }
        return hand;
    }

    public Entity getHeldMob() {
        int id = this.getHeldMobId();
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    @Override
    public boolean removeWhenFarAway(double dist) {
        return dist > 16384.0;
    }

    public static boolean checkForsakenSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return m_219013_(entityType, levelAccessor, mobSpawnType, blockPos, randomSource) && randomSource.nextInt(20) == 0;
    }

    private boolean isRaisingArm(boolean left) {
        if (this.currentAnimation != NO_ANIMATION && this.currentAnimation != null && this.animationTick > this.currentAnimation.getDuration() - 5) {
            return false;
        } else if (!left || this.currentAnimation != ANIMATION_LEFT_PICKUP && this.currentAnimation != ANIMATION_LEFT_SLASH) {
            return left || this.currentAnimation != ANIMATION_RIGHT_PICKUP && this.currentAnimation != ANIMATION_RIGHT_SLASH ? this.currentAnimation == ANIMATION_SUMMON || this.currentAnimation == ANIMATION_GROUND_SMASH : true;
        } else {
            return true;
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public float getRunProgress(float partialTick) {
        return (this.prevRunProgress + (this.runProgress - this.prevRunProgress) * partialTick) * 0.2F;
    }

    public boolean isLeaping() {
        return this.f_19804_.get(LEAPING);
    }

    public void setLeaping(boolean bool) {
        this.f_19804_.set(LEAPING, bool);
    }

    public float getLeapProgress(float partialTick) {
        return (this.prevLeapProgress + (this.leapProgress - this.prevLeapProgress) * partialTick) * 0.2F;
    }

    public void setSonarId(int i) {
        this.f_19804_.set(SONAR_ID, i);
    }

    public Entity getSonarTarget() {
        int id = this.f_19804_.get(SONAR_ID);
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    public void setHeldMobId(int i) {
        this.f_19804_.set(HELD_MOB_ID, i);
    }

    public int getHeldMobId() {
        return this.f_19804_.get(HELD_MOB_ID);
    }

    public float getLeapPitch(float partialTick) {
        return this.prevLeapPitch + (this.leapPitch - this.prevLeapPitch) * partialTick;
    }

    public boolean isRunning() {
        return this.f_19804_.get(RUNNING);
    }

    public void setRunning(boolean bool) {
        this.f_19804_.set(RUNNING, bool);
    }

    public boolean hasSonicCharge() {
        return this.f_19804_.get(SONIC_CHARGE);
    }

    public void setSonicCharge(boolean bool) {
        this.f_19804_.set(SONIC_CHARGE, bool);
    }

    public float getRaisedLeftArmAmount(float partialTicks) {
        return (this.prevRaiseLeftArmProgress + (this.raiseLeftArmProgress - this.prevRaiseLeftArmProgress) * partialTicks) * 0.1F;
    }

    public float getRaisedRightArmAmount(float partialTicks) {
        return (this.prevRaiseRightArmProgress + (this.raiseRightArmProgress - this.prevRaiseRightArmProgress) * partialTicks) * 0.1F;
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, 0.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 6.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
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

    public float getDarknessAmount(float partialTicks) {
        float animationValue = 0.0F;
        if (this.currentAnimation == ANIMATION_SUMMON) {
            animationValue = 1.0F - ((float) this.getAnimationTick() + partialTicks) / (float) ANIMATION_SUMMON.getDuration();
        }
        return Math.max((this.prevDarknessProgress + (this.darknessProgress - this.prevDarknessProgress) * partialTicks) * 0.2F, animationValue);
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
        if (this.m_5830_() && this.destroyBlocksTick > 0) {
            this.destroyBlocksTick--;
            if (this.destroyBlocksTick == 0 && ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this)) {
                int j1 = Mth.floor(this.m_20186_());
                int i2 = Mth.floor(this.m_20185_());
                int j2 = Mth.floor(this.m_20189_());
                boolean flag = false;
                for (int j = -1; j <= 1; j++) {
                    for (int k2 = -1; k2 <= 1; k2++) {
                        for (int k = 0; k <= 3; k++) {
                            int l2 = i2 + j;
                            int l = j1 + k;
                            int i1 = j2 + k2;
                            BlockPos blockpos = new BlockPos(l2, l, i1);
                            BlockState blockstate = this.m_9236_().getBlockState(blockpos);
                            if (blockstate.canEntityDestroy(this.m_9236_(), blockpos, this) && !blockstate.m_204336_(ACTagRegistry.UNMOVEABLE) && ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                                flag = this.m_9236_().m_46953_(blockpos, true, this) || flag;
                            }
                        }
                    }
                }
                if (flag) {
                    this.m_9236_().m_5898_((Player) null, 1022, this.m_20183_(), 0);
                }
                this.destroyBlocksTick = 20;
            }
        }
    }

    @Override
    public float getScreenShakeAmount(float partialTicks) {
        return this.prevScreenShakeAmount + (this.screenShakeAmount - this.prevScreenShakeAmount) * partialTicks;
    }

    @Override
    public boolean canFeelShake(Entity player) {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (damageSource.is(DamageTypes.SONIC_BOOM)) {
            this.setSonicCharge(true);
            return false;
        } else {
            if (damageSource.getEntity() instanceof AbstractGolem) {
                f *= 0.5F;
            }
            return super.m_6469_(damageSource, f);
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_SUMMON, ANIMATION_PREPARE_JUMP, ANIMATION_BITE, ANIMATION_LEFT_SLASH, ANIMATION_RIGHT_SLASH, ANIMATION_GROUND_SMASH, ANIMATION_SONIC_ATTACK, ANIMATION_SONIC_BLAST, ANIMATION_LEFT_PICKUP, ANIMATION_RIGHT_PICKUP };
    }

    public float getSonicDamageAgainst(LivingEntity target) {
        return target.m_6095_().is(ACTagRegistry.WEAK_TO_FORSAKEN_SONIC_ATTACK) ? 45.0F : 4.0F;
    }

    public float getStepHeight() {
        return this.hasRunningAttributes ? 1.1F : 0.6F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.FORSAKEN_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.FORSAKEN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.FORSAKEN_DEATH.get();
    }

    @Override
    public float getSoundVolume() {
        return 2.5F;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.m_6162_()) {
            this.m_5496_(ACSoundRegistry.FORSAKEN_STEP.get(), 1.0F, 1.0F);
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }
}