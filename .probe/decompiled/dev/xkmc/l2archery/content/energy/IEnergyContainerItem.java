package dev.xkmc.l2archery.content.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyContainerItem {

    Capability<? extends IEnergyStorage> getEnergyCapability();

    int getExtract(ItemStack var1);

    int getReceive(ItemStack var1);

    int getMaxEnergyStored(ItemStack var1);

    default CompoundTag getOrCreateEnergyTag(ItemStack container) {
        return container.getOrCreateTag();
    }

    default int getSpace(ItemStack container) {
        return this.getMaxEnergyStored(container) - this.getEnergyStored(container);
    }

    static int round(double d) {
        return (int) (d + 0.5);
    }

    default int getScaledEnergyStored(ItemStack container, int scale) {
        return round((double) this.getEnergyStored(container) * (double) scale / (double) this.getMaxEnergyStored(container));
    }

    default int getEnergyStored(ItemStack container) {
        CompoundTag tag = this.getOrCreateEnergyTag(container);
        return Math.min(tag.getInt("Energy"), this.getMaxEnergyStored(container));
    }

    default void setEnergyStored(ItemStack container, int energy) {
        CompoundTag tag = this.getOrCreateEnergyTag(container);
        tag.putInt("Energy", Mth.clamp(energy, 0, this.getMaxEnergyStored(container)));
    }

    default int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        CompoundTag tag = this.getOrCreateEnergyTag(container);
        int stored = Math.min(tag.getInt("Energy"), this.getMaxEnergyStored(container));
        int receive = Math.min(Math.min(maxReceive, this.getReceive(container)), this.getSpace(container));
        if (!simulate) {
            stored += receive;
            tag.putInt("Energy", stored);
        }
        return receive;
    }

    default int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        CompoundTag tag = this.getOrCreateEnergyTag(container);
        int stored = Math.min(tag.getInt("Energy"), this.getMaxEnergyStored(container));
        int extract = Math.min(Math.min(maxExtract, this.getExtract(container)), stored);
        if (!simulate) {
            stored -= extract;
            tag.putInt("Energy", stored);
        }
        return extract;
    }
}