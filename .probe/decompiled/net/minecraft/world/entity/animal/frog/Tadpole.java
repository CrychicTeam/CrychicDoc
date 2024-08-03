package net.minecraft.world.entity.animal.frog;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Tadpole extends AbstractFish {

    @VisibleForTesting
    public static int ticksToBeFrog = Math.abs(-24000);

    public static float HITBOX_WIDTH = 0.4F;

    public static float HITBOX_HEIGHT = 0.3F;

    private int age;

    protected static final ImmutableList<SensorType<? extends Sensor<? super Tadpole>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.FROG_TEMPTATIONS);

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.BREED_TARGET, MemoryModuleType.IS_PANICKING);

    public Tadpole(EntityType<? extends AbstractFish> entityTypeExtendsAbstractFish0, Level level1) {
        super(entityTypeExtendsAbstractFish0, level1);
        this.f_21342_ = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.f_21365_ = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        return new WaterBoundPathNavigation(this, level0);
    }

    @Override
    protected Brain.Provider<Tadpole> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic0) {
        return TadpoleAi.makeBrain(this.brainProvider().makeBrain(dynamic0));
    }

    @Override
    public Brain<Tadpole> getBrain() {
        return super.m_6274_();
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TADPOLE_FLOP;
    }

    @Override
    protected void customServerAiStep() {
        this.m_9236_().getProfiler().push("tadpoleBrain");
        this.getBrain().tick((ServerLevel) this.m_9236_(), this);
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("tadpoleActivityUpdate");
        TadpoleAi.updateActivity(this);
        this.m_9236_().getProfiler().pop();
        super.m_8024_();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 1.0).add(Attributes.MAX_HEALTH, 6.0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.m_9236_().isClientSide) {
            this.setAge(this.age + 1);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("Age", this.age);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setAge(compoundTag0.getInt("Age"));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.TADPOLE_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.TADPOLE_DEATH;
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if (this.isFood($$2)) {
            this.feed(player0, $$2);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return (InteractionResult) Bucketable.bucketMobPickup(player0, interactionHand1, this).orElse(super.mobInteract(player0, interactionHand1));
        }
    }

    @Override
    protected void sendDebugPackets() {
        super.m_8025_();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public boolean fromBucket() {
        return true;
    }

    @Override
    public void setFromBucket(boolean boolean0) {
    }

    @Override
    public void saveToBucketTag(ItemStack itemStack0) {
        Bucketable.saveDefaultDataToBucketTag(this, itemStack0);
        CompoundTag $$1 = itemStack0.getOrCreateTag();
        $$1.putInt("Age", this.getAge());
    }

    @Override
    public void loadFromBucketTag(CompoundTag compoundTag0) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag0);
        if (compoundTag0.contains("Age")) {
            this.setAge(compoundTag0.getInt("Age"));
        }
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(Items.TADPOLE_BUCKET);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_TADPOLE;
    }

    private boolean isFood(ItemStack itemStack0) {
        return Frog.TEMPTATION_ITEM.test(itemStack0);
    }

    private void feed(Player player0, ItemStack itemStack1) {
        this.usePlayerItem(player0, itemStack1);
        this.ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(this.getTicksLeftUntilAdult()));
        this.m_9236_().addParticle(ParticleTypes.HAPPY_VILLAGER, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), 0.0, 0.0, 0.0);
    }

    private void usePlayerItem(Player player0, ItemStack itemStack1) {
        if (!player0.getAbilities().instabuild) {
            itemStack1.shrink(1);
        }
    }

    private int getAge() {
        return this.age;
    }

    private void ageUp(int int0) {
        this.setAge(this.age + int0 * 20);
    }

    private void setAge(int int0) {
        this.age = int0;
        if (this.age >= ticksToBeFrog) {
            this.ageUp();
        }
    }

    private void ageUp() {
        if (this.m_9236_() instanceof ServerLevel $$0) {
            Frog $$1 = EntityType.FROG.create(this.m_9236_());
            if ($$1 != null) {
                $$1.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
                $$1.finalizeSpawn($$0, this.m_9236_().getCurrentDifficultyAt($$1.m_20183_()), MobSpawnType.CONVERSION, null, null);
                $$1.m_21557_(this.m_21525_());
                if (this.m_8077_()) {
                    $$1.m_6593_(this.m_7770_());
                    $$1.m_20340_(this.m_20151_());
                }
                $$1.m_21530_();
                this.m_5496_(SoundEvents.TADPOLE_GROW_UP, 0.15F, 1.0F);
                $$0.m_47205_($$1);
                this.m_146870_();
            }
        }
    }

    private int getTicksLeftUntilAdult() {
        return Math.max(0, ticksToBeFrog - this.age);
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }
}