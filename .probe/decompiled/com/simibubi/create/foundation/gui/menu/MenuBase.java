package com.simibubi.create.foundation.gui.menu;

import com.simibubi.create.foundation.utility.IInteractionChecker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class MenuBase<T> extends AbstractContainerMenu {

    public Player player;

    public Inventory playerInventory;

    public T contentHolder;

    protected MenuBase(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id);
        this.init(inv, this.createOnClient(extraData));
    }

    protected MenuBase(MenuType<?> type, int id, Inventory inv, T contentHolder) {
        super(type, id);
        this.init(inv, contentHolder);
    }

    protected void init(Inventory inv, T contentHolderIn) {
        this.player = inv.player;
        this.playerInventory = inv;
        this.contentHolder = contentHolderIn;
        this.initAndReadInventory(this.contentHolder);
        this.addSlots();
        this.m_38946_();
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract T createOnClient(FriendlyByteBuf var1);

    protected abstract void initAndReadInventory(T var1);

    protected abstract void addSlots();

    protected abstract void saveData(T var1);

    protected void addPlayerSlots(int x, int y) {
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            this.m_38897_(new Slot(this.playerInventory, hotbarSlot, x + hotbarSlot * 18, y + 58));
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.m_38897_(new Slot(this.playerInventory, col + row * 9 + 9, x + col * 18, y + row * 18));
            }
        }
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.saveData(this.contentHolder);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.contentHolder == null) {
            return false;
        } else {
            return this.contentHolder instanceof IInteractionChecker ? ((IInteractionChecker) this.contentHolder).canPlayerUse(player) : true;
        }
    }
}