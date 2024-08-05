package dev.xkmc.modulargolems.content.entity.common;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.capability.GolemConfigStorage;
import dev.xkmc.modulargolems.content.capability.PathConfig;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.goals.FollowOwnerGoal;
import dev.xkmc.modulargolems.content.entity.goals.GolemFloatGoal;
import dev.xkmc.modulargolems.content.entity.goals.GolemMeleeGoal;
import dev.xkmc.modulargolems.content.entity.goals.GolemRandomStrollGoal;
import dev.xkmc.modulargolems.content.entity.goals.GolemSwimMoveControl;
import dev.xkmc.modulargolems.content.entity.goals.TeleportToOwnerGoal;
import dev.xkmc.modulargolems.content.entity.humanoid.ItemWrapper;
import dev.xkmc.modulargolems.content.entity.mode.GolemMode;
import dev.xkmc.modulargolems.content.entity.mode.GolemModes;
import dev.xkmc.modulargolems.content.entity.sync.SyncedData;
import dev.xkmc.modulargolems.content.item.card.DefaultFilterCard;
import dev.xkmc.modulargolems.content.item.card.PathRecordCard;
import dev.xkmc.modulargolems.content.item.equipments.GolemEquipmentItem;
import dev.xkmc.modulargolems.content.item.equipments.TickEquipmentItem;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

@SerialClass
public class AbstractGolemEntity<T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> extends AbstractGolem implements IEntityAdditionalSpawnData, NeutralMob, OwnableEntity, PowerableMob {

    private static final SyncedData GOLEM_DATA = new SyncedData(AbstractGolemEntity::defineId);

    @SerialField(toClient = true)
    private ArrayList<GolemMaterial> materials = new ArrayList();

    @SerialField(toClient = true)
    private ArrayList<Item> upgrades = new ArrayList();

    @SerialField(toClient = true)
    @Nullable
    private UUID owner;

    @SerialField(toClient = true)
    private HashMap<GolemModifier, Integer> modifiers = new LinkedHashMap();

    @SerialField(toClient = true)
    private final HashSet<GolemFlags> golemFlags = new HashSet();

    @SerialField
    private Vec3 recordedPosition = Vec3.ZERO;

    @SerialField
    private BlockPos recordedGuardPos = BlockPos.ZERO;

    public int inventoryTick = 0;

    protected final PathNavigation waterNavigation;

    protected final GroundPathNavigation groundNavigation;

    private static final EntityDataAccessor<Integer> DATA_MODE = GOLEM_DATA.define(SyncedData.INT, 0, "follow_mode");

    private static final EntityDataAccessor<BlockPos> GUARD_POS = GOLEM_DATA.define(SyncedData.BLOCK_POS, BlockPos.ZERO, "guard_pos");

    private static final EntityDataAccessor<Optional<UUID>> CONFIG_ID = GOLEM_DATA.define(SyncedData.UUID, Optional.empty(), "config_owner");

    private static final EntityDataAccessor<Integer> CONFIG_COLOR = GOLEM_DATA.define(SyncedData.INT, 0, "config_color");

    private static final EntityDataAccessor<Integer> PATROL_STAGE = GOLEM_DATA.define(SyncedData.INT, 0, "patrol_stage");

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = GOLEM_DATA.define(SyncedData.INT, 0, null);

    @Nullable
    private UUID persistentAngerTarget;

    private static <T> EntityDataAccessor<T> defineId(EntityDataSerializer<T> ser) {
        return SynchedEntityData.defineId(AbstractGolemEntity.class, ser);
    }

    protected AbstractGolemEntity(EntityType<T> type, Level level) {
        super(type, level);
        this.waterNavigation = new AmphibiousPathNavigation(this, level);
        this.groundNavigation = new GroundPathNavigation(this, level);
    }

    public void onCreate(ArrayList<GolemMaterial> materials, ArrayList<UpgradeItem> upgrades, @Nullable UUID owner) {
        this.updateAttributes(materials, upgrades, owner);
        this.m_21153_(this.m_21233_());
    }

