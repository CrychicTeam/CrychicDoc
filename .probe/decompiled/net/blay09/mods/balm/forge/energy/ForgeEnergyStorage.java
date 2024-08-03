package net.blay09.mods.balm.forge.energy;

import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgeEnergyStorage implements IEnergyStorage {

    private final EnergyStorage energyStorage;

    public ForgeEnergyStorage(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return this.energyStorage.fill(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return this.energyStorage.drain(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return this.energyStorage.getEnergy();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.energyStorage.getCapacity();
    }

    @Override
    public boolean canExtract() {
        return this.energyStorage.canDrain();
    }

    @Override
    public boolean canReceive() {
        return this.energyStorage.canFill();
    }
}