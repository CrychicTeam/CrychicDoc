package net.minecraftforge.energy;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IEnergyStorage {

    int receiveEnergy(int var1, boolean var2);

    int extractEnergy(int var1, boolean var2);

    int getEnergyStored();

    int getMaxEnergyStored();

    boolean canExtract();

    boolean canReceive();
}