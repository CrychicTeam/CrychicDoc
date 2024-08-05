package com.simibubi.create.content.equipment.toolbox;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

public class ItemReturnInvWrapper extends PlayerMainInvWrapper {

    public ItemReturnInvWrapper(Inventory inv) {
        super(inv);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return slot >= 0 && slot < 9 ? stack : super.insertItem(slot, stack, simulate);
    }
}