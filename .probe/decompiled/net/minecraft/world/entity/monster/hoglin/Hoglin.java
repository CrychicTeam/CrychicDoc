package net.minecraft.world.entity.monster.hoglin;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class Hoglin extends Animal implements Enemy, HoglinBase {

    private static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(Hoglin.class, EntityDataSerializers.BOOLEAN);

    private static final float PROBABILITY_OF_SPAWNING_AS_BABY = 0.2F;

    private static final int MAX_HEALTH = 40;

    private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.3F;

    private static final int ATTACK_KNOCKBACK = 1;

    private static final float KNOCKBACK_RESISTANCE = 0.6F;

    private static final int ATTACK_DAMAGE = 6;

    private static final float BABY_ATTACK_DAMAGE = 0.5F;

    private static final int CONVERSION_TIME = 300;

    private int attackAnimationRemainingTicks;

    private int timeInOverworld;

    private boolean cannotBeHunted;

    protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Hoglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ADULT, SensorType.HOGLIN_SPECIFIC_SENSOR);

    protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, new MemoryModuleType[] { MemoryModuleType.AVOID_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.PACIFIED });

    public Hoglin(EntityType<? extends Hoglin> entityTypeExtendsHoglin0, Level level1) {
        super(entityTypeExtendsHoglin0, level1);
        this.f_21364_ = 5;
    }

    @Override
    public boolean canBeLeashed(Player player0) {
        return !this.m_21523_();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.6F).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        if (!(entity0 instanceof LivingEntity)) {
            return false;
        } else {
            this.attackAnimationRemainingTicks = 10;
            this.m_9236_().broadcastEntityEvent(this, (byte) 4);
            this.m_5496_(SoundEvents.HOGLIN_ATTACK, 1.0F, this.m_6100_());
            HoglinAi.onHitTarget(this, (LivingEntity) entity0);
            return HoglinBase.hurtAndThrowTarget(this, (LivingEntity) entity0);
        }
    }

    @Override
    protected void blockedByShield(LivingEntity livingEntity0) {
        if (this.isAdult()) {
            HoglinBase.throwTarget(this, livingEntity0);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        boolean $$2 = super.hurt(damageSource0, float1);
        if (this.m_9236_().isClientSide) {
            return false;
        } else {
            if ($$2 && damageSource0.getEntity() instanceof LivingEntity) {
                HoglinAi.wasHurtBy(this, (LivingEntity) damageSource0.getEntity());
            }
            return $$2;
        }
    }

    @Override
    protected Brain.Provider<Hoglin> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic0) {
        return HoglinAi.makeBrain(this.brainProvider().makeBrain(dynamic0));
    }

    @Override
    public Brain<Hoglin> getBrain() {
        return super.m_6274_();
    }

    @Override
    protected void customServerAiStep() {
        this.m_9236_().getProfiler().push("hoglinBrain");
        this.getBrain().tick((ServerLevel) this.m_9236_(), this);
        this.m_9236_().getProfiler().pop();
        HoglinAi.updateActivity(this);
        if (this.isConverting()) {
            this.timeInOverworld++;
            if (this.timeInOverworld > 300) {
                this.playSoundEvent(SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED);
                this.finishConversion((ServerLevel) this.m_9236_());
            }
        } else {
            this.timeInOverworld = 0;
        }
    }

    @Override
    public void aiStep() {
        if (this.attackAnimationRemainingTicks > 0) {
            this.attackAnimationRemainingTicks--;
        }
        super.aiStep();
    }

    @Override
    protected void ageBoundaryReached() {
        if (this.m_6162_()) {
            this.f_21364_ = 3;
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(0.5);
        } else {
            this.f_21364_ = 5;
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(6.0);
        }
    }

    public static boolean checkHoglinSpawnRules(EntityType<Hoglin> entityTypeHoglin0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return !levelAccessor1.m_8055_(blockPos3.below()).m_60713_(Blocks.NETHER_WART_BLOCK);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        if (serverLevelAccessor0.m_213780_().nextFloat() < 0.2F) {
            this.m_6863_(true);
        }
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return !this.m_21532_();
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        if (HoglinAi.isPosNearNearestRepellent(this, blockPos0)) {
            return -1.0F;
        } else {
            return levelReader1.m_8055_(blockPos0.below()).m_60713_(Blocks.CRIMSON_NYLIUM) ? 10.0F : 0.0F;
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.m_20206_() - (this.m_6162_() ? 0.2 : 0.15);
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        InteractionResult $$2 = super.mobInteract(player0, interactionHand1);
        if ($$2.consumesAction()) {
            this.m_21530_();
        }
        return $$2;
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 4) {
            this.attackAnimationRemainingTicks = 10;
            this.m_5496_(SoundEvents.HOGLIN_ATTACK, 1.0F, this.m_6100_());
        } else {
            super.handleEntityEvent(byte0);
        }
    }

    @Override
    public int getAttackAnimationRemainingTicks() {
        return this.attackAnimationRemainingTicks;
    }

    @Override
    public boolean shouldDropExperience() {
        return true;
    }

    @Override
    public int getExperienceReward() {
        return this.f_21364_;
    }

    private void finishConversion(ServerLevel serverLevel0) {
        Zoglin $$1 = (Zoglin) this.m_21406_(EntityType.ZOGLIN, true);
        if ($$1 != null) {
            $$1.m_7292_(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return itemStack0.is(Items.CRIMSON_FUNGUS);
    }

    public boolean isAdult() {
        return !this.m_6162_();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_IMMUNE_TO_ZOMBIFICATION, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        if (this.isImmuneToZombification()) {
            compoundTag0.putBoolean("IsImmuneToZombification", true);
        }
        compoundTag0.putInt("TimeInOverworld", this.timeInOverworld);
        if (this.cannotBeHunted) {
            compoundTag0.putBoolean("CannotBeHunted", true);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setImmuneToZombification(compoundTag0.getBoolean("IsImmuneToZombification"));
        this.timeInOverworld = compoundTag0.getInt("TimeInOverworld");
        this.setCannotBeHunted(compoundTag0.getBoolean("CannotBeHunted"));
    }

    public void setImmuneToZombification(boolean boolean0) {
        this.m_20088_().set(DATA_IMMUNE_TO_ZOMBIFICATION, boolean0);
    }

    private boolean isImmuneToZombification() {
        return this.m_20088_().get(DATA_IMMUNE_TO_ZOMBIFICATION);
    }

    public boolean isConverting() {
        return !this.m_9236_().dimensionType().piglinSafe() && !this.isImmuneToZombification() && !this.m_21525_();
    }

    private void setCannotBeHunted(boolean boolean0) {
        this.cannotBeHunted = boolean0;
    }

    public boolean canBeHunted() {
        return this.isAdult() && !this.cannotBeHunted;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        Hoglin $$2 = EntityType.HOGLIN.create(serverLevel0);
        if ($$2 != null) {
            $$2.m_21530_();
        }
        return $$2;
    }

    @Override
    public boolean canFallInLove() {
        return !HoglinAi.isPacified(this) && super.canFallInLove();
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_9236_().isClientSide ? null : (SoundEvent) HoglinAi.getSoundForCurrentActivity(this).orElse(null);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.HOGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HOGLIN_DEATH;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.HOGLIN_STEP, 0.15F, 1.0F);
    }

    protected void playSoundEvent(SoundEvent soundEvent0) {
        this.m_5496_(soundEvent0, this.m_6121_(), this.m_6100_());
    }

    @Override
    protected void sendDebugPackets() {
        super.m_8025_();
        DebugPackets.sendEntityBrain(this);
    }
}