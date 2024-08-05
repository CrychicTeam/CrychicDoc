package com.simibubi.create.content.fluids.hosePulley;

import com.simibubi.create.content.fluids.transfer.FluidDrainingBehaviour;
import com.simibubi.create.content.fluids.transfer.FluidFillingBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class HosePulleyFluidHandler implements IFluidHandler {

    private SmartFluidTank internalTank;

    private FluidFillingBehaviour filler;

    private FluidDrainingBehaviour drainer;

    private Supplier<BlockPos> rootPosGetter;

    private Supplier<Boolean> predicate;

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (!this.internalTank.isEmpty() && !resource.isFluidEqual(this.internalTank.getFluid())) {
            return 0;
        } else if (!resource.isEmpty() && FluidHelper.hasBlockState(resource.getFluid())) {
            int diff = resource.getAmount();
            int totalAmountAfterFill = diff + this.internalTank.getFluidAmount();
            FluidStack remaining = resource.copy();
            boolean deposited = false;
            if ((Boolean) this.predicate.get() && totalAmountAfterFill >= 1000 && this.filler.tryDeposit(resource.getFluid(), (BlockPos) this.rootPosGetter.get(), action.simulate())) {
                this.drainer.counterpartActed();
                remaining.shrink(1000);
                diff -= 1000;
                deposited = true;
            }
            if (action.simulate()) {
                return diff <= 0 ? resource.getAmount() : this.internalTank.fill(remaining, action);
            } else if (diff <= 0) {
                this.internalTank.drain(-diff, IFluidHandler.FluidAction.EXECUTE);
                return resource.getAmount();
            } else {
                return this.internalTank.fill(remaining, action) + (deposited ? 1000 : 0);
            }
        } else {
            return 0;
        }
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.internalTank.isEmpty() ? this.drainer.getDrainableFluid((BlockPos) this.rootPosGetter.get()) : this.internalTank.getFluidInTank(tank);
    }

    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        return this.drainInternal(resource.getAmount(), resource, action);
    }

    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        return this.drainInternal(maxDrain, null, action);
    }

    private FluidStack drainInternal(int maxDrain, @Nullable FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource != null && !this.internalTank.isEmpty() && !resource.isFluidEqual(this.internalTank.getFluid())) {
            return FluidStack.EMPTY;
        } else if (this.internalTank.getFluidAmount() >= 1000) {
            return this.internalTank.drain(maxDrain, action);
        } else {
            BlockPos pos = (BlockPos) this.rootPosGetter.get();
            FluidStack returned = this.drainer.getDrainableFluid(pos);
            if ((Boolean) this.predicate.get() && this.drainer.pullNext(pos, action.simulate())) {
                this.filler.counterpartActed();
                FluidStack leftover = returned.copy();
                int available = 1000 + this.internalTank.getFluidAmount();
                if ((this.internalTank.isEmpty() || this.internalTank.getFluid().isFluidEqual(returned)) && !returned.isEmpty()) {
                    if (resource != null && !returned.isFluidEqual(resource)) {
                        return FluidStack.EMPTY;
                    } else {
                        int drained = Math.min(maxDrain, available);
                        returned.setAmount(drained);
                        leftover.setAmount(available - drained);
                        if (action.execute() && !leftover.isEmpty()) {
                            this.internalTank.setFluid(leftover);
                        }
                        return returned;
                    }
                } else {
                    return this.internalTank.drain(maxDrain, action);
                }
            } else {
                return this.internalTank.drain(maxDrain, action);
            }
        }
    }

    public HosePulleyFluidHandler(SmartFluidTank internalTank, FluidFillingBehaviour filler, FluidDrainingBehaviour drainer, Supplier<BlockPos> rootPosGetter, Supplier<Boolean> predicate) {
        this.internalTank = internalTank;
        this.filler = filler;
        this.drainer = drainer;
        this.rootPosGetter = rootPosGetter;
        this.predicate = predicate;
    }

    @Override
    public int getTanks() {
        return this.internalTank.getTanks();
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.internalTank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return this.internalTank.isFluidValid(tank, stack);
    }

    public SmartFluidTank getInternalTank() {
        return this.internalTank;
    }
}