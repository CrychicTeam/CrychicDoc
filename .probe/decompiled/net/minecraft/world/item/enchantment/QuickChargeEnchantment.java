package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class QuickChargeEnchantment extends Enchantment {

    public QuickChargeEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.CROSSBOW, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 12 + (int0 - 1) * 20;
    }

    @Override
    public int getMaxCost(int int0) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}