package com.mna.enchantments;

import com.mna.enchantments.base.MAEnchantmentBase;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class Bouncing extends MAEnchantmentBase {

    public Bouncing(Enchantment.Rarity rarityIn) {
        super(rarityIn, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[] { EquipmentSlot.FEET });
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return this.f_44672_.canEnchant(pStack.getItem());
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return true;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}