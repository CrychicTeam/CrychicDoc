package net.minecraft.world.entity.decoration;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ArmorStand extends LivingEntity {

    public static final int WOBBLE_TIME = 5;

    private static final boolean ENABLE_ARMS = true;

    private static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);

    private static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);

    private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(-10.0F, 0.0F, -10.0F);

    private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(-15.0F, 0.0F, 10.0F);

    private static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(-1.0F, 0.0F, -1.0F);

    private static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(1.0F, 0.0F, 1.0F);

    private static final EntityDimensions MARKER_DIMENSIONS = new EntityDimensions(0.0F, 0.0F, true);

    private static final EntityDimensions BABY_DIMENSIONS = EntityType.ARMOR_STAND.getDimensions().scale(0.5F);

    private static final double FEET_OFFSET = 0.1;

    private static final double CHEST_OFFSET = 0.9;

    private static final double LEGS_OFFSET = 0.4;

    private static final double HEAD_OFFSET = 1.6;

    public static final int DISABLE_TAKING_OFFSET = 8;

    public static final int DISABLE_PUTTING_OFFSET = 16;

    public static final int CLIENT_FLAG_SMALL = 1;

    public static final int CLIENT_FLAG_SHOW_ARMS = 4;

    public static final int CLIENT_FLAG_NO_BASEPLATE = 8;

    public static final int CLIENT_FLAG_MARKER = 16;

    public static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.BYTE);

    public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<Rotations> DATA_BODY_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<Rotations> DATA_LEFT_LEG_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<Rotations> DATA_RIGHT_LEG_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);

    private static final Predicate<Entity> RIDABLE_MINECARTS = p_31582_ -> p_31582_ instanceof AbstractMinecart && ((AbstractMinecart) p_31582_).getMinecartType() == AbstractMinecart.Type.RIDEABLE;

    private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);

    private boolean invisible;

    public long lastHit;

    private int disabledSlots;

    private Rotations headPose = DEFAULT_HEAD_POSE;

    private Rotations bodyPose = DEFAULT_BODY_POSE;

    private Rotations leftArmPose = DEFAULT_LEFT_ARM_POSE;

    private Rotations rightArmPose = DEFAULT_RIGHT_ARM_POSE;

    private Rotations leftLegPose = DEFAULT_LEFT_LEG_POSE;

    private Rotations rightLegPose = DEFAULT_RIGHT_LEG_POSE;

    public ArmorStand(EntityType<? extends ArmorStand> entityTypeExtendsArmorStand0, Level level1) {
        super(entityTypeExtendsArmorStand0, level1);
        this.m_274367_(0.0F);
    }

    public ArmorStand(Level level0, double double1, double double2, double double3) {
        this(EntityType.ARMOR_STAND, level0);
        this.m_6034_(double1, double2, double3);
    }

    @Override
    public void refreshDimensions() {
        double $$0 = this.m_20185_();
        double $$1 = this.m_20186_();
        double $$2 = this.m_20189_();
        super.m_6210_();
        this.m_6034_($$0, $$1, $$2);
    }

    private boolean hasPhysics() {
        return !this.isMarker() && !this.m_20068_();
    }

    @Override
    public boolean isEffectiveAi() {
        return super.m_21515_() && this.hasPhysics();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_CLIENT_FLAGS, (byte) 0);
        this.f_19804_.define(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
        this.f_19804_.define(DATA_BODY_POSE, DEFAULT_BODY_POSE);
        this.f_19804_.define(DATA_LEFT_ARM_POSE, DEFAULT_LEFT_ARM_POSE);
        this.f_19804_.define(DATA_RIGHT_ARM_POSE, DEFAULT_RIGHT_ARM_POSE);
        this.f_19804_.define(DATA_LEFT_LEG_POSE, DEFAULT_LEFT_LEG_POSE);
        this.f_19804_.define(DATA_RIGHT_LEG_POSE, DEFAULT_RIGHT_LEG_POSE);
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
    public boolean canTakeItem(ItemStack itemStack0) {
        EquipmentSlot $$1 = Mob.m_147233_(itemStack0);
        return this.getItemBySlot($$1).isEmpty() && !this.isDisabled($$1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
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
        compoundTag0.putBoolean("Invisible", this.m_20145_());
        compoundTag0.putBoolean("Small", this.isSmall());
        compoundTag0.putBoolean("ShowArms", this.isShowArms());
        compoundTag0.putInt("DisabledSlots", this.disabledSlots);
        compoundTag0.putBoolean("NoBasePlate", this.isNoBasePlate());
        if (this.isMarker()) {
            compoundTag0.putBoolean("Marker", this.isMarker());
        }
        compoundTag0.put("Pose", this.writePose());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
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
        this.setInvisible(compoundTag0.getBoolean("Invisible"));
        this.setSmall(compoundTag0.getBoolean("Small"));
        this.setShowArms(compoundTag0.getBoolean("ShowArms"));
        this.disabledSlots = compoundTag0.getInt("DisabledSlots");
        this.setNoBasePlate(compoundTag0.getBoolean("NoBasePlate"));
        this.setMarker(compoundTag0.getBoolean("Marker"));
        this.f_19794_ = !this.hasPhysics();
        CompoundTag $$5 = compoundTag0.getCompound("Pose");
        this.readPose($$5);
    }

    private void readPose(CompoundTag compoundTag0) {
        ListTag $$1 = compoundTag0.getList("Head", 5);
        this.setHeadPose($$1.isEmpty() ? DEFAULT_HEAD_POSE : new Rotations($$1));
        ListTag $$2 = compoundTag0.getList("Body", 5);
        this.setBodyPose($$2.isEmpty() ? DEFAULT_BODY_POSE : new Rotations($$2));
        ListTag $$3 = compoundTag0.getList("LeftArm", 5);
        this.setLeftArmPose($$3.isEmpty() ? DEFAULT_LEFT_ARM_POSE : new Rotations($$3));
        ListTag $$4 = compoundTag0.getList("RightArm", 5);
        this.setRightArmPose($$4.isEmpty() ? DEFAULT_RIGHT_ARM_POSE : new Rotations($$4));
        ListTag $$5 = compoundTag0.getList("LeftLeg", 5);
        this.setLeftLegPose($$5.isEmpty() ? DEFAULT_LEFT_LEG_POSE : new Rotations($$5));
        ListTag $$6 = compoundTag0.getList("RightLeg", 5);
        this.setRightLegPose($$6.isEmpty() ? DEFAULT_RIGHT_LEG_POSE : new Rotations($$6));
    }

    private CompoundTag writePose() {
        CompoundTag $$0 = new CompoundTag();
        if (!DEFAULT_HEAD_POSE.equals(this.headPose)) {
            $$0.put("Head", this.headPose.save());
        }
        if (!DEFAULT_BODY_POSE.equals(this.bodyPose)) {
            $$0.put("Body", this.bodyPose.save());
        }
        if (!DEFAULT_LEFT_ARM_POSE.equals(this.leftArmPose)) {
            $$0.put("LeftArm", this.leftArmPose.save());
        }
        if (!DEFAULT_RIGHT_ARM_POSE.equals(this.rightArmPose)) {
            $$0.put("RightArm", this.rightArmPose.save());
        }
        if (!DEFAULT_LEFT_LEG_POSE.equals(this.leftLegPose)) {
            $$0.put("LeftLeg", this.leftLegPose.save());
        }
        if (!DEFAULT_RIGHT_LEG_POSE.equals(this.rightLegPose)) {
            $$0.put("RightLeg", this.rightLegPose.save());
        }
        return $$0;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity0) {
    }

    @Override
    protected void pushEntities() {
        List<Entity> $$0 = this.m_9236_().getEntities(this, this.m_20191_(), RIDABLE_MINECARTS);
        for (int $$1 = 0; $$1 < $$0.size(); $$1++) {
            Entity $$2 = (Entity) $$0.get($$1);
            if (this.m_20280_($$2) <= 0.2) {
                $$2.push(this);
            }
        }
    }

    @Override
    public InteractionResult interactAt(Player player0, Vec3 vec1, InteractionHand interactionHand2) {
        ItemStack $$3 = player0.m_21120_(interactionHand2);
        if (this.isMarker() || $$3.is(Items.NAME_TAG)) {
            return InteractionResult.PASS;
        } else if (player0.isSpectator()) {
            return InteractionResult.SUCCESS;
        } else if (player0.m_9236_().isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            EquipmentSlot $$4 = Mob.m_147233_($$3);
            if ($$3.isEmpty()) {
                EquipmentSlot $$5 = this.getClickedSlot(vec1);
                EquipmentSlot $$6 = this.isDisabled($$5) ? $$4 : $$5;
                if (this.m_21033_($$6) && this.swapItem(player0, $$6, $$3, interactionHand2)) {
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (this.isDisabled($$4)) {
                    return InteractionResult.FAIL;
                }
                if ($$4.getType() == EquipmentSlot.Type.HAND && !this.isShowArms()) {
                    return InteractionResult.FAIL;
                }
                if (this.swapItem(player0, $$4, $$3, interactionHand2)) {
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        }
    }

    private EquipmentSlot getClickedSlot(Vec3 vec0) {
        EquipmentSlot $$1 = EquipmentSlot.MAINHAND;
        boolean $$2 = this.isSmall();
        double $$3 = $$2 ? vec0.y * 2.0 : vec0.y;
        EquipmentSlot $$4 = EquipmentSlot.FEET;
        if ($$3 >= 0.1 && $$3 < 0.1 + ($$2 ? 0.8 : 0.45) && this.m_21033_($$4)) {
            $$1 = EquipmentSlot.FEET;
        } else if ($$3 >= 0.9 + ($$2 ? 0.3 : 0.0) && $$3 < 0.9 + ($$2 ? 1.0 : 0.7) && this.m_21033_(EquipmentSlot.CHEST)) {
            $$1 = EquipmentSlot.CHEST;
        } else if ($$3 >= 0.4 && $$3 < 0.4 + ($$2 ? 1.0 : 0.8) && this.m_21033_(EquipmentSlot.LEGS)) {
            $$1 = EquipmentSlot.LEGS;
        } else if ($$3 >= 1.6 && this.m_21033_(EquipmentSlot.HEAD)) {
            $$1 = EquipmentSlot.HEAD;
        } else if (!this.m_21033_(EquipmentSlot.MAINHAND) && this.m_21033_(EquipmentSlot.OFFHAND)) {
            $$1 = EquipmentSlot.OFFHAND;
        }
        return $$1;
    }

    private boolean isDisabled(EquipmentSlot equipmentSlot0) {
        return (this.disabledSlots & 1 << equipmentSlot0.getFilterFlag()) != 0 || equipmentSlot0.getType() == EquipmentSlot.Type.HAND && !this.isShowArms();
    }

    private boolean swapItem(Player player0, EquipmentSlot equipmentSlot1, ItemStack itemStack2, InteractionHand interactionHand3) {
        ItemStack $$4 = this.getItemBySlot(equipmentSlot1);
        if (!$$4.isEmpty() && (this.disabledSlots & 1 << equipmentSlot1.getFilterFlag() + 8) != 0) {
            return false;
        } else if ($$4.isEmpty() && (this.disabledSlots & 1 << equipmentSlot1.getFilterFlag() + 16) != 0) {
            return false;
        } else if (player0.getAbilities().instabuild && $$4.isEmpty() && !itemStack2.isEmpty()) {
            this.setItemSlot(equipmentSlot1, itemStack2.copyWithCount(1));
            return true;
        } else if (itemStack2.isEmpty() || itemStack2.getCount() <= 1) {
            this.setItemSlot(equipmentSlot1, itemStack2);
            player0.m_21008_(interactionHand3, $$4);
            return true;
        } else if (!$$4.isEmpty()) {
            return false;
        } else {
            this.setItemSlot(equipmentSlot1, itemStack2.split(1));
            return true;
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_9236_().isClientSide || this.m_213877_()) {
            return false;
        } else if (damageSource0.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.kill();
            return false;
        } else if (this.m_6673_(damageSource0) || this.invisible || this.isMarker()) {
            return false;
        } else if (damageSource0.is(DamageTypeTags.IS_EXPLOSION)) {
            this.brokenByAnything(damageSource0);
            this.kill();
            return false;
        } else if (damageSource0.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
            if (this.m_6060_()) {
                this.causeDamage(damageSource0, 0.15F);
            } else {
                this.m_20254_(5);
            }
            return false;
        } else if (damageSource0.is(DamageTypeTags.BURNS_ARMOR_STANDS) && this.m_21223_() > 0.5F) {
            this.causeDamage(damageSource0, 4.0F);
            return false;
        } else {
            boolean $$2 = damageSource0.getDirectEntity() instanceof AbstractArrow;
            boolean $$3 = $$2 && ((AbstractArrow) damageSource0.getDirectEntity()).getPierceLevel() > 0;
            boolean $$4 = "player".equals(damageSource0.getMsgId());
            if (!$$4 && !$$2) {
                return false;
            } else {
                if (damageSource0.getEntity() instanceof Player $$5 && !$$5.getAbilities().mayBuild) {
                    return false;
                }
                if (damageSource0.isCreativePlayer()) {
                    this.playBrokenSound();
                    this.showBreakingParticles();
                    this.kill();
                    return $$3;
                } else {
                    long $$6 = this.m_9236_().getGameTime();
                    if ($$6 - this.lastHit > 5L && !$$2) {
                        this.m_9236_().broadcastEntityEvent(this, (byte) 32);
                        this.m_146852_(GameEvent.ENTITY_DAMAGE, damageSource0.getEntity());
                        this.lastHit = $$6;
                    } else {
                        this.brokenByPlayer(damageSource0);
                        this.showBreakingParticles();
                        this.kill();
                    }
                    return true;
                }
            }
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 32) {
            if (this.m_9236_().isClientSide) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ARMOR_STAND_HIT, this.m_5720_(), 0.3F, 1.0F, false);
                this.lastHit = this.m_9236_().getGameTime();
            }
        } else {
            super.handleEntityEvent(byte0);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double double0) {
        double $$1 = this.m_20191_().getSize() * 4.0;
        if (Double.isNaN($$1) || $$1 == 0.0) {
            $$1 = 4.0;
        }
        $$1 *= 64.0;
        return double0 < $$1 * $$1;
    }

    private void showBreakingParticles() {
        if (this.m_9236_() instanceof ServerLevel) {
            ((ServerLevel) this.m_9236_()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState()), this.m_20185_(), this.m_20227_(0.6666666666666666), this.m_20189_(), 10, (double) (this.m_20205_() / 4.0F), (double) (this.m_20206_() / 4.0F), (double) (this.m_20205_() / 4.0F), 0.05);
        }
    }

    private void causeDamage(DamageSource damageSource0, float float1) {
        float $$2 = this.m_21223_();
        $$2 -= float1;
        if ($$2 <= 0.5F) {
            this.brokenByAnything(damageSource0);
            this.kill();
        } else {
            this.m_21153_($$2);
            this.m_146852_(GameEvent.ENTITY_DAMAGE, damageSource0.getEntity());
        }
    }

    private void brokenByPlayer(DamageSource damageSource0) {
        ItemStack $$1 = new ItemStack(Items.ARMOR_STAND);
        if (this.m_8077_()) {
            $$1.setHoverName(this.m_7770_());
        }
        Block.popResource(this.m_9236_(), this.m_20183_(), $$1);
        this.brokenByAnything(damageSource0);
    }

    private void brokenByAnything(DamageSource damageSource0) {
        this.playBrokenSound();
        this.m_6668_(damageSource0);
        for (int $$1 = 0; $$1 < this.handItems.size(); $$1++) {
            ItemStack $$2 = this.handItems.get($$1);
            if (!$$2.isEmpty()) {
                Block.popResource(this.m_9236_(), this.m_20183_().above(), $$2);
                this.handItems.set($$1, ItemStack.EMPTY);
            }
        }
        for (int $$3 = 0; $$3 < this.armorItems.size(); $$3++) {
            ItemStack $$4 = this.armorItems.get($$3);
            if (!$$4.isEmpty()) {
                Block.popResource(this.m_9236_(), this.m_20183_().above(), $$4);
                this.armorItems.set($$3, ItemStack.EMPTY);
            }
        }
    }

    private void playBrokenSound() {
        this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ARMOR_STAND_BREAK, this.m_5720_(), 1.0F, 1.0F);
    }

    @Override
    protected float tickHeadTurn(float float0, float float1) {
        this.f_20884_ = this.f_19859_;
        this.f_20883_ = this.m_146908_();
        return 0.0F;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height * (this.isBaby() ? 0.5F : 0.9F);
    }

    @Override
    public double getMyRidingOffset() {
        return this.isMarker() ? 0.0 : 0.1F;
    }

    @Override
    public void travel(Vec3 vec0) {
        if (this.hasPhysics()) {
            super.travel(vec0);
        }
    }

    @Override
    public void setYBodyRot(float float0) {
        this.f_20884_ = this.f_19859_ = float0;
        this.f_20886_ = this.f_20885_ = float0;
    }

    @Override
    public void setYHeadRot(float float0) {
        this.f_20884_ = this.f_19859_ = float0;
        this.f_20886_ = this.f_20885_ = float0;
    }

    @Override
    public void tick() {
        super.tick();
        Rotations $$0 = this.f_19804_.get(DATA_HEAD_POSE);
        if (!this.headPose.equals($$0)) {
            this.setHeadPose($$0);
        }
        Rotations $$1 = this.f_19804_.get(DATA_BODY_POSE);
        if (!this.bodyPose.equals($$1)) {
            this.setBodyPose($$1);
        }
        Rotations $$2 = this.f_19804_.get(DATA_LEFT_ARM_POSE);
        if (!this.leftArmPose.equals($$2)) {
            this.setLeftArmPose($$2);
        }
        Rotations $$3 = this.f_19804_.get(DATA_RIGHT_ARM_POSE);
        if (!this.rightArmPose.equals($$3)) {
            this.setRightArmPose($$3);
        }
        Rotations $$4 = this.f_19804_.get(DATA_LEFT_LEG_POSE);
        if (!this.leftLegPose.equals($$4)) {
            this.setLeftLegPose($$4);
        }
        Rotations $$5 = this.f_19804_.get(DATA_RIGHT_LEG_POSE);
        if (!this.rightLegPose.equals($$5)) {
            this.setRightLegPose($$5);
        }
    }

    @Override
    protected void updateInvisibilityStatus() {
        this.setInvisible(this.invisible);
    }

    @Override
    public void setInvisible(boolean boolean0) {
        this.invisible = boolean0;
        super.m_6842_(boolean0);
    }

    @Override
    public boolean isBaby() {
        return this.isSmall();
    }

    @Override
    public void kill() {
        this.m_142687_(Entity.RemovalReason.KILLED);
        this.m_146850_(GameEvent.ENTITY_DIE);
    }

    @Override
    public boolean ignoreExplosion() {
        return this.m_20145_();
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return this.isMarker() ? PushReaction.IGNORE : super.m_7752_();
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return this.isMarker();
    }

    private void setSmall(boolean boolean0) {
        this.f_19804_.set(DATA_CLIENT_FLAGS, this.setBit(this.f_19804_.get(DATA_CLIENT_FLAGS), 1, boolean0));
    }

    public boolean isSmall() {
        return (this.f_19804_.get(DATA_CLIENT_FLAGS) & 1) != 0;
    }

    public void setShowArms(boolean boolean0) {
        this.f_19804_.set(DATA_CLIENT_FLAGS, this.setBit(this.f_19804_.get(DATA_CLIENT_FLAGS), 4, boolean0));
    }

    public boolean isShowArms() {
        return (this.f_19804_.get(DATA_CLIENT_FLAGS) & 4) != 0;
    }

    public void setNoBasePlate(boolean boolean0) {
        this.f_19804_.set(DATA_CLIENT_FLAGS, this.setBit(this.f_19804_.get(DATA_CLIENT_FLAGS), 8, boolean0));
    }

    public boolean isNoBasePlate() {
        return (this.f_19804_.get(DATA_CLIENT_FLAGS) & 8) != 0;
    }

    private void setMarker(boolean boolean0) {
        this.f_19804_.set(DATA_CLIENT_FLAGS, this.setBit(this.f_19804_.get(DATA_CLIENT_FLAGS), 16, boolean0));
    }

    public boolean isMarker() {
        return (this.f_19804_.get(DATA_CLIENT_FLAGS) & 16) != 0;
    }

    private byte setBit(byte byte0, int int1, boolean boolean2) {
        if (boolean2) {
            byte0 = (byte) (byte0 | int1);
        } else {
            byte0 = (byte) (byte0 & ~int1);
        }
        return byte0;
    }

    public void setHeadPose(Rotations rotations0) {
        this.headPose = rotations0;
        this.f_19804_.set(DATA_HEAD_POSE, rotations0);
    }

    public void setBodyPose(Rotations rotations0) {
        this.bodyPose = rotations0;
        this.f_19804_.set(DATA_BODY_POSE, rotations0);
    }

    public void setLeftArmPose(Rotations rotations0) {
        this.leftArmPose = rotations0;
        this.f_19804_.set(DATA_LEFT_ARM_POSE, rotations0);
    }

    public void setRightArmPose(Rotations rotations0) {
        this.rightArmPose = rotations0;
        this.f_19804_.set(DATA_RIGHT_ARM_POSE, rotations0);
    }

    public void setLeftLegPose(Rotations rotations0) {
        this.leftLegPose = rotations0;
        this.f_19804_.set(DATA_LEFT_LEG_POSE, rotations0);
    }

    public void setRightLegPose(Rotations rotations0) {
        this.rightLegPose = rotations0;
        this.f_19804_.set(DATA_RIGHT_LEG_POSE, rotations0);
    }

    public Rotations getHeadPose() {
        return this.headPose;
    }

    public Rotations getBodyPose() {
        return this.bodyPose;
    }

    public Rotations getLeftArmPose() {
        return this.leftArmPose;
    }

    public Rotations getRightArmPose() {
        return this.rightArmPose;
    }

    public Rotations getLeftLegPose() {
        return this.leftLegPose;
    }

    public Rotations getRightLegPose() {
        return this.rightLegPose;
    }

    @Override
    public boolean isPickable() {
        return super.isPickable() && !this.isMarker();
    }

    @Override
    public boolean skipAttackInteraction(Entity entity0) {
        return entity0 instanceof Player && !this.m_9236_().mayInteract((Player) entity0, this.m_20183_());
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.ARMOR_STAND_HIT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ARMOR_STAND_BREAK;
    }

    @Override
    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (DATA_CLIENT_FLAGS.equals(entityDataAccessor0)) {
            this.refreshDimensions();
            this.f_19850_ = !this.isMarker();
        }
        super.onSyncedDataUpdated(entityDataAccessor0);
    }

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        return this.getDimensionsMarker(this.isMarker());
    }

    private EntityDimensions getDimensionsMarker(boolean boolean0) {
        if (boolean0) {
            return MARKER_DIMENSIONS;
        } else {
            return this.isBaby() ? BABY_DIMENSIONS : this.m_6095_().getDimensions();
        }
    }

    @Override
    public Vec3 getLightProbePosition(float float0) {
        if (this.isMarker()) {
            AABB $$1 = this.getDimensionsMarker(false).makeBoundingBox(this.m_20182_());
            BlockPos $$2 = this.m_20183_();
            int $$3 = Integer.MIN_VALUE;
            for (BlockPos $$4 : BlockPos.betweenClosed(BlockPos.containing($$1.minX, $$1.minY, $$1.minZ), BlockPos.containing($$1.maxX, $$1.maxY, $$1.maxZ))) {
                int $$5 = Math.max(this.m_9236_().m_45517_(LightLayer.BLOCK, $$4), this.m_9236_().m_45517_(LightLayer.SKY, $$4));
                if ($$5 == 15) {
                    return Vec3.atCenterOf($$4);
                }
                if ($$5 > $$3) {
                    $$3 = $$5;
                    $$2 = $$4.immutable();
                }
            }
            return Vec3.atCenterOf($$2);
        } else {
            return super.m_7371_(float0);
        }
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.ARMOR_STAND);
    }

    @Override
    public boolean canBeSeenByAnyone() {
        return !this.m_20145_() && !this.isMarker();
    }
}