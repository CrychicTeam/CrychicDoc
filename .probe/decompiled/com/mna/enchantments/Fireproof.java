package com.mna.enchantments;

import com.mna.enchantments.base.MAEnchantmentBase;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class Fireproof extends MAEnchantmentBase {

    public Fireproof() {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[0]);
    }

    @Override
    public boolean canEnchant(ItemStack p_92089_1_) {
        return true;
    }
}