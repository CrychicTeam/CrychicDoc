package dev.xkmc.l2library.base.tile;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

public class CombinedTankWrapper implements IFluidHandler {

    private final List<Pair<IFluidHandler, CombinedTankWrapper.Type>> list = new ArrayList();

    protected int[] baseIndex;

    protected int tankCount;

    protected boolean enforceVariety;

    public CombinedTankWrapper build() {
        this.baseIndex = new int[this.list.size()];
        int index = 0;
        for (int i = 0; i < this.list.size(); i++) {
            index += ((IFluidHandler) ((Pair) this.list.get(i)).getFirst()).getTanks();
            this.baseIndex[i] = index;
        }
        this.tankCount = index;
        return this;
    }

    public CombinedTankWrapper add(CombinedTankWrapper.Type type, IFluidHandler... handlers) {
        for (IFluidHandler handler : handlers) {
            this.list.add(Pair.of(handler, type));
        }
        return this;
    }

    protected Iterable<IFluidHandler> fillable() {
        return this.list.stream().filter(e -> e.getSecond() != CombinedTankWrapper.Type.EXTRACT).map(Pair::getFirst).toList();
    }

    protected Iterable<IFluidHandler> drainable() {
        return this.list.stream().filter(e -> e.getSecond() != CombinedTankWrapper.Type.INSERT).map(Pair::getFirst).toList();
    }

    public CombinedTankWrapper enforceVariety() {
        this.enforceVariety = true;
        return this;
    }

    @Override
    public int getTanks() {
        return this.tankCount;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        int index = this.getIndexForSlot(tank);
        IFluidHandler handler = this.getHandlerFromIndex(index);
        tank = this.getSlotFromIndex(tank, index);
        return handler.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        int index = this.getIndexForSlot(tank);
        IFluidHandler handler = this.getHandlerFromIndex(index);
        int localSlot = this.getSlotFromIndex(tank, index);
        return handler.getTankCapacity(localSlot);
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        int index = this.getIndexForSlot(tank);
        IFluidHandler handler = this.getHandlerFromIndex(index);
        int localSlot = this.getSlotFromIndex(tank, index);
        return handler.isFluidValid(localSlot, stack);
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty()) {
            return 0;
        } else {
            int filled = 0;
            resource = resource.copy();
            boolean fittingHandlerFound = false;
            for (boolean searchPass : new boolean[] { true, false }) {
                for (IFluidHandler iFluidHandler : this.fillable()) {
                    for (int i = 0; i < iFluidHandler.getTanks(); i++) {
                        if (searchPass && iFluidHandler.getFluidInTank(i).isFluidEqual(resource)) {
                            fittingHandlerFound = true;
                        }
                    }
                    if (!searchPass || fittingHandlerFound) {
                        int filledIntoCurrent = iFluidHandler.fill(resource, action);
                        resource.shrink(filledIntoCurrent);
                        filled += filledIntoCurrent;
                        if (resource.isEmpty() || fittingHandlerFound || this.enforceVariety && filledIntoCurrent != 0) {
                            return filled;
                        }
                    }
                }
            }
            return filled;
        }
    }

    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty()) {
            return resource;
        } else {
            FluidStack drained = FluidStack.EMPTY;
            resource = resource.copy();
            for (IFluidHandler iFluidHandler : this.drainable()) {
                FluidStack drainedFromCurrent = iFluidHandler.drain(resource, action);
                int amount = drainedFromCurrent.getAmount();
                resource.shrink(amount);
                if (!drainedFromCurrent.isEmpty() && (drained.isEmpty() || drainedFromCurrent.isFluidEqual(drained))) {
                    drained = new FluidStack(drainedFromCurrent.getFluid(), amount + drained.getAmount(), drainedFromCurrent.getTag());
                }
                if (resource.isEmpty()) {
                    break;
                }
            }
            return drained;
        }
    }

    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        FluidStack drained = FluidStack.EMPTY;
        for (IFluidHandler iFluidHandler : this.drainable()) {
            FluidStack drainedFromCurrent = iFluidHandler.drain(maxDrain, action);
            int amount = drainedFromCurrent.getAmount();
            maxDrain -= amount;
            if (!drainedFromCurrent.isEmpty() && (drained.isEmpty() || drainedFromCurrent.isFluidEqual(drained))) {
                drained = new FluidStack(drainedFromCurrent.getFluid(), amount + drained.getAmount(), drainedFromCurrent.getTag());
            }
            if (maxDrain == 0) {
                break;
            }
        }
        return drained;
    }

    protected int getIndexForSlot(int slot) {
        if (slot < 0) {
            return -1;
        } else {
            for (int i = 0; i < this.baseIndex.length; i++) {
                if (slot - this.baseIndex[i] < 0) {
                    return i;
                }
            }
            return -1;
        }
    }

    protected IFluidHandler getHandlerFromIndex(int index) {
        return index >= 0 && index < this.list.size() ? (IFluidHandler) ((Pair) this.list.get(index)).getFirst() : (IFluidHandler) EmptyHandler.INSTANCE;
    }

    protected int getSlotFromIndex(int slot, int index) {
        return index > 0 && index < this.baseIndex.length ? slot - this.baseIndex[index - 1] : slot;
    }

    public static enum Type {

        INSERT, EXTRACT, ALL
    }
}