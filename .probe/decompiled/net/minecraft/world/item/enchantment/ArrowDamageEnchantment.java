package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class ArrowDamageEnchantment extends Enchantment {

    public ArrowDamageEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.BOW, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 1 + (int0 - 1) * 10;
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}