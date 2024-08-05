package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class SweepingEdgeEnchantment extends Enchantment {

    public SweepingEdgeEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.WEAPON, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 5 + (int0 - 1) * 9;
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public static float getSweepingDamageRatio(int int0) {
        return 1.0F - 1.0F / (float) (int0 + 1);
    }
}