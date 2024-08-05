package com.mna.api.items.inventory;

import net.minecraft.world.item.ItemStack;

public interface ISpellBookInventory {

    ItemStack[] getActiveSpells();
}