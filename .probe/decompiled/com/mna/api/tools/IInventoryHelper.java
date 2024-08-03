package com.mna.api.tools;

import com.mna.api.items.DynamicItemFilter;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IInventoryHelper {

    boolean hasRoomFor(IItemHandlerModifiable var1, ItemStack var2);

    boolean mergeIntoInventory(IItemHandler var1, ItemStack var2);

    boolean mergeIntoInventory(IItemHandler var1, ItemStack var2, int var3);

    ItemStack mergeToPlayerInvPrioritizeOffhand(Player var1, ItemStack var2);

    ItemStack getFirstItemFromContainer(List<Item> var1, int var2, IItemHandler var3, Direction var4);

    ItemStack getFirstItemFromContainer(List<Item> var1, int var2, IItemHandler var3, Direction var4, boolean var5);

    ItemStack getFirstItemFromContainer(DynamicItemFilter var1, int var2, IItemHandler var3, Direction var4);

    ItemStack getFirstItemFromContainer(DynamicItemFilter var1, int var2, IItemHandler var3, Direction var4, boolean var5);

    ItemStack getFirstItemFromContainer(DynamicItemFilter var1, int var2, IItemHandler var3, Direction var4, boolean var5, boolean var6);

    boolean hasStackInInventory(DynamicItemFilter var1, IItemHandlerModifiable var2);

    boolean hasStackInInventory(ItemStack var1, boolean var2, boolean var3, IItemHandlerModifiable var4);

    boolean removeItemFromInventory(ItemStack var1, boolean var2, boolean var3, IItemHandlerModifiable var4);

    boolean hasStackInInventory(ItemStack var1, boolean var2, boolean var3, IItemHandler var4, Direction var5);

    boolean removeItemFromInventory(ItemStack var1, boolean var2, boolean var3, IItemHandler var4, Direction var5);

    boolean consumeAcrossInventories(ItemStack var1, boolean var2, boolean var3, boolean var4, List<Pair<IItemHandler, Direction>> var5);

    boolean consumeAcrossInventories(List<Item> var1, int var2, boolean var3, boolean var4, boolean var5, List<Pair<IItemHandler, Direction>> var6);

    int countItem(ItemStack var1, IItemHandler var2, Direction var3, boolean var4, boolean var5);

    ItemStack removeSingleItemFromInventory(ResourceLocation var1, Container var2);

    void redirectCaptureOrDrop(Player var1, Level var2, List<ItemStack> var3, boolean var4);

    Pair<Boolean, Boolean> getCaptureAndRedirect(Player var1);
}