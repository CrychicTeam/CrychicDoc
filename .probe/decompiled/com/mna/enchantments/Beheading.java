package com.mna.enchantments;

import com.mna.enchantments.base.MAEnchantmentBase;
import com.mna.enchantments.framework.EnchantmentEnumExtender;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class Beheading extends MAEnchantmentBase {

    public Beheading() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentEnumExtender.StaffOrWeapon(), new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND });
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return EnchantmentEnumExtender.StaffOrWeapon().canEnchant(stack.getItem());
    }
}