package net.minecraftforge.common.capabilities;

import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;

public class ForgeCapabilities {

    public static final Capability<IEnergyStorage> ENERGY = CapabilityManager.get(new CapabilityToken<IEnergyStorage>() {
    });

    public static final Capability<IFluidHandler> FLUID_HANDLER = CapabilityManager.get(new CapabilityToken<IFluidHandler>() {
    });

    public static final Capability<IFluidHandlerItem> FLUID_HANDLER_ITEM = CapabilityManager.get(new CapabilityToken<IFluidHandlerItem>() {
    });

    public static final Capability<IItemHandler> ITEM_HANDLER = CapabilityManager.get(new CapabilityToken<IItemHandler>() {
    });
}