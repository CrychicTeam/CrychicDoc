package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class ArrowKnockbackEnchantment extends Enchantment {

    public ArrowKnockbackEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.BOW, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 12 + (int0 - 1) * 20;
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + 25;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}