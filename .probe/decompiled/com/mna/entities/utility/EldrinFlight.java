package com.mna.entities.utility;

import com.mna.ManaAndArtifice;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.tools.math.Vector3;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class EldrinFlight extends Entity implements IEntityAdditionalSpawnData {

    private static final EntityDataAccessor<BlockPos> END_POS = SynchedEntityData.defineId(EldrinFlight.class, EntityDataSerializers.BLOCK_POS);

    private Vector3 startPosition;

    private Vector3 endPosition;

    private Vector3 controlPointA;

    private Vector3 controlPointB;

    private float travelTime;

    private Player player;

    private int age = 0;

    private boolean heightAdjusted = false;

    private static final int MIN_Y = 4;

    public EldrinFlight(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EldrinFlight(Level world, Player player, Vec3 start, Vec3 end) {
        super(EntityInit.ELDRIN_FLIGHT.get(), world);
        this.player = player;
        this.startPosition = new Vector3(start);
        if (!world.isClientSide) {
            if (end.y == -1.0) {
                ChunkAccess chunk = world.m_46865_(BlockPos.containing(end));
                int yPos = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, (int) end.x(), (int) end.z());
                this.endPosition = new Vector3(end.x, (double) (yPos + 1), end.z);
            } else {
                this.endPosition = new Vector3(end);
            }
        } else {
            this.endPosition = new Vector3(end);
        }
        Vector3 delta = this.endPosition.sub(this.startPosition);
        this.controlPointA = this.startPosition.add(delta.scale(0.33F)).add(new Vector3(0.0, 100.0, 0.0));
        this.controlPointB = this.startPosition.add(delta.scale(0.66F)).add(new Vector3(0.0, 100.0, 0.0));
        this.f_19794_ = true;
        float blocksPerSecond = 50.0F;
        this.travelTime = delta.length() / blocksPerSecond * 20.0F;
        this.m_6034_(start.x(), start.y(), start.z());
    }

    @Override
    public void tick() {
        this.age++;
        if (this.m_9236_().isClientSide() || this.startPosition != null && this.endPosition != null && this.controlPointA != null && this.controlPointB != null) {
            if (this.m_9236_().isClientSide()) {
                if (this.player != null) {
                    this.player.getPersistentData().putBoolean("eldrin_flight", true);
                }
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), (Math.random() - 0.5) * 0.2, 0.0, (Math.random() - 0.5) * 0.2);
                if (this.age < 60 && this.age % 10 == 0) {
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.TRAIL.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), (double) this.m_19879_(), this.m_9236_().getRandom().nextDouble(), (double) (this.travelTime + 70.0F - (float) this.age));
                }
            } else if (this.age == 1 && this.player != null) {
                this.player.m_7998_(this, true);
            }
            if (!this.heightAdjusted) {
                if (this.getEndPos().m_123341_() == 0 && this.getEndPos().m_123342_() == 0 && this.getEndPos().m_123343_() == 0) {
                    this.setEndPos(BlockPos.containing(this.endPosition.toVec3D()));
                }
                if (this.m_9236_().isLoaded(this.getEndPos())) {
                    BlockPos test = this.getEndPos();
                    if (test.m_123342_() >= this.m_9236_().dimensionType().logicalHeight()) {
                        int delta = this.m_9236_().dimensionType().logicalHeight() - 3 - test.m_123342_();
                        test = test.offset(0, delta, 0);
                    }
                    while (test.m_123342_() > 4 && !this.m_9236_().m_46859_(test) && !this.m_9236_().m_46859_(test.above())) {
                        test = test.below();
                    }
                    while (test.m_123342_() > 4 && this.m_9236_().m_46859_(test.below())) {
                        test = test.below();
                    }
                    while (test.m_123342_() < this.m_9236_().m_141928_() - 3 && !this.m_9236_().m_46859_(test) && !this.m_9236_().m_46859_(test.above())) {
                        test = test.above();
                    }
                    if (!this.m_9236_().m_46859_(test) && !this.m_9236_().m_46859_(test.above())) {
                        test.offset(0, this.m_9236_().m_141928_() - test.m_123342_(), 0);
                    }
                    this.setEndPos(test);
                    this.endPosition = new Vector3(test);
                    this.heightAdjusted = true;
                }
            }
            if (!this.m_9236_().isClientSide() && this.m_20197_().size() == 0) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            } else {
                if ((float) this.age < this.travelTime) {
                    Vector3 tickPos = Vector3.bezier(this.startPosition, this.endPosition, this.controlPointA, this.controlPointB, (float) this.age / this.travelTime);
                    Vector3 nextTickPos = Vector3.bezier(this.startPosition, this.endPosition, this.controlPointA, this.controlPointB, (float) this.age + 1.0F / this.travelTime);
                    this.m_6034_((double) tickPos.x, (double) tickPos.y, (double) tickPos.z);
                    this.m_7618_(EntityAnchorArgument.Anchor.FEET, nextTickPos.toVec3D());
                } else if ((float) this.age >= this.travelTime && !this.m_9236_().isClientSide()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                    if (this.player != null) {
                        this.player.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                    }
                }
            }
        } else {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    public void onRemovedFromWorld() {
        if (this.player != null) {
            if (this.m_9236_().isClientSide()) {
                for (int i = 0; i < 200; i++) {
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), this.player.m_20185_(), this.player.m_20186_(), this.player.m_20189_(), (Math.random() - 0.5) * 0.2, Math.random() * 0.2, (Math.random() - 0.5) * 0.2);
                }
                ManaAndArtifice.instance.proxy.resetRenderViewEntity();
            }
            this.player.getPersistentData().remove("eldrin_flight");
            this.player.getPersistentData().remove("eldrin_flight_entity_id");
            this.player.f_19789_ = 0.0F;
            this.player.m_21195_(EffectInit.ELDRIN_FLIGHT.get());
        }
        super.onRemovedFromWorld();
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(END_POS, BlockPos.ZERO);
    }

    private void setEndPos(BlockPos pos) {
        if (!this.m_9236_().isClientSide()) {
            this.f_19804_.set(END_POS, pos);
        }
    }

    private BlockPos getEndPos() {
        return this.f_19804_.get(END_POS);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_184206_1_) {
        super.onSyncedDataUpdated(p_184206_1_);
        if (p_184206_1_ == END_POS && this.m_9236_().isClientSide()) {
            this.heightAdjusted = true;
            this.endPosition = new Vector3(this.getEndPos());
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("start")) {
            this.startPosition = Vector3.readFromNBT(compound.getCompound("start"));
        }
        if (compound.contains("end")) {
            this.endPosition = Vector3.readFromNBT(compound.getCompound("end"));
        }
        if (compound.contains("control_a")) {
            this.controlPointA = Vector3.readFromNBT(compound.getCompound("control_a"));
        }
        if (compound.contains("control_b")) {
            this.controlPointB = Vector3.readFromNBT(compound.getCompound("control_b"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        CompoundTag start = new CompoundTag();
        CompoundTag end = new CompoundTag();
        CompoundTag control_a = new CompoundTag();
        CompoundTag control_b = new CompoundTag();
        this.startPosition.writeToNBT(start);
        this.endPosition.writeToNBT(end);
        this.controlPointA.writeToNBT(control_a);
        this.controlPointB.writeToNBT(control_b);
        compound.put("start", start);
        compound.put("end", end);
        compound.put("control_a", control_a);
        compound.put("control_b", control_b);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        if (this.player != null) {
            buffer.writeInt(this.player.m_19879_());
        } else {
            buffer.writeInt(-1);
        }
        buffer.writeFloat(this.travelTime);
        if (this.startPosition != null) {
            buffer.writeDouble((double) this.startPosition.x);
            buffer.writeDouble((double) this.startPosition.y);
            buffer.writeDouble((double) this.startPosition.z);
        }
        if (this.endPosition != null) {
            buffer.writeDouble((double) this.endPosition.x);
            buffer.writeDouble((double) this.endPosition.y);
            buffer.writeDouble((double) this.endPosition.z);
        }
        if (this.controlPointA != null) {
            buffer.writeDouble((double) this.controlPointA.x);
            buffer.writeDouble((double) this.controlPointA.y);
            buffer.writeDouble((double) this.controlPointA.z);
        }
        if (this.controlPointB != null) {
            buffer.writeDouble((double) this.controlPointB.x);
            buffer.writeDouble((double) this.controlPointB.y);
            buffer.writeDouble((double) this.controlPointB.z);
        }
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        Entity e = this.m_9236_().getEntity(additionalData.readInt());
        if (e instanceof Player) {
            this.player = (Player) e;
        }
        this.travelTime = additionalData.readFloat();
        this.startPosition = new Vector3(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());
        this.endPosition = new Vector3(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());
        this.controlPointA = new Vector3(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());
        this.controlPointB = new Vector3(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());
    }
}