package com.mna.inventory;

import com.mna.api.items.inventory.ISpellBookInventory;
import java.util.ArrayList;
import net.minecraft.world.item.ItemStack;

public class InventorySpellBook extends ItemInventoryBase implements ISpellBookInventory {

    public InventorySpellBook(ItemStack bag) {
        super(bag, 54);
    }

    @Override
    public ItemStack[] getActiveSpells() {
        ArrayList<ItemStack> activeSpells = new ArrayList();
        for (int i = 0; i < 8; i++) {
            activeSpells.add(this.getStackInSlot(i * 5));
        }
        return (ItemStack[]) activeSpells.toArray(new ItemStack[0]);
    }
}