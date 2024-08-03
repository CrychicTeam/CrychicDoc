package net.minecraft.world.entity.animal.allay;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class Allay extends PathfinderMob implements InventoryCarrier, VibrationSystem {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 1, 1);

    private static final int LIFTING_ITEM_ANIMATION_DURATION = 5;

    private static final float DANCING_LOOP_DURATION = 55.0F;

    private static final float SPINNING_ANIMATION_DURATION = 15.0F;

    private static final Ingredient DUPLICATION_ITEM = Ingredient.of(Items.AMETHYST_SHARD);

    private static final int DUPLICATION_COOLDOWN_TICKS = 6000;

    private static final int NUM_OF_DUPLICATION_HEARTS = 3;

    private static final double RIDING_OFFSET = 0.4;

    private static final EntityDataAccessor<Boolean> DATA_DANCING = SynchedEntityData.defineId(Allay.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_CAN_DUPLICATE = SynchedEntityData.defineId(Allay.class, EntityDataSerializers.BOOLEAN);

    protected static final ImmutableList<SensorType<? extends Sensor<? super Allay>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NEAREST_ITEMS);

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.HURT_BY, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.LIKED_PLAYER, MemoryModuleType.LIKED_NOTEBLOCK_POSITION, MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.IS_PANICKING, new MemoryModuleType[0]);

    public static final ImmutableList<Float> THROW_SOUND_PITCHES = ImmutableList.of(0.5625F, 0.625F, 0.75F, 0.9375F, 1.0F, 1.0F, 1.125F, 1.25F, 1.5F, 1.875F, 2.0F, 2.25F, new Float[] { 2.5F, 3.0F, 3.75F, 4.0F });

    private final DynamicGameEventListener<VibrationSystem.Listener> dynamicVibrationListener;

    private VibrationSystem.Data vibrationData;

    private final VibrationSystem.User vibrationUser;

    private final DynamicGameEventListener<Allay.JukeboxListener> dynamicJukeboxListener;

    private final SimpleContainer inventory = new SimpleContainer(1);

    @Nullable
    private BlockPos jukeboxPos;

    private long duplicationCooldown;

    private float holdingItemAnimationTicks;

    private float holdingItemAnimationTicks0;

    private float dancingAnimationTicks;

    private float spinningAnimationTicks;

    private float spinningAnimationTicks0;

    public Allay(EntityType<? extends Allay> entityTypeExtendsAllay0, Level level1) {
        super(entityTypeExtendsAllay0, level1);
        this.f_21342_ = new FlyingMoveControl(this, 20, true);
        this.m_21553_(this.canPickUpLoot());
        this.vibrationUser = new Allay.VibrationUser();
        this.vibrationData = new VibrationSystem.Data();
        this.dynamicVibrationListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
        this.dynamicJukeboxListener = new DynamicGameEventListener<>(new Allay.JukeboxListener(this.vibrationUser.getPositionSource(), GameEvent.JUKEBOX_PLAY.getNotificationRadius()));
    }

    @Override
    protected Brain.Provider<Allay> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic0) {
        return AllayAi.makeBrain(this.brainProvider().makeBrain(dynamic0));
    }

    @Override
    public Brain<Allay> getBrain() {
        return super.m_6274_();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.FLYING_SPEED, 0.1F).add(Attributes.MOVEMENT_SPEED, 0.1F).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        FlyingPathNavigation $$1 = new FlyingPathNavigation(this, level0);
        $$1.setCanOpenDoors(false);
        $$1.m_7008_(true);
        $$1.setCanPassDoors(true);
        return $$1;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_DANCING, false);
        this.f_19804_.define(DATA_CAN_DUPLICATE, true);
    }

    @Override
    public void travel(Vec3 vec0) {
        if (this.m_6109_()) {
            if (this.m_20069_()) {
                this.m_19920_(0.02F, vec0);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.8F));
            } else if (this.m_20077_()) {
                this.m_19920_(0.02F, vec0);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.5));
            } else {
                this.m_19920_(this.m_6113_(), vec0);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.91F));
            }
        }
        this.m_267651_(false);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height * 0.6F;
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (damageSource0.getEntity() instanceof Player $$2) {
            Optional<UUID> $$3 = this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
            if ($$3.isPresent() && $$2.m_20148_().equals($$3.get())) {
                return false;
            }
        }
        return super.m_6469_(damageSource0, float1);
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
    }

    @Override
    protected void checkFallDamage(double double0, boolean boolean1, BlockState blockState2, BlockPos blockPos3) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_21033_(EquipmentSlot.MAINHAND) ? SoundEvents.ALLAY_AMBIENT_WITH_ITEM : SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.ALLAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ALLAY_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void customServerAiStep() {
        this.m_9236_().getProfiler().push("allayBrain");
        this.getBrain().tick((ServerLevel) this.m_9236_(), this);
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("allayActivityUpdate");
        AllayAi.updateActivity(this);
        this.m_9236_().getProfiler().pop();
        super.m_8024_();
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_9236_().isClientSide && this.m_6084_() && this.f_19797_ % 10 == 0) {
            this.m_5634_(1.0F);
        }
        if (this.isDancing() && this.shouldStopDancing() && this.f_19797_ % 20 == 0) {
            this.setDancing(false);
            this.jukeboxPos = null;
        }
        this.updateDuplicationCooldown();
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.m_9236_().isClientSide) {
            this.holdingItemAnimationTicks0 = this.holdingItemAnimationTicks;
            if (this.hasItemInHand()) {
                this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks + 1.0F, 0.0F, 5.0F);
            } else {
                this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks - 1.0F, 0.0F, 5.0F);
            }
            if (this.isDancing()) {
                this.dancingAnimationTicks++;
                this.spinningAnimationTicks0 = this.spinningAnimationTicks;
                if (this.isSpinning()) {
                    this.spinningAnimationTicks++;
                } else {
                    this.spinningAnimationTicks--;
                }
                this.spinningAnimationTicks = Mth.clamp(this.spinningAnimationTicks, 0.0F, 15.0F);
            } else {
                this.dancingAnimationTicks = 0.0F;
                this.spinningAnimationTicks = 0.0F;
                this.spinningAnimationTicks0 = 0.0F;
            }
        } else {
            VibrationSystem.Ticker.tick(this.m_9236_(), this.vibrationData, this.vibrationUser);
            if (this.isPanicking()) {
                this.setDancing(false);
            }
        }
    }

    @Override
    public boolean canPickUpLoot() {
        return !this.isOnPickupCooldown() && this.hasItemInHand();
    }

    public boolean hasItemInHand() {
        return !this.m_21120_(InteractionHand.MAIN_HAND).isEmpty();
    }

    @Override
    public boolean canTakeItem(ItemStack itemStack0) {
        return false;
    }

    private boolean isOnPickupCooldown() {
        return this.getBrain().checkMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.VALUE_PRESENT);
    }

    @Override
    protected InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        ItemStack $$3 = this.m_21120_(InteractionHand.MAIN_HAND);
        if (this.isDancing() && this.isDuplicationItem($$2) && this.canDuplicate()) {
            this.duplicateAllay();
            this.m_9236_().broadcastEntityEvent(this, (byte) 18);
            this.m_9236_().playSound(player0, this, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 2.0F, 1.0F);
            this.removeInteractionItem(player0, $$2);
            return InteractionResult.SUCCESS;
        } else if ($$3.isEmpty() && !$$2.isEmpty()) {
            ItemStack $$4 = $$2.copyWithCount(1);
            this.m_21008_(InteractionHand.MAIN_HAND, $$4);
            this.removeInteractionItem(player0, $$2);
            this.m_9236_().playSound(player0, this, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
            this.getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player0.m_20148_());
            return InteractionResult.SUCCESS;
        } else if (!$$3.isEmpty() && interactionHand1 == InteractionHand.MAIN_HAND && $$2.isEmpty()) {
            this.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.m_9236_().playSound(player0, this, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
            this.m_6674_(InteractionHand.MAIN_HAND);
            for (ItemStack $$5 : this.getInventory().removeAllItems()) {
                BehaviorUtils.throwItem(this, $$5, this.m_20182_());
            }
            this.getBrain().eraseMemory(MemoryModuleType.LIKED_PLAYER);
            player0.addItem($$3);
            return InteractionResult.SUCCESS;
        } else {
            return super.m_6071_(player0, interactionHand1);
        }
    }

    public void setJukeboxPlaying(BlockPos blockPos0, boolean boolean1) {
        if (boolean1) {
            if (!this.isDancing()) {
                this.jukeboxPos = blockPos0;
                this.setDancing(true);
            }
        } else if (blockPos0.equals(this.jukeboxPos) || this.jukeboxPos == null) {
            this.jukeboxPos = null;
            this.setDancing(false);
        }
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    protected Vec3i getPickupReach() {
        return ITEM_PICKUP_REACH;
    }

    @Override
    public boolean wantsToPickUp(ItemStack itemStack0) {
        ItemStack $$1 = this.m_21120_(InteractionHand.MAIN_HAND);
        return !$$1.isEmpty() && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.inventory.canAddItem(itemStack0) && this.allayConsidersItemEqual($$1, itemStack0);
    }

    private boolean allayConsidersItemEqual(ItemStack itemStack0, ItemStack itemStack1) {
        return ItemStack.isSameItem(itemStack0, itemStack1) && !this.hasNonMatchingPotion(itemStack0, itemStack1);
    }

    private boolean hasNonMatchingPotion(ItemStack itemStack0, ItemStack itemStack1) {
        CompoundTag $$2 = itemStack0.getTag();
        boolean $$3 = $$2 != null && $$2.contains("Potion");
        if (!$$3) {
            return false;
        } else {
            CompoundTag $$4 = itemStack1.getTag();
            boolean $$5 = $$4 != null && $$4.contains("Potion");
            if (!$$5) {
                return true;
            } else {
                Tag $$6 = $$2.get("Potion");
                Tag $$7 = $$4.get("Potion");
                return $$6 != null && $$7 != null && !$$6.equals($$7);
            }
        }
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity0) {
        InventoryCarrier.pickUpItem(this, this, itemEntity0);
    }

    @Override
    protected void sendDebugPackets() {
        super.m_8025_();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public boolean isFlapping() {
        return !this.m_20096_();
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumerDynamicGameEventListenerServerLevel0) {
        if (this.m_9236_() instanceof ServerLevel $$1) {
            biConsumerDynamicGameEventListenerServerLevel0.accept(this.dynamicVibrationListener, $$1);
            biConsumerDynamicGameEventListenerServerLevel0.accept(this.dynamicJukeboxListener, $$1);
        }
    }

    public boolean isDancing() {
        return this.f_19804_.get(DATA_DANCING);
    }

    public boolean isPanicking() {
        return this.f_20939_.getMemory(MemoryModuleType.IS_PANICKING).isPresent();
    }

    public void setDancing(boolean boolean0) {
        if (!this.m_9236_().isClientSide && this.m_21515_() && (!boolean0 || !this.isPanicking())) {
            this.f_19804_.set(DATA_DANCING, boolean0);
        }
    }

    private boolean shouldStopDancing() {
        return this.jukeboxPos == null || !this.jukeboxPos.m_203195_(this.m_20182_(), (double) GameEvent.JUKEBOX_PLAY.getNotificationRadius()) || !this.m_9236_().getBlockState(this.jukeboxPos).m_60713_(Blocks.JUKEBOX);
    }

    public float getHoldingItemAnimationProgress(float float0) {
        return Mth.lerp(float0, this.holdingItemAnimationTicks0, this.holdingItemAnimationTicks) / 5.0F;
    }

    public boolean isSpinning() {
        float $$0 = this.dancingAnimationTicks % 55.0F;
        return $$0 < 15.0F;
    }

    public float getSpinningProgress(float float0) {
        return Mth.lerp(float0, this.spinningAnimationTicks0, this.spinningAnimationTicks) / 15.0F;
    }

    @Override
    public boolean equipmentHasChanged(ItemStack itemStack0, ItemStack itemStack1) {
        return !this.allayConsidersItemEqual(itemStack0, itemStack1);
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        this.inventory.removeAllItems().forEach(this::m_19983_);
        ItemStack $$0 = this.m_6844_(EquipmentSlot.MAINHAND);
        if (!$$0.isEmpty() && !EnchantmentHelper.hasVanishingCurse($$0)) {
            this.m_19983_($$0);
            this.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        this.m_252802_(compoundTag0);
        VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(LOGGER::error).ifPresent(p_218353_ -> compoundTag0.put("listener", p_218353_));
        compoundTag0.putLong("DuplicationCooldown", this.duplicationCooldown);
        compoundTag0.putBoolean("CanDuplicate", this.canDuplicate());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.m_253224_(compoundTag0);
        if (compoundTag0.contains("listener", 10)) {
            VibrationSystem.Data.CODEC.parse(new Dynamic(NbtOps.INSTANCE, compoundTag0.getCompound("listener"))).resultOrPartial(LOGGER::error).ifPresent(p_281082_ -> this.vibrationData = p_281082_);
        }
        this.duplicationCooldown = (long) compoundTag0.getInt("DuplicationCooldown");
        this.f_19804_.set(DATA_CAN_DUPLICATE, compoundTag0.getBoolean("CanDuplicate"));
    }

    @Override
    protected boolean shouldStayCloseToLeashHolder() {
        return false;
    }

    private void updateDuplicationCooldown() {
        if (this.duplicationCooldown > 0L) {
            this.duplicationCooldown--;
        }
        if (!this.m_9236_().isClientSide() && this.duplicationCooldown == 0L && !this.canDuplicate()) {
            this.f_19804_.set(DATA_CAN_DUPLICATE, true);
        }
    }

    private boolean isDuplicationItem(ItemStack itemStack0) {
        return DUPLICATION_ITEM.test(itemStack0);
    }

    private void duplicateAllay() {
        Allay $$0 = EntityType.ALLAY.create(this.m_9236_());
        if ($$0 != null) {
            $$0.m_20219_(this.m_20182_());
            $$0.m_21530_();
            $$0.resetDuplicationCooldown();
            this.resetDuplicationCooldown();
            this.m_9236_().m_7967_($$0);
        }
    }

    private void resetDuplicationCooldown() {
        this.duplicationCooldown = 6000L;
        this.f_19804_.set(DATA_CAN_DUPLICATE, false);
    }

    private boolean canDuplicate() {
        return this.f_19804_.get(DATA_CAN_DUPLICATE);
    }

    private void removeInteractionItem(Player player0, ItemStack itemStack1) {
        if (!player0.getAbilities().instabuild) {
            itemStack1.shrink(1);
        }
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) this.m_20192_() * 0.6, (double) this.m_20205_() * 0.1);
    }

    @Override
    public double getMyRidingOffset() {
        return 0.4;
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 18) {
            for (int $$1 = 0; $$1 < 3; $$1++) {
                this.spawnHeartParticle();
            }
        } else {
            super.m_7822_(byte0);
        }
    }

    private void spawnHeartParticle() {
        double $$0 = this.f_19796_.nextGaussian() * 0.02;
        double $$1 = this.f_19796_.nextGaussian() * 0.02;
        double $$2 = this.f_19796_.nextGaussian() * 0.02;
        this.m_9236_().addParticle(ParticleTypes.HEART, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), $$0, $$1, $$2);
    }

    @Override
    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    class JukeboxListener implements GameEventListener {

        private final PositionSource listenerSource;

        private final int listenerRadius;

        public JukeboxListener(PositionSource positionSource0, int int1) {
            this.listenerSource = positionSource0;
            this.listenerRadius = int1;
        }

        @Override
        public PositionSource getListenerSource() {
            return this.listenerSource;
        }

        @Override
        public int getListenerRadius() {
            return this.listenerRadius;
        }

        @Override
        public boolean handleGameEvent(ServerLevel serverLevel0, GameEvent gameEvent1, GameEvent.Context gameEventContext2, Vec3 vec3) {
            if (gameEvent1 == GameEvent.JUKEBOX_PLAY) {
                Allay.this.setJukeboxPlaying(BlockPos.containing(vec3), true);
                return true;
            } else if (gameEvent1 == GameEvent.JUKEBOX_STOP_PLAY) {
                Allay.this.setJukeboxPlaying(BlockPos.containing(vec3), false);
                return true;
            } else {
                return false;
            }
        }
    }

    class VibrationUser implements VibrationSystem.User {

        private static final int VIBRATION_EVENT_LISTENER_RANGE = 16;

        private final PositionSource positionSource = new EntityPositionSource(Allay.this, Allay.this.m_20192_());

        @Override
        public int getListenerRadius() {
            return 16;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel serverLevel0, BlockPos blockPos1, GameEvent gameEvent2, GameEvent.Context gameEventContext3) {
            if (Allay.this.m_21525_()) {
                return false;
            } else {
                Optional<GlobalPos> $$4 = Allay.this.getBrain().getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
                if ($$4.isEmpty()) {
                    return true;
                } else {
                    GlobalPos $$5 = (GlobalPos) $$4.get();
                    return $$5.dimension().equals(serverLevel0.m_46472_()) && $$5.pos().equals(blockPos1);
                }
            }
        }

        @Override
        public void onReceiveVibration(ServerLevel serverLevel0, BlockPos blockPos1, GameEvent gameEvent2, @Nullable Entity entity3, @Nullable Entity entity4, float float5) {
            if (gameEvent2 == GameEvent.NOTE_BLOCK_PLAY) {
                AllayAi.hearNoteblock(Allay.this, new BlockPos(blockPos1));
            }
        }

        @Override
        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.ALLAY_CAN_LISTEN;
        }
    }
}