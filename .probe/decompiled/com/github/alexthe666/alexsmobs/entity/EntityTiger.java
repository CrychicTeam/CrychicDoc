package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIPanicBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.MovementControllerCustomCollisions;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.server.entity.collision.ICustomCollisions;
import java.util.EnumSet;
import java.util.UUID;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EntityTiger extends Animal implements ICustomCollisions, IAnimatedEntity, NeutralMob, ITargetsDroppedItems {

    public static final Animation ANIMATION_PAW_R = Animation.create(15);

    public static final Animation ANIMATION_PAW_L = Animation.create(15);

    public static final Animation ANIMATION_TAIL_FLICK = Animation.create(45);

    public static final Animation ANIMATION_LEAP = Animation.create(20);

    private static final EntityDataAccessor<Boolean> WHITE = SynchedEntityData.defineId(EntityTiger.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(EntityTiger.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityTiger.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(EntityTiger.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> STEALTH_MODE = SynchedEntityData.defineId(EntityTiger.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HOLDING = SynchedEntityData.defineId(EntityTiger.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(EntityTiger.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> LAST_SCARED_MOB_ID = SynchedEntityData.defineId(EntityTiger.class, EntityDataSerializers.INT);

    private static final UniformInt ANGRY_TIMER = TimeUtil.rangeOfSeconds(40, 80);

    private static final Predicate<LivingEntity> NO_BLESSING_EFFECT = mob -> !mob.hasEffect(AMEffectRegistry.TIGERS_BLESSING.get());

    public float prevSitProgress;

    public float sitProgress;

    public float prevSleepProgress;

    public float sleepProgress;

    public float prevHoldProgress;

    public float holdProgress;

    public float prevStealthProgress;

    public float stealthProgress;

    private int animationTick;

    private Animation currentAnimation;

    private boolean hasSpedUp = false;

    private UUID lastHurtBy;

    private int sittingTime;

    private int maxSitTime;

    private int holdTime = 0;

    private int prevScaredMobId = -1;

    private boolean dontSitFlag = false;

    protected EntityTiger(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.f_21342_ = new MovementControllerCustomCollisions(this);
    }

    public static boolean canTigerSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_45524_(pos, 0) > 8;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.ATTACK_DAMAGE, 12.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.FOLLOW_RANGE, 86.0);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.tigerSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.m_6425_(pos.below()).isEmpty() && worldIn.m_6425_(pos).is(FluidTags.WATER) ? 0.0F : super.getWalkTargetValue(pos, worldIn);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return !worldIn.containsAnyLiquid(this.m_20191_());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("TigerSitting", this.isSitting());
        compound.putBoolean("TigerSleeping", this.isSleeping());
        compound.putBoolean("White", this.isWhite());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSitting(compound.getBoolean("TigerSitting"));
        this.setSleeping(compound.getBoolean("TigerSleeping"));
        this.setWhite(compound.getBoolean("White"));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(WHITE, false);
        this.f_19804_.define(RUNNING, false);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(STEALTH_MODE, false);
        this.f_19804_.define(HOLDING, false);
        this.f_19804_.define(SLEEPING, false);
        this.f_19804_.define(ANGER_TIME, 0);
        this.f_19804_.define(LAST_SCARED_MOB_ID, -1);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new AnimalAIPanicBaby(this, 1.25));
        this.f_21345_.addGoal(3, new EntityTiger.AIMelee());
        this.f_21345_.addGoal(5, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(6, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(7, new AnimalAIWanderRanged(this, 60, 1.0, 14, 7));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 25.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false, 10));
        this.f_21346_.addGoal(2, new EntityTiger.AngerGoal(this));
        this.f_21346_.addGoal(3, new EntityTiger.AttackPlayerGoal());
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, LivingEntity.class, 220, false, false, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.TIGER_TARGETS)) {

            @Override
            public boolean canUse() {
                return !EntityTiger.this.m_6162_() && super.canUse();
            }
        });
        this.f_21346_.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isStealth() ? super.m_7515_() : (this.getRemainingPersistentAngerTime() > 0 ? AMSoundRegistry.TIGER_ANGRY.get() : AMSoundRegistry.TIGER_IDLE.get());
    }

    @Override
    public int getAmbientSoundInterval() {
        return this.getRemainingPersistentAngerTime() > 0 ? 40 : 80;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.TIGER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.TIGER_HURT.get();
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.99F;
    }

    public boolean shouldMove() {
        return !this.isSitting() && !this.isSleeping() && !this.isHolding();
    }

    @Override
    public double getVisibilityPercent(@Nullable Entity lookingEntity) {
        return this.isStealth() ? 0.2 : super.m_20968_(lookingEntity);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(AMTagRegistry.TIGER_BREEDABLES);
    }

    public void awardKillScore(LivingEntity entity, int score, DamageSource src) {
        this.m_5634_(5.0F);
        super.m_5993_(entity, score, src);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (!this.shouldMove()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new EntityTiger.Navigator(this, worldIn);
    }

    public boolean isWhite() {
        return this.f_19804_.get(WHITE);
    }

    public void setWhite(boolean white) {
        this.f_19804_.set(WHITE, white);
    }

    public boolean isRunning() {
        return this.f_19804_.get(RUNNING);
    }

    public void setRunning(boolean running) {
        this.f_19804_.set(RUNNING, running);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    public void setSitting(boolean bar) {
        this.f_19804_.set(SITTING, bar);
    }

    public boolean isStealth() {
        return this.f_19804_.get(STEALTH_MODE);
    }

    public void setStealth(boolean bar) {
        this.f_19804_.set(STEALTH_MODE, bar);
    }

    public boolean isHolding() {
        return this.f_19804_.get(HOLDING);
    }

    public void setHolding(boolean running) {
        this.f_19804_.set(HOLDING, running);
    }

    @Override
    public boolean isSleeping() {
        return this.f_19804_.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.f_19804_.set(SLEEPING, sleeping);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.f_19804_.get(ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.f_19804_.set(ANGER_TIME, time);
    }

    @Override
    public UUID getPersistentAngerTarget() {
        return this.lastHurtBy;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.lastHurtBy = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGRY_TIMER.sample(this.f_19796_));
    }

    @Override
    protected void customServerAiStep() {
        if (!this.m_9236_().isClientSide) {
            this.m_21666_((ServerLevel) this.m_9236_(), false);
        }
    }

    @Override
    public boolean isColliding(BlockPos pos, BlockState blockstate) {
        return blockstate.m_60734_() != Blocks.BAMBOO && !blockstate.m_204336_(BlockTags.LEAVES) && super.m_20039_(pos, blockstate);
    }

    @Override
    public Vec3 collide(Vec3 vec3) {
        return ICustomCollisions.getAllowedMovementForEntity(this, vec3);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSitProgress = this.sitProgress;
        this.prevSleepProgress = this.sleepProgress;
        this.prevHoldProgress = this.holdProgress;
        this.prevStealthProgress = this.stealthProgress;
        boolean sitting = this.isSitting();
        boolean sleeping = this.isSleeping();
        boolean holding = this.isHolding();
        boolean stealth = this.isStealth();
        if (sitting) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (sleeping) {
            if (this.sleepProgress < 5.0F) {
                this.sleepProgress++;
            }
        } else if (this.sleepProgress > 0.0F) {
            this.sleepProgress--;
        }
        if (holding) {
            if (this.holdProgress < 5.0F) {
                this.holdProgress++;
            }
        } else if (this.holdProgress > 0.0F) {
            this.holdProgress--;
        }
        if (stealth) {
            if (this.stealthProgress < 10.0F) {
                this.stealthProgress += 0.25F;
            }
        } else if (this.stealthProgress > 0.0F) {
            this.stealthProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isRunning() && !this.hasSpedUp) {
                this.hasSpedUp = true;
                this.m_274367_(1.0F);
                this.m_6858_(true);
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.4F);
            }
            if (!this.isRunning() && this.hasSpedUp) {
                this.hasSpedUp = false;
                this.m_274367_(0.6F);
                this.m_6858_(false);
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
            }
            if ((this.isSitting() || this.isSleeping()) && (++this.sittingTime > this.maxSitTime || this.m_5448_() != null || this.m_27593_() || this.dontSitFlag || this.m_20072_())) {
                this.setSitting(false);
                this.setSleeping(false);
                this.sittingTime = 0;
                this.maxSitTime = 100 + this.f_19796_.nextInt(50);
            }
            if (this.m_5448_() == null && !this.dontSitFlag && this.m_20184_().lengthSqr() < 0.03 && this.getAnimation() == NO_ANIMATION && !this.isSleeping() && !this.isSitting() && !this.m_20072_() && this.f_19796_.nextInt(100) == 0) {
                this.sittingTime = 0;
                if (this.m_217043_().nextBoolean()) {
                    this.maxSitTime = 100 + this.f_19796_.nextInt(550);
                    this.setSitting(true);
                    this.setSleeping(false);
                } else {
                    this.maxSitTime = 200 + this.f_19796_.nextInt(550);
                    this.setSitting(false);
                    this.setSleeping(true);
                }
            }
            if (this.m_20184_().lengthSqr() < 0.03 && this.getAnimation() == NO_ANIMATION && !this.isSleeping() && !this.isSitting() && this.f_19796_.nextInt(100) == 0) {
                this.setAnimation(ANIMATION_TAIL_FLICK);
            }
        }
        if (this.isHolding()) {
            this.m_6858_(false);
            this.setRunning(false);
            if (!this.m_9236_().isClientSide && this.m_5448_() != null && this.m_5448_().isAlive()) {
                this.m_146926_(0.0F);
                float radius = 1.0F + this.m_5448_().m_20205_() * 0.5F;
                float angle = (float) (Math.PI / 180.0) * this.f_20883_;
                double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
                double extraZ = (double) (radius * Mth.cos(angle));
                double extraY = -0.5;
                Vec3 minus = new Vec3(this.m_20185_() + extraX - this.m_5448_().m_20185_(), this.m_20186_() + -0.5 - this.m_5448_().m_20186_(), this.m_20189_() + extraZ - this.m_5448_().m_20189_());
                this.m_5448_().m_20256_(minus);
                if (this.holdTime % 20 == 0) {
                    this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) (5 + this.m_217043_().nextInt(2)));
                }
            }
            this.holdTime++;
            if (this.holdTime > 100) {
                this.holdTime = 0;
                this.setHolding(false);
            }
        } else {
            this.holdTime = 0;
        }
        if (this.prevScaredMobId != this.f_19804_.get(LAST_SCARED_MOB_ID) && this.m_9236_().isClientSide) {
            Entity e = this.m_9236_().getEntity(this.f_19804_.get(LAST_SCARED_MOB_ID));
            if (e != null) {
                double d2 = this.f_19796_.nextGaussian() * 0.1;
                double d0 = this.f_19796_.nextGaussian() * 0.1;
                double d1 = this.f_19796_.nextGaussian() * 0.1;
                this.m_9236_().addParticle(AMParticleRegistry.SHOCKED.get(), e.getX(), e.getEyeY() + (double) (e.getBbHeight() * 0.15F) + (double) (this.f_19796_.nextFloat() * e.getBbHeight() * 0.15F), e.getZ(), d0, d1, d2);
            }
        }
        if (this.m_5448_() != null && this.m_5448_().hasEffect(AMEffectRegistry.TIGERS_BLESSING.get())) {
            this.m_6710_(null);
            this.m_6703_(null);
        }
        this.prevScaredMobId = this.f_19804_.get(LAST_SCARED_MOB_ID);
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev) {
            if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
                LivingEntity hurter = (LivingEntity) source.getEntity();
                if (hurter.hasEffect(AMEffectRegistry.TIGERS_BLESSING.get())) {
                    hurter.removeEffect(AMEffectRegistry.TIGERS_BLESSING.get());
                }
            }
            return prev;
        } else {
            return prev;
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public BlockPos getLightPosition() {
        BlockPos pos = AMBlockPos.fromVec3(this.m_20182_());
        return !this.m_9236_().getBlockState(pos).m_60815_() ? pos.above() : pos;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        boolean whiteOther = p_241840_2_ instanceof EntityTiger && ((EntityTiger) p_241840_2_).isWhite();
        EntityTiger baby = AMEntityRegistry.TIGER.get().create(p_241840_1_);
        double whiteChance = 0.1;
        if (this.isWhite() && whiteOther) {
            whiteChance = 0.8;
        }
        if (this.isWhite() != whiteOther) {
            whiteChance = 0.4;
        }
        baby.setWhite(this.f_19796_.nextDouble() < whiteChance);
        return baby;
    }

    @Override
    public boolean canPassThrough(BlockPos mutablePos, BlockState blockstate, VoxelShape voxelshape) {
        return blockstate.m_60734_() == Blocks.BAMBOO || blockstate.m_204336_(BlockTags.LEAVES);
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
        return new Animation[] { ANIMATION_PAW_R, ANIMATION_PAW_L, ANIMATION_LEAP, ANIMATION_TAIL_FLICK };
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
    public void push(Entity entityIn) {
        if (!this.isHolding() || entityIn != this.m_5448_()) {
            super.m_7334_(entityIn);
        }
    }

    @Override
    protected void doPush(Entity entityIn) {
        if (!this.isHolding() || entityIn != this.m_5448_()) {
            super.m_7324_(entityIn);
        }
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.getItem().isEdible() && stack.getItem().getFoodProperties() != null && stack.getItem().getFoodProperties().isMeat() && stack.getItem() != Items.ROTTEN_FLESH;
    }

    @Override
    public double getMaxDistToItem() {
        return 3.0;
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.dontSitFlag = false;
        ItemStack stack = e.getItem();
        if (stack.getItem().isEdible() && stack.getItem().getFoodProperties() != null && stack.getItem().getFoodProperties().isMeat() && stack.getItem() != Items.ROTTEN_FLESH) {
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.CAT_EAT, this.m_6100_(), this.m_6121_());
            this.m_5634_(5.0F);
            Entity thrower = e.getOwner();
            if (thrower != null && (double) this.f_19796_.nextFloat() < this.getChanceForEffect(stack) && this.m_9236_().m_46003_(thrower.getUUID()) != null) {
                Player player = this.m_9236_().m_46003_(thrower.getUUID());
                player.m_7292_(new MobEffectInstance(AMEffectRegistry.TIGERS_BLESSING.get(), 12000));
                this.m_6710_(null);
                this.m_6703_(null);
            }
        }
    }

    @Override
    public void onFindTarget(ItemEntity e) {
        this.dontSitFlag = true;
        this.setSitting(false);
        this.setSleeping(false);
    }

    public double getChanceForEffect(ItemStack stack) {
        if (stack.getItem() == Items.PORKCHOP || stack.getItem() == Items.COOKED_PORKCHOP) {
            return 0.4F;
        } else {
            return stack.getItem() != Items.CHICKEN && stack.getItem() != Items.COOKED_CHICKEN ? 0.1F : 0.3F;
        }
    }

    @Override
    protected void jumpFromGround() {
        if (!this.isSleeping() && !this.isSitting()) {
            super.m_6135_();
        }
    }

    private class AIMelee extends Goal {

        private final EntityTiger tiger;

        private int jumpAttemptCooldown = 0;

        public AIMelee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.tiger = EntityTiger.this;
        }

        @Override
        public boolean canUse() {
            return this.tiger.m_5448_() != null && this.tiger.m_5448_().isAlive();
        }

        @Override
        public void tick() {
            if (this.jumpAttemptCooldown > 0) {
                this.jumpAttemptCooldown--;
            }
            LivingEntity target = this.tiger.m_5448_();
            if (target != null && target.isAlive()) {
                double dist = (double) this.tiger.m_20270_(target);
                if (dist < 10.0 && this.tiger.m_21188_() != null && this.tiger.m_21188_().isAlive()) {
                    this.tiger.setStealth(false);
                } else if (dist > 20.0) {
                    this.tiger.setRunning(false);
                    this.tiger.setStealth(true);
                }
                if (dist <= 20.0) {
                    this.tiger.setStealth(false);
                    this.tiger.setRunning(true);
                    if (this.tiger.f_19804_.get(EntityTiger.LAST_SCARED_MOB_ID) != target.m_19879_()) {
                        this.tiger.f_19804_.set(EntityTiger.LAST_SCARED_MOB_ID, target.m_19879_());
                        target.addEffect(new MobEffectInstance(AMEffectRegistry.FEAR.get(), 100, 0, true, false));
                    }
                }
                if (dist < 12.0 && this.tiger.getAnimation() == IAnimatedEntity.NO_ANIMATION && this.tiger.m_20096_() && this.jumpAttemptCooldown == 0 && !this.tiger.isHolding()) {
                    this.tiger.setAnimation(EntityTiger.ANIMATION_LEAP);
                    this.jumpAttemptCooldown = 70;
                }
                if ((this.jumpAttemptCooldown > 0 || this.tiger.m_20072_()) && !this.tiger.isHolding() && this.tiger.getAnimation() == IAnimatedEntity.NO_ANIMATION && dist < (double) (4.0F + target.m_20205_())) {
                    this.tiger.setAnimation(this.tiger.m_217043_().nextBoolean() ? EntityTiger.ANIMATION_PAW_L : EntityTiger.ANIMATION_PAW_R);
                }
                if (dist < (double) (4.0F + target.m_20205_()) && (this.tiger.getAnimation() == EntityTiger.ANIMATION_PAW_L || this.tiger.getAnimation() == EntityTiger.ANIMATION_PAW_R) && this.tiger.getAnimationTick() == 8) {
                    target.hurt(this.tiger.m_269291_().mobAttack(this.tiger), (float) (7 + this.tiger.m_217043_().nextInt(5)));
                }
                if (this.tiger.getAnimation() == EntityTiger.ANIMATION_LEAP) {
                    this.tiger.m_21573_().stop();
                    Vec3 vec = target.m_20182_().subtract(this.tiger.m_20182_());
                    this.tiger.m_146922_(-((float) Mth.atan2(vec.x, vec.z)) * (180.0F / (float) Math.PI));
                    this.tiger.f_20883_ = this.tiger.m_146908_();
                    if (this.tiger.getAnimationTick() >= 5 && this.tiger.getAnimationTick() < 11 && this.tiger.m_20096_()) {
                        Vec3 vector3d1 = new Vec3(target.m_20185_() - this.tiger.m_20185_(), 0.0, target.m_20189_() - this.tiger.m_20189_());
                        if (vector3d1.lengthSqr() > 1.0E-7) {
                            vector3d1 = vector3d1.normalize().scale(Math.min(dist, 15.0) * 0.2F);
                        }
                        this.tiger.m_20334_(vector3d1.x, vector3d1.y + 0.3F + 0.1F * Mth.clamp(target.m_20188_() - this.tiger.m_20186_(), 0.0, 2.0), vector3d1.z);
                    }
                    if (dist < (double) (target.m_20205_() + 3.0F) && this.tiger.getAnimationTick() >= 15) {
                        target.hurt(this.tiger.m_269291_().mobAttack(this.tiger), 2.0F);
                        this.tiger.setRunning(false);
                        this.tiger.setStealth(false);
                        this.tiger.setHolding(true);
                    }
                } else if (target != null) {
                    this.tiger.m_21573_().moveTo(target, this.tiger.isStealth() ? 0.75 : 1.0);
                }
            }
        }

        @Override
        public void stop() {
            this.tiger.setStealth(false);
            this.tiger.setRunning(false);
            this.tiger.setHolding(false);
        }
    }

    class AngerGoal extends HurtByTargetGoal {

        AngerGoal(EntityTiger beeIn) {
            super(beeIn);
        }

        @Override
        public boolean canContinueToUse() {
            return EntityTiger.this.m_21660_() && super.m_8045_();
        }

        @Override
        public void start() {
            super.start();
            if (EntityTiger.this.m_6162_()) {
                this.m_26047_();
                this.m_8041_();
            }
        }

        @Override
        protected void alertOther(Mob mobIn, LivingEntity targetIn) {
            if (!mobIn.m_6162_()) {
                super.alertOther(mobIn, targetIn);
            }
        }
    }

    class AttackPlayerGoal extends NearestAttackableTargetGoal<Player> {

        public AttackPlayerGoal() {
            super(EntityTiger.this, Player.class, 100, false, true, EntityTiger.NO_BLESSING_EFFECT);
        }

        @Override
        public boolean canUse() {
            return EntityTiger.this.m_6162_() ? false : super.canUse();
        }

        @Override
        protected double getFollowDistance() {
            return 4.0;
        }
    }

    static class Navigator extends GroundPathNavigatorWide {

        public Navigator(Mob mob, Level world) {
            super(mob, world, 1.2F);
        }

        @Override
        protected PathFinder createPathFinder(int i) {
            this.f_26508_ = new EntityTiger.TigerNodeEvaluator();
            return new PathFinder(this.f_26508_, i);
        }
    }

    static class TigerNodeEvaluator extends WalkNodeEvaluator {

        @Override
        protected BlockPathTypes evaluateBlockPathType(BlockGetter level, BlockPos pos, BlockPathTypes typeIn) {
            return typeIn != BlockPathTypes.LEAVES && level.getBlockState(pos).m_60734_() != Blocks.BAMBOO ? super.evaluateBlockPathType(level, pos, typeIn) : BlockPathTypes.OPEN;
        }
    }
}