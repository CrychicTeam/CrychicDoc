package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class FireAspectEnchantment extends Enchantment {

    protected FireAspectEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.WEAPON, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 10 + 20 * (int0 - 1);
    }

    @Override
    public int getMaxCost(int int0) {
        return super.getMinCost(int0) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}