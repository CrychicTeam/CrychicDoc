package net.minecraft.world.entity.animal;

import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BegGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class Wolf extends TamableAnimal implements NeutralMob {

    private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.INT);

    public static final Predicate<LivingEntity> PREY_SELECTOR = p_289448_ -> {
        EntityType<?> $$1 = p_289448_.m_6095_();
        return $$1 == EntityType.SHEEP || $$1 == EntityType.RABBIT || $$1 == EntityType.FOX;
    };

    private static final float START_HEALTH = 8.0F;

    private static final float TAME_HEALTH = 20.0F;

    private float interestedAngle;

    private float interestedAngleO;

    private boolean isWet;

    private boolean isShaking;

    private float shakeAnim;

    private float shakeAnimO;

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    @Nullable
    private UUID persistentAngerTarget;

    public Wolf(EntityType<? extends Wolf> entityTypeExtendsWolf0, Level level1) {
        super(entityTypeExtendsWolf0, level1);
        this.setTame(false);
        this.m_21441_(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.m_21441_(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(1, new Wolf.WolfPanicGoal(1.5));
        this.f_21345_.addGoal(2, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(3, new Wolf.WolfAvoidEntityGoal(this, Llama.class, 24.0F, 1.5, 1.5));
        this.f_21345_.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.f_21345_.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.f_21345_.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.f_21345_.addGoal(7, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(9, new BegGoal(this, 8.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::m_21674_));
        this.f_21346_.addGoal(5, new NonTameRandomTargetGoal(this, Animal.class, false, PREY_SELECTOR));
        this.f_21346_.addGoal(6, new NonTameRandomTargetGoal(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.f_21346_.addGoal(7, new NearestAttackableTargetGoal(this, AbstractSkeleton.class, false));
        this.f_21346_.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_INTERESTED_ID, false);
        this.f_19804_.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
        this.f_19804_.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putByte("CollarColor", (byte) this.getCollarColor().getId());
        this.m_21678_(compoundTag0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(compoundTag0.getInt("CollarColor")));
        }
        this.m_147285_(this.m_9236_(), compoundTag0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.m_21660_()) {
            return SoundEvents.WOLF_GROWL;
        } else if (this.f_19796_.nextInt(3) == 0) {
            return this.m_21824_() && this.m_21223_() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
        } else {
            return SoundEvents.WOLF_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_9236_().isClientSide && this.isWet && !this.isShaking && !this.m_21691_() && this.m_20096_()) {
            this.isShaking = true;
            this.shakeAnim = 0.0F;
            this.shakeAnimO = 0.0F;
            this.m_9236_().broadcastEntityEvent(this, (byte) 8);
        }
        if (!this.m_9236_().isClientSide) {
            this.m_21666_((ServerLevel) this.m_9236_(), true);
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.m_6084_()) {
            this.interestedAngleO = this.interestedAngle;
            if (this.isInterested()) {
                this.interestedAngle = this.interestedAngle + (1.0F - this.interestedAngle) * 0.4F;
            } else {
                this.interestedAngle = this.interestedAngle + (0.0F - this.interestedAngle) * 0.4F;
            }
            if (this.m_20071_()) {
                this.isWet = true;
                if (this.isShaking && !this.m_9236_().isClientSide) {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 56);
                    this.cancelShake();
                }
            } else if ((this.isWet || this.isShaking) && this.isShaking) {
                if (this.shakeAnim == 0.0F) {
                    this.m_5496_(SoundEvents.WOLF_SHAKE, this.getSoundVolume(), (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
                    this.m_146850_(GameEvent.ENTITY_SHAKE);
                }
                this.shakeAnimO = this.shakeAnim;
                this.shakeAnim += 0.05F;
                if (this.shakeAnimO >= 2.0F) {
                    this.isWet = false;
                    this.isShaking = false;
                    this.shakeAnimO = 0.0F;
                    this.shakeAnim = 0.0F;
                }
                if (this.shakeAnim > 0.4F) {
                    float $$0 = (float) this.m_20186_();
                    int $$1 = (int) (Mth.sin((this.shakeAnim - 0.4F) * (float) Math.PI) * 7.0F);
                    Vec3 $$2 = this.m_20184_();
                    for (int $$3 = 0; $$3 < $$1; $$3++) {
                        float $$4 = (this.f_19796_.nextFloat() * 2.0F - 1.0F) * this.m_20205_() * 0.5F;
                        float $$5 = (this.f_19796_.nextFloat() * 2.0F - 1.0F) * this.m_20205_() * 0.5F;
                        this.m_9236_().addParticle(ParticleTypes.SPLASH, this.m_20185_() + (double) $$4, (double) ($$0 + 0.8F), this.m_20189_() + (double) $$5, $$2.x, $$2.y, $$2.z);
                    }
                }
            }
        }
    }

    private void cancelShake() {
        this.isShaking = false;
        this.shakeAnim = 0.0F;
        this.shakeAnimO = 0.0F;
    }

    @Override
    public void die(DamageSource damageSource0) {
        this.isWet = false;
        this.isShaking = false;
        this.shakeAnimO = 0.0F;
        this.shakeAnim = 0.0F;
        super.die(damageSource0);
    }

    public boolean isWet() {
        return this.isWet;
    }

    public float getWetShade(float float0) {
        return Math.min(0.5F + Mth.lerp(float0, this.shakeAnimO, this.shakeAnim) / 2.0F * 0.5F, 1.0F);
    }

    public float getBodyRollAngle(float float0, float float1) {
        float $$2 = (Mth.lerp(float0, this.shakeAnimO, this.shakeAnim) + float1) / 1.8F;
        if ($$2 < 0.0F) {
            $$2 = 0.0F;
        } else if ($$2 > 1.0F) {
            $$2 = 1.0F;
        }
        return Mth.sin($$2 * (float) Math.PI) * Mth.sin($$2 * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
    }

    public float getHeadRollAngle(float float0) {
        return Mth.lerp(float0, this.interestedAngleO, this.interestedAngle) * 0.15F * (float) Math.PI;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height * 0.8F;
    }

    @Override
    public int getMaxHeadXRot() {
        return this.m_21825_() ? 20 : super.m_8132_();
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else {
            Entity $$2 = damageSource0.getEntity();
            if (!this.m_9236_().isClientSide) {
                this.m_21839_(false);
            }
            if ($$2 != null && !($$2 instanceof Player) && !($$2 instanceof AbstractArrow)) {
                float1 = (float1 + 1.0F) / 2.0F;
            }
            return super.m_6469_(damageSource0, float1);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        boolean $$1 = entity0.hurt(this.m_269291_().mobAttack(this), (float) ((int) this.m_21133_(Attributes.ATTACK_DAMAGE)));
        if ($$1) {
            this.m_19970_(this, entity0);
        }
        return $$1;
    }

    @Override
    public void setTame(boolean boolean0) {
        super.setTame(boolean0);
        if (boolean0) {
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(20.0);
            this.m_21153_(20.0F);
        } else {
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(8.0);
        }
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        Item $$3 = $$2.getItem();
        if (this.m_9236_().isClientSide) {
            boolean $$4 = this.m_21830_(player0) || this.m_21824_() || $$2.is(Items.BONE) && !this.m_21824_() && !this.m_21660_();
            return $$4 ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else if (this.m_21824_()) {
            if (this.isFood($$2) && this.m_21223_() < this.m_21233_()) {
                if (!player0.getAbilities().instabuild) {
                    $$2.shrink(1);
                }
                this.m_5634_((float) $$3.getFoodProperties().getNutrition());
                return InteractionResult.SUCCESS;
            } else {
                if ($$3 instanceof DyeItem $$5 && this.m_21830_(player0)) {
                    DyeColor $$6 = $$5.getDyeColor();
                    if ($$6 != this.getCollarColor()) {
                        this.setCollarColor($$6);
                        if (!player0.getAbilities().instabuild) {
                            $$2.shrink(1);
                        }
                        return InteractionResult.SUCCESS;
                    }
                    return super.m_6071_(player0, interactionHand1);
                }
                InteractionResult $$7 = super.m_6071_(player0, interactionHand1);
                if ((!$$7.consumesAction() || this.m_6162_()) && this.m_21830_(player0)) {
                    this.m_21839_(!this.m_21827_());
                    this.f_20899_ = false;
                    this.f_21344_.stop();
                    this.m_6710_(null);
                    return InteractionResult.SUCCESS;
                } else {
                    return $$7;
                }
            }
        } else if ($$2.is(Items.BONE) && !this.m_21660_()) {
            if (!player0.getAbilities().instabuild) {
                $$2.shrink(1);
            }
            if (this.f_19796_.nextInt(3) == 0) {
                this.m_21828_(player0);
                this.f_21344_.stop();
                this.m_6710_(null);
                this.m_21839_(true);
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.m_6071_(player0, interactionHand1);
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 8) {
            this.isShaking = true;
            this.shakeAnim = 0.0F;
            this.shakeAnimO = 0.0F;
        } else if (byte0 == 56) {
            this.cancelShake();
        } else {
            super.handleEntityEvent(byte0);
        }
    }

    public float getTailAngle() {
        if (this.m_21660_()) {
            return 1.5393804F;
        } else {
            return this.m_21824_() ? (0.55F - (this.m_21233_() - this.m_21223_()) * 0.02F) * (float) Math.PI : (float) (Math.PI / 5);
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        Item $$1 = itemStack0.getItem();
        return $$1.isEdible() && $$1.getFoodProperties().isMeat();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.f_19804_.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int int0) {
        this.f_19804_.set(DATA_REMAINING_ANGER_TIME, int0);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.f_19796_));
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uUID0) {
        this.persistentAngerTarget = uUID0;
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.f_19804_.get(DATA_COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor dyeColor0) {
        this.f_19804_.set(DATA_COLLAR_COLOR, dyeColor0.getId());
    }

    @Nullable
    public Wolf getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        Wolf $$2 = EntityType.WOLF.create(serverLevel0);
        if ($$2 != null) {
            UUID $$3 = this.m_21805_();
            if ($$3 != null) {
                $$2.m_21816_($$3);
                $$2.setTame(true);
            }
        }
        return $$2;
    }

    public void setIsInterested(boolean boolean0) {
        this.f_19804_.set(DATA_INTERESTED_ID, boolean0);
    }

    @Override
    public boolean canMate(Animal animal0) {
        if (animal0 == this) {
            return false;
        } else if (!this.m_21824_()) {
            return false;
        } else if (!(animal0 instanceof Wolf $$1)) {
            return false;
        } else if (!$$1.m_21824_()) {
            return false;
        } else {
            return $$1.m_21825_() ? false : this.m_27593_() && $$1.m_27593_();
        }
    }

    public boolean isInterested() {
        return this.f_19804_.get(DATA_INTERESTED_ID);
    }

    @Override
    public boolean wantsToAttack(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        if (livingEntity0 instanceof Creeper || livingEntity0 instanceof Ghast) {
            return false;
        } else if (livingEntity0 instanceof Wolf $$2) {
            return !$$2.m_21824_() || $$2.m_269323_() != livingEntity1;
        } else if (livingEntity0 instanceof Player && livingEntity1 instanceof Player && !((Player) livingEntity1).canHarmPlayer((Player) livingEntity0)) {
            return false;
        } else {
            return livingEntity0 instanceof AbstractHorse && ((AbstractHorse) livingEntity0).isTamed() ? false : !(livingEntity0 instanceof TamableAnimal) || !((TamableAnimal) livingEntity0).isTame();
        }
    }

    @Override
    public boolean canBeLeashed(Player player0) {
        return !this.m_21660_() && super.canBeLeashed(player0);
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (0.6F * this.m_20192_()), (double) (this.m_20205_() * 0.4F));
    }

    public static boolean checkWolfSpawnRules(EntityType<Wolf> entityTypeWolf0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return levelAccessor1.m_8055_(blockPos3.below()).m_204336_(BlockTags.WOLVES_SPAWNABLE_ON) && m_186209_(levelAccessor1, blockPos3);
    }

    class WolfAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

        private final Wolf wolf;

        public WolfAvoidEntityGoal(Wolf wolf0, Class<T> classT1, float float2, double double3, double double4) {
            super(wolf0, classT1, float2, double3, double4);
            this.wolf = wolf0;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.f_25016_ instanceof Llama ? !this.wolf.m_21824_() && this.avoidLlama((Llama) this.f_25016_) : false;
        }

        private boolean avoidLlama(Llama llama0) {
            return llama0.getStrength() >= Wolf.this.f_19796_.nextInt(5);
        }

        @Override
        public void start() {
            Wolf.this.m_6710_(null);
            super.start();
        }

        @Override
        public void tick() {
            Wolf.this.m_6710_(null);
            super.tick();
        }
    }

    class WolfPanicGoal extends PanicGoal {

        public WolfPanicGoal(double double0) {
            super(Wolf.this, double0);
        }

        @Override
        protected boolean shouldPanic() {
            return this.f_25684_.m_203117_() || this.f_25684_.m_6060_();
        }
    }
}