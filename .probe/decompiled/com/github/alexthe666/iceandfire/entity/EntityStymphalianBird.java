package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.ai.StymphalianBirdAIAirTarget;
import com.github.alexthe666.iceandfire.entity.ai.StymphalianBirdAIFlee;
import com.github.alexthe666.iceandfire.entity.ai.StymphalianBirdAITarget;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.entity.util.StymphalianBirdFlock;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityStymphalianBird extends Monster implements IAnimatedEntity, Enemy, IVillagerFear, IAnimalFear {

    public static final Predicate<Entity> STYMPHALIAN_PREDICATE = new Predicate<Entity>() {

        public boolean apply(@Nullable Entity entity) {
            return entity instanceof EntityStymphalianBird;
        }
    };

    private static final int FLIGHT_CHANCE_PER_TICK = 100;

    private static final EntityDataAccessor<Optional<UUID>> VICTOR_ENTITY = SynchedEntityData.defineId(EntityStymphalianBird.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityStymphalianBird.class, EntityDataSerializers.BOOLEAN);

    public static Animation ANIMATION_PECK = Animation.create(20);

    public static Animation ANIMATION_SHOOT_ARROWS = Animation.create(30);

    public static Animation ANIMATION_SPEAK = Animation.create(10);

    public float flyProgress;

    public BlockPos airTarget;

    public StymphalianBirdFlock flock;

    private int animationTick;

    private Animation currentAnimation;

    private boolean isFlying;

    private int flyTicks;

    private int launchTicks;

    private boolean aiFlightLaunch = false;

    private int airBorneCounter;

    public EntityStymphalianBird(EntityType<? extends Monster> t, Level worldIn) {
        super(t, worldIn);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new StymphalianBirdAIFlee(this, 10.0F));
        this.f_21345_.addGoal(3, new MeleeAttackGoal(this, 1.5, false));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(6, new StymphalianBirdAIAirTarget(this));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new StymphalianBirdAITarget(this, LivingEntity.class, true));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 24.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength * 2.0).add(Attributes.FOLLOW_RANGE, (double) Math.min(2048, IafConfig.stymphalianBirdTargetSearchLength)).add(Attributes.ARMOR, 4.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(VICTOR_ENTITY, Optional.empty());
        this.f_19804_.define(FLYING, Boolean.FALSE);
    }

    @Override
    public int getExperienceReward() {
        return 10;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide && this.m_9236_().m_46791_() == Difficulty.PEACEFUL) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.m_7380_(tag);
        if (this.getVictorId() != null) {
            tag.putUUID("VictorUUID", this.getVictorId());
        }
        tag.putBoolean("Flying", this.isFlying());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.m_7378_(tag);
        UUID s;
        if (tag.hasUUID("VictorUUID")) {
            s = tag.getUUID("VictorUUID");
        } else {
            String s1 = tag.getString("VictorUUID");
            s = OldUsersConverter.convertMobOwnerIfNecessary(this.m_20194_(), s1);
        }
        if (s != null) {
            try {
                this.setVictorId(s);
            } catch (Throwable var4) {
            }
        }
        this.setFlying(tag.getBoolean("Flying"));
    }

    public boolean isFlying() {
        return this.m_9236_().isClientSide ? (this.isFlying = this.f_19804_.get(FLYING)) : this.isFlying;
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
        if (!this.m_9236_().isClientSide) {
            this.isFlying = flying;
        }
    }

    @Override
    public void die(DamageSource cause) {
        if (cause.getEntity() != null && cause.getEntity() instanceof LivingEntity && !this.m_9236_().isClientSide) {
            this.setVictorId(cause.getEntity().getUUID());
            if (this.flock != null) {
                this.flock.setFearTarget((LivingEntity) cause.getEntity());
            }
        }
        super.m_6667_(cause);
    }

    @Override
    protected void tickDeath() {
        super.m_6153_();
    }

    @Nullable
    public UUID getVictorId() {
        return (UUID) this.f_19804_.get(VICTOR_ENTITY).orElse(null);
    }

    public void setVictorId(@Nullable UUID uuid) {
        this.f_19804_.set(VICTOR_ENTITY, Optional.ofNullable(uuid));
    }

    @Nullable
    public LivingEntity getVictor() {
        try {
            UUID uuid = this.getVictorId();
            return uuid == null ? null : this.m_9236_().m_46003_(uuid);
        } catch (IllegalArgumentException var21) {
            return null;
        }
    }

    public void setVictor(LivingEntity player) {
        this.setVictorId(player.m_20148_());
    }

    public boolean isVictor(LivingEntity entityIn) {
        return entityIn == this.getVictor();
    }

    public boolean isTargetBlocked(Vec3 target) {
        return this.m_9236_().m_45547_(new ClipContext(target, this.m_20299_(1.0F), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_PECK);
        }
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_5448_() instanceof Player) {
            this.setTarget(null);
        }
        if (this.m_5448_() != null && (this.m_5448_() instanceof Player && ((Player) this.m_5448_()).isCreative() || this.getVictor() != null && this.isVictor(this.m_5448_()))) {
            this.setTarget(null);
        }
        if (this.flock == null) {
            StymphalianBirdFlock otherFlock = StymphalianBirdFlock.getNearbyFlock(this);
            if (otherFlock == null) {
                this.flock = StymphalianBirdFlock.createFlock(this);
            } else {
                this.flock = otherFlock;
                this.flock.addToFlock(this);
            }
        } else {
            if (!this.flock.isLeader(this)) {
                double dist = this.m_20280_(this.flock.getLeader());
                if (dist > 360.0) {
                    this.setFlying(true);
                    this.f_21344_.stop();
                    this.airTarget = StymphalianBirdAIAirTarget.getNearbyAirTarget(this.flock.getLeader());
                    this.aiFlightLaunch = false;
                } else if (!this.flock.getLeader().isFlying()) {
                    this.setFlying(false);
                    this.airTarget = null;
                    this.aiFlightLaunch = false;
                }
                if (this.m_20096_() && dist < 40.0 && this.getAnimation() != ANIMATION_SHOOT_ARROWS) {
                    this.setFlying(false);
                }
            }
            this.flock.update();
        }
        if (!this.m_9236_().isClientSide && this.m_5448_() != null && this.m_5448_().isAlive()) {
            double distx = this.m_20280_(this.m_5448_());
            if (this.getAnimation() == ANIMATION_PECK && this.getAnimationTick() == 7) {
                if (distx < 1.5) {
                    this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
                }
                if (this.m_20096_()) {
                    this.setFlying(false);
                }
            }
            if (this.getAnimation() != ANIMATION_PECK && this.getAnimation() != ANIMATION_SHOOT_ARROWS && distx > 3.0 && distx < 225.0) {
                this.setAnimation(ANIMATION_SHOOT_ARROWS);
            }
            if (this.getAnimation() == ANIMATION_SHOOT_ARROWS) {
                LivingEntity target = this.m_5448_();
                this.m_21391_(target, 360.0F, 360.0F);
                if (this.isFlying()) {
                    this.m_146922_(this.f_20883_);
                    if ((this.getAnimationTick() == 7 || this.getAnimationTick() == 14) && this.isDirectPathBetweenPoints(this, this.m_20182_(), target.m_20182_())) {
                        this.m_5496_(IafSoundRegistry.STYMPHALIAN_BIRD_ATTACK, 1.0F, 1.0F);
                        for (int i = 0; i < 4; i++) {
                            float wingX = (float) (this.m_20185_() + (double) (0.9F * Mth.cos((float) ((double) (this.m_146908_() + (float) (180 * (i % 2))) * Math.PI / 180.0))));
                            float wingZ = (float) (this.m_20189_() + (double) (0.9F * Mth.sin((float) ((double) (this.m_146908_() + (float) (180 * (i % 2))) * Math.PI / 180.0))));
                            float wingY = (float) (this.m_20186_() + 1.0);
                            double d0 = target.m_20185_() - (double) wingX;
                            double d1 = target.m_20191_().minY - (double) wingY;
                            double d2 = target.m_20189_() - (double) wingZ;
                            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                            EntityStymphalianFeather entityarrow = new EntityStymphalianFeather(IafEntityRegistry.STYMPHALIAN_FEATHER.get(), this.m_9236_(), this);
                            entityarrow.m_6034_((double) wingX, (double) wingY, (double) wingZ);
                            entityarrow.m_6686_(d0, d1 + d3 * 0.10000000298023223, d2, 1.6F, (float) (14 - this.m_9236_().m_46791_().getId() * 4));
                            this.m_5496_(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
                            this.m_9236_().m_7967_(entityarrow);
                        }
                    }
                } else {
                    this.setFlying(true);
                }
            }
        }
        boolean flying = this.isFlying() && !this.m_20096_() || this.airBorneCounter > 10 || this.getAnimation() == ANIMATION_SHOOT_ARROWS;
        if (flying && this.flyProgress < 20.0F) {
            this.flyProgress++;
        } else if (!flying && this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (!this.isFlying() && this.airTarget != null && this.m_20096_() && !this.m_9236_().isClientSide) {
            this.airTarget = null;
        }
        if (this.isFlying() && this.m_5448_() == null) {
            this.flyAround();
        } else if (this.m_5448_() != null) {
            this.flyTowardsTarget();
        }
        if (!this.m_9236_().isClientSide && this.doesWantToLand() && !this.aiFlightLaunch && this.getAnimation() != ANIMATION_SHOOT_ARROWS) {
            this.setFlying(false);
            this.airTarget = null;
        }
        if (!this.m_9236_().isClientSide && this.m_20229_(0.0, 0.0, 0.0) && !this.isFlying()) {
            this.setFlying(true);
            this.launchTicks = 0;
            this.flyTicks = 0;
            this.aiFlightLaunch = true;
        }
        if (!this.m_9236_().isClientSide && this.m_20096_() && this.isFlying() && !this.aiFlightLaunch && this.getAnimation() != ANIMATION_SHOOT_ARROWS) {
            this.setFlying(false);
            this.airTarget = null;
        }
        if (!this.m_9236_().isClientSide && (this.flock == null || this.flock != null && this.flock.isLeader(this)) && this.m_217043_().nextInt(100) == 0 && !this.isFlying() && this.m_20197_().isEmpty() && !this.m_6162_() && this.m_20096_()) {
            this.setFlying(true);
            this.launchTicks = 0;
            this.flyTicks = 0;
            this.aiFlightLaunch = true;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.aiFlightLaunch && this.launchTicks < 40) {
                this.launchTicks++;
            } else {
                this.launchTicks = 0;
                this.aiFlightLaunch = false;
            }
            if (this.isFlying()) {
                this.flyTicks++;
            } else {
                this.flyTicks = 0;
            }
        }
        if (!this.m_20096_()) {
            this.airBorneCounter++;
        } else {
            this.airBorneCounter = 0;
        }
        if (this.getAnimation() == ANIMATION_SHOOT_ARROWS && !this.isFlying() && !this.m_9236_().isClientSide) {
            this.setFlying(true);
            this.aiFlightLaunch = true;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3 vec1, Vec3 vec2) {
        return this.m_9236_().m_45547_(new ClipContext(vec1, vec2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    public void flyAround() {
        if (this.airTarget != null && this.isFlying()) {
            if (!this.isTargetInAir() || this.flyTicks > 6000 || !this.isFlying()) {
                this.airTarget = null;
            }
            this.flyTowardsTarget();
        }
    }

    public void flyTowardsTarget() {
        if (this.airTarget != null && this.isTargetInAir() && this.isFlying() && this.getDistanceSquared(new Vec3((double) this.airTarget.m_123341_(), this.m_20186_(), (double) this.airTarget.m_123343_())) > 3.0F) {
            double targetX = (double) this.airTarget.m_123341_() + 0.5 - this.m_20185_();
            double targetY = (double) Math.min(this.airTarget.m_123342_(), 256) + 1.0 - this.m_20186_();
            double targetZ = (double) this.airTarget.m_123343_() + 0.5 - this.m_20189_();
            double motionX = (Math.signum(targetX) * 0.5 - this.m_20184_().x) * 0.100000000372529 * (double) this.getFlySpeed(false);
            double motionY = (Math.signum(targetY) * 0.5 - this.m_20184_().y) * 0.100000000372529 * (double) this.getFlySpeed(true);
            double motionZ = (Math.signum(targetZ) * 0.5 - this.m_20184_().z) * 0.100000000372529 * (double) this.getFlySpeed(false);
            this.m_20256_(this.m_20184_().add(motionX, motionY, motionZ));
            float angle = (float) (Math.atan2(this.m_20184_().z, this.m_20184_().x) * 180.0 / Math.PI) - 90.0F;
            float rotation = Mth.wrapDegrees(angle - this.m_146908_());
            this.f_20902_ = 0.5F;
            this.f_19859_ = this.m_146908_();
            this.m_146922_(this.m_146908_() + rotation);
            if (!this.isFlying()) {
                this.setFlying(true);
            }
        } else {
            this.airTarget = null;
        }
        if (this.airTarget != null && this.isTargetInAir() && this.isFlying() && this.getDistanceSquared(new Vec3((double) this.airTarget.m_123341_(), this.m_20186_(), (double) this.airTarget.m_123343_())) < 3.0F && this.doesWantToLand()) {
            this.setFlying(false);
        }
    }

    private float getFlySpeed(boolean y) {
        float speed = 2.0F;
        if (this.flock != null && !this.flock.isLeader(this) && this.m_20280_(this.flock.getLeader()) > 10.0) {
            speed = 4.0F;
        }
        if (this.getAnimation() == ANIMATION_SHOOT_ARROWS && !y) {
            speed = (float) ((double) speed * 0.05);
        }
        return speed;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_8032_();
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_6677_(source);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.STYMPHALIAN_BIRD_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.STYMPHALIAN_BIRD_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.STYMPHALIAN_BIRD_DIE;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue((double) IafConfig.stymphalianBirdTargetSearchLength);
        return spawnDataIn;
    }

    @Override
    public void setTarget(LivingEntity entity) {
        if (!this.isVictor(entity) || entity == null) {
            super.m_6710_(entity);
            if (this.flock != null && this.flock.isLeader(this) && entity != null) {
                this.flock.onLeaderAttack(entity);
            }
        }
    }

    public float getDistanceSquared(Vec3 Vector3d) {
        float f = (float) (this.m_20185_() - Vector3d.x);
        float f1 = (float) (this.m_20186_() - Vector3d.y);
        float f2 = (float) (this.m_20189_() - Vector3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }

    protected boolean isTargetInAir() {
        return this.airTarget != null && (this.m_9236_().getBlockState(this.airTarget).m_60795_() || this.m_9236_().getBlockState(this.airTarget).m_60795_());
    }

    public boolean doesWantToLand() {
        return this.flock != null && !this.flock.isLeader(this) && this.flock.getLeader() != null ? this.flock.getLeader().doesWantToLand() : this.flyTicks > 500 || this.flyTicks > 40 && this.flyProgress == 0.0F;
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
        return new Animation[] { NO_ANIMATION, ANIMATION_PECK, ANIMATION_SHOOT_ARROWS, ANIMATION_SPEAK };
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return IafConfig.stympahlianBirdAttackAnimals;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}