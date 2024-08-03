package com.mna.items.relic;

import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.ritual.PractitionersPouchPatches;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class MithionsMagnificentMbag extends ItemPractitionersPouch {

    @Override
    public int getPatchLevel(ItemStack stack, PractitionersPouchPatches patch) {
        return 9999;
    }

    @Override
    public Rarity getRarity(ItemStack p_77613_1_) {
        return Rarity.EPIC;
    }

    @Override
    public PractitionersPouchPatches[] getAppliedPatches(ItemStack stack) {
        return PractitionersPouchPatches.values();
    }
}