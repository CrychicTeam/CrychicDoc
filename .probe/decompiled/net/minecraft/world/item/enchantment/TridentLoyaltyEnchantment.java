package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class TridentLoyaltyEnchantment extends Enchantment {

    public TridentLoyaltyEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.TRIDENT, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 5 + int0 * 7;
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