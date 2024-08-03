package com.mna.enchantments.staves;

import com.mna.api.spells.attributes.Attribute;
import com.mna.enchantments.base.ModifierEnchantment;
import com.mna.enchantments.framework.EnchantmentEnumExtender;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SpeedModifier extends ModifierEnchantment {

    public SpeedModifier() {
        super(Attribute.SPEED, 1, Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND });
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
}