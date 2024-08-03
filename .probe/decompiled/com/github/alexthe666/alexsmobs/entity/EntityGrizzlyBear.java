package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.GrizzlyBearAIAprilFools;
import com.github.alexthe666.alexsmobs.entity.ai.GrizzlyBearAIBeehive;
import com.github.alexthe666.alexsmobs.entity.ai.GrizzlyBearAIFleeBees;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIFollowOwner;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAITempt;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityGrizzlyBear extends TamableAnimal implements NeutralMob, IAnimatedEntity, ITargetsDroppedItems, IFollower {

    public static final Animation ANIMATION_MAUL = Animation.create(20);

    public static final Animation ANIMATION_SNIFF = Animation.create(12);

    public static final Animation ANIMATION_SWIPE_R = Animation.create(15);

    public static final Animation ANIMATION_SWIPE_L = Animation.create(20);

    private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.defineId(EntityGrizzlyBear.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityGrizzlyBear.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HONEYED = SynchedEntityData.defineId(EntityGrizzlyBear.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(EntityGrizzlyBear.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SNOWY = SynchedEntityData.defineId(EntityGrizzlyBear.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> APRIL_FOOLS_MODE = SynchedEntityData.defineId(EntityGrizzlyBear.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityGrizzlyBear.class, EntityDataSerializers.INT);

    private static final UniformInt angerLogic = TimeUtil.rangeOfSeconds(20, 39);

    public float prevStandProgress;

    public float prevSitProgress;

    public float standProgress;

    public float sitProgress;

    public int maxStandTime = 75;

    public boolean forcedSit = false;

    private int animationTick;

    private Animation currentAnimation;

    private int standingTime = 0;

    private int sittingTime = 0;

    private int maxSitTime = 75;

    private int eatingTime = 0;

    private int angerTime;

    private UUID angerTarget;

    private int honeyedTime;

    @Nullable
    private UUID salmonThrowerID = null;

    private static final Ingredient TEMPTATION_ITEMS = Ingredient.of(Items.SALMON, Items.HONEYCOMB, Items.HONEY_BOTTLE);

    public int timeUntilNextFur = this.f_19796_.nextInt(24000) + 24000;

    protected static final EntityDimensions STANDING_SIZE = EntityDimensions.scalable(1.7F, 2.75F);

    private boolean recalcSize = false;

    private int snowTimer = 0;

    private boolean permSnow = false;

    protected EntityGrizzlyBear(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 55.0).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isStanding() ? STANDING_SIZE.scale(this.m_6134_()) : super.m_6972_(poseIn);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.grizzlyBearSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            this.setOrderedToSit(false);
            if (entity != null && this.m_21824_() && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
                amount = (amount + 1.0F) / 3.0F;
            }
            return super.m_6469_(source, amount);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.GRIZZLY_BEAR_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.GRIZZLY_BEAR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.GRIZZLY_BEAR_DIE.get();
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            float sitAdd = -0.065F * this.sitProgress;
            float standAdd = -0.07F * this.standProgress;
            float radius = standAdd + sitAdd;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), this.m_20189_() + extraZ);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        float f = Math.min(0.25F, this.f_267362_.speed());
        float f1 = this.f_267362_.position();
        float sitAdd = 0.01F * this.sitProgress;
        float standAdd = 0.07F * this.standProgress;
        return (double) this.m_20206_() - 0.3 + (double) (0.12F * Mth.cos(f1 * 0.7F) * 0.7F * f) + (double) sitAdd + (double) standAdd;
    }

    @Override
    public void playAmbientSound() {
        if (!this.isFreddy()) {
            super.m_8032_();
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return this.m_20160_() ? 0.9F : 0.98F;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(angerLogic.sample(this.f_19796_));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.angerTime = time;
    }

    @Override
    public UUID getPersistentAngerTarget() {
        return this.angerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.angerTarget = target;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.getMsgId() != null && source.getMsgId().equals("sting") || source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new TameableAIFollowOwner(this, 1.2, 5.0F, 2.0F, false));
        this.f_21345_.addGoal(3, new GrizzlyBearAIAprilFools(this));
        this.f_21345_.addGoal(4, new EntityGrizzlyBear.MeleeAttackGoal());
        this.f_21345_.addGoal(4, new EntityGrizzlyBear.PanicGoal());
        this.f_21345_.addGoal(5, new TameableAITempt(this, 1.1, TEMPTATION_ITEMS, false));
        this.f_21345_.addGoal(5, new FollowParentGoal(this, 1.25));
        this.f_21345_.addGoal(5, new GrizzlyBearAIBeehive(this));
        this.f_21345_.addGoal(6, new GrizzlyBearAIFleeBees(this, 14.0F, 1.0, 1.0));
        this.f_21345_.addGoal(6, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(7, new RandomStrollGoal(this, 0.75));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new EntityGrizzlyBear.HurtByTargetGoal());
        this.f_21346_.addGoal(4, new CreatureAITargetItems(this, false));
        this.f_21346_.addGoal(5, new EntityGrizzlyBear.AttackPlayerGoal());
        this.f_21346_.addGoal(6, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::m_21674_));
        this.f_21346_.addGoal(7, new NonTameRandomTargetGoal(this, Fox.class, false, (Predicate<LivingEntity>) null));
        this.f_21346_.addGoal(8, new NonTameRandomTargetGoal(this, Wolf.class, false, (Predicate<LivingEntity>) null));
        this.f_21346_.addGoal(7, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Honeyed", this.isHoneyed());
        compound.putBoolean("Snowy", this.isSnowy());
        compound.putBoolean("Standing", this.isStanding());
        compound.putBoolean("BearSitting", this.isSitting());
        compound.putBoolean("ForcedToSit", this.forcedSit);
        compound.putBoolean("SnowPerm", this.permSnow);
        compound.putInt("FurTime", this.timeUntilNextFur);
        compound.putInt("BearCommand", this.getCommand());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setHoneyed(compound.getBoolean("Honeyed"));
        this.setSnowy(compound.getBoolean("Snowy"));
        this.setStanding(compound.getBoolean("Standing"));
        this.setOrderedToSit(compound.getBoolean("BearSitting"));
        this.setCommand(compound.getInt("BearCommand"));
        this.forcedSit = compound.getBoolean("ForcedToSit");
        this.permSnow = compound.getBoolean("SnowPerm");
        this.timeUntilNextFur = compound.getInt("FurTime");
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return this.m_21824_() && item == Items.SALMON;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 67) {
            AlexsMobs.PROXY.onEntityStatus(this, id);
        } else if (id == 68) {
            AlexsMobs.PROXY.spawnSpecialParticle(0);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        for (Entity passenger : this.m_20197_()) {
            if (passenger instanceof Player) {
                return (Player) passenger;
            }
        }
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        if (item == Items.SNOW && !this.isSnowy() && !this.m_9236_().isClientSide) {
            this.m_142075_(player, hand, itemstack);
            this.permSnow = true;
            this.setSnowy(true);
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(SoundEvents.SNOW_PLACE, this.m_6121_(), this.m_6100_());
            return InteractionResult.SUCCESS;
        } else if (item instanceof ShovelItem && this.isSnowy() && !this.m_9236_().isClientSide) {
            this.permSnow = false;
            if (!player.isCreative()) {
                itemstack.hurt(1, this.m_217043_(), player instanceof ServerPlayer ? (ServerPlayer) player : null);
            }
            this.setSnowy(false);
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(SoundEvents.SNOW_BREAK, this.m_6121_(), this.m_6100_());
            return InteractionResult.SUCCESS;
        } else {
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            if (interactionresult == InteractionResult.SUCCESS || type == InteractionResult.SUCCESS || !this.m_21824_() || !this.m_21830_(player) || this.isFood(itemstack)) {
                return type;
            } else if (!player.m_6144_() && !this.m_6162_()) {
                player.m_20329_(this);
                return InteractionResult.SUCCESS;
            } else {
                this.setCommand((this.getCommand() + 1) % 3);
                if (this.getCommand() == 3) {
                    this.setCommand(0);
                }
                player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
                boolean sit = this.getCommand() == 2;
                if (sit) {
                    this.forcedSit = true;
                    this.setOrderedToSit(true);
                    return InteractionResult.SUCCESS;
                } else {
                    this.forcedSit = false;
                    this.setOrderedToSit(false);
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        if (player.f_20902_ != 0.0F) {
            float f = player.f_20902_ < 0.0F ? 0.5F : 1.0F;
            return new Vec3((double) (player.f_20900_ * 0.25F), 0.0, (double) (player.f_20902_ * 0.5F * f));
        } else {
            this.m_6858_(false);
            return Vec3.ZERO;
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        if (player.f_20902_ != 0.0F || player.f_20900_ != 0.0F) {
            this.m_19915_(player.m_146908_(), player.m_146909_() * 0.25F);
            this.f_19859_ = this.f_20883_ = this.f_20885_ = this.m_146908_();
            this.m_274367_(1.0F);
            this.m_21573_().stop();
            this.m_6710_(null);
            this.m_6858_(true);
        }
    }

    @Override
    protected float getRiddenSpeed(Player rider) {
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
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
    public void tick() {
        super.m_8119_();
        if (this.m_6162_() || this.m_20192_() > this.m_20206_()) {
            this.m_6210_();
        }
        if (!this.isStanding() && this.m_20206_() >= 2.75F) {
            this.m_6210_();
        }
        this.prevStandProgress = this.standProgress;
        this.prevSitProgress = this.sitProgress;
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
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && this.canTargetItem(this.m_21120_(InteractionHand.MAIN_HAND))) {
            this.setEating(true);
            this.setOrderedToSit(true);
            this.setStanding(false);
        }
        if (this.recalcSize) {
            this.recalcSize = false;
            this.m_6210_();
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
            for (int i = 0; i < 3; i++) {
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_21120_(InteractionHand.MAIN_HAND)), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
            }
            if (this.eatingTime % 5 == 0) {
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
            }
            if (this.eatingTime > 100) {
                ItemStack stack = this.m_21120_(InteractionHand.MAIN_HAND);
                if (!stack.isEmpty()) {
                    if (stack.is(AMTagRegistry.GRIZZLY_HONEY)) {
                        this.setHoneyed(true);
                        this.m_5634_(10.0F);
                        this.honeyedTime = 700;
                    } else {
                        this.m_5634_(4.0F);
                    }
                    if (stack.getItem() == Items.SALMON && !this.m_21824_() && this.salmonThrowerID != null) {
                        if (this.m_217043_().nextFloat() < 0.3F) {
                            this.m_7105_(true);
                            this.m_21816_(this.salmonThrowerID);
                            Player player = this.m_9236_().m_46003_(this.salmonThrowerID);
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
        if (this.isStanding() && ++this.standingTime > this.maxStandTime) {
            this.setStanding(false);
            this.standingTime = 0;
            this.maxStandTime = 75 + this.f_19796_.nextInt(50);
        }
        if (this.isSitting() && !this.forcedSit && ++this.sittingTime > this.maxSitTime) {
            this.setOrderedToSit(false);
            this.sittingTime = 0;
            this.maxSitTime = 75 + this.f_19796_.nextInt(50);
        }
        if (!this.m_9236_().isClientSide && this.getAnimation() == NO_ANIMATION && !this.isStanding() && !this.isSitting() && this.f_19796_.nextInt(1500) == 0) {
            this.maxSitTime = 300 + this.f_19796_.nextInt(250);
            this.setOrderedToSit(true);
        }
        if (!this.forcedSit && this.isSitting() && (this.m_5448_() != null || this.isStanding()) && !this.isEating()) {
            this.setOrderedToSit(false);
        }
        if (this.getAnimation() == NO_ANIMATION && this.getAprilFoolsFlag() < 1 && this.f_19796_.nextInt(this.isStanding() ? 350 : 2500) == 0) {
            this.setAnimation(ANIMATION_SNIFF);
        }
        if (this.isSitting()) {
            this.m_21573_().stop();
        }
        LivingEntity attackTarget = this.m_5448_();
        if (this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {
            Player rider = (Player) this.getControllingPassenger();
            if (rider.m_21214_() != null && this.m_20270_(rider.m_21214_()) < this.m_20205_() + 3.0F && !this.isAlliedTo(rider.m_21214_())) {
                UUID preyUUID = rider.m_21214_().m_20148_();
                if (!this.m_20148_().equals(preyUUID)) {
                    attackTarget = rider.m_21214_();
                    if (this.getAnimation() == NO_ANIMATION || this.getAnimation() == ANIMATION_SNIFF) {
                        this.setAnimation(this.f_19796_.nextBoolean() ? ANIMATION_MAUL : (this.f_19796_.nextBoolean() ? ANIMATION_SWIPE_L : ANIMATION_SWIPE_R));
                    }
                }
            }
        }
        if (attackTarget != null) {
            if (!this.m_9236_().isClientSide) {
                this.m_6858_(true);
            }
            if (this.m_20270_(attackTarget) < attackTarget.m_20205_() + this.m_20205_() + 2.5F) {
                if (this.getAnimation() == ANIMATION_MAUL && this.getAnimationTick() % 5 == 0 && this.getAnimationTick() > 3) {
                    this.m_7327_(attackTarget);
                }
                if (this.getAnimation() == ANIMATION_SWIPE_L && this.getAnimationTick() == 7) {
                    this.m_7327_(attackTarget);
                    float rot = this.m_146908_() + 90.0F;
                    attackTarget.knockback(0.5, (double) Mth.sin(rot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(rot * (float) (Math.PI / 180.0))));
                }
                if (this.getAnimation() == ANIMATION_SWIPE_R && this.getAnimationTick() == 7) {
                    this.m_7327_(attackTarget);
                    float rot = this.m_146908_() - 90.0F;
                    attackTarget.knockback(0.5, (double) Mth.sin(rot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(rot * (float) (Math.PI / 180.0))));
                }
            }
        } else if (!this.m_9236_().isClientSide && this.getControllingPassenger() == null) {
            this.m_6858_(false);
        }
        if (!this.m_9236_().isClientSide && this.isHoneyed() && --this.honeyedTime <= 0) {
            this.setHoneyed(false);
            this.honeyedTime = 0;
        }
        if (this.forcedSit && !this.m_20160_() && this.m_21824_()) {
            this.setOrderedToSit(true);
        }
        if (this.m_20160_() && this.isSitting()) {
            this.setOrderedToSit(false);
        }
        if (!this.m_9236_().isClientSide && this.m_6084_() && this.m_21824_() && !this.m_6162_() && --this.timeUntilNextFur <= 0) {
            this.m_19998_(AMItemRegistry.BEAR_FUR.get());
            this.timeUntilNextFur = this.f_19796_.nextInt(24000) + 24000;
        }
        if (this.snowTimer > 0) {
            this.snowTimer--;
        }
        if (this.snowTimer == 0 && !this.m_9236_().isClientSide) {
            this.snowTimer = 200 + this.f_19796_.nextInt(400);
            if (this.isSnowy()) {
                if (!this.permSnow && (!this.m_9236_().isClientSide || this.m_20094_() > 0 || this.m_20072_() || !isSnowingAt(this.m_9236_(), this.m_20183_().above()))) {
                    this.setSnowy(false);
                }
            } else if (!this.m_9236_().isClientSide && isSnowingAt(this.m_9236_(), this.m_20183_())) {
                this.setSnowy(true);
            }
        }
        if (this.isFreddy()) {
            this.setStanding(true);
            this.standingTime = 0;
            this.maxStandTime = 40;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public static boolean isSnowingAt(Level world, BlockPos position) {
        if (!world.isRaining()) {
            return false;
        } else if (!world.m_45527_(position)) {
            return false;
        } else {
            return world.m_5452_(Heightmap.Types.MOTION_BLOCKING, position).m_123342_() > position.m_123342_() ? false : ((Biome) world.m_204166_(position).value()).getPrecipitationAt(position) == Biome.Precipitation.SNOW;
        }
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (this.m_21824_()) {
            LivingEntity livingentity = this.m_269323_();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.m_7307_(entityIn);
            }
        }
        return super.isAlliedTo(entityIn);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(STANDING, false);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(HONEYED, false);
        this.f_19804_.define(SNOWY, false);
        this.f_19804_.define(EATING, false);
        this.f_19804_.define(APRIL_FOOLS_MODE, 0);
        this.f_19804_.define(COMMAND, 0);
    }

    public boolean isEating() {
        return this.f_19804_.get(EATING);
    }

    public void setEating(boolean eating) {
        this.f_19804_.set(EATING, eating);
    }

    public boolean isHoneyed() {
        return this.f_19804_.get(HONEYED);
    }

    public void setHoneyed(boolean honeyed) {
        this.f_19804_.set(HONEYED, honeyed);
    }

    public boolean isSnowy() {
        return this.f_19804_.get(SNOWY);
    }

    public void setSnowy(boolean honeyed) {
        this.f_19804_.set(SNOWY, honeyed);
    }

    public boolean isStanding() {
        return this.f_19804_.get(STANDING);
    }

    public void setStanding(boolean standing) {
        this.f_19804_.set(STANDING, standing);
        this.recalcSize = true;
    }

    public int getAprilFoolsFlag() {
        return this.f_19804_.get(APRIL_FOOLS_MODE);
    }

    public void setAprilFoolsFlag(int i) {
        this.f_19804_.set(APRIL_FOOLS_MODE, i);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob p_241840_2_) {
        return AMEntityRegistry.GRIZZLY_BEAR.get().create(world);
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
        if (animation == ANIMATION_MAUL) {
            this.maxStandTime = 21;
            this.setStanding(true);
        }
        if (animation == ANIMATION_SWIPE_R || animation == ANIMATION_SWIPE_L) {
            this.maxStandTime = 2 + this.f_19796_.nextInt(5);
            this.setStanding(true);
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_MAUL, ANIMATION_SNIFF, ANIMATION_SWIPE_R, ANIMATION_SWIPE_L };
    }

    public boolean shouldMove() {
        return !this.isSitting();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMob.AgeableMobGroupData(1.0F);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.is(AMTagRegistry.GRIZZLY_FOODSTUFFS);
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
        if (targetEntity.getItem().getItem() == Items.SALMON && thrower != null && this.isHoneyed()) {
            this.salmonThrowerID = thrower.getUUID();
        } else {
            this.salmonThrowerID = null;
        }
    }

    public boolean isEatingHeldItem() {
        return false;
    }

    public boolean isFreddy() {
        return this.getAprilFoolsFlag() > 1;
    }

    @Override
    public boolean shouldFollow() {
        return this.getAprilFoolsFlag() == 0 && this.getCommand() == 1;
    }

    class AttackPlayerGoal extends NearestAttackableTargetGoal<Player> {

        public AttackPlayerGoal() {
            super(EntityGrizzlyBear.this, Player.class, 3, true, true, null);
        }

        @Override
        public boolean canUse() {
            return !EntityGrizzlyBear.this.m_6162_() && EntityGrizzlyBear.this.getAprilFoolsFlag() < 1 && !EntityGrizzlyBear.this.isHoneyed() ? super.canUse() : false;
        }

        @Override
        protected double getFollowDistance() {
            return 5.0;
        }
    }

    class HurtByTargetGoal extends net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal {

        public HurtByTargetGoal() {
            super(EntityGrizzlyBear.this);
        }

        @Override
        public void start() {
            super.start();
            if (EntityGrizzlyBear.this.m_6162_()) {
                this.m_26047_();
                this.m_8041_();
            }
        }

        @Override
        protected void alertOther(Mob mobIn, LivingEntity targetIn) {
            if (mobIn instanceof EntityGrizzlyBear && !mobIn.m_6162_()) {
                super.alertOther(mobIn, targetIn);
            }
        }
    }

    class MeleeAttackGoal extends net.minecraft.world.entity.ai.goal.MeleeAttackGoal {

        public MeleeAttackGoal() {
            super(EntityGrizzlyBear.this, 1.25, true);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            double d0 = this.getAttackReachSqr(enemy);
            if (distToEnemySqr <= d0 && (EntityGrizzlyBear.this.getAnimation() == IAnimatedEntity.NO_ANIMATION || EntityGrizzlyBear.this.getAnimation() == EntityGrizzlyBear.ANIMATION_SNIFF)) {
                EntityGrizzlyBear.this.setAnimation(EntityGrizzlyBear.this.f_19796_.nextBoolean() ? EntityGrizzlyBear.ANIMATION_MAUL : (EntityGrizzlyBear.this.f_19796_.nextBoolean() ? EntityGrizzlyBear.ANIMATION_SWIPE_L : EntityGrizzlyBear.ANIMATION_SWIPE_R));
            }
        }

        @Override
        public void stop() {
            EntityGrizzlyBear.this.setStanding(false);
            super.stop();
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double) (3.0F + attackTarget.m_20205_());
        }
    }

    class PanicGoal extends net.minecraft.world.entity.ai.goal.PanicGoal {

        public PanicGoal() {
            super(EntityGrizzlyBear.this, 2.0);
        }

        @Override
        public boolean canUse() {
            return (EntityGrizzlyBear.this.m_6162_() || EntityGrizzlyBear.this.m_6060_()) && super.canUse();
        }
    }
}