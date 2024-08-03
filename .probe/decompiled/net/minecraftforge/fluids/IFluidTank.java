package net.minecraftforge.fluids;

import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public interface IFluidTank {

    @NotNull
    FluidStack getFluid();

    int getFluidAmount();

    int getCapacity();

    boolean isFluidValid(FluidStack var1);

    int fill(FluidStack var1, IFluidHandler.FluidAction var2);

    @NotNull
    FluidStack drain(int var1, IFluidHandler.FluidAction var2);

    @NotNull
    FluidStack drain(FluidStack var1, IFluidHandler.FluidAction var2);
}