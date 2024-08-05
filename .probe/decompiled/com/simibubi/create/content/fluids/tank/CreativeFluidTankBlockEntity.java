package com.simibubi.create.content.fluids.tank;

import com.simibubi.create.foundation.fluid.SmartFluidTank;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class CreativeFluidTankBlockEntity extends FluidTankBlockEntity {

    public CreativeFluidTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected SmartFluidTank createInventory() {
        return new CreativeFluidTankBlockEntity.CreativeSmartFluidTank(getCapacityMultiplier(), this::onFluidStackChanged);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }

    public static class CreativeSmartFluidTank extends SmartFluidTank {

        public CreativeSmartFluidTank(int capacity, Consumer<FluidStack> updateCallback) {
            super(capacity, updateCallback);
        }

        @Override
        public int getFluidAmount() {
            return this.getFluid().isEmpty() ? 0 : this.getTankCapacity(0);
        }

        public void setContainedFluid(FluidStack fluidStack) {
            this.fluid = fluidStack.copy();
            if (!fluidStack.isEmpty()) {
                this.fluid.setAmount(this.getTankCapacity(0));
            }
            this.onContentsChanged();
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            return resource.getAmount();
        }

        @Override
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            return super.drain(resource, IFluidHandler.FluidAction.SIMULATE);
        }

        @Override
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            return super.drain(maxDrain, IFluidHandler.FluidAction.SIMULATE);
        }
    }
}