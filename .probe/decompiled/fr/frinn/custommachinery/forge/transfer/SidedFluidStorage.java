package fr.frinn.custommachinery.forge.transfer;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import fr.frinn.custommachinery.common.component.FluidMachineComponent;
import fr.frinn.custommachinery.common.component.handler.FluidComponentHandler;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.impl.component.config.SideMode;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SidedFluidStorage implements IFluidHandler {

    @Nullable
    private final Direction direction;

    private final FluidComponentHandler handler;

    public SidedFluidStorage(@Nullable Direction direction, FluidComponentHandler handler) {
        this.direction = direction;
        this.handler = handler;
    }

    public List<FluidMachineComponent> getSideComponents(Predicate<SideMode> filter) {
        return this.direction == null ? this.handler.getComponents() : this.handler.getComponents().stream().filter(component -> filter.test(component.getConfig().getSideMode(this.direction))).toList();
    }

    @Override
    public int getTanks() {
        return this.handler.getComponents().size();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return FluidStackHooksForge.toForge(((FluidMachineComponent) this.handler.getComponents().get(tank)).getFluidStack());
    }

    @Override
    public int getTankCapacity(int tank) {
        return Utils.toInt(((FluidMachineComponent) this.handler.getComponents().get(tank)).getCapacity());
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return ((FluidMachineComponent) this.handler.getComponents().get(tank)).isFluidValid(FluidStackHooksForge.fromForge(stack));
    }

    @Override
    public int fill(FluidStack forgeStack, IFluidHandler.FluidAction action) {
        AtomicLong remaining = new AtomicLong((long) forgeStack.getAmount());
        dev.architectury.fluid.FluidStack stack = FluidStackHooksForge.fromForge(forgeStack);
        this.getSideComponents(SideMode::isInput).stream().filter(component -> component.isFluidValid(stack) && component.getRemainingSpace() > 0L && component.getMode().isInput()).sorted(Comparator.comparingInt(component -> component.getFluidStack().isFluidEqual(stack) ? -1 : 1)).forEach(component -> {
            long toInput = Math.min(remaining.get(), component.insert(stack.getFluid(), stack.getAmount(), stack.getTag(), true));
            if (toInput > 0L) {
                remaining.addAndGet(-toInput);
                if (action.execute()) {
                    component.insert(stack.getFluid(), toInput, stack.getTag(), false);
                }
            }
        });
        return (int) (stack.getAmount() - remaining.get());
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack maxDrain, IFluidHandler.FluidAction action) {
        int remainingToDrain = maxDrain.getAmount();
        for (FluidMachineComponent component : this.getSideComponents(SideMode::isOutput)) {
            if (!component.getFluidStack().isEmpty() && component.getFluidStack().isFluidEqual(FluidStackHooksForge.fromForge(maxDrain))) {
                FluidStack stack = FluidStackHooksForge.toForge(component.extract((long) maxDrain.getAmount(), true));
                if (stack.getAmount() >= remainingToDrain) {
                    if (action.execute()) {
                        component.extract((long) remainingToDrain, false);
                    }
                    return maxDrain;
                }
                if (action.execute()) {
                    component.extract((long) stack.getAmount(), false);
                }
                remainingToDrain -= stack.getAmount();
            }
        }
        return remainingToDrain == maxDrain.getAmount() ? FluidStack.EMPTY : new FluidStack(maxDrain.getFluid(), maxDrain.getAmount() - remainingToDrain, maxDrain.getTag());
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        FluidStack toDrain = FluidStack.EMPTY;
        int remainingToDrain = maxDrain;
        for (FluidMachineComponent component : this.getSideComponents(SideMode::isOutput)) {
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