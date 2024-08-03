package com.almostreliable.morejs.features.enchantment;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.List;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class FilterAvailableEnchantmentsEventJS extends EventJS {

    private final List<EnchantmentInstance> enchantments;

    private final int powerLevel;

    private final ItemStack item;

    public FilterAvailableEnchantmentsEventJS(List<EnchantmentInstance> enchantments, int powerLevel, ItemStack item) {
        this.enchantments = enchantments;
        this.powerLevel = powerLevel;
        this.item = item;
    }

    public void printEnchantmentInstances() {
        this.enchantments.stream().sorted((ei1, ei2) -> {
            ResourceLocation key1 = BuiltInRegistries.ENCHANTMENT.getKey(ei1.enchantment);
            ResourceLocation key2 = BuiltInRegistries.ENCHANTMENT.getKey(ei2.enchantment);
            assert key1 != null;
            assert key2 != null;
            int c = key1.toString().compareTo(key2.toString());
            return c != 0 ? c : Integer.compare(ei1.level, ei2.level);
        }).forEach(ei -> ConsoleJS.SERVER.info(BuiltInRegistries.ENCHANTMENT.getKey(ei.enchantment) + " (Level: " + ei.level + ")"));
    }

    public List<EnchantmentInstance> getEnchantmentInstances() {
        return this.enchantments;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getPowerLevel() {
        return this.powerLevel;
    }

    public void addWithLevel(Enchantment enchantment, int level) {
        this.enchantments.add(new EnchantmentInstance(enchantment, level));
    }

    public void add(Enchantment... enchantments) {
        int pl = this.getPowerLevel();
        for (Enchantment enchantment : enchantments) {
            for (int el = enchantment.getMaxLevel(); el > enchantment.getMinLevel() - 1; el--) {
                if (enchantment.getMinCost(el) <= pl && pl <= enchantment.getMaxCost(el)) {
                    this.enchantments.add(new EnchantmentInstance(enchantment, el));
                    break;
                }
            }
        }
    }

    public void remove(Enchantment... enchantments) {
        Set<Enchantment> filter = Set.of(enchantments);
        this.enchantments.removeIf(ei -> filter.contains(ei.enchantment));
    }
}