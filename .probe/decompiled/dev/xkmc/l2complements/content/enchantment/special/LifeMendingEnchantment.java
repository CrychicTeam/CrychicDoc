package dev.xkmc.l2complements.content.enchantment.special;

import dev.xkmc.l2complements.content.enchantment.core.UnobtainableEnchantment;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LifeMendingEnchantment extends UnobtainableEnchantment {

    public LifeMendingEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int lv) {
        return 1;
    }

    @Override
    public ChatFormatting getColor() {
        return ChatFormatting.LIGHT_PURPLE;
    }
}