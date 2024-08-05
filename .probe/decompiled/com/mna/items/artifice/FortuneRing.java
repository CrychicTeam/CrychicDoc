package com.mna.items.artifice;

import java.util.ArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class FortuneRing extends SpellModifierRing {

    private final ArrayList<Enchantment> allowedEnchantments = new ArrayList();

    public FortuneRing(Item.Properties properties, float maxMana) {
        super(properties, maxMana);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return this.getAllowedEnchantments().contains(enchantment);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    private ArrayList<Enchantment> getAllowedEnchantments() {
        if (this.allowedEnchantments.size() == 0) {
            this.allowedEnchantments.add(Enchantments.BLOCK_FORTUNE);
        }
        return this.allowedEnchantments;
    }
}