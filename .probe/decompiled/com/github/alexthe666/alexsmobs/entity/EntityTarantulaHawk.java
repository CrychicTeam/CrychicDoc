package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.FlyingAIFollowOwner;
import com.github.alexthe666.alexsmobs.message.MessageTarantulaHawkSting;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
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
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityTarantulaHawk extends TamableAnimal implements IFollower {

    public static final int STING_DURATION = 2400;

    protected static final EntityDimensions FLIGHT_SIZE = EntityDimensions.fixed(0.9F, 1.5F);

    private static final EntityDataAccessor<Float> FLY_ANGLE = SynchedEntityData.defineId(EntityTarantulaHawk.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> NETHER = SynchedEntityData.defineId(EntityTarantulaHawk.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityTarantulaHawk.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DRAGGING = SynchedEntityData.defineId(EntityTarantulaHawk.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityTarantulaHawk.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DIGGING = SynchedEntityData.defineId(EntityTarantulaHawk.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SCARED = SynchedEntityData.defineId(EntityTarantulaHawk.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(EntityTarantulaHawk.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityTarantulaHawk.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(EntityTarantulaHawk.class, EntityDataSerializers.BOOLEAN);

    public float prevFlyAngle;

    public float prevSitProgress;

    public float sitProgress;

    public float prevDragProgress;

    public float dragProgress;

    public float prevFlyProgress;

    public float flyProgress;

    public float prevAttackProgress;

    public float attackProgress;

    public float prevDigProgress;

    public float digProgress;

    private boolean isLandNavigator;

    private boolean flightSize = false;

    private int timeFlying = 0;

    private boolean bredBuryFlag = false;

    private int spiderFeedings = 0;

    private int dragTime = 0;

    protected EntityTarantulaHawk(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.switchNavigator(false);
    }

    public static boolean canTarantulaHawkSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_60713_(Blocks.SAND);
        return spawnBlock && worldIn.m_45524_(pos, 0) > 8 || isBiomeNether(worldIn, pos) || AMConfig.fireproofTarantulaHawk;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 18.0).add(Attributes.ARMOR, 4.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.tarantulaHawkSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (isBiomeNether(worldIn, this.m_20183_())) {
            this.setNether(true);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private static boolean isBiomeNether(LevelAccessor worldIn, BlockPos position) {
        return worldIn.m_204166_(position).is(AMTagRegistry.SPAWNS_NETHER_TARANTULA_HAWKS);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new FlyingAIFollowOwner(this, 1.0, 10.0F, 2.0F, false));
        this.f_21345_.addGoal(3, new EntityTarantulaHawk.AIFleeRoadrunners());
        this.f_21345_.addGoal(4, new EntityTarantulaHawk.AIMelee());
        this.f_21345_.addGoal(5, new EntityTarantulaHawk.AIBury());
        this.f_21345_.addGoal(6, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(7, new TemptGoal(this, 1.1, Ingredient.of(Items.SPIDER_EYE, Items.FERMENTED_SPIDER_EYE), false));
        this.f_21345_.addGoal(8, new EntityTarantulaHawk.AIWalkIdle());
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new AnimalAIHurtByTargetNotBaby(this));
        this.f_21346_.addGoal(4, new EntityAINearestTarget3D(this, Spider.class, 15, true, true, null) {

            @Override
            public boolean canUse() {
                return super.m_8036_() && !EntityTarantulaHawk.this.m_6162_() && !EntityTarantulaHawk.this.isSitting();
            }
        });
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.TARANTULA_HAWK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.TARANTULA_HAWK_HURT.get();
    }

    @Override
    public boolean fireImmune() {
        return this.isNether() || AMConfig.fireproofTarantulaHawk;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new EntityTarantulaHawk.MoveController();
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(FLY_ANGLE, 0.0F);
        this.f_19804_.define(NETHER, false);
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(DRAGGING, false);
        this.f_19804_.define(DIGGING, false);
        this.f_19804_.define(SCARED, false);
        this.f_19804_.define(ANGRY, false);
        this.f_19804_.define(ATTACK_TICK, 0);
        this.f_19804_.define(COMMAND, 0);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return source.getEntity() instanceof LivingEntity && ((LivingEntity) source.getEntity()).getMobType() == MobType.ARTHROPOD && ((LivingEntity) source.getEntity()).hasEffect(AMEffectRegistry.DEBILITATING_STING.get()) ? false : super.m_6469_(source, amount);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("HawkSitting", this.isSitting());
        compound.putBoolean("Nether", this.isNether());
        compound.putBoolean("Digging", this.isDigging());
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("Command", this.getCommand());
        compound.putInt("SpiderFeedings", this.spiderFeedings);
        compound.putBoolean("BreedFlag", this.bredBuryFlag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOrderedToSit(compound.getBoolean("HawkSitting"));
        this.setNether(compound.getBoolean("Nether"));
        this.setDigging(compound.getBoolean("Digging"));
        this.setFlying(compound.getBoolean("Flying"));
        this.setCommand(compound.getInt("Command"));
        this.spiderFeedings = compound.getInt("SpiderFeedings");
        this.bredBuryFlag = compound.getBoolean("BreedFlag");
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

    public float getFlyAngle() {
        return this.f_19804_.get(FLY_ANGLE);
    }

    public void setFlyAngle(float progress) {
        this.f_19804_.set(FLY_ANGLE, progress);
    }

    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        if (!flying || !this.m_6162_()) {
            this.f_19804_.set(FLYING, flying);
        }
    }

    public boolean isNether() {
        return this.f_19804_.get(NETHER);
    }

    public void setNether(boolean sit) {
        this.f_19804_.set(NETHER, sit);
    }

    public boolean isScared() {
        return this.f_19804_.get(SCARED);
    }

    public void setScared(boolean sit) {
        this.f_19804_.set(SCARED, sit);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public boolean isDragging() {
        return this.f_19804_.get(DRAGGING);
    }

    public void setDragging(boolean sit) {
        this.f_19804_.set(DRAGGING, sit);
    }

    public boolean isDigging() {
        return this.f_19804_.get(DIGGING);
    }

    public void setDigging(boolean sit) {
        this.f_19804_.set(DIGGING, sit);
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isFlying() && !this.m_6162_() ? FLIGHT_SIZE : super.m_6972_(poseIn);
    }

    @Override
    public void tick() {
        this.prevFlyAngle = this.getFlyAngle();
        super.m_8119_();
        this.prevAttackProgress = this.attackProgress;
        this.prevFlyProgress = this.flyProgress;
        this.prevSitProgress = this.sitProgress;
        this.prevDragProgress = this.dragProgress;
        this.prevDigProgress = this.digProgress;
        boolean flying = this.isFlying();
        boolean sitting = this.isSitting();
        boolean dragging = this.isDragging();
        boolean digging = this.isDigging();
        if (flying) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (sitting) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (dragging) {
            if (this.dragProgress < 5.0F) {
                this.dragProgress++;
            }
        } else if (this.dragProgress > 0.0F) {
            this.dragProgress--;
        }
        if (digging) {
            if (this.digProgress < 5.0F) {
                this.digProgress++;
            }
        } else if (this.digProgress > 0.0F) {
            this.digProgress--;
        }
        if (this.flightSize && !flying) {
            this.m_6210_();
            this.flightSize = false;
        }
        if (!this.flightSize && this.isFlying()) {
            this.m_6210_();
            this.flightSize = true;
        }
        float threshold = 0.015F;
        if (this.isFlying() && this.f_19859_ - this.m_146908_() > threshold) {
            this.setFlyAngle(this.getFlyAngle() + 5.0F);
        } else if (this.isFlying() && this.f_19859_ - this.m_146908_() < -threshold) {
            this.setFlyAngle(this.getFlyAngle() - 5.0F);
        } else if (this.getFlyAngle() > 0.0F) {
            this.setFlyAngle(Math.max(this.getFlyAngle() - 4.0F, 0.0F));
        } else if (this.getFlyAngle() < 0.0F) {
            this.setFlyAngle(Math.min(this.getFlyAngle() + 4.0F, 0.0F));
        }
        this.setFlyAngle(Mth.clamp(this.getFlyAngle(), -30.0F, 30.0F));
        if (!this.m_9236_().isClientSide) {
            if (this.isFlying() && this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (!this.isFlying() && !this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.isFlying()) {
                if (this.timeFlying % 25 == 0) {
                    this.m_5496_(AMSoundRegistry.TARANTULA_HAWK_WING.get(), this.m_6121_(), this.m_6100_());
                }
                this.timeFlying++;
                this.m_20242_(true);
                if (this.isSitting() || this.m_20159_() || this.m_27593_()) {
                    this.setFlying(false);
                }
            } else {
                this.timeFlying = 0;
                this.m_20242_(false);
            }
            if (this.m_5448_() != null && this.m_5448_() instanceof Player && !this.m_21824_()) {
                this.f_19804_.set(ANGRY, true);
            } else {
                this.f_19804_.set(ANGRY, false);
            }
        }
        if (this.f_19804_.get(ATTACK_TICK) > 0) {
            this.f_19804_.set(ATTACK_TICK, this.f_19804_.get(ATTACK_TICK) - 1);
            if (this.attackProgress < 5.0F) {
                this.attackProgress++;
            }
        } else if (this.attackProgress > 0.0F) {
            this.attackProgress--;
        }
        if (this.isDigging() && this.m_9236_().getBlockState(this.m_20099_()).m_60815_()) {
            BlockPos posit = this.m_20099_();
            BlockState understate = this.m_9236_().getBlockState(posit);
            for (int i = 0; i < 4 + this.f_19796_.nextInt(2); i++) {
                double particleX = (double) ((float) posit.m_123341_() + this.f_19796_.nextFloat());
                double particleY = (double) ((float) posit.m_123342_() + 1.0F);
                double particleZ = (double) ((float) posit.m_123343_() + this.f_19796_.nextFloat());
                double motX = this.f_19796_.nextGaussian() * 0.02;
                double motY = (double) (0.1F + this.f_19796_.nextFloat() * 0.2F);
                double motZ = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, understate), particleX, particleY, particleZ, motX, motY, motZ);
            }
        }
        if (this.f_19797_ > 0 && this.f_19797_ % 300 == 0 && this.m_21223_() < this.m_21233_()) {
            this.m_5634_(1.0F);
        }
        if (!this.m_9236_().isClientSide && this.isDragging() && this.m_20197_().isEmpty() && !this.isDigging()) {
            this.dragTime++;
            if (this.dragTime > 5000) {
                this.dragTime = 0;
                for (Entity e : this.m_20197_()) {
                    e.hurt(this.m_269291_().mobAttack(this), 10.0F);
                }
                this.m_20153_();
                this.setDragging(false);
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        if (!this.m_21824_() && item == Items.SPIDER_EYE) {
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.STRIDER_EAT, this.m_6121_(), this.m_6100_());
            this.spiderFeedings++;
            if ((this.spiderFeedings < 15 || this.m_217043_().nextInt(6) != 0) && this.spiderFeedings <= 25) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            } else {
                this.m_21828_(player);
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            }
            return InteractionResult.SUCCESS;
        } else if (this.m_21824_() && itemstack.is(ItemTags.FLOWERS)) {
            if (this.m_21223_() < this.m_21233_()) {
                this.m_142075_(player, hand, itemstack);
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.STRIDER_EAT, this.m_6121_(), this.m_6100_());
                this.m_5634_(5.0F);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else {
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21824_() && this.m_21830_(player)) {
                if (player.m_6144_()) {
                    if (this.m_21205_().isEmpty()) {
                        ItemStack cop = itemstack.copy();
                        cop.setCount(1);
                        this.m_21008_(InteractionHand.MAIN_HAND, cop);
                        itemstack.shrink(1);
                        return InteractionResult.SUCCESS;
                    }
                    this.m_19983_(this.m_21205_().copy());
                    this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
                if (!this.isFood(itemstack)) {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() == 3) {
                        this.setCommand(0);
                    }
                    player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
                    boolean sit = this.getCommand() == 2;
                    if (sit) {
                        this.setOrderedToSit(true);
                        return InteractionResult.SUCCESS;
                    }
                    this.setOrderedToSit(false);
                    return InteractionResult.SUCCESS;
                }
            }
            return type;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return this.m_21824_() && item == Items.FERMENTED_SPIDER_EYE;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return null;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.CACTUS) || super.m_6673_(source);
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal animalEntity) {
        this.bredBuryFlag = true;
        ServerPlayer serverplayerentity = this.m_27592_();
        if (serverplayerentity == null && animalEntity.getLoveCause() != null) {
            serverplayerentity = animalEntity.getLoveCause();
        }
        if (serverplayerentity != null) {
            serverplayerentity.m_36220_(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this, animalEntity, this);
        }
        this.m_146762_(6000);
        animalEntity.m_146762_(6000);
        this.m_27594_();
        animalEntity.resetLove();
        world.broadcastEntityEvent(this, (byte) 7);
        world.broadcastEntityEvent(this, (byte) 18);
        if (world.m_46469_().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            world.addFreshEntity(new ExperienceOrb(world, this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_217043_().nextInt(7) + 1));
        }
    }

    @Override
    public void followEntity(TamableAnimal tameable, LivingEntity owner, double followSpeed) {
        if (this.m_20270_(owner) > 5.0F) {
            this.setFlying(true);
            this.m_21566_().setWantedPosition(owner.m_20185_(), owner.m_20186_() + (double) owner.m_20206_(), owner.m_20189_(), followSpeed);
        } else {
            if (this.m_20096_()) {
                this.setFlying(false);
            }
            if (this.isFlying() && !this.isOverWater()) {
                BlockPos vec = this.getCrowGround(this.m_20183_());
                if (vec != null) {
                    this.m_21566_().setWantedPosition((double) vec.m_123341_(), (double) vec.m_123342_(), (double) vec.m_123343_(), followSpeed);
                }
            } else {
                this.m_21573_().moveTo(owner, followSpeed);
            }
        }
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        this.m_146926_(0.0F);
        float radius = 1.0F + passenger.getBbWidth() * 0.5F;
        float angle = (float) (Math.PI / 180.0) * (this.f_20883_ - 180.0F);
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        double extraY = 0.0;
        passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + extraY, this.m_20189_() + extraZ);
    }

    private boolean isOverWater() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > 0 && this.m_9236_().m_46859_(position)) {
            position = position.below();
        }
        return !this.m_9236_().getFluidState(position).isEmpty() || position.m_123342_() <= 0;
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = -9.45F - (float) this.m_217043_().nextInt(24) - radiusAdd;
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), 0, (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getCrowGround(radialPos);
        int distFromGround = (int) this.m_20186_() - ground.m_123342_();
        int flightHeight = 4 + this.m_217043_().nextInt(10);
        BlockPos newPos = ground.above(distFromGround > 8 ? flightHeight : this.m_217043_().nextInt(6) + 1);
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.m_20238_(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
    }

    private BlockPos getCrowGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int) this.m_20186_(), in.m_123343_());
        while (position.m_123342_() > -64 && !this.m_9236_().getBlockState(position).m_280296_() && this.m_9236_().getFluidState(position).isEmpty()) {
            position = position.below();
        }
        return position;
    }

    public Vec3 getBlockGrounding(Vec3 fleePos) {
        float radius = -9.45F - (float) this.m_217043_().nextInt(24);
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), (int) this.m_20186_(), (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getCrowGround(radialPos);
        if (ground.m_123342_() == -64) {
            return this.m_20182_();
        } else {
            ground = this.m_20183_();
            while (ground.m_123342_() > -62 && !this.m_9236_().getBlockState(ground).m_280296_()) {
                ground = ground.below();
            }
            return !this.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? Vec3.atCenterOf(ground) : null;
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    private Vec3 getOrbitVec(Vec3 vector3d, float gatheringCircleDist, boolean orbitClockwise) {
        float angle = 0.034906585F * (float) (orbitClockwise ? -this.f_19797_ : this.f_19797_);
        double extraX = (double) (gatheringCircleDist * Mth.sin(angle));
        double extraZ = (double) (gatheringCircleDist * Mth.cos(angle));
        if (vector3d != null) {
            Vec3 pos = new Vec3(vector3d.x() + extraX, vector3d.y() + (double) this.f_19796_.nextInt(2) + 4.0, vector3d.z() + extraZ);
            if (this.m_9236_().m_46859_(AMBlockPos.fromVec3(pos))) {
                return pos;
            }
        }
        return null;
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    private BlockPos genSandPos(BlockPos parent) {
        LevelAccessor world = this.m_9236_();
        Random random = new Random();
        int range = 24;
        for (int i = 0; i < 15; i++) {
            BlockPos sandAir = parent.offset(random.nextInt(range) - range / 2, -5, random.nextInt(range) - range / 2);
            while (!world.m_46859_(sandAir) && sandAir.m_123342_() < 255) {
                sandAir = sandAir.above();
            }
            BlockState state = world.m_8055_(sandAir.below());
            if (state.m_204336_(BlockTags.SAND)) {
                return sandAir.below();
            }
        }
        return null;
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1 && !this.isDragging() && !this.isDigging() && (this.m_5448_() == null || !this.m_5448_().isAlive());
    }

    public boolean isAngry() {
        return this.f_19804_.get(ANGRY);
    }

    private class AIBury extends Goal {

        private final EntityTarantulaHawk hawk;

        private BlockPos buryPos = null;

        private int digTime = 0;

        private double stageX;

        private double stageY;

        private double stageZ;

        private AIBury() {
            this.hawk = EntityTarantulaHawk.this;
        }

        @Override
        public boolean canUse() {
            if (this.hawk.isDragging() && this.hawk.m_5448_() != null) {
                BlockPos pos = this.hawk.genSandPos(this.hawk.m_20183_());
                if (pos != null) {
                    this.buryPos = pos;
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.hawk.isDragging() && this.digTime < 200 && this.hawk.m_5448_() != null && this.buryPos != null && EntityTarantulaHawk.this.m_9236_().getBlockState(this.buryPos).m_204336_(BlockTags.SAND);
        }

        @Override
        public void start() {
            this.digTime = 0;
            this.stageX = this.hawk.m_20185_();
            this.stageY = this.hawk.m_20186_();
            this.stageZ = this.hawk.m_20189_();
        }

        @Override
        public void stop() {
            this.digTime = 0;
            this.hawk.setDigging(false);
            this.hawk.setDragging(false);
            this.hawk.m_6710_(null);
            this.hawk.m_6703_(null);
        }

        @Override
        public void tick() {
            this.hawk.setFlying(false);
            this.hawk.setDragging(true);
            LivingEntity target = this.hawk.m_5448_();
            if (this.hawk.m_20238_(Vec3.atCenterOf(this.buryPos)) < 9.0 && !this.hawk.isDigging()) {
                this.hawk.setDigging(true);
                this.stageX = target.m_20185_();
                this.stageY = target.m_20186_();
                this.stageZ = target.m_20189_();
            }
            if (this.hawk.isDigging()) {
                target.f_19794_ = true;
                this.digTime++;
                this.hawk.m_20153_();
                target.m_6034_(this.stageX, this.stageY - (double) Math.min(3.0F, (float) this.digTime * 0.05F), this.stageZ);
                this.hawk.m_21573_().moveTo(this.stageX, this.stageY, this.stageZ, 0.85F);
            } else {
                this.hawk.m_21573_().moveTo((double) this.buryPos.m_123341_(), (double) this.buryPos.m_123342_(), (double) this.buryPos.m_123343_(), 0.5);
            }
        }
    }

    private class AIFleeRoadrunners extends Goal {

        private int searchCooldown = 0;

        private LivingEntity fear = null;

        private Vec3 fearVec = null;

        @Override
        public boolean canUse() {
            if (this.searchCooldown <= 0) {
                this.searchCooldown = 100 + EntityTarantulaHawk.this.f_19796_.nextInt(100);
                for (EntityRoadrunner roadrunner : EntityTarantulaHawk.this.m_9236_().m_45976_(EntityRoadrunner.class, EntityTarantulaHawk.this.m_20191_().inflate(15.0, 32.0, 15.0))) {
                    if (this.fear == null || EntityTarantulaHawk.this.m_20270_(this.fear) > EntityTarantulaHawk.this.m_20270_(roadrunner)) {
                        this.fear = roadrunner;
                    }
                }
            } else {
                this.searchCooldown--;
            }
            return EntityTarantulaHawk.this.m_6084_() && this.fear != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.fear != null && this.fear.isAlive() && EntityTarantulaHawk.this.m_20270_(this.fear) < 32.0F;
        }

        @Override
        public void start() {
            super.start();
            EntityTarantulaHawk.this.setScared(true);
        }

        @Override
        public void tick() {
            if (this.fear != null) {
                if (this.fearVec == null || EntityTarantulaHawk.this.m_20238_(this.fearVec) < 4.0) {
                    this.fearVec = EntityTarantulaHawk.this.getBlockInViewAway(this.fearVec == null ? this.fear.m_20182_() : this.fearVec, 12.0F);
                }
                if (this.fearVec != null) {
                    EntityTarantulaHawk.this.setFlying(true);
                    EntityTarantulaHawk.this.m_21566_().setWantedPosition(this.fearVec.x, this.fearVec.y, this.fearVec.z, 1.1F);
                }
            }
        }

        @Override
        public void stop() {
            EntityTarantulaHawk.this.setScared(false);
            this.fear = null;
            this.fearVec = null;
        }
    }

    private class AIMelee extends Goal {

        private final EntityTarantulaHawk hawk;

        private int orbitCooldown = 0;

        private boolean clockwise = false;

        private Vec3 orbitVec = null;

        private BlockPos sandPos = null;

        public AIMelee() {
            this.hawk = EntityTarantulaHawk.this;
        }

        @Override
        public boolean canUse() {
            return this.hawk.m_5448_() != null && !this.hawk.isSitting() && !this.hawk.isScared() && this.hawk.m_5448_().isAlive() && !this.hawk.isDragging() && !this.hawk.isDigging() && !this.hawk.m_5448_().f_19794_ && !this.hawk.m_5448_().m_20159_();
        }

        @Override
        public void start() {
            this.hawk.setDragging(false);
            this.clockwise = EntityTarantulaHawk.this.f_19796_.nextBoolean();
        }

        @Override
        public void tick() {
            LivingEntity target = this.hawk.m_5448_();
            boolean paralized = target != null && target.getMobType() == MobType.ARTHROPOD && !target.f_19794_ && target.hasEffect(AMEffectRegistry.DEBILITATING_STING.get());
            boolean paralizedWithChild = paralized && target.getEffect(AMEffectRegistry.DEBILITATING_STING.get()).getAmplifier() > 0;
            if (this.sandPos == null || !EntityTarantulaHawk.this.m_9236_().getBlockState(this.sandPos).m_204336_(BlockTags.SAND)) {
                this.sandPos = this.hawk.genSandPos(target.m_20183_());
            }
            if (this.orbitCooldown > 0) {
                this.orbitCooldown--;
                this.hawk.setFlying(true);
                if (target != null && (this.orbitVec == null || this.hawk.m_20238_(this.orbitVec) < 4.0 || !this.hawk.m_21566_().hasWanted())) {
                    this.orbitVec = this.hawk.getOrbitVec(target.m_20182_().add(0.0, (double) target.m_20206_(), 0.0), (float) (10 + EntityTarantulaHawk.this.f_19796_.nextInt(2)), false);
                    if (this.orbitVec != null) {
                        this.hawk.m_21566_().setWantedPosition(this.orbitVec.x, this.orbitVec.y, this.orbitVec.z, 1.0);
                    }
                }
            } else if ((paralized && !this.hawk.m_21824_() || paralizedWithChild && this.hawk.bredBuryFlag) && this.sandPos != null) {
                if (this.hawk.m_20096_()) {
                    this.hawk.setFlying(false);
                    this.hawk.m_21573_().moveTo(target, 1.0);
                } else {
                    Vec3 vector3d = this.hawk.getBlockGrounding(this.hawk.m_20182_());
                    if (vector3d != null && this.hawk.isFlying()) {
                        this.hawk.m_21566_().setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0);
                    }
                }
                if (this.hawk.m_20270_(target) < target.m_20205_() + 1.5F && !target.m_20159_()) {
                    this.hawk.setDragging(true);
                    this.hawk.setFlying(false);
                    target.m_7998_(this.hawk, true);
                }
            } else if (target != null && !paralizedWithChild) {
                double dist = (double) this.hawk.m_20270_(target);
                if (dist < 10.0 && !this.hawk.isFlying()) {
                    if (this.hawk.m_20096_()) {
                        this.hawk.setFlying(false);
                    }
                    this.hawk.m_21573_().moveTo(target, 1.0);
                } else {
                    this.hawk.setFlying(true);
                    this.hawk.m_21566_().setWantedPosition(target.m_20185_(), target.m_20188_(), target.m_20189_(), 1.0);
                }
                if (dist < (double) (target.m_20205_() + 2.5F)) {
                    if (this.hawk.f_19804_.get(EntityTarantulaHawk.ATTACK_TICK) == 0 && this.hawk.attackProgress == 0.0F) {
                        this.hawk.f_19804_.set(EntityTarantulaHawk.ATTACK_TICK, 7);
                    }
                    if (this.hawk.attackProgress == 5.0F) {
                        this.hawk.m_7327_(target);
                        if (this.hawk.bredBuryFlag && target.getHealth() <= 1.0F) {
                            target.heal(5.0F);
                        }
                        target.addEffect(new MobEffectInstance(AMEffectRegistry.DEBILITATING_STING.get(), target.getMobType() == MobType.ARTHROPOD ? 2400 : 600, this.hawk.bredBuryFlag ? 1 : 0));
                        if (!this.hawk.m_9236_().isClientSide && target.getMobType() == MobType.ARTHROPOD) {
                            AlexsMobs.sendMSGToAll(new MessageTarantulaHawkSting(this.hawk.m_19879_(), target.m_19879_()));
                        }
                        this.orbitCooldown = target.getMobType() == MobType.ARTHROPOD ? 200 + EntityTarantulaHawk.this.f_19796_.nextInt(200) : 10 + EntityTarantulaHawk.this.f_19796_.nextInt(20);
                    }
                }
            }
        }

        @Override
        public void stop() {
            this.orbitCooldown = 0;
            this.hawk.bredBuryFlag = false;
            this.clockwise = EntityTarantulaHawk.this.f_19796_.nextBoolean();
            this.orbitVec = null;
            if (this.hawk.m_20197_().isEmpty()) {
                this.hawk.m_6710_(null);
            }
        }
    }

    private class AIWalkIdle extends Goal {

        protected final EntityTarantulaHawk hawk;

        protected double x;

        protected double y;

        protected double z;

        private boolean flightTarget = false;

        public AIWalkIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.hawk = EntityTarantulaHawk.this;
        }

        @Override
        public boolean canUse() {
            if (this.hawk.m_20160_() || this.hawk.isScared() || this.hawk.isDragging() || EntityTarantulaHawk.this.getCommand() == 1 || this.hawk.m_5448_() != null && this.hawk.m_5448_().isAlive() || this.hawk.m_20159_() || this.hawk.isSitting()) {
                return false;
            } else if (this.hawk.m_217043_().nextInt(30) != 0 && !this.hawk.isFlying()) {
                return false;
            } else {
                if (this.hawk.m_20096_()) {
                    this.flightTarget = EntityTarantulaHawk.this.f_19796_.nextBoolean();
                } else {
                    this.flightTarget = EntityTarantulaHawk.this.f_19796_.nextInt(5) > 0 && this.hawk.timeFlying < 200;
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
                this.hawk.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                this.hawk.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
            if (!this.flightTarget && EntityTarantulaHawk.this.isFlying() && this.hawk.m_20096_()) {
                this.hawk.setFlying(false);
            }
            if (EntityTarantulaHawk.this.isFlying() && this.hawk.m_20096_() && this.hawk.timeFlying > 10) {
                this.hawk.setFlying(false);
            }
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = this.hawk.m_20182_();
            if (this.hawk.isOverWater()) {
                this.flightTarget = true;
            }
            if (this.flightTarget) {
                return this.hawk.timeFlying >= 50 && !this.hawk.isOverWater() ? this.hawk.getBlockGrounding(vector3d) : this.hawk.getBlockInViewAway(vector3d, 0.0F);
            } else {
                return LandRandomPos.getPos(this.hawk, 10, 7);
            }
        }

        @Override
        public boolean canContinueToUse() {
            if (this.hawk.isSitting() || EntityTarantulaHawk.this.getCommand() == 1) {
                return false;
            } else {
                return this.flightTarget ? this.hawk.isFlying() && this.hawk.m_20275_(this.x, this.y, this.z) > 2.0 : !this.hawk.m_21573_().isDone() && !this.hawk.m_20160_();
            }
        }

        @Override
        public void start() {
            if (this.flightTarget) {
                this.hawk.setFlying(true);
                this.hawk.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                this.hawk.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
        }

        @Override
        public void stop() {
            this.hawk.m_21573_().stop();
            super.stop();
        }
    }

    class MoveController extends MoveControl {

        private final Mob parentEntity = EntityTarantulaHawk.this;

        public MoveController() {
            super(EntityTarantulaHawk.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                double width = this.parentEntity.m_20191_().getSize();
                if (d0 < width) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.5));
                } else {
                    float angle = (float) (Math.PI / 180.0) * (this.parentEntity.f_20883_ + 90.0F);
                    float radius = (float) Math.sin((double) ((float) this.parentEntity.f_19797_ * 0.2F)) * 2.0F;
                    double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
                    double extraZ = (double) (radius * Mth.cos(angle));
                    Vec3 vector3d1 = vector3d.scale(this.f_24978_ * 0.05 / d0);
                    Vec3 strafPlus = new Vec3(extraX, 0.0, extraZ).scale(0.003 * Math.min(d0, 100.0));
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(strafPlus));
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d1));
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    if (!EntityTarantulaHawk.this.isDragging()) {
                        this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                    }
                }
            }
        }
    }
}