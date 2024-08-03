package net.blay09.mods.balm.api.fluid;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidTank {

    private final int capacity;

    private final int maxFill;

    private final int maxDrain;

    private Fluid fluid = Fluids.EMPTY;

    private int amount;

    public FluidTank(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public FluidTank(int capacity, int maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public FluidTank(int capacity, int maxFill, int maxDrain) {
        this(maxDrain, capacity, maxFill, 0);
    }

    public FluidTank(int maxDrain, int capacity, int maxFill, int amount) {
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
        this.amount = Math.max(0, Math.min(capacity, amount));
    }

    public int fill(Fluid fluid, int maxFill, boolean simulate) {
        if (!this.canFill(fluid)) {
            return 0;
        } else {
            if (this.fluid.isSame(Fluids.EMPTY)) {
                this.fluid = fluid;
            }
            int filled = Math.min(this.capacity - this.amount, Math.min(this.maxFill, maxFill));
            if (!simulate) {
                this.amount += filled;
                this.setChanged();
            }
            return filled;
        }
    }

    public int drain(Fluid fluid, int maxDrain, boolean simulate) {
        if (!this.canDrain(fluid)) {
            return 0;
        } else {
            int drained = Math.min(this.amount, Math.min(this.maxDrain, maxDrain));
            if (!simulate) {
                this.amount -= drained;
                this.setChanged();
            }
            return drained;
        }
    }

    public Fluid getFluid() {
        return this.amount >= 0 ? this.fluid : Fluids.EMPTY;
    }

    public void setFluid(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
        this.setChanged();
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean canDrain(Fluid fluid) {
        return (this.fluid.isSame(fluid) || this.fluid.isSame(Fluids.EMPTY)) && this.maxDrain > 0;
    }

    public boolean canFill(Fluid fluid) {
        return (this.fluid.isSame(fluid) || this.fluid.isSame(Fluids.EMPTY)) && this.maxFill > 0;
    }

    public boolean isEmpty() {
        return this.amount <= 0 || this.fluid.isSame(Fluids.EMPTY);
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Fluid", Balm.getRegistries().getKey(this.fluid).toString());
        tag.putInt("Amount", this.amount);
        return tag;
    }

    public void deserialize(CompoundTag tag) {
        this.fluid = Balm.getRegistries().getFluid(ResourceLocation.tryParse(tag.getString("Fluid")));
        this.amount = tag.getInt("Amount");
    }

    public void setChanged() {
    }
}