package com.mna.api.items;

import com.mna.api.recipes.IMARecipe;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

public interface ITieredItem<T extends Item> {

    int TIER_NOT_CHECKED = -1;

    int TIER_NO_RECIPE = -2;

    String TAG_HIDE_TIER_IN_TOOLTIP = "hideTier";

    void setCachedTier(int var1);

    int getCachedTier();

    default int getTier(Level world, ItemStack stack) {
        return EnchantmentHelper.getEnchantments(stack).size() > 0 ? this.resolveTier(world, stack) : this.getCachedTier();
    }

    default int resolveTier(Level world, ItemStack stack) {
        List<Recipe<?>> recipes = (List<Recipe<?>>) world.getRecipeManager().getRecipes().stream().filter(r -> {
            if (r == null) {
                return false;
            } else {
                ItemStack res = r.getResultItem(world.registryAccess());
                return res.getItem() == this;
            }
        }).collect(Collectors.toList());
        if (recipes.size() == 0) {
            return -2;
        } else {
            Map<Enchantment, Integer> itemEnchantments = EnchantmentHelper.getEnchantments(stack);
            if (itemEnchantments.size() == 1) {
                recipes = (List<Recipe<?>>) recipes.stream().filter(r -> {
                    Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(r.getResultItem(world.registryAccess()));
                    if (enchs.size() != 1) {
                        return false;
                    } else {
                        MutableBoolean match = new MutableBoolean(false);
                        itemEnchantments.entrySet().forEach(e -> enchs.entrySet().forEach(e2 -> {
                            if (e.getKey() == e2.getKey() && e.getValue() == e2.getValue()) {
                                match.setTrue();
                            }
                        }));
                        return match.getValue();
                    }
                }).collect(Collectors.toList());
            }
            MutableInt lowest = new MutableInt(-2);
            recipes.stream().sorted((a, b) -> {
                if (a instanceof IMARecipe aR && b instanceof IMARecipe bR) {
                    return aR.getTier() < bR.getTier() ? -1 : (aR.getTier() > bR.getTier() ? 1 : 0);
                }
                return -1;
            }).findFirst().ifPresent(r -> {
                if (r instanceof IMARecipe mR) {
                    lowest.setValue(mR.getTier());
                } else {
                    lowest.setValue(0);
                }
            });
            return lowest.getValue();
        }
    }
}