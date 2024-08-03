package com.rekindled.embers.power;

import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultEmberCapability implements IEmberCapability {

    public static boolean allAcceptVolatile = false;

    private double ember = 0.0;

    private double capacity = 0.0;

    private final LazyOptional<IEmberCapability> holder;

    public DefaultEmberCapability() {
        this.holder = LazyOptional.of(() -> this);
    }

    public DefaultEmberCapability(IEmberCapability capability) {
        this.holder = LazyOptional.of(() -> capability);
    }

    @Override
    public double getEmber() {
        return this.ember;
    }

    @Override
    public double getEmberCapacity() {
        return this.capacity;
    }

    @Override
    public void setEmber(double value) {
        this.ember = value;
    }

    @Override
    public void setEmberCapacity(double value) {
        this.capacity = value;
    }

    @Override
    public double addAmount(double value, boolean doAdd) {
        double added = Math.min(this.capacity - this.ember, value);
        double newEmber = this.ember + added;
        if (doAdd) {
            if (newEmber != this.ember) {
                this.onContentsChanged();
            }
            this.ember += added;
        }
        return added;
    }

    @Override
    public double removeAmount(double value, boolean doRemove) {
        double removed = Math.min(this.ember, value);
        double newEmber = this.ember - removed;
        if (doRemove) {
            if (newEmber != this.ember) {
                this.onContentsChanged();
            }
            this.ember -= removed;
        }
        return removed;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        this.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        nbt.putDouble("embers:ember", this.ember);
        nbt.putDouble("embers:ember_capacity", this.capacity);
    }

    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("embers:ember")) {
            this.ember = nbt.getDouble("embers:ember");
        }
        if (nbt.contains("embers:ember_capacity")) {
            this.capacity = nbt.getDouble("embers:ember_capacity");
        }
    }

    @Override
    public void onContentsChanged() {
    }

    @Override
    public boolean acceptsVolatile() {
        return allAcceptVolatile;
    }

    @Override
    public void invalidate() {
        this.holder.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return EmbersCapabilities.EMBER_CAPABILITY != null && cap == EmbersCapabilities.EMBER_CAPABILITY ? EmbersCapabilities.EMBER_CAPABILITY.orEmpty(cap, this.holder) : LazyOptional.empty();
    }
}