package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public class TridentRiptideEnchantment extends Enchantment {

    public TridentRiptideEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.TRIDENT, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 10 + int0 * 7;
    }

    @Override
    public int getMaxCost(int int0) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment0) {
        return super.checkCompatibility(enchantment0) && enchantment0 != Enchantments.LOYALTY && enchantment0 != Enchantments.CHANNELING;
    }
}