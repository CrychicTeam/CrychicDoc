package net.minecraft.world.entity.monster.piglin;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class Piglin extends AbstractPiglin implements CrossbowAttackMob, InventoryCarrier {

    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);

    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");

    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.2F, AttributeModifier.Operation.MULTIPLY_BASE);

    private static final int MAX_HEALTH = 16;

    private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;

    private static final int ATTACK_DAMAGE = 5;

    private static final float CROSSBOW_POWER = 1.6F;

    private static final float CHANCE_OF_WEARING_EACH_ARMOUR_ITEM = 0.1F;

    private static final int MAX_PASSENGERS_ON_ONE_HOGLIN = 3;

    private static final float PROBABILITY_OF_SPAWNING_AS_BABY = 0.2F;

    private static final float BABY_EYE_HEIGHT_ADJUSTMENT = 0.82F;

    private static final double PROBABILITY_OF_SPAWNING_WITH_CROSSBOW_INSTEAD_OF_SWORD = 0.5;

    private final SimpleContainer inventory = new SimpleContainer(8);

    private boolean cannotHunt;

    protected static final ImmutableList<SensorType<? extends Sensor<? super Piglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR);

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, new MemoryModuleType[] { MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.NEAREST_REPELLENT });

    public Piglin(EntityType<? extends AbstractPiglin> entityTypeExtendsAbstractPiglin0, Level level1) {
        super(entityTypeExtendsAbstractPiglin0, level1);
        this.f_21364_ = 5;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        if (this.isBaby()) {
            compoundTag0.putBoolean("IsBaby", true);
        }
        if (this.cannotHunt) {
            compoundTag0.putBoolean("CannotHunt", true);
        }
        this.m_252802_(compoundTag0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setBaby(compoundTag0.getBoolean("IsBaby"));
        this.setCannotHunt(compoundTag0.getBoolean("CannotHunt"));
        this.m_253224_(compoundTag0);
    }

    @VisibleForDebug
    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource0, int int1, boolean boolean2) {
        super.m_7472_(damageSource0, int1, boolean2);
        if (damageSource0.getEntity() instanceof Creeper $$4 && $$4.canDropMobsSkull()) {
            ItemStack $$5 = new ItemStack(Items.PIGLIN_HEAD);
            $$4.increaseDroppedSkulls();
            this.m_19983_($$5);
        }
        this.inventory.removeAllItems().forEach(this::m_19983_);
    }

    protected ItemStack addToInventory(ItemStack itemStack0) {
        return this.inventory.addItem(itemStack0);
    }

    protected boolean canAddToInventory(ItemStack itemStack0) {
        return this.inventory.canAddItem(itemStack0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_BABY_ID, false);
        this.f_19804_.define(DATA_IS_CHARGING_CROSSBOW, false);
        this.f_19804_.define(DATA_IS_DANCING, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        super.m_7350_(entityDataAccessor0);
        if (DATA_BABY_ID.equals(entityDataAccessor0)) {
            this.m_6210_();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    public static boolean checkPiglinSpawnRules(EntityType<Piglin> entityTypePiglin0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return !levelAccessor1.m_8055_(blockPos3.below()).m_60713_(Blocks.NETHER_WART_BLOCK);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        RandomSource $$5 = serverLevelAccessor0.m_213780_();
        if (mobSpawnType2 != MobSpawnType.STRUCTURE) {
            if ($$5.nextFloat() < 0.2F) {
                this.setBaby(true);
            } else if (this.m_34667_()) {
                this.m_8061_(EquipmentSlot.MAINHAND, this.createSpawnWeapon());
            }
        }
        PiglinAi.initMemories(this, serverLevelAccessor0.m_213780_());
        this.populateDefaultEquipmentSlots($$5, difficultyInstance1);
        this.m_213946_($$5, difficultyInstance1);
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return !this.m_21532_();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
        if (this.m_34667_()) {
            this.maybeWearArmor(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET), randomSource0);
            this.maybeWearArmor(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE), randomSource0);
            this.maybeWearArmor(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS), randomSource0);
            this.maybeWearArmor(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS), randomSource0);
        }
    }

    private void maybeWearArmor(EquipmentSlot equipmentSlot0, ItemStack itemStack1, RandomSource randomSource2) {
        if (randomSource2.nextFloat() < 0.1F) {
            this.m_8061_(equipmentSlot0, itemStack1);
        }
    }

    @Override
    protected Brain.Provider<Piglin> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic0) {
        return PiglinAi.makeBrain(this, this.brainProvider().makeBrain(dynamic0));
    }

    @Override
    public Brain<Piglin> getBrain() {
        return super.m_6274_();
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        InteractionResult $$2 = super.m_6071_(player0, interactionHand1);
        if ($$2.consumesAction()) {
            return $$2;
        } else if (!this.m_9236_().isClientSide) {
            return PiglinAi.mobInteract(this, player0, interactionHand1);
        } else {
            boolean $$3 = PiglinAi.canAdmire(this, player0.m_21120_(interactionHand1)) && this.getArmPose() != PiglinArmPose.ADMIRING_ITEM;
            return $$3 ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        float $$2 = super.getStandingEyeHeight(pose0, entityDimensions1);
        return this.isBaby() ? $$2 - 0.82F : $$2;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.m_20206_() * 0.92;
    }

    @Override
    public void setBaby(boolean boolean0) {
        this.m_20088_().set(DATA_BABY_ID, boolean0);
        if (!this.m_9236_().isClientSide) {
            AttributeInstance $$1 = this.m_21051_(Attributes.MOVEMENT_SPEED);
            $$1.removeModifier(SPEED_MODIFIER_BABY);
            if (boolean0) {
                $$1.addTransientModifier(SPEED_MODIFIER_BABY);
            }
        }
    }

    @Override
    public boolean isBaby() {
        return this.m_20088_().get(DATA_BABY_ID);
    }

    private void setCannotHunt(boolean boolean0) {
        this.cannotHunt = boolean0;
    }

    @Override
    protected boolean canHunt() {
        return !this.cannotHunt;
    }

    @Override
    protected void customServerAiStep() {
        this.m_9236_().getProfiler().push("piglinBrain");
        this.getBrain().tick((ServerLevel) this.m_9236_(), this);
        this.m_9236_().getProfiler().pop();
        PiglinAi.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public int getExperienceReward() {
        return this.f_21364_;
    }

    @Override
    protected void finishConversion(ServerLevel serverLevel0) {
        PiglinAi.cancelAdmiring(this);
        this.inventory.removeAllItems().forEach(this::m_19983_);
        super.finishConversion(serverLevel0);
    }

    private ItemStack createSpawnWeapon() {
        return (double) this.f_19796_.nextFloat() < 0.5 ? new ItemStack(Items.CROSSBOW) : new ItemStack(Items.GOLDEN_SWORD);
    }

    private boolean isChargingCrossbow() {
        return this.f_19804_.get(DATA_IS_CHARGING_CROSSBOW);
    }

    @Override
    public void setChargingCrossbow(boolean boolean0) {
        this.f_19804_.set(DATA_IS_CHARGING_CROSSBOW, boolean0);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.f_20891_ = 0;
    }

    @Override
    public PiglinArmPose getArmPose() {
        if (this.isDancing()) {
            return PiglinArmPose.DANCING;
        } else if (PiglinAi.isLovedItem(this.m_21206_())) {
            return PiglinArmPose.ADMIRING_ITEM;
        } else if (this.m_5912_() && this.m_34668_()) {
            return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
        } else if (this.isChargingCrossbow()) {
            return PiglinArmPose.CROSSBOW_CHARGE;
        } else {
            return this.m_5912_() && this.m_21055_(Items.CROSSBOW) ? PiglinArmPose.CROSSBOW_HOLD : PiglinArmPose.DEFAULT;
        }
    }

    public boolean isDancing() {
        return this.f_19804_.get(DATA_IS_DANCING);
    }

    public void setDancing(boolean boolean0) {
        this.f_19804_.set(DATA_IS_DANCING, boolean0);
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        boolean $$2 = super.m_6469_(damageSource0, float1);
        if (this.m_9236_().isClientSide) {
            return false;
        } else {
            if ($$2 && damageSource0.getEntity() instanceof LivingEntity) {
                PiglinAi.wasHurtBy(this, (LivingEntity) damageSource0.getEntity());
            }
            return $$2;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity0, float float1) {
        this.m_32336_(this, 1.6F);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity livingEntity0, ItemStack itemStack1, Projectile projectile2, float float3) {
        this.m_32322_(this, livingEntity0, projectile2, float3, 1.6F);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeaponItem0) {
        return projectileWeaponItem0 == Items.CROSSBOW;
    }

    protected void holdInMainHand(ItemStack itemStack0) {
        this.m_21468_(EquipmentSlot.MAINHAND, itemStack0);
    }

    protected void holdInOffHand(ItemStack itemStack0) {
        if (itemStack0.is(PiglinAi.BARTERING_ITEM)) {
            this.m_8061_(EquipmentSlot.OFFHAND, itemStack0);
            this.m_21508_(EquipmentSlot.OFFHAND);
        } else {
            this.m_21468_(EquipmentSlot.OFFHAND, itemStack0);
        }
    }

    @Override
    public boolean wantsToPickUp(ItemStack itemStack0) {
        return this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.m_21531_() && PiglinAi.wantsToPickup(this, itemStack0);
    }

    protected boolean canReplaceCurrentItem(ItemStack itemStack0) {
        EquipmentSlot $$1 = Mob.m_147233_(itemStack0);
        ItemStack $$2 = this.m_6844_($$1);
        return this.canReplaceCurrentItem(itemStack0, $$2);
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack itemStack0, ItemStack itemStack1) {
        if (EnchantmentHelper.hasBindingCurse(itemStack1)) {
            return false;
        } else {
            boolean $$2 = PiglinAi.isLovedItem(itemStack0) || itemStack0.is(Items.CROSSBOW);
            boolean $$3 = PiglinAi.isLovedItem(itemStack1) || itemStack1.is(Items.CROSSBOW);
            if ($$2 && !$$3) {
                return true;
            } else if (!$$2 && $$3) {
                return false;
            } else {
                return this.m_34667_() && !itemStack0.is(Items.CROSSBOW) && itemStack1.is(Items.CROSSBOW) ? false : super.m_7808_(itemStack0, itemStack1);
            }
        }
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity0) {
        this.m_21053_(itemEntity0);
        PiglinAi.pickUpItem(this, itemEntity0);
    }

    @Override
    public boolean startRiding(Entity entity0, boolean boolean1) {
        if (this.isBaby() && entity0.getType() == EntityType.HOGLIN) {
            entity0 = this.getTopPassenger(entity0, 3);
        }
        return super.m_7998_(entity0, boolean1);
    }

    private Entity getTopPassenger(Entity entity0, int int1) {
        List<Entity> $$2 = entity0.getPassengers();
        return int1 != 1 && !$$2.isEmpty() ? this.getTopPassenger((Entity) $$2.get(0), int1 - 1) : entity0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_9236_().isClientSide ? null : (SoundEvent) PiglinAi.getSoundForCurrentActivity(this).orElse(null);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }

    protected void playSoundEvent(SoundEvent soundEvent0) {
        this.m_5496_(soundEvent0, this.m_6121_(), this.m_6100_());
    }

    @Override
    protected void playConvertedSound() {
        this.playSoundEvent(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }
}