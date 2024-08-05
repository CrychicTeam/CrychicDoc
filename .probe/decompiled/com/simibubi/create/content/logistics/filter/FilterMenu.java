package com.simibubi.create.content.logistics.filter;

import com.simibubi.create.AllMenuTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FilterMenu extends AbstractFilterMenu {

    boolean respectNBT;

    boolean blacklist;

    public FilterMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public FilterMenu(MenuType<?> type, int id, Inventory inv, ItemStack stack) {
        super(type, id, inv, stack);
    }

    public static FilterMenu create(int id, Inventory inv, ItemStack stack) {
        return new FilterMenu((MenuType<?>) AllMenuTypes.FILTER.get(), id, inv, stack);
    }

    @Override
    protected int getPlayerInventoryXOffset() {
        return 38;
    }

    @Override
    protected int getPlayerInventoryYOffset() {
        return 121;
    }

    @Override
    protected void addFilterSlots() {
        int x = 23;
        int y = 22;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 9; col++) {
                this.m_38897_(new SlotItemHandler(this.ghostInventory, col + row * 9, x + col * 18, y + row * 18));
            }
        }
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return FilterItem.getFilterItems(this.contentHolder);
    }

    protected void initAndReadInventory(ItemStack filterItem) {
        super.initAndReadInventory(filterItem);
        CompoundTag tag = filterItem.getOrCreateTag();
        this.respectNBT = tag.getBoolean("RespectNBT");
        this.blacklist = tag.getBoolean("Blacklist");
    }

    @Override
    protected void saveData(ItemStack filterItem) {
        super.saveData(filterItem);
        CompoundTag tag = filterItem.getOrCreateTag();
        tag.putBoolean("RespectNBT", this.respectNBT);
        tag.putBoolean("Blacklist", this.blacklist);
        if (!this.respectNBT && !this.blacklist) {
            for (int i = 0; i < this.ghostInventory.getSlots(); i++) {
                if (!this.ghostInventory.getStackInSlot(i).isEmpty()) {
                    return;
                }
            }
            filterItem.setTag(null);
        }
    }
}