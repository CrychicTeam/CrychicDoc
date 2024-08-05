package dev.xkmc.l2complements.compat;

import java.util.Map;
import java.util.Map.Entry;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class LCEmiPlugin {

    public static String partSubType(ItemStack stack, UidContext context) {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        if (map.size() != 1) {
            return "";
        } else {
            Entry<Enchantment, Integer> e = (Entry<Enchantment, Integer>) map.entrySet().stream().findFirst().get();
            return ForgeRegistries.ENCHANTMENTS.getKey((Enchantment) e.getKey()).toString();
        }
    }
}