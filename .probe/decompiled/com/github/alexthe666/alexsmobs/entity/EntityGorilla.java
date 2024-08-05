package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIRideParent;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.GorillaAIChargeLooker;
import com.github.alexthe666.alexsmobs.entity.ai.GorillaAIFollowCaravan;
import com.github.alexthe666.alexsmobs.entity.ai.GorillaAIForageLeaves;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAITempt;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityGorilla extends TamableAnimal implements IAnimatedEntity, ITargetsDroppedItems {

    public static final Animation ANIMATION_BREAKBLOCK_R = Animation.create(20);

    public static final Animation ANIMATION_BREAKBLOCK_L = Animation.create(20);

    public static final Animation ANIMATION_POUNDCHEST = Animation.create(40);

    public static final Animation ANIMATION_ATTACK = Animation.create(20);

    protected static final EntityDimensions SILVERBACK_SIZE = EntityDimensions.scalable(1.35F, 1.95F);

    private static final EntityDataAccessor<Boolean> SILVERBACK = SynchedEntityData.defineId(EntityGorilla.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.defineId(EntityGorilla.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityGorilla.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(EntityGorilla.class, EntityDataSerializers.BOOLEAN);

    public int maxStandTime = 75;

    public float prevStandProgress;

    public float prevSitProgress;

    public float standProgress;

    public float sitProgress;

    public boolean forcedSit = false;

    private int animationTick;

    private Animation currentAnimation;

    private int standingTime = 0;

    private int eatingTime;

    @Nullable
    private EntityGorilla caravanHead;

    @Nullable
    private EntityGorilla caravanTail;

    private int sittingTime = 0;

    private int maxSitTime = 75;

    @Nullable
    private UUID bananaThrowerID = null;

    private boolean hasSilverbackAttributes = false;

    public int poundChestCooldown = 0;

    protected EntityGorilla(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.LEAVES, 0.0F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ARMOR, 0.0).add(Attributes.ATTACK_DAMAGE, 7.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.5).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static boolean isBanana(ItemStack stack) {
        return stack.is(AMTagRegistry.BANANAS);
    }

    public static boolean canGorillaSpawn(EntityType<EntityGorilla> gorilla, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.m_8055_(p_223317_3_.below());
        return (blockstate.m_204336_(BlockTags.LEAVES) || blockstate.m_60713_(Blocks.GRASS_BLOCK) || blockstate.m_204336_(BlockTags.LOGS) || blockstate.m_60713_(Blocks.AIR)) && worldIn.m_45524_(p_223317_3_, 0) > 8;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.gorillaSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return this.m_21824_() && isBanana(stack);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            this.setOrderedToSit(false);
            if (entity != null && this.m_21824_() && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
                amount = (amount + 1.0F) / 2.0F;
            }
            return super.m_6469_(source, amount);
        }
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, 1.2, true));
        this.f_21345_.addGoal(2, new GorillaAIFollowCaravan(this, 0.8));
        this.f_21345_.addGoal(3, new GorillaAIChargeLooker(this, 1.6));
        this.f_21345_.addGoal(4, new TameableAITempt(this, 1.1, Ingredient.of(AMTagRegistry.BANANAS), false));
        this.f_21345_.addGoal(4, new AnimalAIRideParent(this, 1.25));
        this.f_21345_.addGoal(6, new EntityGorilla.AIWalkIdle(this, 0.8));
        this.f_21345_.addGoal(5, new GorillaAIForageLeaves(this));
        this.f_21345_.addGoal(5, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.GORILLA_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.GORILLA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.GORILLA_HURT.get();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ATTACK);
        }
        return true;
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isSitting()) {
            if (this.getNavigation().getPath() != null) {
                this.getNavigation().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (spawnDataIn instanceof AgeableMob.AgeableMobGroupData lvt_6_1_) {
            if (lvt_6_1_.getGroupSize() == 0) {
                this.setSilverback(true);
            }
        } else {
            this.setSilverback(this.m_217043_().nextBoolean());
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Nullable
    public EntityGorilla getNearestSilverback(LevelAccessor world, double dist) {
        List<? extends EntityGorilla> list = world.m_45976_(this.getClass(), this.m_20191_().inflate(dist, dist / 2.0, dist));
        if (list.isEmpty()) {
            return null;
        } else {
            EntityGorilla gorilla = null;
            double d0 = Double.MAX_VALUE;
            for (EntityGorilla gorrila2 : list) {
                if (gorrila2.isSilverback()) {
                    double d1 = this.m_20280_(gorrila2);
                    if (!(d1 > d0)) {
                        d0 = d1;
                        gorilla = gorrila2;
                    }
                }
            }
            return gorilla;
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isSilverback() && !this.m_6162_() ? SILVERBACK_SIZE.scale(this.m_6134_()) : super.m_6972_(poseIn);
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            this.setOrderedToSit(false);
            if (passenger instanceof EntityGorilla babyGorilla) {
                babyGorilla.setStanding(this.isStanding());
                babyGorilla.setOrderedToSit(this.isSitting());
                babyGorilla.f_20883_ = this.f_20883_;
            }
            float sitAdd = -0.03F * this.sitProgress;
            float standAdd = -0.03F * this.standProgress;
            float radius = standAdd + sitAdd;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), this.m_20189_() + extraZ);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.m_20206_() * 0.65F * (double) this.getGorillaScale() * (double) (this.isSilverback() ? 0.75F : 1.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(SILVERBACK, false);
        this.f_19804_.define(STANDING, false);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(EATING, false);
    }

    public boolean isSilverback() {
        return this.f_19804_.get(SILVERBACK);
    }

    public void setSilverback(boolean silver) {
        this.f_19804_.set(SILVERBACK, silver);
    }

    public boolean isStanding() {
        return this.f_19804_.get(STANDING);
    }

    public void setStanding(boolean standing) {
        this.f_19804_.set(STANDING, standing);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public boolean isEating() {
        return this.f_19804_.get(EATING);
    }

    public void setEating(boolean eating) {
        this.f_19804_.set(EATING, eating);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Silverback", this.isSilverback());
        compound.putBoolean("Standing", this.isStanding());
        compound.putBoolean("GorillaSitting", this.isSitting());
        compound.putBoolean("ForcedToSit", this.forcedSit);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSilverback(compound.getBoolean("Silverback"));
        this.setStanding(compound.getBoolean("Standing"));
        this.setOrderedToSit(compound.getBoolean("GorillaSitting"));
        this.forcedSit = compound.getBoolean("ForcedToSit");
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        if (itemstack.getItem() == Items.NAME_TAG) {
            return super.m_6071_(player, hand);
        } else if (this.m_21824_() && isBanana(itemstack) && this.m_21223_() < this.m_21233_()) {
            this.m_5634_(5.0F);
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
            return InteractionResult.SUCCESS;
        } else {
            InteractionResult type = super.m_6071_(player, hand);
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            if (interactionresult == InteractionResult.SUCCESS || type == InteractionResult.SUCCESS || !this.m_21824_() || !this.m_21830_(player) || this.isFood(itemstack)) {
                return type;
            } else if (this.isSitting()) {
                this.forcedSit = false;
                this.setOrderedToSit(false);
                return InteractionResult.SUCCESS;
            } else {
                this.forcedSit = true;
                this.setOrderedToSit(true);
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
        if (animation == ANIMATION_POUNDCHEST) {
            this.maxStandTime = 45;
            this.setStanding(true);
        }
        if (animation == ANIMATION_ATTACK) {
            this.maxStandTime = 10;
            this.setStanding(true);
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && this.canTargetItem(this.m_21120_(InteractionHand.MAIN_HAND))) {
            this.setEating(true);
            this.setOrderedToSit(true);
            this.setStanding(false);
        }
        if (this.isEating() && !this.canTargetItem(this.m_21120_(InteractionHand.MAIN_HAND))) {
            this.setEating(false);
            this.eatingTime = 0;
            if (!this.forcedSit) {
                this.setOrderedToSit(true);
            }
        }
        if (this.isEating()) {
            this.eatingTime++;
            if (!this.m_21205_().is(ItemTags.LEAVES)) {
                for (int i = 0; i < 3; i++) {
                    double d2 = this.f_19796_.nextGaussian() * 0.02;
                    double d0 = this.f_19796_.nextGaussian() * 0.02;
                    double d1 = this.f_19796_.nextGaussian() * 0.02;
                    this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_21120_(InteractionHand.MAIN_HAND)), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
                }
            }
            if (this.eatingTime % 5 == 0) {
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.PANDA_EAT, this.m_6121_(), this.m_6100_());
            }
            if (this.eatingTime > 100) {
                ItemStack stack = this.m_21120_(InteractionHand.MAIN_HAND);
                if (!stack.isEmpty()) {
                    this.m_5634_(4.0F);
                    if (isBanana(stack) && this.bananaThrowerID != null) {
                        if (this.m_217043_().nextFloat() < 0.3F) {
                            this.m_7105_(true);
                            this.m_21816_(this.bananaThrowerID);
                            Player player = this.m_9236_().m_46003_(this.bananaThrowerID);
                            if (player instanceof ServerPlayer) {
                                CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer) player, this);
                            }
                            this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                        } else {
                            this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                        }
                    }
                    if (stack.hasCraftingRemainingItem()) {
                        this.m_19983_(stack.getCraftingRemainingItem());
                    }
                    stack.shrink(1);
                }
                this.eatingTime = 0;
            }
        }
        this.prevSitProgress = this.sitProgress;
        this.prevStandProgress = this.standProgress;
        if (this.isSitting()) {
            if (this.sitProgress < 10.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (this.isStanding()) {
            if (this.standProgress < 10.0F) {
                this.standProgress++;
            }
        } else if (this.standProgress > 0.0F) {
            this.standProgress--;
        }
        if (this.m_20159_() && this.m_20202_() instanceof EntityGorilla) {
            if (!this.m_6162_()) {
                this.m_6038_();
            } else {
                EntityGorilla mount = (EntityGorilla) this.m_20202_();
                this.m_146922_(mount.f_20883_);
                this.f_20885_ = mount.f_20883_;
                this.f_20883_ = mount.f_20883_;
            }
        }
        if (this.isStanding() && ++this.standingTime > this.maxStandTime) {
            this.setStanding(false);
            this.standingTime = 0;
            this.maxStandTime = 75 + this.f_19796_.nextInt(50);
        }
        if (!this.forcedSit && this.isSitting() && ++this.sittingTime > this.maxSitTime) {
            this.setOrderedToSit(false);
            this.sittingTime = 0;
            this.maxSitTime = 75 + this.f_19796_.nextInt(50);
        }
        if (!this.forcedSit && this.isSitting() && (this.m_5448_() != null || this.isStanding()) && !this.isEating()) {
            this.setOrderedToSit(false);
        }
        if (!this.m_9236_().isClientSide && this.getAnimation() == NO_ANIMATION && !this.isStanding() && !this.isSitting() && this.f_19796_.nextInt(1500) == 0) {
            this.maxSitTime = 300 + this.f_19796_.nextInt(250);
            this.setOrderedToSit(true);
        }
        if (this.forcedSit && !this.m_20160_() && this.m_21824_()) {
            this.setOrderedToSit(true);
        }
        if (this.sitProgress == 0.0F && this.poundChestCooldown <= 0 && this.isSilverback() && this.f_19796_.nextInt(800) == 0 && this.getAnimation() == NO_ANIMATION && !this.isSitting() && !this.m_21525_() && this.m_21205_().isEmpty()) {
            this.setAnimation(ANIMATION_POUNDCHEST);
        }
        if (!this.m_9236_().isClientSide && this.m_5448_() != null && this.getAnimation() == ANIMATION_ATTACK && this.getAnimationTick() == 10) {
            float f1 = this.m_146908_() * (float) (Math.PI / 180.0);
            this.m_20256_(this.m_20184_().add((double) (-Mth.sin(f1) * 0.02F), 0.0, (double) (Mth.cos(f1) * 0.02F)));
            this.m_5448_().knockback(1.0, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
            this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
        }
        if (!this.hasSilverbackAttributes && this.isSilverback() && !this.m_6162_()) {
            this.hasSilverbackAttributes = true;
            this.m_6210_();
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(50.0);
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(10.0);
            this.m_5634_(50.0F);
        }
        if (this.hasSilverbackAttributes && !this.isSilverback() && !this.m_6162_()) {
            this.hasSilverbackAttributes = false;
            this.m_6210_();
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(30.0);
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(8.0);
            this.m_5634_(30.0F);
        }
        if (this.poundChestCooldown > 0) {
            this.poundChestCooldown--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    public PathNavigation getNavigation() {
        return this.f_21344_;
    }

    @Nullable
    @Override
    public Entity getControlledVehicle() {
        return this.m_20202_() instanceof EntityGorilla ? null : super.m_275832_();
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
    public boolean canTargetItem(ItemStack stack) {
        return stack.is(AMTagRegistry.GORILLA_FOODSTUFFS);
    }

    @Override
    public void onGetItem(ItemEntity targetEntity) {
        ItemStack duplicate = targetEntity.getItem().copy();
        duplicate.setCount(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.m_9236_().isClientSide) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
        }
        this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
        Entity thrower = targetEntity.getOwner();
        if (isBanana(targetEntity.getItem()) && thrower != null && !this.m_21824_()) {
            this.bananaThrowerID = thrower.getUUID();
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_BREAKBLOCK_R, ANIMATION_BREAKBLOCK_L, ANIMATION_POUNDCHEST, ANIMATION_ATTACK };
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return AMEntityRegistry.GORILLA.get().create(p_241840_1_);
    }

    public void leaveCaravan() {
        if (this.caravanHead != null) {
            this.caravanHead.caravanTail = null;
        }
        this.caravanHead = null;
    }

    public void joinCaravan(EntityGorilla caravanHeadIn) {
        this.caravanHead = caravanHeadIn;
        this.caravanHead.caravanTail = this;
    }

    public boolean hasCaravanTrail() {
        return this.caravanTail != null;
    }

    public boolean inCaravan() {
        return this.caravanHead != null;
    }

    @Nullable
    public EntityGorilla getCaravanHead() {
        return this.caravanHead;
    }

    public float getGorillaScale() {
        return this.m_6162_() ? 0.5F : (this.isSilverback() ? 1.3F : 1.0F);
    }

    public boolean isDonkeyKong() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && (s.toLowerCase().contains("donkey") && s.toLowerCase().contains("kong") || s.equalsIgnoreCase("dk"));
    }

    public boolean isFunkyKong() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("funky") && s.toLowerCase().contains("kong");
    }

    private class AIWalkIdle extends RandomStrollGoal {

        public AIWalkIdle(EntityGorilla entityGorilla, double v) {
            super(entityGorilla, v);
        }

        @Override
        public boolean canUse() {
            this.f_25730_ = EntityGorilla.this.isSilverback() ? 10 : 120;
            return super.canUse();
        }

        @Nullable
        @Override
        protected Vec3 getPosition() {
            return LandRandomPos.getPos(this.f_25725_, EntityGorilla.this.isSilverback() ? 25 : 10, 7);
        }
    }
}