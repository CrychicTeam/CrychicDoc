package com.mna.items.sorcery;

import com.mna.api.items.TieredItem;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.items.ItemInit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class VellumItem extends TieredItem implements ICanContainSpell {

    public VellumItem() {
        super(new Item.Properties());
    }

    @Override
    public ItemStack setSpell(ItemStack stack, ISpellDefinition spell) {
        if (stack.getItem() != this) {
            return stack;
        } else {
            ItemStack output = new ItemStack(ItemInit.SPELL.get());
            spell.writeToNBT(output.getOrCreateTag());
            return output;
        }
    }
}