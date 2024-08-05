package net.minecraft.world.item.enchantment;

import net.minecraft.util.random.WeightedEntry;

public class EnchantmentInstance extends WeightedEntry.IntrusiveBase {

    public final Enchantment enchantment;

    public final int level;

    public EnchantmentInstance(Enchantment enchantment0, int int1) {
        super(enchantment0.getRarity().getWeight());
        this.enchantment = enchantment0;
        this.level = int1;
    }
}