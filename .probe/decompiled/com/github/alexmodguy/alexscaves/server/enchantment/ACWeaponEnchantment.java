package com.github.alexmodguy.alexscaves.server.enchantment;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ACWeaponEnchantment extends Enchantment {

    private int levels;

    private int minXP;

    private String registryName;

    protected ACWeaponEnchantment(String name, Enchantment.Rarity rarity, EnchantmentCategory category, int levels, int minXP, EquipmentSlot... equipmentSlot) {
        super(rarity, category, equipmentSlot);
        this.levels = levels;
        this.minXP = minXP;
        this.registryName = name;
    }

    @Override
    public int getMinCost(int i) {
        return 1 + (i - 1) * this.minXP;
    }

    @Override
    public int getMaxCost(int i) {
        return super.getMinCost(i) + 30;
    }

    @Override
    public int getMaxLevel() {
        return this.levels;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return this != enchantment && ACEnchantmentRegistry.areCompatible(this, enchantment);
    }

    @Override
    public boolean isTradeable() {
        return AlexsCaves.COMMON_CONFIG.enchantmentsInLoot.get();
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }

    public boolean isAllowedOnBooks() {
        return AlexsCaves.COMMON_CONFIG.enchantmentsInLoot.get();
    }

    public String getName() {
        return this.registryName;
    }
}