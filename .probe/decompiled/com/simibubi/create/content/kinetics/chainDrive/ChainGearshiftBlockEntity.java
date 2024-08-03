package com.simibubi.create.content.kinetics.chainDrive;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ChainGearshiftBlockEntity extends KineticBlockEntity {

    int signal = 0;

    boolean signalChanged;

    public ChainGearshiftBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(40);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Signal", this.signal);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.signal = compound.getInt("Signal");
        super.read(compound, clientPacket);
    }

    public float getModifier() {
        return this.getModifierForSignal(this.signal);
    }

    public void neighbourChanged() {
        if (this.m_58898_()) {
            int power = this.f_58857_.m_277086_(this.f_58858_);
            if (power != this.signal) {
                this.signalChanged = true;
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        this.neighbourChanged();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_58857_.isClientSide) {
            if (this.signalChanged) {
                this.signalChanged = false;
                this.analogSignalChanged(this.f_58857_.m_277086_(this.f_58858_));
            }
        }
    }

    protected void analogSignalChanged(int newSignal) {
        this.detachKinetics();
        this.removeSource();
        this.signal = newSignal;
        this.attachKinetics();
    }

    protected float getModifierForSignal(int newPower) {
        return newPower == 0 ? 1.0F : 1.0F + (float) (newPower + 1) / 16.0F;
    }
}