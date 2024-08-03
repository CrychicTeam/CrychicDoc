package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.BlueJayAIMelee;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.FlyingAITempt;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

public class EntityBlueJay extends Animal implements ITargetsDroppedItems {

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityBlueJay.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(EntityBlueJay.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> CREST_TARGET = SynchedEntityData.defineId(EntityBlueJay.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Optional<UUID>> LAST_FEEDER_UUID = SynchedEntityData.defineId(EntityBlueJay.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Optional<UUID>> RACCOON_UUID = SynchedEntityData.defineId(EntityBlueJay.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> FEED_TIME = SynchedEntityData.defineId(EntityBlueJay.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SING_TIME = SynchedEntityData.defineId(EntityBlueJay.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> BLUE_VISUAL_FLAG = SynchedEntityData.defineId(EntityBlueJay.class, EntityDataSerializers.BOOLEAN);

    private static final Predicate<Entity> HIGHLIGHTS_WITH_SONG = entity -> entity instanceof Enemy;

    public float prevFlyProgress;

    public float flyProgress;

    public float prevFlapAmount;

    public float flapAmount;

    public float attackProgress;

    public float prevAttackProgress;

    public float prevCrestAmount;

    public float crestAmount;

    private boolean isLandNavigator;

    private int timeFlying;

    public float birdPitch = 0.0F;

    public float prevBirdPitch = 0.0F;

    public boolean aiItemFlag = false;

    private int prevSingTime = 0;

    private int blueTime = 0;

    private int raiseCrestOverrideTicks;

    protected EntityBlueJay(EntityType<? extends Animal> animal, Level level) {
        super(animal, level);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 16.0F);
        this.m_21441_(BlockPathTypes.COCOA, -1.0F);
        this.m_21441_(BlockPathTypes.FENCE, -1.0F);
        this.switchNavigator(false);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(2, new BlueJayAIMelee(this));
        this.f_21345_.addGoal(3, new FollowParentGoal(this, 1.0));
        this.f_21345_.addGoal(4, new FlyingAITempt(this, 1.0, Ingredient.of(AMTagRegistry.BLUE_JAY_FOODSTUFFS), false));
        this.f_21345_.addGoal(5, new EntityBlueJay.AIFollowFeederOrRaccoon());
        this.f_21345_.addGoal(6, new EntityBlueJay.AIFlyIdle());
        this.f_21345_.addGoal(7, new EntityBlueJay.AIScatter());
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, PathfinderMob.class, 6.0F));
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new EntityBlueJay.AITargetItems(this, false, false, 40, 16));
        this.f_21346_.addGoal(4, new HurtByTargetGoal(this, Player.class).setAlertOthers());
    }

    public static boolean checkBlueJaySpawnRules(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return m_186209_(worldIn, pos);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader reader) {
        if (reader.m_45784_(this) && !reader.containsAnyLiquid(this.m_20191_())) {
            BlockPos blockpos = this.m_20183_();
            BlockState blockstate2 = reader.m_8055_(blockpos.below());
            return blockstate2.m_204336_(BlockTags.LEAVES) || blockstate2.m_204336_(BlockTags.LOGS) || blockstate2.m_60713_(Blocks.GRASS_BLOCK);
        } else {
            return false;
        }
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.blueJaySpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(AMTagRegistry.INSECT_ITEMS);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(ATTACK_TICK, 0);
        this.f_19804_.define(FEED_TIME, 0);
        this.f_19804_.define(SING_TIME, 0);
        this.f_19804_.define(CREST_TARGET, 0.0F);
        this.f_19804_.define(BLUE_VISUAL_FLAG, false);
        this.f_19804_.define(RACCOON_UUID, Optional.empty());
        this.f_19804_.define(LAST_FEEDER_UUID, Optional.empty());
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new FlightMoveController(this, 1.0F, false);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevCrestAmount = this.crestAmount;
        this.prevAttackProgress = this.attackProgress;
        this.prevFlapAmount = this.flapAmount;
        this.prevFlyProgress = this.flyProgress;
        this.prevBirdPitch = this.birdPitch;
        if (this.isFlying()) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (this.f_19804_.get(ATTACK_TICK) > 0) {
            this.f_19804_.set(ATTACK_TICK, this.f_19804_.get(ATTACK_TICK) - 1);
            if (this.attackProgress < 5.0F) {
                this.attackProgress++;
            }
        } else if (this.attackProgress > 0.0F) {
            this.attackProgress--;
        }
        float yMov = (float) this.m_20184_().y;
        this.birdPitch = yMov * 2.0F * (-180.0F / (float) Math.PI);
        if (yMov >= 0.0F) {
            if (this.flapAmount < 1.0F) {
                this.flapAmount += 0.25F;
            }
        } else if (yMov < -0.07F && this.flapAmount > 0.0F) {
            this.flapAmount -= 0.25F;
        }
        if (this.raiseCrestOverrideTicks > 0) {
            this.raiseCrestOverrideTicks--;
            this.crestAmount = 0.75F;
        } else {
            this.crestAmount = Mth.approach(this.crestAmount, this.getTargetCrest(), 0.3F);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isFlying()) {
                if (this.isLandNavigator) {
                    this.switchNavigator(false);
                }
            } else if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.isFlying()) {
                this.timeFlying++;
                this.m_20242_(true);
                if (this.m_20159_() || this.m_27593_()) {
                    this.setFlying(false);
                }
            } else {
                this.timeFlying = 0;
                this.m_20242_(false);
            }
            if (this.m_5448_() != null) {
                this.setCrestTarget(1.0F);
            } else if (this.getRaccoonUUID() != null) {
                this.setCrestTarget(0.5F);
            } else {
                this.setCrestTarget(0.0F);
            }
        }
        if (this.getFeedTime() > 0) {
            this.setFeedTime(this.getFeedTime() - 1);
            if (this.getFeedTime() == 0) {
                this.setLastFeeder(null);
            }
        }
        if (this.m_20202_() instanceof EntityRaccoon riddenRaccoon) {
            this.f_20883_ = riddenRaccoon.f_20883_;
        }
        if (this.getRaccoon() instanceof EntityRaccoon raccoon) {
            LivingEntity jayTarget = this.m_5448_();
            LivingEntity raccoonTarget = raccoon.m_5448_();
            if (jayTarget != null && jayTarget.isAlive()) {
                if (this.m_20159_()) {
                    this.m_8127_();
                }
            } else if (raccoonTarget != null && raccoonTarget.isAlive() && this.m_6779_(raccoonTarget)) {
                this.m_6710_(raccoonTarget);
            }
        }
        if (this.getSingTime() > 0) {
            this.setSingTime(this.getSingTime() - 1);
            if (this.prevSingTime % 15 == 0) {
                this.m_5496_(AMSoundRegistry.BLUE_JAY_SONG.get(), this.m_6121_(), this.m_6100_());
            }
            if (this.m_9236_().isClientSide && this.getSingTime() % 5 == 0 && this.m_9236_().isClientSide) {
                Vec3 modelFront = new Vec3(0.0, 0.2F, 0.3F).scale((double) this.m_6134_()).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_146908_() * (float) (Math.PI / 180.0));
                Vec3 particleFrom = this.m_20182_().add(modelFront);
                this.m_9236_().addParticle(AMParticleRegistry.BIRD_SONG.get(), particleFrom.x, particleFrom.y, particleFrom.z, modelFront.x, modelFront.y, modelFront.z);
            }
        }
        if (this.prevSingTime < this.getSingTime() && !this.m_9236_().isClientSide) {
            this.blueTime = 1200;
            this.f_19804_.set(BLUE_VISUAL_FLAG, true);
            this.highlightMonsters();
        }
        if (this.blueTime > 0) {
            this.blueTime--;
            if (this.blueTime == 0) {
                this.f_19804_.set(BLUE_VISUAL_FLAG, false);
                this.m_9236_().broadcastEntityEvent(this, (byte) 68);
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 67);
            }
        }
        this.prevSingTime = this.getSingTime();
    }

    @Override
    public void playAmbientSound() {
        super.m_8032_();
        this.raiseCrestOverrideTicks = 15;
    }

    private boolean highlightMonsters() {
        AABB allyBox = this.m_20191_().inflate(64.0);
        allyBox = allyBox.setMinY(-64.0);
        allyBox = allyBox.setMaxY(320.0);
        boolean any = false;
        for (LivingEntity entity : this.m_9236_().m_6443_(LivingEntity.class, allyBox, HIGHLIGHTS_WITH_SONG)) {
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, this.blueTime, 0, true, false));
        }
        return any;
    }

    public boolean isMakingMonstersBlue() {
        return this.f_19804_.get(BLUE_VISUAL_FLAG);
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        if (this.getSingTime() > 0 && !this.m_9236_().isClientSide) {
            this.f_19804_.set(BLUE_VISUAL_FLAG, false);
            this.m_9236_().broadcastEntityEvent(this, (byte) 68);
        }
        super.m_142687_(removalReason);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.m_20069_() && this.m_20184_().y > 0.0) {
            this.m_20256_(this.m_20184_().multiply(1.0, 0.5, 1.0));
        }
        super.m_7023_(vec3d);
    }

    public BlockPos getBlueJayGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int) this.m_20186_(), in.m_123343_());
        while (position.m_123342_() < 320 && !this.m_9236_().getFluidState(position).isEmpty()) {
            position = position.above();
        }
        while (position.m_123342_() > -64 && !this.m_9236_().getBlockState(position).m_280296_() && this.m_9236_().getFluidState(position).isEmpty()) {
            position = position.below();
        }
        return position;
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (this.getRaccoonUUID() != null) {
            if (entityIn instanceof EntityRaccoon && this.getRaccoonUUID().equals(entityIn.getUUID())) {
                return true;
            }
            Entity raccoon = this.getRaccoon();
            if (raccoon != null && (raccoon.isAlliedTo(entityIn) || entityIn.isAlliedTo(raccoon))) {
                return true;
            }
        }
        return super.m_7307_(entityIn);
    }

    public Vec3 getBlockGrounding(Vec3 fleePos) {
        float radius = (float) (10 + this.m_217043_().nextInt(15));
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), (int) this.m_20186_(), (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getBlueJayGround(radialPos);
        if (ground.m_123342_() < -64) {
            return null;
        } else {
            ground = this.m_20183_();
            while (ground.m_123342_() > -64 && !this.m_9236_().getBlockState(ground).m_280296_()) {
                ground = ground.below();
            }
            return !this.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? Vec3.atCenterOf(ground.below()) : null;
        }
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = 5.0F + radiusAdd + (float) this.m_217043_().nextInt(5);
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), 0, (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getBlueJayGround(radialPos);
        int distFromGround = (int) this.m_20186_() - ground.m_123342_();
        int flightHeight = 5 + this.m_217043_().nextInt(5);
        int j = this.m_217043_().nextInt(5) + 5;
        BlockPos newPos = ground.above(distFromGround > 5 ? flightHeight : j);
        if (this.m_9236_().getBlockState(ground).m_204336_(BlockTags.LEAVES)) {
            newPos = ground.above(1 + this.m_217043_().nextInt(3));
        }
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.m_20238_(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.BLUE_JAY_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.BLUE_JAY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.BLUE_JAY_HURT.get();
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    @Override
    public void setFlying(boolean flying) {
        if (flying && this.m_6162_()) {
            flying = false;
        }
        this.f_19804_.set(FLYING, flying);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.blueTime = compound.getInt("BlueTime");
        if (compound.hasUUID("FeederUUID")) {
            this.setLastFeederUUID(compound.getUUID("FeederUUID"));
        }
        if (compound.hasUUID("RaccoonUUID")) {
            this.setRaccoonUUID(compound.getUUID("RaccoonUUID"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("BlueTime", this.blueTime);
        if (this.getLastFeederUUID() != null) {
            compound.putUUID("FeederUUID", this.getLastFeederUUID());
        }
        if (this.getRaccoonUUID() != null) {
            compound.putUUID("RaccoonUUID", this.getRaccoonUUID());
        }
    }

    public int getFeedTime() {
        return this.f_19804_.get(FEED_TIME);
    }

    public void setFeedTime(int feedTime) {
        this.f_19804_.set(FEED_TIME, feedTime);
    }

    public int getSingTime() {
        return this.f_19804_.get(SING_TIME);
    }

    public void setSingTime(int singTime) {
        this.f_19804_.set(SING_TIME, singTime);
    }

    public float getTargetCrest() {
        return this.f_19804_.get(CREST_TARGET);
    }

    public void setCrestTarget(float crestTarget) {
        this.f_19804_.set(CREST_TARGET, crestTarget);
    }

    @Nullable
    public UUID getLastFeederUUID() {
        return (UUID) this.f_19804_.get(LAST_FEEDER_UUID).orElse(null);
    }

    public void setLastFeederUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(LAST_FEEDER_UUID, Optional.ofNullable(uniqueId));
    }

    @Nullable
    public Entity getLastFeeder() {
        UUID id = this.getLastFeederUUID();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    public void setLastFeeder(@Nullable Entity feeder) {
        if (feeder == null) {
            this.setLastFeederUUID(null);
        } else {
            this.setLastFeederUUID(feeder.getUUID());
        }
    }

    @Nullable
    public UUID getRaccoonUUID() {
        return (UUID) this.f_19804_.get(RACCOON_UUID).orElse(null);
    }

    public void setRaccoonUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(RACCOON_UUID, Optional.ofNullable(uniqueId));
    }

    @Nullable
    public Entity getRaccoon() {
        UUID id = this.getRaccoonUUID();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    public void setRaccoon(@Nullable Entity feeder) {
        if (feeder == null) {
            this.setRaccoonUUID(null);
        } else {
            this.setRaccoonUUID(feeder.getUUID());
        }
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -65 && this.m_9236_().m_46859_(position)) {
            position = position.below();
        }
        return !this.m_9236_().getFluidState(position).isEmpty() || this.m_9236_().getBlockState(position).m_60713_(Blocks.VINE) || position.m_123342_() <= -65;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return AMEntityRegistry.BLUE_JAY.get().create(this.m_9236_());
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.getItem().isEdible() || stack.is(AMTagRegistry.BLUE_JAY_FOODSTUFFS);
    }

    @Override
    public double getMaxDistToItem() {
        return 1.0;
    }

    @Override
    public void onGetItem(ItemEntity e) {
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.m_9236_().isClientSide) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
        }
        this.m_5634_(3.0F);
        Entity itemThrower = e.getOwner();
        if (itemThrower != null && e.getItem().is(Items.GLOW_BERRIES)) {
            this.setLastFeederUUID(itemThrower.getUUID());
            this.setFeedTime(1200);
            this.m_8127_();
        }
        if (e.getOwner() != null && e.getItem().is(Tags.Items.SEEDS)) {
            this.setSingTime(40);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.mobInteract(player, hand);
        if (!type.consumesAction()) {
            if (itemstack.is(Items.GLOW_BERRIES) && this.getFeedTime() <= 0) {
                this.m_5634_(3.0F);
                this.m_142075_(player, hand, itemstack);
                this.setRaccoonUUID(null);
                this.m_8127_();
                this.setLastFeeder(player);
                this.setFeedTime(1200);
                return InteractionResult.SUCCESS;
            }
            if (itemstack.is(Tags.Items.SEEDS) && this.getSingTime() <= 0) {
                this.m_5634_(3.0F);
                this.setSingTime(40);
                this.m_142075_(player, hand, itemstack);
                return InteractionResult.SUCCESS;
            }
        }
        return type;
    }

    @Override
    public void peck() {
        this.f_19804_.set(ATTACK_TICK, 7);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id != 67 && id != 68) {
            super.handleEntityEvent(id);
        } else {
            AlexsMobs.PROXY.onEntityStatus(this, id);
        }
    }

    private boolean isTrusting() {
        return this.getFeedTime() > 0 || this.getSingTime() > 0 || this.getRaccoonUUID() != null || this.aiItemFlag;
    }

    private class AIFlyIdle extends Goal {

        protected double x;

        protected double y;

        protected double z;

        private boolean flightTarget;

        public AIFlyIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (EntityBlueJay.this.m_20160_() || EntityBlueJay.this.m_5448_() != null && EntityBlueJay.this.m_5448_().isAlive() || EntityBlueJay.this.m_20159_() || EntityBlueJay.this.aiItemFlag || EntityBlueJay.this.getSingTime() > 0) {
                return false;
            } else if (EntityBlueJay.this.m_217043_().nextInt(45) != 0 && !EntityBlueJay.this.isFlying()) {
                return false;
            } else {
                if (EntityBlueJay.this.m_20096_()) {
                    this.flightTarget = EntityBlueJay.this.f_19796_.nextBoolean();
                } else {
                    this.flightTarget = EntityBlueJay.this.f_19796_.nextInt(5) > 0 && EntityBlueJay.this.timeFlying < 200;
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
                EntityBlueJay.this.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                EntityBlueJay.this.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
            if (!this.flightTarget && EntityBlueJay.this.isFlying() && EntityBlueJay.this.m_20096_()) {
                EntityBlueJay.this.setFlying(false);
            }
            if (EntityBlueJay.this.isFlying() && EntityBlueJay.this.m_20096_() && EntityBlueJay.this.timeFlying > 10) {
                EntityBlueJay.this.setFlying(false);
            }
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = EntityBlueJay.this.m_20182_();
            if (EntityBlueJay.this.isOverWaterOrVoid()) {
                this.flightTarget = true;
            }
            if (this.flightTarget) {
                return EntityBlueJay.this.timeFlying >= 200 && !EntityBlueJay.this.isOverWaterOrVoid() ? EntityBlueJay.this.getBlockGrounding(vector3d) : EntityBlueJay.this.getBlockInViewAway(vector3d, 0.0F);
            } else {
                return LandRandomPos.getPos(EntityBlueJay.this, 10, 7);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.flightTarget ? EntityBlueJay.this.isFlying() && EntityBlueJay.this.m_20275_(this.x, this.y, this.z) > 5.0 : !EntityBlueJay.this.m_21573_().isDone() && !EntityBlueJay.this.m_20160_();
        }

        @Override
        public void start() {
            if (this.flightTarget) {
                EntityBlueJay.this.setFlying(true);
                EntityBlueJay.this.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                EntityBlueJay.this.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
        }

        @Override
        public void stop() {
            EntityBlueJay.this.m_21573_().stop();
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
            super.stop();
        }
    }

    private class AIFollowFeederOrRaccoon extends Goal {

        private Entity following;

        AIFollowFeederOrRaccoon() {
            this.m_7021_(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!EntityBlueJay.this.m_20159_() && (EntityBlueJay.this.m_5448_() == null || !EntityBlueJay.this.m_5448_().isAlive())) {
                if (EntityBlueJay.this.getRaccoonUUID() != null) {
                    Entity raccoon = EntityBlueJay.this.getRaccoon();
                    if (raccoon != null) {
                        this.following = raccoon;
                        return true;
                    }
                }
                if (EntityBlueJay.this.getFeedTime() > 0) {
                    Entity feeder = EntityBlueJay.this.getLastFeeder();
                    if (feeder != null) {
                        this.following = feeder;
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = EntityBlueJay.this.m_5448_();
            return this.following != null && this.following.isAlive() && (target == null || !target.isAlive()) && (this.following instanceof EntityRaccoon || EntityBlueJay.this.getFeedTime() > 0) && !EntityBlueJay.this.m_20159_();
        }

        @Override
        public void tick() {
            double dist = (double) EntityBlueJay.this.m_20270_(this.following);
            if (!(dist > 6.0) && !EntityBlueJay.this.isFlying()) {
                EntityBlueJay.this.m_21573_().moveTo(this.following.getX(), this.following.getY(), this.following.getZ(), 1.0);
            } else {
                EntityBlueJay.this.setFlying(true);
                EntityBlueJay.this.m_21566_().setWantedPosition(this.following.getX(), this.following.getY(), this.following.getZ(), 1.0);
            }
            if (EntityBlueJay.this.isFlying() && EntityBlueJay.this.m_20096_() && dist < 3.0) {
                EntityBlueJay.this.setFlying(false);
            }
            if (this.following instanceof EntityRaccoon raccoon) {
                if (dist > 40.0) {
                    EntityBlueJay.this.m_6021_(this.following.getX(), this.following.getY(), this.following.getZ());
                }
                if (dist < 2.5) {
                    EntityBlueJay.this.m_21566_().setWantedPosition(this.following.getX(), this.following.getY(), this.following.getZ(), 1.0);
                }
                if (dist < 1.0 && raccoon.m_20197_().isEmpty()) {
                    EntityBlueJay.this.m_7998_(raccoon, false);
                }
            }
        }
    }

    private class AIScatter extends Goal {

        protected final EntityBlueJay.AIScatter.Sorter theNearestAttackableTargetSorter;

        protected final com.google.common.base.Predicate<? super Entity> targetEntitySelector;

        protected int executionChance = 8;

        protected boolean mustUpdate;

        private Entity targetEntity;

        private Vec3 flightTarget = null;

        private int cooldown = 0;

        AIScatter() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.theNearestAttackableTargetSorter = new EntityBlueJay.AIScatter.Sorter(EntityBlueJay.this);
            this.targetEntitySelector = new com.google.common.base.Predicate<Entity>() {

                public boolean apply(@Nullable Entity e) {
                    return e.isAlive() && e.getType().is(AMTagRegistry.SCATTERS_CROWS) || e instanceof Player && !((Player) e).isCreative();
                }
            };
        }

        @Override
        public boolean canUse() {
            Entity entity = EntityBlueJay.this.m_5448_();
            if (!EntityBlueJay.this.m_20159_() && !EntityBlueJay.this.m_20160_() && (entity == null || !entity.isAlive()) && !EntityBlueJay.this.isTrusting()) {
                if (!this.mustUpdate) {
                    long worldTime = EntityBlueJay.this.m_9236_().getGameTime() % 10L;
                    if (EntityBlueJay.this.m_21216_() >= 100 && worldTime != 0L) {
                        return false;
                    }
                    if (EntityBlueJay.this.m_217043_().nextInt(this.executionChance) != 0 && worldTime != 0L) {
                        return false;
                    }
                }
                List<Entity> list = EntityBlueJay.this.m_9236_().m_6443_(Entity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
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
            return this.targetEntity != null;
        }

        @Override
        public void stop() {
            this.flightTarget = null;
            this.targetEntity = null;
        }

        @Override
        public void tick() {
            if (this.cooldown > 0) {
                this.cooldown--;
            }
            if (this.flightTarget != null) {
                EntityBlueJay.this.setFlying(true);
                EntityBlueJay.this.m_21566_().setWantedPosition(this.flightTarget.x, this.flightTarget.y, this.flightTarget.z, 1.0);
                if (this.cooldown == 0 && EntityBlueJay.this.isTargetBlocked(this.flightTarget)) {
                    this.cooldown = 30;
                    this.flightTarget = null;
                }
            }
            if (this.targetEntity != null) {
                if (EntityBlueJay.this.m_20096_() || this.flightTarget == null || this.flightTarget != null && EntityBlueJay.this.m_20238_(this.flightTarget) < 3.0) {
                    Vec3 vec = EntityBlueJay.this.getBlockInViewAway(this.targetEntity.position(), 0.0F);
                    if (vec != null && vec.y() > EntityBlueJay.this.m_20186_()) {
                        this.flightTarget = vec;
                    }
                }
                if (EntityBlueJay.this.m_20270_(this.targetEntity) > 20.0F) {
                    this.stop();
                }
            }
        }

        protected double getTargetDistance() {
            return 4.0;
        }

        protected AABB getTargetableArea(double targetDistance) {
            Vec3 renderCenter = new Vec3(EntityBlueJay.this.m_20185_(), EntityBlueJay.this.m_20186_() + 0.5, EntityBlueJay.this.m_20189_());
            AABB aabb = new AABB(-targetDistance, -targetDistance, -targetDistance, targetDistance, targetDistance, targetDistance);
            return aabb.move(renderCenter);
        }

        public static record Sorter(Entity theEntity) implements Comparator<Entity> {

            public int compare(Entity p_compare_1_, Entity p_compare_2_) {
                double d0 = this.theEntity.distanceToSqr(p_compare_1_);
                double d1 = this.theEntity.distanceToSqr(p_compare_2_);
                return Double.compare(d0, d1);
            }
        }
    }

    private static class AITargetItems extends CreatureAITargetItems {

        public AITargetItems(PathfinderMob creature, boolean checkSight, boolean onlyNearby, int tickThreshold, int radius) {
            super(creature, checkSight, onlyNearby, tickThreshold, radius);
            this.executionChance = 1;
        }

        @Override
        public void stop() {
            super.stop();
            ((EntityBlueJay) this.f_26135_).aiItemFlag = false;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && (this.f_26135_.getTarget() == null || !this.f_26135_.getTarget().isAlive());
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && (this.f_26135_.getTarget() == null || !this.f_26135_.getTarget().isAlive());
        }

        @Override
        protected void moveTo() {
            EntityBlueJay jay = (EntityBlueJay) this.f_26135_;
            if (this.targetEntity != null) {
                jay.aiItemFlag = true;
                if (this.f_26135_.m_20270_(this.targetEntity) < 2.0F) {
                    jay.m_21566_().setWantedPosition(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.0);
                    jay.peck();
                }
                if (!(this.f_26135_.m_20270_(this.targetEntity) > 8.0F) && !jay.isFlying()) {
                    this.f_26135_.getNavigation().moveTo(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.0);
                } else {
                    jay.setFlying(true);
                    float f = (float) (jay.m_20185_() - this.targetEntity.m_20185_());
                    float f1 = 1.8F;
                    float f2 = (float) (jay.m_20189_() - this.targetEntity.m_20189_());
                    float xzDist = Mth.sqrt(f * f + f2 * f2);
                    if (!jay.m_142582_(this.targetEntity)) {
                        jay.m_21566_().setWantedPosition(this.targetEntity.m_20185_(), 1.0 + jay.m_20186_(), this.targetEntity.m_20189_(), 1.0);
                    } else {
                        if (xzDist < 5.0F) {
                            f1 = 0.0F;
                        }
                        jay.m_21566_().setWantedPosition(this.targetEntity.m_20185_(), (double) f1 + this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.0);
                    }
                }
            }
        }

        @Override
        public void tick() {
            super.tick();
            this.moveTo();
        }
    }
}