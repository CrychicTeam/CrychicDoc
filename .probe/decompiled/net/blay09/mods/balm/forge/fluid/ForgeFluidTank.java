package net.blay09.mods.balm.forge.fluid;

import net.blay09.mods.balm.api.fluid.FluidTank;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class ForgeFluidTank implements IFluidHandler {

    private final FluidTank fluidTank;

    public ForgeFluidTank(FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return new FluidStack(this.fluidTank.getFluid(), this.fluidTank.getAmount());
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.fluidTank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return this.fluidTank.canFill(stack.getFluid());
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        return this.fluidTank.fill(resource.getFluid(), resource.getAmount(), action.simulate());
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        int drained = this.fluidTank.drain(resource.getFluid(), resource.getAmount(), action.simulate());
        return new FluidStack(this.fluidTank.getFluid(), drained);
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        int drained = this.fluidTank.drain(this.fluidTank.getFluid(), maxDrain, action.simulate());
        return new FluidStack(this.fluidTank.getFluid(), drained);
    }
}