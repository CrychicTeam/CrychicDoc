package com.mna.enchantments;

import com.mna.enchantments.base.MAEnchantmentBase;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class Soulbound extends MAEnchantmentBase {

    public Soulbound() {
        super(Enchantment.Rarity.COMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[0]);
    }

    @Override
    public boolean canEnchant(ItemStack p_92089_1_) {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}