package dev.xkmc.l2archery.content.enchantment;

import dev.xkmc.l2complements.content.enchantment.core.UnobtainableEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class BaseBowEnchantment extends UnobtainableEnchantment implements IBowEnchantment {

    protected final int max;

    public BaseBowEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots, int max) {
        super(pRarity, pCategory, pApplicableSlots);
        this.max = max;
    }

    @Override
    public final int getMaxLevel() {
        return this.max;
    }
}