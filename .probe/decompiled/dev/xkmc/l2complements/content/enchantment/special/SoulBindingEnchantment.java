package dev.xkmc.l2complements.content.enchantment.special;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class SoulBindingEnchantment extends LegendaryEnchantment {

    public SoulBindingEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    protected boolean checkCompatibility(Enchantment pOther) {
        return pOther != Enchantments.VANISHING_CURSE;
    }
}