package com.simibubi.create.content.logistics.filter;

import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractFilterMenu extends GhostItemMenu<ItemStack> {

    protected AbstractFilterMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    protected AbstractFilterMenu(MenuType<?> type, int id, Inventory inv, ItemStack contentHolder) {
        super(type, id, inv, contentHolder);
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId != this.playerInventory.selected || clickTypeIn == ClickType.THROW) {
            super.clicked(slotId, dragType, clickTypeIn, player);
        }
    }

    @Override
    protected boolean allowRepeats() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    protected ItemStack createOnClient(FriendlyByteBuf extraData) {
        return extraData.readItem();
    }

    protected abstract int getPlayerInventoryXOffset();

    protected abstract int getPlayerInventoryYOffset();

    protected abstract void addFilterSlots();

    @Override
    protected void addSlots() {
        this.addPlayerSlots(this.getPlayerInventoryXOffset(), this.getPlayerInventoryYOffset());
        this.addFilterSlots();
    }

    protected void saveData(ItemStack contentHolder) {
        contentHolder.getOrCreateTag().put("Items", this.ghostInventory.serializeNBT());
    }

    @Override
    public boolean stillValid(Player player) {
        return this.playerInventory.getSelected() == this.contentHolder;
    }
}