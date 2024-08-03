package net.minecraftforge.fluids.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemFluidContainer extends Item {

    protected final int capacity;

    public ItemFluidContainer(Item.Properties properties, int capacity) {
        super(properties);
        this.capacity = capacity;
    }

    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidHandlerItemStack(stack, this.capacity);
    }
}