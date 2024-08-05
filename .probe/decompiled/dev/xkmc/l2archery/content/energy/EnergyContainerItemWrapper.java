package dev.xkmc.l2archery.content.energy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyContainerItemWrapper implements IEnergyStorage, ICapabilityProvider {

    private final Capability<? extends IEnergyStorage> capability;

    private final LazyOptional<IEnergyStorage> holder = LazyOptional.of(() -> this);

    protected final ItemStack container;

    protected final IEnergyContainerItem item;

    public EnergyContainerItemWrapper(ItemStack containerIn, IEnergyContainerItem itemIn, Capability<? extends IEnergyStorage> capability) {
        this.container = containerIn;
        this.item = itemIn;
        this.capability = capability;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return !this.canReceive() ? 0 : this.item.receiveEnergy(this.container, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return !this.canExtract() ? 0 : this.item.extractEnergy(this.container, maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return this.item.getEnergyStored(this.container);
    }

    @Override
    public int getMaxEnergyStored() {
        return this.item.getMaxEnergyStored(this.container);
    }

    @Override
    public boolean canExtract() {
        return this.item.getExtract(this.container) > 0;
    }

    @Override
    public boolean canReceive() {
        return this.item.getReceive(this.container) > 0;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == this.capability ? this.holder.cast() : LazyOptional.empty();
    }
}