package com.simibubi.create.content.fluids;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.BlockFace;
import java.lang.ref.WeakReference;
import java.util.function.Predicate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class FlowSource {

    private static final LazyOptional<IFluidHandler> EMPTY = LazyOptional.empty();

    BlockFace location;

    public FlowSource(BlockFace location) {
        this.location = location;
    }

    public FluidStack provideFluid(Predicate<FluidStack> extractionPredicate) {
        IFluidHandler tank = this.provideHandler().orElse(null);
        if (tank == null) {
            return FluidStack.EMPTY;
        } else {
            FluidStack immediateFluid = tank.drain(1, IFluidHandler.FluidAction.SIMULATE);
            if (extractionPredicate.test(immediateFluid)) {
                return immediateFluid;
            } else {
                for (int i = 0; i < tank.getTanks(); i++) {
                    FluidStack contained = tank.getFluidInTank(i);
                    if (!contained.isEmpty() && extractionPredicate.test(contained)) {
                        FluidStack toExtract = contained.copy();
                        toExtract.setAmount(1);
                        return tank.drain(toExtract, IFluidHandler.FluidAction.SIMULATE);
                    }
                }
                return FluidStack.EMPTY;
            }
        }
    }

    public void keepAlive() {
    }

    public abstract boolean isEndpoint();

    public void manageSource(Level world) {
    }

    public void whileFlowPresent(Level world, boolean pulling) {
    }

    public LazyOptional<IFluidHandler> provideHandler() {
        return EMPTY;
    }

    public static class Blocked extends FlowSource {

        public Blocked(BlockFace location) {
            super(location);
        }

        @Override
        public boolean isEndpoint() {
            return false;
        }
    }

    public static class FluidHandler extends FlowSource {

        LazyOptional<IFluidHandler> fluidHandler = FlowSource.EMPTY;

        public FluidHandler(BlockFace location) {
            super(location);
        }

        @Override
        public void manageSource(Level world) {
            if (!this.fluidHandler.isPresent() || world.getGameTime() % 20L == 0L) {
                BlockEntity blockEntity = world.getBlockEntity(this.location.getConnectedPos());
                if (blockEntity != null) {
                    this.fluidHandler = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, this.location.getOppositeFace());
                }
            }
        }

        @Override
        public LazyOptional<IFluidHandler> provideHandler() {
            return this.fluidHandler;
        }

        @Override
        public boolean isEndpoint() {
            return true;
        }
    }

    public static class OtherPipe extends FlowSource {

        WeakReference<FluidTransportBehaviour> cached;

        public OtherPipe(BlockFace location) {
            super(location);
        }

        @Override
        public void manageSource(Level world) {
            if (this.cached == null || this.cached.get() == null || ((FluidTransportBehaviour) this.cached.get()).blockEntity.m_58901_()) {
                this.cached = null;
                FluidTransportBehaviour fluidTransportBehaviour = BlockEntityBehaviour.get(world, this.location.getConnectedPos(), FluidTransportBehaviour.TYPE);
                if (fluidTransportBehaviour != null) {
                    this.cached = new WeakReference(fluidTransportBehaviour);
                }
            }
        }

        @Override
        public FluidStack provideFluid(Predicate<FluidStack> extractionPredicate) {
            if (this.cached != null && this.cached.get() != null) {
                FluidTransportBehaviour behaviour = (FluidTransportBehaviour) this.cached.get();
                FluidStack providedOutwardFluid = behaviour.getProvidedOutwardFluid(this.location.getOppositeFace());
                return extractionPredicate.test(providedOutwardFluid) ? providedOutwardFluid : FluidStack.EMPTY;
            } else {
                return FluidStack.EMPTY;
            }
        }

        @Override
        public boolean isEndpoint() {
            return false;
        }
    }
}