package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class MultiShotEnchantment extends Enchantment {

    public MultiShotEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.CROSSBOW, equipmentSlot1);
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
        return super.checkCompatibility(enchantment0) && enchantment0 != Enchantments.PIERCING;
    }
}