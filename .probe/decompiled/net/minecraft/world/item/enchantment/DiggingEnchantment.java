package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DiggingEnchantment extends Enchantment {

    protected DiggingEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.DIGGER, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 1 + 10 * (int0 - 1);
    }

    @Override
    public int getMaxCost(int int0) {
        return super.getMinCost(int0) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack0) {
        return itemStack0.is(Items.SHEARS) ? true : super.canEnchant(itemStack0);
    }
}