    public void updateAttributes(ArrayList<GolemMaterial> materials, ArrayList<UpgradeItem> upgrades, @Nullable UUID owner) {
        this.materials = materials;
        this.upgrades = (ArrayList<Item>) Wrappers.cast(upgrades);
        this.owner = owner;
        this.modifiers = GolemMaterial.collectModifiers(materials, upgrades);
        this.golemFlags.clear();
        this.m_274367_(1.0F);
        this.getModifiers().forEach((m, i) -> m.onRegisterFlag(this.golemFlags::add));
        if (this.canSwim()) {
            this.f_21342_ = new GolemSwimMoveControl(this);
            this.f_21344_ = this.waterNavigation;
            this.m_21441_(BlockPathTypes.WATER, 0.0F);
            this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        }
        if (!this.m_9236_().isClientSide()) {
            this.getModifiers().forEach((m, i) -> m.onRegisterGoals(this, i, this.f_21345_::m_25352_));
        }
        GolemMaterial.addAttributes(materials, upgrades, this.getThis());
        this.m_6210_();
    }

    @Override
    public EntityType<T> getType() {
        return (EntityType<T>) Wrappers.cast(super.m_6095_());
    }

    public ArrayList<GolemMaterial> getMaterials() {
        return this.materials;
    }

    public ArrayList<Item> getUpgrades() {
        return this.upgrades;
    }

    public HashMap<GolemModifier, Integer> getModifiers() {
        return this.modifiers;
    }

    public boolean hasFlag(GolemFlags flag) {
        return this.golemFlags.contains(flag);
    }

