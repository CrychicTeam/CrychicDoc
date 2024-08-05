package com.mna.entities.constructs;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.entities.ISpellInteractibleEntity;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.entities.EntityInit;
import com.mna.entities.constructs.animated.Construct;
import com.mna.events.EventDispatcher;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.ItemAnimusDust;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ConstructAssemblyStand extends LivingEntity implements ISpellInteractibleEntity<ConstructAssemblyStand> {

    public static final int WOBBLE_TIME = 5;

    private static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);

    private static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);

    private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(-10.0F, 0.0F, -10.0F);

    private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(-15.0F, 0.0F, 10.0F);

    private static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(-1.0F, 0.0F, -1.0F);

    private static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(1.0F, 0.0F, 1.0F);

    public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE = SynchedEntityData.defineId(ConstructAssemblyStand.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<Rotations> DATA_BODY_POSE = SynchedEntityData.defineId(ConstructAssemblyStand.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE = SynchedEntityData.defineId(ConstructAssemblyStand.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE = SynchedEntityData.defineId(ConstructAssemblyStand.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<Rotations> DATA_LEFT_LEG_POSE = SynchedEntityData.defineId(ConstructAssemblyStand.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<Rotations> DATA_RIGHT_LEG_POSE = SynchedEntityData.defineId(ConstructAssemblyStand.class, EntityDataSerializers.ROTATIONS);

    private Rotations headPose = DEFAULT_HEAD_POSE;

    private Rotations bodyPose = DEFAULT_BODY_POSE;

    private Rotations leftArmPose = DEFAULT_LEFT_ARM_POSE;

    private Rotations rightArmPose = DEFAULT_RIGHT_ARM_POSE;

    private Rotations leftLegPose = DEFAULT_LEFT_LEG_POSE;

    private Rotations rightLegPose = DEFAULT_RIGHT_LEG_POSE;

    private final NonNullList<ItemStack> bodyItems = NonNullList.withSize(4, ItemStack.EMPTY);

    private final NonNullList<ItemStack> armItems = NonNullList.withSize(2, ItemStack.EMPTY);

    public long lastHit;

    private static final Predicate<Entity> RIDABLE_MINECARTS = p_31582_ -> p_31582_ instanceof AbstractMinecart && ((AbstractMinecart) p_31582_).canBeRidden();

    public ConstructAssemblyStand(EntityType<? extends ConstructAssemblyStand> type, Level world) {
        super(type, world);
    }

    public ConstructAssemblyStand(Level world, double x, double y, double z) {
        this(EntityInit.CONSTRUCT_ASSEMBLY_STAND.get(), world);
        this.m_6034_(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
        this.f_19804_.define(DATA_BODY_POSE, DEFAULT_BODY_POSE);
        this.f_19804_.define(DATA_LEFT_ARM_POSE, DEFAULT_LEFT_ARM_POSE);
        this.f_19804_.define(DATA_RIGHT_ARM_POSE, DEFAULT_RIGHT_ARM_POSE);
        this.f_19804_.define(DATA_LEFT_LEG_POSE, DEFAULT_LEFT_LEG_POSE);
        this.f_19804_.define(DATA_RIGHT_LEG_POSE, DEFAULT_RIGHT_LEG_POSE);
    }

    public float getStepHeight() {
        return 0.0F;
    }

    public ConstructMaterial[] getComposition() {
        ArrayList<ConstructMaterial> materials = new ArrayList();
        this.bodyItems.forEach(is -> {
            if (is.getItem() instanceof ItemConstructPart) {
                materials.add(((ItemConstructPart) is.getItem()).getMaterial());
            }
        });
        this.armItems.forEach(is -> {
            if (is.getItem() instanceof ItemConstructPart) {
                materials.add(((ItemConstructPart) is.getItem()).getMaterial());
            }
        });
        return (ConstructMaterial[]) materials.toArray(new ConstructMaterial[0]);
    }

    public ItemConstructPart[] getPartsForMaterial(ConstructMaterial matl) {
        ArrayList<ItemConstructPart> parts = new ArrayList();
        this.bodyItems.forEach(is -> {
            if (is.getItem() instanceof ItemConstructPart && ((ItemConstructPart) is.getItem()).getMaterial() == matl) {
                parts.add((ItemConstructPart) is.getItem());
            }
        });
        this.armItems.forEach(is -> {
            if (is.getItem() instanceof ItemConstructPart && ((ItemConstructPart) is.getItem()).getMaterial() == matl) {
                parts.add((ItemConstructPart) is.getItem());
            }
        });
        return (ItemConstructPart[]) parts.toArray(new ItemConstructPart[0]);
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.m_20185_();
        double d1 = this.m_20186_();
        double d2 = this.m_20189_();
        super.m_6210_();
        this.m_6034_(d0, d1, d2);
    }

    private boolean hasPhysics() {
        return !this.m_20068_();
    }

    @Override
    public boolean isEffectiveAi() {
        return super.m_21515_() && this.hasPhysics();
    }

    @Override
    public Iterable<ItemStack> getHandSlots() {
        return this.armItems;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return this.bodyItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        switch(slot) {
            case LEGS:
            case CHEST:
            case HEAD:
                return this.bodyItems.get(slot.getIndex());
            case MAINHAND:
            case OFFHAND:
                return this.armItems.get(slot.getIndex());
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        this.m_181122_(stack);
        if (!stack.isEmpty()) {
            this.m_5496_(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
        }
        if (slot.getType() == EquipmentSlot.Type.HAND) {
            this.armItems.set(slot.getIndex(), stack);
        } else {
            this.bodyItems.set(slot.getIndex(), stack);
        }
    }

    @Override
    public boolean canTakeItem(ItemStack itemStack0) {
        EquipmentSlot equipmentslot = Mob.m_147233_(itemStack0);
        return this.getItemBySlot(equipmentslot).isEmpty();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ListTag listtag = new ListTag();
        for (ItemStack itemstack : this.bodyItems) {
            CompoundTag compoundtag = new CompoundTag();
            if (!itemstack.isEmpty()) {
                itemstack.save(compoundtag);
            }
            listtag.add(compoundtag);
        }
        tag.put("ArmorItems", listtag);
        ListTag listtag1 = new ListTag();
        for (ItemStack itemstack1 : this.armItems) {
            CompoundTag compoundtag1 = new CompoundTag();
            if (!itemstack1.isEmpty()) {
                itemstack1.save(compoundtag1);
            }
            listtag1.add(compoundtag1);
        }
        tag.put("HandItems", listtag1);
        tag.put("Pose", this.writePose());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("ArmorItems", 9)) {
            ListTag listtag = compoundTag0.getList("ArmorItems", 10);
            for (int i = 0; i < this.bodyItems.size(); i++) {
                this.bodyItems.set(i, ItemStack.of(listtag.getCompound(i)));
            }
        }
        if (compoundTag0.contains("HandItems", 9)) {
            ListTag listtag1 = compoundTag0.getList("HandItems", 10);
            for (int j = 0; j < this.armItems.size(); j++) {
                this.armItems.set(j, ItemStack.of(listtag1.getCompound(j)));
            }
        }
        this.f_19794_ = !this.hasPhysics();
        CompoundTag compoundtag = compoundTag0.getCompound("Pose");
        this.readPose(compoundtag);
    }

    private void readPose(CompoundTag compoundTag0) {
        ListTag listtag = compoundTag0.getList("Head", 5);
        this.setHeadPose(listtag.isEmpty() ? DEFAULT_HEAD_POSE : new Rotations(listtag));
        ListTag listtag1 = compoundTag0.getList("Body", 5);
        this.setBodyPose(listtag1.isEmpty() ? DEFAULT_BODY_POSE : new Rotations(listtag1));
        ListTag listtag2 = compoundTag0.getList("LeftArm", 5);
        this.setLeftArmPose(listtag2.isEmpty() ? DEFAULT_LEFT_ARM_POSE : new Rotations(listtag2));
        ListTag listtag3 = compoundTag0.getList("RightArm", 5);
        this.setRightArmPose(listtag3.isEmpty() ? DEFAULT_RIGHT_ARM_POSE : new Rotations(listtag3));
        ListTag listtag4 = compoundTag0.getList("LeftLeg", 5);
        this.setLeftLegPose(listtag4.isEmpty() ? DEFAULT_LEFT_LEG_POSE : new Rotations(listtag4));
        ListTag listtag5 = compoundTag0.getList("RightLeg", 5);
        this.setRightLegPose(listtag5.isEmpty() ? DEFAULT_RIGHT_LEG_POSE : new Rotations(listtag5));
    }

    private CompoundTag writePose() {
        CompoundTag compoundtag = new CompoundTag();
        if (!DEFAULT_HEAD_POSE.equals(this.headPose)) {
            compoundtag.put("Head", this.headPose.save());
        }
        if (!DEFAULT_BODY_POSE.equals(this.bodyPose)) {
            compoundtag.put("Body", this.bodyPose.save());
        }
        if (!DEFAULT_LEFT_ARM_POSE.equals(this.leftArmPose)) {
            compoundtag.put("LeftArm", this.leftArmPose.save());
        }
        if (!DEFAULT_RIGHT_ARM_POSE.equals(this.rightArmPose)) {
            compoundtag.put("RightArm", this.rightArmPose.save());
        }
        if (!DEFAULT_LEFT_LEG_POSE.equals(this.leftLegPose)) {
            compoundtag.put("LeftLeg", this.leftLegPose.save());
        }
        if (!DEFAULT_RIGHT_LEG_POSE.equals(this.rightLegPose)) {
            compoundtag.put("RightLeg", this.rightLegPose.save());
        }
        return compoundtag;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
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
        List<Entity> list = this.m_9236_().getEntities(this, this.m_20191_(), RIDABLE_MINECARTS);
        for (int i = 0; i < list.size(); i++) {
            Entity entity = (Entity) list.get(i);
            if (this.m_20280_(entity) <= 0.2) {
                entity.push(this);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        Rotations rotations = this.f_19804_.get(DATA_HEAD_POSE);
        if (!this.headPose.equals(rotations)) {
            this.setHeadPose(rotations);
        }
        Rotations rotations1 = this.f_19804_.get(DATA_BODY_POSE);
        if (!this.bodyPose.equals(rotations1)) {
            this.setBodyPose(rotations1);
        }
        Rotations rotations2 = this.f_19804_.get(DATA_LEFT_ARM_POSE);
        if (!this.leftArmPose.equals(rotations2)) {
            this.setLeftArmPose(rotations2);
        }
        Rotations rotations3 = this.f_19804_.get(DATA_RIGHT_ARM_POSE);
        if (!this.rightArmPose.equals(rotations3)) {
            this.setRightArmPose(rotations3);
        }
        Rotations rotations4 = this.f_19804_.get(DATA_LEFT_LEG_POSE);
        if (!this.leftLegPose.equals(rotations4)) {
            this.setLeftLegPose(rotations4);
        }
        Rotations rotations5 = this.f_19804_.get(DATA_RIGHT_LEG_POSE);
        if (!this.rightLegPose.equals(rotations5)) {
            this.setRightLegPose(rotations5);
        }
    }

    @Override
    public InteractionResult interactAt(Player player0, Vec3 vec1, InteractionHand interactionHand2) {
        ItemStack itemstack = player0.m_21120_(interactionHand2);
        if (player0.isSpectator()) {
            return InteractionResult.SUCCESS;
        } else if (player0.m_9236_().isClientSide()) {
            return InteractionResult.CONSUME;
        } else if (itemstack.getItem() instanceof ItemConstructPart) {
            EquipmentSlot slot = EquipmentSlot.FEET;
            switch(((ItemConstructPart) itemstack.getItem()).getSlot()) {
                case HEAD:
                    slot = EquipmentSlot.HEAD;
                    break;
                case LEFT_ARM:
                    slot = EquipmentSlot.MAINHAND;
                    break;
                case LEGS:
                    slot = EquipmentSlot.LEGS;
                    break;
                case RIGHT_ARM:
                    slot = EquipmentSlot.OFFHAND;
                    break;
                case TORSO:
                    slot = EquipmentSlot.CHEST;
            }
            if (itemstack.isEmpty()) {
                EquipmentSlot equipmentslot1 = this.getClickedSlot(vec1);
                if (this.m_21033_(equipmentslot1) && this.swapItem(player0, equipmentslot1, itemstack, interactionHand2)) {
                    return InteractionResult.SUCCESS;
                }
            } else if (this.swapItem(player0, slot, itemstack, interactionHand2)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        } else if (itemstack.getItem() == ItemInit.ANIMUS_DUST.get()) {
            this.summonConstruct(player0);
            itemstack.shrink(1);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    private boolean isConstructComplete() {
        List<ItemStack> parts = new ArrayList();
        parts.add(this.getItemBySlot(EquipmentSlot.HEAD));
        parts.add(this.getItemBySlot(EquipmentSlot.CHEST));
        parts.add(this.getItemBySlot(EquipmentSlot.MAINHAND));
        parts.add(this.getItemBySlot(EquipmentSlot.OFFHAND));
        parts.add(this.getItemBySlot(EquipmentSlot.LEGS));
        return parts.stream().allMatch(is -> !is.isEmpty() && is.getItem() instanceof ItemConstructPart);
    }

    private void summonConstruct(Player player) {
        if (this.isConstructComplete()) {
            if (!this.m_9236_().isClientSide() && player.getGameProfile() != null && player.getGameProfile().getId() != null) {
                Construct construct = new Construct(this.m_9236_());
                construct.setConstructParts(this.getItemBySlot(EquipmentSlot.HEAD), this.getItemBySlot(EquipmentSlot.CHEST), this.getItemBySlot(EquipmentSlot.MAINHAND), this.getItemBySlot(EquipmentSlot.OFFHAND), this.getItemBySlot(EquipmentSlot.LEGS));
                Vec3 fwd = this.m_20156_().normalize();
                construct.m_7678_(this.m_20185_() + fwd.x, this.m_20186_() + fwd.y, this.m_20189_() + fwd.z, 0.0F, 0.0F);
                construct.m_7618_(EntityAnchorArgument.Anchor.FEET, this.m_20182_().add(fwd.scale(10.0)));
                construct.setOwner(player.getGameProfile().getId());
                this.m_9236_().m_7967_(construct);
                EventDispatcher.DispatchConstructCrafted(construct, player);
                if (player instanceof ServerPlayer) {
                    CustomAdvancementTriggers.SUMMON_CONSTRUCT.trigger((ServerPlayer) player, construct);
                }
                this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                this.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
                this.brokenByPlayer(this.m_269291_().generic());
                this.kill();
            } else {
                ItemAnimusDust.spawnParticles(this.m_9236_(), this.m_20183_());
            }
        }
    }

    private EquipmentSlot getClickedSlot(Vec3 vec0) {
        EquipmentSlot equipmentslot = EquipmentSlot.MAINHAND;
        double d0 = vec0.y;
        EquipmentSlot equipmentslot1 = EquipmentSlot.LEGS;
        if (d0 >= 0.1 && d0 < 0.55 && this.m_21033_(equipmentslot1)) {
            equipmentslot = EquipmentSlot.LEGS;
        } else if (d0 >= 0.9 && d0 < 1.6 && this.m_21033_(EquipmentSlot.CHEST)) {
            equipmentslot = EquipmentSlot.CHEST;
        } else if (d0 >= 0.4 && d0 < 1.2 && this.m_21033_(EquipmentSlot.LEGS)) {
            equipmentslot = EquipmentSlot.LEGS;
        } else if (d0 >= 1.6 && this.m_21033_(EquipmentSlot.HEAD)) {
            equipmentslot = EquipmentSlot.HEAD;
        } else if (!this.m_21033_(EquipmentSlot.MAINHAND) && this.m_21033_(EquipmentSlot.OFFHAND)) {
            equipmentslot = EquipmentSlot.OFFHAND;
        }
        return equipmentslot;
    }

    private boolean swapItem(Player player, EquipmentSlot slot, ItemStack insert, InteractionHand hand) {
        ItemStack itemstack = this.getItemBySlot(slot);
        if (player.getAbilities().instabuild && itemstack.isEmpty() && !insert.isEmpty()) {
            ItemStack itemstack2 = insert.copy();
            itemstack2.setCount(1);
            this.setItemSlot(slot, itemstack2);
            return true;
        } else if (insert.isEmpty() || insert.getCount() <= 1) {
            this.setItemSlot(slot, insert);
            player.m_21008_(hand, itemstack);
            return true;
        } else if (!itemstack.isEmpty()) {
            return false;
        } else {
            ItemStack itemstack1 = insert.copy();
            itemstack1.setCount(1);
            this.setItemSlot(slot, itemstack1);
            insert.shrink(1);
            return true;
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_9236_().isClientSide() || this.m_213877_()) {
            return false;
        } else if (damageSource0.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            this.kill();
            return false;
        } else if (this.m_6673_(damageSource0)) {
            return false;
        } else if (damageSource0.is(DamageTypeTags.IS_EXPLOSION)) {
            this.brokenByAnything(damageSource0);
            this.kill();
            return false;
        } else if (damageSource0.is(DamageTypes.IN_FIRE)) {
            if (this.m_6060_()) {
                this.causeDamage(damageSource0, 0.15F);
            } else {
                this.m_20254_(5);
            }
            return false;
        } else if (damageSource0.is(DamageTypes.ON_FIRE) && this.m_21223_() > 0.5F) {
            this.causeDamage(damageSource0, 4.0F);
            return false;
        } else {
            boolean flag = damageSource0.getDirectEntity() instanceof AbstractArrow;
            boolean flag1 = flag && ((AbstractArrow) damageSource0.getDirectEntity()).getPierceLevel() > 0;
            boolean flag2 = "player".equals(damageSource0.getMsgId());
            if (!flag2 && !flag) {
                return false;
            } else if (damageSource0.getEntity() instanceof Player && !((Player) damageSource0.getEntity()).getAbilities().mayBuild) {
                return false;
            } else if (damageSource0.isCreativePlayer()) {
                this.playBrokenSound();
                this.showBreakingParticles();
                this.kill();
                return flag1;
            } else {
                long i = this.m_9236_().getGameTime();
                if (i - this.lastHit > 5L && !flag) {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 32);
                    this.m_146852_(GameEvent.ENTITY_DAMAGE, damageSource0.getEntity());
                    this.lastHit = i;
                } else {
                    this.brokenByPlayer(damageSource0);
                    this.showBreakingParticles();
                    this.kill();
                }
                return true;
            }
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 32) {
            if (this.m_9236_().isClientSide()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ARMOR_STAND_HIT, this.m_5720_(), 0.3F, 1.0F, false);
                this.lastHit = this.m_9236_().getGameTime();
            }
        } else {
            super.handleEntityEvent(byte0);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double double0) {
        double d0 = this.m_20191_().getSize() * 4.0;
        if (Double.isNaN(d0) || d0 == 0.0) {
            d0 = 4.0;
        }
        d0 *= 64.0;
        return double0 < d0 * d0;
    }

    private void showBreakingParticles() {
        if (this.m_9236_() instanceof ServerLevel) {
            ((ServerLevel) this.m_9236_()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState()), this.m_20185_(), this.m_20227_(0.6666666666666666), this.m_20189_(), 10, (double) (this.m_20205_() / 4.0F), (double) (this.m_20206_() / 4.0F), (double) (this.m_20205_() / 4.0F), 0.05);
        }
    }

    private void causeDamage(DamageSource damageSource0, float float1) {
        float f = this.m_21223_();
        f -= float1;
        if (f <= 0.5F) {
            this.brokenByAnything(damageSource0);
            this.kill();
        } else {
            this.m_21153_(f);
            this.m_146852_(GameEvent.ENTITY_DAMAGE, damageSource0.getEntity());
        }
    }

    private void brokenByPlayer(DamageSource damageSource0) {
        Block.popResource(this.m_9236_(), this.m_20183_(), new ItemStack(Items.ARMOR_STAND));
        this.brokenByAnything(damageSource0);
    }

    private void brokenByAnything(DamageSource damageSource0) {
        this.playBrokenSound();
        this.m_6668_(damageSource0);
        for (int i = 0; i < this.armItems.size(); i++) {
            ItemStack itemstack = this.armItems.get(i);
            if (!itemstack.isEmpty()) {
                Block.popResource(this.m_9236_(), this.m_20183_().above(), itemstack);
                this.armItems.set(i, ItemStack.EMPTY);
            }
        }
        for (int j = 0; j < this.bodyItems.size(); j++) {
            ItemStack itemstack1 = this.bodyItems.get(j);
            if (!itemstack1.isEmpty()) {
                Block.popResource(this.m_9236_(), this.m_20183_().above(), itemstack1);
                this.bodyItems.set(j, ItemStack.EMPTY);
            }
        }
    }

    private void playBrokenSound() {
        this.m_9236_().playSound((Player) null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ARMOR_STAND_BREAK, this.m_5720_(), 1.0F, 1.0F);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height * (this.m_6162_() ? 0.5F : 0.9F);
    }

    @Override
    public double getMyRidingOffset() {
        return 0.1;
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
    public void kill() {
        this.m_142687_(Entity.RemovalReason.KILLED);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean skipAttackInteraction(Entity entity0) {
        return entity0 instanceof Player && !this.m_9236_().mayInteract((Player) entity0, this.m_20183_());
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
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
    public boolean attackable() {
        return false;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.ARMOR_STAND);
    }

    public static boolean checkAndConvert(ArmorStand stand, ItemStack clickedWith, Player player, InteractionHand hand) {
        if (!stand.m_9236_().isClientSide() && clickedWith.getItem() instanceof ItemConstructPart) {
            MutableBoolean allEmpty = new MutableBoolean(true);
            stand.getHandSlots().forEach(is -> {
                if (!is.isEmpty()) {
                    allEmpty.setFalse();
                }
            });
            stand.getArmorSlots().forEach(is -> {
                if (!is.isEmpty()) {
                    allEmpty.setFalse();
                }
            });
            if (!allEmpty.getValue()) {
                return false;
            } else {
                Vec3 pos = stand.m_20182_();
                ConstructAssemblyStand conStand = new ConstructAssemblyStand(stand.m_9236_(), pos.x, pos.y, pos.z);
                conStand.m_7678_(stand.m_20185_(), stand.m_20186_(), stand.m_20189_(), stand.m_146908_(), 0.0F);
                stand.m_9236_().m_7967_(conStand);
                stand.m_142687_(Entity.RemovalReason.DISCARDED);
                EquipmentSlot slot = EquipmentSlot.FEET;
                switch(((ItemConstructPart) clickedWith.getItem()).getSlot()) {
                    case HEAD:
                        slot = EquipmentSlot.HEAD;
                        break;
                    case LEFT_ARM:
                        slot = EquipmentSlot.MAINHAND;
                        break;
                    case LEGS:
                        slot = EquipmentSlot.LEGS;
                        break;
                    case RIGHT_ARM:
                        slot = EquipmentSlot.OFFHAND;
                        break;
                    case TORSO:
                        slot = EquipmentSlot.CHEST;
                }
                conStand.swapItem(player, slot, clickedWith, hand);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean onShapeTarget(ISpellDefinition spell, SpellSource source) {
        if (source.isPlayerCaster() && spell.getComponents().stream().anyMatch(c -> c.getPart() == Components.ANIMUS || c.getPart() == Components.GREATER_ANIMUS)) {
            this.summonConstruct(source.getPlayer());
            return false;
        } else {
            return true;
        }
    }
}