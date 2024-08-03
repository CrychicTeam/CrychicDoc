package dev.xkmc.l2hostility.content.enchantments;

import dev.xkmc.l2complements.content.enchantment.core.UnobtainableEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class HostilityEnchantment extends UnobtainableEnchantment {

    private final int maxLv;

    public HostilityEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots, int maxLv) {
        super(pRarity, pCategory, pApplicableSlots);
        this.maxLv = maxLv;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLv;
    }
}