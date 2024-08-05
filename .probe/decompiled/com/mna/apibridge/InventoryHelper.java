package com.mna.apibridge;

import com.mna.api.items.DynamicItemFilter;
import com.mna.api.tools.IInventoryHelper;
import com.mna.tools.InventoryUtilities;
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

public class InventoryHelper implements IInventoryHelper {

    @Override
    public boolean hasRoomFor(IItemHandlerModifiable inventory, ItemStack item) {
        return InventoryUtilities.hasRoomFor(inventory, item);
    }

    @Override
    public boolean mergeIntoInventory(IItemHandler handler, ItemStack toMerge) {
        return InventoryUtilities.mergeIntoInventory(handler, toMerge);
    }

    @Override
    public boolean mergeIntoInventory(IItemHandler handler, ItemStack toMerge, int quantity) {
        return InventoryUtilities.mergeIntoInventory(handler, toMerge, quantity);
    }

    @Override
    public ItemStack mergeToPlayerInvPrioritizeOffhand(Player player, ItemStack toMerge) {
        return InventoryUtilities.mergeToPlayerInvPrioritizeOffhand(player, toMerge);
    }

    @Override
    public ItemStack getFirstItemFromContainer(List<Item> items, int maxCount, IItemHandler handler, Direction side) {
        return InventoryUtilities.getFirstItemFromContainer(items, maxCount, handler, side);
    }

    @Override
    public ItemStack getFirstItemFromContainer(List<Item> items, int maxCount, IItemHandler handler, Direction side, boolean simulate) {
        return InventoryUtilities.getFirstItemFromContainer(items, maxCount, handler, side, simulate);
    }

    @Override
    public ItemStack getFirstItemFromContainer(DynamicItemFilter filter, int maxCount, IItemHandler handler, Direction side) {
        return InventoryUtilities.getFirstItemFromContainer(filter, maxCount, handler, side);
    }

    @Override
    public ItemStack getFirstItemFromContainer(DynamicItemFilter filter, int maxCount, IItemHandler handler, Direction side, boolean simulate) {
        return InventoryUtilities.getFirstItemFromContainer(filter, maxCount, handler, side, simulate);
    }

    @Override
    public ItemStack getFirstItemFromContainer(DynamicItemFilter filter, int maxCount, IItemHandler handler, Direction side, boolean randomFilterItem, boolean simulate) {
        return InventoryUtilities.getFirstItemFromContainer(filter, maxCount, handler, side, randomFilterItem, simulate);
    }

    @Override
    public boolean hasStackInInventory(DynamicItemFilter search, IItemHandlerModifiable inventory) {
        return InventoryUtilities.hasStackInInventory(search, inventory);
    }

    @Override
    public boolean hasStackInInventory(ItemStack search, boolean ignoreDurability, boolean compareNBT, IItemHandlerModifiable inventory) {
        return InventoryUtilities.hasStackInInventory(search, ignoreDurability, compareNBT, inventory);
    }

    @Override
    public boolean removeItemFromInventory(ItemStack search, boolean ignoreDurability, boolean compareNBT, IItemHandlerModifiable inventory) {
        return InventoryUtilities.removeItemFromInventory(search, ignoreDurability, compareNBT, inventory);
    }

    @Override
    public boolean hasStackInInventory(ItemStack search, boolean ignoreDurability, boolean compareNBT, IItemHandler inventory, Direction face) {
        return InventoryUtilities.hasStackInInventory(search, ignoreDurability, compareNBT, inventory, face);
    }

    @Override
    public boolean removeItemFromInventory(ItemStack search, boolean ignoreDurability, boolean compareNBT, IItemHandler inventory, Direction face) {
        return InventoryUtilities.removeItemFromInventory(search, ignoreDurability, compareNBT, inventory, face);
    }

    @Override
    public boolean consumeAcrossInventories(ItemStack search, boolean ignoreDurability, boolean compareNBT, boolean simulate, List<Pair<IItemHandler, Direction>> inventories) {
        return InventoryUtilities.consumeAcrossInventories(search, ignoreDurability, compareNBT, simulate, inventories);
    }

    @Override
    public boolean consumeAcrossInventories(List<Item> search, int count, boolean ignoreDurability, boolean compareNBT, boolean simulate, List<Pair<IItemHandler, Direction>> inventories) {
        return InventoryUtilities.consumeAcrossInventories(search, count, ignoreDurability, compareNBT, simulate, inventories);
    }

    @Override
    public int countItem(ItemStack search, IItemHandler inventory, Direction face, boolean compareNBT, boolean ignoreDurability) {
        return InventoryUtilities.countItem(search, inventory, face, compareNBT, ignoreDurability);
    }

    @Override
    public ItemStack removeSingleItemFromInventory(ResourceLocation search, Container inventory) {
        return InventoryUtilities.removeSingleItemFromInventory(search, inventory);
    }

    @Override
    public void redirectCaptureOrDrop(Player player, Level world, List<ItemStack> drops, boolean allowRedirect) {
        InventoryUtilities.redirectCaptureOrDrop(player, world, drops, allowRedirect);
    }

    @Override
    public Pair<Boolean, Boolean> getCaptureAndRedirect(Player player) {
        return InventoryUtilities.getCaptureAndRedirect(player);
    }
}