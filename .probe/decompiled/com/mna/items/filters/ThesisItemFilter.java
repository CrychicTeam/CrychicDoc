package com.mna.items.filters;

import com.mna.api.spells.ICanContainSpell;
import com.mna.items.sorcery.ItemTornJournalPage;
import net.minecraft.world.item.ItemStack;

public class ThesisItemFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() instanceof ItemTornJournalPage || stack.getItem() instanceof ICanContainSpell;
    }
}