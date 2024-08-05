package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class ArrowPiercingEnchantment extends Enchantment {

    public ArrowPiercingEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.CROSSBOW, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 1 + (int0 - 1) * 10;
    }

    @Override
    public int getMaxCost(int int0) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment0) {
        return super.checkCompatibility(enchantment0) && enchantment0 != Enchantments.MULTISHOT;
    }
}