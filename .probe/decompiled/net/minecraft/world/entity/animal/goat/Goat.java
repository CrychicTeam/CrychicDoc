package net.minecraft.world.entity.animal.goat;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class Goat extends Animal {

    public static final EntityDimensions LONG_JUMPING_DIMENSIONS = EntityDimensions.scalable(0.9F, 1.3F).scale(0.7F);

    private static final int ADULT_ATTACK_DAMAGE = 2;

    private static final int BABY_ATTACK_DAMAGE = 1;

    protected static final ImmutableList<SensorType<? extends Sensor<? super Goat>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_ADULT, SensorType.HURT_BY, SensorType.GOAT_TEMPTATIONS);

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.BREED_TARGET, MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, new MemoryModuleType[] { MemoryModuleType.IS_TEMPTED, MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryModuleType.RAM_TARGET, MemoryModuleType.IS_PANICKING });

    public static final int GOAT_FALL_DAMAGE_REDUCTION = 10;

    public static final double GOAT_SCREAMING_CHANCE = 0.02;

    public static final double UNIHORN_CHANCE = 0.1F;

    private static final EntityDataAccessor<Boolean> DATA_IS_SCREAMING_GOAT = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_HAS_LEFT_HORN = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_HAS_RIGHT_HORN = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);

    private boolean isLoweringHead;

    private int lowerHeadTick;

    public Goat(EntityType<? extends Goat> entityTypeExtendsGoat0, Level level1) {
        super(entityTypeExtendsGoat0, level1);
        this.m_21573_().setCanFloat(true);
        this.m_21441_(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.m_21441_(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    public ItemStack createHorn() {
        RandomSource $$0 = RandomSource.create((long) this.m_20148_().hashCode());
        TagKey<Instrument> $$1 = this.isScreamingGoat() ? InstrumentTags.SCREAMING_GOAT_HORNS : InstrumentTags.REGULAR_GOAT_HORNS;
        HolderSet<Instrument> $$2 = BuiltInRegistries.INSTRUMENT.getOrCreateTag($$1);
        return InstrumentItem.create(Items.GOAT_HORN, (Holder<Instrument>) $$2.getRandomElement($$0).get());
    }

    @Override
    protected Brain.Provider<Goat> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic0) {
        return GoatAi.makeBrain(this.brainProvider().makeBrain(dynamic0));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void ageBoundaryReached() {
        if (this.m_6162_()) {
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(1.0);
            this.removeHorns();
        } else {
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(2.0);
            this.addHorns();
        }
    }

    @Override
    protected int calculateFallDamage(float float0, float float1) {
        return super.m_5639_(float0, float1) - 10;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_AMBIENT : SoundEvents.GOAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return this.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_HURT : SoundEvents.GOAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_DEATH : SoundEvents.GOAT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.GOAT_STEP, 0.15F, 1.0F);
    }

    protected SoundEvent getMilkingSound() {
        return this.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_MILK : SoundEvents.GOAT_MILK;
    }

    @Nullable
    public Goat getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        Goat $$2 = EntityType.GOAT.create(serverLevel0);
        if ($$2 != null) {
            GoatAi.initMemories($$2, serverLevel0.m_213780_());
            AgeableMob $$3 = (AgeableMob) (serverLevel0.m_213780_().nextBoolean() ? this : ageableMob1);
            boolean $$5 = $$3 instanceof Goat $$4 && $$4.isScreamingGoat() || serverLevel0.m_213780_().nextDouble() < 0.02;
            $$2.setScreamingGoat($$5);
        }
        return $$2;
    }

    @Override
    public Brain<Goat> getBrain() {
        return super.m_6274_();
    }

    @Override
    protected void customServerAiStep() {
        this.m_9236_().getProfiler().push("goatBrain");
        this.getBrain().tick((ServerLevel) this.m_9236_(), this);
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("goatActivityUpdate");
        GoatAi.updateActivity(this);
        this.m_9236_().getProfiler().pop();
        super.customServerAiStep();
    }

    @Override
    public int getMaxHeadYRot() {
        return 15;
    }

    @Override
    public void setYHeadRot(float float0) {
        int $$1 = this.getMaxHeadYRot();
        float $$2 = Mth.degreesDifference(this.f_20883_, float0);
        float $$3 = Mth.clamp($$2, (float) (-$$1), (float) $$1);
        super.m_5616_(this.f_20883_ + $$3);
    }

    @Override
    public SoundEvent getEatingSound(ItemStack itemStack0) {
        return this.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_EAT : SoundEvents.GOAT_EAT;
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if ($$2.is(Items.BUCKET) && !this.m_6162_()) {
            player0.playSound(this.getMilkingSound(), 1.0F, 1.0F);
            ItemStack $$3 = ItemUtils.createFilledResult($$2, player0, Items.MILK_BUCKET.getDefaultInstance());
            player0.m_21008_(interactionHand1, $$3);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            InteractionResult $$4 = super.mobInteract(player0, interactionHand1);
            if ($$4.consumesAction() && this.m_6898_($$2)) {
                this.m_9236_().playSound(null, this, this.getEatingSound($$2), SoundSource.NEUTRAL, 1.0F, Mth.randomBetween(this.m_9236_().random, 0.8F, 1.2F));
            }
            return $$4;
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        RandomSource $$5 = serverLevelAccessor0.m_213780_();
        GoatAi.initMemories(this, $$5);
        this.setScreamingGoat($$5.nextDouble() < 0.02);
        this.ageBoundaryReached();
        if (!this.m_6162_() && (double) $$5.nextFloat() < 0.1F) {
            EntityDataAccessor<Boolean> $$6 = $$5.nextBoolean() ? DATA_HAS_LEFT_HORN : DATA_HAS_RIGHT_HORN;
            this.f_19804_.set($$6, false);
        }
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    protected void sendDebugPackets() {
        super.m_8025_();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        return pose0 == Pose.LONG_JUMPING ? LONG_JUMPING_DIMENSIONS.scale(this.m_6134_()) : super.m_6972_(pose0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putBoolean("IsScreamingGoat", this.isScreamingGoat());
        compoundTag0.putBoolean("HasLeftHorn", this.hasLeftHorn());
        compoundTag0.putBoolean("HasRightHorn", this.hasRightHorn());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setScreamingGoat(compoundTag0.getBoolean("IsScreamingGoat"));
        this.f_19804_.set(DATA_HAS_LEFT_HORN, compoundTag0.getBoolean("HasLeftHorn"));
        this.f_19804_.set(DATA_HAS_RIGHT_HORN, compoundTag0.getBoolean("HasRightHorn"));
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 58) {
            this.isLoweringHead = true;
        } else if (byte0 == 59) {
            this.isLoweringHead = false;
        } else {
            super.handleEntityEvent(byte0);
        }
    }

    @Override
    public void aiStep() {
        if (this.isLoweringHead) {
            this.lowerHeadTick++;
        } else {
            this.lowerHeadTick -= 2;
        }
        this.lowerHeadTick = Mth.clamp(this.lowerHeadTick, 0, 20);
        super.aiStep();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_IS_SCREAMING_GOAT, false);
        this.f_19804_.define(DATA_HAS_LEFT_HORN, true);
        this.f_19804_.define(DATA_HAS_RIGHT_HORN, true);
    }

    public boolean hasLeftHorn() {
        return this.f_19804_.get(DATA_HAS_LEFT_HORN);
    }

    public boolean hasRightHorn() {
        return this.f_19804_.get(DATA_HAS_RIGHT_HORN);
    }

    public boolean dropHorn() {
        boolean $$0 = this.hasLeftHorn();
        boolean $$1 = this.hasRightHorn();
        if (!$$0 && !$$1) {
            return false;
        } else {
            EntityDataAccessor<Boolean> $$2;
            if (!$$0) {
                $$2 = DATA_HAS_RIGHT_HORN;
            } else if (!$$1) {
                $$2 = DATA_HAS_LEFT_HORN;
            } else {
                $$2 = this.f_19796_.nextBoolean() ? DATA_HAS_LEFT_HORN : DATA_HAS_RIGHT_HORN;
            }
            this.f_19804_.set($$2, false);
            Vec3 $$5 = this.m_20182_();
            ItemStack $$6 = this.createHorn();
            double $$7 = (double) Mth.randomBetween(this.f_19796_, -0.2F, 0.2F);
            double $$8 = (double) Mth.randomBetween(this.f_19796_, 0.3F, 0.7F);
            double $$9 = (double) Mth.randomBetween(this.f_19796_, -0.2F, 0.2F);
            ItemEntity $$10 = new ItemEntity(this.m_9236_(), $$5.x(), $$5.y(), $$5.z(), $$6, $$7, $$8, $$9);
            this.m_9236_().m_7967_($$10);
            return true;
        }
    }

    public void addHorns() {
        this.f_19804_.set(DATA_HAS_LEFT_HORN, true);
        this.f_19804_.set(DATA_HAS_RIGHT_HORN, true);
    }

    public void removeHorns() {
        this.f_19804_.set(DATA_HAS_LEFT_HORN, false);
        this.f_19804_.set(DATA_HAS_RIGHT_HORN, false);
    }

    public boolean isScreamingGoat() {
        return this.f_19804_.get(DATA_IS_SCREAMING_GOAT);
    }

    public void setScreamingGoat(boolean boolean0) {
        this.f_19804_.set(DATA_IS_SCREAMING_GOAT, boolean0);
    }

    public float getRammingXHeadRot() {
        return (float) this.lowerHeadTick / 20.0F * 30.0F * (float) (Math.PI / 180.0);
    }

    public static boolean checkGoatSpawnRules(EntityType<? extends Animal> entityTypeExtendsAnimal0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return levelAccessor1.m_8055_(blockPos3.below()).m_204336_(BlockTags.GOATS_SPAWNABLE_ON) && m_186209_(levelAccessor1, blockPos3);
    }
}