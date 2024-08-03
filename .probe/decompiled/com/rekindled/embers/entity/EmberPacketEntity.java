package com.rekindled.embers.entity;

import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.power.IEmberPacketReceiver;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.StarParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EmberPacketEntity extends Entity {

    public BlockPos pos = new BlockPos(0, 0, 0);

    public BlockPos dest = new BlockPos(0, 0, 0);

    public double value = 0.0;

    public int lifetime = 80;

    public EmberPacketEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.m_20242_(true);
        this.m_20331_(true);
        this.f_19794_ = true;
    }

    public void initCustom(BlockPos pos, BlockPos dest, double vx, double vy, double vz, double value) {
        this.m_6027_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5);
        this.m_20334_(vx, vy, vz);
        this.dest = dest;
        this.pos = pos;
        this.value = value;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("destX")) {
            this.dest = new BlockPos(nbt.getInt("destX"), nbt.getInt("destY"), nbt.getInt("destZ"));
        }
        this.value = nbt.getDouble("value");
        this.lifetime = nbt.getInt("lifetime");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        if (this.dest != null) {
            nbt.putInt("destX", this.dest.m_123341_());
            nbt.putInt("destY", this.dest.m_123342_());
            nbt.putInt("destZ", this.dest.m_123343_());
        }
        nbt.putDouble("value", this.value);
        nbt.putInt("lifetime", this.lifetime);
    }

    @Override
    public void tick() {
        if (this.lifetime == 79 && this.m_9236_() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new StarParticleOptions(GlowParticleOptions.EMBER_COLOR, 3.5F + 0.5F * this.f_19796_.nextFloat()), this.m_20185_(), this.m_20186_(), this.m_20189_(), 12, (double) (0.0125F * (this.f_19796_.nextFloat() - 0.5F)), (double) (0.0125F * (this.f_19796_.nextFloat() - 0.5F)), (double) (0.0125F * (this.f_19796_.nextFloat() - 0.5F)), 0.0);
        }
        this.lifetime--;
        if (this.lifetime <= 0) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (!this.m_213877_()) {
            Vec3 oldPosition = new Vec3(this.m_20185_(), this.m_20186_(), this.m_20189_());
            if (this.dest.m_123341_() != 0 || this.dest.m_123342_() != 0 || this.dest.m_123343_() != 0) {
                double targetX = (double) this.dest.m_123341_() + 0.5;
                double targetY = (double) this.dest.m_123342_() + 0.5;
                double targetZ = (double) this.dest.m_123343_() + 0.5;
                Vec3 targetVector = new Vec3(targetX - this.m_20185_(), targetY - this.m_20186_(), targetZ - this.m_20189_());
                double length = targetVector.length();
                targetVector = targetVector.scale(0.3 / length);
                double weight = 0.0;
                if (length <= 3.0) {
                    weight = 0.9 * ((3.0 - length) / 3.0);
                }
                this.m_20334_((0.9 - weight) * this.m_20184_().x + (0.1 + weight) * targetVector.x, (0.9 - weight) * this.m_20184_().y + (0.1 + weight) * targetVector.y, (0.9 - weight) * this.m_20184_().z + (0.1 + weight) * targetVector.z);
            }
            this.m_6478_(MoverType.SELF, this.m_20184_());
            BlockPos pos = this.m_20183_();
            if (this.m_20185_() > (double) pos.m_123341_() + 0.25 && this.m_20185_() < (double) pos.m_123341_() + 0.75 && this.m_20186_() > (double) pos.m_123342_() + 0.25 && this.m_20186_() < (double) pos.m_123342_() + 0.75 && this.m_20189_() > (double) pos.m_123343_() + 0.25 && this.m_20189_() < (double) pos.m_123343_() + 0.75) {
                this.affectTileEntity(this.m_9236_().getBlockState(this.m_20183_()), this.m_9236_().getBlockEntity(this.m_20183_()));
            }
            if (this.m_9236_().isClientSide() && this.lifetime != 80) {
                double deltaX = this.m_20185_() - oldPosition.x;
                double deltaY = this.m_20186_() - oldPosition.y;
                double deltaZ = this.m_20189_() - oldPosition.z;
                double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 20.0);
                for (double i = 0.0; i < dist; i++) {
                    double coeff = i / dist;
                    this.m_9236_().addParticle(GlowParticleOptions.EMBER, oldPosition.x + deltaX * coeff, oldPosition.y + deltaY * coeff, oldPosition.z + deltaZ * coeff, (double) (0.125F * (this.f_19796_.nextFloat() - 0.5F)), (double) (0.125F * (this.f_19796_.nextFloat() - 0.5F)), (double) (0.125F * (this.f_19796_.nextFloat() - 0.5F)));
                }
            }
        }
    }

    public void affectTileEntity(BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof IEmberPacketReceiver && this.lifetime > 1 && ((IEmberPacketReceiver) blockEntity).onReceive(this)) {
            IEmberCapability capability = (IEmberCapability) blockEntity.getCapability(EmbersCapabilities.EMBER_CAPABILITY).orElse(null);
            if (capability != null) {
                capability.addAmount(this.value, true);
                blockEntity.setChanged();
            }
            this.m_20334_(0.0, 0.0, 0.0);
            this.lifetime = 2;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}