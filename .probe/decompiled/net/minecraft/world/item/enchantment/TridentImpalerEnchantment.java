package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;

public class TridentImpalerEnchantment extends Enchantment {

    public TridentImpalerEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.TRIDENT, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 1 + (int0 - 1) * 8;
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public float getDamageBonus(int int0, MobType mobType1) {
        return mobType1 == MobType.WATER ? (float) int0 * 2.5F : 0.0F;
    }
}