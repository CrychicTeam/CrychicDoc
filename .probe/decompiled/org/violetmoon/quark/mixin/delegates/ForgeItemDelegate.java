package org.violetmoon.quark.mixin.delegates;

import java.util.Map;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.violetmoon.quark.content.tweaks.module.GoldToolsHaveFortuneModule;

public class ForgeItemDelegate {

    public static int getEnchantmentLevel(int previous, ItemStack stack, Enchantment enchantment) {
        return GoldToolsHaveFortuneModule.getActualEnchantmentLevel(enchantment, stack, previous);
    }

    public static Map<Enchantment, Integer> getAllEnchantments(Map<Enchantment, Integer> previous, ItemStack stack) {
        GoldToolsHaveFortuneModule.addEnchantmentsIfMissing(stack, previous);
        return previous;
    }
}