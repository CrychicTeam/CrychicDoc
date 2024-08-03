package io.github.apace100.origins.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.jetbrains.annotations.NotNull;

public class WaterProtectionEnchantment extends Enchantment {

    public WaterProtectionEnchantment(Enchantment.Rarity weight, EnchantmentCategory type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMinCost(int level) {
        return 8 + level * 5;
    }

    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 8;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment other) {
        return other != this && (!(other instanceof ProtectionEnchantment) || ((ProtectionEnchantment) other).type == ProtectionEnchantment.Type.FALL) ? super.checkCompatibility(other) : false;
    }
}