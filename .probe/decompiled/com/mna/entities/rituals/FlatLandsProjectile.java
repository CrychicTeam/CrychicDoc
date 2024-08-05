package com.mna.entities.rituals;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.entities.EntityInit;
import com.mna.tools.BlockUtils;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class FlatLandsProjectile extends Entity {

    private static final EntityDataAccessor<BlockPos> END = SynchedEntityData.defineId(FlatLandsProjectile.class, EntityDataSerializers.BLOCK_POS);

    int age = 0;

    float interpPct = 0.0F;

    Player caster_reference;

    UUID casterID;

    int shotTime = 200;

    public FlatLandsProjectile(EntityType<? extends FlatLandsProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public FlatLandsProjectile(Level worldIn) {
        this(EntityInit.FLAT_LANDS_PROJECTILE.get(), worldIn);
    }

    @Override
    public void tick() {
        this.age++;
        if (this.age > 200 && !this.m_9236_().isClientSide()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            Player caster = this.getCaster();
            if (caster != null) {
                if (!this.m_9236_().isClientSide() && this.age > this.shotTime) {
                    BlockPos end = this.f_19804_.get(END);
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            for (int k = -1; k <= 1; k++) {
                                BlockPos targetPos = end.offset(i, j, k);
                                if (this.m_9236_().getBlockEntity(targetPos) == null && BlockUtils.destroyBlock(caster, this.m_9236_(), targetPos, true, Tiers.IRON)) {
                                    BlockUtils.updateBlockState(this.m_9236_(), targetPos);
                                }
                            }
                        }
                    }
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    private Player getCaster() {
        if (this.caster_reference == null && this.casterID != null) {
            this.caster_reference = this.m_9236_().m_46003_(this.casterID);
        }
        return this.caster_reference;
    }

    public Vec3 getCurrentEndPoint() {
        return Vec3.atCenterOf(this.f_19804_.get(END));
    }

    public void setPoints(Vec3 start, BlockPos end) {
        this.m_146884_(start);
        this.f_19804_.set(END, end);
        this.shotTime = (int) start.distanceTo(Vec3.atCenterOf(end));
    }

    public void setCaster(Player caster) {
        this.caster_reference = caster;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(END, this.m_20183_());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (END.equals(pKey)) {
            Vec3 start = this.m_20182_();
            Vec3 end = Vec3.atCenterOf(this.f_19804_.get(END));
            this.shotTime = (int) start.distanceTo(end);
            Vec3 delta = end.subtract(start).normalize();
            start = start.add(delta);
            int[] color = Affinity.EARTH.getColor();
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()).setMaxAge(this.shotTime).setColor(color[0], color[1], color[2]), start.x(), start.y(), start.z(), end.x(), end.y(), end.z());
        }
        super.onSyncedDataUpdated(pKey);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("end_x") && compound.contains("end_y") && compound.contains("end_z")) {
            this.f_19804_.set(END, new BlockPos(compound.getInt("end_x"), compound.getInt("end_y"), compound.getInt("end_z")));
        }
        if (compound.contains("caster_uuid")) {
            try {
                this.casterID = UUID.fromString(compound.getString("caster_uuid"));
            } catch (Exception var3) {
                ManaAndArtifice.LOGGER.error("Error loading caster UUID for flat lands projectile!");
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("end_x", this.f_19804_.get(END).m_123341_());
        compound.putInt("end_y", this.f_19804_.get(END).m_123342_());
        compound.putInt("end_z", this.f_19804_.get(END).m_123343_());
        compound.putString("caster_uuid", this.caster_reference != null ? this.caster_reference.m_20148_().toString() : "");
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entityIn) {
        return false;
    }
}