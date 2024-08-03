package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.upgrade.ClockworkAttenuatorUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class ClockworkAttenuatorBlockEntity extends BlockEntity {

    public ClockworkAttenuatorUpgrade upgrade;

    public double activeSpeed = 0.0;

    public double inactiveSpeed = 1.0;

    public double[] validSpeeds = new double[] { 0.0, 0.0625, 0.125, 0.25, 0.5, 1.0 };

    public ClockworkAttenuatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.CLOCKWORK_ATTENUATOR_ENTITY.get(), pPos, pBlockState);
        this.upgrade = new ClockworkAttenuatorUpgrade(this);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.activeSpeed = nbt.getDouble("active_speed");
        this.inactiveSpeed = nbt.getDouble("inactive_speed");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putDouble("active_speed", this.activeSpeed);
        nbt.putDouble("inactive_speed", this.inactiveSpeed);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putDouble("active_speed", this.activeSpeed);
        nbt.putDouble("inactive_speed", this.inactiveSpeed);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return this.f_58859_ || cap != EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY || side != null && ((Direction) this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.FACING)).getOpposite() != side ? super.getCapability(cap, side) : this.upgrade.getCapability(cap, side);
    }

    public double getSpeed() {
        return this.f_58857_.m_276867_(this.f_58858_) ? this.activeSpeed : this.inactiveSpeed;
    }

    public double getNext(double current) {
        for (int i = 0; i < this.validSpeeds.length - 1; i++) {
            double a = this.validSpeeds[i];
            double b = this.validSpeeds[i + 1];
            if (b > current && a <= current) {
                return b;
            }
        }
        return current;
    }

    public double getPrevious(double current) {
        for (int i = 0; i < this.validSpeeds.length - 1; i++) {
            double a = this.validSpeeds[i];
            double b = this.validSpeeds[i + 1];
            if (b >= current && a < current) {
                return a;
            }
        }
        return current;
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.upgrade.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }
}