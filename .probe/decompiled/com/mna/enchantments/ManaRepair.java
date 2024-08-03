package com.mna.enchantments;

import com.mna.enchantments.base.MAEnchantmentBase;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ManaRepair extends MAEnchantmentBase {

    public ManaRepair() {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.isDamageableItem() && stack.isRepairable();
    }
}