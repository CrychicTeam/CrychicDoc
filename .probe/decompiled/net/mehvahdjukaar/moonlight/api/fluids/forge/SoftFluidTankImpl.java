package net.mehvahdjukaar.moonlight.api.fluids.forge;

import com.google.common.base.Objects;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

@Deprecated(forRemoval = true)
public class SoftFluidTankImpl extends SoftFluidTank {

    public static SoftFluidTank create(int capacity) {
        return new SoftFluidTankImpl(capacity);
    }

    protected SoftFluidTankImpl(int capacity) {
        super(capacity);
    }

    @Deprecated(forRemoval = true)
    public boolean isSameFluidAs(FluidStack fluidStack, CompoundTag com) {
        return this.fluidStack.isEquivalent(fluidStack.getFluid()) && Objects.equal(com, this.fluidStack.getTag());
    }

    @Deprecated(forRemoval = true)
    public boolean addVanillaFluid(FluidStack fluidStack) {
        SoftFluidStack s = SoftFluidStackImpl.fromForgeFluid(fluidStack);
        return s.isEmpty() ? false : this.addFluid(s, false) == s.getCount();
    }

    @Deprecated(forRemoval = true)
    @NotNull
    public static SoftFluidStack convertForgeFluid(FluidStack fluidStack) {
        int amount = SoftFluidStackImpl.MBtoBottles(fluidStack.getAmount());
        return SoftFluidStack.fromFluid(fluidStack.getFluid(), amount, fluidStack.hasTag() ? fluidStack.getTag().copy() : null);
    }

    public boolean transferToFluidTank(IFluidHandler fluidDestination, int bottles) {
        if (!this.isEmpty() && this.getFluidCount() >= bottles) {
            int milliBuckets = SoftFluidStackImpl.bottlesToMB(bottles);
            FluidStack stack = this.toEquivalentVanillaFluid(milliBuckets);
            if (!stack.isEmpty()) {
                int fillableAmount = fluidDestination.fill(stack, IFluidHandler.FluidAction.SIMULATE);
                if (fillableAmount == milliBuckets) {
                    fluidDestination.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    this.fluidStack.shrink(bottles);
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public boolean transferToFluidTank(IFluidHandler fluidDestination) {
        return this.transferToFluidTank(fluidDestination, 1);
    }

    public boolean drainFluidTank(IFluidHandler fluidSource, int bottles) {
        if (this.getSpace() < bottles) {
            return false;
        } else {
            int milliBuckets = SoftFluidStackImpl.bottlesToMB(bottles);
            FluidStack drainable = fluidSource.drain(milliBuckets, IFluidHandler.FluidAction.SIMULATE);
            if (!drainable.isEmpty() && drainable.getAmount() == milliBuckets) {
                boolean transfer = false;
                CompoundTag fsTag = drainable.getTag();
                if (this.fluidStack.isEmpty()) {
                    this.setFluid(drainable);
                    transfer = true;
                } else if (this.isSameFluidAs(drainable, fsTag)) {
                    transfer = true;
                }
                if (transfer) {
                    fluidSource.drain(milliBuckets, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            }
            return false;
        }
    }

    public boolean drainFluidTank(IFluidHandler fluidSource) {
        return this.drainFluidTank(fluidSource, 1);
    }

    @Deprecated(forRemoval = true)
    public FluidStack toEquivalentVanillaFluid(int mb) {
        if (!(this.fluidStack instanceof SoftFluidStackImpl)) {
            return FluidStack.EMPTY;
        } else {
            FluidStack s = ((SoftFluidStackImpl) this.fluidStack).toForgeFluid();
            s.setAmount(mb);
            return s;
        }
    }

    public void copy(IFluidHandler other) {
        FluidStack forgeFluid = other.getFluidInTank(0).copy();
        this.setFluid(forgeFluid);
        this.capCapacity();
    }

    public void setFluid(FluidStack fluidStack) {
        this.setFluid(SoftFluidStackImpl.fromForgeFluid(fluidStack));
    }
}