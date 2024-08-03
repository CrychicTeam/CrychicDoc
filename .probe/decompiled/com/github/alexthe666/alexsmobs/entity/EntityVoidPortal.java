package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.event.ServerEvents;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.item.ItemDimensionalCarver;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.antlr.v4.runtime.misc.Triple;

public class EntityVoidPortal extends Entity {

    protected static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(EntityVoidPortal.class, EntityDataSerializers.DIRECTION);

    protected static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(EntityVoidPortal.class, EntityDataSerializers.INT);

    protected static final EntityDataAccessor<Boolean> SHATTERED = SynchedEntityData.defineId(EntityVoidPortal.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockPos>> DESTINATION = SynchedEntityData.defineId(EntityVoidPortal.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Optional<UUID>> SISTER_UUID = SynchedEntityData.defineId(EntityVoidPortal.class, EntityDataSerializers.OPTIONAL_UUID);

    public ResourceKey<Level> exitDimension;

    private boolean madeOpenNoise = false;

    private boolean madeCloseNoise = false;

    private boolean isDummy = false;

    private boolean hasClearedObstructions;

    public EntityVoidPortal(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntityVoidPortal(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.VOID_PORTAL.get(), world);
    }

    public EntityVoidPortal(Level world, ItemDimensionalCarver item) {
        this(AMEntityRegistry.VOID_PORTAL.get(), world);
        if (item == AMItemRegistry.SHATTERED_DIMENSIONAL_CARVER.get()) {
            this.setShattered(true);
            this.setLifespan(2000);
        } else {
            this.setShattered(false);
            this.setLifespan(1200);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_19797_ == 1 && this.getLifespan() == 0) {
            this.setLifespan(2000);
        }
        if (!this.madeOpenNoise) {
            this.m_146850_(GameEvent.ENTITY_PLACE);
            this.m_5496_(AMSoundRegistry.VOID_PORTAL_OPEN.get(), 1.0F, 1.0F + this.f_19796_.nextFloat() * 0.2F);
            this.madeOpenNoise = true;
        }
        Direction direction2 = this.getAttachmentFacing().getOpposite();
        float minX = -0.15F;
        float minY = -0.15F;
        float minZ = -0.15F;
        float maxX = 0.15F;
        float maxY = 0.15F;
        float maxZ = 0.15F;
        switch(direction2) {
            case NORTH:
            case SOUTH:
                minX = -1.5F;
                maxX = 1.5F;
                minY = -1.5F;
                maxY = 1.5F;
                break;
            case EAST:
            case WEST:
                minZ = -1.5F;
                maxZ = 1.5F;
                minY = -1.5F;
                maxY = 1.5F;
                break;
            case UP:
            case DOWN:
                minX = -1.5F;
                maxX = 1.5F;
                minZ = -1.5F;
                maxZ = 1.5F;
        }
        AABB bb = new AABB(this.m_20185_() + (double) minX, this.m_20186_() + (double) minY, this.m_20189_() + (double) minZ, this.m_20185_() + (double) maxX, this.m_20186_() + (double) maxY, this.m_20189_() + (double) maxZ);
        this.m_20011_(bb);
        if (this.m_9236_().isClientSide && this.f_19796_.nextFloat() < 0.5F && Math.min(this.f_19797_, this.getLifespan()) >= 20) {
            double particleX = this.m_20191_().minX + (double) this.f_19796_.nextFloat() * (this.m_20191_().maxX - this.m_20191_().minX);
            double particleY = this.m_20191_().minY + (double) this.f_19796_.nextFloat() * (this.m_20191_().maxY - this.m_20191_().minY);
            double particleZ = this.m_20191_().minZ + (double) this.f_19796_.nextFloat() * (this.m_20191_().maxZ - this.m_20191_().minZ);
            this.m_9236_().addParticle(AMParticleRegistry.WORM_PORTAL.get(), particleX, particleY, particleZ, 0.1 * this.f_19796_.nextGaussian(), 0.1 * this.f_19796_.nextGaussian(), 0.1 * this.f_19796_.nextGaussian());
        }
        List<Entity> entities = new ArrayList();
        entities.addAll(this.m_9236_().m_45933_(this, bb.deflate(0.2F)));
        entities.addAll(this.m_9236_().m_45976_(EntityVoidWorm.class, bb.inflate(1.5)));
        if (!this.m_9236_().isClientSide) {
            MinecraftServer server = this.m_9236_().getServer();
            if (this.getDestination() != null && this.getLifespan() > 20 && this.f_19797_ > 20) {
                BlockPos offsetPos = this.getDestination().relative(this.getAttachmentFacing().getOpposite(), 2);
                for (Entity e : entities) {
                    if (!e.isOnPortalCooldown() && !e.isShiftKeyDown() && !(e instanceof EntityVoidPortal) && e.getParts() == null && !(e instanceof PartEntity) && !e.getType().is(AMTagRegistry.VOID_PORTAL_IGNORES)) {
                        if (e instanceof EntityVoidWormPart) {
                            if (this.getLifespan() < 22) {
                                this.setLifespan(this.getLifespan() + 1);
                            }
                        } else if (e instanceof EntityVoidWorm) {
                            ((EntityVoidWorm) e).teleportTo(Vec3.atCenterOf(this.getDestination()));
                            e.setPortalCooldown();
                            ((EntityVoidWorm) e).resetPortalLogic();
                        } else {
                            boolean flag = true;
                            if (this.exitDimension != null) {
                                ServerLevel dimWorld = server.getLevel(this.exitDimension);
                                if (dimWorld != null && this.m_9236_().dimension() != this.exitDimension) {
                                    this.teleportEntityFromDimension(e, dimWorld, offsetPos, true);
                                    flag = false;
                                }
                            }
                            if (flag) {
                                e.teleportToWithTicket((double) ((float) offsetPos.m_123341_() + 0.5F), (double) ((float) offsetPos.m_123342_() + 0.5F), (double) ((float) offsetPos.m_123343_() + 0.5F));
                                e.setPortalCooldown();
                            }
                        }
                    }
                }
            }
        }
        this.setLifespan(this.getLifespan() - 1);
        if (this.getLifespan() <= 20 && !this.madeCloseNoise) {
            this.m_146850_(GameEvent.ENTITY_PLACE);
            this.m_5496_(AMSoundRegistry.VOID_PORTAL_CLOSE.get(), 1.0F, 1.0F + this.f_19796_.nextFloat() * 0.2F);
            this.madeCloseNoise = true;
        }
        if (this.getLifespan() <= 0) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (this.f_19797_ > 1) {
            this.clearObstructions();
        }
    }

    private void teleportEntityFromDimension(Entity entity, ServerLevel endpointWorld, BlockPos endpoint, boolean b) {
        if (entity instanceof ServerPlayer) {
            ServerEvents.teleportPlayers.add(new Triple((ServerPlayer) entity, endpointWorld, endpoint));
            if (this.getSisterId() == null) {
                this.createAndSetSister(endpointWorld, Direction.DOWN);
            }
        } else {
            entity.unRide();
            entity.setLevel(endpointWorld);
            Entity teleportedEntity = entity.getType().create(endpointWorld);
            if (teleportedEntity != null) {
                teleportedEntity.restoreFrom(entity);
                teleportedEntity.moveTo((double) endpoint.m_123341_() + 0.5, (double) endpoint.m_123342_() + 0.5, (double) endpoint.m_123343_() + 0.5, entity.getYRot(), entity.getXRot());
                teleportedEntity.setYHeadRot(entity.getYHeadRot());
                teleportedEntity.setPortalCooldown();
                endpointWorld.addFreshEntity(teleportedEntity);
            }
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    public void clearObstructions() {
        if (!this.hasClearedObstructions && this.isShattered() && this.getDestination() != null) {
            this.hasClearedObstructions = true;
            for (int i = -1; i <= -1; i++) {
                for (int j = -1; j <= -1; j++) {
                    for (int k = -1; k <= -1; k++) {
                        BlockPos toAir = this.getDestination().offset(i, j, k);
                        this.m_9236_().m_46961_(toAir, true);
                    }
                }
            }
        }
    }

    public Direction getAttachmentFacing() {
        return this.f_19804_.get(ATTACHED_FACE);
    }

    public void setAttachmentFacing(Direction facing) {
        this.f_19804_.set(ATTACHED_FACE, facing);
    }

    public int getLifespan() {
        return this.f_19804_.get(LIFESPAN);
    }

    public void setLifespan(int i) {
        this.f_19804_.set(LIFESPAN, i);
    }

    public boolean isShattered() {
        return this.f_19804_.get(SHATTERED);
    }

    public void setShattered(boolean set) {
        this.f_19804_.set(SHATTERED, set);
    }

    public BlockPos getDestination() {
        return (BlockPos) this.f_19804_.get(DESTINATION).orElse(null);
    }

    public void setDestination(BlockPos destination) {
        this.f_19804_.set(DESTINATION, Optional.ofNullable(destination));
        if (this.getSisterId() == null && (this.exitDimension == null || this.exitDimension == this.m_9236_().dimension())) {
            this.createAndSetSister(this.m_9236_(), null);
        }
    }

    public void createAndSetSister(Level world, Direction dir) {
        EntityVoidPortal portal = AMEntityRegistry.VOID_PORTAL.get().create(world);
        portal.setAttachmentFacing(dir != null ? dir : this.getAttachmentFacing().getOpposite());
        BlockPos safeDestination = this.getDestination();
        portal.m_20324_((double) ((float) safeDestination.m_123341_() + 0.5F), (double) ((float) safeDestination.m_123342_() + 0.5F), (double) ((float) safeDestination.m_123343_() + 0.5F));
        portal.link(this);
        portal.exitDimension = this.m_9236_().dimension();
        world.m_7967_(portal);
        portal.setShattered(this.isShattered());
    }

    public void setDestination(BlockPos destination, Direction dir) {
        this.f_19804_.set(DESTINATION, Optional.ofNullable(destination));
        if (this.getSisterId() == null && (this.exitDimension == null || this.exitDimension == this.m_9236_().dimension())) {
            this.createAndSetSister(this.m_9236_(), dir);
        }
    }

    public void link(EntityVoidPortal portal) {
        this.setSisterId(portal.m_20148_());
        portal.setSisterId(this.m_20148_());
        portal.setLifespan(this.getLifespan());
        this.setDestination(portal.m_20183_());
        portal.setDestination(this.m_20183_());
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(ATTACHED_FACE, Direction.DOWN);
        this.f_19804_.define(LIFESPAN, 300);
        this.f_19804_.define(SHATTERED, false);
        this.f_19804_.define(SISTER_UUID, Optional.empty());
        this.f_19804_.define(DESTINATION, Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.f_19804_.set(ATTACHED_FACE, Direction.from3DDataValue(compound.getByte("AttachFace")));
        this.setLifespan(compound.getInt("Lifespan"));
        if (compound.contains("Shattered")) {
            this.setShattered(compound.getBoolean("Shattered"));
        }
        if (compound.contains("DX")) {
            int i = compound.getInt("DX");
            int j = compound.getInt("DY");
            int k = compound.getInt("DZ");
            this.f_19804_.set(DESTINATION, Optional.of(new BlockPos(i, j, k)));
        } else {
            this.f_19804_.set(DESTINATION, Optional.empty());
        }
        if (compound.hasUUID("SisterUUID")) {
            this.setSisterId(compound.getUUID("SisterUUID"));
        }
        if (compound.contains("ExitDimension")) {
            this.exitDimension = (ResourceKey<Level>) Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compound.get("ExitDimension")).resultOrPartial(AlexsMobs.LOGGER::error).orElse(Level.OVERWORLD);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putByte("AttachFace", (byte) this.f_19804_.get(ATTACHED_FACE).get3DDataValue());
        compound.putInt("Lifespan", this.getLifespan());
        compound.putBoolean("Shattered", this.isShattered());
        BlockPos blockpos = this.getDestination();
        if (blockpos != null) {
            compound.putInt("DX", blockpos.m_123341_());
            compound.putInt("DY", blockpos.m_123342_());
            compound.putInt("DZ", blockpos.m_123343_());
        }
        if (this.getSisterId() != null) {
            compound.putUUID("SisterUUID", this.getSisterId());
        }
        if (this.exitDimension != null) {
            ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, this.exitDimension.location()).resultOrPartial(AlexsMobs.LOGGER::error).ifPresent(p_241148_1_ -> compound.put("ExitDimension", p_241148_1_));
        }
    }

    public Entity getSister() {
        UUID id = this.getSisterId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    @Nullable
    public UUID getSisterId() {
        return (UUID) this.f_19804_.get(SISTER_UUID).orElse(null);
    }

    public void setSisterId(@Nullable UUID uniqueId) {
        this.f_19804_.set(SISTER_UUID, Optional.ofNullable(uniqueId));
    }
}