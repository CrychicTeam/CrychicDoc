package com.github.alexthe666.alexsmobs.enchantment;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class StraddleEnchantment extends Enchantment {

    protected StraddleEnchantment(Enchantment.Rarity r, EnchantmentCategory type, EquipmentSlot... types) {
        super(r, type, types);
    }

    @Override
    public int getMinCost(int i) {
        return 6 + (i + 1) * 6;
    }

    @Override
    public int getMaxCost(int i) {
        return super.getMinCost(i) + 10;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isTradeable() {
        return super.isTradeable() && AMConfig.straddleboardEnchants;
    }

    @Override
    public boolean isDiscoverable() {
        return super.isDiscoverable() && AMConfig.straddleboardEnchants;
    }

    public boolean isAllowedOnBooks() {
        return super.isAllowedOnBooks() && AMConfig.straddleboardEnchants;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return super.canApplyAtEnchantingTable(stack) && AMConfig.straddleboardEnchants;
    }
}