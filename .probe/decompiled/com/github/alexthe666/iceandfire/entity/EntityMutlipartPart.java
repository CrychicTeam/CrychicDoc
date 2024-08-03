package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageMultipartInteract;
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
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public abstract class EntityMutlipartPart extends Entity {

    private static final EntityDataAccessor<Optional<UUID>> PARENT_UUID = SynchedEntityData.defineId(EntityMutlipartPart.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Float> SCALE_WIDTH = SynchedEntityData.defineId(EntityMutlipartPart.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> SCALE_HEIGHT = SynchedEntityData.defineId(EntityMutlipartPart.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> PART_YAW = SynchedEntityData.defineId(EntityMutlipartPart.class, EntityDataSerializers.FLOAT);

    public EntityDimensions multipartSize;

    protected float radius;

    protected float angleYaw;

    protected float offsetY;

    protected float damageMultiplier;

    protected EntityMutlipartPart(EntityType<?> t, Level world) {
        super(t, world);
        this.multipartSize = t.getDimensions();
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
    }

    @Override
    protected void doWaterSplashEffect() {
    }

    public EntityMutlipartPart(EntityType<?> t, Entity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(t, parent.level());
        this.setParent(parent);
        this.setScaleX(sizeX);
        this.setScaleY(sizeY);
        this.radius = radius;
        this.angleYaw = (angleYaw + 90.0F) * (float) (Math.PI / 180.0);
        this.offsetY = offsetY;
        this.damageMultiplier = damageMultiplier;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0).add(Attributes.MOVEMENT_SPEED, 0.1);
    }

    @NotNull
    @Override
    public EntityDimensions getDimensions(@NotNull Pose poseIn) {
        return new EntityDimensions(this.getScaleX(), this.getScaleY(), false);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(PARENT_UUID, Optional.empty());
        this.f_19804_.define(SCALE_WIDTH, 0.5F);
        this.f_19804_.define(SCALE_HEIGHT, 0.5F);
        this.f_19804_.define(PART_YAW, 0.0F);
    }

    @Nullable
    public UUID getParentId() {
        return (UUID) this.f_19804_.get(PARENT_UUID).orElse(null);
    }

    public void setParentId(@Nullable UUID uniqueId) {
        this.f_19804_.set(PARENT_UUID, Optional.ofNullable(uniqueId));
    }

    private float getScaleX() {
        return this.f_19804_.get(SCALE_WIDTH);
    }

    private void setScaleX(float scale) {
        this.f_19804_.set(SCALE_WIDTH, scale);
    }

    private float getScaleY() {
        return this.f_19804_.get(SCALE_HEIGHT);
    }

    private void setScaleY(float scale) {
        this.f_19804_.set(SCALE_HEIGHT, scale);
    }

    public float getPartYaw() {
        return this.f_19804_.get(PART_YAW);
    }

    private void setPartYaw(float yaw) {
        this.f_19804_.set(PART_YAW, yaw % 360.0F);
    }

    @Override
    public void tick() {
        this.f_19798_ = false;
        if (this.f_19797_ > 10) {
            Entity parent = this.getParent();
            this.m_6210_();
            if (parent != null && !this.m_9236_().isClientSide) {
                float renderYawOffset = parent.getYRot();
                if (parent instanceof LivingEntity) {
                    renderYawOffset = ((LivingEntity) parent).yBodyRot;
                }
                if (this.isSlowFollow()) {
                    this.m_6034_(parent.xo + (double) (this.radius * Mth.cos((float) ((double) renderYawOffset * (Math.PI / 180.0) + (double) this.angleYaw))), parent.yo + (double) this.offsetY, parent.zo + (double) (this.radius * Mth.sin((float) ((double) renderYawOffset * (Math.PI / 180.0) + (double) this.angleYaw))));
                    double d0 = parent.getX() - this.m_20185_();
                    double d1 = parent.getY() - this.m_20186_();
                    double d2 = parent.getZ() - this.m_20189_();
                    float f2 = -((float) (Mth.atan2(d1, (double) Mth.sqrt((float) (d0 * d0 + d2 * d2))) * 180.0F / (float) Math.PI));
                    this.m_146926_(this.limitAngle(this.m_146909_(), f2, 5.0F));
                    this.m_5834_();
                    this.m_146922_(renderYawOffset);
                    this.setPartYaw(this.m_146908_());
                    if (!this.m_9236_().isClientSide) {
                        this.collideWithNearbyEntities();
                    }
                } else {
                    this.m_6034_(parent.getX() + (double) (this.radius * Mth.cos((float) ((double) renderYawOffset * (Math.PI / 180.0) + (double) this.angleYaw))), parent.getY() + (double) this.offsetY, parent.getZ() + (double) (this.radius * Mth.sin((float) ((double) renderYawOffset * (Math.PI / 180.0) + (double) this.angleYaw))));
                    this.m_5834_();
                }
                if (!this.m_9236_().isClientSide) {
                    this.collideWithNearbyEntities();
                }
                if (parent.isRemoved() && !this.m_9236_().isClientSide) {
                    this.remove(Entity.RemovalReason.DISCARDED);
                }
            } else if (this.f_19797_ > 20 && !this.m_9236_().isClientSide) {
                this.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        super.tick();
    }

    protected boolean isSlowFollow() {
        return false;
    }

    protected float limitAngle(float sourceAngle, float targetAngle, float maximumChange) {
        float f = Mth.wrapDegrees(targetAngle - sourceAngle);
        if (f > maximumChange) {
            f = maximumChange;
        }
        if (f < -maximumChange) {
            f = -maximumChange;
        }
        float f1 = sourceAngle + f;
        if (f1 < 0.0F) {
            f1 += 360.0F;
        } else if (f1 > 360.0F) {
            f1 -= 360.0F;
        }
        return f1;
    }

    @Override
    public void remove(@NotNull Entity.RemovalReason reason) {
        super.remove(Entity.RemovalReason.DISCARDED);
    }

    public Entity getParent() {
        UUID id = this.getParentId();
        return id != null && this.m_9236_() instanceof ServerLevel serverLevel ? serverLevel.getEntity(id) : null;
    }

    public void setParent(Entity entity) {
        this.setParentId(entity.getUUID());
    }

    @Override
    public boolean is(@NotNull Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void collideWithNearbyEntities() {
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2F, 0.0, 0.2F));
        Entity parent = this.getParent();
        if (parent != null) {
            entities.stream().filter(entity -> entity != parent && !sharesRider(parent, entity) && !(entity instanceof EntityMutlipartPart) && entity.isPushable()).forEach(entity -> entity.push(parent));
        }
    }

    public static boolean sharesRider(Entity parent, Entity entityIn) {
        for (Entity entity : parent.getPassengers()) {
            if (entity.equals(entityIn)) {
                return true;
            }
            if (sharesRider(entity, entityIn)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        Entity parent = this.getParent();
        if (this.m_9236_().isClientSide && parent != null) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageMultipartInteract(parent.getId(), 0.0F));
        }
        return parent != null ? parent.interact(player, hand) : InteractionResult.PASS;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float damage) {
        Entity parent = this.getParent();
        if (this.m_9236_().isClientSide && source.getEntity() instanceof Player && parent != null) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageMultipartInteract(parent.getId(), damage * this.damageMultiplier));
        }
        return parent != null && parent.hurt(source, damage * this.damageMultiplier);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.FALLING_BLOCK) || source.is(DamageTypes.LAVA) || source.is(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(source);
    }

    public boolean shouldContinuePersisting() {
        return this.isAddedToWorld() || this.m_213877_();
    }
}