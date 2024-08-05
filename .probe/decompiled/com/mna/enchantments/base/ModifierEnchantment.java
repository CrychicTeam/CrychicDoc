package com.mna.enchantments.base;

import com.mna.api.spells.attributes.Attribute;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class ModifierEnchantment extends MAEnchantmentBase {

    public final Attribute boost;

    public final int bonusStepsPerLevel;

    protected ModifierEnchantment(Attribute boost, int bonusStepsPerLevel, Enchantment.Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        this.boost = boost;
        this.bonusStepsPerLevel = bonusStepsPerLevel;
    }

    @Override
    protected boolean checkCompatibility(Enchantment pOther) {
        return !(pOther instanceof ModifierEnchantment);
    }
}