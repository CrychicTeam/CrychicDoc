package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class OxygenEnchantment extends Enchantment {

    public OxygenEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.ARMOR_HEAD, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 10 * int0;
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + 30;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}