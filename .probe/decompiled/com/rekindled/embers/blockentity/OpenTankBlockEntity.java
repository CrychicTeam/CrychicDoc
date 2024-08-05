package com.rekindled.embers.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import org.jetbrains.annotations.NotNull;

public abstract class OpenTankBlockEntity extends FluidHandlerBlockEntity {

    FluidStack lastEscaped = null;

    long lastEscapedTickServer;

    long lastEscapedTickClient;

    public OpenTankBlockEntity(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("lastEscaped")) {
            this.lastEscaped = FluidStack.loadFluidStackFromNBT(nbt.getCompound("lastEscaped"));
            this.lastEscapedTickServer = nbt.getLong("lastEscapedTick");
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (this.lastEscaped != null) {
            nbt.put("lastEscaped", this.lastEscaped.writeToNBT(new CompoundTag()));
            nbt.putLong("lastEscapedTick", this.lastEscapedTickServer);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.m_5995_();
        this.tank.writeToNBT(nbt);
        if (this.lastEscaped != null) {
            nbt.put("lastEscaped", this.lastEscaped.writeToNBT(new CompoundTag()));
            nbt.putLong("lastEscapedTick", this.lastEscapedTickServer);
        }
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.m_6596_();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    public void setEscapedFluid(FluidStack stack) {
        if (stack != null && !stack.isEmpty()) {
            this.lastEscaped = stack.copy();
            this.lastEscapedTickServer = this.f_58857_.getLevelData().getGameTime();
            this.setChanged();
        }
    }

    protected boolean shouldEmitParticles() {
        if (this.lastEscaped == null || this.lastEscaped.isEmpty()) {
            return false;
        } else if (this.lastEscapedTickClient < this.lastEscapedTickServer) {
            this.lastEscapedTickClient = this.lastEscapedTickServer;
            return true;
        } else {
            long dTime = this.f_58857_.getLevelData().getGameTime() - this.lastEscapedTickClient;
            return dTime < (long) (this.lastEscaped.getAmount() + 5);
        }
    }

    protected abstract void updateEscapeParticles();
}