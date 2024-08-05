package net.blay09.mods.balm.api.energy;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;

public class EnergyStorage {

    private final int capacity;

    private final int maxFill;

    private final int maxDrain;

    private int energy;

    public EnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public EnergyStorage(int capacity, int maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public EnergyStorage(int capacity, int maxFill, int maxDrain) {
        this(maxDrain, capacity, maxFill, 0);
    }

    public EnergyStorage(int maxDrain, int capacity, int maxFill, int amount) {
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
        this.energy = Math.max(0, Math.min(capacity, amount));
    }

    public int fill(int maxFill, boolean simulate) {
        if (!this.canFill()) {
            return 0;
        } else {
            int filled = Math.min(this.capacity - this.energy, Math.min(this.maxFill, maxFill));
            if (!simulate) {
                this.energy += filled;
            }
            return filled;
        }
    }

    public int drain(int maxDrain, boolean simulate) {
        if (!this.canDrain()) {
            return 0;
        } else {
            int drained = Math.min(this.energy, Math.min(this.maxDrain, maxDrain));
            if (!simulate) {
                this.energy -= drained;
            }
            return drained;
        }
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean canDrain() {
        return this.maxDrain > 0;
    }

    public boolean canFill() {
        return this.maxFill > 0;
    }

    public IntTag serialize() {
        return IntTag.valueOf(this.energy);
    }

    @Deprecated
    public void deserialize(IntTag tag) {
        this.energy = tag.getAsInt();
    }

    public void deserialize(Tag tag) {
        this.energy = ((IntTag) tag).getAsInt();
    }
}