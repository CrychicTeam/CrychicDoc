package dev.xkmc.l2complements.content.enchantment.core;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ImmuneEnchantment extends UnobtainableEnchantment {

    public ImmuneEnchantment(Enchantment.Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {
        super(rarity, category, slots);
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return !(enchantment instanceof ImmuneEnchantment);
    }

    @Override
    public Component getFullname(int lv) {
        MutableComponent component = Component.translatable(this.m_44704_());
        if (lv != 1 || this.m_6586_() != 1) {
            component.append(" ").append(Component.translatable("enchantment.level." + lv));
        }
        component.withStyle(ChatFormatting.GOLD);
        return component;
    }
}