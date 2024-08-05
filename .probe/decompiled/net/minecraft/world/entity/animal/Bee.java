package net.minecraft.world.entity.animal;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class Bee extends Animal implements NeutralMob, FlyingAnimal {

    public static final float FLAP_DEGREES_PER_TICK = 120.32113F;

    public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);

    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Bee.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(Bee.class, EntityDataSerializers.INT);

    private static final int FLAG_ROLL = 2;

    private static final int FLAG_HAS_STUNG = 4;

    private static final int FLAG_HAS_NECTAR = 8;

    private static final int STING_DEATH_COUNTDOWN = 1200;

    private static final int TICKS_BEFORE_GOING_TO_KNOWN_FLOWER = 2400;

    private static final int TICKS_WITHOUT_NECTAR_BEFORE_GOING_HOME = 3600;

    private static final int MIN_ATTACK_DIST = 4;

    private static final int MAX_CROPS_GROWABLE = 10;

    private static final int POISON_SECONDS_NORMAL = 10;

    private static final int POISON_SECONDS_HARD = 18;

    private static final int TOO_FAR_DISTANCE = 32;

    private static final int HIVE_CLOSE_ENOUGH_DISTANCE = 2;

    private static final int PATHFIND_TO_HIVE_WHEN_CLOSER_THAN = 16;

    private static final int HIVE_SEARCH_DISTANCE = 20;

    public static final String TAG_CROPS_GROWN_SINCE_POLLINATION = "CropsGrownSincePollination";

    public static final String TAG_CANNOT_ENTER_HIVE_TICKS = "CannotEnterHiveTicks";

    public static final String TAG_TICKS_SINCE_POLLINATION = "TicksSincePollination";

    public static final String TAG_HAS_STUNG = "HasStung";

    public static final String TAG_HAS_NECTAR = "HasNectar";

    public static final String TAG_FLOWER_POS = "FlowerPos";

    public static final String TAG_HIVE_POS = "HivePos";

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    @Nullable
    private UUID persistentAngerTarget;

    private float rollAmount;

    private float rollAmountO;

    private int timeSinceSting;

    int ticksWithoutNectarSinceExitingHive;

    private int stayOutOfHiveCountdown;

    private int numCropsGrownSincePollination;

    private static final int COOLDOWN_BEFORE_LOCATING_NEW_HIVE = 200;

    int remainingCooldownBeforeLocatingNewHive;

    private static final int COOLDOWN_BEFORE_LOCATING_NEW_FLOWER = 200;

    int remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.f_19796_, 20, 60);

    @Nullable
    BlockPos savedFlowerPos;

    @Nullable
    BlockPos hivePos;

    Bee.BeePollinateGoal beePollinateGoal;

    Bee.BeeGoToHiveGoal goToHiveGoal;

    private Bee.BeeGoToKnownFlowerGoal goToKnownFlowerGoal;

    private int underWaterTicks;

    public Bee(EntityType<? extends Bee> entityTypeExtendsBee0, Level level1) {
        super(entityTypeExtendsBee0, level1);
        this.f_21342_ = new FlyingMoveControl(this, 20, true);
        this.f_21365_ = new Bee.BeeLookControl(this);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 16.0F);
        this.m_21441_(BlockPathTypes.COCOA, -1.0F);
        this.m_21441_(BlockPathTypes.FENCE, -1.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_FLAGS_ID, (byte) 0);
        this.f_19804_.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        return levelReader1.m_8055_(blockPos0).m_60795_() ? 10.0F : 0.0F;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new Bee.BeeAttackGoal(this, 1.4F, true));
        this.f_21345_.addGoal(1, new Bee.BeeEnterHiveGoal());
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(ItemTags.FLOWERS), false));
        this.beePollinateGoal = new Bee.BeePollinateGoal();
        this.f_21345_.addGoal(4, this.beePollinateGoal);
        this.f_21345_.addGoal(5, new FollowParentGoal(this, 1.25));
        this.f_21345_.addGoal(5, new Bee.BeeLocateHiveGoal());
        this.goToHiveGoal = new Bee.BeeGoToHiveGoal();
        this.f_21345_.addGoal(5, this.goToHiveGoal);
        this.goToKnownFlowerGoal = new Bee.BeeGoToKnownFlowerGoal();
        this.f_21345_.addGoal(6, this.goToKnownFlowerGoal);
        this.f_21345_.addGoal(7, new Bee.BeeGrowCropGoal());
        this.f_21345_.addGoal(8, new Bee.BeeWanderGoal());
        this.f_21345_.addGoal(9, new FloatGoal(this));
        this.f_21346_.addGoal(1, new Bee.BeeHurtByOtherGoal(this).m_26044_(new Class[0]));
        this.f_21346_.addGoal(2, new Bee.BeeBecomeAngryTargetGoal(this));
        this.f_21346_.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        if (this.hasHive()) {
            compoundTag0.put("HivePos", NbtUtils.writeBlockPos(this.getHivePos()));
        }
        if (this.hasSavedFlowerPos()) {
            compoundTag0.put("FlowerPos", NbtUtils.writeBlockPos(this.getSavedFlowerPos()));
        }
        compoundTag0.putBoolean("HasNectar", this.hasNectar());
        compoundTag0.putBoolean("HasStung", this.hasStung());
        compoundTag0.putInt("TicksSincePollination", this.ticksWithoutNectarSinceExitingHive);
        compoundTag0.putInt("CannotEnterHiveTicks", this.stayOutOfHiveCountdown);
        compoundTag0.putInt("CropsGrownSincePollination", this.numCropsGrownSincePollination);
        this.m_21678_(compoundTag0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        this.hivePos = null;
        if (compoundTag0.contains("HivePos")) {
            this.hivePos = NbtUtils.readBlockPos(compoundTag0.getCompound("HivePos"));
        }
        this.savedFlowerPos = null;
        if (compoundTag0.contains("FlowerPos")) {
            this.savedFlowerPos = NbtUtils.readBlockPos(compoundTag0.getCompound("FlowerPos"));
        }
        super.readAdditionalSaveData(compoundTag0);
        this.setHasNectar(compoundTag0.getBoolean("HasNectar"));
        this.setHasStung(compoundTag0.getBoolean("HasStung"));
        this.ticksWithoutNectarSinceExitingHive = compoundTag0.getInt("TicksSincePollination");
        this.stayOutOfHiveCountdown = compoundTag0.getInt("CannotEnterHiveTicks");
        this.numCropsGrownSincePollination = compoundTag0.getInt("CropsGrownSincePollination");
        this.m_147285_(this.m_9236_(), compoundTag0);
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        boolean $$1 = entity0.hurt(this.m_269291_().sting(this), (float) ((int) this.m_21133_(Attributes.ATTACK_DAMAGE)));
        if ($$1) {
            this.m_19970_(this, entity0);
            if (entity0 instanceof LivingEntity) {
                ((LivingEntity) entity0).setStingerCount(((LivingEntity) entity0).getStingerCount() + 1);
                int $$2 = 0;
                if (this.m_9236_().m_46791_() == Difficulty.NORMAL) {
                    $$2 = 10;
                } else if (this.m_9236_().m_46791_() == Difficulty.HARD) {
                    $$2 = 18;
                }
                if ($$2 > 0) {
                    ((LivingEntity) entity0).addEffect(new MobEffectInstance(MobEffects.POISON, $$2 * 20, 0), this);
                }
            }
            this.setHasStung(true);
            this.m_21662_();
            this.m_5496_(SoundEvents.BEE_STING, 1.0F, 1.0F);
        }
        return $$1;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.f_19796_.nextFloat() < 0.05F) {
            for (int $$0 = 0; $$0 < this.f_19796_.nextInt(2) + 1; $$0++) {
                this.spawnFluidParticle(this.m_9236_(), this.m_20185_() - 0.3F, this.m_20185_() + 0.3F, this.m_20189_() - 0.3F, this.m_20189_() + 0.3F, this.m_20227_(0.5), ParticleTypes.FALLING_NECTAR);
            }
        }
        this.updateRollAmount();
    }

    private void spawnFluidParticle(Level level0, double double1, double double2, double double3, double double4, double double5, ParticleOptions particleOptions6) {
        level0.addParticle(particleOptions6, Mth.lerp(level0.random.nextDouble(), double1, double2), double5, Mth.lerp(level0.random.nextDouble(), double3, double4), 0.0, 0.0, 0.0);
    }

    void pathfindRandomlyTowards(BlockPos blockPos0) {
        Vec3 $$1 = Vec3.atBottomCenterOf(blockPos0);
        int $$2 = 0;
        BlockPos $$3 = this.m_20183_();
        int $$4 = (int) $$1.y - $$3.m_123342_();
        if ($$4 > 2) {
            $$2 = 4;
        } else if ($$4 < -2) {
            $$2 = -4;
        }
        int $$5 = 6;
        int $$6 = 8;
        int $$7 = $$3.m_123333_(blockPos0);
        if ($$7 < 15) {
            $$5 = $$7 / 2;
            $$6 = $$7 / 2;
        }
        Vec3 $$8 = AirRandomPos.getPosTowards(this, $$5, $$6, $$2, $$1, (float) (Math.PI / 10));
        if ($$8 != null) {
            this.f_21344_.setMaxVisitedNodesMultiplier(0.5F);
            this.f_21344_.moveTo($$8.x, $$8.y, $$8.z, 1.0);
        }
    }

    @Nullable
    public BlockPos getSavedFlowerPos() {
        return this.savedFlowerPos;
    }

    public boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }

    public void setSavedFlowerPos(BlockPos blockPos0) {
        this.savedFlowerPos = blockPos0;
    }

    @VisibleForDebug
    public int getTravellingTicks() {
        return Math.max(this.goToHiveGoal.travellingTicks, this.goToKnownFlowerGoal.travellingTicks);
    }

    @VisibleForDebug
    public List<BlockPos> getBlacklistedHives() {
        return this.goToHiveGoal.blacklistedTargets;
    }

    private boolean isTiredOfLookingForNectar() {
        return this.ticksWithoutNectarSinceExitingHive > 3600;
    }

    boolean wantsToEnterHive() {
        if (this.stayOutOfHiveCountdown <= 0 && !this.beePollinateGoal.isPollinating() && !this.hasStung() && this.m_5448_() == null) {
            boolean $$0 = this.isTiredOfLookingForNectar() || this.m_9236_().isRaining() || this.m_9236_().isNight() || this.hasNectar();
            return $$0 && !this.isHiveNearFire();
        } else {
            return false;
        }
    }

    public void setStayOutOfHiveCountdown(int int0) {
        this.stayOutOfHiveCountdown = int0;
    }

    public float getRollAmount(float float0) {
        return Mth.lerp(float0, this.rollAmountO, this.rollAmount);
    }

    private void updateRollAmount() {
        this.rollAmountO = this.rollAmount;
        if (this.isRolling()) {
            this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
        } else {
            this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
        }
    }

    @Override
    protected void customServerAiStep() {
        boolean $$0 = this.hasStung();
        if (this.m_20072_()) {
            this.underWaterTicks++;
        } else {
            this.underWaterTicks = 0;
        }
        if (this.underWaterTicks > 20) {
            this.hurt(this.m_269291_().drown(), 1.0F);
        }
        if ($$0) {
            this.timeSinceSting++;
            if (this.timeSinceSting % 5 == 0 && this.f_19796_.nextInt(Mth.clamp(1200 - this.timeSinceSting, 1, 1200)) == 0) {
                this.hurt(this.m_269291_().generic(), this.m_21223_());
            }
        }
        if (!this.hasNectar()) {
            this.ticksWithoutNectarSinceExitingHive++;
        }
        if (!this.m_9236_().isClientSide) {
            this.m_21666_((ServerLevel) this.m_9236_(), false);
        }
    }

    public void resetTicksWithoutNectarSinceExitingHive() {
        this.ticksWithoutNectarSinceExitingHive = 0;
    }

    private boolean isHiveNearFire() {
        if (this.hivePos == null) {
            return false;
        } else {
            BlockEntity $$0 = this.m_9236_().getBlockEntity(this.hivePos);
            return $$0 instanceof BeehiveBlockEntity && ((BeehiveBlockEntity) $$0).isFireNearby();
        }
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.f_19804_.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int int0) {
        this.f_19804_.set(DATA_REMAINING_ANGER_TIME, int0);
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

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.f_19796_));
    }

    private boolean doesHiveHaveSpace(BlockPos blockPos0) {
        BlockEntity $$1 = this.m_9236_().getBlockEntity(blockPos0);
        return $$1 instanceof BeehiveBlockEntity ? !((BeehiveBlockEntity) $$1).isFull() : false;
    }

    @VisibleForDebug
    public boolean hasHive() {
        return this.hivePos != null;
    }

    @Nullable
    @VisibleForDebug
    public BlockPos getHivePos() {
        return this.hivePos;
    }

    @VisibleForDebug
    public GoalSelector getGoalSelector() {
        return this.f_21345_;
    }

    @Override
    protected void sendDebugPackets() {
        super.m_8025_();
        DebugPackets.sendBeeInfo(this);
    }

    int getCropsGrownSincePollination() {
        return this.numCropsGrownSincePollination;
    }

    private void resetNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination = 0;
    }

    void incrementNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination++;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.m_9236_().isClientSide) {
            if (this.stayOutOfHiveCountdown > 0) {
                this.stayOutOfHiveCountdown--;
            }
            if (this.remainingCooldownBeforeLocatingNewHive > 0) {
                this.remainingCooldownBeforeLocatingNewHive--;
            }
            if (this.remainingCooldownBeforeLocatingNewFlower > 0) {
                this.remainingCooldownBeforeLocatingNewFlower--;
            }
            boolean $$0 = this.m_21660_() && !this.hasStung() && this.m_5448_() != null && this.m_5448_().m_20280_(this) < 4.0;
            this.setRolling($$0);
            if (this.f_19797_ % 20 == 0 && !this.isHiveValid()) {
                this.hivePos = null;
            }
        }
    }

    boolean isHiveValid() {
        if (!this.hasHive()) {
            return false;
        } else if (this.isTooFarAway(this.hivePos)) {
            return false;
        } else {
            BlockEntity $$0 = this.m_9236_().getBlockEntity(this.hivePos);
            return $$0 != null && $$0.getType() == BlockEntityType.BEEHIVE;
        }
    }

    public boolean hasNectar() {
        return this.getFlag(8);
    }

    void setHasNectar(boolean boolean0) {
        if (boolean0) {
            this.resetTicksWithoutNectarSinceExitingHive();
        }
        this.setFlag(8, boolean0);
    }

    public boolean hasStung() {
        return this.getFlag(4);
    }

    private void setHasStung(boolean boolean0) {
        this.setFlag(4, boolean0);
    }

    private boolean isRolling() {
        return this.getFlag(2);
    }

    private void setRolling(boolean boolean0) {
        this.setFlag(2, boolean0);
    }

    boolean isTooFarAway(BlockPos blockPos0) {
        return !this.closerThan(blockPos0, 32);
    }

    private void setFlag(int int0, boolean boolean1) {
        if (boolean1) {
            this.f_19804_.set(DATA_FLAGS_ID, (byte) (this.f_19804_.get(DATA_FLAGS_ID) | int0));
        } else {
            this.f_19804_.set(DATA_FLAGS_ID, (byte) (this.f_19804_.get(DATA_FLAGS_ID) & ~int0));
        }
    }

    private boolean getFlag(int int0) {
        return (this.f_19804_.get(DATA_FLAGS_ID) & int0) != 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        FlyingPathNavigation $$1 = new FlyingPathNavigation(this, level0) {

            @Override
            public boolean isStableDestination(BlockPos p_27947_) {
                return !this.f_26495_.getBlockState(p_27947_.below()).m_60795_();
            }

            @Override
            public void tick() {
                if (!Bee.this.beePollinateGoal.isPollinating()) {
                    super.tick();
                }
            }
        };
        $$1.setCanOpenDoors(false);
        $$1.m_7008_(false);
        $$1.setCanPassDoors(true);
        return $$1;
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return itemStack0.is(ItemTags.FLOWERS);
    }

    boolean isFlowerValid(BlockPos blockPos0) {
        return this.m_9236_().isLoaded(blockPos0) && this.m_9236_().getBlockState(blockPos0).m_204336_(BlockTags.FLOWERS);
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.BEE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    public Bee getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.BEE.create(serverLevel0);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return this.m_6162_() ? entityDimensions1.height * 0.5F : entityDimensions1.height * 0.5F;
    }

    @Override
    protected void checkFallDamage(double double0, boolean boolean1, BlockState blockState2, BlockPos blockPos3) {
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.f_19797_ % TICKS_PER_FLAP == 0;
    }

    @Override
    public boolean isFlying() {
        return !this.m_20096_();
    }

    public void dropOffNectar() {
        this.setHasNectar(false);
        this.resetNumCropsGrownSincePollination();
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else {
            if (!this.m_9236_().isClientSide) {
                this.beePollinateGoal.stopPollinating();
            }
            return super.hurt(damageSource0, float1);
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> tagKeyFluid0) {
        this.m_20256_(this.m_20184_().add(0.0, 0.01, 0.0));
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (0.5F * this.m_20192_()), (double) (this.m_20205_() * 0.2F));
    }

    boolean closerThan(BlockPos blockPos0, int int1) {
        return blockPos0.m_123314_(this.m_20183_(), (double) int1);
    }

    abstract class BaseBeeGoal extends Goal {

        public abstract boolean canBeeUse();

        public abstract boolean canBeeContinueToUse();

        @Override
        public boolean canUse() {
            return this.canBeeUse() && !Bee.this.m_21660_();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canBeeContinueToUse() && !Bee.this.m_21660_();
        }
    }

    class BeeAttackGoal extends MeleeAttackGoal {

        BeeAttackGoal(PathfinderMob pathfinderMob0, double double1, boolean boolean2) {
            super(pathfinderMob0, double1, boolean2);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && Bee.this.m_21660_() && !Bee.this.hasStung();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && Bee.this.m_21660_() && !Bee.this.hasStung();
        }
    }

    static class BeeBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {

        BeeBecomeAngryTargetGoal(Bee bee0) {
            super(bee0, Player.class, 10, true, false, bee0::m_21674_);
        }

        @Override
        public boolean canUse() {
            return this.beeCanTarget() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            boolean $$0 = this.beeCanTarget();
            if ($$0 && this.f_26135_.getTarget() != null) {
                return super.m_8045_();
            } else {
                this.f_26137_ = null;
                return false;
            }
        }

        private boolean beeCanTarget() {
            Bee $$0 = (Bee) this.f_26135_;
            return $$0.m_21660_() && !$$0.hasStung();
        }
    }

    class BeeEnterHiveGoal extends Bee.BaseBeeGoal {

        @Override
        public boolean canBeeUse() {
            if (Bee.this.hasHive() && Bee.this.wantsToEnterHive() && Bee.this.hivePos.m_203195_(Bee.this.m_20182_(), 2.0) && Bee.this.m_9236_().getBlockEntity(Bee.this.hivePos) instanceof BeehiveBlockEntity $$1) {
                if (!$$1.isFull()) {
                    return true;
                }
                Bee.this.hivePos = null;
            }
            return false;
        }

        @Override
        public boolean canBeeContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            if (Bee.this.m_9236_().getBlockEntity(Bee.this.hivePos) instanceof BeehiveBlockEntity $$1) {
                $$1.addOccupant(Bee.this, Bee.this.hasNectar());
            }
        }
    }

    @VisibleForDebug
    public class BeeGoToHiveGoal extends Bee.BaseBeeGoal {

        public static final int MAX_TRAVELLING_TICKS = 600;

        int travellingTicks = Bee.this.m_9236_().random.nextInt(10);

        private static final int MAX_BLACKLISTED_TARGETS = 3;

        final List<BlockPos> blacklistedTargets = Lists.newArrayList();

        @Nullable
        private Path lastPath;

        private static final int TICKS_BEFORE_HIVE_DROP = 60;

        private int ticksStuck;

        BeeGoToHiveGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canBeeUse() {
            return Bee.this.hivePos != null && !Bee.this.m_21536_() && Bee.this.wantsToEnterHive() && !this.hasReachedTarget(Bee.this.hivePos) && Bee.this.m_9236_().getBlockState(Bee.this.hivePos).m_204336_(BlockTags.BEEHIVES);
        }

        @Override
        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }

        @Override
        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            super.m_8056_();
        }

        @Override
        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            Bee.this.f_21344_.stop();
            Bee.this.f_21344_.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (Bee.this.hivePos != null) {
                this.travellingTicks++;
                if (this.travellingTicks > this.m_183277_(600)) {
                    this.dropAndBlacklistHive();
                } else if (!Bee.this.f_21344_.isInProgress()) {
                    if (!Bee.this.closerThan(Bee.this.hivePos, 16)) {
                        if (Bee.this.isTooFarAway(Bee.this.hivePos)) {
                            this.dropHive();
                        } else {
                            Bee.this.pathfindRandomlyTowards(Bee.this.hivePos);
                        }
                    } else {
                        boolean $$0 = this.pathfindDirectlyTowards(Bee.this.hivePos);
                        if (!$$0) {
                            this.dropAndBlacklistHive();
                        } else if (this.lastPath != null && Bee.this.f_21344_.getPath().sameAs(this.lastPath)) {
                            this.ticksStuck++;
                            if (this.ticksStuck > 60) {
                                this.dropHive();
                                this.ticksStuck = 0;
                            }
                        } else {
                            this.lastPath = Bee.this.f_21344_.getPath();
                        }
                    }
                }
            }
        }

        private boolean pathfindDirectlyTowards(BlockPos blockPos0) {
            Bee.this.f_21344_.setMaxVisitedNodesMultiplier(10.0F);
            Bee.this.f_21344_.moveTo((double) blockPos0.m_123341_(), (double) blockPos0.m_123342_(), (double) blockPos0.m_123343_(), 1.0);
            return Bee.this.f_21344_.getPath() != null && Bee.this.f_21344_.getPath().canReach();
        }

        boolean isTargetBlacklisted(BlockPos blockPos0) {
            return this.blacklistedTargets.contains(blockPos0);
        }

        private void blacklistTarget(BlockPos blockPos0) {
            this.blacklistedTargets.add(blockPos0);
            while (this.blacklistedTargets.size() > 3) {
                this.blacklistedTargets.remove(0);
            }
        }

        void clearBlacklist() {
            this.blacklistedTargets.clear();
        }

        private void dropAndBlacklistHive() {
            if (Bee.this.hivePos != null) {
                this.blacklistTarget(Bee.this.hivePos);
            }
            this.dropHive();
        }

        private void dropHive() {
            Bee.this.hivePos = null;
            Bee.this.remainingCooldownBeforeLocatingNewHive = 200;
        }

        private boolean hasReachedTarget(BlockPos blockPos0) {
            if (Bee.this.closerThan(blockPos0, 2)) {
                return true;
            } else {
                Path $$1 = Bee.this.f_21344_.getPath();
                return $$1 != null && $$1.getTarget().equals(blockPos0) && $$1.canReach() && $$1.isDone();
            }
        }
    }

    public class BeeGoToKnownFlowerGoal extends Bee.BaseBeeGoal {

        private static final int MAX_TRAVELLING_TICKS = 600;

        int travellingTicks = Bee.this.m_9236_().random.nextInt(10);

        BeeGoToKnownFlowerGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canBeeUse() {
            return Bee.this.savedFlowerPos != null && !Bee.this.m_21536_() && this.wantsToGoToKnownFlower() && Bee.this.isFlowerValid(Bee.this.savedFlowerPos) && !Bee.this.closerThan(Bee.this.savedFlowerPos, 2);
        }

        @Override
        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }

        @Override
        public void start() {
            this.travellingTicks = 0;
            super.m_8056_();
        }

        @Override
        public void stop() {
            this.travellingTicks = 0;
            Bee.this.f_21344_.stop();
            Bee.this.f_21344_.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (Bee.this.savedFlowerPos != null) {
                this.travellingTicks++;
                if (this.travellingTicks > this.m_183277_(600)) {
                    Bee.this.savedFlowerPos = null;
                } else if (!Bee.this.f_21344_.isInProgress()) {
                    if (Bee.this.isTooFarAway(Bee.this.savedFlowerPos)) {
                        Bee.this.savedFlowerPos = null;
                    } else {
                        Bee.this.pathfindRandomlyTowards(Bee.this.savedFlowerPos);
                    }
                }
            }
        }

        private boolean wantsToGoToKnownFlower() {
            return Bee.this.ticksWithoutNectarSinceExitingHive > 2400;
        }
    }

    class BeeGrowCropGoal extends Bee.BaseBeeGoal {

        static final int GROW_CHANCE = 30;

        @Override
        public boolean canBeeUse() {
            if (Bee.this.getCropsGrownSincePollination() >= 10) {
                return false;
            } else {
                return Bee.this.f_19796_.nextFloat() < 0.3F ? false : Bee.this.hasNectar() && Bee.this.isHiveValid();
            }
        }

        @Override
        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }

        @Override
        public void tick() {
            if (Bee.this.f_19796_.nextInt(this.m_183277_(30)) == 0) {
                for (int $$0 = 1; $$0 <= 2; $$0++) {
                    BlockPos $$1 = Bee.this.m_20183_().below($$0);
                    BlockState $$2 = Bee.this.m_9236_().getBlockState($$1);
                    Block $$3 = $$2.m_60734_();
                    BlockState $$4 = null;
                    if ($$2.m_204336_(BlockTags.BEE_GROWABLES)) {
                        if ($$3 instanceof CropBlock) {
                            CropBlock $$5 = (CropBlock) $$3;
                            if (!$$5.isMaxAge($$2)) {
                                $$4 = $$5.getStateForAge($$5.getAge($$2) + 1);
                            }
                        } else if ($$3 instanceof StemBlock) {
                            int $$6 = (Integer) $$2.m_61143_(StemBlock.AGE);
                            if ($$6 < 7) {
                                $$4 = (BlockState) $$2.m_61124_(StemBlock.AGE, $$6 + 1);
                            }
                        } else if ($$2.m_60713_(Blocks.SWEET_BERRY_BUSH)) {
                            int $$7 = (Integer) $$2.m_61143_(SweetBerryBushBlock.AGE);
                            if ($$7 < 3) {
                                $$4 = (BlockState) $$2.m_61124_(SweetBerryBushBlock.AGE, $$7 + 1);
                            }
                        } else if ($$2.m_60713_(Blocks.CAVE_VINES) || $$2.m_60713_(Blocks.CAVE_VINES_PLANT)) {
                            ((BonemealableBlock) $$2.m_60734_()).performBonemeal((ServerLevel) Bee.this.m_9236_(), Bee.this.f_19796_, $$1, $$2);
                        }
                        if ($$4 != null) {
                            Bee.this.m_9236_().m_46796_(2005, $$1, 0);
                            Bee.this.m_9236_().setBlockAndUpdate($$1, $$4);
                            Bee.this.incrementNumCropsGrownSincePollination();
                        }
                    }
                }
            }
        }
    }

    class BeeHurtByOtherGoal extends HurtByTargetGoal {

        BeeHurtByOtherGoal(Bee bee0) {
            super(bee0);
        }

        @Override
        public boolean canContinueToUse() {
            return Bee.this.m_21660_() && super.m_8045_();
        }

        @Override
        protected void alertOther(Mob mob0, LivingEntity livingEntity1) {
            if (mob0 instanceof Bee && this.f_26135_.m_142582_(livingEntity1)) {
                mob0.setTarget(livingEntity1);
            }
        }
    }

    class BeeLocateHiveGoal extends Bee.BaseBeeGoal {

        @Override
        public boolean canBeeUse() {
            return Bee.this.remainingCooldownBeforeLocatingNewHive == 0 && !Bee.this.hasHive() && Bee.this.wantsToEnterHive();
        }

        @Override
        public boolean canBeeContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Bee.this.remainingCooldownBeforeLocatingNewHive = 200;
            List<BlockPos> $$0 = this.findNearbyHivesWithSpace();
            if (!$$0.isEmpty()) {
                for (BlockPos $$1 : $$0) {
                    if (!Bee.this.goToHiveGoal.isTargetBlacklisted($$1)) {
                        Bee.this.hivePos = $$1;
                        return;
                    }
                }
                Bee.this.goToHiveGoal.clearBlacklist();
                Bee.this.hivePos = (BlockPos) $$0.get(0);
            }
        }

        private List<BlockPos> findNearbyHivesWithSpace() {
            BlockPos $$0 = Bee.this.m_20183_();
            PoiManager $$1 = ((ServerLevel) Bee.this.m_9236_()).getPoiManager();
            Stream<PoiRecord> $$2 = $$1.getInRange(p_218130_ -> p_218130_.is(PoiTypeTags.BEE_HOME), $$0, 20, PoiManager.Occupancy.ANY);
            return (List<BlockPos>) $$2.map(PoiRecord::m_27257_).filter(Bee.this::m_27884_).sorted(Comparator.comparingDouble(p_148811_ -> p_148811_.m_123331_($$0))).collect(Collectors.toList());
        }
    }

    class BeeLookControl extends LookControl {

        BeeLookControl(Mob mob0) {
            super(mob0);
        }

        @Override
        public void tick() {
            if (!Bee.this.m_21660_()) {
                super.tick();
            }
        }

        @Override
        protected boolean resetXRotOnTick() {
            return !Bee.this.beePollinateGoal.isPollinating();
        }
    }

    class BeePollinateGoal extends Bee.BaseBeeGoal {

        private static final int MIN_POLLINATION_TICKS = 400;

        private static final int MIN_FIND_FLOWER_RETRY_COOLDOWN = 20;

        private static final int MAX_FIND_FLOWER_RETRY_COOLDOWN = 60;

        private final Predicate<BlockState> VALID_POLLINATION_BLOCKS = p_28074_ -> {
            if (p_28074_.m_61138_(BlockStateProperties.WATERLOGGED) && (Boolean) p_28074_.m_61143_(BlockStateProperties.WATERLOGGED)) {
                return false;
            } else if (p_28074_.m_204336_(BlockTags.FLOWERS)) {
                return p_28074_.m_60713_(Blocks.SUNFLOWER) ? p_28074_.m_61143_(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER : true;
            } else {
                return false;
            }
        };

        private static final double ARRIVAL_THRESHOLD = 0.1;

        private static final int POSITION_CHANGE_CHANCE = 25;

        private static final float SPEED_MODIFIER = 0.35F;

        private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6F;

        private static final float HOVER_POS_OFFSET = 0.33333334F;

        private int successfulPollinatingTicks;

        private int lastSoundPlayedTick;

        private boolean pollinating;

        @Nullable
        private Vec3 hoverPos;

        private int pollinatingTicks;

        private static final int MAX_POLLINATING_TICKS = 600;

        BeePollinateGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canBeeUse() {
            if (Bee.this.remainingCooldownBeforeLocatingNewFlower > 0) {
                return false;
            } else if (Bee.this.hasNectar()) {
                return false;
            } else if (Bee.this.m_9236_().isRaining()) {
                return false;
            } else {
                Optional<BlockPos> $$0 = this.findNearbyFlower();
                if ($$0.isPresent()) {
                    Bee.this.savedFlowerPos = (BlockPos) $$0.get();
                    Bee.this.f_21344_.moveTo((double) Bee.this.savedFlowerPos.m_123341_() + 0.5, (double) Bee.this.savedFlowerPos.m_123342_() + 0.5, (double) Bee.this.savedFlowerPos.m_123343_() + 0.5, 1.2F);
                    return true;
                } else {
                    Bee.this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(Bee.this.f_19796_, 20, 60);
                    return false;
                }
            }
        }

        @Override
        public boolean canBeeContinueToUse() {
            if (!this.pollinating) {
                return false;
            } else if (!Bee.this.hasSavedFlowerPos()) {
                return false;
            } else if (Bee.this.m_9236_().isRaining()) {
                return false;
            } else if (this.hasPollinatedLongEnough()) {
                return Bee.this.f_19796_.nextFloat() < 0.2F;
            } else if (Bee.this.f_19797_ % 20 == 0 && !Bee.this.isFlowerValid(Bee.this.savedFlowerPos)) {
                Bee.this.savedFlowerPos = null;
                return false;
            } else {
                return true;
            }
        }

        private boolean hasPollinatedLongEnough() {
            return this.successfulPollinatingTicks > 400;
        }

        boolean isPollinating() {
            return this.pollinating;
        }

        void stopPollinating() {
            this.pollinating = false;
        }

        @Override
        public void start() {
            this.successfulPollinatingTicks = 0;
            this.pollinatingTicks = 0;
            this.lastSoundPlayedTick = 0;
            this.pollinating = true;
            Bee.this.resetTicksWithoutNectarSinceExitingHive();
        }

        @Override
        public void stop() {
            if (this.hasPollinatedLongEnough()) {
                Bee.this.setHasNectar(true);
            }
            this.pollinating = false;
            Bee.this.f_21344_.stop();
            Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.pollinatingTicks++;
            if (this.pollinatingTicks > 600) {
                Bee.this.savedFlowerPos = null;
            } else {
                Vec3 $$0 = Vec3.atBottomCenterOf(Bee.this.savedFlowerPos).add(0.0, 0.6F, 0.0);
                if ($$0.distanceTo(Bee.this.m_20182_()) > 1.0) {
                    this.hoverPos = $$0;
                    this.setWantedPos();
                } else {
                    if (this.hoverPos == null) {
                        this.hoverPos = $$0;
                    }
                    boolean $$1 = Bee.this.m_20182_().distanceTo(this.hoverPos) <= 0.1;
                    boolean $$2 = true;
                    if (!$$1 && this.pollinatingTicks > 600) {
                        Bee.this.savedFlowerPos = null;
                    } else {
                        if ($$1) {
                            boolean $$3 = Bee.this.f_19796_.nextInt(25) == 0;
                            if ($$3) {
                                this.hoverPos = new Vec3($$0.x() + (double) this.getOffset(), $$0.y(), $$0.z() + (double) this.getOffset());
                                Bee.this.f_21344_.stop();
                            } else {
                                $$2 = false;
                            }
                            Bee.this.m_21563_().setLookAt($$0.x(), $$0.y(), $$0.z());
                        }
                        if ($$2) {
                            this.setWantedPos();
                        }
                        this.successfulPollinatingTicks++;
                        if (Bee.this.f_19796_.nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
                            this.lastSoundPlayedTick = this.successfulPollinatingTicks;
                            Bee.this.m_5496_(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }

        private void setWantedPos() {
            Bee.this.m_21566_().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.35F);
        }

        private float getOffset() {
            return (Bee.this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
        }

        private Optional<BlockPos> findNearbyFlower() {
            return this.findNearestBlock(this.VALID_POLLINATION_BLOCKS, 5.0);
        }

        private Optional<BlockPos> findNearestBlock(Predicate<BlockState> predicateBlockState0, double double1) {
            BlockPos $$2 = Bee.this.m_20183_();
            BlockPos.MutableBlockPos $$3 = new BlockPos.MutableBlockPos();
            for (int $$4 = 0; (double) $$4 <= double1; $$4 = $$4 > 0 ? -$$4 : 1 - $$4) {
                for (int $$5 = 0; (double) $$5 < double1; $$5++) {
                    for (int $$6 = 0; $$6 <= $$5; $$6 = $$6 > 0 ? -$$6 : 1 - $$6) {
                        for (int $$7 = $$6 < $$5 && $$6 > -$$5 ? $$5 : 0; $$7 <= $$5; $$7 = $$7 > 0 ? -$$7 : 1 - $$7) {
                            $$3.setWithOffset($$2, $$6, $$4 - 1, $$7);
                            if ($$2.m_123314_($$3, double1) && predicateBlockState0.test(Bee.this.m_9236_().getBlockState($$3))) {
                                return Optional.of($$3);
                            }
                        }
                    }
                }
            }
            return Optional.empty();
        }
    }

    class BeeWanderGoal extends Goal {

        private static final int WANDER_THRESHOLD = 22;

        BeeWanderGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return Bee.this.f_21344_.isDone() && Bee.this.f_19796_.nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return Bee.this.f_21344_.isInProgress();
        }

        @Override
        public void start() {
            Vec3 $$0 = this.findPos();
            if ($$0 != null) {
                Bee.this.f_21344_.moveTo(Bee.this.f_21344_.createPath(BlockPos.containing($$0), 1), 1.0);
            }
        }

        @Nullable
        private Vec3 findPos() {
            Vec3 $$1;
            if (Bee.this.isHiveValid() && !Bee.this.closerThan(Bee.this.hivePos, 22)) {
                Vec3 $$0 = Vec3.atCenterOf(Bee.this.hivePos);
                $$1 = $$0.subtract(Bee.this.m_20182_()).normalize();
            } else {
                $$1 = Bee.this.m_20252_(0.0F);
            }
            int $$3 = 8;
            Vec3 $$4 = HoverRandomPos.getPos(Bee.this, 8, 7, $$1.x, $$1.z, (float) (Math.PI / 2), 3, 1);
            return $$4 != null ? $$4 : AirAndWaterRandomPos.getPos(Bee.this, 8, 4, -2, $$1.x, $$1.z, (float) (Math.PI / 2));
        }
    }
}