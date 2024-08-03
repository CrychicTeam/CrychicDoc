package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class MechanicalPumpTopBlockEntity extends FluidHandlerBlockEntity implements IExtraCapabilityInformation {

    public static int capacity = 8000;

    public MechanicalPumpTopBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.MECHANICAL_PUMP_TOP_ENTITY.get(), pPos, pBlockState);
        this.tank = new FluidTank(capacity) {

            @Override
            public void onContentsChanged() {
                MechanicalPumpTopBlockEntity.this.m_6596_();
            }
        };
    }

    public int getCapacity() {
        return this.tank.getCapacity();
    }

    public FluidStack getFluidStack() {
        return this.tank.getFluid();
    }

    public FluidTank getTank() {
        return this.tank;
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.FLUID_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.OUTPUT, "embers.tooltip.goggles.fluid", Component.translatable("embers.tooltip.goggles.fluid.water")));
        }
    }
}