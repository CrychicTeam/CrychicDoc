package com.mna.gui.containers.slots;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class NonStupidFurnaceFuelSlot extends SlotItemHandler {

    public NonStupidFurnaceFuelSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
    }
}