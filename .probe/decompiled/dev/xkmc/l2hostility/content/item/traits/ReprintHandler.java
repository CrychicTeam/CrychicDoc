package dev.xkmc.l2hostility.content.item.traits;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ReprintHandler {

    public static void reprint(ItemStack dst, ItemStack src) {
        if ((dst.isEnchanted() || dst.isEnchantable()) && src.isEnchanted()) {
            Map<Enchantment, Integer> selfEnch = dst.getAllEnchantments();
            Map<Enchantment, Integer> targetEnch = src.getAllEnchantments();
            Map<Enchantment, Integer> newEnch = new LinkedHashMap();
            for (Entry<Enchantment, Integer> pair : targetEnch.entrySet()) {
                Enchantment e = (Enchantment) pair.getKey();
                if (dst.canApplyAtEnchantingTable(e) && allow(newEnch, e)) {
                    int lv = (Integer) pair.getValue();
                    newEnch.compute(e, (k, v) -> v == null ? lv : Math.max(v, lv));
                }
            }
            for (Entry<Enchantment, Integer> pairx : selfEnch.entrySet()) {
                Enchantment e = (Enchantment) pairx.getKey();
                if (dst.canApplyAtEnchantingTable(e) && allow(newEnch, e)) {
                    int lv = (Integer) pairx.getValue();
                    newEnch.compute(e, (k, v) -> v == null ? lv : Math.max(v, lv));
                }
            }
            EnchantmentHelper.setEnchantments(newEnch, dst);
        }
    }

    private static boolean allow(Map<Enchantment, Integer> map, Enchantment ench) {
        if (map.containsKey(ench)) {
            return true;
        } else {
            for (Enchantment e : map.keySet()) {
                if (!e.isCompatibleWith(ench)) {
                    return false;
                }
            }
            return true;
        }
    }
}