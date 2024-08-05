package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class UntouchingEnchantment extends Enchantment {

    protected UntouchingEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.DIGGER, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 15;
    }

    @Override
    public int getMaxCost(int int0) {
        return super.getMinCost(int0) + 50;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment0) {
        return super.checkCompatibility(enchantment0) && enchantment0 != Enchantments.BLOCK_FORTUNE;
    }
}