package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.message.MessageHurtMultipart;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EntityBoneSerpentPart extends LivingEntity implements IHurtableMultipart {

    private static final EntityDataAccessor<Boolean> TAIL = SynchedEntityData.defineId(EntityBoneSerpentPart.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> BODYINDEX = SynchedEntityData.defineId(EntityBoneSerpentPart.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Optional<UUID>> PARENT_UUID = SynchedEntityData.defineId(EntityBoneSerpentPart.class, EntityDataSerializers.OPTIONAL_UUID);

    public EntityDimensions multipartSize;

    protected float radius;

    protected float angleYaw;

    protected float offsetY;

    protected float damageMultiplier = 1.0F;

    public EntityBoneSerpentPart(EntityType t, Level world) {
        super(t, world);
        this.multipartSize = t.getDimensions();
    }

    public EntityBoneSerpentPart(EntityType t, LivingEntity parent, float radius, float angleYaw, float offsetY) {
        super(t, parent.m_9236_());
        this.setParent(parent);
        this.radius = radius;
        this.angleYaw = (angleYaw + 90.0F) * (float) (Math.PI / 180.0);
        this.offsetY = offsetY;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean startRiding(Entity entityIn) {
        return !(entityIn instanceof AbstractMinecart) && !(entityIn instanceof Boat) ? super.m_20329_(entityIn) : false;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.15F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getParentId() != null) {
            compound.putUUID("ParentUUID", this.getParentId());
        }
        compound.putBoolean("TailPart", this.isTail());
        compound.putInt("BodyIndex", this.getBodyIndex());
        compound.putFloat("PartAngle", this.angleYaw);
        compound.putFloat("PartRadius", this.radius);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("ParentUUID")) {
            this.setParentId(compound.getUUID("ParentUUID"));
        }
        this.setTail(compound.getBoolean("TailPart"));
        this.setBodyIndex(compound.getInt("BodyIndex"));
        this.angleYaw = compound.getFloat("PartAngle");
        this.radius = compound.getFloat("PartRadius");
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(PARENT_UUID, Optional.empty());
        this.f_19804_.define(TAIL, false);
        this.f_19804_.define(BODYINDEX, 0);
    }

    @Nullable
    public UUID getParentId() {
        return (UUID) this.f_19804_.get(PARENT_UUID).orElse(null);
    }

    public void setParentId(@Nullable UUID uniqueId) {
        this.f_19804_.set(PARENT_UUID, Optional.ofNullable(uniqueId));
    }

    public void setInitialPartPos(Entity parent) {
        this.m_6034_(parent.xo + (double) this.radius * Math.cos((double) (parent.getYRot() * (float) (Math.PI / 180.0) + this.angleYaw)), parent.yo + (double) this.offsetY, parent.zo + (double) this.radius * Math.sin((double) (parent.getYRot() * (float) (Math.PI / 180.0) + this.angleYaw)));
    }

    @Override
    public void tick() {
        this.f_19817_ = false;
        if (this.f_19797_ > 10) {
            Entity parent = this.getParent();
            this.m_6210_();
            if (parent != null && !this.m_9236_().isClientSide) {
                this.m_20242_(true);
                this.m_6034_(parent.xo + (double) this.radius * Math.cos((double) (parent.yRotO * (float) (Math.PI / 180.0) + this.angleYaw)), parent.yo + (double) this.offsetY, parent.zo + (double) this.radius * Math.sin((double) (parent.yRotO * (float) (Math.PI / 180.0) + this.angleYaw)));
                double d0 = parent.getX() - this.m_20185_();
                double d1 = parent.getY() - this.m_20186_();
                double d2 = parent.getZ() - this.m_20189_();
                float f2 = -((float) (Mth.atan2(d1, (double) Mth.sqrt((float) (d0 * d0 + d2 * d2))) * 180.0F / (float) Math.PI));
                this.m_146926_(this.limitAngle(this.m_146909_(), f2, 5.0F));
                this.m_5834_();
                this.m_146922_(parent.yRotO);
                this.f_20885_ = this.m_146908_();
                this.f_20883_ = this.f_19859_;
                if (parent instanceof LivingEntity && !this.m_9236_().isClientSide && (((LivingEntity) parent).hurtTime > 0 || ((LivingEntity) parent).deathTime > 0)) {
                    AlexsMobs.sendMSGToAll(new MessageHurtMultipart(this.m_19879_(), parent.getId(), 0.0F));
                    this.f_20916_ = ((LivingEntity) parent).hurtTime;
                    this.f_20919_ = ((LivingEntity) parent).deathTime;
                }
                this.pushEntities();
                if (parent.isRemoved() && !this.m_9236_().isClientSide) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            } else if (this.f_19797_ > 20 && !this.m_9236_().isClientSide) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
        super.tick();
    }

    public Entity getParent() {
        UUID id = this.getParentId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    public void setParent(Entity entity) {
        this.setParentId(entity.getUUID());
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void pushEntities() {
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2, 0.0, 0.2));
        Entity parent = this.getParent();
        if (parent != null) {
            entities.stream().filter(entity -> entity != parent && !(entity instanceof EntityBoneSerpentPart) && entity.isPushable()).forEach(entity -> entity.push(parent));
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        Entity parent = this.getParent();
        return parent != null ? parent.interact(player, hand) : InteractionResult.PASS;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity parent = this.getParent();
        boolean prev = parent != null && parent.hurt(source, damage * this.damageMultiplier);
        if (prev && !this.m_9236_().isClientSide) {
            AlexsMobs.sendMSGToAll(new MessageHurtMultipart(this.m_19879_(), parent.getId(), damage * this.damageMultiplier));
        }
        return prev;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return ImmutableList.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
    }

    public boolean isTail() {
        return this.f_19804_.get(TAIL);
    }

    public void setTail(boolean tail) {
        this.f_19804_.set(TAIL, tail);
    }

    public int getBodyIndex() {
        return this.f_19804_.get(BODYINDEX);
    }

    public void setBodyIndex(int index) {
        this.f_19804_.set(BODYINDEX, index);
    }

    public boolean shouldNotExist() {
        Entity parent = this.getParent();
        return !parent.isAlive();
    }

    @Override
    public void onAttackedFromServer(LivingEntity parent, float damage, DamageSource damageSource) {
        if (parent.deathTime > 0) {
            this.f_20919_ = parent.deathTime;
        }
        if (parent.hurtTime > 0) {
            this.f_20916_ = parent.hurtTime;
        }
    }

    public boolean shouldContinuePersisting() {
        return this.isAddedToWorld() || this.m_213877_();
    }
}