package net.minecraft.world.entity.animal;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class Turtle extends Animal {

    private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BLOCK_POS);

    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<BlockPos> TRAVEL_POS = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BLOCK_POS);

    private static final EntityDataAccessor<Boolean> GOING_HOME = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> TRAVELLING = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);

    public static final Ingredient FOOD_ITEMS = Ingredient.of(Blocks.SEAGRASS.asItem());

    int layEggCounter;

    public static final Predicate<LivingEntity> BABY_ON_LAND_SELECTOR = p_289447_ -> p_289447_.isBaby() && !p_289447_.m_20069_();

    public Turtle(EntityType<? extends Turtle> entityTypeExtendsTurtle0, Level level1) {
        super(entityTypeExtendsTurtle0, level1);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.DOOR_IRON_CLOSED, -1.0F);
        this.m_21441_(BlockPathTypes.DOOR_WOOD_CLOSED, -1.0F);
        this.m_21441_(BlockPathTypes.DOOR_OPEN, -1.0F);
        this.f_21342_ = new Turtle.TurtleMoveControl(this);
        this.m_274367_(1.0F);
    }

    public void setHomePos(BlockPos blockPos0) {
        this.f_19804_.set(HOME_POS, blockPos0);
    }

    BlockPos getHomePos() {
        return this.f_19804_.get(HOME_POS);
    }

    void setTravelPos(BlockPos blockPos0) {
        this.f_19804_.set(TRAVEL_POS, blockPos0);
    }

    BlockPos getTravelPos() {
        return this.f_19804_.get(TRAVEL_POS);
    }

    public boolean hasEgg() {
        return this.f_19804_.get(HAS_EGG);
    }

    void setHasEgg(boolean boolean0) {
        this.f_19804_.set(HAS_EGG, boolean0);
    }

    public boolean isLayingEgg() {
        return this.f_19804_.get(LAYING_EGG);
    }

    void setLayingEgg(boolean boolean0) {
        this.layEggCounter = boolean0 ? 1 : 0;
        this.f_19804_.set(LAYING_EGG, boolean0);
    }

    boolean isGoingHome() {
        return this.f_19804_.get(GOING_HOME);
    }

    void setGoingHome(boolean boolean0) {
        this.f_19804_.set(GOING_HOME, boolean0);
    }

    boolean isTravelling() {
        return this.f_19804_.get(TRAVELLING);
    }

    void setTravelling(boolean boolean0) {
        this.f_19804_.set(TRAVELLING, boolean0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(HOME_POS, BlockPos.ZERO);
        this.f_19804_.define(HAS_EGG, false);
        this.f_19804_.define(TRAVEL_POS, BlockPos.ZERO);
        this.f_19804_.define(GOING_HOME, false);
        this.f_19804_.define(TRAVELLING, false);
        this.f_19804_.define(LAYING_EGG, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("HomePosX", this.getHomePos().m_123341_());
        compoundTag0.putInt("HomePosY", this.getHomePos().m_123342_());
        compoundTag0.putInt("HomePosZ", this.getHomePos().m_123343_());
        compoundTag0.putBoolean("HasEgg", this.hasEgg());
        compoundTag0.putInt("TravelPosX", this.getTravelPos().m_123341_());
        compoundTag0.putInt("TravelPosY", this.getTravelPos().m_123342_());
        compoundTag0.putInt("TravelPosZ", this.getTravelPos().m_123343_());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        int $$1 = compoundTag0.getInt("HomePosX");
        int $$2 = compoundTag0.getInt("HomePosY");
        int $$3 = compoundTag0.getInt("HomePosZ");
        this.setHomePos(new BlockPos($$1, $$2, $$3));
        super.readAdditionalSaveData(compoundTag0);
        this.setHasEgg(compoundTag0.getBoolean("HasEgg"));
        int $$4 = compoundTag0.getInt("TravelPosX");
        int $$5 = compoundTag0.getInt("TravelPosY");
        int $$6 = compoundTag0.getInt("TravelPosZ");
        this.setTravelPos(new BlockPos($$4, $$5, $$6));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        this.setHomePos(this.m_20183_());
        this.setTravelPos(BlockPos.ZERO);
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    public static boolean checkTurtleSpawnRules(EntityType<Turtle> entityTypeTurtle0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return blockPos3.m_123342_() < levelAccessor1.m_5736_() + 4 && TurtleEggBlock.onSand(levelAccessor1, blockPos3) && m_186209_(levelAccessor1, blockPos3);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new Turtle.TurtlePanicGoal(this, 1.2));
        this.f_21345_.addGoal(1, new Turtle.TurtleBreedGoal(this, 1.0));
        this.f_21345_.addGoal(1, new Turtle.TurtleLayEggGoal(this, 1.0));
        this.f_21345_.addGoal(2, new TemptGoal(this, 1.1, FOOD_ITEMS, false));
        this.f_21345_.addGoal(3, new Turtle.TurtleGoToWaterGoal(this, 1.0));
        this.f_21345_.addGoal(4, new Turtle.TurtleGoHomeGoal(this, 1.0));
        this.f_21345_.addGoal(7, new Turtle.TurtleTravelGoal(this, 1.0));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(9, new Turtle.TurtleRandomStrollGoal(this, 1.0, 100));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return !this.m_20069_() && this.m_20096_() && !this.m_6162_() ? SoundEvents.TURTLE_AMBIENT_LAND : super.m_7515_();
    }

    @Override
    protected void playSwimSound(float float0) {
        super.m_5625_(float0 * 1.5F);
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.TURTLE_SWIM;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return this.m_6162_() ? SoundEvents.TURTLE_HURT_BABY : SoundEvents.TURTLE_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.m_6162_() ? SoundEvents.TURTLE_DEATH_BABY : SoundEvents.TURTLE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        SoundEvent $$2 = this.m_6162_() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
        this.m_5496_($$2, 0.15F, 1.0F);
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Override
    protected float nextStep() {
        return this.f_19788_ + 0.15F;
    }

    @Override
    public float getScale() {
        return this.m_6162_() ? 0.3F : 1.0F;
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        return new Turtle.TurtlePathNavigation(this, level0);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.TURTLE.create(serverLevel0);
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return itemStack0.is(Blocks.SEAGRASS.asItem());
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        if (!this.isGoingHome() && levelReader1.m_6425_(blockPos0).is(FluidTags.WATER)) {
            return 10.0F;
        } else {
            return TurtleEggBlock.onSand(levelReader1, blockPos0) ? 10.0F : levelReader1.getPathfindingCostFromLightLevels(blockPos0);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.m_6084_() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0) {
            BlockPos $$0 = this.m_20183_();
            if (TurtleEggBlock.onSand(this.m_9236_(), $$0)) {
                this.m_9236_().m_46796_(2001, $$0, Block.getId(this.m_9236_().getBlockState($$0.below())));
            }
        }
    }

    @Override
    protected void ageBoundaryReached() {
        super.m_30232_();
        if (!this.m_6162_() && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.m_20000_(Items.SCUTE, 1);
        }
    }

    @Override
    public void travel(Vec3 vec0) {
        if (this.m_6109_() && this.m_20069_()) {
            this.m_19920_(0.1F, vec0);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null && (!this.isGoingHome() || !this.getHomePos().m_203195_(this.m_20182_(), 20.0))) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(vec0);
        }
    }

    @Override
    public boolean canBeLeashed(Player player0) {
        return false;
    }

    @Override
    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
        this.m_6469_(this.m_269291_().lightningBolt(), Float.MAX_VALUE);
    }

    static class TurtleBreedGoal extends BreedGoal {

        private final Turtle turtle;

        TurtleBreedGoal(Turtle turtle0, double double1) {
            super(turtle0, double1);
            this.turtle = turtle0;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.turtle.hasEgg();
        }

        @Override
        protected void breed() {
            ServerPlayer $$0 = this.f_25113_.getLoveCause();
            if ($$0 == null && this.f_25115_.getLoveCause() != null) {
                $$0 = this.f_25115_.getLoveCause();
            }
            if ($$0 != null) {
                $$0.m_36220_(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger($$0, this.f_25113_, this.f_25115_, null);
            }
            this.turtle.setHasEgg(true);
            this.f_25113_.m_146762_(6000);
            this.f_25115_.m_146762_(6000);
            this.f_25113_.resetLove();
            this.f_25115_.resetLove();
            RandomSource $$1 = this.f_25113_.m_217043_();
            if (this.f_25114_.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.f_25114_.m_7967_(new ExperienceOrb(this.f_25114_, this.f_25113_.m_20185_(), this.f_25113_.m_20186_(), this.f_25113_.m_20189_(), $$1.nextInt(7) + 1));
            }
        }
    }

    static class TurtleGoHomeGoal extends Goal {

        private final Turtle turtle;

        private final double speedModifier;

        private boolean stuck;

        private int closeToHomeTryTicks;

        private static final int GIVE_UP_TICKS = 600;

        TurtleGoHomeGoal(Turtle turtle0, double double1) {
            this.turtle = turtle0;
            this.speedModifier = double1;
        }

        @Override
        public boolean canUse() {
            if (this.turtle.m_6162_()) {
                return false;
            } else if (this.turtle.hasEgg()) {
                return true;
            } else {
                return this.turtle.m_217043_().nextInt(m_186073_(700)) != 0 ? false : !this.turtle.getHomePos().m_203195_(this.turtle.m_20182_(), 64.0);
            }
        }

        @Override
        public void start() {
            this.turtle.setGoingHome(true);
            this.stuck = false;
            this.closeToHomeTryTicks = 0;
        }

        @Override
        public void stop() {
            this.turtle.setGoingHome(false);
        }

        @Override
        public boolean canContinueToUse() {
            return !this.turtle.getHomePos().m_203195_(this.turtle.m_20182_(), 7.0) && !this.stuck && this.closeToHomeTryTicks <= this.m_183277_(600);
        }

        @Override
        public void tick() {
            BlockPos $$0 = this.turtle.getHomePos();
            boolean $$1 = $$0.m_203195_(this.turtle.m_20182_(), 16.0);
            if ($$1) {
                this.closeToHomeTryTicks++;
            }
            if (this.turtle.m_21573_().isDone()) {
                Vec3 $$2 = Vec3.atBottomCenterOf($$0);
                Vec3 $$3 = DefaultRandomPos.getPosTowards(this.turtle, 16, 3, $$2, (float) (Math.PI / 10));
                if ($$3 == null) {
                    $$3 = DefaultRandomPos.getPosTowards(this.turtle, 8, 7, $$2, (float) (Math.PI / 2));
                }
                if ($$3 != null && !$$1 && !this.turtle.m_9236_().getBlockState(BlockPos.containing($$3)).m_60713_(Blocks.WATER)) {
                    $$3 = DefaultRandomPos.getPosTowards(this.turtle, 16, 5, $$2, (float) (Math.PI / 2));
                }
                if ($$3 == null) {
                    this.stuck = true;
                    return;
                }
                this.turtle.m_21573_().moveTo($$3.x, $$3.y, $$3.z, this.speedModifier);
            }
        }
    }

    static class TurtleGoToWaterGoal extends MoveToBlockGoal {

        private static final int GIVE_UP_TICKS = 1200;

        private final Turtle turtle;

        TurtleGoToWaterGoal(Turtle turtle0, double double1) {
            super(turtle0, turtle0.m_6162_() ? 2.0 : double1, 24);
            this.turtle = turtle0;
            this.f_25603_ = -1;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.turtle.m_20069_() && this.f_25601_ <= 1200 && this.isValidTarget(this.turtle.m_9236_(), this.f_25602_);
        }

        @Override
        public boolean canUse() {
            if (this.turtle.m_6162_() && !this.turtle.m_20069_()) {
                return super.canUse();
            } else {
                return !this.turtle.isGoingHome() && !this.turtle.m_20069_() && !this.turtle.hasEgg() ? super.canUse() : false;
            }
        }

        @Override
        public boolean shouldRecalculatePath() {
            return this.f_25601_ % 160 == 0;
        }

        @Override
        protected boolean isValidTarget(LevelReader levelReader0, BlockPos blockPos1) {
            return levelReader0.m_8055_(blockPos1).m_60713_(Blocks.WATER);
        }
    }

    static class TurtleLayEggGoal extends MoveToBlockGoal {

        private final Turtle turtle;

        TurtleLayEggGoal(Turtle turtle0, double double1) {
            super(turtle0, double1, 16);
            this.turtle = turtle0;
        }

        @Override
        public boolean canUse() {
            return this.turtle.hasEgg() && this.turtle.getHomePos().m_203195_(this.turtle.m_20182_(), 9.0) ? super.canUse() : false;
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.turtle.hasEgg() && this.turtle.getHomePos().m_203195_(this.turtle.m_20182_(), 9.0);
        }

        @Override
        public void tick() {
            super.tick();
            BlockPos $$0 = this.turtle.m_20183_();
            if (!this.turtle.m_20069_() && this.m_25625_()) {
                if (this.turtle.layEggCounter < 1) {
                    this.turtle.setLayingEgg(true);
                } else if (this.turtle.layEggCounter > this.m_183277_(200)) {
                    Level $$1 = this.turtle.m_9236_();
                    $$1.playSound(null, $$0, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + $$1.random.nextFloat() * 0.2F);
                    BlockPos $$2 = this.f_25602_.above();
                    BlockState $$3 = (BlockState) Blocks.TURTLE_EGG.defaultBlockState().m_61124_(TurtleEggBlock.EGGS, this.turtle.f_19796_.nextInt(4) + 1);
                    $$1.setBlock($$2, $$3, 3);
                    $$1.m_220407_(GameEvent.BLOCK_PLACE, $$2, GameEvent.Context.of(this.turtle, $$3));
                    this.turtle.setHasEgg(false);
                    this.turtle.setLayingEgg(false);
                    this.turtle.m_27601_(600);
                }
                if (this.turtle.isLayingEgg()) {
                    this.turtle.layEggCounter++;
                }
            }
        }

        @Override
        protected boolean isValidTarget(LevelReader levelReader0, BlockPos blockPos1) {
            return !levelReader0.isEmptyBlock(blockPos1.above()) ? false : TurtleEggBlock.isSand(levelReader0, blockPos1);
        }
    }

    static class TurtleMoveControl extends MoveControl {

        private final Turtle turtle;

        TurtleMoveControl(Turtle turtle0) {
            super(turtle0);
            this.turtle = turtle0;
        }

        private void updateSpeed() {
            if (this.turtle.m_20069_()) {
                this.turtle.m_20256_(this.turtle.m_20184_().add(0.0, 0.005, 0.0));
                if (!this.turtle.getHomePos().m_203195_(this.turtle.m_20182_(), 16.0)) {
                    this.turtle.m_7910_(Math.max(this.turtle.m_6113_() / 2.0F, 0.08F));
                }
                if (this.turtle.m_6162_()) {
                    this.turtle.m_7910_(Math.max(this.turtle.m_6113_() / 3.0F, 0.06F));
                }
            } else if (this.turtle.m_20096_()) {
                this.turtle.m_7910_(Math.max(this.turtle.m_6113_() / 2.0F, 0.06F));
            }
        }

        @Override
        public void tick() {
            this.updateSpeed();
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.turtle.m_21573_().isDone()) {
                double $$0 = this.f_24975_ - this.turtle.m_20185_();
                double $$1 = this.f_24976_ - this.turtle.m_20186_();
                double $$2 = this.f_24977_ - this.turtle.m_20189_();
                double $$3 = Math.sqrt($$0 * $$0 + $$1 * $$1 + $$2 * $$2);
                if ($$3 < 1.0E-5F) {
                    this.f_24974_.setSpeed(0.0F);
                } else {
                    $$1 /= $$3;
                    float $$4 = (float) (Mth.atan2($$2, $$0) * 180.0F / (float) Math.PI) - 90.0F;
                    this.turtle.m_146922_(this.m_24991_(this.turtle.m_146908_(), $$4, 90.0F));
                    this.turtle.f_20883_ = this.turtle.m_146908_();
                    float $$5 = (float) (this.f_24978_ * this.turtle.m_21133_(Attributes.MOVEMENT_SPEED));
                    this.turtle.m_7910_(Mth.lerp(0.125F, this.turtle.m_6113_(), $$5));
                    this.turtle.m_20256_(this.turtle.m_20184_().add(0.0, (double) this.turtle.m_6113_() * $$1 * 0.1, 0.0));
                }
            } else {
                this.turtle.m_7910_(0.0F);
            }
        }
    }

    static class TurtlePanicGoal extends PanicGoal {

        TurtlePanicGoal(Turtle turtle0, double double1) {
            super(turtle0, double1);
        }

        @Override
        public boolean canUse() {
            if (!this.m_202729_()) {
                return false;
            } else {
                BlockPos $$0 = this.m_198172_(this.f_25684_.m_9236_(), this.f_25684_, 7);
                if ($$0 != null) {
                    this.f_25686_ = (double) $$0.m_123341_();
                    this.f_25687_ = (double) $$0.m_123342_();
                    this.f_25688_ = (double) $$0.m_123343_();
                    return true;
                } else {
                    return this.m_25702_();
                }
            }
        }
    }

    static class TurtlePathNavigation extends AmphibiousPathNavigation {

        TurtlePathNavigation(Turtle turtle0, Level level1) {
            super(turtle0, level1);
        }

        @Override
        public boolean isStableDestination(BlockPos blockPos0) {
            if (this.f_26494_ instanceof Turtle $$1 && $$1.isTravelling()) {
                return this.f_26495_.getBlockState(blockPos0).m_60713_(Blocks.WATER);
            }
            return !this.f_26495_.getBlockState(blockPos0.below()).m_60795_();
        }
    }

    static class TurtleRandomStrollGoal extends RandomStrollGoal {

        private final Turtle turtle;

        TurtleRandomStrollGoal(Turtle turtle0, double double1, int int2) {
            super(turtle0, double1, int2);
            this.turtle = turtle0;
        }

        @Override
        public boolean canUse() {
            return !this.f_25725_.m_20069_() && !this.turtle.isGoingHome() && !this.turtle.hasEgg() ? super.canUse() : false;
        }
    }

    static class TurtleTravelGoal extends Goal {

        private final Turtle turtle;

        private final double speedModifier;

        private boolean stuck;

        TurtleTravelGoal(Turtle turtle0, double double1) {
            this.turtle = turtle0;
            this.speedModifier = double1;
        }

        @Override
        public boolean canUse() {
            return !this.turtle.isGoingHome() && !this.turtle.hasEgg() && this.turtle.m_20069_();
        }

        @Override
        public void start() {
            int $$0 = 512;
            int $$1 = 4;
            RandomSource $$2 = this.turtle.f_19796_;
            int $$3 = $$2.nextInt(1025) - 512;
            int $$4 = $$2.nextInt(9) - 4;
            int $$5 = $$2.nextInt(1025) - 512;
            if ((double) $$4 + this.turtle.m_20186_() > (double) (this.turtle.m_9236_().getSeaLevel() - 1)) {
                $$4 = 0;
            }
            BlockPos $$6 = BlockPos.containing((double) $$3 + this.turtle.m_20185_(), (double) $$4 + this.turtle.m_20186_(), (double) $$5 + this.turtle.m_20189_());
            this.turtle.setTravelPos($$6);
            this.turtle.setTravelling(true);
            this.stuck = false;
        }

        @Override
        public void tick() {
            if (this.turtle.m_21573_().isDone()) {
                Vec3 $$0 = Vec3.atBottomCenterOf(this.turtle.getTravelPos());
                Vec3 $$1 = DefaultRandomPos.getPosTowards(this.turtle, 16, 3, $$0, (float) (Math.PI / 10));
                if ($$1 == null) {
                    $$1 = DefaultRandomPos.getPosTowards(this.turtle, 8, 7, $$0, (float) (Math.PI / 2));
                }
                if ($$1 != null) {
                    int $$2 = Mth.floor($$1.x);
                    int $$3 = Mth.floor($$1.z);
                    int $$4 = 34;
                    if (!this.turtle.m_9236_().m_151572_($$2 - 34, $$3 - 34, $$2 + 34, $$3 + 34)) {
                        $$1 = null;
                    }
                }
                if ($$1 == null) {
                    this.stuck = true;
                    return;
                }
                this.turtle.m_21573_().moveTo($$1.x, $$1.y, $$1.z, this.speedModifier);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !this.turtle.m_21573_().isDone() && !this.stuck && !this.turtle.isGoingHome() && !this.turtle.m_27593_() && !this.turtle.hasEgg();
        }

        @Override
        public void stop() {
            this.turtle.setTravelling(false);
            super.stop();
        }
    }
}