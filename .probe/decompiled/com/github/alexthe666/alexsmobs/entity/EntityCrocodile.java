package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.block.BlockReptileEgg;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.CrocodileAIMelee;
import com.github.alexthe666.alexsmobs.entity.ai.CrocodileAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityCrocodile extends TamableAnimal implements IAnimatedEntity, ISemiAquatic {

    public static final Animation ANIMATION_LUNGE = Animation.create(23);

    public static final Animation ANIMATION_DEATHROLL = Animation.create(40);

    public static final Predicate<Entity> NOT_CREEPER = entity -> entity.isAlive() && !(entity instanceof Creeper);

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(EntityCrocodile.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityCrocodile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DESERT = SynchedEntityData.defineId(EntityCrocodile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EntityCrocodile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_DIGGING = SynchedEntityData.defineId(EntityCrocodile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> STUN_TICKS = SynchedEntityData.defineId(EntityCrocodile.class, EntityDataSerializers.INT);

    public float groundProgress = 0.0F;

    public float prevGroundProgress = 0.0F;

    public float swimProgress = 0.0F;

    public float prevSwimProgress = 0.0F;

    public float baskingProgress = 0.0F;

    public float prevBaskingProgress = 0.0F;

    public float grabProgress = 0.0F;

    public float prevGrabProgress = 0.0F;

    public int baskingType = 0;

    public boolean forcedSit = false;

    private int baskingTimer = 0;

    private int swimTimer = -1000;

    private int ticksSinceInWater = 0;

    private int passengerTimer = 0;

    private boolean isLandNavigator;

    private boolean hasSpedUp = false;

    private int animationTick;

    private Animation currentAnimation;

    protected EntityCrocodile(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(false);
        this.baskingType = this.f_19796_.nextInt(1);
    }

    public static boolean canCrocodileSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(AMTagRegistry.CROCODILE_SPAWNS);
        return spawnBlock && pos.m_123342_() < worldIn.m_5736_() + 4;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 15.0).add(Attributes.ARMOR, 8.0).add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.4F).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.crocSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    protected void ageBoundaryReached() {
        super.m_30232_();
        if (!this.m_6162_() && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.m_5552_(new ItemStack(AMItemRegistry.CROCODILE_SCUTE.get(), this.f_19796_.nextInt(1) + 1), 1.0F);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setDesert(this.isBiomeDesert(worldIn, this.m_20183_()));
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private boolean isBiomeDesert(LevelAccessor worldIn, BlockPos position) {
        return worldIn.m_204166_(position).is(AMTagRegistry.SPAWNS_DESERT_CROCODILES);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_6162_() ? AMSoundRegistry.CROCODILE_BABY.get() : AMSoundRegistry.CROCODILE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.CROCODILE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.CROCODILE_HURT.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("CrocodileSitting", this.isSitting());
        compound.putBoolean("Desert", this.isDesert());
        compound.putBoolean("ForcedToSit", this.forcedSit);
        compound.putInt("BaskingStyle", this.baskingType);
        compound.putInt("BaskingTimer", this.baskingTimer);
        compound.putInt("SwimTimer", this.swimTimer);
        compound.putInt("StunTimer", this.getStunTicks());
        compound.putBoolean("HasEgg", this.hasEgg());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOrderedToSit(compound.getBoolean("CrocodileSitting"));
        this.setDesert(compound.getBoolean("Desert"));
        this.forcedSit = compound.getBoolean("ForcedToSit");
        this.baskingType = compound.getInt("BaskingStyle");
        this.baskingTimer = compound.getInt("BaskingTimer");
        this.swimTimer = compound.getInt("SwimTimer");
        this.setHasEgg(compound.getBoolean("HasEgg"));
        this.setStunTicks(compound.getInt("StunTimer"));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            PathNavigation prevNav = this.f_21344_;
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AquaticMoveController(this, 1.0F);
            PathNavigation prevNav = this.f_21344_;
            this.f_21344_ = new SemiAquaticPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(DESERT, false);
        this.f_19804_.define(HAS_EGG, false);
        this.f_19804_.define(IS_DIGGING, false);
        this.f_19804_.define(CLIMBING, (byte) 0);
        this.f_19804_.define(STUN_TICKS, 0);
    }

    public boolean isBesideClimbableBlock() {
        return (this.f_19804_.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.f_19804_.get(CLIMBING);
        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }
        this.f_19804_.set(CLIMBING, b0);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevGroundProgress = this.groundProgress;
        this.prevSwimProgress = this.swimProgress;
        this.prevBaskingProgress = this.baskingProgress;
        this.prevGrabProgress = this.grabProgress;
        boolean ground = !this.m_20069_();
        boolean groundAnimate = !this.m_20069_();
        boolean basking = groundAnimate && this.isSitting();
        boolean grabbing = !this.m_20197_().isEmpty();
        if (!ground && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (ground && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if (groundAnimate) {
            if (this.groundProgress < 10.0F) {
                this.groundProgress++;
            }
            if (this.swimProgress > 0.0F) {
                this.swimProgress--;
            }
        } else {
            if (this.groundProgress > 0.0F) {
                this.groundProgress--;
            }
            if (this.swimProgress < 10.0F) {
                this.swimProgress++;
            }
        }
        if (basking) {
            if (this.baskingProgress < 10.0F) {
                this.baskingProgress++;
            }
        } else if (this.baskingProgress > 0.0F) {
            this.baskingProgress--;
        }
        if (grabbing) {
            if (this.grabProgress < 10.0F) {
                this.grabProgress++;
            }
        } else if (this.grabProgress > 0.0F) {
            this.grabProgress--;
        }
        if (this.m_5448_() == null) {
            if (this.hasSpedUp) {
                this.hasSpedUp = false;
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
            }
        } else if (!this.hasSpedUp) {
            this.hasSpedUp = true;
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.28F);
        }
        if (!this.m_9236_().isClientSide) {
            this.setBesideClimbableBlock(this.f_19862_);
        }
        if (this.baskingTimer < 0) {
            this.baskingTimer++;
        }
        if (this.passengerTimer > 0 && this.m_20197_().isEmpty()) {
            this.passengerTimer = 0;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_20069_()) {
                this.swimTimer++;
                this.ticksSinceInWater = 0;
            } else {
                this.ticksSinceInWater++;
                this.swimTimer--;
            }
            if (!this.m_20069_() && this.m_20096_() && !this.m_21824_()) {
                if (!this.isSitting() && this.baskingTimer == 0 && this.m_5448_() == null && this.m_21573_().isDone()) {
                    this.setOrderedToSit(true);
                    this.baskingTimer = 1000 + this.f_19796_.nextInt(750);
                }
                if (this.isSitting() && (this.baskingTimer <= 0 || this.m_5448_() != null || this.swimTimer < -1000)) {
                    this.setOrderedToSit(false);
                    this.baskingTimer = -2000 - this.f_19796_.nextInt(750);
                }
                if (this.isSitting() && this.baskingTimer > 0) {
                    this.baskingTimer--;
                }
            }
            if (this.getStunTicks() == 0 && this.m_6084_() && this.m_5448_() != null && this.getAnimation() == ANIMATION_LUNGE && (this.m_9236_().m_46791_() != Difficulty.PEACEFUL || !(this.m_5448_() instanceof Player)) && this.getAnimationTick() > 5 && this.getAnimationTick() < 9) {
                float f1 = this.m_146908_() * (float) (Math.PI / 180.0);
                this.m_20256_(this.m_20184_().add((double) (-Mth.sin(f1) * 0.02F), 0.0, (double) (Mth.cos(f1) * 0.02F)));
                if (this.m_20270_(this.m_5448_()) < 3.5F && this.m_142582_(this.m_5448_())) {
                    boolean flag = this.m_5448_().isBlocking();
                    if (!flag && this.m_5448_().m_20205_() < this.m_20205_() && this.m_20197_().isEmpty() && !this.m_5448_().m_6144_()) {
                        this.m_5448_().m_7998_(this, true);
                    }
                    if (flag) {
                        if (this.m_5448_() instanceof Player player) {
                            this.damageShieldFor(player, (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                        }
                        if (this.getStunTicks() == 0) {
                            this.setStunTicks(25 + this.f_19796_.nextInt(20));
                        }
                    } else {
                        this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                    }
                    this.m_5496_(AMSoundRegistry.CROCODILE_BITE.get(), this.m_6121_(), this.m_6100_());
                }
            }
            if (this.m_6084_() && this.m_5448_() != null && this.m_20069_() && (this.m_9236_().m_46791_() != Difficulty.PEACEFUL || !(this.m_5448_() instanceof Player)) && this.m_5448_().m_20202_() != null && this.m_5448_().m_20202_() == this) {
                if (this.getAnimation() == NO_ANIMATION) {
                    this.setAnimation(ANIMATION_DEATHROLL);
                }
                if (this.getAnimation() == ANIMATION_DEATHROLL && this.getAnimationTick() % 10 == 0 && (double) this.m_20270_(this.m_5448_()) < 5.0) {
                    this.m_5448_().hurt(this.m_269291_().mobAttack(this), 5.0F);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_DEATHROLL) {
            this.m_21573_().stop();
        }
        if (this.m_27593_() && this.m_5448_() != null) {
            this.setTarget(null);
        }
        if (this.getStunTicks() > 0) {
            this.setStunTicks(this.getStunTicks() - 1);
            if (this.m_9236_().isClientSide) {
                float angle = (float) (Math.PI / 180.0) * this.f_20883_;
                double headX = (double) (1.5F * this.m_6134_() * Mth.sin((float) Math.PI + angle));
                double headZ = (double) (1.5F * this.m_6134_() * Mth.cos(angle));
                for (int i = 0; i < 5; i++) {
                    float innerAngle = (float) (Math.PI / 180.0) * (this.f_20883_ + (float) (this.f_19797_ * 5)) * (float) (i + 1);
                    double extraX = (double) (0.5F * Mth.sin((float) (Math.PI + (double) innerAngle)));
                    double extraZ = (double) (0.5F * Mth.cos(innerAngle));
                    this.m_9236_().addParticle(ParticleTypes.CRIT, true, this.m_20185_() + headX + extraX, this.m_20188_() + 0.5, this.m_20189_() + headZ + extraZ, 0.0, 0.0, 0.0);
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    protected void damageShieldFor(Player holder, float damage) {
        if (holder.m_21211_().canPerformAction(ToolActions.SHIELD_BLOCK)) {
            if (!this.m_9236_().isClientSide) {
                holder.awardStat(Stats.ITEM_USED.get(holder.m_21211_().getItem()));
            }
            if (damage >= 3.0F) {
                int i = 1 + Mth.floor(damage);
                InteractionHand hand = holder.m_7655_();
                holder.m_21211_().hurtAndBreak(i, holder, p_213833_1_ -> {
                    p_213833_1_.m_21190_(hand);
                    ForgeEventFactory.onPlayerDestroyItem(holder, holder.m_21211_(), hand);
                });
                if (holder.m_21211_().isEmpty()) {
                    if (hand == InteractionHand.MAIN_HAND) {
                        holder.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        holder.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }
                    holder.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.m_9236_().random.nextFloat() * 0.4F);
                }
            }
        }
    }

    @Override
    protected boolean isImmobile() {
        return super.m_6107_() || this.getStunTicks() > 0;
    }

    public boolean canRiderInteract() {
        return true;
    }

    public boolean shouldRiderSit() {
        return false;
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
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (!this.m_20197_().isEmpty()) {
            this.f_20883_ = Mth.wrapDegrees(this.m_146908_() - 180.0F);
        }
        if (this.m_20363_(passenger)) {
            float radius = 2.0F;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (2.0F * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (2.0F * Mth.cos(angle));
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + 0.1F, this.m_20189_() + extraZ);
            this.passengerTimer++;
            if (this.m_6084_() && this.passengerTimer > 0 && this.passengerTimer % 40 == 0) {
                passenger.hurt(this.m_269291_().mobAttack(this), 2.0F);
            }
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    public boolean onClimbable() {
        return this.m_20069_() && this.isBesideClimbableBlock();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION && this.m_20197_().isEmpty() && this.getStunTicks() == 0) {
            this.setAnimation(ANIMATION_LUNGE);
        }
        return true;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isSitting()) {
            super.m_7023_(Vec3.ZERO);
        } else if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.DROWN) || source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return super.m_5610_(pos, worldIn);
    }

    @Override
    public boolean shouldLeaveWater() {
        if (!this.m_20197_().isEmpty()) {
            return false;
        } else {
            return this.m_5448_() != null && !this.m_5448_().m_20069_() ? true : this.swimTimer > 600;
        }
    }

    @Override
    public boolean shouldStopMoving() {
        return this.getAnimation() == ANIMATION_DEATHROLL || this.isSitting();
    }

    @Override
    public int getWaterSearchRange() {
        return this.m_20197_().isEmpty() ? 15 : 45;
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public boolean isDesert() {
        return this.f_19804_.get(DESERT);
    }

    public void setDesert(boolean desert) {
        this.f_19804_.set(DESERT, desert);
    }

    public boolean hasEgg() {
        return this.f_19804_.get(HAS_EGG);
    }

    private void setHasEgg(boolean hasEgg) {
        this.f_19804_.set(HAS_EGG, hasEgg);
    }

    public boolean isDigging() {
        return this.f_19804_.get(IS_DIGGING);
    }

    private void setDigging(boolean isDigging) {
        this.f_19804_.set(IS_DIGGING, isDigging);
    }

    public int getStunTicks() {
        return this.f_19804_.get(STUN_TICKS);
    }

    private void setStunTicks(int stun) {
        this.f_19804_.set(STUN_TICKS, stun);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(1, new EntityCrocodile.MateGoal(this, 1.0));
        this.f_21345_.addGoal(1, new EntityCrocodile.LayEggGoal(this, 1.0));
        this.f_21345_.addGoal(2, new BreathAirGoal(this));
        this.f_21345_.addGoal(2, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(2, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(4, new CrocodileAIMelee(this, 1.0, true));
        this.f_21345_.addGoal(5, new CrocodileAIRandomSwimming(this, 1.0, 7));
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new AnimalAIHurtByTargetNotBaby(this).m_26044_(new Class[0]));
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(4, new EntityAINearestTarget3D(this, Player.class, 80, false, true, null) {

            @Override
            public boolean canUse() {
                return !EntityCrocodile.this.m_6162_() && !EntityCrocodile.this.m_21824_() && EntityCrocodile.this.m_9236_().m_46791_() != Difficulty.PEACEFUL && super.m_8036_();
            }
        });
        this.f_21346_.addGoal(5, new EntityAINearestTarget3D(this, LivingEntity.class, 180, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.CROCODILE_TARGETS)) {

            @Override
            public boolean canUse() {
                return !EntityCrocodile.this.m_6162_() && !EntityCrocodile.this.m_21824_() && super.m_8036_();
            }
        });
        this.f_21346_.addGoal(6, new EntityAINearestTarget3D(this, Monster.class, 180, false, true, NOT_CREEPER) {

            @Override
            public boolean canUse() {
                return !EntityCrocodile.this.m_6162_() && EntityCrocodile.this.m_21824_() && super.m_8036_();
            }
        });
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

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return AMEntityRegistry.CROCODILE.get().create(p_241840_1_);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        if (item == Items.NAME_TAG) {
            return super.m_6071_(player, hand);
        } else if (this.m_21824_() && item.isEdible() && item.getFoodProperties() != null && item.getFoodProperties().isMeat() && this.m_21223_() < this.m_21233_()) {
            this.m_142075_(player, hand, itemstack);
            this.m_5634_(10.0F);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
            return InteractionResult.SUCCESS;
        } else {
            InteractionResult type = super.m_6071_(player, hand);
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21824_() && this.m_21830_(player) && !this.isFood(itemstack)) {
                if (this.isSitting()) {
                    this.forcedSit = false;
                    this.setOrderedToSit(false);
                } else {
                    this.forcedSit = true;
                    this.setOrderedToSit(true);
                }
                return InteractionResult.SUCCESS;
            } else {
                return type;
            }
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity entitylivingbaseIn) {
        if (!this.m_6162_()) {
            super.m_6710_(entitylivingbaseIn);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.ROTTEN_FLESH;
    }

    @Override
    public boolean shouldEnterWater() {
        return !this.m_20197_().isEmpty() ? true : this.m_5448_() == null && !this.isSitting() && this.baskingTimer <= 0 && !this.shouldLeaveWater() && this.swimTimer <= -1000;
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
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_LUNGE, ANIMATION_DEATHROLL };
    }

    public boolean isCrowned() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && (s.toLowerCase().contains("crown") || s.toLowerCase().contains("king") || s.toLowerCase().contains("rool"));
    }

    static class LayEggGoal extends MoveToBlockGoal {

        private final EntityCrocodile turtle;

        private int digTime;

        LayEggGoal(EntityCrocodile turtle, double speedIn) {
            super(turtle, speedIn, 16);
            this.turtle = turtle;
        }

        @Override
        public void stop() {
            this.digTime = 0;
        }

        @Override
        public boolean canUse() {
            return this.turtle.hasEgg() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.turtle.hasEgg();
        }

        @Override
        public double acceptedDistance() {
            return (double) this.turtle.m_20205_() + 0.5;
        }

        @Override
        public void tick() {
            super.tick();
            this.turtle.setOrderedToSit(false);
            this.turtle.baskingTimer = -100;
            if (!this.turtle.m_20069_() && this.m_25625_()) {
                BlockPos blockpos = this.turtle.m_20183_();
                Level world = this.turtle.m_9236_();
                this.turtle.m_146850_(GameEvent.BLOCK_PLACE);
                world.playSound(null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
                world.setBlock(this.f_25602_.above(), (BlockState) AMBlockRegistry.CROCODILE_EGG.get().defaultBlockState().m_61124_(BlockReptileEgg.EGGS, this.turtle.f_19796_.nextInt(1) + 1), 3);
                this.turtle.setHasEgg(false);
                this.turtle.setDigging(false);
                this.turtle.m_27601_(600);
            }
        }

        @Override
        protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
            return worldIn.isEmptyBlock(pos.above()) && BlockReptileEgg.isProperHabitat(worldIn, pos);
        }
    }

    static class MateGoal extends BreedGoal {

        private final EntityCrocodile crocodile;

        MateGoal(EntityCrocodile crocodile, double speedIn) {
            super(crocodile, speedIn);
            this.crocodile = crocodile;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.crocodile.hasEgg();
        }

        @Override
        protected void breed() {
            ServerPlayer serverplayerentity = this.f_25113_.getLoveCause();
            if (serverplayerentity == null && this.f_25115_.getLoveCause() != null) {
                serverplayerentity = this.f_25115_.getLoveCause();
            }
            if (serverplayerentity != null) {
                serverplayerentity.m_36220_(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.f_25113_, this.f_25115_, this.f_25113_);
            }
            this.crocodile.setHasEgg(true);
            this.f_25113_.resetLove();
            this.f_25115_.resetLove();
            this.f_25113_.m_146762_(6000);
            this.f_25115_.m_146762_(6000);
            if (this.f_25114_.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                RandomSource random = this.f_25113_.m_217043_();
                this.f_25114_.m_7967_(new ExperienceOrb(this.f_25114_, this.f_25113_.m_20185_(), this.f_25113_.m_20186_(), this.f_25113_.m_20189_(), random.nextInt(7) + 1));
            }
        }
    }
}