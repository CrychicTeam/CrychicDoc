package net.minecraft.world.entity;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public abstract class Mob extends LivingEntity implements Targeting {

    private static final EntityDataAccessor<Byte> DATA_MOB_FLAGS_ID = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BYTE);

    private static final int MOB_FLAG_NO_AI = 1;

    private static final int MOB_FLAG_LEFTHANDED = 2;

    private static final int MOB_FLAG_AGGRESSIVE = 4;

    protected static final int PICKUP_REACH = 1;

    private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 0, 1);

    public static final float MAX_WEARING_ARMOR_CHANCE = 0.15F;

    public static final float MAX_PICKUP_LOOT_CHANCE = 0.55F;

    public static final float MAX_ENCHANTED_ARMOR_CHANCE = 0.5F;

    public static final float MAX_ENCHANTED_WEAPON_CHANCE = 0.25F;

    public static final String LEASH_TAG = "Leash";

    public static final float DEFAULT_EQUIPMENT_DROP_CHANCE = 0.085F;

    public static final int PRESERVE_ITEM_DROP_CHANCE = 2;

    public static final int UPDATE_GOAL_SELECTOR_EVERY_N_TICKS = 2;

    public int ambientSoundTime;

    protected int xpReward;

    protected LookControl lookControl;

    protected MoveControl moveControl;

    protected JumpControl jumpControl;

    private final BodyRotationControl bodyRotationControl;

    protected PathNavigation navigation;

    protected final GoalSelector goalSelector;

    protected final GoalSelector targetSelector;

    @Nullable
    private LivingEntity target;

    private final Sensing sensing;

    private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);

    protected final float[] handDropChances = new float[2];

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);

    protected final float[] armorDropChances = new float[4];

    private boolean canPickUpLoot;

    private boolean persistenceRequired;

    private final Map<BlockPathTypes, Float> pathfindingMalus = Maps.newEnumMap(BlockPathTypes.class);

    @Nullable
    private ResourceLocation lootTable;

    private long lootTableSeed;

    @Nullable
    private Entity leashHolder;

    private int delayedLeashHolderId;

    @Nullable
    private CompoundTag leashInfoTag;

    private BlockPos restrictCenter = BlockPos.ZERO;

    private float restrictRadius = -1.0F;

    protected Mob(EntityType<? extends Mob> entityTypeExtendsMob0, Level level1) {
        super(entityTypeExtendsMob0, level1);
        this.goalSelector = new GoalSelector(level1.getProfilerSupplier());
        this.targetSelector = new GoalSelector(level1.getProfilerSupplier());
        this.lookControl = new LookControl(this);
        this.moveControl = new MoveControl(this);
        this.jumpControl = new JumpControl(this);
        this.bodyRotationControl = this.createBodyControl();
        this.navigation = this.createNavigation(level1);
        this.sensing = new Sensing(this);
        Arrays.fill(this.armorDropChances, 0.085F);
        Arrays.fill(this.handDropChances, 0.085F);
        if (level1 != null && !level1.isClientSide) {
            this.registerGoals();
        }
    }

    protected void registerGoals() {
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.ATTACK_KNOCKBACK);
    }

    protected PathNavigation createNavigation(Level level0) {
        return new GroundPathNavigation(this, level0);
    }

    protected boolean shouldPassengersInheritMalus() {
        return false;
    }

    public float getPathfindingMalus(BlockPathTypes blockPathTypes0) {
        Mob $$2;
        label17: {
            if (this.m_275832_() instanceof Mob $$1 && $$1.shouldPassengersInheritMalus()) {
                $$2 = $$1;
                break label17;
            }
            $$2 = this;
        }
        Float $$4 = (Float) $$2.pathfindingMalus.get(blockPathTypes0);
        return $$4 == null ? blockPathTypes0.getMalus() : $$4;
    }

    public void setPathfindingMalus(BlockPathTypes blockPathTypes0, float float1) {
        this.pathfindingMalus.put(blockPathTypes0, float1);
    }

    public void onPathfindingStart() {
    }

    public void onPathfindingDone() {
    }

    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this);
    }

    public LookControl getLookControl() {
        return this.lookControl;
    }

    public MoveControl getMoveControl() {
        return this.m_275832_() instanceof Mob $$0 ? $$0.getMoveControl() : this.moveControl;
    }

    public JumpControl getJumpControl() {
        return this.jumpControl;
    }

    public PathNavigation getNavigation() {
        return this.m_275832_() instanceof Mob $$0 ? $$0.getNavigation() : this.navigation;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return !this.isNoAi() && this.m_146895_() instanceof Mob $$0 ? $$0 : null;
    }

    public Sensing getSensing() {
        return this.sensing;
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.target;
    }

    public void setTarget(@Nullable LivingEntity livingEntity0) {
        this.target = livingEntity0;
    }

    @Override
    public boolean canAttackType(EntityType<?> entityType0) {
        return entityType0 != EntityType.GHAST;
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeaponItem0) {
        return false;
    }

    public void ate() {
        this.m_146850_(GameEvent.EAT);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_MOB_FLAGS_ID, (byte) 0);
    }

    public int getAmbientSoundInterval() {
        return 80;
    }

    public void playAmbientSound() {
        SoundEvent $$0 = this.getAmbientSound();
        if ($$0 != null) {
            this.m_5496_($$0, this.m_6121_(), this.m_6100_());
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.m_9236_().getProfiler().push("mobBaseTick");
        if (this.m_6084_() && this.f_19796_.nextInt(1000) < this.ambientSoundTime++) {
            this.resetAmbientSoundTime();
            this.playAmbientSound();
        }
        this.m_9236_().getProfiler().pop();
    }

    @Override
    protected void playHurtSound(DamageSource damageSource0) {
        this.resetAmbientSoundTime();
        super.playHurtSound(damageSource0);
    }

    private void resetAmbientSoundTime() {
        this.ambientSoundTime = -this.getAmbientSoundInterval();
    }

    @Override
    public int getExperienceReward() {
        if (this.xpReward > 0) {
            int $$0 = this.xpReward;
            for (int $$1 = 0; $$1 < this.armorItems.size(); $$1++) {
                if (!this.armorItems.get($$1).isEmpty() && this.armorDropChances[$$1] <= 1.0F) {
                    $$0 += 1 + this.f_19796_.nextInt(3);
                }
            }
            for (int $$2 = 0; $$2 < this.handItems.size(); $$2++) {
                if (!this.handItems.get($$2).isEmpty() && this.handDropChances[$$2] <= 1.0F) {
                    $$0 += 1 + this.f_19796_.nextInt(3);
                }
            }
            return $$0;
        } else {
            return this.xpReward;
        }
    }

    public void spawnAnim() {
        if (this.m_9236_().isClientSide) {
            for (int $$0 = 0; $$0 < 20; $$0++) {
                double $$1 = this.f_19796_.nextGaussian() * 0.02;
                double $$2 = this.f_19796_.nextGaussian() * 0.02;
                double $$3 = this.f_19796_.nextGaussian() * 0.02;
                double $$4 = 10.0;
                this.m_9236_().addParticle(ParticleTypes.POOF, this.m_20165_(1.0) - $$1 * 10.0, this.m_20187_() - $$2 * 10.0, this.m_20262_(1.0) - $$3 * 10.0, $$1, $$2, $$3);
            }
        } else {
            this.m_9236_().broadcastEntityEvent(this, (byte) 20);
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 20) {
            this.spawnAnim();
        } else {
            super.handleEntityEvent(byte0);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_9236_().isClientSide) {
            this.tickLeash();
            if (this.f_19797_ % 5 == 0) {
                this.updateControlFlags();
            }
        }
    }

    protected void updateControlFlags() {
        boolean $$0 = !(this.getControllingPassenger() instanceof Mob);
        boolean $$1 = !(this.m_20202_() instanceof Boat);
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, $$0);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, $$0 && $$1);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, $$0);
    }

    @Override
    protected float tickHeadTurn(float float0, float float1) {
        this.bodyRotationControl.clientTick();
        return float1;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putBoolean("CanPickUpLoot", this.canPickUpLoot());
        compoundTag0.putBoolean("PersistenceRequired", this.persistenceRequired);
        ListTag $$1 = new ListTag();
        for (ItemStack $$2 : this.armorItems) {
            CompoundTag $$3 = new CompoundTag();
            if (!$$2.isEmpty()) {
                $$2.save($$3);
            }
            $$1.add($$3);
        }
        compoundTag0.put("ArmorItems", $$1);
        ListTag $$4 = new ListTag();
        for (ItemStack $$5 : this.handItems) {
            CompoundTag $$6 = new CompoundTag();
            if (!$$5.isEmpty()) {
                $$5.save($$6);
            }
            $$4.add($$6);
        }
        compoundTag0.put("HandItems", $$4);
        ListTag $$7 = new ListTag();
        for (float $$8 : this.armorDropChances) {
            $$7.add(FloatTag.valueOf($$8));
        }
        compoundTag0.put("ArmorDropChances", $$7);
        ListTag $$9 = new ListTag();
        for (float $$10 : this.handDropChances) {
            $$9.add(FloatTag.valueOf($$10));
        }
        compoundTag0.put("HandDropChances", $$9);
        if (this.leashHolder != null) {
            CompoundTag $$11 = new CompoundTag();
            if (this.leashHolder instanceof LivingEntity) {
                UUID $$12 = this.leashHolder.getUUID();
                $$11.putUUID("UUID", $$12);
            } else if (this.leashHolder instanceof HangingEntity) {
                BlockPos $$13 = ((HangingEntity) this.leashHolder).getPos();
                $$11.putInt("X", $$13.m_123341_());
                $$11.putInt("Y", $$13.m_123342_());
                $$11.putInt("Z", $$13.m_123343_());
            }
            compoundTag0.put("Leash", $$11);
        } else if (this.leashInfoTag != null) {
            compoundTag0.put("Leash", this.leashInfoTag.copy());
        }
        compoundTag0.putBoolean("LeftHanded", this.isLeftHanded());
        if (this.lootTable != null) {
            compoundTag0.putString("DeathLootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                compoundTag0.putLong("DeathLootTableSeed", this.lootTableSeed);
            }
        }
        if (this.isNoAi()) {
            compoundTag0.putBoolean("NoAI", this.isNoAi());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("CanPickUpLoot", 1)) {
            this.setCanPickUpLoot(compoundTag0.getBoolean("CanPickUpLoot"));
        }
        this.persistenceRequired = compoundTag0.getBoolean("PersistenceRequired");
        if (compoundTag0.contains("ArmorItems", 9)) {
            ListTag $$1 = compoundTag0.getList("ArmorItems", 10);
            for (int $$2 = 0; $$2 < this.armorItems.size(); $$2++) {
                this.armorItems.set($$2, ItemStack.of($$1.getCompound($$2)));
            }
        }
        if (compoundTag0.contains("HandItems", 9)) {
            ListTag $$3 = compoundTag0.getList("HandItems", 10);
            for (int $$4 = 0; $$4 < this.handItems.size(); $$4++) {
                this.handItems.set($$4, ItemStack.of($$3.getCompound($$4)));
            }
        }
        if (compoundTag0.contains("ArmorDropChances", 9)) {
            ListTag $$5 = compoundTag0.getList("ArmorDropChances", 5);
            for (int $$6 = 0; $$6 < $$5.size(); $$6++) {
                this.armorDropChances[$$6] = $$5.getFloat($$6);
            }
        }
        if (compoundTag0.contains("HandDropChances", 9)) {
            ListTag $$7 = compoundTag0.getList("HandDropChances", 5);
            for (int $$8 = 0; $$8 < $$7.size(); $$8++) {
                this.handDropChances[$$8] = $$7.getFloat($$8);
            }
        }
        if (compoundTag0.contains("Leash", 10)) {
            this.leashInfoTag = compoundTag0.getCompound("Leash");
        }
        this.setLeftHanded(compoundTag0.getBoolean("LeftHanded"));
        if (compoundTag0.contains("DeathLootTable", 8)) {
            this.lootTable = new ResourceLocation(compoundTag0.getString("DeathLootTable"));
            this.lootTableSeed = compoundTag0.getLong("DeathLootTableSeed");
        }
        this.setNoAi(compoundTag0.getBoolean("NoAI"));
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSource0, boolean boolean1) {
        super.dropFromLootTable(damageSource0, boolean1);
        this.lootTable = null;
    }

    @Override
    public final ResourceLocation getLootTable() {
        return this.lootTable == null ? this.getDefaultLootTable() : this.lootTable;
    }

    protected ResourceLocation getDefaultLootTable() {
        return super.getLootTable();
    }

    @Override
    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    public void setZza(float float0) {
        this.f_20902_ = float0;
    }

    public void setYya(float float0) {
        this.f_20901_ = float0;
    }

    public void setXxa(float float0) {
        this.f_20900_ = float0;
    }

    @Override
    public void setSpeed(float float0) {
        super.setSpeed(float0);
        this.setZza(float0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.m_9236_().getProfiler().push("looting");
        if (!this.m_9236_().isClientSide && this.canPickUpLoot() && this.m_6084_() && !this.f_20890_ && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            Vec3i $$0 = this.getPickupReach();
            for (ItemEntity $$2 : this.m_9236_().m_45976_(ItemEntity.class, this.m_20191_().inflate((double) $$0.getX(), (double) $$0.getY(), (double) $$0.getZ()))) {
                if (!$$2.m_213877_() && !$$2.getItem().isEmpty() && !$$2.hasPickUpDelay() && this.wantsToPickUp($$2.getItem())) {
                    this.pickUpItem($$2);
                }
            }
        }
        this.m_9236_().getProfiler().pop();
    }

    protected Vec3i getPickupReach() {
        return ITEM_PICKUP_REACH;
    }

    protected void pickUpItem(ItemEntity itemEntity0) {
        ItemStack $$1 = itemEntity0.getItem();
        ItemStack $$2 = this.equipItemIfPossible($$1.copy());
        if (!$$2.isEmpty()) {
            this.m_21053_(itemEntity0);
            this.m_7938_(itemEntity0, $$2.getCount());
            $$1.shrink($$2.getCount());
            if ($$1.isEmpty()) {
                itemEntity0.m_146870_();
            }
        }
    }

    public ItemStack equipItemIfPossible(ItemStack itemStack0) {
        EquipmentSlot $$1 = m_147233_(itemStack0);
        ItemStack $$2 = this.getItemBySlot($$1);
        boolean $$3 = this.canReplaceCurrentItem(itemStack0, $$2);
        if ($$1.isArmor() && !$$3) {
            $$1 = EquipmentSlot.MAINHAND;
            $$2 = this.getItemBySlot($$1);
            $$3 = $$2.isEmpty();
        }
        if ($$3 && this.canHoldItem(itemStack0)) {
            double $$4 = (double) this.getEquipmentDropChance($$1);
            if (!$$2.isEmpty() && (double) Math.max(this.f_19796_.nextFloat() - 0.1F, 0.0F) < $$4) {
                this.m_19983_($$2);
            }
            if ($$1.isArmor() && itemStack0.getCount() > 1) {
                ItemStack $$5 = itemStack0.copyWithCount(1);
                this.setItemSlotAndDropWhenKilled($$1, $$5);
                return $$5;
            } else {
                this.setItemSlotAndDropWhenKilled($$1, itemStack0);
                return itemStack0;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    protected void setItemSlotAndDropWhenKilled(EquipmentSlot equipmentSlot0, ItemStack itemStack1) {
        this.setItemSlot(equipmentSlot0, itemStack1);
        this.setGuaranteedDrop(equipmentSlot0);
        this.persistenceRequired = true;
    }

    public void setGuaranteedDrop(EquipmentSlot equipmentSlot0) {
        switch(equipmentSlot0.getType()) {
            case HAND:
                this.handDropChances[equipmentSlot0.getIndex()] = 2.0F;
                break;
            case ARMOR:
                this.armorDropChances[equipmentSlot0.getIndex()] = 2.0F;
        }
    }

    protected boolean canReplaceCurrentItem(ItemStack itemStack0, ItemStack itemStack1) {
        if (itemStack1.isEmpty()) {
            return true;
        } else if (itemStack0.getItem() instanceof SwordItem) {
            if (!(itemStack1.getItem() instanceof SwordItem)) {
                return true;
            } else {
                SwordItem $$2 = (SwordItem) itemStack0.getItem();
                SwordItem $$3 = (SwordItem) itemStack1.getItem();
                return $$2.getDamage() != $$3.getDamage() ? $$2.getDamage() > $$3.getDamage() : this.canReplaceEqualItem(itemStack0, itemStack1);
            }
        } else if (itemStack0.getItem() instanceof BowItem && itemStack1.getItem() instanceof BowItem) {
            return this.canReplaceEqualItem(itemStack0, itemStack1);
        } else if (itemStack0.getItem() instanceof CrossbowItem && itemStack1.getItem() instanceof CrossbowItem) {
            return this.canReplaceEqualItem(itemStack0, itemStack1);
        } else if (itemStack0.getItem() instanceof ArmorItem) {
            if (EnchantmentHelper.hasBindingCurse(itemStack1)) {
                return false;
            } else if (!(itemStack1.getItem() instanceof ArmorItem)) {
                return true;
            } else {
                ArmorItem $$4 = (ArmorItem) itemStack0.getItem();
                ArmorItem $$5 = (ArmorItem) itemStack1.getItem();
                if ($$4.getDefense() != $$5.getDefense()) {
                    return $$4.getDefense() > $$5.getDefense();
                } else {
                    return $$4.getToughness() != $$5.getToughness() ? $$4.getToughness() > $$5.getToughness() : this.canReplaceEqualItem(itemStack0, itemStack1);
                }
            }
        } else {
            if (itemStack0.getItem() instanceof DiggerItem) {
                if (itemStack1.getItem() instanceof BlockItem) {
                    return true;
                }
                if (itemStack1.getItem() instanceof DiggerItem) {
                    DiggerItem $$6 = (DiggerItem) itemStack0.getItem();
                    DiggerItem $$7 = (DiggerItem) itemStack1.getItem();
                    if ($$6.getAttackDamage() != $$7.getAttackDamage()) {
                        return $$6.getAttackDamage() > $$7.getAttackDamage();
                    }
                    return this.canReplaceEqualItem(itemStack0, itemStack1);
                }
            }
            return false;
        }
    }

    public boolean canReplaceEqualItem(ItemStack itemStack0, ItemStack itemStack1) {
        if (itemStack0.getDamageValue() >= itemStack1.getDamageValue() && (!itemStack0.hasTag() || itemStack1.hasTag())) {
            return itemStack0.hasTag() && itemStack1.hasTag() ? itemStack0.getTag().getAllKeys().stream().anyMatch(p_21513_ -> !p_21513_.equals("Damage")) && !itemStack1.getTag().getAllKeys().stream().anyMatch(p_21503_ -> !p_21503_.equals("Damage")) : false;
        } else {
            return true;
        }
    }

    public boolean canHoldItem(ItemStack itemStack0) {
        return true;
    }

    public boolean wantsToPickUp(ItemStack itemStack0) {
        return this.canHoldItem(itemStack0);
    }

    public boolean removeWhenFarAway(double double0) {
        return true;
    }

    public boolean requiresCustomPersistence() {
        return this.m_20159_();
    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void checkDespawn() {
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.m_146870_();
        } else if (!this.isPersistenceRequired() && !this.requiresCustomPersistence()) {
            Entity $$0 = this.m_9236_().m_45930_(this, -1.0);
            if ($$0 != null) {
                double $$1 = $$0.distanceToSqr(this);
                int $$2 = this.m_6095_().getCategory().getDespawnDistance();
                int $$3 = $$2 * $$2;
                if ($$1 > (double) $$3 && this.removeWhenFarAway($$1)) {
                    this.m_146870_();
                }
                int $$4 = this.m_6095_().getCategory().getNoDespawnDistance();
                int $$5 = $$4 * $$4;
                if (this.f_20891_ > 600 && this.f_19796_.nextInt(800) == 0 && $$1 > (double) $$5 && this.removeWhenFarAway($$1)) {
                    this.m_146870_();
                } else if ($$1 < (double) $$5) {
                    this.f_20891_ = 0;
                }
            }
        } else {
            this.f_20891_ = 0;
        }
    }

    @Override
    protected final void serverAiStep() {
        this.f_20891_++;
        this.m_9236_().getProfiler().push("sensing");
        this.sensing.tick();
        this.m_9236_().getProfiler().pop();
        int $$0 = this.m_9236_().getServer().getTickCount() + this.m_19879_();
        if ($$0 % 2 != 0 && this.f_19797_ > 1) {
            this.m_9236_().getProfiler().push("targetSelector");
            this.targetSelector.tickRunningGoals(false);
            this.m_9236_().getProfiler().pop();
            this.m_9236_().getProfiler().push("goalSelector");
            this.goalSelector.tickRunningGoals(false);
            this.m_9236_().getProfiler().pop();
        } else {
            this.m_9236_().getProfiler().push("targetSelector");
            this.targetSelector.tick();
            this.m_9236_().getProfiler().pop();
            this.m_9236_().getProfiler().push("goalSelector");
            this.goalSelector.tick();
            this.m_9236_().getProfiler().pop();
        }
        this.m_9236_().getProfiler().push("navigation");
        this.navigation.tick();
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("mob tick");
        this.customServerAiStep();
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("controls");
        this.m_9236_().getProfiler().push("move");
        this.moveControl.tick();
        this.m_9236_().getProfiler().popPush("look");
        this.lookControl.tick();
        this.m_9236_().getProfiler().popPush("jump");
        this.jumpControl.tick();
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().pop();
        this.sendDebugPackets();
    }

    protected void sendDebugPackets() {
        DebugPackets.sendGoalSelector(this.m_9236_(), this, this.goalSelector);
    }

    protected void customServerAiStep() {
    }

    public int getMaxHeadXRot() {
        return 40;
    }

    public int getMaxHeadYRot() {
        return 75;
    }

    public int getHeadRotSpeed() {
        return 10;
    }

    public void lookAt(Entity entity0, float float1, float float2) {
        double $$3 = entity0.getX() - this.m_20185_();
        double $$4 = entity0.getZ() - this.m_20189_();
        double $$6;
        if (entity0 instanceof LivingEntity $$5) {
            $$6 = $$5.m_20188_() - this.m_20188_();
        } else {
            $$6 = (entity0.getBoundingBox().minY + entity0.getBoundingBox().maxY) / 2.0 - this.m_20188_();
        }
        double $$8 = Math.sqrt($$3 * $$3 + $$4 * $$4);
        float $$9 = (float) (Mth.atan2($$4, $$3) * 180.0F / (float) Math.PI) - 90.0F;
        float $$10 = (float) (-(Mth.atan2($$6, $$8) * 180.0F / (float) Math.PI));
        this.m_146926_(this.rotlerp(this.m_146909_(), $$10, float2));
        this.m_146922_(this.rotlerp(this.m_146908_(), $$9, float1));
    }

    private float rotlerp(float float0, float float1, float float2) {
        float $$3 = Mth.wrapDegrees(float1 - float0);
        if ($$3 > float2) {
            $$3 = float2;
        }
        if ($$3 < -float2) {
            $$3 = -float2;
        }
        return float0 + $$3;
    }

    public static boolean checkMobSpawnRules(EntityType<? extends Mob> entityTypeExtendsMob0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        BlockPos $$5 = blockPos3.below();
        return mobSpawnType2 == MobSpawnType.SPAWNER || levelAccessor1.m_8055_($$5).m_60643_(levelAccessor1, $$5, entityTypeExtendsMob0);
    }

    public boolean checkSpawnRules(LevelAccessor levelAccessor0, MobSpawnType mobSpawnType1) {
        return true;
    }

    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        return !levelReader0.containsAnyLiquid(this.m_20191_()) && levelReader0.m_45784_(this);
    }

    public int getMaxSpawnClusterSize() {
        return 4;
    }

    public boolean isMaxGroupSizeReached(int int0) {
        return false;
    }

    @Override
    public int getMaxFallDistance() {
        if (this.getTarget() == null) {
            return 3;
        } else {
            int $$0 = (int) (this.m_21223_() - this.m_21233_() * 0.33F);
            $$0 -= (3 - this.m_9236_().m_46791_().getId()) * 4;
            if ($$0 < 0) {
                $$0 = 0;
            }
            return $$0 + 3;
        }
    }

    @Override
    public Iterable<ItemStack> getHandSlots() {
        return this.handItems;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return this.armorItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot0) {
        switch(equipmentSlot0.getType()) {
            case HAND:
                return this.handItems.get(equipmentSlot0.getIndex());
            case ARMOR:
                return this.armorItems.get(equipmentSlot0.getIndex());
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot0, ItemStack itemStack1) {
        this.m_181122_(itemStack1);
        switch(equipmentSlot0.getType()) {
            case HAND:
                this.m_238392_(equipmentSlot0, this.handItems.set(equipmentSlot0.getIndex(), itemStack1), itemStack1);
                break;
            case ARMOR:
                this.m_238392_(equipmentSlot0, this.armorItems.set(equipmentSlot0.getIndex(), itemStack1), itemStack1);
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource0, int int1, boolean boolean2) {
        super.dropCustomDeathLoot(damageSource0, int1, boolean2);
        for (EquipmentSlot $$3 : EquipmentSlot.values()) {
            ItemStack $$4 = this.getItemBySlot($$3);
            float $$5 = this.getEquipmentDropChance($$3);
            boolean $$6 = $$5 > 1.0F;
            if (!$$4.isEmpty() && !EnchantmentHelper.hasVanishingCurse($$4) && (boolean2 || $$6) && Math.max(this.f_19796_.nextFloat() - (float) int1 * 0.01F, 0.0F) < $$5) {
                if (!$$6 && $$4.isDamageableItem()) {
                    $$4.setDamageValue($$4.getMaxDamage() - this.f_19796_.nextInt(1 + this.f_19796_.nextInt(Math.max($$4.getMaxDamage() - 3, 1))));
                }
                this.m_19983_($$4);
                this.setItemSlot($$3, ItemStack.EMPTY);
            }
        }
    }

    protected float getEquipmentDropChance(EquipmentSlot equipmentSlot0) {
        return switch(equipmentSlot0.getType()) {
            case HAND ->
                this.handDropChances[equipmentSlot0.getIndex()];
            case ARMOR ->
                this.armorDropChances[equipmentSlot0.getIndex()];
            default ->
                0.0F;
        };
    }

    protected void populateDefaultEquipmentSlots(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
        if (randomSource0.nextFloat() < 0.15F * difficultyInstance1.getSpecialMultiplier()) {
            int $$2 = randomSource0.nextInt(2);
            float $$3 = this.m_9236_().m_46791_() == Difficulty.HARD ? 0.1F : 0.25F;
            if (randomSource0.nextFloat() < 0.095F) {
                $$2++;
            }
            if (randomSource0.nextFloat() < 0.095F) {
                $$2++;
            }
            if (randomSource0.nextFloat() < 0.095F) {
                $$2++;
            }
            boolean $$4 = true;
            for (EquipmentSlot $$5 : EquipmentSlot.values()) {
                if ($$5.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack $$6 = this.getItemBySlot($$5);
                    if (!$$4 && randomSource0.nextFloat() < $$3) {
                        break;
                    }
                    $$4 = false;
                    if ($$6.isEmpty()) {
                        Item $$7 = getEquipmentForSlot($$5, $$2);
                        if ($$7 != null) {
                            this.setItemSlot($$5, new ItemStack($$7));
                        }
                    }
                }
            }
        }
    }

    @Nullable
    public static Item getEquipmentForSlot(EquipmentSlot equipmentSlot0, int int1) {
        switch(equipmentSlot0) {
            case HEAD:
                if (int1 == 0) {
                    return Items.LEATHER_HELMET;
                } else if (int1 == 1) {
                    return Items.GOLDEN_HELMET;
                } else if (int1 == 2) {
                    return Items.CHAINMAIL_HELMET;
                } else if (int1 == 3) {
                    return Items.IRON_HELMET;
                } else if (int1 == 4) {
                    return Items.DIAMOND_HELMET;
                }
            case CHEST:
                if (int1 == 0) {
                    return Items.LEATHER_CHESTPLATE;
                } else if (int1 == 1) {
                    return Items.GOLDEN_CHESTPLATE;
                } else if (int1 == 2) {
                    return Items.CHAINMAIL_CHESTPLATE;
                } else if (int1 == 3) {
                    return Items.IRON_CHESTPLATE;
                } else if (int1 == 4) {
                    return Items.DIAMOND_CHESTPLATE;
                }
            case LEGS:
                if (int1 == 0) {
                    return Items.LEATHER_LEGGINGS;
                } else if (int1 == 1) {
                    return Items.GOLDEN_LEGGINGS;
                } else if (int1 == 2) {
                    return Items.CHAINMAIL_LEGGINGS;
                } else if (int1 == 3) {
                    return Items.IRON_LEGGINGS;
                } else if (int1 == 4) {
                    return Items.DIAMOND_LEGGINGS;
                }
            case FEET:
                if (int1 == 0) {
                    return Items.LEATHER_BOOTS;
                } else if (int1 == 1) {
                    return Items.GOLDEN_BOOTS;
                } else if (int1 == 2) {
                    return Items.CHAINMAIL_BOOTS;
                } else if (int1 == 3) {
                    return Items.IRON_BOOTS;
                } else if (int1 == 4) {
                    return Items.DIAMOND_BOOTS;
                }
            default:
                return null;
        }
    }

    protected void populateDefaultEquipmentEnchantments(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
        float $$2 = difficultyInstance1.getSpecialMultiplier();
        this.enchantSpawnedWeapon(randomSource0, $$2);
        for (EquipmentSlot $$3 : EquipmentSlot.values()) {
            if ($$3.getType() == EquipmentSlot.Type.ARMOR) {
                this.enchantSpawnedArmor(randomSource0, $$2, $$3);
            }
        }
    }

    protected void enchantSpawnedWeapon(RandomSource randomSource0, float float1) {
        if (!this.m_21205_().isEmpty() && randomSource0.nextFloat() < 0.25F * float1) {
            this.setItemSlot(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem(randomSource0, this.m_21205_(), (int) (5.0F + float1 * (float) randomSource0.nextInt(18)), false));
        }
    }

    protected void enchantSpawnedArmor(RandomSource randomSource0, float float1, EquipmentSlot equipmentSlot2) {
        ItemStack $$3 = this.getItemBySlot(equipmentSlot2);
        if (!$$3.isEmpty() && randomSource0.nextFloat() < 0.5F * float1) {
            this.setItemSlot(equipmentSlot2, EnchantmentHelper.enchantItem(randomSource0, $$3, (int) (5.0F + float1 * (float) randomSource0.nextInt(18)), false));
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        RandomSource $$5 = serverLevelAccessor0.m_213780_();
        this.m_21051_(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random spawn bonus", $$5.triangle(0.0, 0.11485000000000001), AttributeModifier.Operation.MULTIPLY_BASE));
        if ($$5.nextFloat() < 0.05F) {
            this.setLeftHanded(true);
        } else {
            this.setLeftHanded(false);
        }
        return spawnGroupData3;
    }

    public void setPersistenceRequired() {
        this.persistenceRequired = true;
    }

    public void setDropChance(EquipmentSlot equipmentSlot0, float float1) {
        switch(equipmentSlot0.getType()) {
            case HAND:
                this.handDropChances[equipmentSlot0.getIndex()] = float1;
                break;
            case ARMOR:
                this.armorDropChances[equipmentSlot0.getIndex()] = float1;
        }
    }

    public boolean canPickUpLoot() {
        return this.canPickUpLoot;
    }

    public void setCanPickUpLoot(boolean boolean0) {
        this.canPickUpLoot = boolean0;
    }

    @Override
    public boolean canTakeItem(ItemStack itemStack0) {
        EquipmentSlot $$1 = m_147233_(itemStack0);
        return this.getItemBySlot($$1).isEmpty() && this.canPickUpLoot();
    }

    public boolean isPersistenceRequired() {
        return this.persistenceRequired;
    }

    @Override
    public final InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        if (!this.m_6084_()) {
            return InteractionResult.PASS;
        } else if (this.getLeashHolder() == player0) {
            this.dropLeash(true, !player0.getAbilities().instabuild);
            this.m_146852_(GameEvent.ENTITY_INTERACT, player0);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            InteractionResult $$2 = this.checkAndHandleImportantInteractions(player0, interactionHand1);
            if ($$2.consumesAction()) {
                this.m_146852_(GameEvent.ENTITY_INTERACT, player0);
                return $$2;
            } else {
                $$2 = this.mobInteract(player0, interactionHand1);
                if ($$2.consumesAction()) {
                    this.m_146852_(GameEvent.ENTITY_INTERACT, player0);
                    return $$2;
                } else {
                    return super.m_6096_(player0, interactionHand1);
                }
            }
        }
    }

    private InteractionResult checkAndHandleImportantInteractions(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if ($$2.is(Items.LEAD) && this.canBeLeashed(player0)) {
            this.setLeashedTo(player0, true);
            $$2.shrink(1);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            if ($$2.is(Items.NAME_TAG)) {
                InteractionResult $$3 = $$2.interactLivingEntity(player0, this, interactionHand1);
                if ($$3.consumesAction()) {
                    return $$3;
                }
            }
            if ($$2.getItem() instanceof SpawnEggItem) {
                if (this.m_9236_() instanceof ServerLevel) {
                    SpawnEggItem $$4 = (SpawnEggItem) $$2.getItem();
                    Optional<Mob> $$5 = $$4.spawnOffspringFromSpawnEgg(player0, this, this.m_6095_(), (ServerLevel) this.m_9236_(), this.m_20182_(), $$2);
                    $$5.ifPresent(p_21476_ -> this.onOffspringSpawnedFromEgg(player0, p_21476_));
                    return $$5.isPresent() ? InteractionResult.SUCCESS : InteractionResult.PASS;
                } else {
                    return InteractionResult.CONSUME;
                }
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    protected void onOffspringSpawnedFromEgg(Player player0, Mob mob1) {
    }

    protected InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        return InteractionResult.PASS;
    }

    public boolean isWithinRestriction() {
        return this.isWithinRestriction(this.m_20183_());
    }

    public boolean isWithinRestriction(BlockPos blockPos0) {
        return this.restrictRadius == -1.0F ? true : this.restrictCenter.m_123331_(blockPos0) < (double) (this.restrictRadius * this.restrictRadius);
    }

    public void restrictTo(BlockPos blockPos0, int int1) {
        this.restrictCenter = blockPos0;
        this.restrictRadius = (float) int1;
    }

    public BlockPos getRestrictCenter() {
        return this.restrictCenter;
    }

    public float getRestrictRadius() {
        return this.restrictRadius;
    }

    public void clearRestriction() {
        this.restrictRadius = -1.0F;
    }

    public boolean hasRestriction() {
        return this.restrictRadius != -1.0F;
    }

    @Nullable
    public <T extends Mob> T convertTo(EntityType<T> entityTypeT0, boolean boolean1) {
        if (this.m_213877_()) {
            return null;
        } else {
            T $$2 = (T) entityTypeT0.create(this.m_9236_());
            if ($$2 == null) {
                return null;
            } else {
                $$2.m_20359_(this);
                $$2.setBaby(this.m_6162_());
                $$2.setNoAi(this.isNoAi());
                if (this.m_8077_()) {
                    $$2.m_6593_(this.m_7770_());
                    $$2.m_20340_(this.m_20151_());
                }
                if (this.isPersistenceRequired()) {
                    $$2.setPersistenceRequired();
                }
                $$2.m_20331_(this.m_20147_());
                if (boolean1) {
                    $$2.setCanPickUpLoot(this.canPickUpLoot());
                    for (EquipmentSlot $$3 : EquipmentSlot.values()) {
                        ItemStack $$4 = this.getItemBySlot($$3);
                        if (!$$4.isEmpty()) {
                            $$2.setItemSlot($$3, $$4.copyAndClear());
                            $$2.setDropChance($$3, this.getEquipmentDropChance($$3));
                        }
                    }
                }
                this.m_9236_().m_7967_($$2);
                if (this.m_20159_()) {
                    Entity $$5 = this.m_20202_();
                    this.m_8127_();
                    $$2.startRiding($$5, true);
                }
                this.m_146870_();
                return $$2;
            }
        }
    }

    protected void tickLeash() {
        if (this.leashInfoTag != null) {
            this.restoreLeashFromSave();
        }
        if (this.leashHolder != null) {
            if (!this.m_6084_() || !this.leashHolder.isAlive()) {
                this.dropLeash(true, true);
            }
        }
    }

    public void dropLeash(boolean boolean0, boolean boolean1) {
        if (this.leashHolder != null) {
            this.leashHolder = null;
            this.leashInfoTag = null;
            if (!this.m_9236_().isClientSide && boolean1) {
                this.m_19998_(Items.LEAD);
            }
            if (!this.m_9236_().isClientSide && boolean0 && this.m_9236_() instanceof ServerLevel) {
                ((ServerLevel) this.m_9236_()).getChunkSource().broadcast(this, new ClientboundSetEntityLinkPacket(this, null));
            }
        }
    }

    public boolean canBeLeashed(Player player0) {
        return !this.isLeashed() && !(this instanceof Enemy);
    }

    public boolean isLeashed() {
        return this.leashHolder != null;
    }

    @Nullable
    public Entity getLeashHolder() {
        if (this.leashHolder == null && this.delayedLeashHolderId != 0 && this.m_9236_().isClientSide) {
            this.leashHolder = this.m_9236_().getEntity(this.delayedLeashHolderId);
        }
        return this.leashHolder;
    }

    public void setLeashedTo(Entity entity0, boolean boolean1) {
        this.leashHolder = entity0;
        this.leashInfoTag = null;
        if (!this.m_9236_().isClientSide && boolean1 && this.m_9236_() instanceof ServerLevel) {
            ((ServerLevel) this.m_9236_()).getChunkSource().broadcast(this, new ClientboundSetEntityLinkPacket(this, this.leashHolder));
        }
        if (this.m_20159_()) {
            this.m_8127_();
        }
    }

    public void setDelayedLeashHolderId(int int0) {
        this.delayedLeashHolderId = int0;
        this.dropLeash(false, false);
    }

    @Override
    public boolean startRiding(Entity entity0, boolean boolean1) {
        boolean $$2 = super.m_7998_(entity0, boolean1);
        if ($$2 && this.isLeashed()) {
            this.dropLeash(true, true);
        }
        return $$2;
    }

    private void restoreLeashFromSave() {
        if (this.leashInfoTag != null && this.m_9236_() instanceof ServerLevel) {
            if (this.leashInfoTag.hasUUID("UUID")) {
                UUID $$0 = this.leashInfoTag.getUUID("UUID");
                Entity $$1 = ((ServerLevel) this.m_9236_()).getEntity($$0);
                if ($$1 != null) {
                    this.setLeashedTo($$1, true);
                    return;
                }
            } else if (this.leashInfoTag.contains("X", 99) && this.leashInfoTag.contains("Y", 99) && this.leashInfoTag.contains("Z", 99)) {
                BlockPos $$2 = NbtUtils.readBlockPos(this.leashInfoTag);
                this.setLeashedTo(LeashFenceKnotEntity.getOrCreateKnot(this.m_9236_(), $$2), true);
                return;
            }
            if (this.f_19797_ > 100) {
                this.m_19998_(Items.LEAD);
                this.leashInfoTag = null;
            }
        }
    }

    @Override
    public boolean isEffectiveAi() {
        return super.m_21515_() && !this.isNoAi();
    }

    public void setNoAi(boolean boolean0) {
        byte $$1 = this.f_19804_.get(DATA_MOB_FLAGS_ID);
        this.f_19804_.set(DATA_MOB_FLAGS_ID, boolean0 ? (byte) ($$1 | 1) : (byte) ($$1 & -2));
    }

    public void setLeftHanded(boolean boolean0) {
        byte $$1 = this.f_19804_.get(DATA_MOB_FLAGS_ID);
        this.f_19804_.set(DATA_MOB_FLAGS_ID, boolean0 ? (byte) ($$1 | 2) : (byte) ($$1 & -3));
    }

    public void setAggressive(boolean boolean0) {
        byte $$1 = this.f_19804_.get(DATA_MOB_FLAGS_ID);
        this.f_19804_.set(DATA_MOB_FLAGS_ID, boolean0 ? (byte) ($$1 | 4) : (byte) ($$1 & -5));
    }

    public boolean isNoAi() {
        return (this.f_19804_.get(DATA_MOB_FLAGS_ID) & 1) != 0;
    }

    public boolean isLeftHanded() {
        return (this.f_19804_.get(DATA_MOB_FLAGS_ID) & 2) != 0;
    }

    public boolean isAggressive() {
        return (this.f_19804_.get(DATA_MOB_FLAGS_ID) & 4) != 0;
    }

    public void setBaby(boolean boolean0) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return this.isLeftHanded() ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }

    public double getMeleeAttackRangeSqr(LivingEntity livingEntity0) {
        return (double) (this.m_20205_() * 2.0F * this.m_20205_() * 2.0F + livingEntity0.m_20205_());
    }

    public double getPerceivedTargetDistanceSquareForMeleeAttack(LivingEntity livingEntity0) {
        return Math.max(this.m_20238_(livingEntity0.getMeleeAttackReferencePosition()), this.m_20238_(livingEntity0.m_20182_()));
    }

    public boolean isWithinMeleeAttackRange(LivingEntity livingEntity0) {
        double $$1 = this.getPerceivedTargetDistanceSquareForMeleeAttack(livingEntity0);
        return $$1 <= this.getMeleeAttackRangeSqr(livingEntity0);
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        float $$1 = (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
        float $$2 = (float) this.m_21133_(Attributes.ATTACK_KNOCKBACK);
        if (entity0 instanceof LivingEntity) {
            $$1 += EnchantmentHelper.getDamageBonus(this.m_21205_(), ((LivingEntity) entity0).getMobType());
            $$2 += (float) EnchantmentHelper.getKnockbackBonus(this);
        }
        int $$3 = EnchantmentHelper.getFireAspect(this);
        if ($$3 > 0) {
            entity0.setSecondsOnFire($$3 * 4);
        }
        boolean $$4 = entity0.hurt(this.m_269291_().mobAttack(this), $$1);
        if ($$4) {
            if ($$2 > 0.0F && entity0 instanceof LivingEntity) {
                ((LivingEntity) entity0).knockback((double) ($$2 * 0.5F), (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                this.m_20256_(this.m_20184_().multiply(0.6, 1.0, 0.6));
            }
            if (entity0 instanceof Player $$5) {
                this.maybeDisableShield($$5, this.m_21205_(), $$5.m_6117_() ? $$5.m_21211_() : ItemStack.EMPTY);
            }
            this.m_19970_(this, entity0);
            this.m_21335_(entity0);
        }
        return $$4;
    }

    private void maybeDisableShield(Player player0, ItemStack itemStack1, ItemStack itemStack2) {
        if (!itemStack1.isEmpty() && !itemStack2.isEmpty() && itemStack1.getItem() instanceof AxeItem && itemStack2.is(Items.SHIELD)) {
            float $$3 = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.f_19796_.nextFloat() < $$3) {
                player0.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.m_9236_().broadcastEntityEvent(player0, (byte) 30);
            }
        }
    }

    protected boolean isSunBurnTick() {
        if (this.m_9236_().isDay() && !this.m_9236_().isClientSide) {
            float $$0 = this.m_213856_();
            BlockPos $$1 = BlockPos.containing(this.m_20185_(), this.m_20188_(), this.m_20189_());
            boolean $$2 = this.m_20071_() || this.f_146808_ || this.f_146809_;
            if ($$0 > 0.5F && this.f_19796_.nextFloat() * 30.0F < ($$0 - 0.4F) * 2.0F && !$$2 && this.m_9236_().m_45527_($$1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> tagKeyFluid0) {
        if (this.getNavigation().canFloat()) {
            super.jumpInLiquid(tagKeyFluid0);
        } else {
            this.m_20256_(this.m_20184_().add(0.0, 0.3, 0.0));
        }
    }

    public void removeFreeWill() {
        this.removeAllGoals(p_262562_ -> true);
        this.m_6274_().removeAllBehaviors();
    }

    public void removeAllGoals(Predicate<Goal> predicateGoal0) {
        this.goalSelector.removeAllGoals(predicateGoal0);
    }

    @Override
    protected void removeAfterChangingDimensions() {
        super.m_6089_();
        this.dropLeash(true, false);
        this.m_20158_().forEach(p_278936_ -> {
            if (!p_278936_.isEmpty()) {
                p_278936_.setCount(0);
            }
        });
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        SpawnEggItem $$0 = SpawnEggItem.byId(this.m_6095_());
        return $$0 == null ? null : new ItemStack($$0);
    }
}