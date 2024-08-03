package snownee.kiwi.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraftforge.energy.EnergyStorage;

public class Battery extends EnergyStorage {

    public Battery(int capacity) {
        super(capacity);
    }

    public Battery(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public Battery(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public Battery(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public Battery readFromNBT(CompoundTag nbt) {
        if (nbt.contains("Energy", 3)) {
            this.energy = nbt.getInt("Energy");
        } else {
            this.energy = 0;
        }
        return this;
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        if (this.energy > 0) {
            nbt.putInt("Energy", this.energy);
        }
        return nbt;
    }

    public void setEnergy(int energy) {
        int old = this.energy;
        this.energy = Mth.clamp(energy, 0, this.getMaxEnergyStored());
        if (old != this.energy) {
            this.onEnergyChanged();
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int amount = super.extractEnergy(maxExtract, simulate);
        if (!simulate && amount > 0) {
            this.onEnergyChanged();
        }
        return amount;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int amount = super.receiveEnergy(maxReceive, simulate);
        if (!simulate && amount > 0) {
            this.onEnergyChanged();
        }
        return amount;
    }

    protected void onEnergyChanged() {
    }
}