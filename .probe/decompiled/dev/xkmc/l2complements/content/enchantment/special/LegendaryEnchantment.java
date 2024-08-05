package dev.xkmc.l2complements.content.enchantment.special;

import dev.xkmc.l2complements.content.enchantment.core.SingleLevelEnchantment;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LegendaryEnchantment extends SingleLevelEnchantment {

    public LegendaryEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public ChatFormatting getColor() {
        return ChatFormatting.GOLD;
    }
}