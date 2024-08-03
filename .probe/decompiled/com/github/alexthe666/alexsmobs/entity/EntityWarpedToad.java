package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeapRandomly;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.BoneSerpentPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.WarpedToadAIRandomSwimming;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class EntityWarpedToad extends TamableAnimal implements ITargetsDroppedItems, IFollower, ISemiAquatic {

    private static final EntityDataAccessor<Float> TONGUE_LENGTH = SynchedEntityData.defineId(EntityWarpedToad.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> TONGUE_OUT = SynchedEntityData.defineId(EntityWarpedToad.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityWarpedToad.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityWarpedToad.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> JUMP_ACTIVE = SynchedEntityData.defineId(EntityWarpedToad.class, EntityDataSerializers.BOOLEAN);

    public float blinkProgress;

    public float prevBlinkProgress;

    public float attackProgress;

    public float prevAttackProgress;

    public float sitProgress;

    public float prevSitProgress;

    public float swimProgress;

    public float prevSwimProgress;

    public float jumpProgress;

    public float prevJumpProgress;

    public float reboundProgress;

    public float prevReboundProgress;

    private boolean isLandNavigator;

    private int currentMoveTypeDuration;

    private int swimTimer = -100;

    protected EntityWarpedToad(EntityType entityType, Level world) {
        super(entityType, world);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.LAVA, 0.0F);
        this.switchNavigator(false);
    }

    public boolean isBased() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("pepe");
    }

    public static boolean canWarpedToadSpawn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        BlockPos blockpos = pos.below();
        boolean spawnBlock = worldIn.m_6425_(blockpos).is(FluidTags.LAVA) || worldIn.m_8055_(blockpos).m_60815_();
        return reason == MobSpawnType.SPAWNER || spawnBlock;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.25).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.WARPED_TOAD_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.WARPED_TOAD_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.WARPED_TOAD_HURT.get();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.warpedToadSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 5;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("ToadSitting", this.isOrderedToSit());
        compound.putInt("Command", this.getCommand());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOrderedToSit(compound.getBoolean("ToadSitting"));
        this.setCommand(compound.getInt("Command"));
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(1, new EntityWarpedToad.TongueAttack(this));
        this.f_21345_.addGoal(2, new EntityWarpedToad.FollowOwner(this, 1.3, 4.0F, 2.0F, false));
        this.f_21345_.addGoal(3, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(3, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(3, new BreedGoal(this, 0.8));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.0, Ingredient.of(AMTagRegistry.INSECT_ITEMS), false));
        this.f_21345_.addGoal(5, new WarpedToadAIRandomSwimming(this, 1.0, 7));
        this.f_21345_.addGoal(6, new AnimalAILeapRandomly(this, 50, 7) {

            @Override
            public boolean canUse() {
                return super.canUse() && !EntityWarpedToad.this.isOrderedToSit();
            }
        });
        this.f_21345_.addGoal(7, new AnimalAIWanderRanged(this, 60, 1.0, 5, 4));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.f_21345_.addGoal(11, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false));
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(4, new EntityAINearestTarget3D(this, LivingEntity.class, 50, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.WARPED_TOAD_TARGETS)));
        this.f_21346_.addGoal(5, new HurtByTargetGoal(this));
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isOrderedToSit()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            travelVector = Vec3.ZERO;
            super.m_7023_(travelVector);
        } else if (this.m_21515_() && (this.m_20069_() || this.m_20077_())) {
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
    protected float getJumpPower() {
        return 0.5F;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void customServerAiStep() {
        super.m_8024_();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == AMItemRegistry.MOSQUITO_LARVA.get() && this.m_21824_();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        if (!this.m_21824_() && item == AMItemRegistry.MOSQUITO_LARVA.get()) {
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.STRIDER_EAT, this.m_6121_(), this.m_6100_());
            if (this.m_217043_().nextInt(3) == 0) {
                this.m_21828_(player);
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        } else if (this.m_21824_() && itemstack.is(AMTagRegistry.INSECT_ITEMS)) {
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
            if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21824_() && this.m_21830_(player) && !this.isFood(itemstack)) {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() == 3) {
                    this.setCommand(0);
                }
                player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
                boolean sit = this.getCommand() == 2;
                if (sit) {
                    this.setOrderedToSit(true);
                    return InteractionResult.SUCCESS;
                } else {
                    this.setOrderedToSit(false);
                    return InteractionResult.SUCCESS;
                }
            } else {
                return type;
            }
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
    public boolean canSpawnSprintParticle() {
        return false;
    }

    private void calculateRotationYaw(double x, double z) {
        this.m_146922_((float) (Mth.atan2(z - this.m_20189_(), x - this.m_20185_()) * 180.0F / (float) Math.PI) - 90.0F);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.m_6162_() && this.m_20192_() > this.m_20206_()) {
            this.m_6210_();
        }
        if (!this.m_9236_().isClientSide) {
            if (!this.m_20069_() && !this.m_20077_()) {
                if (this.swimTimer > 0) {
                    this.swimTimer = 0;
                }
                this.swimTimer--;
            } else {
                if (this.swimTimer < 0) {
                    this.swimTimer = 0;
                }
                this.swimTimer++;
            }
        }
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = this.m_6037_(this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AquaticMoveController(this, 1.2F);
            this.f_21344_ = new BoneSerpentPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(TONGUE_LENGTH, 1.0F);
        this.f_19804_.define(TONGUE_OUT, false);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(JUMP_ACTIVE, false);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    @Override
    public boolean isOrderedToSit() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevBlinkProgress = this.blinkProgress;
        this.prevAttackProgress = this.attackProgress;
        this.prevSitProgress = this.sitProgress;
        this.prevSwimProgress = this.swimProgress;
        this.prevJumpProgress = this.jumpProgress;
        this.prevReboundProgress = this.reboundProgress;
        this.m_274367_(1.0F);
        boolean isTechnicalBlinking = this.f_19797_ % 50 > 42;
        if (isTechnicalBlinking) {
            if (this.blinkProgress < 5.0F) {
                this.blinkProgress++;
            }
        } else if (this.blinkProgress > 0.0F) {
            this.blinkProgress--;
        }
        boolean isTongueOut = this.isTongueOut();
        if (isTongueOut && this.attackProgress < 5.0F) {
            this.attackProgress++;
        }
        if (!this.m_9236_().isClientSide) {
            this.f_19804_.set(JUMP_ACTIVE, !this.m_20096_());
        }
        if (this.f_19804_.get(JUMP_ACTIVE) && !this.m_20072_()) {
            this.f_20883_ = this.m_146908_();
            this.f_20885_ = this.m_146908_();
            if (this.jumpProgress < 5.0F) {
                this.jumpProgress += 0.5F;
                if (this.reboundProgress > 0.0F) {
                    this.reboundProgress--;
                }
            }
            if (this.jumpProgress >= 5.0F && this.reboundProgress < 5.0F) {
                this.reboundProgress += 0.5F;
            }
        } else {
            if (this.reboundProgress > 0.0F) {
                this.reboundProgress = Math.max(this.reboundProgress - 1.0F, 0.0F);
            }
            if (this.jumpProgress > 0.0F) {
                this.jumpProgress = Math.max(this.jumpProgress - 1.0F, 0.0F);
            }
        }
        LivingEntity entityIn = this.m_5448_();
        if (entityIn != null && this.attackProgress > 0.0F) {
            if (this.isTongueOut()) {
                double d0 = entityIn.m_20185_() - this.m_20185_();
                double d2 = entityIn.m_20189_() - this.m_20189_();
                double d1 = entityIn.m_20188_() - this.m_20188_();
                double d3 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
                float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                float f1 = (float) (-(Mth.atan2(d1, d3) * 180.0F / (float) Math.PI));
                this.m_146926_(f1);
                this.m_146922_(f);
                this.f_20883_ = this.m_146908_();
                this.f_20885_ = this.m_146908_();
            } else {
                if (entityIn instanceof EntityCrimsonMosquito) {
                    ((EntityCrimsonMosquito) entityIn).setShrink(true);
                }
                this.m_146926_(0.0F);
                float radius = this.attackProgress * 0.2F * 1.2F * (this.getTongueLength() - this.getTongueLength() * 0.4F);
                float angle = (float) (Math.PI / 180.0) * this.f_20883_;
                double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
                double extraZ = (double) (radius * Mth.cos(angle));
                double yHelp = (double) entityIn.m_20206_();
                Vec3 minus = new Vec3(this.m_20185_() + extraX - this.m_5448_().m_20185_(), (double) this.m_20192_() - yHelp - this.m_5448_().m_20186_(), this.m_20189_() + extraZ - this.m_5448_().m_20189_());
                this.m_5448_().m_20256_(minus);
                if (this.attackProgress == 0.5F) {
                    float damage = (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue();
                    if (entityIn instanceof EntityCrimsonMosquito) {
                        damage = Float.MAX_VALUE;
                    }
                    entityIn.hurt(this.m_269291_().mobAttack(this), damage);
                }
            }
        }
        if (!this.m_9236_().isClientSide && this.attackProgress == 5.0F && isTongueOut) {
            this.setTongueOut(false);
            this.attackProgress = 4.0F;
        }
        if (this.attackProgress > 0.0F && !this.isTongueOut()) {
            this.attackProgress -= 0.5F;
        }
        if (this.isOrderedToSit()) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (this.shouldSwim()) {
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (this.swimProgress < 5.0F) {
                this.swimProgress++;
            }
        } else {
            if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.swimProgress > 0.0F) {
                this.swimProgress--;
            }
        }
    }

    public boolean shouldSwim() {
        return this.m_20069_() || this.m_20077_();
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.is(AMTagRegistry.INSECT_ITEMS);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.m_5634_(5.0F);
    }

    public boolean isBlinking() {
        return this.blinkProgress > 1.0F || this.blinkProgress < -1.0F || this.attackProgress > 1.0F;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.WARPED_TOAD.get().create(serverWorld);
    }

    public float getTongueLength() {
        return this.f_19804_.get(TONGUE_LENGTH);
    }

    public void setTongueLength(float length) {
        this.f_19804_.set(TONGUE_LENGTH, length);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean shouldEnterWater() {
        return this.swimTimer < -200 && !this.isOrderedToSit() && this.getCommand() != 1;
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.swimTimer > 600 && !this.isOrderedToSit() && this.getCommand() != 1;
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isOrderedToSit();
    }

    private boolean isTongueOut() {
        return this.f_19804_.get(TONGUE_OUT);
    }

    private void setTongueOut(boolean out) {
        this.f_19804_.set(TONGUE_OUT, out);
    }

    @Override
    public int getWaterSearchRange() {
        return 8;
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    public static class FollowOwner extends Goal {

        private final EntityWarpedToad tameable;

        private final LevelReader world;

        private final double followSpeed;

        private final float maxDist;

        private final float minDist;

        private final boolean teleportToLeaves;

        private LivingEntity owner;

        private int timeToRecalcPath;

        private float oldWaterCost;

        public FollowOwner(EntityWarpedToad p_i225711_1_, double p_i225711_2_, float p_i225711_4_, float p_i225711_5_, boolean p_i225711_6_) {
            this.tameable = p_i225711_1_;
            this.world = p_i225711_1_.m_9236_();
            this.followSpeed = p_i225711_2_;
            this.minDist = p_i225711_4_;
            this.maxDist = p_i225711_5_;
            this.teleportToLeaves = p_i225711_6_;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            if (!(p_i225711_1_.m_21573_() instanceof GroundPathNavigation) && !(p_i225711_1_.m_21573_() instanceof FlyingPathNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        @Override
        public boolean canUse() {
            LivingEntity lvt_1_1_ = this.tameable.m_269323_();
            if (lvt_1_1_ == null) {
                return false;
            } else if (lvt_1_1_.m_5833_()) {
                return false;
            } else if (this.tameable.isOrderedToSit() || this.tameable.getCommand() != 1) {
                return false;
            } else if (this.tameable.m_20280_(lvt_1_1_) < (double) (this.minDist * this.minDist)) {
                return false;
            } else {
                this.owner = lvt_1_1_;
                return true;
            }
        }

        @Override
        public boolean canContinueToUse() {
            if (this.tameable.m_21573_().isDone()) {
                return false;
            } else {
                return !this.tameable.isOrderedToSit() && this.tameable.getCommand() == 1 ? this.tameable.m_20280_(this.owner) > (double) (this.maxDist * this.maxDist) : false;
            }
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.tameable.m_21439_(BlockPathTypes.WATER);
            this.tameable.m_21441_(BlockPathTypes.WATER, 0.0F);
        }

        @Override
        public void stop() {
            this.owner = null;
            this.tameable.m_21573_().stop();
            this.tameable.m_21441_(BlockPathTypes.WATER, this.oldWaterCost);
        }

        @Override
        public void tick() {
            this.tameable.m_21563_().setLookAt(this.owner, 10.0F, (float) this.tameable.m_8132_());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.tameable.m_21523_() && !this.tameable.m_20159_()) {
                    if (this.tameable.m_20280_(this.owner) >= 144.0) {
                        this.tryToTeleportNearEntity();
                    } else {
                        this.tameable.m_21573_().moveTo(this.owner, this.followSpeed);
                    }
                }
            }
        }

        private void tryToTeleportNearEntity() {
            BlockPos lvt_1_1_ = this.owner.m_20183_();
            for (int lvt_2_1_ = 0; lvt_2_1_ < 10; lvt_2_1_++) {
                int lvt_3_1_ = this.getRandomNumber(-3, 3);
                int lvt_4_1_ = this.getRandomNumber(-1, 1);
                int lvt_5_1_ = this.getRandomNumber(-3, 3);
                boolean lvt_6_1_ = this.tryToTeleportToLocation(lvt_1_1_.m_123341_() + lvt_3_1_, lvt_1_1_.m_123342_() + lvt_4_1_, lvt_1_1_.m_123343_() + lvt_5_1_);
                if (lvt_6_1_) {
                    return;
                }
            }
        }

        private boolean tryToTeleportToLocation(int p_226328_1_, int p_226328_2_, int p_226328_3_) {
            if (Math.abs((double) p_226328_1_ - this.owner.m_20185_()) < 2.0 && Math.abs((double) p_226328_3_ - this.owner.m_20189_()) < 2.0) {
                return false;
            } else if (!this.isTeleportFriendlyBlock(new BlockPos(p_226328_1_, p_226328_2_, p_226328_3_))) {
                return false;
            } else {
                this.tameable.m_7678_((double) p_226328_1_ + 0.5, (double) p_226328_2_, (double) p_226328_3_ + 0.5, this.tameable.m_146908_(), this.tameable.m_146909_());
                this.tameable.m_21573_().stop();
                return true;
            }
        }

        private boolean isTeleportFriendlyBlock(BlockPos p_226329_1_) {
            BlockPathTypes lvt_2_1_ = WalkNodeEvaluator.getBlockPathTypeStatic(this.world, p_226329_1_.mutable());
            if (lvt_2_1_ != BlockPathTypes.WALKABLE) {
                return false;
            } else {
                BlockState lvt_3_1_ = this.world.m_8055_(p_226329_1_.below());
                if (!this.teleportToLeaves && lvt_3_1_.m_60734_() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos lvt_4_1_ = p_226329_1_.subtract(this.tameable.m_20183_());
                    return this.world.m_45756_(this.tameable, this.tameable.m_20191_().move(lvt_4_1_));
                }
            }
        }

        private int getRandomNumber(int p_226327_1_, int p_226327_2_) {
            return this.tameable.m_217043_().nextInt(p_226327_2_ - p_226327_1_ + 1) + p_226327_1_;
        }
    }

    public static class TongueAttack extends Goal {

        private final EntityWarpedToad parentEntity;

        private int spitCooldown = 0;

        private BlockPos shootPos = null;

        public TongueAttack(EntityWarpedToad toad) {
            this.parentEntity = toad;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.parentEntity.m_5448_() != null && this.parentEntity.m_20197_().isEmpty();
        }

        @Override
        public boolean canContinueToUse() {
            return this.parentEntity.m_5448_() != null && this.parentEntity.m_20197_().isEmpty();
        }

        @Override
        public void stop() {
            this.spitCooldown = 20;
            this.parentEntity.m_21573_().stop();
        }

        @Override
        public void tick() {
            if (this.spitCooldown > 0) {
                this.spitCooldown--;
            }
            Entity entityIn = this.parentEntity.m_5448_();
            if (entityIn != null) {
                double dist = (double) this.parentEntity.m_20270_(entityIn);
                if (dist < 8.0 && this.parentEntity.m_142582_(entityIn) && !this.parentEntity.isTongueOut() && this.parentEntity.attackProgress == 0.0F && this.spitCooldown == 0) {
                    this.parentEntity.setTongueLength((float) Math.max(1.0, dist + 2.0));
                    this.spitCooldown = 10;
                    this.parentEntity.setTongueOut(true);
                }
                this.parentEntity.m_21573_().moveTo(entityIn, 1.4F);
            }
        }
    }
}