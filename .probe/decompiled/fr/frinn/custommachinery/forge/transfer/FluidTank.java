package fr.frinn.custommachinery.forge.transfer;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import fr.frinn.custommachinery.common.component.FluidMachineComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class FluidTank implements IFluidHandler {

    private final FluidMachineComponent component;

    public FluidTank(FluidMachineComponent component) {
        this.component = component;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int i) {
        return FluidStackHooksForge.toForge(this.component.getFluidStack());
    }

    @Override
    public int getTankCapacity(int i) {
        return (int) this.component.getCapacity();
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack stack) {
        return this.component.isFluidValid(FluidStackHooksForge.fromForge(stack));
    }

    @Override
    public int fill(FluidStack stack, IFluidHandler.FluidAction action) {
        return !this.component.isFluidValid(FluidStackHooksForge.fromForge(stack)) ? 0 : (int) this.component.insert(stack.getFluid(), (long) stack.getAmount(), stack.getTag(), action.simulate());
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack stack, IFluidHandler.FluidAction action) {
        return stack.isFluidEqual(FluidStackHooksForge.toForge(this.component.getFluidStack())) ? FluidStackHooksForge.toForge(this.component.extract((long) stack.getAmount(), action.simulate())) : FluidStack.EMPTY;
    }

    @NotNull
    @Override
    public FluidStack drain(int amount, IFluidHandler.FluidAction action) {
        return FluidStackHooksForge.toForge(this.component.extract((long) amount, action.simulate()));
    }
}