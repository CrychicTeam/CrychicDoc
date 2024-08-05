package net.minecraft.world.entity.animal.frog;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

public class Frog extends Animal implements VariantHolder<FrogVariant> {

    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.SLIME_BALL);

    protected static final ImmutableList<SensorType<? extends Sensor<? super Frog>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FROG_ATTACKABLES, SensorType.FROG_TEMPTATIONS, SensorType.IS_IN_WATER);

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.BREED_TARGET, MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, new MemoryModuleType[] { MemoryModuleType.IS_TEMPTED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.IS_IN_WATER, MemoryModuleType.IS_PREGNANT, MemoryModuleType.IS_PANICKING, MemoryModuleType.UNREACHABLE_TONGUE_TARGETS });

    private static final EntityDataAccessor<FrogVariant> DATA_VARIANT_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.FROG_VARIANT);

    private static final EntityDataAccessor<OptionalInt> DATA_TONGUE_TARGET_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);

    private static final int FROG_FALL_DAMAGE_REDUCTION = 5;

    public static final String VARIANT_KEY = "variant";

    public final AnimationState jumpAnimationState = new AnimationState();

    public final AnimationState croakAnimationState = new AnimationState();

    public final AnimationState tongueAnimationState = new AnimationState();

    public final AnimationState swimIdleAnimationState = new AnimationState();

    public Frog(EntityType<? extends Animal> entityTypeExtendsAnimal0, Level level1) {
        super(entityTypeExtendsAnimal0, level1);
        this.f_21365_ = new Frog.FrogLookControl(this);
        this.m_21441_(BlockPathTypes.WATER, 4.0F);
        this.m_21441_(BlockPathTypes.TRAPDOOR, -1.0F);
        this.f_21342_ = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.m_274367_(1.0F);
    }

    @Override
    protected Brain.Provider<Frog> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic0) {
        return FrogAi.makeBrain(this.brainProvider().makeBrain(dynamic0));
    }

    @Override
    public Brain<Frog> getBrain() {
        return super.m_6274_();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_VARIANT_ID, FrogVariant.TEMPERATE);
        this.f_19804_.define(DATA_TONGUE_TARGET_ID, OptionalInt.empty());
    }

    public void eraseTongueTarget() {
        this.f_19804_.set(DATA_TONGUE_TARGET_ID, OptionalInt.empty());
    }

    public Optional<Entity> getTongueTarget() {
        return this.f_19804_.get(DATA_TONGUE_TARGET_ID).stream().mapToObj(this.m_9236_()::m_6815_).filter(Objects::nonNull).findFirst();
    }

    public void setTongueTarget(Entity entity0) {
        this.f_19804_.set(DATA_TONGUE_TARGET_ID, OptionalInt.of(entity0.getId()));
    }

    @Override
    public int getHeadRotSpeed() {
        return 35;
    }

    @Override
    public int getMaxHeadYRot() {
        return 5;
    }

    public FrogVariant getVariant() {
        return this.f_19804_.get(DATA_VARIANT_ID);
    }

    public void setVariant(FrogVariant frogVariant0) {
        this.f_19804_.set(DATA_VARIANT_ID, frogVariant0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putString("variant", BuiltInRegistries.FROG_VARIANT.getKey(this.getVariant()).toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        FrogVariant $$1 = BuiltInRegistries.FROG_VARIANT.get(ResourceLocation.tryParse(compoundTag0.getString("variant")));
        if ($$1 != null) {
            this.setVariant($$1);
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void customServerAiStep() {
        this.m_9236_().getProfiler().push("frogBrain");
        this.getBrain().tick((ServerLevel) this.m_9236_(), this);
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("frogActivityUpdate");
        FrogAi.updateActivity(this);
        this.m_9236_().getProfiler().pop();
        super.customServerAiStep();
    }

    @Override
    public void tick() {
        if (this.m_9236_().isClientSide()) {
            this.swimIdleAnimationState.animateWhen(this.m_20072_() && !this.f_267362_.isMoving(), this.f_19797_);
        }
        super.m_8119_();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (f_19806_.equals(entityDataAccessor0)) {
            Pose $$1 = this.m_20089_();
            if ($$1 == Pose.LONG_JUMPING) {
                this.jumpAnimationState.start(this.f_19797_);
            } else {
                this.jumpAnimationState.stop();
            }
            if ($$1 == Pose.CROAKING) {
                this.croakAnimationState.start(this.f_19797_);
            } else {
                this.croakAnimationState.stop();
            }
            if ($$1 == Pose.USING_TONGUE) {
                this.tongueAnimationState.start(this.f_19797_);
            } else {
                this.tongueAnimationState.stop();
            }
        }
        super.m_7350_(entityDataAccessor0);
    }

    @Override
    protected void updateWalkAnimation(float float0) {
        float $$1;
        if (this.jumpAnimationState.isStarted()) {
            $$1 = 0.0F;
        } else {
            $$1 = Math.min(float0 * 25.0F, 1.0F);
        }
        this.f_267362_.update($$1, 0.4F);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        Frog $$2 = EntityType.FROG.create(serverLevel0);
        if ($$2 != null) {
            FrogAi.initMemories($$2, serverLevel0.m_213780_());
        }
        return $$2;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void setBaby(boolean boolean0) {
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel serverLevel0, Animal animal1) {
        this.m_277117_(serverLevel0, animal1, null);
        this.getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        Holder<Biome> $$5 = serverLevelAccessor0.m_204166_(this.m_20183_());
        if ($$5.is(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)) {
            this.setVariant(FrogVariant.COLD);
        } else if ($$5.is(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)) {
            this.setVariant(FrogVariant.WARM);
        } else {
            this.setVariant(FrogVariant.TEMPERATE);
        }
        FrogAi.initMemories(this, serverLevelAccessor0.m_213780_());
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 1.0).add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ATTACK_DAMAGE, 10.0);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.FROG_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.FROG_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FROG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.FROG_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected void sendDebugPackets() {
        super.m_8025_();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    protected int calculateFallDamage(float float0, float float1) {
        return super.m_5639_(float0, float1) - 5;
    }

    @Override
    public void travel(Vec3 vec0) {
        if (this.m_6109_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), vec0);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
        } else {
            super.m_7023_(vec0);
        }
    }

    public static boolean canEat(LivingEntity livingEntity0) {
        if (livingEntity0 instanceof Slime $$1 && $$1.getSize() != 1) {
            return false;
        }
        return livingEntity0.m_6095_().is(EntityTypeTags.FROG_FOOD);
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        return new Frog.FrogPathNavigation(this, level0);
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return TEMPTATION_ITEM.test(itemStack0);
    }

    public static boolean checkFrogSpawnRules(EntityType<? extends Animal> entityTypeExtendsAnimal0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return levelAccessor1.m_8055_(blockPos3.below()).m_204336_(BlockTags.FROGS_SPAWNABLE_ON) && m_186209_(levelAccessor1, blockPos3);
    }

    class FrogLookControl extends LookControl {

        FrogLookControl(Mob mob0) {
            super(mob0);
        }

        @Override
        protected boolean resetXRotOnTick() {
            return Frog.this.getTongueTarget().isEmpty();
        }
    }

    static class FrogNodeEvaluator extends AmphibiousNodeEvaluator {

        private final BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();

        public FrogNodeEvaluator(boolean boolean0) {
            super(boolean0);
        }

        @Override
        public Node getStart() {
            return !this.f_77313_.m_20069_() ? super.getStart() : this.m_230631_(new BlockPos(Mth.floor(this.f_77313_.m_20191_().minX), Mth.floor(this.f_77313_.m_20191_().minY), Mth.floor(this.f_77313_.m_20191_().minZ)));
        }

        @Override
        public BlockPathTypes getBlockPathType(BlockGetter blockGetter0, int int1, int int2, int int3) {
            this.belowPos.set(int1, int2 - 1, int3);
            BlockState $$4 = blockGetter0.getBlockState(this.belowPos);
            return $$4.m_204336_(BlockTags.FROG_PREFER_JUMP_TO) ? BlockPathTypes.OPEN : super.getBlockPathType(blockGetter0, int1, int2, int3);
        }
    }

    static class FrogPathNavigation extends AmphibiousPathNavigation {

        FrogPathNavigation(Frog frog0, Level level1) {
            super(frog0, level1);
        }

        @Override
        public boolean canCutCorner(BlockPathTypes blockPathTypes0) {
            return blockPathTypes0 != BlockPathTypes.WATER_BORDER && super.m_264193_(blockPathTypes0);
        }

        @Override
        protected PathFinder createPathFinder(int int0) {
            this.f_26508_ = new Frog.FrogNodeEvaluator(true);
            this.f_26508_.setCanPassDoors(true);
            return new PathFinder(this.f_26508_, int0);
        }
    }
}