    @Override
    protected final InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.m_21120_(hand).is(MGTagGen.GOLEM_INTERACT)) {
            return InteractionResult.PASS;
        } else {
            for (Entry<GolemModifier, Integer> ent : this.modifiers.entrySet()) {
                InteractionResult result = ((GolemModifier) ent.getKey()).interact(player, this, hand);
                if (result != InteractionResult.PASS) {
                    return result;
                }
            }
            return this.mobInteractImpl(player, hand);
        }
    }

    protected InteractionResult mobInteractImpl(Player player, InteractionHand hand) {
        if (!MGConfig.COMMON.barehandRetrieve.get() || !this.canModify(player)) {
            return InteractionResult.FAIL;
        } else if (player.m_21205_().isEmpty()) {
            if (!this.m_9236_().isClientSide()) {
                this.m_19877_();
                player.setItemSlot(EquipmentSlot.MAINHAND, this.toItem());
            }
            return InteractionResult.SUCCESS;
        } else {
            ItemStack stack = player.m_21120_(hand);
            if (stack.getItem() instanceof GolemEquipmentItem item && item.isFor(this.getType()) && this.m_6844_(item.getSlot()).isEmpty()) {
                if (!this.m_9236_().isClientSide()) {
                    this.m_8061_(item.getSlot(), stack.split(1));
                }
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }
    }

    public ItemStack toItem() {
        this.recordedPosition = this.m_20182_();
        this.recordedGuardPos = this.getGuardPos();
        ItemStack ans = GolemHolder.setEntity(this.getThis());
        this.m_9236_().broadcastEntityEvent(this, (byte) 60);
        this.m_146870_();
        return ans;
    }

    @Override
    public boolean fireImmune() {
        return this.hasFlag(GolemFlags.FIRE_IMMUNE);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float damage) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            damage *= 1000.0F;
        }
        super.m_6475_(source, damage);
        if (this.m_21223_() <= 0.0F && this.hasFlag(GolemFlags.RECYCLE)) {
            Player player = this.getOwner();
            this.m_19877_();
            ItemStack stack = GolemHolder.setEntity(this.getThis());
            if (player != null && player.m_6084_()) {
                player.getInventory().placeItemBackInInventory(stack);
            } else {
                this.m_19983_(stack);
            }
            this.m_9236_().broadcastEntityEvent(this, (byte) 60);
            this.m_146870_();
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int i, boolean b) {
        Map<Item, Integer> drop = new HashMap();
        for (GolemMaterial mat : this.getMaterials()) {
            Item item = ((Ingredient) GolemMaterialConfig.get().ingredients.get(mat.id())).getItems()[0].getItem();
            drop.compute(item, (e, old) -> (old == null ? 0 : old) + 1);
        }
        drop.forEach((k, v) -> this.m_19983_(new ItemStack(k, v)));
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.dropSlot(slot, true);
        }
    }

    protected void dropSlot(EquipmentSlot slot, boolean isDeath) {
        ItemStack itemstack = this.m_6844_(slot);
        if (!itemstack.isEmpty()) {
            if (isDeath || !EnchantmentHelper.hasBindingCurse(itemstack)) {
                if (!isDeath || !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                    this.m_19983_(itemstack);
                    this.m_8061_(slot, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public float getScale() {
        return this.materials != null && !this.materials.isEmpty() && !this.m_19880_().contains("ClientOnly") ? (float) (this.m_21133_((Attribute) GolemTypes.GOLEM_SIZE.get()) / DefaultAttributes.getSupplier(this.getType()).getValue((Attribute) GolemTypes.GOLEM_SIZE.get())) : 1.0F;
    }

    public boolean canSwim() {
        return this.hasFlag(GolemFlags.SWIM);
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (!this.getMode().isMovable()) {
            pTravelVector = Vec3.ZERO;
        }
        if ((this.m_6109_() || this.m_21515_()) && this.m_20069_() && this.canSwim()) {
            this.m_19920_(0.08F, pTravelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_6109_()) {
                super.m_7023_(pTravelVector);
            }
        } else {
            super.m_7023_(pTravelVector);
        }
    }

    @Override
    public void updateSwimming() {
        if (!this.m_9236_().isClientSide) {
            this.m_20282_(this.m_21515_() && this.m_20069_() && this.canSwim());
        }
    }

    @Override
    public boolean isPushable() {
        return this.getMode().isMovable();
    }

    @Override
    public boolean isPushedByFluid() {
        return !this.m_6069_() && this.getMode().isMovable();
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.owner;
    }

    @Nullable
    public Player getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.m_9236_().m_46003_(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.m_7380_(tag);
        this.m_21678_(tag);
        tag.put("auto-serial", (Tag) Objects.requireNonNull(TagCodec.toTag(new CompoundTag(), this)));
        GOLEM_DATA.write(tag, this.f_19804_);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.m_7378_(tag);
        this.m_147285_(this.m_9236_(), tag);
        if (tag.contains("auto-serial")) {
            Wrappers.run(() -> TagCodec.fromTag(tag.getCompound("auto-serial"), this.getClass(), this, f -> true));
        }
        this.updateAttributes(this.materials, (ArrayList<UpgradeItem>) Wrappers.cast(this.getUpgrades()), this.owner);
        GOLEM_DATA.read(tag, this.f_19804_);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        PacketCodec.to(buffer, this);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf data) {
        PacketCodec.from(data, (Class) Wrappers.cast(this.getClass()), this.getThis());
        this.updateAttributes(this.materials, (ArrayList<UpgradeItem>) Wrappers.cast(this.upgrades), this.owner);
    }

    public T getThis() {
        return (T) Wrappers.cast(this);
    }

    @Override
    public boolean hasLineOfSight(Entity target) {
        if (target.level() == this.m_9236_() && this.hasFlag(GolemFlags.SEE_THROUGH)) {
            Vec3 self = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
            Vec3 tarp = new Vec3(target.getX(), target.getEyeY(), target.getZ());
            double dist = tarp.distanceTo(self);
            if (dist <= 128.0) {
                if (target.level().m_45527_(target.blockPosition())) {
                    return true;
                }
                if (dist < 5.0) {
                    return true;
                }
                if (self.y() < tarp.y()) {
                    return true;
                }
            }
        }
        return super.m_142582_(target);
    }

    @Override
    public boolean canFreeze() {
        return !this.hasFlag(GolemFlags.FREEZE_IMMUNE);
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return !this.hasFlag(GolemFlags.PASSIVE) && super.m_142066_();
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target == null || this.canAttack(target)) {
            super.m_6710_(target);
            if (target instanceof Mob mob) {
                if (mob.getTarget() == null && mob.m_6779_(this)) {
                    mob.setTarget(this);
                }
                for (Entry<GolemModifier, Integer> entry : this.getModifiers().entrySet()) {
                    ((GolemModifier) entry.getKey()).onSetTarget(this, mob, (Integer) entry.getValue());
                }
            }
        }
    }

    @Override
    public boolean canAttackType(EntityType<?> type) {
        return !this.hasFlag(GolemFlags.PASSIVE);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target == this.getOwner()) {
            return false;
        } else {
            if (target instanceof OwnableEntity own && this.getOwner() == own.getOwner()) {
                return false;
            }
            GolemConfigEntry config = this.getConfigEntry(null);
            if (config == null) {
                if (target.m_6095_().is(MGTagGen.GOLEM_FRIENDLY)) {
                    return false;
                }
            } else if (config.targetFilter.friendlyToward(target)) {
                return false;
            }
            return !this.isAlliedTo(target) && this.canAttackType(target.m_6095_()) && super.m_6779_(target);
        }
    }

    protected float getAttackDamage() {
        return (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.inventoryTick > 0) {
            this.inventoryTick--;
        }
        if (this.m_9236_().isClientSide) {
            for (Entry<GolemModifier, Integer> entry : this.getModifiers().entrySet()) {
                ((GolemModifier) entry.getKey()).onClientTick(this, (Integer) entry.getValue());
            }
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = this.m_6844_(slot);
            if (stack.getItem() instanceof TickEquipmentItem tickItem) {
                tickItem.tick(stack, this.m_9236_(), this);
            }
        }
    }

    @Override
    public void aiStep() {
        this.m_21203_();
        super.m_8107_();
        if (!this.m_9236_().isClientSide) {
            if (this.f_19797_ % 20 == 0) {
                double heal = this.m_21133_((Attribute) GolemTypes.GOLEM_REGEN.get());
                for (Entry<GolemModifier, Integer> entry : this.getModifiers().entrySet()) {
                    heal = ((GolemModifier) entry.getKey()).onHealTick(heal, this, (Integer) entry.getValue());
                }
                if (heal > 0.0) {
                    this.m_5634_((float) heal);
                }
            }
            for (Entry<GolemModifier, Integer> entry : this.getModifiers().entrySet()) {
                ((GolemModifier) entry.getKey()).onAiStep(this, (Integer) entry.getValue());
            }
            this.m_21666_((ServerLevel) this.m_9236_(), true);
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = this.m_6844_(slot);
            if (!stack.isEmpty()) {
                stack.inventoryTick(this.m_9236_(), this, slot.ordinal(), slot == EquipmentSlot.MAINHAND);
            }
        }
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity target) {
        Player player = this.getOwner();
        if (player != null) {
            GolemTriggers.KILL.trigger((ServerPlayer) player, target);
        }
        return super.m_214076_(level, target);
    }

    @Override
    public void handleEntityEvent(byte event) {
        for (Entry<GolemModifier, Integer> e : this.modifiers.entrySet()) {
            ((GolemModifier) e.getKey()).handleEvent(this, (Integer) e.getValue(), event);
        }
        super.m_7822_(event);
    }

    public GolemMode getMode() {
        return GolemModes.get(this.f_19804_.get(DATA_MODE));
    }

    public BlockPos getGuardPos() {
        return this.f_19804_.get(GUARD_POS);
    }

    public void setMode(int mode, BlockPos pos) {
        this.f_19804_.set(DATA_MODE, mode);
        this.f_19804_.set(GUARD_POS, pos);
    }

    public boolean initMode(@Nullable Player player) {
        GolemConfigEntry config = this.getConfigEntry(null);
        int mode = config == null ? 0 : config.defaultMode;
        boolean far = config != null && config.summonToPosition && mode != 0 && this.recordedPosition.lengthSqr() > 0.0;
        BlockPos guard = far && !this.recordedGuardPos.equals(BlockPos.ZERO) ? this.recordedGuardPos : this.m_20183_();
        Vec3 pos = far ? this.recordedPosition : this.m_20182_();
        boolean succeed = this.m_9236_().isLoaded(BlockPos.containing(pos)) && pos.distanceTo(this.m_20182_()) < (double) MGConfig.COMMON.summonDistance.get().intValue();
        if (!succeed) {
            if (player instanceof ServerPlayer sp) {
                sp.sendSystemMessage(MGLangData.SUMMON_FAILED.get(this.m_5446_()));
            }
            return false;
        } else {
            if (far && player instanceof ServerPlayer sp) {
                sp.sendSystemMessage(MGLangData.SUMMON_FAR.get(this.m_5446_(), (int) pos.x(), (int) pos.y(), (int) pos.z()));
            }
            this.setMode(mode, mode == 0 ? BlockPos.ZERO : guard);
            this.m_20219_(pos);
            return true;
        }
    }

    @Override
    public boolean canChangeDimensions() {
        return this.getMode().canChangeDimensions() && super.m_6072_();
    }

    @Nullable
    public GolemConfigEntry getConfigEntry(@Nullable Component dummy) {
        UUID configOwner = (UUID) this.f_19804_.get(CONFIG_ID).orElse(null);
        int configColor = this.f_19804_.get(CONFIG_COLOR);
        if (configColor >= 0 && configOwner != null) {
            GolemConfigStorage storage = GolemConfigStorage.get(this.m_9236_());
            return dummy == null ? storage.getStorage(configOwner, configColor) : storage.getOrCreateStorage(configOwner, configColor, dummy);
        } else {
            return null;
        }
    }

    public void setConfigCard(@Nullable UUID owner, int color) {
        this.f_19804_.set(CONFIG_ID, Optional.ofNullable(owner));
        this.f_19804_.set(CONFIG_COLOR, color);
    }

    public void setPatrolStage(int stage) {
        this.f_19804_.set(PATROL_STAGE, stage);
    }

    public int getPatrolStage() {
        return this.f_19804_.get(PATROL_STAGE);
    }

    public void advancePatrolStage() {
        List<PathRecordCard.Pos> list = PathConfig.getPath(this);
        if (list != null) {
            int stage = this.getPatrolStage();
            if (++stage >= list.size()) {
                stage = 0;
            }
            this.setPatrolStage(stage);
        }
    }

    public List<PathRecordCard.Pos> getPatrolList() {
        List<PathRecordCard.Pos> list = PathConfig.getPath(this);
        if (list == null) {
            return List.of();
        } else {
            int stage = this.getPatrolStage();
            if (stage > 0 && stage < list.size()) {
                List<PathRecordCard.Pos> first = list.subList(stage, list.size());
                List<PathRecordCard.Pos> second = list.subList(0, stage);
                ArrayList<PathRecordCard.Pos> ans = new ArrayList(first);
                ans.addAll(second);
                return ans;
            } else {
                return list;
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        GOLEM_DATA.register(this.f_19804_);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.f_19796_));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.f_19804_.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int pTime) {
        this.f_19804_.set(DATA_REMAINING_ANGER_TIME, pTime);
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public Team getTeam() {
        LivingEntity owner = this.getOwner();
        return owner != null ? owner.m_5647_() : super.m_5647_();
    }

    public boolean canModify(Player player) {
        GolemConfigEntry entry = this.getConfigEntry(null);
        if (entry != null && entry.locked) {
            return false;
        } else {
            LivingEntity owner = this.getOwner();
            if (player == owner) {
                return true;
            } else if (!player.getAbilities().instabuild && (this.getOwnerUUID() != null || this.predicateSecondaryTarget(player))) {
                return MGConfig.COMMON.ownerPickupOnly.get() ? false : this.isAlliedTo(player);
            } else {
                return true;
            }
        }
    }

    @Override
    public boolean isAlliedTo(Entity other) {
        if (other == this) {
            return true;
        } else {
            LivingEntity owner = this.getOwner();
            if (other == owner) {
                return true;
            } else {
                return owner == null ? super.m_7307_(other) : owner.m_7307_(other) || other.isAlliedTo(owner);
            }
        }
    }

    @Override
    protected void doPush(Entity entity) {
        if (entity instanceof Enemy && !(entity instanceof Creeper)) {
            this.setTarget((LivingEntity) entity);
        }
        super.m_7324_(entity);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (target instanceof LivingEntity le) {
            le.setLastHurtByPlayer(this.getOwner());
        }
        return super.m_7327_(target);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new GolemFloatGoal(this));
        this.f_21345_.addGoal(1, new TeleportToOwnerGoal(this));
        this.f_21345_.addGoal(3, new FollowOwnerGoal(this));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new GolemRandomStrollGoal(this));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, (Class<T>) Mob.class, 5, false, false, this::predicatePriorityTarget));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, (Class<T>) LivingEntity.class, 5, false, false, this::predicateSecondaryTarget));
        this.f_21346_.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    protected boolean predicatePriorityTarget(LivingEntity e) {
        if (e instanceof Mob mob) {
            for (Optional<LivingEntity> target : List.of(Optional.ofNullable(mob.m_21214_()), Optional.ofNullable(mob.getTarget()), Optional.ofNullable(mob.m_21188_()))) {
                if (target.isPresent()) {
                    Player owner = this.getOwner();
                    if (target.get() == owner) {
                        return true;
                    }
                    if (((LivingEntity) target.get()).m_7307_(this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean predicateSecondaryTarget(LivingEntity e) {
        GolemConfigEntry config = this.getConfigEntry(null);
        return config == null ? DefaultFilterCard.defaultPredicate(e) : config.targetFilter.aggressiveToward(e);
    }

    public boolean isInSittingPose() {
        return false;
    }

    @Nullable
    public LivingEntity getFollowTarget() {
        return (LivingEntity) (this.getMode() == GolemModes.SQUAD ? this.getCaptain() : this.getOwner());
    }

    @Nullable
    public LivingEntity getCaptain() {
        if (this.m_9236_() instanceof ServerLevel sl) {
            GolemConfigEntry config = this.getConfigEntry(null);
            if (config == null) {
                return null;
            } else {
                UUID uuid = config.squadConfig.getCaptainId();
                if (uuid == null) {
                    return null;
                } else {
                    Entity captain = sl.getEntity(uuid);
                    if (captain == null) {
                        return null;
                    } else if (!captain.isAlive() || captain.level() != sl) {
                        return null;
                    } else {
                        return captain instanceof LivingEntity ? (LivingEntity) captain : null;
                    }
                }
            }
        } else {
            return null;
        }
    }

    public Vec3 getTargetPos() {
        if (this.getMode() == GolemModes.ROUTE) {
            List<PathRecordCard.Pos> list = PathConfig.getPath(this);
            if (list != null) {
                int target = this.getPatrolStage();
                if (!list.isEmpty()) {
                    return ((PathRecordCard.Pos) list.get(Math.min(target, list.size() - 1))).pos().getCenter();
                }
            }
            return this.m_20182_();
        } else if (this.getMode().hasPos()) {
            BlockPos pos = this.getGuardPos();
            return new Vec3((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5);
        } else {
            LivingEntity owner = this.getFollowTarget();
            return owner == null ? this.m_20318_(1.0F) : owner.m_20318_(1.0F);
        }
    }

    @Override
    public boolean isPowered() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return this.hasFlag(GolemFlags.IMMUNITY);
    }

    @Override
    public void die(DamageSource source) {
        ModularGolems.LOGGER.info("Golem {} died, message: '{}'", this, source.getLocalizedDeathMessage(this).getString());
        Player owner = this.getOwner();
        if (owner != null && !this.m_9236_().isClientSide) {
            owner.m_213846_(source.getLocalizedDeathMessage(this));
        }
        super.m_6667_(source);
    }

    @Override
    public double getPerceivedTargetDistanceSquareForMeleeAttack(LivingEntity target) {
        return GolemMeleeGoal.calculateDistSqr(this, target);
    }

    public void checkRide(LivingEntity target) {
    }

    public void resetTarget(@Nullable LivingEntity le) {
        for (WrappedGoal e : this.f_21346_.getAvailableGoals()) {
            if (e.getGoal() instanceof TargetGoal t) {
                t.stop();
            }
        }
        if (le != null) {
            this.m_6703_(le);
        }
    }

    public ItemWrapper getWrapperOfHand(EquipmentSlot slot) {
        return ItemWrapper.simple(() -> this.m_6844_(slot), e -> super.m_8061_(slot, e));
    }
}