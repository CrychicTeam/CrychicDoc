package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class MendingEnchantment extends Enchantment {

    public MendingEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.BREAKABLE, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return int0 * 25;
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + 50;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }
}