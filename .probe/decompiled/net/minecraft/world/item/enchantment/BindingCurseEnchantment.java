package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BindingCurseEnchantment extends Enchantment {

    public BindingCurseEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.WEARABLE, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 25;
    }

    @Override
    public int getMaxCost(int int0) {
        return 50;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack0) {
        return !itemStack0.is(Items.SHIELD) && super.canEnchant(itemStack0);
    }
}