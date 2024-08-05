package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class WaterWorkerEnchantment extends Enchantment {

    public WaterWorkerEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.ARMOR_HEAD, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 1;
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + 40;
    }
}