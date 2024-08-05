package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class ArrowInfiniteEnchantment extends Enchantment {

    public ArrowInfiniteEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.BOW, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 20;
    }

    @Override
    public int getMaxCost(int int0) {
        return 50;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment0) {
        return enchantment0 instanceof MendingEnchantment ? false : super.checkCompatibility(enchantment0);
    }
}