package com.simibubi.create.content.contraptions.actors.psi;

import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class PortableFluidInterfaceBlockEntity extends PortableStorageInterfaceBlockEntity {

    protected LazyOptional<IFluidHandler> capability = this.createEmptyHandler();

    public PortableFluidInterfaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void startTransferringTo(Contraption contraption, float distance) {
        LazyOptional<IFluidHandler> oldcap = this.capability;
        this.capability = LazyOptional.of(() -> new PortableFluidInterfaceBlockEntity.InterfaceFluidHandler(contraption.getSharedFluidTanks()));
        oldcap.invalidate();
        super.startTransferringTo(contraption, distance);
    }

    @Override
    protected void invalidateCapability() {
        this.capability.invalidate();
    }

    @Override
    protected void stopTransferring() {
        LazyOptional<IFluidHandler> oldcap = this.capability;
        this.capability = this.createEmptyHandler();
        oldcap.invalidate();
        super.stopTransferring();
    }

    private LazyOptional<IFluidHandler> createEmptyHandler() {
        return LazyOptional.of(() -> new PortableFluidInterfaceBlockEntity.InterfaceFluidHandler(new FluidTank(0)));
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return this.isFluidHandlerCap(cap) ? this.capability.cast() : super.getCapability(cap, side);
    }

    public class InterfaceFluidHandler implements IFluidHandler {

        private IFluidHandler wrapped;

        public InterfaceFluidHandler(IFluidHandler wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public int getTanks() {
            return this.wrapped.getTanks();
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return this.wrapped.getFluidInTank(tank);
        }

        @Override
        public int getTankCapacity(int tank) {
            return this.wrapped.getTankCapacity(tank);
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return this.wrapped.isFluidValid(tank, stack);
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            if (!PortableFluidInterfaceBlockEntity.this.isConnected()) {
                return 0;
            } else {
                int fill = this.wrapped.fill(resource, action);
                if (fill > 0 && action.execute()) {
                    this.keepAlive();
                }
                return fill;
            }
        }

        @Override
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            if (!PortableFluidInterfaceBlockEntity.this.canTransfer()) {
                return FluidStack.EMPTY;
            } else {
                FluidStack drain = this.wrapped.drain(resource, action);
                if (!drain.isEmpty() && action.execute()) {
                    this.keepAlive();
                }
                return drain;
            }
        }

        @Override
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            if (!PortableFluidInterfaceBlockEntity.this.canTransfer()) {
                return FluidStack.EMPTY;
            } else {
                FluidStack drain = this.wrapped.drain(maxDrain, action);
                if (!drain.isEmpty() && action.execute()) {
                    this.keepAlive();
                }
                return drain;
            }
        }

        public void keepAlive() {
            PortableFluidInterfaceBlockEntity.this.onContentTransferred();
        }
    }
}