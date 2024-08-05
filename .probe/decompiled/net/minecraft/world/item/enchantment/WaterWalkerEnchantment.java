package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class WaterWalkerEnchantment extends Enchantment {

    public WaterWalkerEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.ARMOR_FEET, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return int0 * 10;
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment0) {
        return super.checkCompatibility(enchantment0) && enchantment0 != Enchantments.FROST_WALKER;
    }
}