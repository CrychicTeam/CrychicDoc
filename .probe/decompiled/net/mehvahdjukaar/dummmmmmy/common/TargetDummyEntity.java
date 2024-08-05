package net.mehvahdjukaar.dummmmmmy.common;

import com.google.common.math.DoubleMath;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.mehvahdjukaar.dummmmmmy.configs.CommonConfigs;
import net.mehvahdjukaar.dummmmmmy.network.ClientBoundDamageNumberMessage;
import net.mehvahdjukaar.dummmmmmy.network.ClientBoundSyncEquipMessage;
import net.mehvahdjukaar.dummmmmmy.network.ClientBoundUpdateAnimationMessage;
import net.mehvahdjukaar.dummmmmmy.network.NetworkHandler;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TargetDummyEntity extends Mob {

    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(TargetDummyEntity.class, EntityDataSerializers.BOOLEAN);

    private int lastTickActuallyDamaged;

    private float totalDamageTakenInCombat;

    private final List<CritRecord> critRecordsThisTick = new ArrayList();

    private DummyMobType mobType = DummyMobType.UNDEFINED;

    private int damageNumberPos = 0;

    private final NonNullList<ItemStack> lastArmorItems = NonNullList.withSize(4, ItemStack.EMPTY);

    private final Map<ServerPlayer, Integer> currentlyAttacking = new HashMap();

    private DamageSource currentDamageSource = null;

    private boolean unbreakable = false;

    private float prevAnimationPosition = 0.0F;

    private float animationPosition;

    private float shakeAmount = 0.0F;

    private float prevShakeAmount = 0.0F;

    public TargetDummyEntity(EntityType<TargetDummyEntity> type, Level world) {
        super(type, world);
    }

    public TargetDummyEntity(Level world) {
        this((EntityType<TargetDummyEntity>) Dummmmmmy.TARGET_DUMMY.get(), world);
        this.f_21364_ = 0;
        Arrays.fill(this.f_21348_, 1.1F);
    }

    public float getShake(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevShakeAmount, this.shakeAmount);
    }

    public float getAnimationPosition(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevAnimationPosition, this.animationPosition);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(SHEARED, false);
    }

    public boolean isSheared() {
        return this.f_19804_.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.f_19804_.set(SHEARED, sheared);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return PlatHelper.getEntitySpawnPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Type", this.mobType.ordinal());
        tag.putInt("NumberPos", this.damageNumberPos);
        tag.putBoolean("Sheared", this.isSheared());
        if (this.unbreakable) {
            tag.putBoolean("Unbreakable", true);
        }
        this.applyEquipmentModifiers();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.mobType = DummyMobType.values()[tag.getInt("Type")];
        this.damageNumberPos = tag.getInt("NumberPos");
        this.setSheared(tag.getBoolean("Sheared"));
        if (tag.contains("unbreakable")) {
            this.unbreakable = tag.getBoolean("unbreakable");
        }
    }

    @Override
    public void setYBodyRot(float pOffset) {
        float r = this.m_146908_();
        this.f_19859_ = r;
        this.f_20884_ = this.f_20883_ = r;
    }

    @Override
    public void setYHeadRot(float pRotation) {
        float r = this.m_146908_();
        this.f_19859_ = r;
        this.f_20886_ = this.f_20885_ = r;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        boolean inventoryChanged = false;
        if (!player.isSpectator() && player.getAbilities().mayBuild) {
            ItemStack itemstack = player.m_21120_(hand);
            EquipmentSlot equipmentSlot = m_147233_(itemstack);
            Item item = itemstack.getItem();
            if (item instanceof BannerItem || DummyMobType.get(itemstack) != DummyMobType.UNDEFINED || ForgeHelper.canEquipItem(this, itemstack, EquipmentSlot.HEAD)) {
                equipmentSlot = EquipmentSlot.HEAD;
            }
            Level level = player.m_9236_();
            if (itemstack.isEmpty() && hand == InteractionHand.MAIN_HAND) {
                equipmentSlot = this.getClickedSlot(vec);
                if (this.m_21033_(equipmentSlot)) {
                    if (level.isClientSide) {
                        return InteractionResult.CONSUME;
                    }
                    this.unEquipArmor(player, equipmentSlot, hand);
                    inventoryChanged = true;
                }
            } else if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
                if (level.isClientSide) {
                    return InteractionResult.CONSUME;
                }
                this.equipArmor(player, equipmentSlot, itemstack, hand);
                inventoryChanged = true;
            } else if (item instanceof ShearsItem && !this.isSheared()) {
                level.playSound(player, this, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (level.isClientSide) {
                    return InteractionResult.CONSUME;
                }
                this.setSheared(true);
                return InteractionResult.SUCCESS;
            }
            if (inventoryChanged) {
                this.setLastArmorItem(equipmentSlot, itemstack);
                if (!level.isClientSide) {
                    NetworkHandler.CHANNEL.sentToAllClientPlayersTrackingEntity(this, new ClientBoundSyncEquipMessage(this.m_19879_(), equipmentSlot.getIndex(), this.m_6844_(equipmentSlot)));
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private void unEquipArmor(Player player, EquipmentSlot slot, InteractionHand hand) {
        ItemStack itemstack = this.m_6844_(slot);
        ItemStack itemstack2 = itemstack.copy();
        player.m_21008_(hand, itemstack2);
        this.m_8061_(slot, ItemStack.EMPTY);
        this.m_21204_().removeAttributeModifiers(itemstack2.getAttributeModifiers(slot));
        if (slot == EquipmentSlot.HEAD) {
            this.mobType = DummyMobType.UNDEFINED;
        }
    }

    private void equipArmor(Player player, EquipmentSlot slot, ItemStack stack, InteractionHand hand) {
        ItemStack currentItem = this.m_6844_(slot);
        ItemStack newItem = stack.copy();
        newItem.setCount(1);
        player.m_21008_(hand, ItemUtils.createFilledResult(stack.copy(), player, currentItem, player.isCreative()));
        this.m_8061_(slot, newItem);
        this.m_21204_().addTransientAttributeModifiers(newItem.getAttributeModifiers(slot));
        if (slot == EquipmentSlot.HEAD) {
            this.mobType = DummyMobType.get(newItem);
        }
    }

    public boolean canScare() {
        return this.mobType == DummyMobType.SCARECROW;
    }

    public boolean canAttract() {
        return this.mobType == DummyMobType.DECOY;
    }

    private EquipmentSlot getClickedSlot(Vec3 vec3) {
        EquipmentSlot equipmentSlot = EquipmentSlot.MAINHAND;
        double d0 = vec3.y;
        EquipmentSlot slot = EquipmentSlot.FEET;
        if (d0 >= 0.1 && d0 < 0.55 && this.m_21033_(slot)) {
            equipmentSlot = EquipmentSlot.FEET;
        } else if (d0 >= 0.9 && d0 < 1.6 && this.m_21033_(EquipmentSlot.CHEST)) {
            equipmentSlot = EquipmentSlot.CHEST;
        } else if (d0 >= 0.4 && d0 < 1.2000000000000002 && this.m_21033_(EquipmentSlot.LEGS)) {
            equipmentSlot = EquipmentSlot.LEGS;
        } else if (d0 >= 1.6 && this.m_21033_(EquipmentSlot.HEAD)) {
            equipmentSlot = EquipmentSlot.HEAD;
        }
        return equipmentSlot;
    }

    private void setLastArmorItem(EquipmentSlot type, ItemStack stack) {
        this.lastArmorItems.set(type.getIndex(), stack);
    }

    public void applyEquipmentModifiers() {
        if (!this.m_9236_().isClientSide) {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack itemstack = this.lastArmorItems.get(equipmentSlot.getIndex());
                    ItemStack slot = this.m_6844_(equipmentSlot);
                    if (!ItemStack.matches(slot, itemstack)) {
                        if (!slot.equals(itemstack)) {
                            ForgeHelper.onEquipmentChange(this, equipmentSlot, itemstack, slot);
                        }
                        if (!itemstack.isEmpty()) {
                            this.m_21204_().removeAttributeModifiers(itemstack.getAttributeModifiers(equipmentSlot));
                        }
                        if (!slot.isEmpty()) {
                            this.m_21204_().addTransientAttributeModifiers(slot.getAttributeModifiers(equipmentSlot));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void dropEquipment() {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack armor = this.m_6844_(slot);
                if (!armor.isEmpty()) {
                    this.m_5552_(armor, 1.0F);
                }
            }
        }
        this.m_5552_(this.getPickResult(), 1.0F);
    }

    public void dismantle(boolean drops) {
        Level level = this.m_9236_();
        if (!level.isClientSide && this.m_6084_()) {
            if (drops) {
                this.dropEquipment();
            }
            level.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), this.getDeathSound(), this.m_5720_(), 1.0F, 1.0F);
            ((ServerLevel) level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState()), this.m_20185_(), this.m_20227_(0.6666666666666666), this.m_20189_(), 10, (double) (this.m_20205_() / 4.0F), (double) (this.m_20206_() / 4.0F), (double) (this.m_20205_() / 4.0F), 0.05);
            this.m_142687_(Entity.RemovalReason.KILLED);
            this.m_146850_(GameEvent.ENTITY_DIE);
        }
    }

    @NotNull
    @Override
    public ItemStack getPickResult() {
        ItemStack itemStack = new ItemStack((ItemLike) Dummmmmmy.DUMMY_ITEM.get());
        if (this.m_8077_()) {
            itemStack.setHoverName(this.m_7770_());
        }
        return itemStack;
    }

    @Override
    public void kill() {
        this.dismantle(true);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.m_6673_(source) || source == this.m_269291_().drown() || source == this.m_269291_().inWall();
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source == this.m_269291_().fellOutOfWorld()) {
            this.m_142687_(Entity.RemovalReason.KILLED);
            return true;
        } else if (!(source.getDirectEntity() instanceof WitherBoss) && !(source.getEntity() instanceof WitherBoss)) {
            if (source.getEntity() instanceof Player player) {
                if (player instanceof ServerPlayer sp) {
                    this.currentlyAttacking.put(sp, (Integer) CommonConfigs.MAX_COMBAT_INTERVAL.get());
                }
                if (player.m_6144_() && player.m_21205_().isEmpty() && !this.unbreakable) {
                    this.dismantle(!player.isCreative());
                    return false;
                }
            }
            if (this.m_9236_().isClientSide) {
                return false;
            } else {
                DamageSource old = this.currentDamageSource;
                this.currentDamageSource = source;
                if (!this.critRecordsThisTick.isEmpty()) {
                    CritRecord critRecord = (CritRecord) this.critRecordsThisTick.get(this.critRecordsThisTick.size() - 1);
                    if (critRecord.canCompleteWith(source)) {
                        critRecord.addSource(source);
                    }
                }
                boolean result = super.m_6469_(source, damage);
                this.currentDamageSource = old;
                this.f_20916_ = 0;
                return result;
            }
        } else {
            this.dismantle(true);
            return true;
        }
    }

    @Override
    public void setHealth(float newHealth) {
        if (newHealth == this.m_21233_()) {
            super.m_21153_(newHealth);
        } else {
            Level level = this.m_9236_();
            if (level.isClientSide) {
                return;
            }
            float damage = this.m_21223_() - newHealth;
            if (damage > 0.0F) {
                DamageSource actualSource = null;
                if (PlatHelper.getPlatform().isForge()) {
                    CombatEntry currentCombatEntry = this.getLastEntry();
                    if (currentCombatEntry != null && this.m_21231_().lastDamageTime == this.f_19797_ && DoubleMath.fuzzyEquals((double) damage, (double) currentCombatEntry.damage(), 1.0E-6)) {
                        actualSource = currentCombatEntry.source();
                    }
                } else {
                    actualSource = this.currentDamageSource;
                }
                if (actualSource != null) {
                    this.onActuallyDamagedOrTrueDamageDetected(damage, actualSource);
                }
                this.lastTickActuallyDamaged = this.f_19797_;
            }
        }
    }

    private void onActuallyDamagedOrTrueDamageDetected(float damage, @Nullable DamageSource actualSource) {
        this.showDamageAndAnimationsToClients(damage, actualSource);
        this.updateTargetBlock(damage);
        if (this.m_9236_() instanceof ServerLevel sl) {
            float xp = ((Double) CommonConfigs.DROP_XP.get()).floatValue() * damage;
            if (xp > 0.0F) {
                ExperienceOrb.award(sl, this.m_20182_().add(0.0, 0.5, 0.0), Mth.floor(xp));
            }
        }
    }

    @Nullable
    public CombatEntry getLastEntry() {
        CombatTracker tracker = this.m_21231_();
        return tracker.entries.isEmpty() ? null : (CombatEntry) tracker.entries.get(tracker.entries.size() - 1);
    }

    private void showDamageAndAnimationsToClients(float damage, @Nullable DamageSource source) {
        if (this.lastTickActuallyDamaged != this.f_19797_) {
            this.animationPosition = 0.0F;
        }
        this.animationPosition = Math.min(this.animationPosition + damage, 60.0F);
        NetworkHandler.CHANNEL.sentToAllClientPlayersTrackingEntity(this, new ClientBoundUpdateAnimationMessage(this.m_19879_(), this.animationPosition));
        if (source != null && !this.currentlyAttacking.isEmpty()) {
            CritRecord critRec = null;
            for (int j = this.critRecordsThisTick.size() - 1; j >= 0; j--) {
                CritRecord c = (CritRecord) this.critRecordsThisTick.get(j);
                if (c.matches(source)) {
                    critRec = c;
                    break;
                }
            }
            for (ServerPlayer p : this.currentlyAttacking.keySet()) {
                NetworkHandler.CHANNEL.sendToClientPlayer(p, new ClientBoundDamageNumberMessage(this.m_19879_(), damage, source, critRec));
            }
            if (critRec != null) {
                this.critRecordsThisTick.remove(critRec);
            }
        }
        this.totalDamageTakenInCombat += damage;
    }

    private void updateTargetBlock(float damage) {
        BlockPos pos = this.m_20097_();
        BlockState state = this.m_20075_();
        if (state.m_60734_() instanceof TargetBlock) {
            Level level = this.m_9236_();
            if (!level.m_183326_().m_183582_(pos, state.m_60734_())) {
                int power = (int) Mth.clamp(damage / this.m_21223_() * 15.0F, 1.0F, 15.0F);
                level.setBlock(pos, (BlockState) state.m_61124_(BlockStateProperties.POWER, power), 3);
                level.m_186460_(pos, state.m_60734_(), 20);
            }
        }
    }

    @Override
    protected void updateControlFlags() {
    }

    @Override
    protected Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (this.m_20192_() - 1.0F), 0.0);
    }

    @Override
    public void aiStep() {
    }

    @Override
    public void tick() {
        this.critRecordsThisTick.clear();
        Level level = this.m_9236_();
        if (this.lastTickActuallyDamaged + 1 == this.f_19797_ && !level.isClientSide) {
            float trueDamage = this.m_21233_() - this.m_21223_();
            if (trueDamage > 0.0F) {
                this.m_5634_(trueDamage);
                this.onActuallyDamagedOrTrueDamageDetected(trueDamage, null);
            }
        }
        BlockPos onPos = this.m_20097_();
        if (!level.isClientSide && level.getGameTime() % 20L == 0L && level.m_46859_(onPos)) {
            this.dismantle(true);
        } else {
            this.setNoGravity(true);
            BlockState onState = level.getBlockState(onPos);
            onState.m_60734_().stepOn(level, onPos, onState, this);
            super.tick();
            if (level.isClientSide) {
                this.f_20916_ = 0;
                this.prevShakeAmount = this.shakeAmount;
                this.prevAnimationPosition = this.animationPosition;
                if (this.animationPosition > 0.0F) {
                    this.shakeAmount++;
                    this.animationPosition -= 0.8F;
                    if (this.animationPosition <= 0.0F) {
                        this.shakeAmount = 0.0F;
                        this.animationPosition = 0.0F;
                    }
                }
            } else {
                this.displayCombatMessages();
            }
        }
    }

    private void displayCombatMessages() {
        CombatTracker tracker = this.m_21231_();
        tracker.recheckStatus();
        if (tracker.inCombat && this.totalDamageTakenInCombat > 0.0F) {
            float combatDuration = (float) tracker.getCombatDuration();
            CommonConfigs.DpsMode dpsMode = (CommonConfigs.DpsMode) CommonConfigs.DYNAMIC_DPS.get();
            if (dpsMode != CommonConfigs.DpsMode.OFF && combatDuration > 0.0F) {
                boolean dynamic = dpsMode == CommonConfigs.DpsMode.DYNAMIC;
                float seconds = combatDuration / 20.0F + 1.0F;
                float dps = this.totalDamageTakenInCombat / seconds;
                List<ServerPlayer> outOfCombat = new ArrayList();
                for (Entry<ServerPlayer, Integer> e : this.currentlyAttacking.entrySet()) {
                    ServerPlayer p = (ServerPlayer) e.getKey();
                    int timer = (Integer) e.getValue() - 1;
                    this.currentlyAttacking.replace(p, timer);
                    boolean showMessage = dynamic && this.lastTickActuallyDamaged + 1 == this.f_19797_;
                    if (timer <= 0) {
                        outOfCombat.add(p);
                        if (!dynamic) {
                            showMessage = true;
                        }
                    }
                    if (showMessage && p.m_20270_(this) < 64.0F) {
                        p.displayClientMessage(Component.translatable("message.dummmmmmy.dps", this.m_5446_(), new DecimalFormat("#.##").format((double) dps)), true);
                    }
                }
                outOfCombat.forEach(this.currentlyAttacking::remove);
            }
        } else {
            this.currentlyAttacking.clear();
            this.totalDamageTakenInCombat = 0.0F;
        }
    }

    @Override
    public void setDeltaMovement(Vec3 motionIn) {
    }

    @Override
    public void knockback(double strength, double x, double z) {
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
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void markHurt() {
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void setNoGravity(boolean ignored) {
        super.m_20242_(true);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.ARMOR_STAND_HIT;
    }

    @NotNull
    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ARMOR_STAND_BREAK;
    }

    @NotNull
    @Override
    public MobType getMobType() {
        return this.mobType.getType();
    }

    public static AttributeSupplier.Builder makeAttributes() {
        return Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.0).add(Attributes.MAX_HEALTH, 40.0).add(Attributes.ARMOR, 0.0).add(Attributes.ATTACK_DAMAGE, 0.0).add(Attributes.FLYING_SPEED, 0.0);
    }

    public void updateAnimation(float shake) {
        this.animationPosition = shake;
    }

    public void moist(Entity attacker, float critModifier) {
        this.critRecordsThisTick.add(new CritRecord(attacker, critModifier));
    }

    public int getNextNumberPos() {
        return this.damageNumberPos++;
    }
}