package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.message.MessageHurtMultipart;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EntityVoidWormPart extends LivingEntity implements IHurtableMultipart {

    protected static final EntityDimensions SIZE_BASE = EntityDimensions.scalable(1.2F, 1.95F);

    protected static final EntityDimensions TAIL_SIZE = EntityDimensions.scalable(1.6F, 2.0F);

    private static final EntityDataAccessor<Boolean> TAIL = SynchedEntityData.defineId(EntityVoidWormPart.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> BODYINDEX = SynchedEntityData.defineId(EntityVoidWormPart.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> WORM_SCALE = SynchedEntityData.defineId(EntityVoidWormPart.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> WORM_YAW = SynchedEntityData.defineId(EntityVoidWormPart.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> WORM_ANGLE = SynchedEntityData.defineId(EntityVoidWormPart.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Optional<UUID>> PARENT_UUID = SynchedEntityData.defineId(EntityVoidWormPart.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Optional<UUID>> CHILD_UUID = SynchedEntityData.defineId(EntityVoidWormPart.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> PORTAL_TICKS = SynchedEntityData.defineId(EntityVoidWormPart.class, EntityDataSerializers.INT);

    public EntityDimensions multipartSize;

    public float prevWormAngle;

    protected float radius;

    protected float angleYaw;

    protected float offsetY;

    protected float damageMultiplier = 1.0F;

    private float prevWormYaw = 0.0F;

    private Vec3 teleportPos = null;

    private Vec3 enterPos = null;

    private boolean doesParentControlPos = false;

    public EntityVoidWormPart(EntityType t, Level world) {
        super(t, world);
        this.multipartSize = t.getDimensions();
    }

    public EntityVoidWormPart(EntityType t, LivingEntity parent, float radius, float angleYaw, float offsetY) {
        super(t, parent.m_9236_());
        this.setParent(parent);
        this.radius = radius;
        this.angleYaw = (angleYaw + 90.0F) * (float) (Math.PI / 180.0);
        this.offsetY = offsetY;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.15F);
    }

    @Override
    public void push(Entity entityIn) {
    }

    @Override
    public void kill() {
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isTail() ? TAIL_SIZE.scale(this.getScale()) : super.getDimensions(poseIn);
    }

    public float getWormScale() {
        return this.f_19804_.get(WORM_SCALE);
    }

    public void setWormScale(float scale) {
        this.f_19804_.set(WORM_SCALE, scale);
    }

    @Override
    public float getScale() {
        return this.getWormScale() + 0.5F;
    }

    @Override
    public boolean startRiding(Entity entityIn) {
        return !(entityIn instanceof AbstractMinecart) && !(entityIn instanceof Boat) ? super.m_20329_(entityIn) : false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.FELL_OUT_OF_WORLD) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.LAVA) || source.is(DamageTypeTags.IS_FIRE) || super.m_6673_(source);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getParentId() != null) {
            compound.putUUID("ParentUUID", this.getParentId());
        }
        if (this.getChildId() != null) {
            compound.putUUID("ChildUUID", this.getChildId());
        }
        compound.putBoolean("TailPart", this.isTail());
        compound.putInt("BodyIndex", this.getBodyIndex());
        compound.putInt("PortalTicks", this.getPortalTicks());
        compound.putFloat("PartAngle", this.angleYaw);
        compound.putFloat("WormScale", this.getWormScale());
        compound.putFloat("PartRadius", this.radius);
        compound.putFloat("PartYOffset", this.offsetY);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("ParentUUID")) {
            this.setParentId(compound.getUUID("ParentUUID"));
        }
        if (compound.hasUUID("ChildUUID")) {
            this.setChildId(compound.getUUID("ChildUUID"));
        }
        this.setTail(compound.getBoolean("TailPart"));
        this.setBodyIndex(compound.getInt("BodyIndex"));
        this.setPortalTicks(compound.getInt("PortalTicks"));
        this.angleYaw = compound.getFloat("PartAngle");
        this.setWormScale(compound.getFloat("WormScale"));
        this.radius = compound.getFloat("PartRadius");
        this.offsetY = compound.getFloat("PartYOffset");
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(PARENT_UUID, Optional.empty());
        this.f_19804_.define(CHILD_UUID, Optional.empty());
        this.f_19804_.define(TAIL, false);
        this.f_19804_.define(BODYINDEX, 0);
        this.f_19804_.define(WORM_SCALE, 1.0F);
        this.f_19804_.define(WORM_YAW, 0.0F);
        this.f_19804_.define(WORM_ANGLE, 0.0F);
        this.f_19804_.define(PORTAL_TICKS, 0);
    }

    @Nullable
    public UUID getParentId() {
        return (UUID) this.f_19804_.get(PARENT_UUID).orElse(null);
    }

    public void setParentId(@Nullable UUID uniqueId) {
        this.f_19804_.set(PARENT_UUID, Optional.ofNullable(uniqueId));
    }

    @Nullable
    public UUID getChildId() {
        return (UUID) this.f_19804_.get(CHILD_UUID).orElse(null);
    }

    public void setChildId(@Nullable UUID uniqueId) {
        this.f_19804_.set(CHILD_UUID, Optional.ofNullable(uniqueId));
    }

    public void setInitialPartPos(Entity parent) {
        this.m_6034_(parent.xo + (double) this.radius * Math.cos((double) parent.getYRot() * (Math.PI / 180.0) + (double) this.angleYaw), parent.yo + (double) this.offsetY, parent.zo + (double) this.radius * Math.sin((double) parent.getYRot() * (Math.PI / 180.0) + (double) this.angleYaw));
    }

    public float getWormAngle() {
        return this.f_19804_.get(WORM_ANGLE);
    }

    public void setWormAngle(float progress) {
        this.f_19804_.set(WORM_ANGLE, progress);
    }

    public int getPortalTicks() {
        return this.f_19804_.get(PORTAL_TICKS);
    }

    public void setPortalTicks(int ticks) {
        this.f_19804_.set(PORTAL_TICKS, ticks);
    }

    @Override
    public void tick() {
        this.f_19817_ = false;
        this.prevWormAngle = this.getWormAngle();
        this.prevWormYaw = this.f_19804_.get(WORM_YAW);
        this.m_20256_(Vec3.ZERO);
        this.radius = 1.0F + this.getWormScale() * (this.isTail() ? 0.65F : 0.3F) + (this.getBodyIndex() == 0 ? 0.8F : 0.0F);
        if (this.f_19797_ > 3) {
            Entity parent = this.getParent();
            this.m_6210_();
            if (parent != null && !this.m_9236_().isClientSide) {
                this.m_20242_(true);
                Vec3 parentVec = parent.position().subtract(parent.xo, parent.yo, parent.zo);
                double restrictRadius = Mth.clamp((double) this.radius - parentVec.lengthSqr() * 0.25, (double) (this.radius * 0.5F), (double) this.radius);
                if (parent instanceof EntityVoidWorm) {
                    restrictRadius *= (double) (this.isTail() ? 0.8F : 0.4F);
                }
                double x = parent.getX() + restrictRadius * Math.cos((double) parent.getYRot() * (Math.PI / 180.0) + (double) this.angleYaw);
                double yStretch = Math.abs(parent.getY() - parent.yo) > (double) this.m_20205_() ? parent.getY() : parent.yo;
                double y = yStretch + (double) (this.offsetY * this.getWormScale());
                double z = parent.getZ() + restrictRadius * Math.sin((double) parent.getYRot() * (Math.PI / 180.0) + (double) this.angleYaw);
                double d0 = parent.xo - this.m_20185_();
                double d1 = parent.yo - this.m_20186_();
                double d2 = parent.zo - this.m_20189_();
                float yaw = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                float pitch = parent.getXRot();
                if (this.getPortalTicks() <= 1 && !this.doesParentControlPos) {
                    float f2 = -((float) (Mth.atan2(d1, (double) Mth.sqrt((float) (d0 * d0 + d2 * d2))) * 180.0F / (float) Math.PI));
                    this.m_6034_(x, y, z);
                    this.m_146926_(this.limitAngle(this.m_146909_(), f2, 5.0F));
                    this.m_146922_(yaw);
                    this.f_19804_.set(WORM_YAW, this.m_146908_());
                }
                this.m_5834_();
                this.f_20885_ = this.m_146908_();
                this.f_20883_ = pitch;
                if (parent instanceof LivingEntity && !this.m_9236_().isClientSide && (((LivingEntity) parent).hurtTime > 0 || ((LivingEntity) parent).deathTime > 0)) {
                    AlexsMobs.sendMSGToAll(new MessageHurtMultipart(this.m_19879_(), parent.getId(), 0.0F));
                    this.f_20916_ = ((LivingEntity) parent).hurtTime;
                    this.f_20919_ = ((LivingEntity) parent).deathTime;
                }
                this.pushEntities();
                if (parent.isRemoved() && !this.m_9236_().isClientSide) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
                if (parent instanceof EntityVoidWorm) {
                    this.setWormAngle(((EntityVoidWorm) parent).prevWormAngle);
                } else if (parent instanceof EntityVoidWormPart) {
                    this.setWormAngle(((EntityVoidWormPart) parent).prevWormAngle);
                }
            } else if (this.f_19797_ > 20 && !this.m_9236_().isClientSide) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
        if (this.f_19797_ % 400 == 0) {
            this.m_5634_(1.0F);
        }
        super.tick();
        if (this.doesParentControlPos && this.enterPos != null) {
            this.m_6021_(this.enterPos.x, this.enterPos.y, this.enterPos.z);
        }
        if (this.getPortalTicks() > 0) {
            this.setPortalTicks(this.getPortalTicks() - 1);
            if (this.getPortalTicks() <= 5 && this.teleportPos != null) {
                Vec3 vec = this.teleportPos;
                this.m_6021_(vec.x, vec.y, vec.z);
                this.f_19790_ = vec.x;
                this.f_19791_ = vec.y;
                this.f_19792_ = vec.z;
                if (this.getPortalTicks() == 5 && this.getChild() instanceof EntityVoidWormPart) {
                    ((EntityVoidWormPart) this.getChild()).teleportTo(this.enterPos, this.teleportPos);
                }
                this.teleportPos = null;
            } else if (this.getPortalTicks() > 5 && this.enterPos != null) {
                this.m_6021_(this.enterPos.x, this.enterPos.y, this.enterPos.z);
            }
            if (this.getPortalTicks() == 0) {
                this.doesParentControlPos = false;
            }
        }
    }

    @Override
    protected void tickDeath() {
        this.f_20919_++;
        if (this.f_20919_ == 20) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            for (int i = 0; i < 30; i++) {
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(AMParticleRegistry.WORM_PORTAL.get(), this.m_20208_(1.0), this.m_20187_(), this.m_20262_(1.0), d0, d1, d2);
            }
        }
    }

    @Override
    public void die(DamageSource cause) {
        EntityVoidWorm worm = this.getWorm();
        if (worm != null) {
            int segments = Math.max(worm.getSegmentCount() / 2 - 1, 1);
            worm.setSegmentCount(segments);
            if (this.getChild() instanceof EntityVoidWormPart) {
                EntityVoidWormPart segment = (EntityVoidWormPart) this.getChild();
                EntityVoidWorm worm2 = AMEntityRegistry.VOID_WORM.get().create(this.m_9236_());
                worm2.m_20359_(this);
                segment.m_20359_(this);
                worm2.setChildId(segment.m_20148_());
                worm2.setSegmentCount(segments);
                segment.setParent(worm2);
                if (!this.m_9236_().isClientSide) {
                    this.m_9236_().m_7967_(worm2);
                }
                worm2.setSplitter(true);
                worm2.setMaxHealth((double) (worm.m_21233_() / 2.0F), true);
                worm2.setSplitFromUuid(worm.m_20148_());
                worm2.setWormSpeed((float) Mth.clamp((double) worm.getWormSpeed() * 0.8, 0.4F, 1.0));
                worm2.resetWormScales();
                if (!this.m_9236_().isClientSide && cause != null && cause.getEntity() instanceof ServerPlayer) {
                    AMAdvancementTriggerRegistry.VOID_WORM_SPLIT.trigger((ServerPlayer) cause.getEntity());
                }
            }
            worm.resetWormScales();
        }
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        EntityVoidWorm worm = this.getWorm();
        return super.m_7307_(entityIn) || worm != null && worm.isAlliedTo(entityIn);
    }

    public EntityVoidWorm getWorm() {
        Entity parent = this.getParent();
        while (parent instanceof EntityVoidWormPart) {
            parent = ((EntityVoidWormPart) parent).getParent();
        }
        return parent instanceof EntityVoidWorm ? (EntityVoidWorm) parent : null;
    }

    public Entity getChild() {
        UUID id = this.getChildId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
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
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2F, 0.0, 0.2F));
        Entity parent = this.getParent();
        if (parent != null) {
            entities.stream().filter(entity -> !entity.is(parent) && !(entity instanceof EntityVoidWormPart) && entity.isPushable()).forEach(entity -> entity.push(parent));
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        Entity parent = this.getParent();
        return parent != null ? parent.interact(player, hand) : InteractionResult.PASS;
    }

    public boolean isHurt() {
        return (double) this.m_21223_() <= this.getHealthThreshold();
    }

    public double getHealthThreshold() {
        return 5.0;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (super.hurt(source, damage)) {
            EntityVoidWorm worm = this.getWorm();
            if (worm != null) {
                worm.playHurtSoundWorm(source);
            }
            return true;
        } else {
            return false;
        }
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

    public float getWormYaw(float partialTicks) {
        return partialTicks == 0.0F ? this.f_19804_.get(WORM_YAW) : this.prevWormYaw + (this.f_19804_.get(WORM_YAW) - this.prevWormYaw) * partialTicks;
    }

    public void teleportTo(Vec3 enterPos, Vec3 to) {
        this.setPortalTicks(10);
        this.teleportPos = to;
        this.enterPos = enterPos;
        EntityVoidWorm worm = this.getWorm();
        if (worm != null && this.getChild() == null) {
            worm.fullyThrough = true;
        }
    }
}