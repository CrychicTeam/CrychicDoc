package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class TridentChannelingEnchantment extends Enchantment {

    public TridentChannelingEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.TRIDENT, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 25;
    }

    @Override
    public int getMaxCost(int int0) {
        return 50;
    }
}