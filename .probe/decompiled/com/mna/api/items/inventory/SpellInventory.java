package com.mna.api.items.inventory;

import com.mna.inventory.InventorySpellBook;
import java.util.ArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class SpellInventory extends SimpleContainer implements ISpellBookInventory {

    public SpellInventory() {
        this(8);
    }

    public SpellInventory(int size) {
        super(size);
    }

    public void copyTo(ItemStack spellBook) {
        InventorySpellBook book = new InventorySpellBook(spellBook);
        for (int i = 0; i < book.getSlots(); i++) {
            this.m_6836_(i, book.getStackInSlot(i));
        }
        book.writeItemStack();
    }

    @Override
    public ItemStack[] getActiveSpells() {
        ArrayList<ItemStack> activeSpells = new ArrayList();
        for (int i = 0; i < this.m_6643_(); i++) {
            activeSpells.add(this.m_8020_(i));
        }
        return (ItemStack[]) activeSpells.toArray(new ItemStack[0]);
    }
}