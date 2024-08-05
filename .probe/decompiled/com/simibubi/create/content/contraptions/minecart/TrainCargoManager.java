package com.simibubi.create.content.contraptions.minecart;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TrainCargoManager extends MountedStorageManager {

    int ticksSinceLastExchange;

    AtomicInteger version = new AtomicInteger();

    public TrainCargoManager() {
        this.ticksSinceLastExchange = 0;
    }

    @Override
    public void createHandlers() {
        super.createHandlers();
    }

    @Override
    protected Contraption.ContraptionInvWrapper wrapItems(Collection<IItemHandlerModifiable> list, boolean fuel) {
        return (Contraption.ContraptionInvWrapper) (fuel ? super.wrapItems(list, fuel) : new TrainCargoManager.CargoInvWrapper((IItemHandlerModifiable[]) Arrays.copyOf(list.toArray(), list.size(), IItemHandlerModifiable[].class)));
    }

    @Override
    protected CombinedTankWrapper wrapFluids(Collection<IFluidHandler> list) {
        return new TrainCargoManager.CargoTankWrapper((IFluidHandler[]) Arrays.copyOf(list.toArray(), list.size(), IFluidHandler[].class));
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        nbt.putInt("TicksSinceLastExchange", this.ticksSinceLastExchange);
    }

    @Override
    public void read(CompoundTag nbt, Map<BlockPos, BlockEntity> presentBlockEntities, boolean clientPacket) {
        super.read(nbt, presentBlockEntities, clientPacket);
        this.ticksSinceLastExchange = nbt.getInt("TicksSinceLastExchange");
    }

    public void resetIdleCargoTracker() {
        this.ticksSinceLastExchange = 0;
    }

    public void tickIdleCargoTracker() {
        this.ticksSinceLastExchange++;
    }

    public int getTicksSinceLastExchange() {
        return this.ticksSinceLastExchange;
    }

    public int getVersion() {
        return this.version.get();
    }

    void changeDetected() {
        this.version.incrementAndGet();
        this.resetIdleCargoTracker();
    }

    class CargoInvWrapper extends Contraption.ContraptionInvWrapper {

        public CargoInvWrapper(IItemHandlerModifiable... itemHandler) {
            super(false, itemHandler);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack remainder = super.insertItem(slot, stack, simulate);
            if (!simulate && stack.getCount() != remainder.getCount()) {
                TrainCargoManager.this.changeDetected();
            }
            return remainder;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack extracted = super.extractItem(slot, amount, simulate);
            if (!simulate && !extracted.isEmpty()) {
                TrainCargoManager.this.changeDetected();
            }
            return extracted;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            if (!stack.equals(this.getStackInSlot(slot))) {
                TrainCargoManager.this.changeDetected();
            }
            super.setStackInSlot(slot, stack);
        }
    }

    class CargoTankWrapper extends CombinedTankWrapper {

        public CargoTankWrapper(IFluidHandler... fluidHandler) {
            super(fluidHandler);
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            int filled = super.fill(resource, action);
            if (action.execute() && filled > 0) {
                TrainCargoManager.this.changeDetected();
            }
            return filled;
        }

        @Override
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            FluidStack drained = super.drain(resource, action);
            if (action.execute() && !drained.isEmpty()) {
                TrainCargoManager.this.changeDetected();
            }
            return drained;
        }

        @Override
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            FluidStack drained = super.drain(maxDrain, action);
            if (action.execute() && !drained.isEmpty()) {
                TrainCargoManager.this.changeDetected();
            }
            return drained;
        }
    }
}