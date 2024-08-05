package net.minecraft.world.entity.monster;

import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public class Strider extends Animal implements ItemSteerable, Saddleable {

    private static final UUID SUFFOCATING_MODIFIER_UUID = UUID.fromString("9e362924-01de-4ddd-a2b2-d0f7a405a174");

    private static final AttributeModifier SUFFOCATING_MODIFIER = new AttributeModifier(SUFFOCATING_MODIFIER_UUID, "Strider suffocating modifier", -0.34F, AttributeModifier.Operation.MULTIPLY_BASE);

    private static final float SUFFOCATE_STEERING_MODIFIER = 0.35F;

    private static final float STEERING_MODIFIER = 0.55F;

    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WARPED_FUNGUS);

    private static final Ingredient TEMPT_ITEMS = Ingredient.of(Items.WARPED_FUNGUS, Items.WARPED_FUNGUS_ON_A_STICK);

    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DATA_SUFFOCATING = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.BOOLEAN);

    private final ItemBasedSteering steering = new ItemBasedSteering(this.f_19804_, DATA_BOOST_TIME, DATA_SADDLE_ID);

    @Nullable
    private TemptGoal temptGoal;

    @Nullable
    private PanicGoal panicGoal;

    public Strider(EntityType<? extends Strider> entityTypeExtendsStrider0, Level level1) {
        super(entityTypeExtendsStrider0, level1);
        this.f_19850_ = true;
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.LAVA, 0.0F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    }

    public static boolean checkStriderSpawnRules(EntityType<Strider> entityTypeStrider0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        BlockPos.MutableBlockPos $$5 = blockPos3.mutable();
        do {
            $$5.move(Direction.UP);
        } while (levelAccessor1.m_6425_($$5).is(FluidTags.LAVA));
        return levelAccessor1.m_8055_($$5).m_60795_();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (DATA_BOOST_TIME.equals(entityDataAccessor0) && this.m_9236_().isClientSide) {
            this.steering.onSynced();
        }
        super.m_7350_(entityDataAccessor0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_BOOST_TIME, 0);
        this.f_19804_.define(DATA_SUFFOCATING, false);
        this.f_19804_.define(DATA_SADDLE_ID, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        this.steering.addAdditionalSaveData(compoundTag0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.steering.readAdditionalSaveData(compoundTag0);
    }

    @Override
    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    @Override
    public boolean isSaddleable() {
        return this.m_6084_() && !this.m_6162_();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource soundSource0) {
        this.steering.setSaddle(true);
        if (soundSource0 != null) {
            this.m_9236_().playSound(null, this, SoundEvents.STRIDER_SADDLE, soundSource0, 0.5F, 1.0F);
        }
    }

    @Override
    protected void registerGoals() {
        this.panicGoal = new PanicGoal(this, 1.65);
        this.f_21345_.addGoal(1, this.panicGoal);
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.temptGoal = new TemptGoal(this, 1.4, TEMPT_ITEMS, false);
        this.f_21345_.addGoal(3, this.temptGoal);
        this.f_21345_.addGoal(4, new Strider.StriderGoToLavaGoal(this, 1.0));
        this.f_21345_.addGoal(5, new FollowParentGoal(this, 1.0));
        this.f_21345_.addGoal(7, new RandomStrollGoal(this, 1.0, 60));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Strider.class, 8.0F));
    }

    public void setSuffocating(boolean boolean0) {
        this.f_19804_.set(DATA_SUFFOCATING, boolean0);
        AttributeInstance $$1 = this.m_21051_(Attributes.MOVEMENT_SPEED);
        if ($$1 != null) {
            $$1.removeModifier(SUFFOCATING_MODIFIER_UUID);
            if (boolean0) {
                $$1.addTransientModifier(SUFFOCATING_MODIFIER);
            }
        }
    }

    public boolean isSuffocating() {
        return this.f_19804_.get(DATA_SUFFOCATING);
    }

    @Override
    public boolean canStandOnFluid(FluidState fluidState0) {
        return fluidState0.is(FluidTags.LAVA);
    }

    @Override
    public double getPassengersRidingOffset() {
        float $$0 = Math.min(0.25F, this.f_267362_.speed());
        float $$1 = this.f_267362_.position();
        return (double) this.m_20206_() - 0.19 + (double) (0.12F * Mth.cos($$1 * 1.5F) * 2.0F * $$0);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        return levelReader0.m_45784_(this);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.m_146895_() instanceof Player $$0 && ($$0.m_21205_().is(Items.WARPED_FUNGUS_ON_A_STICK) || $$0.m_21206_().is(Items.WARPED_FUNGUS_ON_A_STICK))) {
            return $$0;
        }
        return null;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity0) {
        Vec3[] $$1 = new Vec3[] { m_19903_((double) this.m_20205_(), (double) livingEntity0.m_20205_(), livingEntity0.m_146908_()), m_19903_((double) this.m_20205_(), (double) livingEntity0.m_20205_(), livingEntity0.m_146908_() - 22.5F), m_19903_((double) this.m_20205_(), (double) livingEntity0.m_20205_(), livingEntity0.m_146908_() + 22.5F), m_19903_((double) this.m_20205_(), (double) livingEntity0.m_20205_(), livingEntity0.m_146908_() - 45.0F), m_19903_((double) this.m_20205_(), (double) livingEntity0.m_20205_(), livingEntity0.m_146908_() + 45.0F) };
        Set<BlockPos> $$2 = Sets.newLinkedHashSet();
        double $$3 = this.m_20191_().maxY;
        double $$4 = this.m_20191_().minY - 0.5;
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();
        for (Vec3 $$6 : $$1) {
            $$5.set(this.m_20185_() + $$6.x, $$3, this.m_20189_() + $$6.z);
            for (double $$7 = $$3; $$7 > $$4; $$7--) {
                $$2.add($$5.immutable());
                $$5.move(Direction.DOWN);
            }
        }
        for (BlockPos $$8 : $$2) {
            if (!this.m_9236_().getFluidState($$8).is(FluidTags.LAVA)) {
                double $$9 = this.m_9236_().m_45573_($$8);
                if (DismountHelper.isBlockFloorValid($$9)) {
                    Vec3 $$10 = Vec3.upFromBottomCenterOf($$8, $$9);
                    UnmodifiableIterator var14 = livingEntity0.getDismountPoses().iterator();
                    while (var14.hasNext()) {
                        Pose $$11 = (Pose) var14.next();
                        AABB $$12 = livingEntity0.getLocalBoundsForPose($$11);
                        if (DismountHelper.canDismountTo(this.m_9236_(), livingEntity0, $$12.move($$10))) {
                            livingEntity0.m_20124_($$11);
                            return $$10;
                        }
                    }
                }
            }
        }
        return new Vec3(this.m_20185_(), this.m_20191_().maxY, this.m_20189_());
    }

    @Override
    protected void tickRidden(Player player0, Vec3 vec1) {
        this.m_19915_(player0.m_146908_(), player0.m_146909_() * 0.5F);
        this.f_19859_ = this.f_20883_ = this.f_20885_ = this.m_146908_();
        this.steering.tickBoost();
        super.m_274498_(player0, vec1);
    }

    @Override
    protected Vec3 getRiddenInput(Player player0, Vec3 vec1) {
        return new Vec3(0.0, 0.0, 1.0);
    }

    @Override
    protected float getRiddenSpeed(Player player0) {
        return (float) (this.m_21133_(Attributes.MOVEMENT_SPEED) * (double) (this.isSuffocating() ? 0.35F : 0.55F) * (double) this.steering.boostFactor());
    }

    @Override
    protected float nextStep() {
        return this.f_19788_ + 0.6F;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(this.m_20077_() ? SoundEvents.STRIDER_STEP_LAVA : SoundEvents.STRIDER_STEP, 1.0F, 1.0F);
    }

    @Override
    public boolean boost() {
        return this.steering.boost(this.m_217043_());
    }

    @Override
    protected void checkFallDamage(double double0, boolean boolean1, BlockState blockState2, BlockPos blockPos3) {
        this.m_20101_();
        if (this.m_20077_()) {
            this.m_183634_();
        } else {
            super.m_7840_(double0, boolean1, blockState2, blockPos3);
        }
    }

    @Override
    public void tick() {
        if (this.isBeingTempted() && this.f_19796_.nextInt(140) == 0) {
            this.m_5496_(SoundEvents.STRIDER_HAPPY, 1.0F, this.m_6100_());
        } else if (this.isPanicking() && this.f_19796_.nextInt(60) == 0) {
            this.m_5496_(SoundEvents.STRIDER_RETREAT, 1.0F, this.m_6100_());
        }
        if (!this.m_21525_()) {
            boolean $$2;
            boolean var10000;
            label36: {
                BlockState $$0 = this.m_9236_().getBlockState(this.m_20183_());
                BlockState $$1 = this.m_217002_();
                $$2 = $$0.m_204336_(BlockTags.STRIDER_WARM_BLOCKS) || $$1.m_204336_(BlockTags.STRIDER_WARM_BLOCKS) || this.m_204036_(FluidTags.LAVA) > 0.0;
                if (this.m_20202_() instanceof Strider $$3 && $$3.isSuffocating()) {
                    var10000 = true;
                    break label36;
                }
                var10000 = false;
            }
            boolean $$4 = var10000;
            this.setSuffocating(!$$2 || $$4);
        }
        super.m_8119_();
        this.floatStrider();
        this.m_20101_();
    }

    private boolean isPanicking() {
        return this.panicGoal != null && this.panicGoal.isRunning();
    }

    private boolean isBeingTempted() {
        return this.temptGoal != null && this.temptGoal.isRunning();
    }

    @Override
    protected boolean shouldPassengersInheritMalus() {
        return true;
    }

    private void floatStrider() {
        if (this.m_20077_()) {
            CollisionContext $$0 = CollisionContext.of(this);
            if ($$0.isAbove(LiquidBlock.STABLE_SHAPE, this.m_20183_(), true) && !this.m_9236_().getFluidState(this.m_20183_().above()).is(FluidTags.LAVA)) {
                this.m_6853_(true);
            } else {
                this.m_20256_(this.m_20184_().scale(0.5).add(0.0, 0.05, 0.0));
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.175F).add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return !this.isPanicking() && !this.isBeingTempted() ? SoundEvents.STRIDER_AMBIENT : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.STRIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.STRIDER_DEATH;
    }

    @Override
    protected boolean canAddPassenger(Entity entity0) {
        return !this.m_20160_() && !this.m_204029_(FluidTags.LAVA);
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        return new Strider.StriderPathNavigation(this, level0);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        if (levelReader1.m_8055_(blockPos0).m_60819_().is(FluidTags.LAVA)) {
            return 10.0F;
        } else {
            return this.m_20077_() ? Float.NEGATIVE_INFINITY : 0.0F;
        }
    }

    @Nullable
    public Strider getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.STRIDER.create(serverLevel0);
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return FOOD_ITEMS.test(itemStack0);
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.isSaddled()) {
            this.m_19998_(Items.SADDLE);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        boolean $$2 = this.isFood(player0.m_21120_(interactionHand1));
        if (!$$2 && this.isSaddled() && !this.m_20160_() && !player0.isSecondaryUseActive()) {
            if (!this.m_9236_().isClientSide) {
                player0.m_20329_(this);
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            InteractionResult $$3 = super.mobInteract(player0, interactionHand1);
            if (!$$3.consumesAction()) {
                ItemStack $$4 = player0.m_21120_(interactionHand1);
                return $$4.is(Items.SADDLE) ? $$4.interactLivingEntity(player0, this, interactionHand1) : InteractionResult.PASS;
            } else {
                if ($$2 && !this.m_20067_()) {
                    this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.STRIDER_EAT, this.m_5720_(), 1.0F, 1.0F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F);
                }
                return $$3;
            }
        }
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (0.6F * this.m_20192_()), (double) (this.m_20205_() * 0.4F));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        if (this.m_6162_()) {
            return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
        } else {
            RandomSource $$5 = serverLevelAccessor0.m_213780_();
            if ($$5.nextInt(30) == 0) {
                Mob $$6 = EntityType.ZOMBIFIED_PIGLIN.create(serverLevelAccessor0.getLevel());
                if ($$6 != null) {
                    spawnGroupData3 = this.spawnJockey(serverLevelAccessor0, difficultyInstance1, $$6, new Zombie.ZombieGroupData(Zombie.getSpawnAsBabyOdds($$5), false));
                    $$6.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK));
                    this.equipSaddle(null);
                }
            } else if ($$5.nextInt(10) == 0) {
                AgeableMob $$7 = EntityType.STRIDER.create(serverLevelAccessor0.getLevel());
                if ($$7 != null) {
                    $$7.setAge(-24000);
                    spawnGroupData3 = this.spawnJockey(serverLevelAccessor0, difficultyInstance1, $$7, null);
                }
            } else {
                spawnGroupData3 = new AgeableMob.AgeableMobGroupData(0.5F);
            }
            return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
        }
    }

    private SpawnGroupData spawnJockey(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, Mob mob2, @Nullable SpawnGroupData spawnGroupData3) {
        mob2.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), 0.0F);
        mob2.finalizeSpawn(serverLevelAccessor0, difficultyInstance1, MobSpawnType.JOCKEY, spawnGroupData3, null);
        mob2.startRiding(this, true);
        return new AgeableMob.AgeableMobGroupData(0.0F);
    }

    static class StriderGoToLavaGoal extends MoveToBlockGoal {

        private final Strider strider;

        StriderGoToLavaGoal(Strider strider0, double double1) {
            super(strider0, double1, 8, 2);
            this.strider = strider0;
        }

        @Override
        public BlockPos getMoveToTarget() {
            return this.f_25602_;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.strider.m_20077_() && this.isValidTarget(this.strider.m_9236_(), this.f_25602_);
        }

        @Override
        public boolean canUse() {
            return !this.strider.m_20077_() && super.canUse();
        }

        @Override
        public boolean shouldRecalculatePath() {
            return this.f_25601_ % 20 == 0;
        }

        @Override
        protected boolean isValidTarget(LevelReader levelReader0, BlockPos blockPos1) {
            return levelReader0.m_8055_(blockPos1).m_60713_(Blocks.LAVA) && levelReader0.m_8055_(blockPos1.above()).m_60647_(levelReader0, blockPos1, PathComputationType.LAND);
        }
    }

    static class StriderPathNavigation extends GroundPathNavigation {

        StriderPathNavigation(Strider strider0, Level level1) {
            super(strider0, level1);
        }

        @Override
        protected PathFinder createPathFinder(int int0) {
            this.f_26508_ = new WalkNodeEvaluator();
            this.f_26508_.setCanPassDoors(true);
            return new PathFinder(this.f_26508_, int0);
        }

        @Override
        protected boolean hasValidPathType(BlockPathTypes blockPathTypes0) {
            return blockPathTypes0 != BlockPathTypes.LAVA && blockPathTypes0 != BlockPathTypes.DAMAGE_FIRE && blockPathTypes0 != BlockPathTypes.DANGER_FIRE ? super.hasValidPathType(blockPathTypes0) : true;
        }

        @Override
        public boolean isStableDestination(BlockPos blockPos0) {
            return this.f_26495_.getBlockState(blockPos0).m_60713_(Blocks.LAVA) || super.m_6342_(blockPos0);
        }
    }
}