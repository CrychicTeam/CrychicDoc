package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class LootBonusEnchantment extends Enchantment {

    protected LootBonusEnchantment(Enchantment.Rarity enchantmentRarity0, EnchantmentCategory enchantmentCategory1, EquipmentSlot... equipmentSlot2) {
        super(enchantmentRarity0, enchantmentCategory1, equipmentSlot2);
    }

    @Override
    public int getMinCost(int int0) {
        return 15 + (int0 - 1) * 9;
    }

    @Override
    public int getMaxCost(int int0) {
        return super.getMinCost(int0) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment0) {
        return super.checkCompatibility(enchantment0) && enchantment0 != Enchantments.SILK_TOUCH;
    }
}