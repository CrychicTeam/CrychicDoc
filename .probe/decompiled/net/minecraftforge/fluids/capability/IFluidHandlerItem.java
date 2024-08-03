package net.minecraftforge.fluids.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import org.jetbrains.annotations.NotNull;

@AutoRegisterCapability
public interface IFluidHandlerItem extends IFluidHandler {

    @NotNull
    ItemStack getContainer();
}