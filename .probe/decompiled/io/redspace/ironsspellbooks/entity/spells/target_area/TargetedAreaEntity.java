package io.redspace.ironsspellbooks.entity.spells.target_area;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

public class TargetedAreaEntity extends Entity {

    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(TargetedAreaEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(TargetedAreaEntity.class, EntityDataSerializers.INT);

    @Nullable
    private UUID ownerUUID;

    @Nullable
    private Entity cachedOwner;

    boolean hasOwner;

    private int duration;

    public void setOwner(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.ownerUUID = pOwner.getUUID();
            this.cachedOwner = pOwner;
            this.hasOwner = true;
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && this.cachedOwner.isAlive()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.f_19853_ instanceof ServerLevel serverLevel) {
            this.cachedOwner = serverLevel.getEntity(this.ownerUUID);
            if (serverLevel.getEntity(this.ownerUUID) instanceof LivingEntity livingEntity) {
                this.cachedOwner = livingEntity;
            }
            return this.cachedOwner;
        } else {
            return null;
        }
    }

    public TargetedAreaEntity(EntityType<TargetedAreaEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setRadius(3.0F);
        this.f_19794_ = true;
        this.m_20242_(true);
    }

    public static TargetedAreaEntity createTargetAreaEntity(Level level, Vec3 center, float radius, int color) {
        TargetedAreaEntity targetedAreaEntity = new TargetedAreaEntity(level, radius, color);
        targetedAreaEntity.m_146884_(center);
        level.m_7967_(targetedAreaEntity);
        return targetedAreaEntity;
    }

    public static TargetedAreaEntity createTargetAreaEntity(Level level, Vec3 center, float radius, int color, Entity owner) {
        TargetedAreaEntity targetedAreaEntity = new TargetedAreaEntity(level, radius, color);
        targetedAreaEntity.m_146884_(center);
        targetedAreaEntity.setOwner(owner);
        level.m_7967_(targetedAreaEntity);
        return targetedAreaEntity;
    }

    @Override
    public void tick() {
        this.f_19803_ = false;
        Entity owner = this.getOwner();
        if (owner != null) {
            this.m_146884_(owner.position());
            this.f_19790_ = owner.xOld;
            this.f_19791_ = owner.yOld;
            this.f_19792_ = owner.zOld;
            this.f_19854_ = owner.xo;
            this.f_19855_ = owner.yo;
            this.f_19856_ = owner.zo;
        }
        if (!this.f_19853_.isClientSide && (this.duration > 0 && this.f_19797_ > this.duration || this.duration == 0 && this.f_19797_ > 400 || this.hasOwner && (owner == null || owner.isRemoved()))) {
            this.m_146870_();
        }
    }

    public TargetedAreaEntity(Level level, float radius, int color) {
        this(EntityRegistry.TARGET_AREA_ENTITY.get(), level);
        this.setRadius(radius);
        this.setColor(color);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.8F);
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(DATA_RADIUS, 2.0F);
        this.m_20088_().define(DATA_COLOR, 16777215);
    }

    public void setRadius(float pRadius) {
        if (!this.f_19853_.isClientSide) {
            this.m_20088_().set(DATA_RADIUS, Mth.clamp(pRadius, 0.0F, 32.0F));
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getRadius() {
        return this.m_20088_().get(DATA_RADIUS);
    }

    public void setColor(int color) {
        if (!this.f_19853_.isClientSide) {
            this.m_20088_().set(DATA_COLOR, color);
        }
    }

    public Vector3f getColor() {
        return Utils.deconstructRGB(this.m_20088_().get(DATA_COLOR));
    }

    public int getColorRaw() {
        return this.m_20088_().get(DATA_COLOR);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
            if (this.getRadius() < 0.1F) {
                this.m_146870_();
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.m_20185_();
        double d1 = this.m_20186_();
        double d2 = this.m_20189_();
        super.refreshDimensions();
        this.m_6034_(d0, d1, d2);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Radius", this.getRadius());
        tag.putInt("Color", this.getColorRaw());
        tag.putInt("Age", this.f_19797_);
        if (this.duration > 0) {
            tag.putInt("Duration", this.duration);
        }
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setRadius(tag.getFloat("Radius"));
        this.setColor(tag.getInt("Color"));
        this.f_19797_ = tag.getInt("Age");
        if (tag.contains("Duration")) {
            this.duration = tag.getInt("Duration");
        }
        if (tag.contains("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
            this.hasOwner = true;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        Entity entity = this.f_19853_.getEntity(pPacket.getData());
        if (entity != null) {
            this.setOwner(entity);
        }
    }
}