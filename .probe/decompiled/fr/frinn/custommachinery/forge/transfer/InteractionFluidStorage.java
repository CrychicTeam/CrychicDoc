package fr.frinn.custommachinery.forge.transfer;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import fr.frinn.custommachinery.common.component.FluidMachineComponent;
import fr.frinn.custommachinery.common.component.handler.FluidComponentHandler;
import fr.frinn.custommachinery.common.util.Utils;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Nonnull;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

class InteractionFluidStorage implements IFluidHandler {

    private final FluidComponentHandler handler;

    public InteractionFluidStorage(FluidComponentHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getTanks() {
        return this.handler.getComponents().size();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return FluidStackHooksForge.toForge(((FluidMachineComponent) this.handler.getComponents().get(tank)).getFluidStack());
    }

    @Override
    public int getTankCapacity(int tank) {
        return Utils.toInt(((FluidMachineComponent) this.handler.getComponents().get(tank)).getCapacity());
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return ((FluidMachineComponent) this.handler.getComponents().get(tank)).isFluidValid(FluidStackHooksForge.fromForge(stack));
    }

    @Override
    public int fill(FluidStack forgeStack, IFluidHandler.FluidAction action) {
        AtomicLong remaining = new AtomicLong((long) forgeStack.getAmount());
        dev.architectury.fluid.FluidStack stack = FluidStackHooksForge.fromForge(forgeStack);
        this.handler.getComponents().stream().filter(component -> component.isFluidValid(stack) && component.getRemainingSpace() > 0L && component.getMode().isInput()).sorted(Comparator.comparingInt(component -> component.getFluidStack().isFluidEqual(stack) ? -1 : 1)).forEach(component -> {
            long toInput = Math.min(remaining.get(), component.insert(stack.getFluid(), (long) ((int) stack.getAmount()), stack.getTag(), true));
            if (toInput > 0L) {
                remaining.addAndGet(-toInput);
                if (action.execute()) {
                    component.insert(stack.getFluid(), toInput, stack.getTag(), false);
                }
            }
        });
        return (int) (stack.getAmount() - remaining.get());
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack maxDrain, IFluidHandler.FluidAction action) {
        long remainingToDrain = (long) maxDrain.getAmount();
        for (FluidMachineComponent component : this.handler.getComponents().stream().sorted(Comparator.comparingInt(c -> c.getMode().isOutput() ? 1 : -1)).toList()) {
            if (!component.getFluidStack().isEmpty() && component.getFluidStack().isFluidEqual(FluidStackHooksForge.fromForge(maxDrain))) {
                FluidStack stack = FluidStackHooksForge.toForge(component.extract((long) maxDrain.getAmount(), true));
                if ((long) stack.getAmount() >= remainingToDrain) {
                    if (action.execute()) {
                        component.extract((long) maxDrain.getAmount(), false);
                    }
                    return maxDrain;
                }
                if (action.execute()) {
                    component.extract((long) stack.getAmount(), false);
                }
                remainingToDrain -= (long) stack.getAmount();
            }
        }
        return remainingToDrain == (long) maxDrain.getAmount() ? FluidStack.EMPTY : new FluidStack(maxDrain.getFluid(), (int) ((long) maxDrain.getAmount() - remainingToDrain), maxDrain.getTag());
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        FluidStack toDrain = FluidStack.EMPTY;
        int remainingToDrain = maxDrain;
        for (FluidMachineComponent component : this.handler.getComponents().stream().sorted(Comparator.comparingInt(c -> c.getMode().isOutput() ? 1 : -1)).toList()) {
            if (!component.getFluidStack().isEmpty() && (toDrain.isEmpty() || component.getFluidStack().isFluidEqual(FluidStackHooksForge.fromForge(toDrain)))) {
                FluidStack stack = FluidStackHooksForge.toForge(component.extract((long) remainingToDrain, true));
                if (stack.getAmount() >= remainingToDrain) {
                    if (action.execute()) {
                        component.extract((long) remainingToDrain, false);
                    }
                    return new FluidStack(stack.getFluid(), maxDrain, stack.getTag());
                }
                if (toDrain.isEmpty()) {
                    toDrain = stack;
                }
                if (action.execute()) {
                    component.extract((long) stack.getAmount(), false);
                }
                remainingToDrain -= stack.getAmount();
            }
        }
        return !toDrain.isEmpty() && remainingToDrain != maxDrain ? new FluidStack(toDrain.getFluid(), maxDrain - remainingToDrain, toDrain.getTag()) : FluidStack.EMPTY;
    }
}