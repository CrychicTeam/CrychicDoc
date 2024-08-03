package com.mna.enchantments.staves;

import com.mna.enchantments.base.MADamageEnchantmentBase;
import com.mna.enchantments.framework.EnchantmentEnumExtender;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

public class Bludgeoning extends MADamageEnchantmentBase {

    public Bludgeoning() {
        super(Enchantment.Rarity.COMMON, 0, new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND });
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return EnchantmentEnumExtender.Staves().canEnchant(stack.getItem());
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
    public float getDamageBonus(int pLevel, MobType pCreatureType) {
        return 1.0F + (float) Math.max(0, pLevel - 1) * 0.5F;
    }

    @Override
    public boolean checkCompatibility(Enchantment pEnch) {
        return !(pEnch instanceof DamageEnchantment);
    }
}