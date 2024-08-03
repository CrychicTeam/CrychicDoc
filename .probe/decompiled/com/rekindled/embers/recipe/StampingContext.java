package com.rekindled.embers.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class StampingContext extends RecipeWrapper {

    public IFluidHandler fluids;

    public ItemStack stamp;

    public StampingContext(IItemHandlerModifiable inv, IFluidHandler fluids, ItemStack stamp) {
        super(inv);
        this.fluids = fluids;
        this.stamp = stamp;
    }
}