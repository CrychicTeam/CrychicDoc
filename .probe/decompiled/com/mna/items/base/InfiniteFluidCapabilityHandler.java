package com.mna.items.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class InfiniteFluidCapabilityHandler implements IFluidHandlerItem, ICapabilityProvider {

    private final Fluid contained;

    private final int CAPACITY = 8000;

    public static final String FLUID_NBT_KEY = "Fluid";

    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

    @Nonnull
    protected ItemStack container;

    public InfiniteFluidCapabilityHandler(Fluid contained, @Nonnull ItemStack container) {
        this.container = container;
        this.contained = contained;
    }

    @Nonnull
    @Override
    public ItemStack getContainer() {
        return this.container;
    }

    @Nonnull
    public FluidStack getFluid() {
        return new FluidStack(this.contained, 8000);
    }

    protected void setFluid(FluidStack fluid) {
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return 8000;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return stack.getFluid().equals(this.contained);
    }

    @Override
    public int fill(@Nonnull FluidStack resource, IFluidHandler.FluidAction action) {
        return resource.isFluidEqual(this.getFluid()) ? resource.getAmount() : 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        return this.container.getCount() == 1 && !resource.isEmpty() && resource.isFluidEqual(this.getFluid()) ? this.drain(resource.getAmount(), action) : FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        return new FluidStack(this.contained, maxDrain);
    }

    public boolean canFillFluidType(FluidStack fluid) {
        return fluid.isFluidEqual(this.getFluid());
    }

    public boolean canDrainFluidType(FluidStack fluid) {
        return fluid.isFluidEqual(this.getFluid());
    }

    protected void setContainerToEmpty() {
        this.container.removeTagKey("Fluid");
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(capability, this.holder);
    }
}