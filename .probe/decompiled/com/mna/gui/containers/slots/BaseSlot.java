package com.mna.gui.containers.slots;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class BaseSlot extends SlotItemHandler {

    public BaseSlot(IItemHandler inv, int index, int xPosition, int yPosition) {
        super(inv, index, xPosition, yPosition);
    }

    @Override
    public abstract boolean mayPlace(ItemStack var1);
}