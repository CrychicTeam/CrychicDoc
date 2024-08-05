package com.rekindled.embers.power;

import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class DefaultEmberItemCapability implements IEmberCapability {

    @Nonnull
    public ItemStack stack;

    public final LazyOptional<?> capOptional;

    double ember = 0.0;

    double capacity = 0.0;

    public DefaultEmberItemCapability(@Nonnull ItemStack stack, double capacity) {
        this.stack = stack;
        this.setEmberCapacity(capacity);
        this.capOptional = LazyOptional.of(() -> this);
        CompoundTag BEnbt = stack.getTagElement("BlockEntityTag");
        if (BEnbt != null) {
            this.setEmber(BEnbt.getDouble("embers:ember"));
            this.setEmberCapacity(BEnbt.getDouble("embers:ember_capacity"));
        }
    }

    @Override
    public void invalidate() {
        this.capOptional.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == EmbersCapabilities.EMBER_CAPABILITY ? this.capOptional.cast() : LazyOptional.empty();
    }

    @Override
    public double getEmber() {
        return this.stack.isEmpty() ? 0.0 : this.stack.getOrCreateTagElement("ForgeCaps").getDouble("embers:ember");
    }

    @Override
    public double getEmberCapacity() {
        return this.stack.isEmpty() ? 0.0 : this.stack.getOrCreateTagElement("ForgeCaps").getDouble("embers:ember_capacity");
    }

    @Override
    public void setEmber(double value) {
        this.ember = value;
        this.stack.getOrCreateTagElement("ForgeCaps").putDouble("embers:ember", value);
    }

    @Override
    public void setEmberCapacity(double value) {
        this.capacity = value;
        this.stack.getOrCreateTagElement("ForgeCaps").putDouble("embers:ember_capacity", value);
    }

    @Override
    public double addAmount(double value, boolean doAdd) {
        double ember = this.getEmber();
        double capacity = this.getEmberCapacity();
        double added = Math.min(capacity - ember, value);
        double newEmber = ember + added;
        if (doAdd) {
            if (newEmber != ember) {
                this.onContentsChanged();
            }
            this.setEmber(ember + added);
        }
        return added;
    }

    @Override
    public double removeAmount(double value, boolean doRemove) {
        double ember = this.getEmber();
        double removed = Math.min(ember, value);
        double newEmber = ember - removed;
        if (doRemove) {
            if (newEmber != ember) {
                this.onContentsChanged();
            }
            this.setEmber(ember - removed);
        }
        return removed;
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        nbt.putDouble("embers:ember", this.ember);
        nbt.putDouble("embers:ember_capacity", this.capacity);
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        this.writeToNBT(nbt);
        return nbt;
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
}