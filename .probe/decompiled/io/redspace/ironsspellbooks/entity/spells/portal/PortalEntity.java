package io.redspace.ironsspellbooks.entity.spells.portal;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.PortalManager;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class PortalEntity extends Entity implements AntiMagicSusceptible {

    private static final EntityDataAccessor<Optional<UUID>> DATA_ID_OWNER_UUID = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Boolean> DATA_PORTAL_CONNECTED = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BOOLEAN);

    private long ticksToLive = 0L;

    private boolean isPortalConnected = false;

    public PortalEntity(Level level, PortalData portalData) {
        this(EntityRegistry.PORTAL.get(), level);
        PortalManager.INSTANCE.addPortalData(this.f_19820_, portalData);
        this.ticksToLive = (long) portalData.ticksToLive;
    }

    public PortalEntity(EntityType<PortalEntity> portalEntityEntityType, Level level) {
        super(portalEntityEntityType, level);
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        if (!this.f_19853_.isClientSide) {
            this.m_146870_();
        }
    }

    public void onRemovedFromWorld() {
        if (!this.f_19853_.isClientSide) {
            Entity.RemovalReason removalReason = this.m_146911_();
            if (removalReason != null && removalReason.shouldDestroy()) {
                PortalManager.INSTANCE.killPortal(this.f_19820_, this.getOwnerUUID());
            }
            MagicManager.spawnParticles(this.f_19853_, new DustParticleOptions(new Vector3f(0.5F, 0.05F, 0.6F), 1.5F), this.m_20185_(), this.m_20186_(), this.m_20189_(), 25, 0.4, 0.8, 0.4, 0.03, false);
            super.onRemovedFromWorld();
        }
    }

    public void checkForEntitiesToTeleport() {
        if (!this.f_19853_.isClientSide) {
            this.f_19853_.getEntities((Entity) null, this.m_20191_(), entity -> !entity.getType().is(ModTags.CANT_USE_PORTAL) && (entity.isPickable() || entity instanceof Projectile) && !entity.isVehicle() && !entity.isSpectator()).forEach(entity -> {
                PortalManager.INSTANCE.processDelayCooldown(this.f_19820_, entity.getUUID(), 1);
                if (PortalManager.INSTANCE.canUsePortal(this, entity)) {
                    PortalManager.INSTANCE.addPortalCooldown(entity, this.f_19820_);
                    PortalData portalData = PortalManager.INSTANCE.getPortalData(this);
                    portalData.getConnectedPortalPos(this.f_19820_).ifPresent(portalPos -> {
                        Vec3 destination = portalPos.pos().add(0.0, entity.getY() - this.m_20186_(), 0.0);
                        entity.setYRot(portalPos.rotation());
                        this.f_19853_.playSound(null, this.m_20183_(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        if (this.f_19853_.dimension().equals(portalPos.dimension())) {
                            entity.teleportTo(destination.x, destination.y + 0.1, destination.z);
                            Vec3 delta = entity.getDeltaMovement();
                            float hspeed = (float) Math.sqrt(delta.x * delta.x + delta.z * delta.z);
                            float f = portalPos.rotation() * (float) (Math.PI / 180.0);
                            entity.setDeltaMovement((double) (-Mth.sin(f) * hspeed), delta.y, (double) (Mth.cos(f) * hspeed));
                        } else {
                            MinecraftServer server = this.f_19853_.getServer();
                            if (server != null) {
                                ServerLevel dim = server.getLevel(portalPos.dimension());
                                if (dim != null) {
                                    entity.changeDimension(dim, new PortalTeleporter(destination));
                                }
                            }
                        }
                        this.f_19853_.playSound(null, destination.x, destination.y, destination.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    });
                }
            });
        }
    }

    private Vec3 getDestinationPosition(PortalPos globalPos, Entity entity) {
        Vec3 offset = new Vec3(this.m_20185_() - entity.getX(), this.m_20186_() - entity.getY(), this.m_20189_() - entity.getZ());
        return new Vec3(globalPos.pos().x - offset.x, globalPos.pos().y - offset.y, globalPos.pos().z - offset.z);
    }

    public void setTicksToLive(int ticksToLive) {
        this.ticksToLive = (long) ticksToLive;
    }

    @Override
    public void tick() {
        if (!this.f_19853_.isClientSide) {
            PortalManager.INSTANCE.processCooldownTick(this.f_19820_, -1);
            this.checkForEntitiesToTeleport();
            if (--this.ticksToLive <= 0L) {
                this.m_146870_();
            }
        } else {
            Vec3 center = this.m_20191_().getCenter();
            for (int i = 0; i < 2; i++) {
                this.f_19853_.addParticle(ParticleHelper.PORTAL_FRAME, center.x, center.y, center.z, 1.0, 2.1F, (double) this.m_146908_());
            }
        }
    }

    public void spawnParticles(float radius, Vector3f color) {
        int particles = 40;
        float step = 6.28F / (float) particles;
        Vec3 center = this.m_20191_().getCenter();
        for (int i = 0; i < particles; i++) {
            float x = Mth.cos((float) i * step) * radius;
            float y = Mth.sin((float) i * step) * radius * 2.0F;
            Vec3 offset = new Vec3((double) x, (double) y, 0.0).yRot(-this.m_146908_() * (float) (Math.PI / 180.0));
            this.f_19853_.addParticle(ParticleHelper.UNSTABLE_ENDER, true, center.x + offset.x, center.y + offset.y, center.z + offset.z, 0.0, 0.0, 0.0);
        }
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.f_19804_.set(DATA_ID_OWNER_UUID, Optional.ofNullable(uuid));
    }

    public UUID getOwnerUUID() {
        return (UUID) this.f_19804_.get(DATA_ID_OWNER_UUID).orElseGet(() -> (UUID) this.f_19804_.get(DATA_ID_OWNER_UUID).orElse(null));
    }

    public void setPortalConnected() {
        this.f_19804_.set(DATA_PORTAL_CONNECTED, true);
    }

    public boolean getPortalConnected() {
        return this.f_19804_.get(DATA_PORTAL_CONNECTED);
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DATA_ID_OWNER_UUID, Optional.empty());
        this.f_19804_.define(DATA_PORTAL_CONNECTED, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (this.f_19853_.isClientSide) {
            if (pKey.getId() == DATA_PORTAL_CONNECTED.getId()) {
                this.isPortalConnected = this.getPortalConnected();
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("ownerUUID")) {
            this.setOwnerUUID(compoundTag.getUUID("ownerUUID"));
        }
        if (compoundTag.contains("ticksToLive")) {
            this.ticksToLive = compoundTag.getLong("ticksToLive");
        }
        PortalData portalData = PortalManager.INSTANCE.getPortalData(this);
        if (portalData == null) {
            this.ticksToLive = 0L;
        } else if (portalData.portalEntityId1 != null && portalData.portalEntityId2 != null) {
            this.setPortalConnected();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putLong("ticksToLive", this.ticksToLive);
        compoundTag.putUUID("ownerUUID", this.getOwnerUUID());
    }
}