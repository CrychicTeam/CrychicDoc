package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalSwimMoveControllerSink;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.MantisShrimpAIBreakBlocks;
import com.github.alexthe666.alexsmobs.entity.ai.MantisShrimpAIFryRice;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;

public class EntityMantisShrimp extends TamableAnimal implements ISemiAquatic, IFollower {

    private static final EntityDataAccessor<Float> RIGHT_EYE_PITCH = SynchedEntityData.defineId(EntityMantisShrimp.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> RIGHT_EYE_YAW = SynchedEntityData.defineId(EntityMantisShrimp.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> LEFT_EYE_PITCH = SynchedEntityData.defineId(EntityMantisShrimp.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> LEFT_EYE_YAW = SynchedEntityData.defineId(EntityMantisShrimp.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> PUNCH_TICK = SynchedEntityData.defineId(EntityMantisShrimp.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityMantisShrimp.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityMantisShrimp.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityMantisShrimp.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> MOISTNESS = SynchedEntityData.defineId(EntityMantisShrimp.class, EntityDataSerializers.INT);

    public float prevRightPitch;

    public float prevRightYaw;

    public float prevLeftPitch;

    public float prevLeftYaw;

    public float prevInWaterProgress;

    public float inWaterProgress;

    public float prevPunchProgress;

    public float punchProgress;

    private int leftLookCooldown = 0;

    private int rightLookCooldown = 0;

    private float targetRightPitch;

    private float targetRightYaw;

    private float targetLeftPitch;

    private float targetLeftYaw;

    private boolean isLandNavigator;

    private int fishFeedings;

    private int moistureAttackTime = 0;

    protected EntityMantisShrimp(EntityType type, Level world) {
        super(type, world);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(false);
        this.m_274367_(1.0F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.MANTIS_SHRIMP_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.MANTIS_SHRIMP_HURT.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            if (entity instanceof Shulker || entity instanceof ShulkerBullet) {
                amount = (amount + 1.0F) * 0.33F;
            }
            return super.m_6469_(source, amount);
        }
    }

    @Override
    public void awardKillScore(Entity entity, int score, DamageSource src) {
        if (entity instanceof LivingEntity living && living.m_6095_() == EntityType.SHULKER) {
            CompoundTag fishNbt = new CompoundTag();
            living.addAdditionalSaveData(fishNbt);
            fishNbt.putString("DeathLootTable", BuiltInLootTables.EMPTY.toString());
            living.readAdditionalSaveData(fishNbt);
            living.m_19998_(Items.SHULKER_SHELL);
        }
        super.m_5993_(entity, score, src);
    }

    public static boolean canMantisShrimpSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        BlockPos downPos = pos;
        while (downPos.m_123342_() > 1 && !worldIn.m_6425_(downPos).isEmpty()) {
            downPos = downPos.below();
        }
        boolean spawnBlock = worldIn.m_8055_(downPos).m_204336_(AMTagRegistry.MANTIS_SHRIMP_SPAWNS);
        return worldIn.m_204166_(pos).is(AMTagRegistry.SPAWNS_WHITE_MANTIS_SHRIMP) && randomIn.nextFloat() < 0.5F ? false : spawnBlock && downPos.m_123342_() < worldIn.m_5736_() + 1;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.1).add(Attributes.ARMOR, 8.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.m_21824_();
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.mantisShrimpSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new MantisShrimpAIFryRice(this));
        this.f_21345_.addGoal(0, new MantisShrimpAIBreakBlocks(this));
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new EntityMantisShrimp.FollowOwner(this, 1.3, 4.0F, 2.0F, false));
        this.f_21345_.addGoal(3, new MeleeAttackGoal(this, 1.2F, false));
        this.f_21345_.addGoal(4, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(4, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(5, new BreedGoal(this, 0.8));
        this.f_21345_.addGoal(6, new TemptGoal(this, 1.0, Ingredient.of(Items.TROPICAL_FISH, AMItemRegistry.LOBSTER_TAIL.get(), AMItemRegistry.COOKED_LOBSTER_TAIL.get()), false));
        this.f_21345_.addGoal(7, new SemiAquaticAIRandomSwimming(this, 1.0, 30));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new EntityAINearestTarget3D(this, LivingEntity.class, 120, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.MANTIS_SHRIMP_TARGETS)) {

            @Override
            public boolean canUse() {
                return EntityMantisShrimp.this.getCommand() != 3 && !EntityMantisShrimp.this.isSitting() && super.m_8036_();
            }
        });
        this.f_21346_.addGoal(4, new HurtByTargetGoal(this));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AnimalSwimMoveControllerSink(this, 1.0F, 1.0F);
            this.f_21344_ = new SemiAquaticPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isSitting()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            travelVector = Vec3.ZERO;
            super.m_7023_(travelVector);
        } else {
            if (this.m_21515_() && this.m_20069_()) {
                this.m_19920_(this.m_6113_(), travelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.9));
            } else {
                super.m_7023_(travelVector);
            }
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(RIGHT_EYE_PITCH, 0.0F);
        this.f_19804_.define(RIGHT_EYE_YAW, 0.0F);
        this.f_19804_.define(LEFT_EYE_PITCH, 0.0F);
        this.f_19804_.define(LEFT_EYE_YAW, 0.0F);
        this.f_19804_.define(PUNCH_TICK, 0);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(MOISTNESS, 60000);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return this.m_21824_() && (item == AMItemRegistry.LOBSTER_TAIL.get() || item == AMItemRegistry.COOKED_LOBSTER_TAIL.get());
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        this.punch();
        return true;
    }

    public void punch() {
        this.f_19804_.set(PUNCH_TICK, 4);
    }

    public float getEyeYaw(boolean left) {
        return this.f_19804_.get(left ? LEFT_EYE_YAW : RIGHT_EYE_YAW);
    }

    public float getEyePitch(boolean left) {
        return this.f_19804_.get(left ? LEFT_EYE_PITCH : RIGHT_EYE_PITCH);
    }

    public void setEyePitch(boolean left, float pitch) {
        this.f_19804_.set(left ? LEFT_EYE_PITCH : RIGHT_EYE_PITCH, pitch);
    }

    public void setEyeYaw(boolean left, float yaw) {
        this.f_19804_.set(left ? LEFT_EYE_YAW : RIGHT_EYE_YAW, yaw);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int command) {
        this.f_19804_.set(VARIANT, command);
    }

    public int getMoistness() {
        return this.f_19804_.get(MOISTNESS);
    }

    public void setMoistness(int p_211137_1_) {
        this.f_19804_.set(MOISTNESS, p_211137_1_);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.m_21525_()) {
            this.m_20301_(this.m_6062_());
        } else if (!this.m_20071_() && this.m_21205_().getItem() != Items.WATER_BUCKET) {
            this.setMoistness(this.getMoistness() - 1);
            if (this.getMoistness() <= 0 && this.moistureAttackTime-- <= 0) {
                this.setCommand(0);
                this.setOrderedToSit(false);
                this.hurt(this.m_269291_().dryOut(), this.f_19796_.nextInt(2) == 0 ? 1.0F : 0.0F);
                this.moistureAttackTime = 20;
            }
        } else {
            this.setMoistness(60000);
        }
        if (this.m_21023_(MobEffects.LEVITATION)) {
            this.m_20256_(this.m_20184_().multiply(1.0, 0.5, 1.0));
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        if (!this.m_21824_() && item == Items.TROPICAL_FISH) {
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.STRIDER_EAT, this.m_6121_(), this.m_6100_());
            this.fishFeedings++;
            if ((this.fishFeedings <= 10 || this.m_217043_().nextInt(6) != 0) && this.fishFeedings <= 30) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            } else {
                this.m_21828_(player);
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            }
            return InteractionResult.SUCCESS;
        } else if (this.m_21824_() && itemstack.is(ItemTags.FISHES)) {
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
                if (player.m_6144_() || itemstack.is(AMTagRegistry.SHRIMP_RICE_FRYABLES)) {
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
                    if (this.getCommand() == 4) {
                        this.setCommand(0);
                    }
                    if (this.getCommand() == 3) {
                        player.displayClientMessage(Component.translatable("entity.alexsmobs.mantis_shrimp.command_3", this.m_7755_()), true);
                    } else {
                        player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
                    }
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("MantisShrimpSitting", this.isSitting());
        compound.putInt("Command", this.getCommand());
        compound.putInt("Moisture", this.getMoistness());
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOrderedToSit(compound.getBoolean("MantisShrimpSitting"));
        this.setCommand(compound.getInt("Command"));
        this.setVariant(compound.getInt("Variant"));
        this.setMoistness(compound.getInt("Moisture"));
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.m_6162_() && this.m_20192_() > this.m_20206_()) {
            this.m_6210_();
        }
        this.prevLeftPitch = this.getEyePitch(true);
        this.prevRightPitch = this.getEyePitch(false);
        this.prevLeftYaw = this.getEyeYaw(true);
        this.prevRightYaw = this.getEyeYaw(false);
        this.prevInWaterProgress = this.inWaterProgress;
        this.prevPunchProgress = this.punchProgress;
        this.updateEyes();
        if (this.isSitting() && this.m_21573_().isDone()) {
            this.m_21573_().stop();
        }
        if (this.m_20069_()) {
            if (this.inWaterProgress < 5.0F) {
                this.inWaterProgress++;
            }
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
        } else {
            if (this.inWaterProgress > 0.0F) {
                this.inWaterProgress--;
            }
            if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
        }
        if (this.f_19804_.get(PUNCH_TICK) > 0) {
            if (this.f_19804_.get(PUNCH_TICK) == 2 && this.m_5448_() != null && (double) this.m_20270_(this.m_5448_()) < 2.8) {
                if (this.m_5448_() instanceof AbstractFish && !this.m_21824_()) {
                    AbstractFish fish = (AbstractFish) this.m_5448_();
                    CompoundTag fishNbt = new CompoundTag();
                    fish.addAdditionalSaveData(fishNbt);
                    fishNbt.putString("DeathLootTable", BuiltInLootTables.EMPTY.toString());
                    fish.readAdditionalSaveData(fishNbt);
                }
                this.m_5448_().knockback(1.7F, this.m_20185_() - this.m_5448_().m_20185_(), this.m_20189_() - this.m_5448_().m_20189_());
                float knockbackResist = (float) Mth.clamp(1.0 - this.m_21133_(Attributes.KNOCKBACK_RESISTANCE), 0.0, 1.0);
                this.m_5448_().m_20256_(this.m_5448_().m_20184_().add(0.0, (double) (knockbackResist * 0.8F), 0.0));
                if (!this.m_5448_().m_20069_()) {
                    this.m_5448_().m_20254_(2);
                }
                this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21133_(Attributes.ATTACK_DAMAGE));
            }
            if (this.punchProgress == 1.0F) {
                this.m_5496_(AMSoundRegistry.MANTIS_SHRIMP_SNAP.get(), this.m_6100_(), this.m_6121_());
            }
            if (this.punchProgress == 2.0F && this.m_9236_().isClientSide && this.m_20069_()) {
                for (int i = 0; i < 10 + this.f_19796_.nextInt(8); i++) {
                    double d2 = this.f_19796_.nextGaussian() * 0.6;
                    double d0 = this.f_19796_.nextGaussian() * 0.2;
                    double d1 = this.f_19796_.nextGaussian() * 0.6;
                    float radius = this.m_20205_() * 0.85F;
                    float angle = (float) (Math.PI / 180.0) * this.f_20883_;
                    double extraX = (double) (radius * Mth.sin((float) Math.PI + angle) + this.f_19796_.nextFloat() * 0.5F - 0.25F);
                    double extraZ = (double) (radius * Mth.cos(angle) + this.f_19796_.nextFloat() * 0.5F - 0.25F);
                    ParticleOptions data = ParticleTypes.BUBBLE;
                    this.m_9236_().addParticle(data, this.m_20185_() + extraX, this.m_20186_() + (double) (this.m_20206_() * 0.3F) + (double) (this.f_19796_.nextFloat() * 0.15F), this.m_20189_() + extraZ, d0, d1, d2);
                }
            }
            if (this.punchProgress < 2.0F) {
                this.punchProgress++;
            }
            this.f_19804_.set(PUNCH_TICK, this.f_19804_.get(PUNCH_TICK) - 1);
        } else if (this.punchProgress > 0.0F) {
            this.punchProgress -= 0.25F;
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

    private void updateEyes() {
        float leftPitchDist = Math.abs(this.getEyePitch(true) - this.targetLeftPitch);
        float rightPitchDist = Math.abs(this.getEyePitch(false) - this.targetRightPitch);
        float leftYawDist = Math.abs(this.getEyeYaw(true) - this.targetLeftYaw);
        float rightYawDist = Math.abs(this.getEyeYaw(false) - this.targetRightYaw);
        if (this.rightLookCooldown == 0 && this.f_19796_.nextInt(20) == 0 && rightPitchDist < 0.5F && rightYawDist < 0.5F) {
            this.targetRightPitch = Mth.clamp(this.f_19796_.nextFloat() * 60.0F - 30.0F, -30.0F, 30.0F);
            this.targetRightYaw = Mth.clamp(this.f_19796_.nextFloat() * 60.0F - 30.0F, -30.0F, 30.0F);
            this.rightLookCooldown = 3 + this.f_19796_.nextInt(15);
        }
        if (this.leftLookCooldown == 0 && this.f_19796_.nextInt(20) == 0 && leftPitchDist < 0.5F && leftYawDist < 0.5F) {
            this.targetLeftPitch = Mth.clamp(this.f_19796_.nextFloat() * 60.0F - 30.0F, -30.0F, 30.0F);
            this.targetLeftYaw = Mth.clamp(this.f_19796_.nextFloat() * 60.0F - 30.0F, -30.0F, 30.0F);
            this.leftLookCooldown = 3 + this.f_19796_.nextInt(15);
        }
        if (leftPitchDist > 0.5F) {
            if (this.getEyePitch(true) < this.targetLeftPitch) {
                this.setEyePitch(true, this.getEyePitch(true) + Math.min(leftPitchDist, 4.0F));
            }
            if (this.getEyePitch(true) > this.targetLeftPitch) {
                this.setEyePitch(true, this.getEyePitch(true) - Math.min(leftPitchDist, 4.0F));
            }
        }
        if (rightPitchDist > 0.5F) {
            if (this.getEyePitch(false) < this.targetRightPitch) {
                this.setEyePitch(false, this.getEyePitch(false) + Math.min(rightPitchDist, 4.0F));
            }
            if (this.getEyePitch(false) > this.targetRightPitch) {
                this.setEyePitch(false, this.getEyePitch(false) - Math.min(rightPitchDist, 4.0F));
            }
        }
        if (leftYawDist > 0.5F) {
            if (this.getEyeYaw(true) < this.targetLeftYaw) {
                this.setEyeYaw(true, this.getEyeYaw(true) + Math.min(leftYawDist, 4.0F));
            }
            if (this.getEyeYaw(true) > this.targetLeftYaw) {
                this.setEyeYaw(true, this.getEyeYaw(true) - Math.min(leftYawDist, 4.0F));
            }
        }
        if (rightYawDist > 0.5F) {
            if (this.getEyeYaw(false) < this.targetRightYaw) {
                this.setEyeYaw(false, this.getEyeYaw(false) + Math.min(rightYawDist, 4.0F));
            }
            if (this.getEyeYaw(false) > this.targetRightYaw) {
                this.setEyeYaw(false, this.getEyeYaw(false) - Math.min(rightYawDist, 4.0F));
            }
        }
        if (this.rightLookCooldown > 0) {
            this.rightLookCooldown--;
        }
        if (this.leftLookCooldown > 0) {
            this.leftLookCooldown--;
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        int i;
        if (reason == MobSpawnType.SPAWN_EGG) {
            i = this.m_217043_().nextInt(4);
        } else if (worldIn.m_204166_(this.m_20183_()).is(AMTagRegistry.SPAWNS_WHITE_MANTIS_SHRIMP)) {
            i = 3;
        } else {
            i = this.m_217043_().nextInt(3);
        }
        this.setVariant(i);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityMantisShrimp shrimp = AMEntityRegistry.MANTIS_SHRIMP.get().create(serverWorld);
        shrimp.setVariant(this.m_217043_().nextInt(3));
        return shrimp;
    }

    @Override
    public boolean shouldEnterWater() {
        return (this.m_21205_().isEmpty() || this.m_21205_().getItem() != Items.WATER_BUCKET) && !this.isSitting();
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.m_21205_().getItem() == Items.WATER_BUCKET;
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isSitting();
    }

    @Override
    public int getWaterSearchRange() {
        return 16;
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    protected void updateAir(int p_209207_1_) {
    }

    public static class FollowOwner extends Goal {

        private final EntityMantisShrimp tameable;

        private final LevelReader world;

        private final double followSpeed;

        private final float maxDist;

        private final float minDist;

        private final boolean teleportToLeaves;

        private LivingEntity owner;

        private int timeToRecalcPath;

        private float oldWaterCost;

        public FollowOwner(EntityMantisShrimp p_i225711_1_, double p_i225711_2_, float p_i225711_4_, float p_i225711_5_, boolean p_i225711_6_) {
            this.tameable = p_i225711_1_;
            this.world = p_i225711_1_.m_9236_();
            this.followSpeed = p_i225711_2_;
            this.minDist = p_i225711_4_;
            this.maxDist = p_i225711_5_;
            this.teleportToLeaves = p_i225711_6_;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity lvt_1_1_ = this.tameable.m_269323_();
            if (lvt_1_1_ == null) {
                return false;
            } else if (lvt_1_1_.m_5833_()) {
                return false;
            } else if (this.tameable.isSitting() || this.tameable.getCommand() != 1) {
                return false;
            } else if (this.tameable.m_20280_(lvt_1_1_) < (double) (this.minDist * this.minDist)) {
                return false;
            } else if (this.tameable.m_5448_() != null && this.tameable.m_5448_().isAlive()) {
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
            } else if (this.tameable.isSitting() || this.tameable.getCommand() != 1) {
                return false;
            } else {
                return this.tameable.m_5448_() != null && this.tameable.m_5448_().isAlive() ? false : this.tameable.m_20280_(this.owner) > (double) (this.maxDist * this.maxDist);
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
            if (!this.world.m_6425_(p_226329_1_).is(FluidTags.WATER) && (this.world.m_6425_(p_226329_1_).is(FluidTags.WATER) || !this.world.m_6425_(p_226329_1_.below()).is(FluidTags.WATER))) {
                if (lvt_2_1_ == BlockPathTypes.WALKABLE && this.tameable.getMoistness() >= 2000) {
                    BlockState lvt_3_1_ = this.world.m_8055_(p_226329_1_.below());
                    if (!this.teleportToLeaves && lvt_3_1_.m_60734_() instanceof LeavesBlock) {
                        return false;
                    } else {
                        BlockPos lvt_4_1_ = p_226329_1_.subtract(this.tameable.m_20183_());
                        return this.world.m_45756_(this.tameable, this.tameable.m_20191_().move(lvt_4_1_));
                    }
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

        private int getRandomNumber(int p_226327_1_, int p_226327_2_) {
            return this.tameable.m_217043_().nextInt(p_226327_2_ - p_226327_1_ + 1) + p_226327_1_;
        }
    }
}