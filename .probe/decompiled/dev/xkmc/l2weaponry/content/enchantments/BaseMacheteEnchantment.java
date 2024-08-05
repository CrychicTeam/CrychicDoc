package dev.xkmc.l2weaponry.content.enchantments;

import dev.xkmc.l2complements.content.enchantment.core.UnobtainableEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class BaseMacheteEnchantment extends UnobtainableEnchantment {

    protected BaseMacheteEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return !(other instanceof BaseMacheteEnchantment);
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}