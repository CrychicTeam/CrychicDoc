package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalSwimMoveControllerSink;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIFollowOwnerWater;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityMimicOctopus extends TamableAnimal implements ISemiAquatic, IFollower, Bucketable {

    private static final EntityDataAccessor<Boolean> STOP_CHANGE = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> UPGRADED = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> MIMIC_ORDINAL = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> PREV_MIMIC_ORDINAL = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> MOISTNESS = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Optional<BlockState>> MIMICKED_BLOCK = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);

    private static final EntityDataAccessor<Optional<BlockState>> PREV_MIMICKED_BLOCK = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> LAST_SCARED_MOB_ID = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> UPGRADED_LASER_ENTITY_ID = SynchedEntityData.defineId(EntityMimicOctopus.class, EntityDataSerializers.INT);

    public EntityMimicOctopus.MimicState localMimicState = EntityMimicOctopus.MimicState.OVERLAY;

    public float transProgress = 0.0F;

    public float prevTransProgress = 0.0F;

    public float colorShiftProgress = 0.0F;

    public float prevColorShiftProgress = 0.0F;

    public float groundProgress = 5.0F;

    public float prevGroundProgress = 0.0F;

    public float sitProgress = 0.0F;

    public float prevSitProgress = 0.0F;

    private boolean isLandNavigator;

    private int moistureAttackTime = 0;

    private int camoCooldown = 120 + this.f_19796_.nextInt(1200);

    private int mimicCooldown = 0;

    private int stopMimicCooldown = -1;

    private int fishFeedings;

    private int mimicreamFeedings;

    private int exclaimTime = 0;

    private BlockState localMimic;

    private LivingEntity laserTargetEntity;

    private int guardianLaserTime;

    protected EntityMimicOctopus(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(false);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.ARMOR, 0.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    public static boolean canMimicOctopusSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockPos downPos = pos;
        while (downPos.m_123342_() > 1 && !worldIn.m_6425_(downPos).isEmpty()) {
            downPos = downPos.below();
        }
        boolean spawnBlock = worldIn.m_8055_(downPos).m_204336_(AMTagRegistry.MIMIC_OCTOPUS_SPAWNS);
        return spawnBlock && downPos.m_123342_() < worldIn.m_5736_() + 1;
    }

    public static EntityMimicOctopus.MimicState getStateForItem(ItemStack stack) {
        if (stack.is(AMTagRegistry.MIMIC_OCTOPUS_CREEPER_ITEMS)) {
            return EntityMimicOctopus.MimicState.CREEPER;
        } else if (stack.is(AMTagRegistry.MIMIC_OCTOPUS_GUARDIAN_ITEMS)) {
            return EntityMimicOctopus.MimicState.GUARDIAN;
        } else {
            return stack.is(AMTagRegistry.MIMIC_OCTOPUS_PUFFERFISH_ITEMS) ? EntityMimicOctopus.MimicState.PUFFERFISH : null;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.MIMIC_OCTOPUS_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.MIMIC_OCTOPUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.MIMIC_OCTOPUS_HURT.get();
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.mimicOctopusSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.f_19804_.set(PREV_MIMIC_ORDINAL, 0);
        this.setMimickedBlock(null);
        this.setMimicState(EntityMimicOctopus.MimicState.OVERLAY);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.f_19804_.set(MIMIC_ORDINAL, compound.getInt("MimicState"));
        this.setUpgraded(compound.getBoolean("Upgraded"));
        this.setOrderedToSit(compound.getBoolean("Sitting"));
        this.setStopChange(compound.getBoolean("StopChange"));
        this.setCommand(compound.getInt("OctoCommand"));
        this.setMoistness(compound.getInt("Moistness"));
        this.setFromBucket(compound.getBoolean("FromBucket"));
        BlockState blockstate = null;
        if (compound.contains("MimickedBlockState", 10)) {
            blockstate = NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), compound.getCompound("MimickedBlockState"));
            if (blockstate.m_60795_()) {
                blockstate = null;
            }
        }
        this.setMimickedBlock(blockstate);
        this.camoCooldown = compound.getInt("CamoCooldown");
        this.mimicCooldown = compound.getInt("MimicCooldown");
        this.stopMimicCooldown = compound.getInt("StopMimicCooldown");
        this.fishFeedings = compound.getInt("FishFeedings");
        this.mimicreamFeedings = compound.getInt("MimicreamFeedings");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MimicState", this.getMimicState().ordinal());
        compound.putBoolean("Upgraded", this.isUpgraded());
        compound.putBoolean("Sitting", this.isSitting());
        compound.putInt("OctoCommand", this.getCommand());
        compound.putInt("Moistness", this.getMoistness());
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putBoolean("StopChange", this.isStopChange());
        BlockState blockstate = this.getMimickedBlock();
        if (blockstate != null) {
            compound.put("MimickedBlockState", NbtUtils.writeBlockState(blockstate));
        }
        compound.putInt("CamoCooldown", this.camoCooldown);
        compound.putInt("MimicCooldown", this.mimicCooldown);
        compound.putInt("StopMimicCooldown", this.stopMimicCooldown);
        compound.putInt("FishFeedings", this.fishFeedings);
        compound.putInt("MimicreamFeedings", this.mimicreamFeedings);
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.MIMIC_OCTOPUS_BUCKET.get());
        if (this.m_8077_()) {
            stack.setHoverName(this.m_7770_());
        }
        return stack;
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        CompoundTag platTag = new CompoundTag();
        this.addAdditionalSaveData(platTag);
        CompoundTag compound = bucket.getOrCreateTag();
        compound.put("MimicOctopusData", platTag);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        if (compound.contains("MimicOctopusData")) {
            this.readAdditionalSaveData(compound.getCompound("MimicOctopusData"));
        }
        this.setMoistness(60000);
    }

    @Override
    protected float getJumpPower() {
        return super.m_6118_() * (this.m_20072_() ? 1.3F : 1.0F);
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
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
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new EntityMimicOctopus.AIAttack());
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new TameableAIFollowOwnerWater(this, 1.3, 4.0F, 2.0F, false));
        this.f_21345_.addGoal(3, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(3, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.0, Ingredient.of(AMItemRegistry.LOBSTER_TAIL.get(), AMItemRegistry.COOKED_LOBSTER_TAIL.get(), Items.TROPICAL_FISH), false) {

            @Override
            public void tick() {
                EntityMimicOctopus.this.setMimickedBlock(null);
                super.tick();
                EntityMimicOctopus.this.camoCooldown = 40;
                EntityMimicOctopus.this.stopMimicCooldown = 40;
            }
        });
        this.f_21345_.addGoal(5, new EntityMimicOctopus.AIFlee());
        this.f_21345_.addGoal(7, new BreedGoal(this, 0.8));
        this.f_21345_.addGoal(8, new EntityMimicOctopus.AIMimicNearbyMobs());
        this.f_21345_.addGoal(9, new BreedGoal(this, 0.8));
        this.f_21345_.addGoal(10, new EntityMimicOctopus.AISwim());
        this.f_21345_.addGoal(11, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this) {

            @Override
            public boolean canUse() {
                return EntityMimicOctopus.this.m_21824_() && super.canUse();
            }
        });
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return this.m_21824_() && item == Items.TROPICAL_FISH;
    }

    public boolean isActiveCamo() {
        return this.getMimicState() == EntityMimicOctopus.MimicState.OVERLAY && this.getMimickedBlock() != null;
    }

    @Override
    public double getVisibilityPercent(@Nullable Entity lookingEntity) {
        return this.isActiveCamo() ? super.m_20968_(lookingEntity) * 0.1F : super.m_20968_(lookingEntity);
    }

    @Nonnull
    @Override
    public InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        EntityMimicOctopus.MimicState readState = getStateForItem(itemstack);
        InteractionResult type = super.m_6071_(player, hand);
        if (readState != null && this.m_21824_()) {
            if (this.mimicCooldown == 0) {
                this.setMimicState(readState);
                this.mimicCooldown = 20;
                this.stopMimicCooldown = this.isUpgraded() ? 120 : 1200;
                this.camoCooldown = this.stopMimicCooldown;
                this.setMimickedBlock(null);
            }
            return InteractionResult.SUCCESS;
        } else {
            boolean tame = this.m_21824_();
            if (tame && item == Items.INK_SAC) {
                this.setStopChange(!this.isStopChange());
                if (this.isStopChange()) {
                    this.makeEatingParticles(itemstack);
                } else {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                    this.mimicEnvironment();
                }
                return InteractionResult.SUCCESS;
            } else if (tame || item != AMItemRegistry.LOBSTER_TAIL.get() && item != AMItemRegistry.COOKED_LOBSTER_TAIL.get()) {
                if (!tame || item != AMItemRegistry.LOBSTER_TAIL.get() && item != AMItemRegistry.COOKED_LOBSTER_TAIL.get()) {
                    if (tame) {
                        Optional<InteractionResult> result = Bucketable.bucketMobPickup(player, hand, this);
                        if (result.isPresent()) {
                            return (InteractionResult) result.get();
                        }
                        if (item == Items.SLIME_BALL && this.getMoistness() < 24000) {
                            this.setMoistness(48000);
                            this.makeEatingParticles(itemstack);
                            this.m_142075_(player, hand, itemstack);
                            return InteractionResult.SUCCESS;
                        }
                        if (!this.isUpgraded() && item == AMItemRegistry.MIMICREAM.get()) {
                            this.mimicreamFeedings++;
                            if (this.mimicreamFeedings > 5 || this.mimicreamFeedings > 2 && this.f_19796_.nextInt(2) == 0) {
                                this.m_9236_().broadcastEntityEvent(this, (byte) 46);
                                this.setUpgraded(true);
                                this.setMimicState(EntityMimicOctopus.MimicState.MIMICUBE);
                                this.setStopChange(false);
                                this.setMimickedBlock(null);
                                this.stopMimicCooldown = 40;
                            }
                            this.makeEatingParticles(itemstack);
                            this.m_142075_(player, hand, itemstack);
                            return InteractionResult.SUCCESS;
                        }
                    }
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
                } else if (this.m_21223_() < this.m_21233_()) {
                    this.m_142075_(player, hand, itemstack);
                    this.m_146850_(GameEvent.EAT);
                    this.m_5496_(SoundEvents.DOLPHIN_EAT, this.m_6121_(), this.m_6100_());
                    this.m_5634_(5.0F);
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                this.m_142075_(player, hand, itemstack);
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.DOLPHIN_EAT, this.m_6121_(), this.m_6100_());
                this.fishFeedings++;
                if (this.getMimicState() == EntityMimicOctopus.MimicState.OVERLAY && this.getMimickedBlock() == null) {
                    if ((this.fishFeedings <= 5 || this.m_217043_().nextInt(2) != 0) && this.fishFeedings <= 8) {
                        this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                    } else {
                        this.m_21828_(player);
                        this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    private void makeEatingParticles(ItemStack item) {
        for (int i = 0; i < 6 + this.f_19796_.nextInt(3); i++) {
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, item), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
        }
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * (this.groundProgress < 2.5F ? 4.0F : 8.0F), 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AnimalSwimMoveControllerSink(this, 1.3F, 1.0F);
            this.f_21344_ = new SemiAquaticPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.localMimic != this.getPrevMimickedBlock()) {
            this.localMimic = this.getPrevMimickedBlock();
            this.colorShiftProgress = 0.0F;
        }
        if (this.localMimicState != this.getPrevMimicState()) {
            this.localMimicState = this.getPrevMimicState();
            this.transProgress = 0.0F;
        }
        if (this.m_20069_()) {
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
        } else if (!this.isLandNavigator) {
            this.switchNavigator(true);
        }
        BlockPos pos = AMBlockPos.fromCoords(this.m_20185_(), this.m_20188_() - 1.0, this.m_20189_());
        boolean ground = this.m_9236_().getBlockState(pos).m_60783_(this.m_9236_(), pos, Direction.UP) && this.getMimicState() != EntityMimicOctopus.MimicState.GUARDIAN || !this.m_20072_() || this.isSitting();
        this.prevTransProgress = this.transProgress;
        this.prevColorShiftProgress = this.colorShiftProgress;
        this.prevGroundProgress = this.groundProgress;
        this.prevSitProgress = this.sitProgress;
        if (this.getPrevMimicState() != this.getMimicState() && this.transProgress < 5.0F) {
            this.transProgress += 0.25F;
        }
        if (this.getPrevMimicState() == this.getMimicState() && this.transProgress > 0.0F) {
            this.transProgress -= 0.25F;
        }
        if (this.getPrevMimickedBlock() != this.getMimickedBlock() && this.colorShiftProgress < 5.0F) {
            this.colorShiftProgress += 0.25F;
        }
        if (this.getPrevMimickedBlock() == this.getMimickedBlock() && this.colorShiftProgress > 0.0F) {
            this.colorShiftProgress -= 0.25F;
        }
        if (ground && this.groundProgress < 5.0F) {
            this.groundProgress += 0.5F;
        }
        if (!ground && this.groundProgress > 0.0F) {
            this.groundProgress -= 0.5F;
        }
        if (this.isSitting() && this.sitProgress < 5.0F) {
            this.sitProgress += 0.5F;
        }
        if (!this.isSitting() && this.sitProgress > 0.0F) {
            this.sitProgress -= 0.5F;
        }
        if (this.m_20072_()) {
            float f2 = (float) (-((double) ((float) this.m_20184_().y * 3.0F) * 180.0F / (float) Math.PI));
            this.m_146926_(f2);
        }
        if (this.camoCooldown > 0) {
            this.camoCooldown--;
        }
        if (this.mimicCooldown > 0) {
            this.mimicCooldown--;
        }
        if (this.stopMimicCooldown > 0) {
            this.stopMimicCooldown--;
        }
        if (this.m_21525_()) {
            this.m_20301_(this.m_6062_());
        } else if (!this.m_20071_() && this.m_21205_().getItem() != Items.WATER_BUCKET) {
            this.setMoistness(this.getMoistness() - 1);
            if (this.getMoistness() <= 0 && this.moistureAttackTime-- <= 0) {
                this.setOrderedToSit(false);
                this.m_6469_(this.m_269291_().dryOut(), this.f_19796_.nextInt(2) == 0 ? 1.0F : 0.0F);
                this.moistureAttackTime = 20;
            }
        } else {
            this.setMoistness(60000);
        }
        if (this.camoCooldown <= 0 && this.f_19796_.nextInt(300) == 0) {
            this.mimicEnvironment();
            this.camoCooldown = this.m_217043_().nextInt(2200) + 200;
        }
        if ((this.getMimicState() != EntityMimicOctopus.MimicState.OVERLAY || this.getMimickedBlock() != null) && this.stopMimicCooldown == 0 && !this.isStopChange()) {
            this.setMimicState(EntityMimicOctopus.MimicState.OVERLAY);
            this.setMimickedBlock(null);
            this.stopMimicCooldown = -1;
        }
        if (this.m_9236_().isClientSide && this.exclaimTime > 0) {
            this.exclaimTime--;
            if (this.exclaimTime == 0) {
                Entity e = this.m_9236_().getEntity(this.f_19804_.get(LAST_SCARED_MOB_ID));
                if (e != null && this.transProgress >= 5.0F) {
                    double d2 = this.f_19796_.nextGaussian() * 0.1;
                    double d0 = this.f_19796_.nextGaussian() * 0.1;
                    double d1 = this.f_19796_.nextGaussian() * 0.1;
                    this.m_9236_().addParticle(AMParticleRegistry.SHOCKED.get(), e.getX(), e.getEyeY() + (double) (e.getBbHeight() * 0.15F) + (double) (this.f_19796_.nextFloat() * e.getBbHeight() * 0.15F), e.getZ(), d0, d1, d2);
                }
            }
        }
        if (this.hasGuardianLaser()) {
            if (this.guardianLaserTime < 30) {
                this.guardianLaserTime++;
            }
            LivingEntity livingentity = this.getGuardianLaser();
            if (livingentity != null && this.m_20072_()) {
                this.m_21563_().setLookAt(livingentity, 90.0F, 90.0F);
                this.m_21563_().tick();
                double d5 = (double) this.getLaserAttackAnimationScale(0.0F);
                double d0 = livingentity.m_20185_() - this.m_20185_();
                double d1 = livingentity.m_20227_(0.5) - this.m_20188_();
                double d2 = livingentity.m_20189_() - this.m_20189_();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d0 /= d3;
                d1 /= d3;
                d2 /= d3;
                double d4 = this.f_19796_.nextDouble();
                while (d4 < d3) {
                    d4 += 1.8 - d5 + this.f_19796_.nextDouble() * (1.7 - d5);
                    this.m_9236_().addParticle(ParticleTypes.BUBBLE, this.m_20185_() + d0 * d4, this.m_20188_() + d1 * d4, this.m_20189_() + d2 * d4, 0.0, 0.0, 0.0);
                }
                if (this.guardianLaserTime == 30) {
                    livingentity.hurt(this.m_269291_().mobAttack(this), 5.0F);
                    this.guardianLaserTime = 0;
                    this.f_19804_.set(UPGRADED_LASER_ENTITY_ID, -1);
                }
            }
        }
        if (!this.m_9236_().isClientSide && this.f_19797_ % 40 == 0) {
            this.m_5634_(2.0F);
        }
    }

    public float getLaserAttackAnimationScale(float p_175477_1_) {
        return ((float) this.guardianLaserTime + p_175477_1_) / 30.0F;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 68) {
            if (this.exclaimTime == 0) {
                this.exclaimTime = 20;
            }
        } else if (id == 69) {
            this.creeperExplode();
        } else {
            super.handleEntityEvent(id);
        }
    }

    public void mimicEnvironment() {
        if (!this.isStopChange()) {
            BlockPos down = this.getPositionDown();
            if (!this.m_9236_().m_46859_(down)) {
                this.setMimicState(EntityMimicOctopus.MimicState.OVERLAY);
                this.setMimickedBlock(this.m_9236_().getBlockState(down));
            }
            this.stopMimicCooldown = this.m_217043_().nextInt(2200);
        }
    }

    public int getMoistness() {
        return this.f_19804_.get(MOISTNESS);
    }

    public void setMoistness(int p_211137_1_) {
        this.f_19804_.set(MOISTNESS, p_211137_1_);
    }

    private BlockPos getPositionDown() {
        BlockPos pos = AMBlockPos.fromCoords(this.m_20185_(), this.m_20188_(), this.m_20189_());
        while (pos.m_123342_() > 1 && (this.m_9236_().m_46859_(pos) || this.m_9236_().m_46801_(pos))) {
            pos = pos.below();
        }
        return pos;
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

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean sit) {
        this.f_19804_.set(FROM_BUCKET, sit);
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    public boolean isUpgraded() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    public void setUpgraded(boolean sit) {
        this.f_19804_.set(FROM_BUCKET, sit);
    }

    public boolean isStopChange() {
        return this.f_19804_.get(STOP_CHANGE);
    }

    public void setStopChange(boolean sit) {
        this.f_19804_.set(STOP_CHANGE, sit);
    }

    public boolean hasGuardianLaser() {
        return this.f_19804_.get(UPGRADED_LASER_ENTITY_ID) != -1 && this.isUpgraded() && this.m_20072_();
    }

    @Nullable
    public LivingEntity getGuardianLaser() {
        if (!this.hasGuardianLaser()) {
            return null;
        } else if (this.m_9236_().isClientSide) {
            if (this.laserTargetEntity != null) {
                return this.laserTargetEntity;
            } else {
                Entity lvt_1_1_ = this.m_9236_().getEntity(this.f_19804_.get(UPGRADED_LASER_ENTITY_ID));
                if (lvt_1_1_ instanceof LivingEntity) {
                    this.laserTargetEntity = (LivingEntity) lvt_1_1_;
                    return this.laserTargetEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.m_5448_();
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.MIMIC_OCTOPUS.get().create(serverWorld);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket() || this.m_21824_();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.m_21824_() && !this.fromBucket();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(MIMIC_ORDINAL, 0);
        this.f_19804_.define(PREV_MIMIC_ORDINAL, -1);
        this.f_19804_.define(MOISTNESS, 60000);
        this.f_19804_.define(MIMICKED_BLOCK, Optional.empty());
        this.f_19804_.define(PREV_MIMICKED_BLOCK, Optional.empty());
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(LAST_SCARED_MOB_ID, -1);
        this.f_19804_.define(FROM_BUCKET, false);
        this.f_19804_.define(UPGRADED, false);
        this.f_19804_.define(STOP_CHANGE, false);
        this.f_19804_.define(UPGRADED_LASER_ENTITY_ID, -1);
    }

    public EntityMimicOctopus.MimicState getMimicState() {
        return EntityMimicOctopus.MimicState.values()[Mth.clamp(this.f_19804_.get(MIMIC_ORDINAL), 0, 4)];
    }

    public void setMimicState(EntityMimicOctopus.MimicState state) {
        if (this.getMimicState() != state) {
            this.f_19804_.set(PREV_MIMIC_ORDINAL, this.f_19804_.get(MIMIC_ORDINAL));
        }
        this.f_19804_.set(MIMIC_ORDINAL, state.ordinal());
    }

    public EntityMimicOctopus.MimicState getPrevMimicState() {
        return this.f_19804_.get(PREV_MIMIC_ORDINAL) == -1 ? null : EntityMimicOctopus.MimicState.values()[Mth.clamp(this.f_19804_.get(PREV_MIMIC_ORDINAL), 0, 4)];
    }

    @Nullable
    public BlockState getMimickedBlock() {
        return (BlockState) this.f_19804_.get(MIMICKED_BLOCK).orElse(null);
    }

    public void setMimickedBlock(@Nullable BlockState state) {
        if (this.getMimickedBlock() != state) {
            this.f_19804_.set(PREV_MIMICKED_BLOCK, Optional.ofNullable(this.getMimickedBlock()));
        }
        this.f_19804_.set(MIMICKED_BLOCK, Optional.ofNullable(state));
    }

    @Nullable
    public BlockState getPrevMimickedBlock() {
        return (BlockState) this.f_19804_.get(PREV_MIMICKED_BLOCK).orElse(null);
    }

    protected void updateAir(int p_209207_1_) {
        if (this.m_6084_() && !this.m_20072_()) {
            this.m_20301_(p_209207_1_ - 1);
            if (this.m_20146_() == -20) {
                this.m_20301_(0);
                this.m_6469_(this.m_269291_().dryOut(), 2.0F);
            }
        } else {
            this.m_20301_(1200);
        }
    }

    @Override
    public boolean shouldEnterWater() {
        return !this.isSitting() && (this.m_5448_() == null || this.m_5448_().m_20072_());
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.m_5448_() != null && !this.m_5448_().m_20072_();
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isSitting();
    }

    @Override
    public int getWaterSearchRange() {
        return 16;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = -9.45F - (float) this.m_217043_().nextInt(24) - radiusAdd;
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), 0, (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getOctopusGround(radialPos);
        return ground != null ? Vec3.atCenterOf(ground) : null;
    }

    private BlockPos getOctopusGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int) this.m_20186_(), in.m_123343_());
        while (position.m_123342_() > 2 && this.m_9236_().getFluidState(position).is(FluidTags.WATER)) {
            position = position.below();
        }
        return position;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.m_7350_(key);
        if (UPGRADED_LASER_ENTITY_ID.equals(key)) {
            this.guardianLaserTime = 0;
            this.laserTargetEntity = null;
        }
    }

    private void creeperExplode() {
        Explosion explosion = new Explosion(this.m_9236_(), this, this.m_269291_().mobAttack(this), (ExplosionDamageCalculator) null, this.m_20185_(), this.m_20186_(), this.m_20189_(), 1.0F + this.f_19796_.nextFloat(), false, Explosion.BlockInteraction.KEEP);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }

    private class AIAttack extends Goal {

        private int executionCooldown = 0;

        private int scareMobTime = 0;

        private Vec3 fleePosition = null;

        public AIAttack() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.executionCooldown > 0) {
                EntityMimicOctopus.this.f_19804_.set(EntityMimicOctopus.UPGRADED_LASER_ENTITY_ID, -1);
                this.executionCooldown--;
            }
            return EntityMimicOctopus.this.isStopChange() && EntityMimicOctopus.this.getMimicState() == EntityMimicOctopus.MimicState.OVERLAY ? false : this.executionCooldown == 0 && EntityMimicOctopus.this.m_21824_() && EntityMimicOctopus.this.m_5448_() != null && EntityMimicOctopus.this.m_5448_().isAlive();
        }

        @Override
        public void stop() {
            this.fleePosition = null;
            this.scareMobTime = 0;
            this.executionCooldown = 100 + EntityMimicOctopus.this.f_19796_.nextInt(200);
            if (EntityMimicOctopus.this.isUpgraded()) {
                this.executionCooldown = 30;
            } else {
                EntityMimicOctopus.this.m_6703_(null);
                EntityMimicOctopus.this.m_6710_(null);
            }
            if (EntityMimicOctopus.this.stopMimicCooldown <= 0) {
                EntityMimicOctopus.this.mimicEnvironment();
            }
            EntityMimicOctopus.this.f_19804_.set(EntityMimicOctopus.UPGRADED_LASER_ENTITY_ID, -1);
        }

        public Vec3 generateFleePosition(LivingEntity fleer) {
            for (int i = 0; i < 15; i++) {
                BlockPos pos = fleer.m_20183_().offset(EntityMimicOctopus.this.f_19796_.nextInt(32) - 16, EntityMimicOctopus.this.f_19796_.nextInt(16), EntityMimicOctopus.this.f_19796_.nextInt(32) - 16);
                while (fleer.m_9236_().m_46859_(pos) && pos.m_123342_() > 1) {
                    pos = pos.below();
                }
                if (!(fleer instanceof PathfinderMob)) {
                    return Vec3.atCenterOf(pos);
                }
                if (((PathfinderMob) fleer).getWalkTargetValue(pos) >= 0.0F) {
                    return Vec3.atCenterOf(pos);
                }
            }
            return null;
        }

        @Override
        public void tick() {
            LivingEntity target = EntityMimicOctopus.this.m_5448_();
            if (target != null) {
                if (this.scareMobTime > 0) {
                    if (this.fleePosition == null || target.m_20238_(this.fleePosition) < (double) (target.m_20205_() * target.m_20205_() * 2.0F)) {
                        this.fleePosition = this.generateFleePosition(target);
                    }
                    if (target instanceof Mob && this.fleePosition != null) {
                        ((Mob) target).getNavigation().moveTo(this.fleePosition.x, this.fleePosition.y, this.fleePosition.z, 1.5);
                        ((Mob) target).getMoveControl().setWantedPosition(this.fleePosition.x, this.fleePosition.y, this.fleePosition.z, 1.5);
                        ((Mob) target).setTarget(null);
                    }
                    EntityMimicOctopus.this.camoCooldown = Math.max(EntityMimicOctopus.this.camoCooldown, 20);
                    EntityMimicOctopus.this.stopMimicCooldown = Math.max(EntityMimicOctopus.this.stopMimicCooldown, 20);
                    this.scareMobTime--;
                    if (this.scareMobTime == 0) {
                        this.stop();
                        return;
                    }
                }
                double dist = (double) EntityMimicOctopus.this.m_20270_(target);
                boolean move = true;
                if (dist < 7.0 && EntityMimicOctopus.this.m_142582_(target) && EntityMimicOctopus.this.getMimicState() == EntityMimicOctopus.MimicState.GUARDIAN && EntityMimicOctopus.this.isUpgraded()) {
                    EntityMimicOctopus.this.f_19804_.set(EntityMimicOctopus.UPGRADED_LASER_ENTITY_ID, target.m_19879_());
                    move = false;
                }
                if (dist < 3.0) {
                    EntityMimicOctopus.this.f_19804_.set(EntityMimicOctopus.LAST_SCARED_MOB_ID, target.m_19879_());
                    if (move) {
                        move = EntityMimicOctopus.this.isUpgraded() && dist > 2.0;
                    }
                    EntityMimicOctopus.this.m_21573_().stop();
                    if (!EntityMimicOctopus.this.isStopChange()) {
                        EntityMimicOctopus.this.setMimickedBlock(null);
                        EntityMimicOctopus.MimicState prev = EntityMimicOctopus.this.getMimicState();
                        if (EntityMimicOctopus.this.m_20072_()) {
                            if (prev != EntityMimicOctopus.MimicState.GUARDIAN && prev != EntityMimicOctopus.MimicState.PUFFERFISH) {
                                if (EntityMimicOctopus.this.f_19796_.nextBoolean()) {
                                    EntityMimicOctopus.this.setMimicState(EntityMimicOctopus.MimicState.GUARDIAN);
                                } else {
                                    EntityMimicOctopus.this.setMimicState(EntityMimicOctopus.MimicState.PUFFERFISH);
                                }
                            }
                        } else {
                            EntityMimicOctopus.this.setMimicState(EntityMimicOctopus.MimicState.CREEPER);
                        }
                    }
                    if (EntityMimicOctopus.this.getMimicState() != EntityMimicOctopus.MimicState.OVERLAY) {
                        EntityMimicOctopus.this.mimicCooldown = 40;
                        EntityMimicOctopus.this.stopMimicCooldown = Math.max(EntityMimicOctopus.this.stopMimicCooldown, 60);
                    }
                    if (EntityMimicOctopus.this.isUpgraded() && EntityMimicOctopus.this.transProgress >= 5.0F) {
                        if (EntityMimicOctopus.this.getMimicState() == EntityMimicOctopus.MimicState.PUFFERFISH && EntityMimicOctopus.this.m_20191_().expandTowards(2.0, 1.3, 2.0).intersects(target.m_20191_())) {
                            target.hurt(EntityMimicOctopus.this.m_269291_().mobAttack(EntityMimicOctopus.this), 4.0F);
                            target.addEffect(new MobEffectInstance(MobEffects.POISON, 400, 2));
                        }
                        if (EntityMimicOctopus.this.getMimicState() == EntityMimicOctopus.MimicState.GUARDIAN) {
                            if (EntityMimicOctopus.this.m_20191_().expandTowards(1.0, 1.0, 1.0).intersects(target.m_20191_())) {
                                target.hurt(EntityMimicOctopus.this.m_269291_().mobAttack(EntityMimicOctopus.this), 1.0F);
                            }
                            EntityMimicOctopus.this.f_19804_.set(EntityMimicOctopus.UPGRADED_LASER_ENTITY_ID, target.m_19879_());
                        }
                        if (EntityMimicOctopus.this.getMimicState() == EntityMimicOctopus.MimicState.CREEPER) {
                            EntityMimicOctopus.this.creeperExplode();
                            EntityMimicOctopus.this.m_9236_().broadcastEntityEvent(EntityMimicOctopus.this, (byte) 69);
                            this.executionCooldown = 300;
                        }
                    }
                    if (this.scareMobTime == 0) {
                        EntityMimicOctopus.this.m_9236_().broadcastEntityEvent(EntityMimicOctopus.this, (byte) 68);
                        this.scareMobTime = 60 + EntityMimicOctopus.this.f_19796_.nextInt(60);
                    }
                }
                if (move) {
                    EntityMimicOctopus.this.m_21391_(target, 30.0F, 30.0F);
                    EntityMimicOctopus.this.m_21573_().moveTo(target, 1.2F);
                }
            }
        }
    }

    private class AIFlee extends Goal {

        protected final EntityMimicOctopus.EntitySorter theNearestAttackableTargetSorter;

        protected final Predicate<? super Entity> targetEntitySelector;

        protected int executionChance = 8;

        protected boolean mustUpdate;

        private Entity targetEntity;

        private Vec3 flightTarget = null;

        private int cooldown = 0;

        AIFlee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.theNearestAttackableTargetSorter = new EntityMimicOctopus.EntitySorter(EntityMimicOctopus.this);
            this.targetEntitySelector = new Predicate<Entity>() {

                public boolean apply(@Nullable Entity e) {
                    return e.isAlive() && e.getType().is(AMTagRegistry.MIMIC_OCTOPUS_FEARS) || e instanceof Player && !((Player) e).isCreative();
                }
            };
        }

        @Override
        public boolean canUse() {
            if (!EntityMimicOctopus.this.m_20159_() && !EntityMimicOctopus.this.m_20160_() && !EntityMimicOctopus.this.m_21824_()) {
                if (!this.mustUpdate) {
                    long worldTime = EntityMimicOctopus.this.m_9236_().getGameTime() % 10L;
                    if (EntityMimicOctopus.this.m_21216_() >= 100 && worldTime != 0L) {
                        return false;
                    }
                    if (EntityMimicOctopus.this.m_217043_().nextInt(this.executionChance) != 0 && worldTime != 0L) {
                        return false;
                    }
                }
                List<Entity> list = EntityMimicOctopus.this.m_9236_().m_6443_(Entity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
                if (list.isEmpty()) {
                    return false;
                } else {
                    Collections.sort(list, this.theNearestAttackableTargetSorter);
                    this.targetEntity = (Entity) list.get(0);
                    this.mustUpdate = false;
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.targetEntity != null && !EntityMimicOctopus.this.m_21824_() && EntityMimicOctopus.this.m_20270_(this.targetEntity) < 20.0F;
        }

        @Override
        public void stop() {
            this.flightTarget = null;
            this.targetEntity = null;
            EntityMimicOctopus.this.setMimicState(EntityMimicOctopus.MimicState.OVERLAY);
            EntityMimicOctopus.this.setMimickedBlock(null);
        }

        @Override
        public void tick() {
            if (this.cooldown > 0) {
                this.cooldown--;
            }
            if (!EntityMimicOctopus.this.isActiveCamo()) {
                EntityMimicOctopus.this.mimicEnvironment();
            }
            if (this.flightTarget != null) {
                EntityMimicOctopus.this.m_21573_().moveTo(this.flightTarget.x, this.flightTarget.y, this.flightTarget.z, 1.2F);
                if (this.cooldown == 0 && EntityMimicOctopus.this.isTargetBlocked(this.flightTarget)) {
                    this.cooldown = 30;
                    this.flightTarget = null;
                }
            }
            if (this.targetEntity != null) {
                if (this.flightTarget == null || this.flightTarget != null && EntityMimicOctopus.this.m_20238_(this.flightTarget) < 6.0) {
                    Vec3 vec = DefaultRandomPos.getPosAway(EntityMimicOctopus.this, 16, 7, this.targetEntity.position());
                    if (vec != null) {
                        this.flightTarget = vec;
                    }
                }
                if (EntityMimicOctopus.this.m_20270_(this.targetEntity) > 20.0F) {
                    this.stop();
                }
            }
        }

        protected double getTargetDistance() {
            return 10.0;
        }

        protected AABB getTargetableArea(double targetDistance) {
            Vec3 renderCenter = new Vec3(EntityMimicOctopus.this.m_20185_(), EntityMimicOctopus.this.m_20186_() + 0.5, EntityMimicOctopus.this.m_20189_());
            AABB aabb = new AABB(-targetDistance, -targetDistance, -targetDistance, targetDistance, targetDistance, targetDistance);
            return aabb.move(renderCenter);
        }
    }

    private class AIMimicNearbyMobs extends Goal {

        protected final EntityMimicOctopus.EntitySorter theNearestAttackableTargetSorter;

        protected final Predicate<? super Entity> targetEntitySelector;

        protected int executionChance = 30;

        protected boolean mustUpdate;

        private Entity targetEntity;

        private Vec3 flightTarget = null;

        private int cooldown = 0;

        AIMimicNearbyMobs() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.theNearestAttackableTargetSorter = new EntityMimicOctopus.EntitySorter(EntityMimicOctopus.this);
            this.targetEntitySelector = new Predicate<Entity>() {

                public boolean apply(@Nullable Entity e) {
                    return e.isAlive() && (e instanceof Creeper || e instanceof Guardian || e instanceof Pufferfish);
                }
            };
        }

        @Override
        public boolean canUse() {
            if (!EntityMimicOctopus.this.m_20159_() && !EntityMimicOctopus.this.m_20160_() && EntityMimicOctopus.this.getMimicState() == EntityMimicOctopus.MimicState.OVERLAY && EntityMimicOctopus.this.mimicCooldown <= 0) {
                if (!this.mustUpdate) {
                    long worldTime = EntityMimicOctopus.this.m_9236_().getGameTime() % 10L;
                    if (EntityMimicOctopus.this.m_21216_() >= 100 && worldTime != 0L) {
                        return false;
                    }
                    if (EntityMimicOctopus.this.m_217043_().nextInt(this.executionChance) != 0 && worldTime != 0L) {
                        return false;
                    }
                }
                List<Entity> list = EntityMimicOctopus.this.m_9236_().m_6443_(Entity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
                if (list.isEmpty()) {
                    return false;
                } else {
                    Collections.sort(list, this.theNearestAttackableTargetSorter);
                    this.targetEntity = (Entity) list.get(0);
                    this.mustUpdate = false;
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.targetEntity != null && EntityMimicOctopus.this.m_20270_(this.targetEntity) < 10.0F && EntityMimicOctopus.this.getMimicState() == EntityMimicOctopus.MimicState.OVERLAY;
        }

        @Override
        public void stop() {
            EntityMimicOctopus.this.m_21573_().stop();
            this.flightTarget = null;
            this.targetEntity = null;
        }

        @Override
        public void tick() {
            if (this.cooldown > 0) {
                this.cooldown--;
            }
            if (this.targetEntity != null) {
                EntityMimicOctopus.this.m_21573_().moveTo(this.targetEntity, 1.2F);
                if (EntityMimicOctopus.this.m_20270_(this.targetEntity) > 20.0F) {
                    this.stop();
                    EntityMimicOctopus.this.setMimicState(EntityMimicOctopus.MimicState.OVERLAY);
                    EntityMimicOctopus.this.setMimickedBlock(null);
                } else if (EntityMimicOctopus.this.m_20270_(this.targetEntity) < 5.0F && EntityMimicOctopus.this.m_142582_(this.targetEntity)) {
                    int i = 1200;
                    EntityMimicOctopus.this.stopMimicCooldown = i;
                    EntityMimicOctopus.this.camoCooldown = i + 40;
                    EntityMimicOctopus.this.mimicCooldown = 40;
                    if (this.targetEntity instanceof Creeper) {
                        EntityMimicOctopus.this.setMimicState(EntityMimicOctopus.MimicState.CREEPER);
                    } else if (this.targetEntity instanceof Guardian) {
                        EntityMimicOctopus.this.setMimicState(EntityMimicOctopus.MimicState.GUARDIAN);
                    } else if (this.targetEntity instanceof Pufferfish) {
                        EntityMimicOctopus.this.setMimicState(EntityMimicOctopus.MimicState.PUFFERFISH);
                    } else {
                        EntityMimicOctopus.this.setMimicState(EntityMimicOctopus.MimicState.OVERLAY);
                        EntityMimicOctopus.this.setMimickedBlock(null);
                    }
                    this.stop();
                }
            }
        }

        protected double getTargetDistance() {
            return 10.0;
        }

        protected AABB getTargetableArea(double targetDistance) {
            Vec3 renderCenter = new Vec3(EntityMimicOctopus.this.m_20185_(), EntityMimicOctopus.this.m_20186_() + 0.5, EntityMimicOctopus.this.m_20189_());
            AABB aabb = new AABB(-targetDistance, -targetDistance, -targetDistance, targetDistance, targetDistance, targetDistance);
            return aabb.move(renderCenter);
        }
    }

    private class AISwim extends SemiAquaticAIRandomSwimming {

        public AISwim() {
            super(EntityMimicOctopus.this, 1.0, 35);
        }

        @Override
        protected Vec3 findSurfaceTarget(PathfinderMob creature, int i, int i1) {
            if (creature.m_217043_().nextInt(5) == 0) {
                return super.findSurfaceTarget(creature, i, i1);
            } else {
                BlockPos downPos = creature.m_20183_();
                while (creature.m_9236_().getFluidState(downPos).is(FluidTags.WATER) || creature.m_9236_().getFluidState(downPos).is(FluidTags.LAVA)) {
                    downPos = downPos.below();
                }
                return EntityMimicOctopus.this.m_9236_().getBlockState(downPos).m_60815_() && EntityMimicOctopus.this.m_9236_().getBlockState(downPos).m_60734_() != Blocks.MAGMA_BLOCK ? new Vec3((double) ((float) downPos.m_123341_() + 0.5F), (double) downPos.m_123342_(), (double) ((float) downPos.m_123343_() + 0.5F)) : null;
            }
        }
    }

    public static record EntitySorter(Entity theEntity) implements Comparator<Entity> {

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }

    public static enum MimicState {

        OVERLAY, CREEPER, GUARDIAN, PUFFERFISH, MIMICUBE
    }
}