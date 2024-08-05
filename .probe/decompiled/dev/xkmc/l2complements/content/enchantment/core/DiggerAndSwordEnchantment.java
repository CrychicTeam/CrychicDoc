package dev.xkmc.l2complements.content.enchantment.core;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class DiggerAndSwordEnchantment extends SingleLevelEnchantment {

    public DiggerAndSwordEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return super.m_6081_(stack) || EnchantmentCategory.WEAPON.canEnchant(stack.getItem());
    }
}