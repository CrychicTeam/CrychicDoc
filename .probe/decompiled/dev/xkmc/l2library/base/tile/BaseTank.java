package dev.xkmc.l2library.base.tile;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.AliasCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

@SerialClass
public class BaseTank implements IFluidHandler, AliasCollection<FluidStack> {

    private final int size;

    private final int capacity;

    private final List<BaseContainerListener> listeners = new ArrayList();

    private Predicate<FluidStack> predicate = e -> true;

    private BooleanSupplier allowExtract = () -> true;

    @SerialField
    public NonNullList<FluidStack> list;

    private int click_max;

    public BaseTank(int size, int capacity) {
        this.size = size;
        this.capacity = capacity;
        this.list = NonNullList.withSize(size, FluidStack.EMPTY);
    }

    public BaseTank add(BaseContainerListener listener) {
        this.listeners.add(listener);
        return this;
    }

    public BaseTank setPredicate(Predicate<FluidStack> predicate) {
        this.predicate = predicate;
        return this;
    }

    public BaseTank setExtract(BooleanSupplier allowExtract) {
        this.allowExtract = allowExtract;
        return this;
    }

    public BaseTank setClickMax(int max) {
        this.click_max = max;
        return this;
    }

    @Override
    public int getTanks() {
        return this.size;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.list.get(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.capacity;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty()) {
            return 0;
        } else if (!this.predicate.test(resource)) {
            return 0;
        } else {
            int to_fill = this.click_max == 0 ? resource.getAmount() : (resource.getAmount() >= this.click_max ? this.click_max : 0);
            if (to_fill == 0) {
                return 0;
            } else {
                int filled = 0;
                for (int i = 0; i < this.size; i++) {
                    FluidStack stack = this.list.get(i);
                    if (stack.isFluidEqual(resource)) {
                        int remain = this.capacity - stack.getAmount();
                        int fill = Math.min(to_fill, remain);
                        filled += fill;
                        to_fill -= fill;
                        if (action == IFluidHandler.FluidAction.EXECUTE) {
                            resource.shrink(fill);
                            stack.grow(fill);
                        }
                    } else if (stack.isEmpty()) {
                        int fill = Math.min(to_fill, this.capacity);
                        filled += fill;
                        to_fill -= fill;
                        if (action == IFluidHandler.FluidAction.EXECUTE) {
                            FluidStack rep = resource.copy();
                            rep.setAmount(fill);
                            this.list.set(i, rep);
                            resource.shrink(fill);
                        }
                    }
                    if (resource.isEmpty() || to_fill == 0) {
                        break;
                    }
                }
                if (action == IFluidHandler.FluidAction.EXECUTE && filled > 0) {
                    this.setChanged();
                }
                return filled;
            }
        }
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty()) {
            return resource;
        } else if (!this.allowExtract.getAsBoolean()) {
            return FluidStack.EMPTY;
        } else {
            int to_drain = resource.getAmount();
            if (this.click_max > 0) {
                if (to_drain < this.click_max) {
                    return FluidStack.EMPTY;
                }
                to_drain = this.click_max;
            }
            int drained = 0;
            for (int i = 0; i < this.size; i++) {
                FluidStack stack = this.list.get(i);
                if (stack.isFluidEqual(resource)) {
                    int remain = stack.getAmount();
                    int drain = Math.min(to_drain, remain);
                    drained += drain;
                    to_drain -= drain;
                    if (action == IFluidHandler.FluidAction.EXECUTE) {
                        stack.shrink(drain);
                    }
                }
                if (to_drain == 0) {
                    break;
                }
            }
            if (action == IFluidHandler.FluidAction.EXECUTE && drained > 0) {
                this.setChanged();
            }
            FluidStack ans = resource.copy();
            ans.setAmount(drained);
            return ans;
        }
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (!this.allowExtract.getAsBoolean()) {
            return FluidStack.EMPTY;
        } else {
            FluidStack ans = null;
            int to_drain = maxDrain;
            if (this.click_max > 0) {
                if (maxDrain < this.click_max) {
                    return FluidStack.EMPTY;
                }
                to_drain = this.click_max;
            }
            int drained = 0;
            for (int i = 0; i < this.size; i++) {
                FluidStack stack = this.list.get(i);
                if (!stack.isEmpty() && (ans == null || stack.isFluidEqual(ans))) {
                    int remain = stack.getAmount();
                    int drain = Math.min(to_drain, remain);
                    drained += drain;
                    to_drain -= drain;
                    if (ans == null) {
                        ans = stack.copy();
                    }
                    if (action == IFluidHandler.FluidAction.EXECUTE) {
                        stack.shrink(drain);
                    }
                }
                if (to_drain == 0) {
                    break;
                }
            }
            if (action == IFluidHandler.FluidAction.EXECUTE && drained > 0) {
                this.setChanged();
            }
            if (ans == null) {
                return FluidStack.EMPTY;
            } else {
                ans.setAmount(drained);
                return ans;
            }
        }
    }

    public void setChanged() {
        this.listeners.forEach(BaseContainerListener::notifyTile);
    }

    public List<FluidStack> getAsList() {
        return this.list;
    }

    public void clear() {
        this.list.clear();
    }

    public void set(int n, int i, FluidStack elem) {
        this.list.set(i, elem);
    }

    public Class<FluidStack> getElemClass() {
        return FluidStack.class;
    }

    public boolean isEmpty() {
        for (FluidStack stack : this.list) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}