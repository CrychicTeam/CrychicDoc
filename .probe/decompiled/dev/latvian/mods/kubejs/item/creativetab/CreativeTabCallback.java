package dev.latvian.mods.kubejs.item.creativetab;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface CreativeTabCallback {

    void addAfter(ItemStack var1, ItemStack[] var2, CreativeModeTab.TabVisibility var3);

    void addBefore(ItemStack var1, ItemStack[] var2, CreativeModeTab.TabVisibility var3);

    void remove(Ingredient var1, boolean var2, boolean var3);
}