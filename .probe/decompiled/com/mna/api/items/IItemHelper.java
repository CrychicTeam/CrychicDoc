package com.mna.api.items;

import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IItemHelper {

    Item CreateStaff(Item.Properties var1, float var2, float var3);

    Item CreateSpellBook(Item.Properties var1, @Nullable ResourceLocation var2, @Nullable ResourceLocation var3, boolean var4);

    Item CreateGrimoire(Item.Properties var1, @Nullable ResourceLocation var2, @Nullable ResourceLocation var3, @Nullable ResourceLocation var4, boolean var5);

    boolean AreTagsEqual(ItemStack var1, ItemStack var2);
}